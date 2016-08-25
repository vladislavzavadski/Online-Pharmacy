package by.training.online_pharmacy.domain.prescription;

/**
 * Created by vladislav on 25.08.16.
 */
public class RequestForPrescriptionCriteria {
    private String drugName;
    private String requestDateFrom;
    private String requestDateTo;
    private String requestStatus;

    @Override
    public String toString() {
        return "RequestForPrescriptionCriteria{" +
                "drugName='" + drugName + '\'' +
                ", requestDateFrom='" + requestDateFrom + '\'' +
                ", requestDateTo='" + requestDateTo + '\'' +
                ", requestStatus='" + requestStatus + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestForPrescriptionCriteria that = (RequestForPrescriptionCriteria) o;

        if (drugName != null ? !drugName.equals(that.drugName) : that.drugName != null) return false;
        if (requestDateFrom != null ? !requestDateFrom.equals(that.requestDateFrom) : that.requestDateFrom != null)
            return false;
        if (requestDateTo != null ? !requestDateTo.equals(that.requestDateTo) : that.requestDateTo != null)
            return false;
        return requestStatus != null ? requestStatus.equals(that.requestStatus) : that.requestStatus == null;

    }

    @Override
    public int hashCode() {
        int result = drugName != null ? drugName.hashCode() : 0;
        result = 31 * result + (requestDateFrom != null ? requestDateFrom.hashCode() : 0);
        result = 31 * result + (requestDateTo != null ? requestDateTo.hashCode() : 0);
        result = 31 * result + (requestStatus != null ? requestStatus.hashCode() : 0);
        return result;
    }

    public String getDrugName() {

        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getRequestDateFrom() {
        return requestDateFrom;
    }

    public void setRequestDateFrom(String requestDateFrom) {
        this.requestDateFrom = requestDateFrom;
    }

    public String getRequestDateTo() {
        return requestDateTo;
    }

    public void setRequestDateTo(String requestDateTo) {
        this.requestDateTo = requestDateTo;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
