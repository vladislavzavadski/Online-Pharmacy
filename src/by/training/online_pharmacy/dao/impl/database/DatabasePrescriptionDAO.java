package by.training.online_pharmacy.dao.impl.database;


import by.training.online_pharmacy.dao.PrescriptionDAO;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityNotFoundException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.domain.Period;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.prescription.Prescription;
import by.training.online_pharmacy.domain.prescription.PrescriptionCriteria;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vladislav on 19.06.16.
 */
public class DatabasePrescriptionDAO implements PrescriptionDAO {
    private static final String GET_ALL_PRESCRIPTIONS_QUERY_PREFIX = "select pr_drug_id, pr_appointment_date, pr_expiration_date, pr_drug_count, pr_drug_dosage, dr_name, pr_doctor, pr_doctor_login_via, us_first_name, us_second_name" +
            "\n inner join users on pr_doctor=us_login and login_via=pr_doctor_login_via where pr_client_login=? and pr_login_via=? ";
    private static final String DRUG_NAME = " and dr_name like ? ";
    private static final String ACTIVE_PRESCRIPTIONS = " and pr_expiration_date>=curdate() ";
    private static final String NON_ACTIVE_PRESCRIPTIONS = " and pr_expiration_date<curdate() ";
    private static final String GET_ALL_PRESCRIPTIONS_QUERY_SUFFIX = " order by pr_expiration_date desc limit ?,?;";
    private static final String INCREACE_DRUG_COUNT_IN_PRESCRIPTION_BY_NEW_ORDER = "update prescriptions set pr_drug_count=pr_drug_count+(select or_drug_count from orders where or_id=?) where pr_client_login=? and pr_login_via=? and pr_drug_id=(select or_drug_id from orders where or_id=?)";
    private static final String REDUCE_DRUG_COUNT_IN_PRESCRIPTION_QUERY_BY_ORDER = "update prescriptions set pr_drug_count=pr_drug_count-? where pr_client_login=? and pr_login_via=? and pr_drug_id=? and pr_drug_dosage=?";
    private static final String REDUCE_DRUG_COUNT_IN_PRESCRIPTION_BY_ORDER = "update prescriptions set pr_drug_count=pr_drug_count-(select or_drug_count from orders where or_id=?) where pr_client_login=? and pr_login_via=? and pr_drug_id=(select or_drug_id from orders where or_id=?)";
    private static final String IS_PRESCRIPTION_EXIST_QUERY = "select pr_drug_id from prescriptions where pr_client_login=? and pr_login_via=? and pr_drug_id=? and pr_expiration_date>=curdate();";
    @Override
    public void increaseDrugCountByOrder(User user, int orderId) throws DaoException {
        try {
            DatabaseOperation databaseOperation = new DatabaseOperation(INCREACE_DRUG_COUNT_IN_PRESCRIPTION_BY_NEW_ORDER);
            databaseOperation.setParameter(1, orderId);
            databaseOperation.setParameter(2, user.getLogin());
            databaseOperation.setParameter(3, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(4, orderId);
            databaseOperation.invokeWriteOperation();
        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Can not update prescription count for user="+user, e);
        }
    }

    @Override
    public boolean isPrescriptionExist(User user, int drugId) throws DaoException {
        try(DatabaseOperation databaseOperation = new DatabaseOperation(IS_PRESCRIPTION_EXIST_QUERY)){
            databaseOperation.setParameter(1, user.getLogin());
            databaseOperation.setParameter(2, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(3, drugId);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            return resultSet.next();
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not check is user have prescription for user="+user, e);
        }
    }

    @Override
    public void reduceDrugCount(User user, int orderId) throws DaoException {
        try {
            DatabaseOperation databaseOperation = new DatabaseOperation(REDUCE_DRUG_COUNT_IN_PRESCRIPTION_BY_ORDER);
            databaseOperation.setParameter(1, orderId);
            databaseOperation.setParameter(2, user.getLogin());
            databaseOperation.setParameter(3, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(4, orderId);
            if(databaseOperation.invokeWriteOperation()==0){
                databaseOperation.rollBack();
                databaseOperation.close();
                throw new EntityNotFoundException("Prescription for user="+user+" was not found");
            }

        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Can not update prescription count for user="+user, e);
        }
    }

    @Override
    public void reduceDrugCount(User user, int drugId, int drugCount, int drugDosage) throws DaoException {
        try {
            DatabaseOperation databaseOperation = new DatabaseOperation(REDUCE_DRUG_COUNT_IN_PRESCRIPTION_QUERY_BY_ORDER);
            databaseOperation.setParameter(1, drugCount);
            databaseOperation.setParameter(2, user.getLogin());
            databaseOperation.setParameter(3, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(4, drugId);
            databaseOperation.setParameter(5, drugDosage);
            if(databaseOperation.invokeWriteOperation()==0){
                databaseOperation.rollBack();
                databaseOperation.close();
                throw new EntityNotFoundException("Prescription for user="+user+" was not found");
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not update prescription count for user="+user, e);
        }
    }

    @Override
    public List<Prescription> searchPrescriptions(User user, PrescriptionCriteria prescriptionCriteria, int limit, int startFrom) throws DaoException {
        StringBuilder query = new StringBuilder(GET_ALL_PRESCRIPTIONS_QUERY_PREFIX);
        if(prescriptionCriteria.getDrugName()!=null&&!prescriptionCriteria.getDrugName().isEmpty()){
            query.append(DRUG_NAME);
        }
        if(prescriptionCriteria.getPrescriptionStatus()!=null){
            switch (prescriptionCriteria.getPrescriptionStatus()){
                case ACTIVE:{
                    query.append(ACTIVE_PRESCRIPTIONS);
                }
                case NON_ACTIVE:{
                    query.append(NON_ACTIVE_PRESCRIPTIONS);
                }
            }
        }
        query.append(GET_ALL_PRESCRIPTIONS_QUERY_SUFFIX);
        try (DatabaseOperation databaseOperation = new DatabaseOperation(query.toString())){
            int paramNumber = 1;
            databaseOperation.setParameter(paramNumber++, user.getLogin());
            databaseOperation.setParameter(paramNumber++, user.getRegistrationType().toString().toLowerCase());
            if(prescriptionCriteria.getDrugName()!=null&&!prescriptionCriteria.getDrugName().isEmpty()){
                databaseOperation.setParameter(paramNumber++, Param.PER_CENT+prescriptionCriteria.getDrugName()+Param.PER_CENT);
            }
            databaseOperation.setParameter(paramNumber++, startFrom);
            databaseOperation.setParameter(paramNumber, limit);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            List<Prescription> prescriptions = resultSetToPrescription(resultSet);
            return prescriptions;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not load prescriptions from database by criteria="+prescriptionCriteria, e);
        }
    }

    @Override
    public List<Prescription> getUsersPrescriptions(String clientLogin, int limit, int startFrom) throws DaoException {
        return null;
    }

    @Override
    public List<Prescription> getPrescriptionsByDrugId(int drugId, int limit, int startFrom) throws DaoException {
        return null;
    }

    @Override
    public List<Prescription> getDoctorsPrescriptions(String doctorLogin, int limit, int startFrom) throws DaoException {
        return null;
    }

    @Override
    public Prescription getPrescriptionByPrimaryKey(String userLogin, int drugId) throws DaoException {
        return null;
    }

    @Override
    public List<Prescription> getPrescriptionsByAppointmentDate(Date date, Period period, int limit, int startFrom) throws DaoException {
        return null;
    }

    @Override
    public List<Prescription> getPrescriptionsByExpirationDate(Date date, Period period, int limit, int startFrom) throws DaoException {
        return null;
    }

    @Override
    public void insertPrescription(Prescription prescription) throws DaoException {

    }

    @Override
    public void updatePrescription(Prescription prescription) throws DaoException {

    }

    @Override
    public void deletePrescription(String clientLogin, int drugId) throws DaoException {

    }


    /*@Override
    public List<Prescription> getUsersPrescriptions(String clientLogin, int limit, int startFrom) throws DaoException {
        List<Prescription> prescriptions = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_USERS_PRESCRIPTIONS_QUERY, clientLogin, limit, startFrom)){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            prescriptions = resultSetToPrescription(resultSet);
        } catch (Exception e) {
            throw new DaoException("Can not load users prescriptions with login = \'" + clientLogin + "\' from database", e);
        }
        return prescriptions;
    }

    @Override
    public List<Prescription> getPrescriptionsByDrugId(int drugId, int limit, int startFrom) throws DaoException {
        List<Prescription> prescriptions = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_PRESCRIPTIONS_BY_ID_QUERY, drugId, limit, startFrom)){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            prescriptions = resultSetToPrescription(resultSet);
        } catch (Exception e) {
            throw new DaoException("Can not load prescriptions with drugId = \'" + drugId + "\' from database", e);
        }
        return prescriptions;
    }

    @Override
    public List<Prescription> getDoctorsPrescriptions(String doctorLogin, int limit, int startFrom) throws DaoException {
        List<Prescription> prescriptions = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_DOCTORS_PRESCRIPTIONS_QUERY, doctorLogin, limit, startFrom)){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            prescriptions = resultSetToPrescription(resultSet);
        } catch (Exception e) {
            throw new DaoException("Can not load prescriptions with doctor login = \'" + doctorLogin + "\' from database", e);
        }
        return prescriptions;
    }

    @Override
    public Prescription getPrescriptionByPrimaryKey(String userLogin, int drugId) throws DaoException {
        List<Prescription> prescriptions = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_PRESCRIPTIONS_BY_PRIMARY_KEY_QUERY, userLogin, drugId)){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            prescriptions = resultSetToPrescription(resultSet);
            if (!prescriptions.isEmpty()) {
                return prescriptions.get(0);
            }
        } catch (Exception e) {
            throw new DaoException("Can not load prescriptions with login = \'" + userLogin + "\' and drugId = \'" + drugId + "\' from database", e);
        }
        return null;
    }

    @Override
    public List<Prescription> getPrescriptionsByAppointmentDate(Date date, Period period, int limit, int startFrom) throws DaoException {
        List<Prescription> prescriptions = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation()){
            switch (period) {
                case AFTER_DATE: {
                    databaseOperation.init(GET_PRESCRIPTIONS_BY_APPOINTMENT_DATE_AFTER_QUERY, date, limit, startFrom);
                    break;
                }

                case BEFORE_DATE: {
                    databaseOperation.init(GET_PRESCRIPTIONS_BY_APPOINTMENT_DATE_BEFORE_QUERY, date, limit, startFrom);
                    break;
                }

                case CURRENT_DATE: {
                    databaseOperation.init(GET_PRESCRIPTIONS_BY_APPOINTMENT_DATE_CURRENT_QUERY, date, limit, startFrom);
                    break;
                }
            }
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            prescriptions = resultSetToPrescription(resultSet);
        } catch (Exception e) {
            throw new DaoException("Can not load prescriptions by appointment date = \'" + date + "\'", e);
        }
        return prescriptions;
    }

    @Override
    public List<Prescription> getPrescriptionsByExpirationDate(Date date, Period period, int limit, int startFrom) throws DaoException {
        List<Prescription> prescriptions = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation()){
            switch (period) {
                case AFTER_DATE: {
                    databaseOperation.init(GET_PRESCRIPTIONS_BY_EXPIRATION_DATE_AFTER_QUERY, date, limit, startFrom);
                    break;
                }

                case BEFORE_DATE: {
                    databaseOperation.init(GET_PRESCRIPTIONS_BY_EXPIRATION_DATE_BEFORE_QUERY, date, limit, startFrom);
                    break;
                }

                case CURRENT_DATE: {
                    databaseOperation.init(GET_PRESCRIPTIONS_BY_EXPIRATION_DATE_CURRENT_QUERY, date, limit, startFrom);
                    break;
                }
            }
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            prescriptions = resultSetToPrescription(resultSet);
        } catch (Exception e) {
            throw new DaoException("Can not load prescriptions by appointment date = \'" + date + "\'", e);
        }
        return prescriptions;
    }

    @Override
    public void insertPrescription(Prescription prescription) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_PRESCRIPTION_QUERY, prescription.getClient().getLogin(), prescription.getDrug().getId(), prescription.getDoctor().getLogin(), prescription.getAppointmentDate(), prescription.getExpirationDate(), prescription.getDrugCount(), prescription.getDrugDosage())){
            databaseOperation.invokeWriteOperation();
        } catch (Exception e) {
            throw new DaoException("Can not insert prescription " + prescription, e);
        }
    }

    @Override
    public void updatePrescription(Prescription prescription) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(UPDATE_PRESCRIPTION_QUERY, prescription.getDoctor().getLogin(), prescription.getAppointmentDate(), prescription.getExpirationDate(), prescription.getDrugCount(), prescription.getDrugDosage(), prescription.getClient().getLogin(), prescription.getDrug().getId())){
            databaseOperation.invokeWriteOperation();
        } catch (Exception e) {
            throw new DaoException("Can not update prescription " + prescription, e);
        }
    }

    @Override
    public void deletePrescription(String clientLogin, int drugId) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(DELETE_PRESCRIPTION_QUERY, clientLogin, drugId)){
            databaseOperation.invokeWriteOperation();
        } catch (Exception e) {
            throw new DaoException("Can not delete prescription with clientLogin = \'" + clientLogin + "\' and drugId = \'" + drugId, e);
        }
    }
*/
    private List<Prescription> resultSetToPrescription(ResultSet resultSet) throws SQLException {
        List<Prescription> result = new ArrayList<>();
        while (resultSet.next()) {
            Prescription prescription = new Prescription();
            User doctor = new User();
            User client = new User();
            Drug drug = new Drug();
            prescription.setDoctor(doctor);
            prescription.setClient(client);
            prescription.setDoctor(doctor);
            prescription.setAppointmentDate(resultSet.getDate(TableColumn.PRESCRIPTION_APPOINTMENT_DATE));
            prescription.setExpirationDate(resultSet.getDate(TableColumn.PRESCRIPTION_EXPIRATION_DATE));
            prescription.setDrugCount(resultSet.getShort(TableColumn.PRESCRIPTION_DRUG_COUNT));
            prescription.setDrugDosage(resultSet.getShort(TableColumn.PRESCRIPTION_DRUG_DOSAGE));
            doctor.setFirstName(resultSet.getString(TableColumn.USER_FIRST_NAME));
            doctor.setSecondName(resultSet.getString(TableColumn.USER_SECOND_NAME));
            doctor.setLogin(resultSet.getString(TableColumn.USER_LOGIN));
            doctor.setRegistrationType(RegistrationType.valueOf(resultSet.getString(TableColumn.LOGIN_VIA).toUpperCase()));
            drug.setName(resultSet.getString(TableColumn.DRUG_NAME));
            drug.setId(resultSet.getInt(TableColumn.DRUG_ID));

            result.add(prescription);
        }
        return result;

    }
}