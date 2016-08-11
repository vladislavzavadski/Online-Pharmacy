package by.training.online_pharmacy.dao.impl.database;


import by.training.online_pharmacy.dao.RequestForPrescriptionDAO;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.domain.Period;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.prescription.RequestForPrescription;
import by.training.online_pharmacy.domain.prescription.RequestStatus;
import by.training.online_pharmacy.domain.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vladislav on 19.06.16.
 */
public class DatabaseRequestForPrescriptionDAO implements RequestForPrescriptionDAO {
    private static final String GET_REQUESTS_BY_CLIENT_QUERY =  "select cl.us_login, dr_id, re_doctor, re_id, re_prolong_to, re_request_date, re_clients_comment, re_doctors_comment, dr_name, cl.us_first_name, cl.us_second_name, doc.us_first_name as doc_first_name, doc.us_second_name as doc_second_name, doc.us_login as doc_login  from requests_for_prescriptions inner join drugs on dr_id = re_drug_id inner join users as cl on re_client_login = cl.us_login inner join users as doc on re_doctor = doc.us_login WHERE re_client_login=? LIMIT ?, ?;";
    private static final String GET_REQUESTS_BY_DRUG_ID_QUERY = "select cl.us_login, dr_id, re_doctor, re_id, re_prolong_to, re_request_date, re_clients_comment,  re_doctors_comment, dr_name, cl.us_first_name, cl.us_second_name, doc.us_first_name as doc_first_name, doc.us_second_name as doc_second_name, doc.us_login as doc_login  from requests_for_prescriptions inner join drugs on dr_id = re_drug_id inner join users as cl on re_client_login = cl.us_login inner join users as doc on re_doctor = doc.us_login WHERE re_drug_id=? LIMIT ?, ?;";
    private static final String GET_REQUESTS_BY_DOCTOR_QUERY = "select cl.us_login, dr_id, re_doctor, re_id, re_prolong_to, re_request_date, re_clients_comment, re_doctors_comment, dr_name, cl.us_first_name, cl.us_second_name, doc.us_first_name as doc_first_name, doc.us_second_name as doc_second_name, doc.us_login as doc_login  from requests_for_prescriptions inner join drugs on dr_id = re_drug_id inner join users as cl on re_client_login = cl.us_login inner join users as doc on re_doctor = doc.us_login WHERE re_doctor=? LIMIT ?, ?;";
    private static final String GET_REQUESTS_BY_STATUS_QUERY = "select cl.us_login, dr_id, re_doctor, re_id, re_prolong_to, re_request_date, re_clients_comment, re_doctors_comment, dr_name, cl.us_first_name, cl.us_second_name, doc.us_first_name as doc_first_name, doc.us_second_name as doc_second_name, doc.us_login as doc_login  from requests_for_prescriptions inner join drugs on dr_id = re_drug_id inner join users as cl on re_client_login = cl.us_login inner join users as doc on re_doctor = doc.us_login WHERE re_status=? LIMIT ?, ?;";
    private static final String GET_REQUESTS_BY_DATE_BEFORE_QUERY = "select cl.us_login, dr_id, re_doctor, re_id, re_prolong_to, re_request_date, re_clients_comment, re_doctors_comment, dr_name, cl.us_first_name, cl.us_second_name, doc.us_first_name as doc_first_name, doc.us_second_name as doc_second_name, doc.us_login as doc_login  from requests_for_prescriptions inner join drugs on dr_id = re_drug_id inner join users as cl on re_client_login = cl.us_login inner join users as doc on re_doctor = doc.us_login WHERE re_request_date<? LIMIT ?, ?;";
    private static final String GET_REQUESTS_BY_DATE_AFTER_QUERY = "select cl.us_login, dr_id, re_doctor, re_id, re_prolong_to, re_request_date, re_clients_comment, re_doctors_comment, dr_name, cl.us_first_name, cl.us_second_name, doc.us_first_name as doc_first_name, doc.us_second_name as doc_second_name, doc.us_login as doc_login  from requests_for_prescriptions inner join drugs on dr_id = re_drug_id inner join users as cl on re_client_login = cl.us_login inner join users as doc on re_doctor = doc.us_login WHERE re_request_date>? LIMIT ?, ?;";
    private static final String GET_REQUESTS_BY_DATE_CURRENT_QUERY = "select cl.us_login, dr_id, re_doctor, re_id, re_prolong_to, re_request_date, re_clients_comment, re_doctors_comment, dr_name, cl.us_first_name, cl.us_second_name, doc.us_first_name as doc_first_name, doc.us_second_name as doc_second_name, doc.us_login as doc_login  from requests_for_prescriptions inner join drugs on dr_id = re_drug_id inner join users as cl on re_client_login = cl.us_login inner join users as doc on re_doctor = doc.us_login WHERE re_request_date=? LIMIT ?, ?;";
    private static final String GET_REQUESTS_BY_ID = "select re_status, cl.us_login, dr_id, re_doctor, re_id, re_prolong_to, re_request_date, re_clients_comment, re_doctors_comment, dr_name, cl.us_first_name, cl.us_second_name, doc.us_first_name as doc_first_name, doc.us_second_name as doc_second_name, doc.us_login as doc_login  from requests_for_prescriptions inner join drugs on dr_id = re_drug_id inner join users as cl on re_client_login = cl.us_login inner join users as doc on re_doctor = doc.us_login WHERE re_id=? LIMIT 1;";
    private static final String UPDATE_REQUEST_QUERY = "UPDATE requests_for_prescriptions SET re_drug_id=?, re_doctor=?, re_prolong_to=?, re_status=?, re_clients_comment=?, re_doctors_comment=? WHERE re_id=?;";
    private static final String DELETE_REQUEST_QUERY = "DELETE FROM requests_for_prescriptions WHERE re_id=?;";

