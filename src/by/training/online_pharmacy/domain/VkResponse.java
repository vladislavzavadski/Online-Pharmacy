package by.training.online_pharmacy.domain;

/**
 * Created by vladislav on 19.07.16.
 */
public class VkResponse {
    private String email;
    private String token;
    private String id;

    @Override
    public String toString() {
        return "VkResponse{" +
                "email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VkResponse that = (VkResponse) o;

        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
