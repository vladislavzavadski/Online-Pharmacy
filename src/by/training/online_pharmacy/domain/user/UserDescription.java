package by.training.online_pharmacy.domain.user;

/**
 * Created by vladislav on 14.06.16.
 */
public class UserDescription {
    private String specialization;
    private String description;
    private String userLogin;
    private RegistrationType registrationType;

    @Override
    public String toString() {
        return "UserDescription{" +
                "specialization='" + specialization + '\'' +
                ", description='" + description + '\'' +
                ", userLogin='" + userLogin + '\'' +
                ", registrationType=" + registrationType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDescription that = (UserDescription) o;

        if (specialization != null ? !specialization.equals(that.specialization) : that.specialization != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (userLogin != null ? !userLogin.equals(that.userLogin) : that.userLogin != null) return false;
        return registrationType == that.registrationType;

    }

    @Override
    public int hashCode() {
        int result = specialization != null ? specialization.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (userLogin != null ? userLogin.hashCode() : 0);
        result = 31 * result + (registrationType != null ? registrationType.hashCode() : 0);
        return result;
    }

    public String getSpecialization() {

        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public RegistrationType getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(RegistrationType registrationType) {
        this.registrationType = registrationType;
    }
}
