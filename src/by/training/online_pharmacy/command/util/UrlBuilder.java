package by.training.online_pharmacy.command.util;


import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


/**
 * Created by vladislav on 25.07.16.
 */
public class UrlBuilder {

    private UrlBuilder(){}

    public static String build(HttpServletRequest request){
        String query = "";
        Enumeration<String> paramNames = request.getParameterNames();

        while(paramNames.hasMoreElements()){
            String key = paramNames.nextElement();
            String value = request.getParameter(key);
            query+=key+"="+value+"&";
        }
        return request.getRequestURL()+"?"+query;

    }
}
