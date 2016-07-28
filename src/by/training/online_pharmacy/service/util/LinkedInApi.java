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
    private String result;

    @Override
    public void authorization(String code) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", "http://localhost:8080/controller?command=USER_LOGIN_LI");
        params.put("client_id", "78i3k2ihydq5da");
        params.put("client_secret", "45P64u8tNmkChUA0");

        String response = RequestSender.sendRequest(params, "GET", OAUTH_URL);
        JSONObject jsonObject = new JSONObject(response);
        String accessToken = jsonObject.getString("access_token");

        params = new HashMap<>();
        params.put("format", "json");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer "+accessToken);
        result = RequestSender.sendRequest(params, headers, "GET", API_URL);
    }

    @Override
    public String getEmail(){
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.getString("emailAddress");
    }

    @Override
    public String getFirstName(){
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.getString("firstName");
    }

    @Override
    public String getSecondName(){
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.getString("lastName");
    }

    @Override
    public String getImage(){
        JSONObject jsonObject = new JSONObject(result);
        jsonObject = jsonObject.getJSONObject("pictureUrls");
        if(jsonObject.getInt("_total")==0) {
            return null;
        }
        return jsonObject.getJSONArray("values").getString(0);
    }

    @Override
    public Gender getGender() {
        return Gender.UNKNOWN;
    }

    @Override
    public String getId(){
        JSONObject jsonObject  = new JSONObject(result);
        return jsonObject.getString("id");
    }


}
