package by.training.online_pharmacy.service;

import by.training.online_pharmacy.service.impl.ConnectionServiceImpl;
import by.training.online_pharmacy.service.impl.SocialNetworkServiceImpl;
import by.training.online_pharmacy.service.impl.UserServiceImpl;

/**
 * Created by vladislav on 18.07.16.
 */
public class ServiceFactory {
    private static final ServiceFactory serviceFactory = new ServiceFactory();

    public static ServiceFactory getInstance() {
        return serviceFactory;
    }

    private ServiceFactory(){}

    public UserService getUserService(){
        return new UserServiceImpl();
    }
    public SocialNetworkService getSocialNetworkService(){return new SocialNetworkServiceImpl();}
    public InitConnectionService getInitConnectionService(){return new ConnectionServiceImpl();}
}
