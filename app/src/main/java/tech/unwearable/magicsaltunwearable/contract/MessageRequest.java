package tech.unwearable.magicsaltunwearable.contract;

/**
 * Created by luke on 8/19/17.
 */

public class MessageRequest {
    String message;

    public MessageRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
