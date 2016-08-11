package by.training.online_pharmacy.dao.impl.database.util;

import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.impl.database.TableColumn;
import by.training.online_pharmacy.dao.impl.database.util.exception.ParameterNotFoundException;

import java.sql.SQLException;

/**
 * Created by vladislav on 10.08.16.
 */
public class DrugCountUpdater {
    private static final String UPDATE_DRUG_COUNT_QUERY = "update drugs set dr_in_stock=dr_in_stock+?  where dr_id=?";
    private static final String SET_DRUG_COUNT_QUERY = "update drugs set dr_in_stock=?  where dr_id=?";
    private DatabaseOperation databaseOperation;

    public DrugCountUpdater(DatabaseOperation databaseOperation){
        this.databaseOperation = databaseOperation;
    }

    public void changeDrugCount(int drugCount, int drugId) throws ConnectionPoolException, SQLException, ParameterNotFoundException {
        databaseOperation.init(UPDATE_DRUG_COUNT_QUERY);
        databaseOperation.setParameter(1, drugCount);
        databaseOperation.setParameter(2, drugId);
        databaseOperation.invokeWriteOperation();
    }

    public void setDrugCount(int drugCount, int drugId) throws ConnectionPoolException, SQLException, ParameterNotFoundException {
        databaseOperation.init(SET_DRUG_COUNT_QUERY);
        databaseOperation.setParameter(TableColumn.DRUG_ID, drugId);
        databaseOperation.setParameter(TableColumn.DRUG_IN_STOCK, drugCount);
        databaseOperation.invokeWriteOperation();
    }
}
