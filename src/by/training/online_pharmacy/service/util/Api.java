package by.training.online_pharmacy.service.util;

import java.io.IOException;

/**
 * Created by vladislav on 24.07.16.
 */
public interface Api {
    void authorization(String code) throws IOException;
    String getFirstName();
    String getSecondName();
    String getEmail();
    String getId();
    String getImage();
    default String getPhone(){
        return null;
    }
}
