package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.drug.DrugClass;
import by.training.online_pharmacy.service.exception.InvalidParameterException;

import java.util.List;

/**
 * Created by vladislav on 06.08.16.
 */
public interface DrugService {
    List<Drug> getAllDrugs(int limit, int startFrom) throws InvalidParameterException;
    List<DrugClass> getAllDrugClasses();
    List<Drug> getDrugsByClass(String className, int limit, int startFrom) throws InvalidParameterException;
    Drug getDrugDetails(int drugId) throws InvalidParameterException;

    List<Drug> searchDrugs(String query, int limit, int startFrom) throws InvalidParameterException;

    List<Drug> extendedDrugSearch(Drug pattern, int limit, int startFrom) throws InvalidParameterException;
}
