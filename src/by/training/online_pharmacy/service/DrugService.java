package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.drug.DrugClass;
import by.training.online_pharmacy.domain.drug.DrugManufacturer;
import by.training.online_pharmacy.domain.drug.SearchDrugsCriteria;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.exception.*;

import javax.servlet.http.Part;
import java.io.InputStream;
import java.util.List;

/**
 * Created by vladislav on 06.08.16.
 */
public interface DrugService {

    void createDrug(User user, Drug drug, Part part) throws InvalidParameterException, InvalidUserStatusException, SpecializationNotFoundException,  InvalidContentException;

    List<DrugClass> getAllDrugClasses();

    List<Drug> getDrugsByClass(String className, int limit, int startFrom) throws InvalidParameterException;

    Drug getDrugDetails(int drugId) throws InvalidParameterException;

    List<Drug> searchDrugs(String query, int limit, int startFrom) throws InvalidParameterException;

    List<Drug> extendedDrugSearch(SearchDrugsCriteria searchDrugsCriteria, int limit, int startFrom) throws InvalidParameterException;

    InputStream getDrugImage(int drugId) throws InvalidParameterException;

    List<DrugManufacturer> getDrugManufactures();

    boolean isDrugClassExist(String className) throws InvalidParameterException;

    void createDrugClass(User user, DrugClass drugClass) throws InvalidParameterException, InvalidUserStatusException;

    boolean isManufacturerExist(DrugManufacturer drugManufacturer) throws InvalidParameterException;

    void createDrugManufacturer(User user, DrugManufacturer drugManufacturer) throws InvalidUserStatusException, InvalidParameterException;

    void updateDrug(User user, Drug drug, Part part) throws InvalidParameterException, InvalidUserStatusException, DrugNotFoundException, InvalidContentException, SpecializationNotFoundException;

    void deleteDrug(User user, int drugId) throws InvalidParameterException, InvalidUserStatusException;
}