    private static final String INSERT_REQUEST_QUERY = "insert into requests_for_prescriptions" +
            " (re_client_login, re_drug_id, re_prolong_to, re_status, re_clients_comment, re_user_login_via,  re_request_date, re_doctor, re_doctor_login_via) \n" +
            "select ?, ?, ?, 'in_progress', ?, ?, CURDATE(), doc.doc_login, doc.doc_via from\n" +
            "(select doctors.us_login as doc_login, doctors.login_via as doc_via, \n" +
            "count(re_id) as count from requests_for_prescriptions right join \n" +
            "(select us_login, login_via from users inner join staff_descriptions on us_login=sd_user_login and login_via=sd_login_via where\n" +
            "sd_specialization=(select doctor_specialization from drugs where dr_id=?)) as doctors on re_doctor=doctors.us_login and re_doctor_login_via=doctors.login_via where re_status='in_progress' or re_status is NULL group by re_doctor,\n" +
            "re_doctor_login_via  order by count limit 1) as doc;";
    private static final String FK_DRUG = "fk_drug";
    private static final String FK_CLIENT = "fk_client";

    @Override
    public void insertRequest(RequestForPrescription requestForPrescription) throws DaoException {
        try(DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_REQUEST_QUERY)){
            databaseOperation.setParameter(1, requestForPrescription.getClient().getLogin());
            databaseOperation.setParameter(2, requestForPrescription.getDrug().getId());
            databaseOperation.setParameter(3, requestForPrescription.getProlongDate());
            databaseOperation.setParameter(4, requestForPrescription.getClientComment());
            databaseOperation.setParameter(5, requestForPrescription.getClient().getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(6, requestForPrescription.getDrug().getId());
            databaseOperation.invokeWriteOperation();
        } catch (ConnectionPoolException e) {
            throw new DaoException("Can not insert new request for prescription "+requestForPrescription, e);
        } catch (SQLException ex){
            if(ex.getErrorCode()==ErrorCode.ERROR_CODE_1452&&ex.getMessage().contains(FK_DRUG)){
                throw new EntityDeletedException("Can not insert new request for prescription "+requestForPrescription, false, ex);
            } else if(ex.getErrorCode()==ErrorCode.ERROR_CODE_1452&&ex.getMessage().contains(FK_CLIENT)){
                throw new EntityDeletedException("Can not insert new request for prescription "+requestForPrescription, true, ex);
            }
            else {
                throw new DaoException("Can not insert new request for prescription "+requestForPrescription, ex);
            }
        } catch (Exception e) {
            throw new DaoException("Can not insert new request for prescription "+requestForPrescription, e);
        }
    }

