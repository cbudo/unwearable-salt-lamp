package tech.unwearable.magicsaltunwearable.contract;

/**
 * Created by budoc on 8/19/2017.
 */

public class GenericResponse {
    private String published_at;
    private String error;
    private String success;
    private String endpoint;
    private String data;

    public GenericResponse(String published_at, String error, String success, String endpoint, String data) {
        this.published_at = published_at;
        this.error = error;
        this.success = success;
        this.endpoint = endpoint;
        this.data = data;
    }

    public String getPublished_at() {

        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
