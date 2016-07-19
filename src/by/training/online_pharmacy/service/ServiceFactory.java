package by.training.online_pharmacy.service;

import by.training.online_pharmacy.service.impl.UserServiceImpl;

/**
 * Created by vladislav on 18.07.16.
 */
public class ServiceFactory {
    private static ServiceFactory serviceFactory;

    public static ServiceFactory getInstance(){
        if(serviceFactory==null){
            synchronized (ServiceFactory.class){
                if(serviceFactory==null){
                    serviceFactory = new ServiceFactory();
                }
            }
        }
        return serviceFactory;
    }

    private ServiceFactory(){}

    public UserService getUserService(){
        return new UserServiceImpl();
    }
}
