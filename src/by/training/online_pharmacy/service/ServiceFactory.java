package by.training.online_pharmacy.service;

import by.training.online_pharmacy.service.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vladislav on 18.07.16.
 */
public class ServiceFactory {

    private static final ServiceFactory serviceFactory = new ServiceFactory();
    private final Map<ServiceType, Object> services = new HashMap<>();

    public static ServiceFactory getInstance() {
        return serviceFactory;
    }

    private ServiceFactory(){

        services.put(ServiceType.USER_SERVICE, new UserServiceImpl());
        services.put(ServiceType.SECRET_QUESTION_SERVICE, new SecretQuestionServiceImpl());
        services.put(ServiceType.SOCIAL_NETWORK_SERVICE, new SocialNetworkServiceImpl());
        services.put(ServiceType.INIT_CONNECTION_SERVICE, new ConnectionServiceImpl());
        services.put(ServiceType.DRUG_SERVICE, new DrugServiceImpl());
        services.put(ServiceType.ORDER_SERVICE, new OrderServiceImpl());
        services.put(ServiceType.REQUEST_SERVICE, new RequestServiceImpl());
        services.put(ServiceType.MESSAGE_SERVICE, new MessageServiceImpl());
        services.put(ServiceType.PRESCRIPTION_SERVICE, new PrescriptionServiceImpl());

    }

    public UserService getUserService(){
        Object service = services.get(ServiceType.USER_SERVICE);

        return (UserService)service;
    }

    public SocialNetworkService getSocialNetworkService(){
        Object service = services.get(ServiceType.SOCIAL_NETWORK_SERVICE);

        return (SocialNetworkService)service;
    }

    public InitConnectionService getInitConnectionService(){
        Object service = services.get(ServiceType.INIT_CONNECTION_SERVICE);

        return (InitConnectionService)service;
    }

    public DrugService getDrugService(){
        Object service = services.get(ServiceType.DRUG_SERVICE);

        return (DrugService)service;
    }

    public OrderService getOrderService(){
        Object service = services.get(ServiceType.ORDER_SERVICE);

        return (OrderService)service;
    }

    public RequestService getRequestService(){
        Object service = services.get(ServiceType.REQUEST_SERVICE);

        return (RequestService) service;
    }

    public MessageService getMessageService(){
        Object service = services.get(ServiceType.MESSAGE_SERVICE);

        return (MessageService)service;
    }

    public PrescriptionService getPrescriptionService(){
        Object service = services.get(ServiceType.PRESCRIPTION_SERVICE);

        return (PrescriptionService)service;
    }

    public SecretQuestionService getSecretQuestionService(){
        Object service = services.get(ServiceType.SECRET_QUESTION_SERVICE);

        return (SecretQuestionService)service;
    }
}