    @Override
    public List<RequestForPrescription> getRequestsByClient(String clientLogin, int limit, int startFrom) throws DaoException {
        return null;
    }

    @Override
    public List<RequestForPrescription> getRequestsByDrugId(int drugId, int limit, int startFrom) throws DaoException {
        return null;
    }

    @Override
    public List<RequestForPrescription> getRequestsByDoctor(String doctorLogin, int limit, int startFrom) throws DaoException {
        return null;
    }

    @Override
    public List<RequestForPrescription> getRequestsByStatus(RequestStatus requestStatus, int limit, int startFrom) throws DaoException {
        return null;
    }

    @Override
    public List<RequestForPrescription> getRequestsByDate(Date requestDate, Period period, int limit, int startFrom) throws DaoException {
        return null;
    }

    @Override
    public RequestForPrescription getRequestById(int requestId) throws DaoException {
        return null;
    }


    @Override
    public void updateRequest(RequestForPrescription requestForPrescription) throws DaoException {

    }

    @Override
    public void deleteRequest(int requestId) throws DaoException {

    }


    /*@Override
    public List<RequestForPrescription> getRequestsByClient(String clientLogin, int limit, int startFrom) throws DaoException {
        List<RequestForPrescription> requestForPrescriptions = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_REQUESTS_BY_CLIENT_QUERY, clientLogin, limit, startFrom)){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            requestForPrescriptions = resultSetToRequest(resultSet);
        } catch (Exception e) {
            throw new DaoException("Can not load requests for user with login = \'"+clientLogin+"\'"+clientLogin, e);
        }
        return requestForPrescriptions;
    }

    @Override
    public List<RequestForPrescription> getRequestsByDrugId(int drugId, int limit, int startFrom) throws DaoException {
        List<RequestForPrescription> requestForPrescriptions = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_REQUESTS_BY_DRUG_ID_QUERY, drugId, limit, startFrom)){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            requestForPrescriptions = resultSetToRequest(resultSet);
        } catch (Exception e) {
            throw new DaoException("Can not load requests for drugId = \'"+drugId+"\'", e);
        }
        return requestForPrescriptions;
    }

    @Override
    public List<RequestForPrescription> getRequestsByDoctor(String doctorLogin, int limit, int startFrom) throws DaoException {
        List<RequestForPrescription> requestForPrescriptions = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_REQUESTS_BY_DOCTOR_QUERY, doctorLogin, limit, startFrom)){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            requestForPrescriptions = resultSetToRequest(resultSet);
        } catch (Exception e) {
            throw new DaoException("Can not load requests with doctorLogin = \'"+doctorLogin+"\'", e);
        }
        return requestForPrescriptions;
    }

    @Override
    public List<RequestForPrescription> getRequestsByStatus(RequestStatus requestStatus, int limit, int startFrom) throws DaoException {
        List<RequestForPrescription> requestForPrescriptions = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_REQUESTS_BY_STATUS_QUERY, requestStatus.toString().toLowerCase(), limit, startFrom)){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            requestForPrescriptions = resultSetToRequest(resultSet);
        } catch (Exception e) {
            throw new DaoException("Can not load requests with requestStatus \'"+requestStatus+"\'", e);
        }
        return requestForPrescriptions;
    }

    @Override
    public List<RequestForPrescription> getRequestsByDate(Date requestDate, Period period, int limit, int startFrom) throws DaoException {
        List<RequestForPrescription> requestForPrescriptions = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation()){

            switch (period) {
                case AFTER_DATE: {
                    databaseOperation.init(GET_REQUESTS_BY_DATE_AFTER_QUERY, requestDate, limit, startFrom);
                    break;
                }

                case BEFORE_DATE: {
                    databaseOperation.init(GET_REQUESTS_BY_DATE_BEFORE_QUERY, requestDate, limit, startFrom);
                    break;
                }

                case CURRENT_DATE: {
                    databaseOperation.init(GET_REQUESTS_BY_DATE_CURRENT_QUERY, requestDate, limit, startFrom);
                    break;
                }
            }
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            requestForPrescriptions = resultSetToRequest(resultSet);
        }catch (Exception e){
            throw new DaoException("Can not load requests from database with requestDate = \'"+requestDate+"\'", e);
        }
        return requestForPrescriptions;
    }

    @Override
    public RequestForPrescription getRequestById(int requestId) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_REQUESTS_BY_ID, requestId)){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            List<RequestForPrescription> results = resultSetToRequest(resultSet);
            if(!results.isEmpty()){
                return results.get(0);
            }
        } catch (Exception e) {
            throw new DaoException("Can not load request with id = \'"+requestId+"\'", e);
        }
        return null;
    }

    @Override
    public void insertRequest(RequestForPrescription requestForPrescription) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_REQUEST_QUERY, requestForPrescription.getId(), requestForPrescription.getClient().getLogin(), requestForPrescription.getDrug().getId(), requestForPrescription.getDoctor().getLogin(), requestForPrescription.getProlongDate(), requestForPrescription.getRequestStatus().toString().toLowerCase(), requestForPrescription.getClientComment(), requestForPrescription.getDoctorComment(), requestForPrescription.getRequestDate())){
            databaseOperation.invokeWriteOperation();
        } catch (Exception e) {
            throw new DaoException("Can not insert new request "+ requestForPrescription, e);
        }
    }

    @Override
    public void updateRequest(RequestForPrescription requestForPrescription) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(UPDATE_REQUEST_QUERY, requestForPrescription.getDrug().getId(), requestForPrescription.getDoctor().getLogin(), requestForPrescription.getProlongDate(), requestForPrescription.getRequestStatus().toString().toLowerCase(), requestForPrescription.getClientComment(), requestForPrescription.getDoctorComment(), requestForPrescription.getId())){
//"UPDATE requests_for_prescriptions re_drug_id=?, re_doctor=?, re_prolong_to=?, re_status=?, re_clients_comment=?, re_doctors_comment=? WHERE re_id=?;";
            databaseOperation.invokeWriteOperation();
        } catch (Exception e) {
            throw new DaoException("Can not update request "+requestForPrescription,e);
        }
    }

    @Override
    public void deleteRequest(int requestId) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(DELETE_REQUEST_QUERY, requestId)){
            databaseOperation.invokeWriteOperation();
        } catch (Exception e) {
            throw new DaoException("Can not delete request with requestId = \'"+requestId+"\'");
        }
    }

    private List<RequestForPrescription> resultSetToRequest(ResultSet resultSet) throws SQLException {
        List<RequestForPrescription> result = new ArrayList<>();
        while (resultSet.next()) {
            RequestForPrescription requestForPrescription = new RequestForPrescription();
            User doctor = new User();
            User client = new User();
            Drug drug = new Drug();
            requestForPrescription.setClient(client);
            requestForPrescription.setDrug(drug);
            requestForPrescription.setDoctor(doctor);
            requestForPrescription.setId(resultSet.getInt(TableColumn.REQUEST_ID));
            requestForPrescription.setProlongDate(resultSet.getDate(TableColumn.REQUEST_PROLONG_TO));
            requestForPrescription.setRequestDate(resultSet.getDate(TableColumn.REQUEST_DATE));
            requestForPrescription.setRequestStatus(RequestStatus.valueOf(resultSet.getString(TableColumn.REQUEST_STATUS).toUpperCase()));
            requestForPrescription.setClientComment(resultSet.getString(TableColumn.REQUEST_CLIENT_COMMENT));
            requestForPrescription.setDoctorComment(resultSet.getString(TableColumn.REQUEST_DOCTOR_COMMENT));
            doctor.setFirstName(resultSet.getString(TableColumn.DOCTOR_FIRST_NAME));
            doctor.setSecondName(resultSet.getString(TableColumn.DOCTOR_SECOND_NAME));
            doctor.setLogin(resultSet.getString(TableColumn.DOCTOR_LOGIN));
            client.setLogin(resultSet.getString(TableColumn.USER_LOGIN));
            client.setFirstName(resultSet.getString(TableColumn.USER_FIRST_NAME));
            client.setSecondName(resultSet.getString(TableColumn.USER_SECOND_NAME));
            drug.setId(resultSet.getInt(TableColumn.DRUG_ID));
            drug.setName(resultSet.getString(TableColumn.DRUG_NAME));
            result.add(requestForPrescription);
        }
        return result;
    }*/
}
