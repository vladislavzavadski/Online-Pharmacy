package by.training.online_pharmacy.service.util;


import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * Created by vladislav on 19.07.16.
 */
public class VkApi {
    private static final String BASE_URL = "https://api.vk.com/method/users.get?";
    private static final String FIRST_NAME_PROPERTY = "first_name";
    private static final String SECOND_NAME_PROPERTY = "second_name";
    private static final String ID_PROPERTY = "id";
    private static final String PHONE_PROPERTY = "home_phone";
    private static final String PHOTO_PROPERTY = "photo_max_orig";
    private String result;

    public void authorization(Map<String, String> params) throws IOException {
        result = RequestSender.sendRequest(params, null, "GET", BASE_URL);
    }

    public String getUserFirstName(){
        return getProperty(FIRST_NAME_PROPERTY);
    }

    public String getUserSecondName(){
        return getProperty(SECOND_NAME_PROPERTY);
    }

    public String getUserId(){
        return getProperty(ID_PROPERTY);
    }

    public String getUserPhone(){
        return getProperty(PHONE_PROPERTY);
    }

    public String getUserPhoto(){
        return getProperty(PHOTO_PROPERTY);
    }

    private String getProperty(String property){
        return new JSONObject(result).getJSONObject("response").getString(property);
    }


}
