package by.training.online_pharmacy.service.util;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by vladislav on 19.07.16.
 */
public class VkApi {
    private static final String baseUrl = "https://api.vk.com/method/users.get?";
    private static final String FIRST_NAME_PROPERTY = "first_name";
    private static final String SECOND_NAME_PROPERTY = "second_name";
    private static final String ID_PROPERTY = "id";
    private static final String PHONE_PROPERTY = "home_phone";
    private static final String PHOTO_PROPERTY = "photo_max_orig";
    private StringBuilder result = new StringBuilder();

    public void sendRequest(Map<String, String> params) throws IOException {
        String fullUrl = baseUrl;
        URL url;
        for(Map.Entry<String, String> entry:params.entrySet()){
            fullUrl+=entry.getKey()+"="+entry.getValue()+"&";
        }
        url = new URL(fullUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = bufferedReader.readLine())!=null){
            result.append(line);
        }
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
        return new JSONObject(result.toString()).getJSONObject("response").getString(property);
    }


}
