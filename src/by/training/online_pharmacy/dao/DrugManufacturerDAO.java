package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.drug.DrugManufacturer;

import java.util.List;

/**
 * Created by vladislav on 19.06.16.
 */
public interface DrugManufacturerDAO {

    boolean isManufactureExist(DrugManufacturer drugManufacturer) throws DaoException;

    void insertDrugManufacturer(DrugManufacturer drugManufacturer) throws DaoException;

    List<DrugManufacturer> getDrugManufactures() throws DaoException;
}
