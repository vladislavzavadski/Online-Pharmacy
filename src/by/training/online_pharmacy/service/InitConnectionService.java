package by.training.online_pharmacy.service;

/**
 * Created by vladislav on 28.07.16.
 */
public interface InitConnectionService {
    void initConnection();
    void destroyConnection();

    void reserveConnection();

    void freeConnection();
}
