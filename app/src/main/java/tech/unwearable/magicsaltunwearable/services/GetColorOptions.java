package tech.unwearable.magicsaltunwearable.services;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tech.unwearable.magicsaltunwearable.MainActivity;
import tech.unwearable.magicsaltunwearable.request.HandleResponseInterface;

/**
 * Created by luke on 8/19/17.
 */

public class GetColorOptions implements HandleResponseInterface{

    MainActivity mainActivityRef = null;

    public GetColorOptions(MainActivity mainActivity){
        mainActivityRef = mainActivity;
    }

    @Override
    public void handleResponse(String response) throws JSONException {
        Log.d("",response);
        JSONObject jsonMessage = new JSONObject(response);

    }

    @Override
    public void handleError() {
        Log.d("","you done goofed");
        Toast.makeText(mainActivityRef,"error in making request", Toast.LENGTH_SHORT);
    }
}
