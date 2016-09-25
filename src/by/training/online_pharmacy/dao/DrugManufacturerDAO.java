package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.drug.DrugManufacturer;

import java.util.List;

/**
 * Created by vladislav on 19.06.16.
 */
public interface DrugManufacturerDAO {

    /**
     * Check is drug manufacturer exist in storage
     * @param drugManufacturer object that represent drug manufacturer
     * @return <code>true</code> if manufacturer exist and <code>false</code> if manufacturer not exist
     * @throws DaoException if fail occurs while invoke read operation*/
    boolean isManufactureExist(DrugManufacturer drugManufacturer) throws DaoException;

    /**
     * Insert new drug manufacturer to storage
     * @param drugManufacturer object that represent new drug manufacturer
     * @throws DaoException if fail occurs while invoke write operation*/
    void insertDrugManufacturer(DrugManufacturer drugManufacturer) throws DaoException;

    /**
     * Retrieve all drug manufacturers from storage
     * @return List that contains all drug manufactures from storage
     * @throws DaoException if fail occurs while invoke read operation*/
    List<DrugManufacturer> getDrugManufactures() throws DaoException;
}
