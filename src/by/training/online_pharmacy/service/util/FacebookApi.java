package by.training.online_pharmacy.service.util;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vladislav on 21.07.16.
 */
public class FacebookApi {
    private static final String API_URL = "https://graph.facebook.com/me?";
    public String result;

    public void authorization (String accessToken) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("fields", "first_name,last_name,email,picture.width(400)");
        params.put("access_token", accessToken);
        result = RequestSender.sendRequest(params, null, "GET", API_URL);
    }

    public String getFirstName(){
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.getString("first_name");
    }

    public String getSecondName(){
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.getString("last_name");
    }

    public String getEmail(){
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.getString("email");
    }

    public String getId(){
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.getString("id");
    }

    public String getImage(){
        JSONObject jsonObject = new JSONObject(result);
        jsonObject = jsonObject.getJSONObject("picture");
        jsonObject = jsonObject.getJSONObject("data");
        return jsonObject.getString("url");
    }
}
