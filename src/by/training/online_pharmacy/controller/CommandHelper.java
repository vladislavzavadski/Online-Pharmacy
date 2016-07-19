package by.training.online_pharmacy.controller;

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
            case USER_REGISTRATION_VK:{
                return new UserRegistrationVKCommand();
            }
            default:{
                return null;
            }
        }
    }
}
