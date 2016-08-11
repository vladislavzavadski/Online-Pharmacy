package by.training.online_pharmacy.dao.impl.database;


import by.training.online_pharmacy.dao.UserDAO;
import by.training.online_pharmacy.dao.connection_pool.ConnectionPool;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.dao.impl.database.util.exception.ParameterNotFoundException;
import by.training.online_pharmacy.domain.user.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladislav on 14.06.16.
 */
public class DatabaseUserDAO implements UserDAO {

    private static final String GET_USER_QUERY = "select US_LOGIN, us_first_name, us_second_name, us_image, us_mail, us_phone, us_group, us_gender, login_via, sd_description, sd_specialization from users left join staff_descriptions on sd_user_login=us_login WHERE  us_login=? and us_password=md5(?) and login_via=?;";
    private static final String GET_USER_BY_LOGIN_QUERY = "SELECT us_login, us_first_name, us_second_name, us_group, us_mail, us_phone, us_image, us_gender, login_via, sd_specialization, sd_description FROM users LEFT JOIN staff_descriptions ON users.us_login = staff_descriptions.sd_user_login WHERE us_login=? and login_via=?;";
    private static final String SEARCH_USER_BY_ROLE_QUERY = "SELECT us_login, us_first_name, us_second_name, us_mail, us_phone, us_image, us_gender, login_via, sd_specialization, sd_description FROM users LEFT JOIN staff_descriptions ON us_login=sd_user_login WHERE us_group=? LIMIT ?, ?;";
    private static final String SEARCH_USERS_QUERY = "select us_login, us_first_name, us_second_name, us_image, us_mail, us_phone, us_group, us_gender, login_via, sd_specialization, sd_description from users LEFT JOIN staff_descriptions on sd_user_login=us_login where us_first_name LIKE ? and us_second_name LIKE ? LIMIT ?, ?;";
    private static final String INSERT_USER_QUERY = "INSERT INTO users (us_login, us_password, us_first_name, us_second_name, us_group, us_image, us_mail, us_phone, login_via, us_gender) VALUES (?,md5(?),?,?,?,?,?,?,?,?)";
    private static final String INSERT_USER_IF_NOT_EXIST_QUERY = "INSERT IGNORE INTO users (us_login, us_first_name, us_second_name, us_group, us_mail, us_phone, login_via, us_gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE us_login=? and login_via=? and us_password=md5(?)";
    private static final String SEARCH_USER_BY_SPECIALIZATION_QUERY = "SELECT us_first_name, us_second_name, us_image, us_group, us_mail, us_phone, us_gender, login_via, sd_specialization, sd_description FROM users INNER JOIN staff_descriptions ON users.us_login = staff_descriptions.sd_user_login WHERE sd_specialization=? LIMIT ?, ?;";
    private static final String GET_COUNT_OF_USERS_WITH_LOGIN = "select count(us_login) login_count from users where us_login=?;";
    private static final String UPDATE_PERSONAL_INFORMATION_QUERY = "update users set us_first_name=?, us_second_name=?, us_gender=? where us_login=? and login_via=?;";
    private static final String UPDATE_PASSWORD = "update online_pharmacy.users set us_password=md5(?) where us_login=? and login_via=? and us_password=md5(?);";
    private static final String UPDATE_CONTACTS = "update users set us_mail=?, us_phone=? where us_login=? and login_via=?;";
    private static final String UPLOAD_PROFILE_IMAGE = "update users set us_image=? where us_login=? and login_via=?;";

