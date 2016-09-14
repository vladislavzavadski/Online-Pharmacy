package by.training.online_pharmacy.service;

import by.training.online_pharmacy.service.impl.*;

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

    public DrugService getDrugService(){return new DrugServiceImpl();}

    public OrderService getOrderService(){return new OrderServiceImpl();}

    public RequestService getRequestService(){return new RequestServiceImpl();}

    public MessageService getMessageService(){return new MessageServiceImpl();}

    public PrescriptionService getPrescriptionService(){return new PrescriptionServiceImpl();}

    public SecretQuestionService getSecretQuestionService(){return new SecretQuestionServiceImpl();}
}
