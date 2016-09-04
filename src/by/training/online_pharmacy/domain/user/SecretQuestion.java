package by.training.online_pharmacy.domain.user;

/**
 * Created by vladislav on 04.09.16.
 */
public class SecretQuestion {
    private int id;
    private String question;

    @Override
    public String toString() {
        return "SecretQuestion{" +
                "id=" + id +
                ", question='" + question + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SecretQuestion that = (SecretQuestion) o;

        if (id != that.id) return false;
        return question != null ? question.equals(that.question) : that.question == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (question != null ? question.hashCode() : 0);
        return result;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
