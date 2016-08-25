package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.DrugClassDAO;
import by.training.online_pharmacy.dao.DrugDAO;
import by.training.online_pharmacy.dao.DrugManufacturerDAO;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.drug.DrugClass;
import by.training.online_pharmacy.domain.drug.DrugManufacturer;
import by.training.online_pharmacy.service.DrugService;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.util.ImageConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by vladislav on 06.08.16.
 */
public class DrugServiceImpl implements DrugService {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public List<Drug> getAllDrugs(int limit, int startFrom, boolean pageOverload) throws InvalidParameterException {
        if(limit<=0){
            throw new InvalidParameterException("Invalid parameter limit. Limit can be >0");
        }
        if(startFrom<0){
            throw new InvalidParameterException("Invalid parameter startFrom startFrom can be >0");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        DrugDAO drugDAO = daoFactory.getDrugDAO();
        try {
            List<Drug> result = drugDAO.getAllDrugs(limit, startFrom, pageOverload);
            return result;
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to get drugs", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public List<DrugClass> getAllDrugClasses() {
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        DrugClassDAO drugClassDAO = daoFactory.getDrugClassDAO();
        try {
            return drugClassDAO.getAllDrugClasses();
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to get drug classes", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public List<Drug> getDrugsByClass(String className, int limit, int startFrom) throws InvalidParameterException {
        if(className==null||className.isEmpty()){
            throw new InvalidParameterException("Parameter class name is invalid");
        }
        if(limit<=0){
            throw new InvalidParameterException("Invalid parameter limit. Limit can be >0");
        }
        if(startFrom<0){
            throw new InvalidParameterException("Invalid parameter startFrom startFrom can be >0");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        DrugDAO drugDAO = daoFactory.getDrugDAO();
        try {
            return drugDAO.getDrugsByClass(className, limit, startFrom);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to get drugs by class", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public Drug getDrugDetails(int drugId) throws InvalidParameterException {
        if(drugId<0){
            throw new InvalidParameterException("Drug id must be positive number");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        DrugDAO drugDAO = daoFactory.getDrugDAO();
        try {
            Drug drug = drugDAO.getDrugById(drugId);
            if(drug==null){
                logger.error("Something went wrong when trying to get user with id="+drugId+"result is null");
                throw new InternalServerException("Something went wrong when trying to get user with id="+drugId+" result is null");
            }
            return drug;
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to get drug by id", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public List<Drug> searchDrugs(String query, int limit, int startFrom) throws InvalidParameterException {
        if(limit<=0){
            throw new InvalidParameterException("Invalid parameter limit. Limit can be >0");
        }
        if(startFrom<0){
            throw new InvalidParameterException("Invalid parameter startFrom startFrom can be >0");
        }
        if(query==null||query.length()==0){
            throw new InvalidParameterException("Parameter params is invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        DrugDAO drugDAO = daoFactory.getDrugDAO();
        try {
            List<Drug> drugs = drugDAO.searchDrugs(query, limit, startFrom);
            return drugs;
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to search drugs", e);
            throw new InvalidParameterException(e);
        }
    }

    @Override
    public List<Drug> extendedDrugSearch(String name, String activeSubstance, String drugMaxPrice, String drugClass, String drugManufacture, String onlyInStock, String prescriptionEnable,  int limit, int startFrom) throws InvalidParameterException {
        if(limit<=0){
            throw new InvalidParameterException("Invalid parameter limit. Limit can be >0");
        }
        if(startFrom<0){
            throw new InvalidParameterException("Invalid parameter startFrom startFrom can be >0");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        DrugDAO drugDAO = daoFactory.getDrugDAO();
        try {
            List<Drug> drugs = drugDAO.extendedSearching(name, activeSubstance, drugMaxPrice, drugClass, drugManufacture, onlyInStock, prescriptionEnable, limit, startFrom);
            return drugs;
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to extended search", e);
            throw new InternalServerException(e);
        }

    }

    @Override
    public InputStream getDrugImage(int drugId) throws InvalidParameterException {
        if(drugId<0){
            throw new InvalidParameterException("Parameter drug id is invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        DrugDAO drugDAO = daoFactory.getDrugDAO();
        try {
            InputStream inputStream = drugDAO.getDrugImage(drugId);
            if(inputStream==null){
                return new FileInputStream(ImageConstant.DEFAULT_DRUG_IMAGE);
            }
            return inputStream;
        } catch (DaoException|FileNotFoundException e) {
            logger.error("Something went wrong when trying to load drug image", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public List<DrugManufacturer> getDrugManufactures(){
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        DrugManufacturerDAO drugManufacturerDAO = daoFactory.getDrugManufacturerDAO();
        try {
            return drugManufacturerDAO.getDrugManufactures();
        } catch (DaoException e) {
            throw new InternalServerException(e);
        }
    }
}
