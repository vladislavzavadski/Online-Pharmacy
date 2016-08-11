package by.training.online_pharmacy.domain.message;

import by.training.online_pharmacy.domain.user.User;

/**
 * Created by vladislav on 11.08.16.
 */
public class Message {
    private User sender;
    private User receiver;
    private String senderMessage;
    private String receiverMessage;
    private int id;
    private boolean responseSended;
    private boolean requestSended;

    @Override
    public String toString() {
        return "Message{" +
                "sender=" + sender +
                ", receiver=" + receiver +
                ", senderMessage='" + senderMessage + '\'' +
                ", receiverMessage='" + receiverMessage + '\'' +
                ", id=" + id +
                ", responseSended=" + responseSended +
                ", requestSended=" + requestSended +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (id != message.id) return false;
        if (responseSended != message.responseSended) return false;
        if (requestSended != message.requestSended) return false;
        if (sender != null ? !sender.equals(message.sender) : message.sender != null) return false;
        if (receiver != null ? !receiver.equals(message.receiver) : message.receiver != null) return false;
        if (senderMessage != null ? !senderMessage.equals(message.senderMessage) : message.senderMessage != null)
            return false;
        return receiverMessage != null ? receiverMessage.equals(message.receiverMessage) : message.receiverMessage == null;

    }

    @Override
    public int hashCode() {
        int result = sender != null ? sender.hashCode() : 0;
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        result = 31 * result + (senderMessage != null ? senderMessage.hashCode() : 0);
        result = 31 * result + (receiverMessage != null ? receiverMessage.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (responseSended ? 1 : 0);
        result = 31 * result + (requestSended ? 1 : 0);
        return result;
    }

    public User getSender() {

        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getSenderMessage() {
        return senderMessage;
    }

    public void setSenderMessage(String senderMessage) {
        this.senderMessage = senderMessage;
    }

    public String getReceiverMessage() {
        return receiverMessage;
    }

    public void setReceiverMessage(String receiverMessage) {
        this.receiverMessage = receiverMessage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isResponseSended() {
        return responseSended;
    }

    public void setResponseSended(boolean responseSended) {
        this.responseSended = responseSended;
    }

    public boolean isRequestSended() {
        return requestSended;
    }

    public void setRequestSended(boolean requestSended) {
        this.requestSended = requestSended;
    }
}
