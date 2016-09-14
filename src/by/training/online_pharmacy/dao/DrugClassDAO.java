package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.drug.DrugClass;

import java.util.List;

/**
 * Created by vladislav on 19.06.16.
 */
public interface DrugClassDAO {

    void insertDrugClass(DrugClass drugClass) throws DaoException;

    List<DrugClass> getAllDrugClasses() throws DaoException;

    boolean isDrugClassExist(String className) throws DaoException;
}
