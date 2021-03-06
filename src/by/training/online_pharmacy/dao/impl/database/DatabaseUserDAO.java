package by.training.online_pharmacy.dao.impl.database;


import by.training.online_pharmacy.dao.UserDAO;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.dao.exception.EntityNotFoundException;
import by.training.online_pharmacy.dao.exception.MultipleRecordsException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.dao.impl.database.util.exception.ParameterNotFoundException;
import by.training.online_pharmacy.domain.user.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static by.training.online_pharmacy.dao.impl.database.Param.AND;
import static by.training.online_pharmacy.dao.impl.database.Param.OR;
import static by.training.online_pharmacy.dao.impl.database.Param.PER_CENT;

/**
 * Created by vladislav on 14.06.16.
 */
public class DatabaseUserDAO implements UserDAO {

    private static final String GET_USER_NATIVE_QUERY = "select us_balance, US_LOGIN, us_first_name, us_second_name, us_image, us_mail, us_phone, us_group, us_gender, login_via, sd_description, sd_specialization from users left join staff_descriptions on sd_user_login=us_login WHERE  us_login=? and us_password=md5(?) and login_via=?;";
    private static final String GET_USER_QUERY = "select us_balance, US_LOGIN, us_first_name, us_second_name, us_image, us_mail, us_phone, us_group, us_gender, login_via, sd_description, sd_specialization from users left join staff_descriptions on sd_user_login=us_login WHERE  us_login=? and login_via=?;";
    private static final String INSERT_USER_QUERY = "INSERT INTO users (us_login, us_password, us_first_name, us_second_name, us_group, us_image, us_mail, us_phone, login_via, us_gender) VALUES (?,md5(?),?,?,?,?,?,?,?,?)";
    private static final String GET_COUNT_OF_USERS_WITH_LOGIN = "select count(us_login) login_count from users where us_login=?;";
    private static final String UPDATE_PERSONAL_INFORMATION_QUERY = "update users set us_first_name=?, us_second_name=?, us_gender=? where us_login=? and login_via=?;";
    private static final String UPDATE_PASSWORD = "update users set us_password=md5(?) where us_login=? and login_via=?;";
    private static final String UPDATE_CONTACTS = "update users set us_mail=?, us_phone=? where us_login=? and login_via=?;";
    private static final String UPLOAD_PROFILE_IMAGE = "update users set us_image=? where us_login=? and login_via=?;";
    private static final String GET_DOCTORS_QUERY_PREFIX= "select us_login, login_via, us_first_name, us_second_name, us_image, sd_specialization from users inner join staff_descriptions on us_login=sd_user_login and login_via=sd_login_via where us_group='doctor' ";
    private static final String SPECIALIZATION = " and sd_specialization=? ";
    private static final String GET_DOCTORS_QUERY_TAIL = " order by us_second_name limit ?, ?;";

    private static final String GET_DOCTORS_DETAILS = "select us_group, us_balance, us_login, login_via, us_first_name,  us_second_name, us_image, us_mail, us_phone, us_gender, sd_specialization, sd_description from users left join staff_descriptions on us_login=sd_user_login and login_via=sd_login_via where us_login=? and login_via=?";
    private static final String SEARCH_DOCTOR_BY_NAME_QUERY = "select us_login, login_via, us_first_name, us_second_name, us_image, sd_specialization from users inner join staff_descriptions on us_login=sd_user_login and login_via=sd_login_via where us_group='doctor' and (";
    private static final String GET_IMAGE_QUERY = "select us_image from users where us_login=? and login_via=?;";
    private static final String TEMPLATE = " (us_first_name like ? and us_second_name like ?) ";
    private static final String QUERY_TAIL = ") order by us_second_name limit ?, ?";
    private static final String WITHDRAW_MONEY_QUERY = "update users set us_balance=us_balance-? where us_login=? and login_via=?;";
    private static final String ADD_MONEY_TO_BALANCE_QUERY = "update users set us_balance=us_balance+? where us_login=? and login_via=?";
    private static final String GET_USER_MAIL_BY_SECRET_WORD = "select us_mail from users inner join secret_word on us_login=sw_user_login and login_via=sw_login_via where us_login=? and login_via='native' and sw_response=md5(?);";


