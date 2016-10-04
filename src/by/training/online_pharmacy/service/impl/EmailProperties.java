package by.training.online_pharmacy.service.impl;

/**
 * Created by vladislav on 28.07.16.
 */
public final class EmailProperties {
    public static final String EMAIL = "vladislav.zavadski@gmail.com";
    public static final String PASSWORD = "vlad7350720";

    private static final String TITLE_RU = "Регистрация в онлайн аптеке";
    private static final String REESTABLISH_ACCOUNT_RU = "Восстановление аккаунта. Онлайн аптека";
    private static final String REESTABLISH_BODY_RU = "Здравствуйте. Ваш аккаунт восстановлен.\nДля входа используйте\n" +
            "Логин: %s\n" +
            "Пароль: %s \n\nhttp://pharmacy.mycloud.by";
    private static final String MESSAGE_BODY_RU = "Здравствуйте, %s вы зарегистрированы в нашей онлайн аптеке.\n" +
            "Для входа используете:\n Логин: %s \nПароль: %s.\nВы можете сменить пароль после входа на персональную страницу." +
            "\n\nhttp://pharmacy.mycloud.by";

    private static final String TITLE_EN = "Registration in online pharmacy";
    private static final String REESTABLISH_ACCOUNT_EN = "Account reestablishing. Online pharmacy";

    private static final String REESTABLISH_BODY_EN = "Hello, Your account was successfully reestablished. \nTo enter" +
            " your account use: \nLogin: %s\n"+"Password: %s. \n\nhttp://pharmacy.mycloud.by";

    private static final String MESSAGE_BODY_EN = "Hello, %s you was successfully registered in pharmacy. \nTo enter" +
            " your account use: \nLogin: %s\n"+"Password: %s. \n\nhttp://pharmacy.mycloud.by";

    private static final String RUSSIAN_LOCALE = "RU";

    public static String getRegisterTitle(String locale){

        switch (locale){
            case RUSSIAN_LOCALE:{
                return TITLE_RU;
            }
            default:{
                return TITLE_EN;
            }
        }
    }

    public static String getReestablishTitle(String locale){

        switch (locale){

            case RUSSIAN_LOCALE:{
                return REESTABLISH_ACCOUNT_RU;
            }
            default:{
                return REESTABLISH_ACCOUNT_EN;
            }
        }
    }

    public static String getRegistrationBody(String locale){

        switch (locale){

            case RUSSIAN_LOCALE: {
                return MESSAGE_BODY_RU;
            }

            default:{
                return MESSAGE_BODY_EN;
            }
        }
    }

    public static String getReestablishBody(String locale){

        switch (locale){

            case RUSSIAN_LOCALE:{
                return REESTABLISH_BODY_RU;
            }

            default:{
                return REESTABLISH_BODY_EN;
            }
        }
    }



}
