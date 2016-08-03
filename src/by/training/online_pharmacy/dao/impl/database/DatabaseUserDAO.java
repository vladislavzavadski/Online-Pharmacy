package by.training.online_pharmacy.dao.impl.database;


import by.training.online_pharmacy.dao.UserDAO;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
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

    private static final String GET_USER_QUERY = "select us_login, us_first_name, us_second_name, us_image, us_mail, us_phone, us_group, us_gender, login_via, sd_description, sd_specialization from users left join staff_descriptions on sd_user_login=us_login WHERE  us_login=? and us_password=md5(?) and login_via=?;";
    private static final String GET_USER_BY_LOGIN_QUERY = "SELECT us_login, us_first_name, us_second_name, us_group, us_mail, us_phone, us_image, us_gender, login_via, sd_specialization, sd_description FROM users LEFT JOIN staff_descriptions ON users.us_login = staff_descriptions.sd_user_login WHERE us_login=?;";
    private static final String SEARCH_USER_BY_ROLE_QUERY = "SELECT us_login, us_first_name, us_second_name, us_mail, us_phone, us_image, us_gender, login_via, sd_specialization, sd_description FROM users LEFT JOIN staff_descriptions ON us_login=sd_user_login WHERE us_group=? LIMIT ?, ?;";
    private static final String SEARCH_USERS_QUERY = "select us_login, us_first_name, us_second_name, us_image, us_mail, us_phone, us_group, us_gender, login_via, sd_specialization, sd_description from users LEFT JOIN staff_descriptions on sd_user_login=us_login where us_first_name LIKE ? and us_second_name LIKE ? LIMIT ?, ?;";
    private static final String INSERT_USER_QUERY = "INSERT INTO users (us_login, us_password, us_first_name, us_second_name, us_group, us_image, us_mail, us_phone, login_via, us_gender) VALUES (?,md5(?),?,?,?,?,?,?,?,?)";
    private static final String INSERT_USER_IF_NOT_EXIST_QUERY = "INSERT INTO users (us_login, us_first_name, us_second_name, us_group, us_mail, us_phone, login_via, us_gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON duplicate key update us_login=us_login;";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET us_password=md5(?), us_first_name=?, us_second_name=?, us_image=?, us_mail=?, us_phone=?, us_gender=? WHERE us_login=?;";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE us_login=?";
    private static final String SEARCH_USER_BY_SPECIALIZATION_QUERY = "SELECT us_first_name, us_second_name, us_image, us_group, us_mail, us_phone, us_gender, login_via, sd_specialization, sd_description FROM users INNER JOIN staff_descriptions ON users.us_login = staff_descriptions.sd_user_login WHERE sd_specialization=? LIMIT ?, ?;";
    private static final String GET_COUNT_OF_USERS_WITH_LOGIN = "select count(us_login) login_count from users where us_login=?;";
    private static final String UPDATE_PERSONAL_INFORMATION_QUERY = "update users set us_first_name=?, us_second_name=?, us_gender=? where us_login=? and login_via=?;";
    private static final String UPDATE_PASSWORD = "update online_pharmacy.users set us_password=md5(?) where us_login=? and login_via=? and us_password=md5(?);";
    private static final String UPDATE_CONTACTS = "update users set us_mail=?, us_phone=? where us_login=? and login_via=?;";
    private static final String UPLOAD_PROFILE_IMAGE = "update users set us_image=? where us_login=? and login_via=?;";

    @Override
    public User userAuthentication(String login, String password, RegistrationType registrationType) throws DaoException {

        User user = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_USER_QUERY, login, password, registrationType.toString().toLowerCase())){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            List<User> result = resultSetToUser(resultSet);
            if(!result.isEmpty()){
                user = result.get(0);
            }
            return user;
        } catch (Exception e) {
            throw new DaoException("Cannot load user from database with login = \'"+login+"\' and password = \'"+password+"\'",e);
        }

    }

    @Override
    public User userAuthentication(User user) throws DaoException {
        try(DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_USER_IF_NOT_EXIST_QUERY, user.getLogin(), user.getFirstName(), user.getSecondName(), user.getUserRole().toString().toLowerCase(), user.getMail(), user.getPhone(), user.getRegistrationType().toString().toLowerCase(), user.getGender().toString().toLowerCase())) {
            int insertsCount = databaseOperation.invokeWriteOperation();
            if(insertsCount==1){
                return user;
            }else if(insertsCount==0) {
                databaseOperation.init(GET_USER_BY_LOGIN_QUERY, user.getLogin());
                ResultSet resultSet = databaseOperation.invokeReadOperation();
                return resultSetToUser(resultSet).get(0);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return null;
    }

    @Override
    public User getUserByLogin(String login) throws DaoException {
        User user = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_USER_BY_LOGIN_QUERY, login)){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            List<User> result = resultSetToUser(resultSet);
            if(!result.isEmpty()){
                user = result.get(0);
            }
            return user;
        } catch (Exception e) {
            throw new DaoException("Can not get user with login = \'"+login+"\'", e);
        }

    }

    @Override
    public List<User> searchUsersByRole(UserRole userRole, int limit, int startFrom) throws DaoException {

        List<User> users;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(SEARCH_USER_BY_ROLE_QUERY, userRole.toString().toLowerCase(), limit, startFrom)){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            users = resultSetToUser(resultSet);
            return users;
        } catch (Exception e) {
            throw new DaoException("Can not search user with role \'"+userRole+"\'", e);
        }

    }

    @Override
    public List<User> searchUsersByName(String firstName, String secondName, int limit, int startFrom) throws DaoException {

        List<User> users;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(SEARCH_USERS_QUERY, '%'+firstName+'%', '%'+secondName+'%', limit, startFrom)){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            users = resultSetToUser(resultSet);
            return users;
        } catch (Exception ex) {
            throw new DaoException("Can not search users in database", ex);
        }
    }

    @Override
    public void insertUser(User user) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_USER_QUERY, user.getLogin(), user.getPassword(),
                user.getFirstName(), user.getSecondName(), user.getUserRole().toString().toLowerCase(),
                user.getUserImage(), user.getMail(), user.getPhone(), user.getRegistrationType().toString().toLowerCase(),
                user.getGender().toString().toLowerCase())){
            databaseOperation.invokeWriteOperation();

        } catch (Exception e) {
            throw new DaoException("Can not insert user to database", e);
        }
    }

    @Override
    public void updateUser(User user) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(UPDATE_USER_QUERY, user.getPassword(),
                user.getFirstName(), user.getSecondName(),
                user.getUserImage(), user.getMail(), user.getPhone(), user.getLogin())){
            databaseOperation.invokeWriteOperation();
        } catch (Exception e) {
            throw new DaoException("Cannot update user", e);
        }
    }

    @Override
    public void deleteUser(String login) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(DELETE_USER_QUERY, login)){
            databaseOperation.invokeWriteOperation();
        } catch (Exception e) {
            throw new DaoException("Can not delete user with login \'"+login+"\'", e);
        }
    }

    @Override
    public List<User> getUsersBySpecialization(String specialization, int limit, int startFrom) throws DaoException {

        List<User> users;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(SEARCH_USER_BY_SPECIALIZATION_QUERY, specialization, limit, startFrom)){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            users = resultSetToUser(resultSet);
            return users;
        } catch (Exception e) {
            throw new DaoException("Can not search users with specialization = \'"+specialization+"\'", e);
        }
    }

    @Override
    public boolean isLoginUsed(String login) throws DaoException {
        try(DatabaseOperation databaseOperation = new DatabaseOperation(GET_COUNT_OF_USERS_WITH_LOGIN, login)) {
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            resultSet.next();
            int usersCount = resultSet.getInt(TableColumn.LOGIN_COUNT);
            return usersCount>=1;
        } catch (Exception e) {
            throw new DaoException("Can not get count of users", e);
        }
    }

    @Override
    public void updatePersonalInformation(User user) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(UPDATE_PERSONAL_INFORMATION_QUERY, user.getFirstName(), user.getSecondName(), user.getGender().toString().toLowerCase(), user.getLogin(), user.getRegistrationType().toString().toLowerCase())){
            databaseOperation.invokeWriteOperation();
        } catch (Exception e) {
            throw new DaoException("Can not update users personal information", e);
        }
    }

    @Override
    public int updateUsersPassword(User user, String newPassword) throws DaoException {
        try(DatabaseOperation databaseOperation = new DatabaseOperation(UPDATE_PASSWORD, newPassword, user.getLogin(), user.getRegistrationType().toString().toLowerCase(), user.getPassword())) {
            return databaseOperation.invokeWriteOperation();
        }
        catch (Exception e) {
            throw new DaoException("Can not update users password", e);
        }
    }

    @Override
    public void updateUsersContacts(User user) throws DaoException {

        try(DatabaseOperation databaseOperation = new DatabaseOperation(UPDATE_CONTACTS, user.getMail(), user.getPhone(), user.getLogin(), user.getRegistrationType().toString().toLowerCase())){
            databaseOperation.invokeWriteOperation();
        }catch (Exception e) {
            throw new DaoException("Can not update users contacts", e);
        }
    }

    @Override
    public void uploadProfileImage(User user) throws DaoException {
        try(DatabaseOperation databaseOperation = new DatabaseOperation(UPLOAD_PROFILE_IMAGE, user.getUserImage(), user.getLogin(), user.getRegistrationType().toString().toLowerCase())) {
            databaseOperation.invokeWriteOperation();
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
            user.setUserImage(resultSet.getBytes(TableColumn.USER_IMAGE));
            user.setRegistrationType(RegistrationType.valueOf(resultSet.getString(TableColumn.LOGIN_VIA).toUpperCase()));
            user.setGender(Gender.valueOf(resultSet.getString(TableColumn.USER_GENDER).toUpperCase()));
            userDescription.setDescription(resultSet.getString(TableColumn.USER_DESCRIPTION));
            userDescription.setSpecialization(resultSet.getString(TableColumn.USER_SPECIALIZATION));
            result.add(user);
        }
        return result;
    }
}
