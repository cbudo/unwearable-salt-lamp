package tech.unwearable.magicsaltunwearable.request;

import org.json.JSONException;

/**
 * Created by budoc on 8/19/2017.
 */

public interface HandleResponseInterface {
    void handleResponse(String response) throws JSONException;
    void handleError();
}
