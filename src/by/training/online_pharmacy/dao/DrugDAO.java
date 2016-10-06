package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.drug.SearchDrugsCriteria;
import by.training.online_pharmacy.domain.user.User;

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

    /**
     * Retrieve is prescription enable from storage by drug identification
     * @return <code>true</code> if prescription enable else <code>false</code>
     * @param drugId drug identification
     * @throws DaoException if fail occurs while invoke read operation*/
    boolean isPrescriptionEnable(int drugId) throws DaoException;

    /**
     * Retrieve is prescription enable by order id
     * @return <code>true</code> if prescription enable else <code>false</code>
     * @param orderId order identification
     * @throws DaoException if fail occurs while invoke read operation*/
    boolean isPrescriptionEnableByOrder(int orderId) throws DaoException;

    /**
     * Reduce drug count by canceled order
     * @param user . Object of user that cancel order
     * @param orderId order identification
     * @throws DaoException if fail occurs while invoke write operation*/
    void updateDrugCountByCanceledOrder(User user, int orderId) throws DaoException;

    /**
     * Search drugs by query. Search matches in drug name and description
     * @param query string query that user enter
     * @param limit of drugs that will be retrieved from storage
     * @param startFrom from this number drugs will be retrieved from storage
     * @return List that contains all searched drugs
     * @throws DaoException if fail occurs while invoke read operation*/
    List<Drug> searchDrugs(String query, int limit, int startFrom) throws DaoException;

    /**
     * Insert new drug into storage
     * @param drug object that represent new drug
     * @throws DaoException if fail occurs while invoke write operation*/
    int insertDrug(Drug drug) throws DaoException;

    /**
     * Update information about drug in storage
     * @param drug object that represent updated drug
     * @throws DaoException if fail occurs while invoke write operation*/
    void updateDrug(Drug drug) throws DaoException;

    /**
     * Delete drug from storage
     * @param drugId drug identification
     * @throws DaoException if fail occurs while invoke write operation*/
    void deleteDrug(int drugId) throws DaoException;

    /**
     * Retrieve drug count that in stock from storage
     * @return int that contains drug count in stock
     * @param drugId drug identification
     * @throws DaoException if fail occurs while invoke read operation*/
    int getDrugCountInStock(int drugId) throws DaoException;
}
