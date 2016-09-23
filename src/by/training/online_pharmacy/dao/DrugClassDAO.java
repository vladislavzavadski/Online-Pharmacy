package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.drug.DrugClass;

import java.util.List;

/**
 * Created by vladislav on 19.06.16.
 */
public interface DrugClassDAO {

    /**
     * Create new drug class and insert it to storage
     * @param drugClass object, that describe new drug class
     * @throws DaoException when fail occurs while new entity insert*/
    void insertDrugClass(DrugClass drugClass) throws DaoException;

    /**
    * Retrieve all drug classes  from storage
     * @return List that include all drug classes
     * @throws DaoException when fail to retrieve drug classes from storage*/
    List<DrugClass> getAllDrugClasses() throws DaoException;

    /**
     * Check if drug class exist
     * @param className string, that include class name
     * @return <code>true</code> if drug class exist and <code>false</code> if drug class not exist
     * @throws DaoException if fail to check is drug class exist*/
    boolean isDrugClassExist(String className) throws DaoException;
}
