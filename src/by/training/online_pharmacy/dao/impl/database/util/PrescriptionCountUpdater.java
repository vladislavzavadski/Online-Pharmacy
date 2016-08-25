package by.training.online_pharmacy.dao.impl.database.util;


import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.domain.user.RegistrationType;

import java.sql.SQLException;

/**
 * Created by vladislav on 11.08.16.
 */
public class PrescriptionCountUpdater {
    private static final String UPDATE_PRESCRIPTION_DRUGS_QUERY = "update prescriptions set pr_drug_count=pr_drug_count-? where pr_client_login=? and pr_login_via=? and pr_drug_id=? and pr_drug_dosage=? and pr_expiration_date>=curdate();";
    private DatabaseOperation databaseOperation;

    public PrescriptionCountUpdater(DatabaseOperation databaseOperation){
        this.databaseOperation = databaseOperation;
    }

    public int changeDrugCount(int drugCount, String clientLogin, RegistrationType registrationType, int drugId, short drugDosage) throws ConnectionPoolException, SQLException {
        databaseOperation.init(UPDATE_PRESCRIPTION_DRUGS_QUERY);
        databaseOperation.setParameter(1, drugCount);
        databaseOperation.setParameter(2, clientLogin);
        databaseOperation.setParameter(3, registrationType.toString().toLowerCase());
        databaseOperation.setParameter(4, drugId);
        databaseOperation.setParameter(5, drugDosage);
        return databaseOperation.invokeWriteOperation();
    }
}
