package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.drug.SearchDrugsCriteria;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.exception.NotFoundException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by vladislav on 13.06.16.
 */
public interface DrugDAO {
    void reduceDrugCountByReestablishedOrder(User user, int orderId) throws DaoException;

    InputStream getDrugImage(int drugId) throws DaoException, FileNotFoundException;

    Drug getDrugById(int drugId) throws DaoException;

    List<Drug> extendedSearching(SearchDrugsCriteria searchDrugsCriteria, int startFrom, int limit) throws DaoException;
    boolean isPrescriptionEnable(int drugId) throws DaoException, NotFoundException;

    boolean isPrescriptionEnableByOrder(int orderId) throws DaoException;

    void updateDrugCountByCanceledOrder(User user, int orderId) throws DaoException;

    List<Drug> searchDrugs(String query, int limit, int startFrom) throws DaoException;

    List<Drug> getAllDrugs(int limit, int startFrom) throws DaoException;
    List<Drug> getDrugsByName(String name, int limit, int startFrom) throws DaoException;
    List<Drug> getDrugsByClass(String drugClass, int limit, int startFrom) throws DaoException;
    List<Drug> getDrugsByActiveSubstance(String activeSubstance, int limit, int startFrom) throws DaoException;
    void insertDrug(Drug drug) throws DaoException;
    void updateDrug(Drug drug) throws DaoException;
    void deleteDrug(int drugId) throws DaoException;
}
