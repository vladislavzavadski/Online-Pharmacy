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
    public String result;

    @Override
    public void authorization (String code) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", "1472508072774891");
        params.put("client_secret", "feaea6b55522d84a2efcae4e9a1728c8");
        params.put("code", code);
        params.put("redirect_uri", "http://localhost:8080/controller?command=USER_LOGIN_FB");
        String response = RequestSender.sendRequest(params, "GET", API_TOKEN_URI);
        String accessToken = new JSONObject(response).getString("access_token");
        params.put("fields", "first_name,last_name,email,picture.width(400),gender");
        params.put("access_token", accessToken);
        result = RequestSender.sendRequest(params, "GET", API_URL);
    }

    @Override
    public String getFirstName(){
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.getString("first_name");
    }

    @Override
    public String getSecondName(){
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.getString("last_name");
    }

    @Override
    public String getEmail(){
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.getString("email");
    }

    @Override
    public String getId(){
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.getString("id");
    }

    @Override
    public String getImage(){
        JSONObject jsonObject = new JSONObject(result);
        jsonObject = jsonObject.getJSONObject("picture");
        jsonObject = jsonObject.getJSONObject("data");
        return jsonObject.getString("url");
    }

    @Override
    public Gender getGender() {
        JSONObject jsonObject = new JSONObject(result);
        String gender = jsonObject.getString("gender");
        return Gender.valueOf(gender.toUpperCase());
    }
}
