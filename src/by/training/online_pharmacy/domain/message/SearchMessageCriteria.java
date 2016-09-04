package by.training.online_pharmacy.domain.message;

/**
 * Created by vladislav on 29.08.16.
 */
public class SearchMessageCriteria {
    private String messageStatus;
    private String dateTo;
    private String dateFrom;

    @Override
    public String toString() {
        return "SearchMessageCriteria{" +
                "messageStatus='" + messageStatus + '\'' +
                ", dateTo='" + dateTo + '\'' +
                ", dateFrom='" + dateFrom + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchMessageCriteria that = (SearchMessageCriteria) o;

        if (messageStatus != null ? !messageStatus.equals(that.messageStatus) : that.messageStatus != null)
            return false;
        if (dateTo != null ? !dateTo.equals(that.dateTo) : that.dateTo != null) return false;
        return dateFrom != null ? dateFrom.equals(that.dateFrom) : that.dateFrom == null;

    }

    @Override
    public int hashCode() {
        int result = messageStatus != null ? messageStatus.hashCode() : 0;
        result = 31 * result + (dateTo != null ? dateTo.hashCode() : 0);
        result = 31 * result + (dateFrom != null ? dateFrom.hashCode() : 0);
        return result;
    }

    public String getMessageStatus() {

        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }
}
