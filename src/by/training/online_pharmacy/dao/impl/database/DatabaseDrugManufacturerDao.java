package by.training.online_pharmacy.dao.impl.database;


import by.training.online_pharmacy.dao.DrugManufacturerDAO;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.domain.drug.DrugManufacturer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladislav on 19.06.16.
 */
public class DatabaseDrugManufacturerDao implements DrugManufacturerDAO {
    private static final String INSERT_MANUFACTURER_QUERY = "INSERT INTO drugs_manufactures (dm_name, dm_country, dm_description) VALUES (?, ?, ?);";
    private static final String GET_MANUFACTURES_NAMES = "select dm_name, dm_country, dm_description from drugs_manufactures order by dm_name;";
    private static final String IS_MANUFACTURE_EXIST = "select dm_name from drugs_manufactures where dm_name=? and dm_country=?;";

    @Override
    public boolean isManufactureExist(DrugManufacturer drugManufacturer) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(IS_MANUFACTURE_EXIST)){
            databaseOperation.setParameter(1, drugManufacturer.getName());
            databaseOperation.setParameter(2, drugManufacturer.getCountry());
            ResultSet resultSet = databaseOperation.invokeReadOperation();

            return resultSet.next();

        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not check is manufacturer exist", e);

        }
    }

    @Override
    public void insertDrugManufacturer(DrugManufacturer drugManufacturer) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_MANUFACTURER_QUERY)){
            databaseOperation.setParameter(1, drugManufacturer.getName());
            databaseOperation.setParameter(2, drugManufacturer.getCountry());
            databaseOperation.setParameter(3, drugManufacturer.getDescription());

            databaseOperation.invokeWriteOperation();
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not insert new drug manufacturer", e);

        }
    }

    @Override
    public List<DrugManufacturer> getDrugManufactures() throws DaoException {
        List<DrugManufacturer> result = new ArrayList<>();

        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_MANUFACTURES_NAMES);){

            ResultSet resultSet = databaseOperation.invokeReadOperation();

            while (resultSet.next()){
                DrugManufacturer drugManufacturer = new DrugManufacturer();
                drugManufacturer.setName(resultSet.getString(TableColumn.DRUG_MANUFACTURE_NAME));
                drugManufacturer.setCountry(resultSet.getString(TableColumn.DRUG_MANUFACTURE_COUNTRY));
                drugManufacturer.setDescription(resultSet.getString(TableColumn.DRUG_MANUFACTURE_DESCRIPTION));

                result.add(drugManufacturer);
            }

            return result;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not load drug manufactures from database", e);

        }
    }
}
