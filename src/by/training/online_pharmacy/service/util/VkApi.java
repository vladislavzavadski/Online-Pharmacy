package by.training.online_pharmacy.service.util;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vladislav on 19.07.16.
 */
public class VkApi implements Api {
    private static final String BASE_URL = "https://api.vk.com/method/users.get?";
    private static final String API_URL = "https://oauth.vk.com/access_token?";
    private static final String FIRST_NAME_PROPERTY = "first_name";
    private static final String SECOND_NAME_PROPERTY = "last_name";
    private static final String PHONE_PROPERTY = "home_phone";
    private static final String PHOTO_PROPERTY = "photo_200";
    private String result;
    private String email;
    private String userId;

    @Override
    public void authorization(String code) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", "5550894");
        params.put("client_secret", "pLVpHGMyHT6hRvZxHLr6");
        params.put("redirect_uri", "http://localhost:8080/controller?command=USER_LOGIN_VK");
        params.put("code", code);
        String response = RequestSender.sendRequest(params, null, "GET", API_URL);
        JSONObject jsonObject = new JSONObject(response);
        try {
            email = jsonObject.getString("email");
        }catch (JSONException ignored){

        }
        userId = String.valueOf(jsonObject.getLong("user_id"));
        String accessToken = jsonObject.getString("access_token");
        params.clear();
        params.put("user_ids", userId);
        params.put("fields", "photo_200,contacts");
        params.put("name_case", "norm");
        params.put("access_token", accessToken);
        result = RequestSender.sendRequest(params, null, "GET", BASE_URL);
    }

    @Override
    public String getFirstName(){
        return getProperty(FIRST_NAME_PROPERTY);
    }

    @Override
    public String getSecondName(){
        return getProperty(SECOND_NAME_PROPERTY);
    }

    @Override
    public String getId(){ return userId; }

    @Override
    public String getPhone(){
        return getProperty(PHONE_PROPERTY);
    }

    @Override
    public String getImage(){
        return getProperty(PHOTO_PROPERTY);
    }

    @Override
    public String getEmail(){return email;}

    private String getProperty(String property){
        return new JSONObject(result).getJSONArray("response").getJSONObject(0).getString(property);
    }


}
