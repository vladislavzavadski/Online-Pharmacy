package by.training.online_pharmacy.dao.impl.database;


import by.training.online_pharmacy.dao.PrescriptionDAO;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityNotFoundException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.prescription.Prescription;
import by.training.online_pharmacy.domain.prescription.PrescriptionCriteria;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladislav on 19.06.16.
 */
public class DatabasePrescriptionDAO implements PrescriptionDAO {
    private static final String GET_ALL_CLIENT_PRESCRIPTIONS_QUERY_PREFIX = "select clients.us_login as cli_login, clients.login_via as cli_login_via, clients.us_first_name as cli_first_name, clients.us_second_name as cli_second_name, pr_drug_id, pr_appointment_date, pr_expiration_date, pr_drug_count, pr_drug_dosage, dr_name, dr_id, pr_doctor, pr_doctor_login_via, doc.us_first_name as doc_first_name, doc.us_second_name as doc_second_name, doc.us_login as doc_login, doc.login_via as doc_login_via" +
            "\nfrom prescriptions inner join users as doc on pr_doctor=us_login and login_via=pr_doctor_login_via inner join drugs on dr_id=pr_drug_id inner join users as clients on clients.us_login=pr_client_login and clients.login_via=pr_login_via where pr_client_login=? and pr_login_via=? ";

    private static final String GET_ALL_DOCTOR_PRESCRIPTIONS_QUERY_PREFIX = "select clients.us_login as cli_login, clients.login_via as cli_login_via, clients.us_first_name as cli_first_name, clients.us_second_name as cli_second_name, pr_drug_id, pr_appointment_date, pr_expiration_date, pr_drug_count, pr_drug_dosage, dr_name, dr_id, pr_doctor, pr_doctor_login_via, doc.us_first_name as doc_first_name, doc.us_second_name as doc_second_name, doc.us_login as doc_login, doc.login_via as doc_login_via" +
            "\nfrom prescriptions inner join users as doc on pr_doctor=us_login and login_via=pr_doctor_login_via inner join drugs on dr_id=pr_drug_id inner join users as clients on clients.us_login=pr_client_login and clients.login_via=pr_login_via where pr_doctor=? and pr_doctor_login_via=? ";

    private static final String DRUG_NAME = " and dr_name like ? ";
    private static final String ACTIVE_PRESCRIPTIONS = " and pr_expiration_date>=curdate() ";
    private static final String NON_ACTIVE_PRESCRIPTIONS = " and pr_expiration_date<curdate() ";
    private static final String GET_ALL_PRESCRIPTIONS_QUERY_SUFFIX = " order by pr_expiration_date desc limit ?,?;";
    private static final String INCREASE_DRUG_COUNT_IN_PRESCRIPTION_BY_NEW_ORDER = "update prescriptions set pr_drug_count=pr_drug_count+(select or_drug_count from orders where or_id=?) where pr_client_login=? and pr_login_via=? and pr_drug_id=(select or_drug_id from orders where or_id=?)";

    private static final String REDUCE_DRUG_COUNT = "update prescriptions set pr_drug_count=pr_drug_count-? where pr_client_login=? and pr_login_via=? and pr_drug_id=? and pr_drug_dosage=?";
    private static final String REDUCE_DRUG_COUNT_IN_PRESCRIPTION_BY_ORDER = "update prescriptions set pr_drug_count=pr_drug_count-(select or_drug_count from orders where or_id=?) where pr_client_login=? and pr_login_via=? and pr_drug_id=(select or_drug_id from orders where or_id=?)";
    private static final String GET_ACTIVE_PRESCRIPTION_QUERY = "select pr_drug_dosage, pr_drug_count from prescriptions where pr_client_login=? and pr_login_via=? and pr_drug_id=? and pr_expiration_date>=curdate();";

    private static final String UPDATE_PRESCRIPTION = "insert into prescriptions (pr_client_login, pr_drug_id, pr_login_via, pr_appointment_date, pr_expiration_date, pr_doctor, pr_drug_count,\n" +
            "pr_drug_dosage,  pr_doctor_login_via) values((select re_client_login from requests_for_prescriptions where re_id=?),(select re_drug_id from requests_for_prescriptions where re_id=?),(select re_user_login_via from requests_for_prescriptions where re_id=?),curdate(),?,?,?,?,?) on duplicate key update pr_expiration_date=values(pr_expiration_date),\n" +
            "pr_doctor=values(pr_doctor), pr_drug_count=values(pr_drug_count), pr_drug_dosage=values(pr_drug_dosage), pr_doctor_login_via=values(pr_doctor_login_via);";

