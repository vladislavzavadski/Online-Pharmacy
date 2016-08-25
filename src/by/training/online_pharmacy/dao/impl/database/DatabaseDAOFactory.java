package by.training.online_pharmacy.dao.impl.database;


import by.training.online_pharmacy.dao.*;
import by.training.online_pharmacy.dao.exception.DaoException;

/**
 * Created by vladislav on 13.06.16.
 */
public class DatabaseDAOFactory extends DaoFactory {

    public DatabaseDAOFactory(){}

    public UserDAO getUserDAO() {
        return new DatabaseUserDAO();
    }

    public DrugDAO getDrugDAO() {
        return new DatabaseDrugDAO();
    }

    public UserDescriptionDAO getUserDescriptionDao() {
        return new DatabaseUserDescriptionDAO();
    }

    public OrderDAO getOrderDao() {
        return new DatabaseOrderDAO();
    }

    public PrescriptionDAO getPrescriptionDAO() {
        return new DatabasePrescriptionDAO();
    }

    public DrugClassDAO getDrugClassDAO() {
        return new DatabaseDrugClassDAO();
    }

    public DrugManufacturerDAO getDrugManufacturerDAO()  {
        return new DatabaseDrugManufacturerDao();
    }

    public RequestForPrescriptionDAO getRequestForPrescriptionDAO() {
        return new DatabaseRequestForPrescriptionDAO();
    }

    public MessageDao getMessageDAO(){return  new DatabaseMessageDao();}

}

