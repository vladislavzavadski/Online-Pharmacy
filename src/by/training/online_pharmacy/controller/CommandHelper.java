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
    }
    public static Command getCommand(CommandName commandName){
        return commands.get(commandName);
    }
}
