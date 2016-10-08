package by.training.online_pharmacy.dao.impl.database;


import by.training.online_pharmacy.dao.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vladislav on 13.06.16.
 */
public class DatabaseDAOFactory extends DaoFactory {

    private final Map<DaoType, Object> daos = new HashMap<>();

    public DatabaseDAOFactory(){

        daos.put(DaoType.USER_DAO, new DatabaseUserDAO());
        daos.put(DaoType.DRUG_DAO, new DatabaseDrugDAO());
        daos.put(DaoType.USER_DESCRIPTION_DAO, new DatabaseUserDescriptionDAO());
        daos.put(DaoType.ORDER_DAO, new DatabaseOrderDAO());
        daos.put(DaoType.PRESCRIPTION_DAO, new DatabasePrescriptionDAO());
        daos.put(DaoType.DRUG_CLASS_DAO, new DatabaseDrugClassDAO());
        daos.put(DaoType.DRUG_MANUFACTURER_DAO, new DatabaseDrugManufacturerDao());
        daos.put(DaoType.REQUEST_FOR_PRESCRIPTION_DAO, new DatabaseRequestForPrescriptionDAO());
        daos.put(DaoType.MESSAGE_DAO, new DatabaseMessageDao());
        daos.put(DaoType.SECRET_WORD_DAO, new DatabaseSecretWordDao());
        daos.put(DaoType.SECRET_QUESTION_DAO, new DatabaseSecretQuestionDao());

    }

    public UserDAO getUserDAO() {
        return (UserDAO)daos.get(DaoType.USER_DAO);
    }

    public DrugDAO getDrugDAO() {
        return (DrugDAO)daos.get(DaoType.DRUG_DAO);
    }

    public UserDescriptionDAO getUserDescriptionDao() {
        return (UserDescriptionDAO)daos.get(DaoType.USER_DESCRIPTION_DAO);
    }

    public OrderDAO getOrderDao() {
        return (OrderDAO)daos.get(DaoType.ORDER_DAO);
    }

    public PrescriptionDAO getPrescriptionDAO() {
        return (PrescriptionDAO)daos.get(DaoType.PRESCRIPTION_DAO);
    }

    public DrugClassDAO getDrugClassDAO() {
        return (DrugClassDAO)daos.get(DaoType.DRUG_CLASS_DAO);
    }

    public DrugManufacturerDAO getDrugManufacturerDAO()  {
        return (DrugManufacturerDAO)daos.get(DaoType.DRUG_MANUFACTURER_DAO);
    }

    public RequestForPrescriptionDAO getRequestForPrescriptionDAO() {
        return (RequestForPrescriptionDAO)daos.get(DaoType.REQUEST_FOR_PRESCRIPTION_DAO);
    }

    public MessageDao getMessageDAO(){return  (MessageDao)daos.get(DaoType.MESSAGE_DAO);}

    public SecretWordDao getSecretWordDao() {return (SecretWordDao)daos.get(DaoType.SECRET_WORD_DAO);}

    public SecretQuestionDao getSecretQuestionDao(){ return (SecretQuestionDao)daos.get(DaoType.SECRET_QUESTION_DAO);}
}

