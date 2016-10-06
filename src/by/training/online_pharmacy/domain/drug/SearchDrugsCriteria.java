package by.training.online_pharmacy.domain.drug;

/**
 * Created by vladislav on 29.08.16.
 */
public class SearchDrugsCriteria {
    private String name;
    private String activeSubstance;
    private String drugMaxPrice;
    private String drugClass;
    private DrugManufacturer drugManufacture;
    private String onlyInStock;
    private String prescriptionEnable;

    @Override
    public String toString() {
        return "SearchDrugsCriteria{" +
                "name='" + name + '\'' +
                ", activeSubstance='" + activeSubstance + '\'' +
                ", drugMaxPrice='" + drugMaxPrice + '\'' +
                ", drugClass='" + drugClass + '\'' +
                ", drugManufacture='" + drugManufacture + '\'' +
                ", onlyInStock='" + onlyInStock + '\'' +
                ", prescriptionEnable='" + prescriptionEnable + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchDrugsCriteria that = (SearchDrugsCriteria) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (activeSubstance != null ? !activeSubstance.equals(that.activeSubstance) : that.activeSubstance != null)
            return false;
        if (drugMaxPrice != null ? !drugMaxPrice.equals(that.drugMaxPrice) : that.drugMaxPrice != null) return false;
        if (drugClass != null ? !drugClass.equals(that.drugClass) : that.drugClass != null) return false;
        if (drugManufacture != null ? !drugManufacture.equals(that.drugManufacture) : that.drugManufacture != null)
            return false;
        if (onlyInStock != null ? !onlyInStock.equals(that.onlyInStock) : that.onlyInStock != null) return false;
        return prescriptionEnable != null ? prescriptionEnable.equals(that.prescriptionEnable) : that.prescriptionEnable == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (activeSubstance != null ? activeSubstance.hashCode() : 0);
        result = 31 * result + (drugMaxPrice != null ? drugMaxPrice.hashCode() : 0);
        result = 31 * result + (drugClass != null ? drugClass.hashCode() : 0);
        result = 31 * result + (drugManufacture != null ? drugManufacture.hashCode() : 0);
        result = 31 * result + (onlyInStock != null ? onlyInStock.hashCode() : 0);
        result = 31 * result + (prescriptionEnable != null ? prescriptionEnable.hashCode() : 0);
        return result;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActiveSubstance() {
        return activeSubstance;
    }

    public void setActiveSubstance(String activeSubstance) {
        this.activeSubstance = activeSubstance;
    }

    public String getDrugMaxPrice() {
        return drugMaxPrice;
    }

    public void setDrugMaxPrice(String drugMaxPrice) {
        this.drugMaxPrice = drugMaxPrice;
    }

    public String getDrugClass() {
        return drugClass;
    }

    public void setDrugClass(String drugClass) {
        this.drugClass = drugClass;
    }

    public DrugManufacturer getDrugManufacture() {
        return drugManufacture;
    }

    public void setDrugManufacture(DrugManufacturer drugManufacture) {
        this.drugManufacture = drugManufacture;
    }

    public String getOnlyInStock() {
        return onlyInStock;
    }

    public void setOnlyInStock(String onlyInStock) {
        this.onlyInStock = onlyInStock;
    }

    public String getPrescriptionEnable() {
        return prescriptionEnable;
    }

    public void setPrescriptionEnable(String prescriptionEnable) {
        this.prescriptionEnable = prescriptionEnable;
    }
}
