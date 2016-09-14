package by.training.online_pharmacy.controller;

import by.training.online_pharmacy.command.*;
import by.training.online_pharmacy.command.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vladislav on 18.07.16.
 */
public class CommandHelper {
    private static final Map<CommandName, Command> commands = new HashMap<>();
    static {
        commands.put(CommandName.USER_LOGIN, new UserLoginCommand());
        commands.put(CommandName.USER_LOGIN_VK, new UserLoginVKCommand());
        commands.put(CommandName.USER_REGISTRATION, new UserRegistrationCommand());
        commands.put(CommandName.USER_LOGIN_FB, new UserLoginFBCommand());
        commands.put(CommandName.USER_LOGIN_LI, new UserLoginLICommand());
        commands.put(CommandName.SWITCH_LOCALE, new SwitchLocaleCommand());
        commands.put(CommandName.CHECK_LOGIN, new CheckLoginCommand());
        commands.put(CommandName.UPDATE_PERSONAL_INFORMATION, new UpdatePersonalInformationCommand());
        commands.put(CommandName.UPDATE_PASSWORD, new UpdatePasswordCommand());
        commands.put(CommandName.UPDATE_CONTACTS, new UpdateContactsCommand());
        commands.put(CommandName.UPLOAD_PROFILE_IMAGE, new UploadProfileImageCommand());
        commands.put(CommandName.INIT_CONNECTION, new InitConnectionCommand());
        commands.put(CommandName.DESTROY_CONNECTION, new DestroyConnectionCommand());
        commands.put(CommandName.DELETE_USER, new DeleteUserCommand());
        commands.put(CommandName.LOG_OUT, new LogOutCommand());
        commands.put(CommandName.GET_ALL_DRUGS, new GetAllDrugsCommand());
        commands.put(CommandName.GET_DRUGS_BY_CLASS, new GetDrugsByClassCommand());
        commands.put(CommandName.GET_DRUG_DETAILS, new GetDrugDetailsCommand());
        commands.put(CommandName.SEARCH_DRUGS, new SearchDrugsCommand());
        commands.put(CommandName.EXTENDED_DRUG_SEARCH, new ExtendedSearchCommand());
        commands.put(CommandName.CREATE_ORDER, new CreateOrderCommand());
        commands.put(CommandName.CREATE_REQUEST, new CreateRequestCommand());
        commands.put(CommandName.SEND_MESSAGE, new SendMessageCommand());
        commands.put(CommandName.GET_ALL_DOCTORS, new GetAllDoctorsCommand());
        commands.put(CommandName.GET_DOCTORS_BY_SPECIALIZATION, new GetDoctorsBySpecializationCommand());
        commands.put(CommandName.SEARCH_DOCTORS, new SearchDoctorsCommand());
        commands.put(CommandName.GET_USER_DETAILS, new GetUserDetailsCommand());
        commands.put(CommandName.GET_PROFILE_IMAGE, new GetProfileImageCommand());
        commands.put(CommandName.GET_USER_IMAGE, new GetUserImageCommand());
        commands.put(CommandName.GET_DRUG_IMAGE, new GetDrugImageCommand());
        commands.put(CommandName.GET_ALL_ORDERS, new GetAllOrdersCommand());
        commands.put(CommandName.CANCEL_ORDER, new CancelOrderCommand());
        commands.put(CommandName.GET_ALL_MESSAGES, new GetAllMessagesCommand());
        commands.put(CommandName.MARK_MESSAGE, new MarkMessageAsReadedCommand());
        commands.put(CommandName.GET_MESSAGE_COUNT, new GetMessageCountCommand());
        commands.put(CommandName.GET_REQUESTS, new GetRequestsCommand());
        commands.put(CommandName.GET_PRESCRIPTIONS, new GetAllPrescriptionsCommand());
        commands.put(CommandName.REESTABLISH_ORDER, new ReestablishOrderCommand());
        commands.put(CommandName.PAY_ORDER, new PayForOrderCommand());
        commands.put(CommandName.CREATE_DRUG, new CreateDrugCommand());
        commands.put(CommandName.CHECK_CLASS, new CheckIsDrugClassExistCommand());
        commands.put(CommandName.CREATE_CLASS, new CreateDrugClassCommand());
        commands.put(CommandName.CHECK_MANUFACTURER, new CheckIsManufacturerExistCommand());
        commands.put(CommandName.CREATE_MANUFACTURER, new CreateDrugManufacturerCommand());
        commands.put(CommandName.RESERVE_CONNECTION, new ReserveConnectionCommand());
        commands.put(CommandName.UPDATE_DRUG, new UpdateDrugCommand());
        commands.put(CommandName.DELETE_DRUG, new DeleteDrugCommand());
        commands.put(CommandName.DOCTOR_REGISTRATION, new DoctorRegistrationCommand());
        commands.put(CommandName.REPLISH_BALANCE, new ReplenishBalanceCommand());
        commands.put(CommandName.CREATE_SECRET, new CreateSecretWordCommand());
        commands.put(CommandName.OPEN_SETTINGS, new OpenSettingsCommand());
        commands.put(CommandName.GET_SECRET, new GetUsersSecretQuestionCommand());
        commands.put(CommandName.REESTABLISH_ACCOUNT, new ReestablishAccountCommand());
        commands.put(CommandName.ANSWER_FOR_REQUEST, new AnswerToRequestForPrescriptionCommand());
        commands.put(CommandName.ANSWER_MESSAGE, new AnswerToMessageCommand());
        commands.put(CommandName.REQUEST_COUNT, new GetRequestCountCommand());
        commands.put(CommandName.UPDATE_DESCRIPTION, new UpdateUserDescriptionCommand());
    }
    public static Command getCommand(CommandName commandName){
        return commands.get(commandName);
    }
}
