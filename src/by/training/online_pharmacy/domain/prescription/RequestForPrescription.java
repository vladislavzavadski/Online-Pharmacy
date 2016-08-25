package by.training.online_pharmacy.domain.prescription;


import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.user.User;

import java.util.Date;

/**
 * Created by vladislav on 19.06.16.
 */
public class RequestForPrescription {
    private int id;
    private User client;
    private Drug drug;
    private User doctor;
    private Date prolongDate;
    private RequestStatus requestStatus;
    private String clientComment;
    private String doctorComment;
    private Date requestDate;
    private Date responseDate;

    @Override
    public String toString() {
        return "RequestForPrescription{" +
                "id=" + id +
                ", client=" + client +
                ", drug=" + drug +
                ", doctor=" + doctor +
                ", prolongDate=" + prolongDate +
                ", requestStatus=" + requestStatus +
                ", clientComment='" + clientComment + '\'' +
                ", doctorComment='" + doctorComment + '\'' +
                ", requestDate=" + requestDate +
                ", responseDate=" + responseDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestForPrescription that = (RequestForPrescription) o;

        if (id != that.id) return false;
        if (client != null ? !client.equals(that.client) : that.client != null) return false;
        if (drug != null ? !drug.equals(that.drug) : that.drug != null) return false;
        if (doctor != null ? !doctor.equals(that.doctor) : that.doctor != null) return false;
        if (prolongDate != null ? !prolongDate.equals(that.prolongDate) : that.prolongDate != null) return false;
        if (requestStatus != that.requestStatus) return false;
        if (clientComment != null ? !clientComment.equals(that.clientComment) : that.clientComment != null)
            return false;
        if (doctorComment != null ? !doctorComment.equals(that.doctorComment) : that.doctorComment != null)
            return false;
        if (requestDate != null ? !requestDate.equals(that.requestDate) : that.requestDate != null) return false;
        return responseDate != null ? responseDate.equals(that.responseDate) : that.responseDate == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (client != null ? client.hashCode() : 0);
        result = 31 * result + (drug != null ? drug.hashCode() : 0);
        result = 31 * result + (doctor != null ? doctor.hashCode() : 0);
        result = 31 * result + (prolongDate != null ? prolongDate.hashCode() : 0);
        result = 31 * result + (requestStatus != null ? requestStatus.hashCode() : 0);
        result = 31 * result + (clientComment != null ? clientComment.hashCode() : 0);
        result = 31 * result + (doctorComment != null ? doctorComment.hashCode() : 0);
        result = 31 * result + (requestDate != null ? requestDate.hashCode() : 0);
        result = 31 * result + (responseDate != null ? responseDate.hashCode() : 0);
        return result;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public Date getProlongDate() {
        return prolongDate;
    }

    public void setProlongDate(Date prolongDate) {
        this.prolongDate = prolongDate;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getClientComment() {
        return clientComment;
    }

    public void setClientComment(String clientComment) {
        this.clientComment = clientComment;
    }

    public String getDoctorComment() {
        return doctorComment;
    }

    public void setDoctorComment(String doctorComment) {
        this.doctorComment = doctorComment;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }
}
