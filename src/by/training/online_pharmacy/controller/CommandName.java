package by.training.online_pharmacy.controller;

/**
 * Created by vladislav on 18.07.16.
 */
public enum CommandName {
    USER_LOGIN, USER_REGISTRATION,
    USER_LOGIN_VK, USER_LOGIN_FB, USER_LOGIN_LI, SWITCH_LOCALE, CHECK_LOGIN, UPDATE_PERSONAL_INFORMATION, UPDATE_PASSWORD
    ,UPDATE_CONTACTS, UPLOAD_PROFILE_IMAGE, INIT_CONNECTION, DESTROY_CONNECTION, LOG_OUT, DELETE_USER, GET_ALL_DRUGS,
    GET_DRUGS_BY_CLASS, GET_DRUG_DETAILS, SEARCH_DRUGS
    ,EXTENDED_DRUG_SEARCH, CREATE_ORDER, CREATE_REQUEST, SEND_MESSAGE, GET_DOCTORS,
    SEARCH_DOCTORS, GET_USER_DETAILS, GET_PROFILE_IMAGE, GET_USER_IMAGE, GET_DRUG_IMAGE, GET_ALL_ORDERS, CANCEL_ORDER, GET_ALL_MESSAGES,
    MARK_MESSAGE, GET_MESSAGE_COUNT, GET_REQUESTS, GET_PRESCRIPTIONS, REESTABLISH_ORDER, PAY_ORDER, CREATE_DRUG, CHECK_CLASS, CREATE_CLASS
    ,CHECK_MANUFACTURER, CREATE_MANUFACTURER, RESERVE_CONNECTION, UPDATE_DRUG, DELETE_DRUG, DOCTOR_REGISTRATION
    ,REPLISH_BALANCE, CREATE_SECRET, OPEN_SETTINGS, GET_SECRET, REESTABLISH_ACCOUNT, ANSWER_FOR_REQUEST, ANSWER_MESSAGE, REQUEST_COUNT,
    UPDATE_DESCRIPTION, COMPLETE_ORDER, GET_ORDER_BY_ID
}