    @Override
    public User userAuthentication(String login, String password, RegistrationType registrationType) throws DaoException {

        User user = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_USER_QUERY)){
            databaseOperation.setParameter(TableColumn.USER_LOGIN, login);
            databaseOperation.setParameter(TableColumn.USER_PASSWORD, password);
            databaseOperation.setParameter(TableColumn.LOGIN_VIA, registrationType.toString().toLowerCase());
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            List<User> result = resultSetToUser(resultSet);
            if(!result.isEmpty()){
                user = result.get(0);
            }
            return user;
        } catch (SQLException|ParameterNotFoundException|ConnectionPoolException e) {
            throw new DaoException("Cannot load user from database with login = \'"+login+"\' and password = \'"+password+"\'",e);
        }
        catch (Exception e) {
            throw new DaoException("Cannot load user from database with login = \'"+login+"\' and password = \'"+password+"\'",e);
        }
    }

    @Override
    public User userAuthentication(User user) throws DaoException {
        try(DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_USER_IF_NOT_EXIST_QUERY)) {
            databaseOperation.setParameter(TableColumn.USER_LOGIN, user.getLogin());
            databaseOperation.setParameter(TableColumn.USER_FIRST_NAME, user.getFirstName());
            databaseOperation.setParameter(TableColumn.USER_SECOND_NAME, user.getSecondName());
            databaseOperation.setParameter(TableColumn.USER_GROUP, user.getUserRole().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.USER_MAIL, user.getMail());
            databaseOperation.setParameter(TableColumn.USER_PHONE, user.getPhone());
            databaseOperation.setParameter(TableColumn.REGISTRATION_TYPE, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.USER_GENDER, user.getGender().toString().toLowerCase());
            int insertsCount = databaseOperation.invokeWriteOperation();
            if(insertsCount==1){
                return user;
            }else if(insertsCount==0) {
                databaseOperation.init(GET_USER_BY_LOGIN_QUERY);
                databaseOperation.setParameter(TableColumn.USER_LOGIN, user.getLogin());
                databaseOperation.setParameter(TableColumn.REGISTRATION_TYPE, user.getRegistrationType().toString().toLowerCase());
                ResultSet resultSet = databaseOperation.invokeReadOperation();
                return resultSetToUser(resultSet).get(0);
            }
        } catch (ParameterNotFoundException | SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not authenticate new user", e);
        } catch (Exception e) {
            throw new DaoException("Can not authenticate new user", e);
        }
        return null;
    }

    @Override
    public User getUserByLogin(String login, RegistrationType registrationType) throws DaoException {
        User user = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_USER_BY_LOGIN_QUERY)){
            databaseOperation.setParameter(TableColumn.USER_LOGIN, login);
            databaseOperation.setParameter(TableColumn.REGISTRATION_TYPE, registrationType.toString().toLowerCase());
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            List<User> result = resultSetToUser(resultSet);
            if(!result.isEmpty()){
                user = result.get(0);
            }
            return user;
        } catch (SQLException | ConnectionPoolException | ParameterNotFoundException e) {
            throw new DaoException("Can not get user with login = \'"+login+"\'", e);
        }  catch (Exception e) {
            throw new DaoException("Can not get user with login = \'"+login+"\'", e);
        }

    }

    @Override
    public List<User> searchUsersByRole(UserRole userRole, int limit, int startFrom) throws DaoException {

        List<User> users;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(SEARCH_USER_BY_ROLE_QUERY)){
            databaseOperation.setParameter(TableColumn.USER_GROUP, userRole.toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.LIMIT, 1, limit);
            databaseOperation.setParameter(TableColumn.LIMIT, 2, startFrom);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            users = resultSetToUser(resultSet);
            return users;
        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not search user with role \'"+userRole+"\'", e);
        }catch (Exception e) {
            throw new DaoException("Can not search user with role \'"+userRole+"\'", e);
        }

    }

    @Override
    public List<User> searchUsersByName(String firstName, String secondName, int limit, int startFrom) throws DaoException {

        List<User> users;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(SEARCH_USERS_QUERY)){
            databaseOperation.setParameter(TableColumn.USER_FIRST_NAME, "%"+firstName+"%");
            databaseOperation.setParameter(TableColumn.USER_SECOND_NAME, "%"+secondName+"%");
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            users = resultSetToUser(resultSet);
            return users;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not search users in database", e);
        } catch (Exception e) {
            throw new DaoException("Can not search users in database", e);
        }
    }

    @Override
    public void insertUser(User user) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_USER_QUERY)){
            databaseOperation.setParameter(TableColumn.USER_LOGIN, user.getLogin());
            databaseOperation.setParameter(TableColumn.USER_PASSWORD, user.getPassword());
            databaseOperation.setParameter(TableColumn.USER_FIRST_NAME, user.getFirstName());
            databaseOperation.setParameter(TableColumn.USER_SECOND_NAME, user.getSecondName());
            databaseOperation.setParameter(TableColumn.USER_GROUP, user.getUserRole().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.USER_IMAGE, user.getPathToImage());
            databaseOperation.setParameter(TableColumn.USER_MAIL, user.getMail());
            databaseOperation.setParameter(TableColumn.USER_PHONE, user.getPhone());
            databaseOperation.setParameter(TableColumn.REGISTRATION_TYPE, user.getRegistrationType());
            databaseOperation.setParameter(TableColumn.USER_GENDER, user.getGender());
            databaseOperation.invokeWriteOperation();

        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not insert user to database", e);
        } catch (Exception e) {
            throw new DaoException("Can not insert user to database", e);
        }
    }

    @Override
    public int deleteUser(User user) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(DELETE_USER_QUERY)){
            databaseOperation.setParameter(TableColumn.USER_LOGIN, user.getLogin());
            databaseOperation.setParameter(TableColumn.REGISTRATION_TYPE, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.USER_PASSWORD, user.getPassword());
            return databaseOperation.invokeWriteOperation();
        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not delete user with login \'"+user.getLogin()+"\'", e);
        } catch (Exception e) {
            throw new DaoException("Can not delete user with login \'"+user.getLogin()+"\'", e);
        }
    }

    @Override
    public List<User> getUsersBySpecialization(String specialization, int limit, int startFrom) throws DaoException {
        List<User> users;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(SEARCH_USER_BY_SPECIALIZATION_QUERY)){
            databaseOperation.setParameter(TableColumn.USER_SPECIALIZATION, specialization);
            databaseOperation.setParameter(TableColumn.LIMIT, 1, limit);
            databaseOperation.setParameter(TableColumn.LIMIT, 2, startFrom);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            users = resultSetToUser(resultSet);
            return users;
        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not search users with specialization = \'"+specialization+"\'", e);
        } catch (Exception e) {
            throw new DaoException("Can not search users with specialization = \'"+specialization+"\'", e);
        }
    }

    @Override
    public boolean isLoginUsed(String login) throws DaoException {
        try(DatabaseOperation databaseOperation = new DatabaseOperation(GET_COUNT_OF_USERS_WITH_LOGIN)) {
            databaseOperation.setParameter(TableColumn.USER_LOGIN, login);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            resultSet.next();
            int usersCount = resultSet.getInt(TableColumn.LOGIN_COUNT);
            return usersCount>=1;
        } catch (SQLException | ConnectionPoolException | ParameterNotFoundException e) {
            throw new DaoException("Can not get count of users", e);
        } catch (Exception e) {
            throw new DaoException("Can not get count of users", e);
        }
    }

    @Override
    public void updatePersonalInformation(User user) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(UPDATE_PERSONAL_INFORMATION_QUERY)){
            databaseOperation.setParameter(TableColumn.USER_LOGIN, user.getLogin());
            databaseOperation.setParameter(TableColumn.USER_FIRST_NAME, user.getFirstName());
            databaseOperation.setParameter(TableColumn.USER_SECOND_NAME, user.getSecondName());
            databaseOperation.setParameter(TableColumn.USER_GENDER, user.getGender().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.REGISTRATION_TYPE, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.invokeWriteOperation();
        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not update users personal information", e);
        } catch (Exception e) {
            throw new DaoException("Can not update users personal information", e);
        }

    }

    @Override
    public int updateUsersPassword(User user, String newPassword) throws DaoException {
        try(DatabaseOperation databaseOperation = new DatabaseOperation(UPDATE_PASSWORD)) {
            databaseOperation.setParameter(TableColumn.USER_PASSWORD, newPassword);
            databaseOperation.setParameter(TableColumn.USER_LOGIN, user.getLogin());
            databaseOperation.setParameter(TableColumn.REGISTRATION_TYPE, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.USER_PASSWORD+1, user.getPassword());
            return databaseOperation.invokeWriteOperation();
        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not update users password", e);
        } catch (Exception e) {
            throw new DaoException("Can not update users password", e);
        }

    }

    @Override
    public void updateUsersContacts(User user) throws DaoException {

        try(DatabaseOperation databaseOperation = new DatabaseOperation(UPDATE_CONTACTS)){
            databaseOperation.setParameter(TableColumn.USER_LOGIN, user.getLogin());
            databaseOperation.setParameter(TableColumn.REGISTRATION_TYPE, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.USER_MAIL, user.getMail());
            databaseOperation.setParameter(TableColumn.USER_PHONE, user.getPhone());
            databaseOperation.invokeWriteOperation();
        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not update users contacts", e);
        } catch (Exception e) {
            throw new DaoException("Can not update users contacts", e);
        }
    }

    @Override
    public void uploadProfileImage(User user) throws DaoException {
        try(DatabaseOperation databaseOperation = new DatabaseOperation(UPLOAD_PROFILE_IMAGE)) {
            databaseOperation.setParameter(TableColumn.USER_LOGIN, user.getLogin());
            databaseOperation.setParameter(TableColumn.REGISTRATION_TYPE, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.USER_IMAGE, user.getPathToImage());
            databaseOperation.invokeWriteOperation();
        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not upload profile image", e);
        } catch (Exception e) {
            throw new DaoException("Can not upload profile image", e);
        }
    }

    private List<User> resultSetToUser(ResultSet resultSet) throws SQLException {
        List<User> result = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            UserDescription userDescription = new UserDescription();
            user.setUserDescription(userDescription);
            user.setLogin(resultSet.getString(TableColumn.USER_LOGIN));
            user.setUserRole(UserRole.valueOf(resultSet.getString(TableColumn.USER_GROUP).toUpperCase()));
            user.setFirstName(resultSet.getString(TableColumn.USER_FIRST_NAME));
            user.setSecondName(resultSet.getString(TableColumn.USER_SECOND_NAME));
            user.setMail(resultSet.getString(TableColumn.USER_MAIL));
            user.setPhone(resultSet.getString(TableColumn.USER_PHONE));
            user.setPathToImage((resultSet.getString(TableColumn.USER_IMAGE)));
            user.setRegistrationType(RegistrationType.valueOf(resultSet.getString(TableColumn.LOGIN_VIA).toUpperCase()));
            user.setGender(Gender.valueOf(resultSet.getString(TableColumn.USER_GENDER).toUpperCase()));
            userDescription.setDescription(resultSet.getString(TableColumn.USER_DESCRIPTION));
            userDescription.setSpecialization(resultSet.getString(TableColumn.USER_SPECIALIZATION));
            result.add(user);
        }
        return result;
    }
}
