package by.training.online_pharmacy.service.util;

import by.training.online_pharmacy.domain.user.Gender;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vladislav on 21.07.16.
 */
public class FacebookApi implements Api {
    private static final String API_URL = "https://graph.facebook.com/me?";
    private static final String API_TOKEN_URI = "https://graph.facebook.com/v2.3/oauth/access_token?";
    private static final String CLIENT_ID = "1472508072774891";
    private static final String CLIENT_SECRET = "feaea6b55522d84a2efcae4e9a1728c8";
    private static final String REDIRECT_URI = "http://localhost:8080/controller?command=USER_LOGIN_FB";
    private static final String PICTURE = "picture";
    private static final String DATA = "data";
    private static final String URL = "url";
    private static final String QUERY_PICTURE = "picture.width(400)";
    public JSONObject result;

    @Override
    public void authorization (String code) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put(Property.CLIENT_ID, CLIENT_ID);
        params.put(Property.CLIENT_SECRET, CLIENT_SECRET);
        params.put(Property.CODE, code);
        params.put(Property.REDIRECT_URI, REDIRECT_URI);
        String response = RequestSender.sendRequest(params, Property.GET, API_TOKEN_URI);
        String accessToken = new JSONObject(response).getString(Property.ACCESS_TOKEN);
        params.put(Property.FIELDS, Property.FIRST_NAME_PROPERTY+","+Property.SECOND_NAME_PROPERTY+","+Property.EMAIL+","+QUERY_PICTURE+","+Property.GENDER);
        params.put(Property.ACCESS_TOKEN, accessToken);
        result = new JSONObject(RequestSender.sendRequest(params, Property.GET, API_URL));
    }

    @Override
    public String getFirstName(){
        return result.getString(Property.FIRST_NAME_PROPERTY);
    }

    @Override
    public String getSecondName(){
        return result.getString(Property.SECOND_NAME_PROPERTY);
    }

    @Override
    public String getEmail(){
        return result.getString(Property.EMAIL);
    }

    @Override
    public String getId(){
        return result.getString(Property.ID);
    }

    @Override
    public String getImage(){
        JSONObject jsonObject;
        jsonObject = result.getJSONObject(PICTURE);
        jsonObject = jsonObject.getJSONObject(DATA);
        return jsonObject.getString(URL);
    }

    @Override
    public Gender getGender() {
        String gender = result.getString(Property.GENDER);
        return Gender.valueOf(gender.toUpperCase());
    }
}
