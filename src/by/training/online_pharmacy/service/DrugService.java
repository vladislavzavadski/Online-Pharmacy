package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.drug.DrugClass;
import by.training.online_pharmacy.domain.drug.DrugManufacturer;
import by.training.online_pharmacy.service.exception.InvalidParameterException;

import java.io.InputStream;
import java.util.List;

/**
 * Created by vladislav on 06.08.16.
 */
public interface DrugService {
    List<Drug> getAllDrugs(int limit, int startFrom, boolean pageOverload) throws InvalidParameterException;
    List<DrugClass> getAllDrugClasses();
    List<Drug> getDrugsByClass(String className, int limit, int startFrom) throws InvalidParameterException;
    Drug getDrugDetails(int drugId) throws InvalidParameterException;

    List<Drug> searchDrugs(String query, int limit, int startFrom) throws InvalidParameterException;

    List<Drug> extendedDrugSearch(String name, String activeSubstance, String drugMaxPrice, String drugClass, String drugManufacture, String onlyInStock, String prescriptionEnable,  int limit, int startFrom) throws InvalidParameterException;

    InputStream getDrugImage(int drugId) throws InvalidParameterException;

    List<DrugManufacturer> getDrugManufactures();
}
