package by.training.online_pharmacy.service.util;


import by.training.online_pharmacy.domain.user.Gender;
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
    private static final String PHONE_PROPERTY = "home_phone";
    private static final String PHOTO_PROPERTY = "photo_200";
    private static final String CLIENT_ID = "5550894";
    private static final String CLIENT_SECRET = "pLVpHGMyHT6hRvZxHLr6";
    private static final String REDIRECT_URI = "http://localhost:8080/controller?command=USER_LOGIN_VK";
    private static final String SEX = "sex";
    private static final String CONTACTS = "contacts";
    private static final String RESPONSE = "response";
    private static final String NAME_CASE = "norm";
    private JSONObject result;
    private String email;
    private String userId;

    @Override
    public void authorization(String code) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put(Property.CLIENT_ID, CLIENT_ID);
        params.put(Property.CLIENT_SECRET, CLIENT_SECRET);
        params.put(Property.REDIRECT_URI, REDIRECT_URI);
        params.put(Property.CODE, code);
        String response = RequestSender.sendRequest(params, Property.GET, API_URL);

        JSONObject jsonObject = new JSONObject(response);
        try {
            email = jsonObject.getString(Property.EMAIL);

        }catch (JSONException ignored){

        }

        userId = String.valueOf(jsonObject.getLong(Property.USER_ID));

        String accessToken = jsonObject.getString(Property.ACCESS_TOKEN);
        params.clear();
        params.put(Property.USER_IDS, userId);
        params.put(Property.FIELDS, PHOTO_PROPERTY+","+CONTACTS+","+SEX);
        params.put(Property.NAME_CASE, NAME_CASE);
        params.put(Property.ACCESS_TOKEN, accessToken);

        result = new JSONObject(RequestSender.sendRequest(params, Property.GET, BASE_URL));
    }

    @Override
    public String getFirstName(){
        return getProperty(Property.FIRST_NAME_PROPERTY);
    }

    @Override
    public String getSecondName(){
        return getProperty(Property.SECOND_NAME_PROPERTY);
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
    public Gender getGender() {

        int gender = result.getJSONArray(RESPONSE).getJSONObject(0).getInt(SEX);

        switch (gender){
            case 1:{
                return Gender.FEMALE;
            }
            case 2:{
                return Gender.MALE;
            }
            default:{
                return Gender.UNKNOWN;
            }
        }

    }

    @Override
    public String getEmail(){return email;}

    private String getProperty(String property){
        return result.getJSONArray(RESPONSE).getJSONObject(0).getString(property);
    }


}
