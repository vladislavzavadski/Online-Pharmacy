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

    /**
     *Reduce drug count in drug storage by reestablished order
     * @param user contains description of user, that reestablish order
     * @param orderId identification of reestablished order
     * @throws DaoException if fail occurs while invoke write operation*/
    void reduceDrugCountByReestablishedOrder(User user, int orderId) throws DaoException;

    /**
     * Reduce drug count by new order
     * @param drugCount number of drugs that was orders
     * @param drugId identification of ordered drug
     * @throws DaoException if fail occurs while invoke write operation*/
    void reduceDrugCountByNewOrder(int drugCount, int drugId) throws DaoException;

    /**
     * Retrieve an image from drug storage
     * @param drugId identification of drug
     * @return InputStream that contains drug image
     * @throws DaoException if fail occurs while invoke read operation
     * @throws FileNotFoundException if file path to that was retrieved not found*/
    InputStream getDrugImage(int drugId) throws DaoException, FileNotFoundException;

    /**
     * Retrieve full information about drug from storage
     * @param drugId identification of drug
     * @return Drug object that contains full information about drug and <code>null</code> if drug not found
     * @throws DaoException if fail occurs while invoke read operation*/
    Drug getDrugById(int drugId) throws DaoException;

    /**
     * Retrieve list of drugs by criteria
     * @return List that contains drugs from storage
     * @param searchDrugsCriteria that contains criteria about searched drugs
     * @param startFrom parameter that contains number of cartage, from that cartage entities will retrieved from storage
     * @param limit number that contains count of drugs that will retrieved
     * @throws DaoException if fail occurs while invoke read operation*/
    List<Drug> extendedSearching(SearchDrugsCriteria searchDrugsCriteria, int startFrom, int limit) throws DaoException;

    boolean isPrescriptionEnable(int drugId) throws DaoException, NotFoundException;

    boolean isPrescriptionEnableByOrder(int orderId) throws DaoException;

    void updateDrugCountByCanceledOrder(User user, int orderId) throws DaoException;

    List<Drug> searchDrugs(String query, int limit, int startFrom) throws DaoException;

    List<Drug> getDrugsByClass(String drugClass, int limit, int startFrom) throws DaoException;

    void insertDrug(Drug drug) throws DaoException;

    void updateDrug(Drug drug) throws DaoException;

    void deleteDrug(int drugId) throws DaoException;

    int getDrugCountInStock(int drugId) throws DaoException;
}