    @Override
    public void createPrescription(Prescription prescription, int requestForPrescriptionId) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(UPDATE_PRESCRIPTION)){

            databaseOperation.setParameter(1, requestForPrescriptionId);
            databaseOperation.setParameter(2, requestForPrescriptionId);
            databaseOperation.setParameter(3, requestForPrescriptionId);
            databaseOperation.setParameter(4, prescription.getExpirationDate());
            databaseOperation.setParameter(5, prescription.getDoctor().getLogin());
            databaseOperation.setParameter(6, prescription.getDrugCount());
            databaseOperation.setParameter(7, prescription.getDrugDosage());
            databaseOperation.setParameter(8, prescription.getDoctor().getRegistrationType().toString().toLowerCase());

            databaseOperation.invokeWriteOperation();

            databaseOperation.endTransaction();
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not create new prescription="+prescription, e);
        }
    }

    @Override
    public void increaseDrugCountByOrder(User user, int orderId) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(INCREASE_DRUG_COUNT_IN_PRESCRIPTION_BY_NEW_ORDER)){

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
    public Prescription getActiveUserPrescription(User user, int drugId) throws DaoException {

        try(DatabaseOperation databaseOperation = new DatabaseOperation(GET_ACTIVE_PRESCRIPTION_QUERY)){

            databaseOperation.setParameter(1, user.getLogin());
            databaseOperation.setParameter(2, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(3, drugId);

            ResultSet resultSet = databaseOperation.invokeReadOperation();

            if(resultSet.next()) {
                Prescription prescription = new Prescription();
                prescription.setDrugCount(resultSet.getShort(TableColumn.PRESCRIPTION_DRUG_COUNT));
                prescription.setDrugDosage(resultSet.getShort(TableColumn.PRESCRIPTION_DRUG_DOSAGE));
                return prescription;
            }
            else {
                return null;
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not check is user have prescription for user="+user, e);
        }
    }

    @Override
    public void reduceDrugCount(User user, int orderId) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(REDUCE_DRUG_COUNT_IN_PRESCRIPTION_BY_ORDER)){

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

        try (DatabaseOperation databaseOperation = new DatabaseOperation(REDUCE_DRUG_COUNT)){

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
    public List<Prescription> getUserPrescriptions(User user, PrescriptionCriteria prescriptionCriteria, int limit,
                                                   int startFrom) throws DaoException {
        return searchPrescriptions(user, prescriptionCriteria, GET_ALL_CLIENT_PRESCRIPTIONS_QUERY_PREFIX, limit, startFrom);
    }

    @Override
    public List<Prescription> getDoctorPrescriptions(User user, PrescriptionCriteria prescriptionCriteria, int limit,
                                                     int startFrom) throws DaoException {
        return searchPrescriptions(user, prescriptionCriteria, GET_ALL_DOCTOR_PRESCRIPTIONS_QUERY_PREFIX, limit, startFrom);
    }

    private List<Prescription> searchPrescriptions(User user, PrescriptionCriteria prescriptionCriteria,
                                                   String queryPrefix, int limit, int startFrom) throws DaoException {
        StringBuilder query = new StringBuilder(queryPrefix);

        if(prescriptionCriteria.getDrugName()!=null&&!prescriptionCriteria.getDrugName().isEmpty()){
            query.append(DRUG_NAME);
        }

        if(prescriptionCriteria.getPrescriptionStatus()!=null){
            switch (prescriptionCriteria.getPrescriptionStatus()){
                case ACTIVE:{
                    query.append(ACTIVE_PRESCRIPTIONS);
                    break;
                }
                case NON_ACTIVE:{
                    query.append(NON_ACTIVE_PRESCRIPTIONS);
                    break;
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

    private List<Prescription> resultSetToPrescription(ResultSet resultSet) throws SQLException {
        List<Prescription> result = new ArrayList<>();
        while (resultSet.next()) {
            Prescription prescription = new Prescription();
            User doctor = new User();
            User client = new User();
            Drug drug = new Drug();

            prescription.setDoctor(doctor);
            prescription.setClient(client);
            prescription.setDrug(drug);
            prescription.setAppointmentDate(resultSet.getDate(TableColumn.PRESCRIPTION_APPOINTMENT_DATE));
            prescription.setExpirationDate(resultSet.getDate(TableColumn.PRESCRIPTION_EXPIRATION_DATE));
            prescription.setDrugCount(resultSet.getShort(TableColumn.PRESCRIPTION_DRUG_COUNT));
            prescription.setDrugDosage(resultSet.getShort(TableColumn.PRESCRIPTION_DRUG_DOSAGE));

            doctor.setFirstName(resultSet.getString(TableColumn.DOC_FIRST_NAME));
            doctor.setSecondName(resultSet.getString(TableColumn.DOC_SECOND_NAME));
            doctor.setLogin(resultSet.getString(TableColumn.DOC_LOGIN));
            doctor.setRegistrationType(RegistrationType.valueOf(resultSet.getString(TableColumn.DOC_LOGIN_VIA).toUpperCase()));

            client.setFirstName(resultSet.getString(TableColumn.CLIENT_FIRST_NAME));
            client.setSecondName(resultSet.getString(TableColumn.CLIENT_SECOND_NAME));
            client.setLogin(resultSet.getString(TableColumn.CLIENT_LOGIN));
            client.setRegistrationType(RegistrationType.valueOf(resultSet.getString(TableColumn.CLIENT_LOGIN_VIA).toUpperCase()));

            drug.setName(resultSet.getString(TableColumn.DRUG_NAME));
            drug.setId(resultSet.getInt(TableColumn.DRUG_ID));

            result.add(prescription);
        }
        return result;

    }
}