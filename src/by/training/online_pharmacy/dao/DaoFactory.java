package by.training.online_pharmacy.dao;

import by.training.online_pharmacy.dao.impl.database.DatabaseDAOFactory;

/**
 * Created by vladislav on 19.06.16.
 */
public abstract class DaoFactory {
    public static final int DATABASE_DAO_IMPL = 1;
    private static final DaoFactory databaseDaoFactory = new DatabaseDAOFactory();

    public static DaoFactory takeFactory(int whichFactory){

        switch (whichFactory){

            case DATABASE_DAO_IMPL:{
                return databaseDaoFactory;
            }

            default:{
                return null;
            }
        }
    }

    public abstract UserDAO getUserDAO();

    public abstract DrugDAO getDrugDAO();

    public abstract UserDescriptionDAO getUserDescriptionDao();

    public abstract OrderDAO getOrderDao();

    public abstract PrescriptionDAO getPrescriptionDAO();

    public abstract DrugClassDAO getDrugClassDAO();

    public abstract DrugManufacturerDAO getDrugManufacturerDAO();

    public abstract RequestForPrescriptionDAO getRequestForPrescriptionDAO();

    public abstract MessageDao getMessageDAO();

    public abstract SecretWordDao getSecretWordDao();

    public abstract SecretQuestionDao getSecretQuestionDao();
}
