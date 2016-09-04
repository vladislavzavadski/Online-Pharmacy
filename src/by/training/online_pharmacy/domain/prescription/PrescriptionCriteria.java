package by.training.online_pharmacy.domain.prescription;

/**
 * Created by vladislav on 27.08.16.
 */
public class PrescriptionCriteria {
    private String drugName;
    private PrescriptionStatus prescriptionStatus;

    @Override
    public String toString() {
        return "PrescriptionCriteria{" +
                "drugName='" + drugName + '\'' +
                ", prescriptionStatus=" + prescriptionStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrescriptionCriteria that = (PrescriptionCriteria) o;

        if (drugName != null ? !drugName.equals(that.drugName) : that.drugName != null) return false;
        return prescriptionStatus == that.prescriptionStatus;

    }

    @Override
    public int hashCode() {
        int result = drugName != null ? drugName.hashCode() : 0;
        result = 31 * result + (prescriptionStatus != null ? prescriptionStatus.hashCode() : 0);
        return result;
    }

    public String getDrugName() {

        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public PrescriptionStatus getPrescriptionStatus() {
        return prescriptionStatus;
    }

    public void setPrescriptionStatus(PrescriptionStatus prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }
}
