package by.training.online_pharmacy.service.util;

import by.training.online_pharmacy.domain.user.Gender;

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

    Gender getGender();

    default String getPhone(){
        return null;
    }
}
