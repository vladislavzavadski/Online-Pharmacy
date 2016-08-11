package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.drug.Drug;

import java.util.List;

/**
 * Created by vladislav on 13.06.16.
 */
public interface DrugDAO {
    Drug getDrugById(int drugId) throws DaoException;

    List<Drug> extendedSearching(Drug pattern, int limit, int startFrom) throws DaoException;

    List<Drug> searchDrugs(String query, int limit, int startFrom) throws DaoException;

    List<Drug> getAllDrugs(int limit, int startFrom) throws DaoException;
    List<Drug> getDrugsByName(String name, int limit, int startFrom) throws DaoException;
    List<Drug> getDrugsByClass(String drugClass, int limit, int startFrom) throws DaoException;
    List<Drug> getDrugsByActiveSubstance(String activeSubstance, int limit, int startFrom) throws DaoException;
    void insertDrug(Drug drug) throws DaoException;
    void updateDrug(Drug drug) throws DaoException;
    void deleteDrug(int drugId) throws DaoException;
}