    @Override
    public String getUserMailBySecretWord(SecretWord secretWord) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_USER_MAIL_BY_SECRET_WORD);){

            databaseOperation.setParameter(1, secretWord.getUser().getLogin());
            databaseOperation.setParameter(2, secretWord.getResponse());

            ResultSet resultSet = databaseOperation.invokeReadOperation();

            if(resultSet.next()){
                return resultSet.getString(TableColumn.USER_MAIL);
            }

            return null;
        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Can not get user email by secret word");
        }
    }

    @Override
    public void addMoneyToBalance(User user, float payment) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(ADD_MONEY_TO_BALANCE_QUERY);){

            databaseOperation.setParameter(1, payment);
            databaseOperation.setParameter(2, user.getLogin());
            databaseOperation.setParameter(3, user.getRegistrationType().toString().toLowerCase());

            if(databaseOperation.invokeWriteOperation()==0){
                throw new EntityNotFoundException("User="+user+" was not found");
            }

        }
         catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Can not add money to balance", e);
        }
    }



    @Override
    public InputStream getProfileImage(User user) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_IMAGE_QUERY)){
            databaseOperation.setParameter(TableColumn.USER_LOGIN, user.getLogin());
            databaseOperation.setParameter(TableColumn.REGISTRATION_TYPE, user.getRegistrationType().toString().toLowerCase());

            ResultSet resultSet = databaseOperation.invokeReadOperation();

            if(resultSet.next()){
                String pathToFile = resultSet.getString(TableColumn.USER_IMAGE);

                if(pathToFile==null){
                    return null;
                }

                return new FileInputStream(pathToFile);
            }

            return null;
        } catch (SQLException | ConnectionPoolException | FileNotFoundException | ParameterNotFoundException e) {
            throw new DaoException("Can not load image from database for user="+user, e);
        }
    }

    @Override
    public User getUserDetails(String userLogin, RegistrationType registrationType) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_DOCTORS_DETAILS)){
            databaseOperation.setParameter(TableColumn.USER_LOGIN, userLogin);
            databaseOperation.setParameter(TableColumn.LOGIN_VIA, registrationType.toString().toLowerCase());

            ResultSet resultSet = databaseOperation.invokeReadOperation();
            List<User> result = resultSetToUser(resultSet);

            if(result.size()>1){
                throw new MultipleRecordsException("The are more then one user searched");
            }

            if(!result.isEmpty()){
                return result.get(0);
            }

            return null;
        } catch (SQLException | ConnectionPoolException | ParameterNotFoundException e) {
            throw new DaoException("Can not load user details from database", e);
        }
    }

    @Override
    public List<User> searchDoctors(String[] criteria, int limit, int startFrom) throws DaoException {
        List<String> items;

        /*Идёт подсчет количества пар слов в введенном запросе*/
        int pairNumber = criteria.length != 1 ? (int)((double)criteria.length/2) * (criteria.length-1) * 2 : 1;

        items = new ArrayList<>(Collections.nCopies(pairNumber, TEMPLATE.replaceAll(AND, OR)));

        try (DatabaseOperation databaseOperation = new DatabaseOperation(SEARCH_DOCTOR_BY_NAME_QUERY+String.join(OR, items)+QUERY_TAIL)){
            int paramNumber = 1;

            if(pairNumber==1){
                databaseOperation.setParameter(1, Param.PER_CENT+criteria[0]+Param.PER_CENT);
                databaseOperation.setParameter(2, Param.PER_CENT+criteria[0]+Param.PER_CENT);
            }

            for(int i=0; i<criteria.length-1; i++){

                for (int j=i+1; j<criteria.length; j++){
                    databaseOperation.setParameter(paramNumber, Param.PER_CENT+criteria[i]+Param.PER_CENT);
                    databaseOperation.setParameter(paramNumber+1, Param.PER_CENT+criteria[j]+Param.PER_CENT);
                    databaseOperation.setParameter(paramNumber+2, Param.PER_CENT+criteria[j]+Param.PER_CENT);
                    databaseOperation.setParameter(paramNumber+3, PER_CENT+criteria[i]+PER_CENT);
                    paramNumber+=4;
                }

            }

            databaseOperation.setParameter(TableColumn.LIMIT, 1, startFrom);
            databaseOperation.setParameter(TableColumn.LIMIT, 2, limit);

            ResultSet resultSet = databaseOperation.invokeReadOperation();

            return resultSetToUsersOnSearch(resultSet);
        } catch (SQLException | ConnectionPoolException | ParameterNotFoundException e) {
            throw new DaoException("Can not search doctors by name", e);
        }
    }

    @Override
    public User userAuthentication(String login, RegistrationType registrationType) throws DaoException {


        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_USER_QUERY)){

            databaseOperation.setParameter(TableColumn.USER_LOGIN, login);
            databaseOperation.setParameter(TableColumn.REGISTRATION_TYPE, registrationType.toString().toLowerCase());

            ResultSet resultSet = databaseOperation.invokeReadOperation();
            List<User> result = resultSetToUser(resultSet);

            if(result.size()>1){
                throw new MultipleRecordsException("The are more then one user searched");
            }

            if(!result.isEmpty()){
                return result.get(0);
            }

            return null;
        } catch (SQLException | ConnectionPoolException | ParameterNotFoundException e) {
            throw new DaoException("Cannot load user from database with login = \'"+login+"\' and register type = \'"+registrationType+"\'",e);
        }
    }


    @Override
    public User userAuthentication(String login, String password, RegistrationType registrationType) throws DaoException {

        User user = null;

        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_USER_NATIVE_QUERY)){

            databaseOperation.setParameter(TableColumn.USER_LOGIN, login);
            databaseOperation.setParameter(TableColumn.USER_PASSWORD, password);
            databaseOperation.setParameter(TableColumn.LOGIN_VIA, registrationType.toString().toLowerCase());

            ResultSet resultSet = databaseOperation.invokeReadOperation();

            List<User> result = resultSetToUser(resultSet);

            if(result.size()>1){
                throw new MultipleRecordsException("The are more then one user searched");
            }

            if(!result.isEmpty()){
                user = result.get(0);
            }

            return user;

        } catch (SQLException|ParameterNotFoundException|ConnectionPoolException e) {
            throw new DaoException("Cannot load user from database with login = \'"+login+"\' and password = \'"+password+"\'",e);
        }
    }

    @Override
    public List<User> searchDoctors(UserDescription userDescription, int limit, int startFrom) throws DaoException {
        List<User> result = new ArrayList<>();
        StringBuilder query = new StringBuilder(GET_DOCTORS_QUERY_PREFIX);

        if(userDescription.getSpecialization()!=null && !userDescription.getSpecialization().isEmpty()){
            query.append(SPECIALIZATION);
        }

        query.append(GET_DOCTORS_QUERY_TAIL);

        try (DatabaseOperation databaseOperation = new DatabaseOperation(query.toString())){
            int paramNumber = 1;

            if(userDescription.getSpecialization()!=null && !userDescription.getSpecialization().isEmpty()){
                databaseOperation.setParameter(paramNumber++, userDescription.getSpecialization());
            }

            databaseOperation.setParameter(paramNumber++, startFrom);
            databaseOperation.setParameter(paramNumber, limit);

            ResultSet resultSet = databaseOperation.invokeReadOperation();

            while (resultSet.next()){
                User user = new User();
                UserDescription searchedDescription = new UserDescription();

                user.setUserDescription(searchedDescription);

                searchedDescription.setSpecialization(resultSet.getString(TableColumn.USER_SPECIALIZATION));
                user.setLogin(resultSet.getString(TableColumn.USER_LOGIN));
                user.setRegistrationType(RegistrationType.valueOf(resultSet.getString(TableColumn.LOGIN_VIA).toUpperCase()));
                user.setFirstName(resultSet.getString(TableColumn.USER_FIRST_NAME));
                user.setSecondName(resultSet.getString(TableColumn.USER_SECOND_NAME));
                user.setPathToImage(resultSet.getString(TableColumn.USER_IMAGE));
                result.add(user);
            }

            return result;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not search doctors by specialization="+userDescription.getSpecialization(), e);
        }
    }

    @Override
    public void insertUser(User user) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_USER_QUERY)){

            if(user.getUserRole()==UserRole.DOCTOR){
                databaseOperation.beginTransaction();
            }

            databaseOperation.setParameter(TableColumn.USER_LOGIN, user.getLogin());
            databaseOperation.setParameter(TableColumn.USER_PASSWORD, user.getPassword());
            databaseOperation.setParameter(TableColumn.USER_FIRST_NAME, user.getFirstName());
            databaseOperation.setParameter(TableColumn.USER_SECOND_NAME, user.getSecondName());
            databaseOperation.setParameter(TableColumn.USER_GROUP, user.getUserRole().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.USER_IMAGE, user.getPathToImage());
            databaseOperation.setParameter(TableColumn.USER_MAIL, user.getMail());
            databaseOperation.setParameter(TableColumn.USER_PHONE, user.getPhone());
            databaseOperation.setParameter(TableColumn.REGISTRATION_TYPE, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.USER_GENDER, user.getGender().toString().toLowerCase());

            databaseOperation.invokeWriteOperation();

        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not insert user to database", e);
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

            if(databaseOperation.invokeWriteOperation()==0){
                throw new EntityDeletedException("User "+user+" was not found in database", true);
            }

        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not update users personal information", e);
        }

    }

    @Override
    public void updateUsersPassword(User user, String newPassword) throws DaoException {

        try(DatabaseOperation databaseOperation = new DatabaseOperation(UPDATE_PASSWORD)) {

            databaseOperation.setParameter(TableColumn.USER_PASSWORD, newPassword);
            databaseOperation.setParameter(TableColumn.USER_LOGIN, user.getLogin());
            databaseOperation.setParameter(TableColumn.REGISTRATION_TYPE, user.getRegistrationType().toString().toLowerCase());

            if(databaseOperation.invokeWriteOperation()==0){
                throw new EntityDeletedException("User "+user+" was not found in database", true);
            }

        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
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

            if(databaseOperation.invokeWriteOperation()==0){
                throw new EntityDeletedException("User "+user+" was not found in database", true);
            }

        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not update users contacts", e);
        }
    }

    @Override
    public void uploadProfileImage(User user) throws DaoException {

        try(DatabaseOperation databaseOperation = new DatabaseOperation(UPLOAD_PROFILE_IMAGE)) {

            databaseOperation.setParameter(TableColumn.USER_LOGIN, user.getLogin());
            databaseOperation.setParameter(TableColumn.REGISTRATION_TYPE, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.USER_IMAGE, user.getPathToImage());

            if(databaseOperation.invokeWriteOperation()==0){
                throw new EntityDeletedException("User "+user+" was not found in database", true);
            }

        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not upload profile image", e);
        }
    }

    @Override
    public void withdrawMoneyFromBalance(User user, float orderSum) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(WITHDRAW_MONEY_QUERY)){

            databaseOperation.setParameter(1, orderSum);
            databaseOperation.setParameter(2, user.getLogin());
            databaseOperation.setParameter(3, user.getRegistrationType().toString().toLowerCase());

            if(databaseOperation.invokeWriteOperation()==0){
                throw new EntityNotFoundException("User="+user+" was not found or order with id="+ orderSum +" does't exist");
            }

            databaseOperation.endTransaction();
        }  catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Can not withdraw cash from balance", e);
        }
    }

    private List<User> resultSetToUser(ResultSet resultSet) throws SQLException {
        List<User> result = new ArrayList<>();

        while (resultSet.next()) {
            User user = new User();
            UserDescription userDescription = new UserDescription();

            user.setUserDescription(userDescription);
            user.setLogin(resultSet.getString(TableColumn.USER_LOGIN));
            user.setRegistrationType(RegistrationType.valueOf(resultSet.getString(TableColumn.LOGIN_VIA).toUpperCase()));
            user.setFirstName(resultSet.getString(TableColumn.USER_FIRST_NAME));
            user.setSecondName(resultSet.getString(TableColumn.USER_SECOND_NAME));
            user.setMail(resultSet.getString(TableColumn.USER_MAIL));
            user.setPhone(resultSet.getString(TableColumn.USER_PHONE));
            user.setPathToImage((resultSet.getString(TableColumn.USER_IMAGE)));
            user.setGender(Gender.valueOf(resultSet.getString(TableColumn.USER_GENDER).toUpperCase()));
            user.setBalance(resultSet.getDouble(TableColumn.USER_BALANCE));
            user.setUserRole(UserRole.valueOf(resultSet.getString(TableColumn.USER_GROUP).toUpperCase()));
            userDescription.setDescription(resultSet.getString(TableColumn.USER_DESCRIPTION));
            userDescription.setSpecialization(resultSet.getString(TableColumn.USER_SPECIALIZATION));

            result.add(user);
        }

        return result;
    }

    private List<User> resultSetToUsersOnSearch(ResultSet resultSet) throws SQLException {
        List<User> doctors = new ArrayList<>();

        while (resultSet.next()){
            User user = new User();
            UserDescription userDescription = new UserDescription();

            user.setUserDescription(userDescription);
            user.setLogin(resultSet.getString(TableColumn.USER_LOGIN));
            user.setRegistrationType(RegistrationType.valueOf(resultSet.getString(TableColumn.LOGIN_VIA).toUpperCase()));
            user.setFirstName(resultSet.getString(TableColumn.USER_FIRST_NAME));
            user.setSecondName(resultSet.getString(TableColumn.USER_SECOND_NAME));
            user.setPathToImage(resultSet.getString(TableColumn.USER_IMAGE));

            userDescription.setSpecialization(resultSet.getString(TableColumn.USER_SPECIALIZATION));

            doctors.add(user);
        }

        return doctors;
    }
}
