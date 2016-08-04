package by.training.online_pharmacy.service.util;

import by.training.online_pharmacy.domain.user.Gender;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vladislav on 21.07.16.
 */
public class LinkedInApi implements Api {
    private static final String OAUTH_URL = "https://www.linkedin.com/oauth/v2/accessToken?";
    private static final String API_URL = "https://api.linkedin.com/v1/people/~:(id,picture-urls::(original),email-address,first-name,last-name)?";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String REDIRECT_URI = "http://localhost:8080/controller?command=USER_LOGIN_LI";
    private static final String CLIENT_ID = "78i3k2ihydq5da";
    private static final String CLIENT_SECRET = "45P64u8tNmkChUA0";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL_ADDRESS = "emailAddress";
    private static final String FORMAT = "json";
    private static final String BEARER = "Bearer ";
    private static final String TOTAL = "_total";
    private static final String PICTURE_URLS = "pictureUrls";
    private static final String VALUES = "values";
    private JSONObject result;

    @Override
    public void authorization(String code) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put(Property.CODE, code);
        params.put(Property.GRANT_TYPE, GRANT_TYPE);
        params.put(Property.REDIRECT_URI, REDIRECT_URI);
        params.put(Property.CLIENT_ID, CLIENT_ID);
        params.put(Property.CLIENT_SECRET, CLIENT_SECRET);

        String response = RequestSender.sendRequest(params, Property.GET, OAUTH_URL);
        JSONObject jsonObject = new JSONObject(response);
        String accessToken = jsonObject.getString(Property.ACCESS_TOKEN);

        params = new HashMap<>();
        params.put(Property.FORMAT, FORMAT);
        Map<String, String> headers = new HashMap<>();
        headers.put(Property.AUTHORIZATION, BEARER+accessToken);
        result = new JSONObject(RequestSender.sendRequest(params, headers, Property.GET, API_URL));
    }

    @Override
    public String getEmail(){
        return result.getString(EMAIL_ADDRESS);
    }

    @Override
    public String getFirstName(){
        return result.getString(FIRST_NAME);
    }

    @Override
    public String getSecondName(){
        return result.getString(LAST_NAME);
    }

    @Override
    public String getImage(){
        JSONObject jsonObject;
        jsonObject = result.getJSONObject(PICTURE_URLS);
        if(jsonObject.getInt(TOTAL)==0) {
            return null;
        }
        return jsonObject.getJSONArray(VALUES).getString(0);
    }

    @Override
    public Gender getGender() {
        return Gender.UNKNOWN;
    }

    @Override
    public String getId(){
        return result.getString(Property.ID);
    }

}
