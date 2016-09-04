package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.*;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.dao.exception.EntityNotFoundException;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.drug.DrugClass;
import by.training.online_pharmacy.domain.drug.DrugManufacturer;
import by.training.online_pharmacy.domain.drug.SearchDrugsCriteria;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserRole;
import by.training.online_pharmacy.service.DrugService;
import by.training.online_pharmacy.service.Encoding;
import by.training.online_pharmacy.service.exception.*;
import by.training.online_pharmacy.service.util.ImageConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Part;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Created by vladislav on 06.08.16.
 */
public class DrugServiceImpl implements DrugService {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public List<Drug> getAllDrugs(int limit, int startFrom) throws InvalidParameterException {
        if(limit<=0){
            throw new InvalidParameterException("Invalid parameter limit. Limit can be >0");
        }
        if(startFrom<0){
            throw new InvalidParameterException("Invalid parameter startFrom startFrom can be >0");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        DrugDAO drugDAO = daoFactory.getDrugDAO();
        try {
            List<Drug> result = drugDAO.getAllDrugs(limit, startFrom);
            return result;
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to get drugs", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public void createDrug(User user, Drug drug, Part part) throws InvalidParameterException, InvalidUserStatusException, SpecializationNotFoundException, NotFoundException, InvalidContentException {
        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(user.getUserRole()!= UserRole.PHARMACIST){
            throw new InvalidUserStatusException("Only pharmacyst can create new drugs");
        }
        if(drug==null){
            throw new InvalidParameterException("Parameter drug is invalid");
        }
        if(drug.getName()==null||drug.getName().isEmpty()){
            throw new InvalidParameterException("Parameter drug name is invalid");
        }
        if(drug.getDrugManufacturer()==null||drug.getDrugManufacturer().getName()==null||drug.getDrugManufacturer().getName().isEmpty()||drug.getDrugManufacturer().getCountry()==null||drug.getDrugManufacturer().getCountry().isEmpty()){
            throw new InvalidParameterException("Parameter drug manufacture is invalid");
        }
        if(drug.getDrugClass()==null||drug.getDrugClass().getName()==null||drug.getDrugClass().getName().isEmpty()){
            throw new InvalidParameterException("Parameter drug class is invalid");
        }
        if(drug.getType()==null){
            throw new InvalidParameterException("Parameter drug type is invalid");
        }
        if(drug.getActiveSubstance()==null||drug.getActiveSubstance().isEmpty()){
            throw new InvalidParameterException("Parameter active substance is invalid");
        }
        if(drug.getPrice()<=0.0){
            throw new InvalidParameterException("Parameter price is invalid");
        }
        if(drug.getDosages()==null||drug.getDosages().isEmpty()){
            throw new InvalidParameterException("Parameter dosages is invalid");
        }
        if(drug.getDrugsInStock()<0){
            throw new InvalidParameterException("Parameter drugs in stock is invalid");
        }
        if(drug.getDoctorSpecialization()==null||drug.getDoctorSpecialization().isEmpty()){
            throw new InvalidParameterException("Parameter doctor specialization is invalid");
        }
        try {
            drug.setName(new String(drug.getName().getBytes(Encoding.ISO_8859), Encoding.UTF8));
            DrugClass drugClass = drug.getDrugClass();
            drugClass.setName(new String(drugClass.getName().getBytes(Encoding.ISO_8859), Encoding.UTF8));
            drug.setDoctorSpecialization(new String(drug.getDoctorSpecialization().getBytes(Encoding.ISO_8859), Encoding.UTF8));
            drug.setActiveSubstance(new String(drug.getActiveSubstance().getBytes(Encoding.ISO_8859), Encoding.UTF8));
            if(drug.getDescription()!=null&&!drug.getDescription().isEmpty()){
                drug.setDescription(new String(drug.getDescription().getBytes(Encoding.ISO_8859), Encoding.UTF8));
            }
            InputStream inputStream = null;
            if(part!=null&&part.getSize()>0) {
               inputStream = part.getInputStream();
               String content = URLConnection.guessContentTypeFromStream(inputStream);
               if (content == null || !content.startsWith("image/")) {
                  throw new InvalidContentException("This file is not an image");
               }

            }
            String drugPathToImage = ImageConstant.DRUG_IMAGES+drug.getName()+drug.getDrugManufacturer().getName()+drug.getDrugManufacturer().getCountry()+drug.getActiveSubstance();
            drug.setPathToImage(drugPathToImage);
            DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
            UserDescriptionDAO userDescriptionDAO = daoFactory.getUserDescriptionDao();
            boolean isSpecializationExist;
            isSpecializationExist = userDescriptionDAO.isSpecializationExist(drug.getDoctorSpecialization());
            if(!isSpecializationExist){
                throw new SpecializationNotFoundException("Specialization with name="+drug.getDoctorSpecialization()+" was not found");
            }
            DrugDAO drugDAO = daoFactory.getDrugDAO();
            drugDAO.insertDrug(drug);
            if(part!=null&&part.getSize()>0) {
                File file = new File(drug.getPathToImage());
                Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            logger.error("Something went wrong when trying to change encoding", e);
            throw new InternalServerException(e);
        }catch (EntityDeletedException e){
            throw new NotFoundException(e);
        }catch (DaoException e) {
            e.printStackTrace();
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
    public List<Drug> extendedDrugSearch(SearchDrugsCriteria searchDrugsCriteria, int limit, int startFrom) throws InvalidParameterException {
        if(limit<=0){
            throw new InvalidParameterException("Invalid parameter limit. Limit can be >0");
        }
        if(startFrom<0){
            throw new InvalidParameterException("Invalid parameter startFrom startFrom can be >0");
        }
        if(searchDrugsCriteria==null){
            throw new InvalidParameterException("Invalid parameter search criteria");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        DrugDAO drugDAO = daoFactory.getDrugDAO();
        try {
            List<Drug> drugs = drugDAO.extendedSearching(searchDrugsCriteria, startFrom, limit);
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
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to load drug image", e);
            throw new InternalServerException(e);
        } catch (FileNotFoundException e) {
            try {
                return new FileInputStream(ImageConstant.DEFAULT_DRUG_IMAGE);
            } catch (FileNotFoundException e1) {
                logger.error("Something went wrong when trying to load drug image", e);
                throw new InternalServerException(e);
            }
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

    @Override
    public boolean isDrugClassExist(String className) throws InvalidParameterException {
        if(className==null||className.isEmpty()){
            throw new InvalidParameterException("Parameter className is invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        DrugClassDAO drugClassDAO = daoFactory.getDrugClassDAO();
        try {
            return drugClassDAO.isDrugClassExist(className);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to check is drug class exist", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public void createDrugClass(User user, DrugClass drugClass) throws InvalidParameterException, InvalidUserStatusException {
        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(user.getUserRole()!=UserRole.PHARMACIST){
            throw new InvalidUserStatusException("Only pharmacist can create new drug class");
        }
        if(drugClass==null){
            throw new InvalidParameterException("Parameter drug class is invalid");
        }
        if(drugClass.getName()==null||drugClass.getName().isEmpty()){
            throw new InvalidParameterException("Parameter class name is invalid");
        }
        if(drugClass.getDescription()==null||drugClass.getDescription().isEmpty()){
            throw new InvalidParameterException("Parameter class description is invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        DrugClassDAO drugClassDAO = daoFactory.getDrugClassDAO();
        try {
            drugClassDAO.insertDrugClass(drugClass);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to create new drug class");
            throw new InternalServerException(e);
        }
    }

    @Override
    public boolean isManufacturerExist(DrugManufacturer drugManufacturer) throws InvalidParameterException {
        if(drugManufacturer==null){
            throw new InvalidParameterException("Parameter drug manufacturer is invalid");
        }
        if(drugManufacturer.getName()==null||drugManufacturer.getName().isEmpty()){
            throw new InvalidParameterException("Parameter manufacturer name is invalid");
        }
        if(drugManufacturer.getCountry()==null||drugManufacturer.getCountry().isEmpty()){
            throw new InvalidParameterException("Parameter manufacturer country is invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        DrugManufacturerDAO drugManufacturerDAO = daoFactory.getDrugManufacturerDAO();
        try {
            return drugManufacturerDAO.isManufactureExist(drugManufacturer);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to check is manufacturer exist", e);
            throw new InternalServerException(e);
        }

    }

    @Override
    public void createDrugManufacturer(User user, DrugManufacturer drugManufacturer) throws InvalidUserStatusException, InvalidParameterException {
        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(user.getUserRole()!=UserRole.PHARMACIST){
            throw new InvalidUserStatusException("Only pharmacist can create new drug class");
        }
        if(drugManufacturer==null){
            throw new InvalidParameterException("Parameter drug manufacturer is invalid");
        }
        if(drugManufacturer.getName()==null||drugManufacturer.getName().isEmpty()){
            throw new InvalidParameterException("Parameter manufacturer name is invalid");
        }
        if(drugManufacturer.getCountry()==null||drugManufacturer.getCountry().isEmpty()){
            throw new InvalidParameterException("Parameter manufacturer country is invalid");
        }
        if(drugManufacturer.getDescription()==null||drugManufacturer.getDescription().isEmpty()){
            throw new InvalidParameterException("Parameter manufacturer description os invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        DrugManufacturerDAO drugManufacturerDAO = daoFactory.getDrugManufacturerDAO();
        try {
            drugManufacturerDAO.insertDrugManufacturer(drugManufacturer);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to create new manufacturer", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public void updateDrug(User user, Drug drug, Part part) throws InvalidParameterException, InvalidUserStatusException, DrugNotFoundException, NotFoundException, InvalidContentException, SpecializationNotFoundException {
        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(user.getUserRole()!= UserRole.PHARMACIST){
            throw new InvalidUserStatusException("Only pharmacyst can create new drugs");
        }
        if(drug==null||drug.getId()<=0){
            throw new InvalidParameterException("Parameter drug is invalid");
        }
        if(drug.getName()==null||drug.getName().isEmpty()){
            throw new InvalidParameterException("Parameter drug name is invalid");
        }
        if(drug.getDrugManufacturer()==null||drug.getDrugManufacturer().getName()==null||drug.getDrugManufacturer().getName().isEmpty()||drug.getDrugManufacturer().getCountry()==null||drug.getDrugManufacturer().getCountry().isEmpty()){
            throw new InvalidParameterException("Parameter drug manufacture is invalid");
        }
        if(drug.getDrugClass()==null||drug.getDrugClass().getName()==null||drug.getDrugClass().getName().isEmpty()){
            throw new InvalidParameterException("Parameter drug class is invalid");
        }
        if(drug.getType()==null){
            throw new InvalidParameterException("Parameter drug type is invalid");
        }
        if(drug.getActiveSubstance()==null||drug.getActiveSubstance().isEmpty()){
            throw new InvalidParameterException("Parameter active substance is invalid");
        }
        if(drug.getPrice()<=0.0){
            throw new InvalidParameterException("Parameter price is invalid");
        }
        if(drug.getDosages()==null||drug.getDosages().isEmpty()){
            throw new InvalidParameterException("Parameter dosages is invalid");
        }
        if(drug.getDrugsInStock()<0){
            throw new InvalidParameterException("Parameter drugs in stock is invalid");
        }
        if(drug.getDoctorSpecialization()==null||drug.getDoctorSpecialization().isEmpty()){
            throw new InvalidParameterException("Parameter doctor specialization is invalid");
        }
        try {
            drug.setName(new String(drug.getName().getBytes(Encoding.ISO_8859), Encoding.UTF8));
            DrugClass drugClass = drug.getDrugClass();
            drugClass.setName(new String(drugClass.getName().getBytes(Encoding.ISO_8859), Encoding.UTF8));
            drug.setDoctorSpecialization(new String(drug.getDoctorSpecialization().getBytes(Encoding.ISO_8859), Encoding.UTF8));
            drug.setActiveSubstance(new String(drug.getActiveSubstance().getBytes(Encoding.ISO_8859), Encoding.UTF8));
            if(drug.getDescription()!=null&&!drug.getDescription().isEmpty()){
                drug.setDescription(new String(drug.getDescription().getBytes(Encoding.ISO_8859), Encoding.UTF8));
            }
            InputStream inputStream = null;
            if(part!=null&&part.getSize()>0) {
                inputStream = part.getInputStream();
                String content = URLConnection.guessContentTypeFromStream(inputStream);
                if (content == null || !content.startsWith("image/")) {
                    throw new InvalidContentException("This file is not an image");
                }
            }
            DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
            UserDescriptionDAO userDescriptionDAO = daoFactory.getUserDescriptionDao();
            boolean isSpecializationExist;
            isSpecializationExist = userDescriptionDAO.isSpecializationExist(drug.getDoctorSpecialization());
            if(!isSpecializationExist){
                throw new SpecializationNotFoundException("Specialization with name="+drug.getDoctorSpecialization()+" was not found");
            }
            DrugDAO drugDAO = daoFactory.getDrugDAO();
            drugDAO.updateDrug(drug);
            if(part!=null&&part.getSize()>0) {
                File file = new File(ImageConstant.DRUG_IMAGES+drug.getName()+drug.getDrugManufacturer().getName()+drug.getDrugManufacturer().getCountry()+drug.getActiveSubstance());
                Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (EntityNotFoundException e) {
            throw new DrugNotFoundException("This drug was deleted or does't exist", e);
        } catch (EntityDeletedException e){
            throw new NotFoundException("Drug class or manufacturer was not found",e);
        } catch(DaoException|IOException e) {
            logger.error("Something went wrong when trying to update drug", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public void deleteDrug(User user, int drugId) throws InvalidParameterException, InvalidUserStatusException {
        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(user.getUserRole()!=UserRole.PHARMACIST){
            throw new InvalidUserStatusException("Only pharmacist can delete drugs");
        }
        if(drugId<=0){
            throw new InvalidParameterException("Parameter drugId is invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        DrugDAO drugDAO = daoFactory.getDrugDAO();
        try {
            drugDAO.deleteDrug(drugId);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to delete drug", e);
            throw new InternalServerException(e);
        }
    }
}
