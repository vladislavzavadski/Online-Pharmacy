package by.training.online_pharmacy.domain.user;

/**
 * Created by vladislav on 04.09.16.
 */
public class SecretWord {
    private User user;
    private String response;
    private SecretQuestion secretQuestion;

    @Override
    public String toString() {
        return "SecretWord{" +
                "user=" + user +
                ", response='" + response + '\'' +
                ", secretQuestion=" + secretQuestion +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SecretWord that = (SecretWord) o;

        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (response != null ? !response.equals(that.response) : that.response != null) return false;
        return secretQuestion != null ? secretQuestion.equals(that.secretQuestion) : that.secretQuestion == null;

    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (response != null ? response.hashCode() : 0);
        result = 31 * result + (secretQuestion != null ? secretQuestion.hashCode() : 0);
        return result;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public SecretQuestion getSecretQuestion() {
        return secretQuestion;
    }

    public void setSecretQuestion(SecretQuestion secretQuestion) {
        this.secretQuestion = secretQuestion;
    }
}
