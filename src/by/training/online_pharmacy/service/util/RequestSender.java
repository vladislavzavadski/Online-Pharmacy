package by.training.online_pharmacy.service.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by vladislav on 21.07.16.
 */
class RequestSender {
    static String sendRequest(Map<String, String> params, Map<String, String> headers, String requestMethod, String url) throws IOException {
        StringBuilder result = new StringBuilder();
        if (params != null){
            for (Map.Entry<String, String> entry : params.entrySet()) {
                url+=entry.getKey()+"="+entry.getValue()+"&";
            }
        }
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
        if(headers!=null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        urlConnection.setRequestMethod(requestMethod);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null){
            result.append(line);
        }
        bufferedReader.close();
        return result.toString();
    }
}
