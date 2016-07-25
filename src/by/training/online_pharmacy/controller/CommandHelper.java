package by.training.online_pharmacy.controller;

import by.training.online_pharmacy.command.*;
import by.training.online_pharmacy.command.impl.*;

/**
 * Created by vladislav on 18.07.16.
 */
public class CommandHelper {

    public static Command getCommand(CommandName commandName){
        switch (commandName){
            case USER_LOGIN:{
                return new UserLoginCommand();
            }
            case USER_LOGIN_VK:{
                return new UserLoginVKCommand();
            }
            case USER_REGISTRATION:{
                return new UserRegistrationCommand();
            }
            case USER_LOGIN_FB:{
                return new UserLoginFBCommand();
            }
            case USER_LOGIN_LI:{
                return new UserLoginLICommand();
            }
            case GET_PROFILE_IMAGE:{
                return new GetProfileImageCommand();
            }
            case SWITCH_LOCALE:{
                return new SwitchLocaleCommand();
            }
            default:{
                return null;
            }
        }
    }
}
