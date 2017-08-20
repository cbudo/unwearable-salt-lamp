package tech.unwearable.magicsaltunwearable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tech.unwearable.magicsaltunwearable.contract.ColorsData;
import tech.unwearable.magicsaltunwearable.contract.ColorsList;
import tech.unwearable.magicsaltunwearable.contract.GenericResponse;
import tech.unwearable.magicsaltunwearable.contract.MessageRequest;
import tech.unwearable.magicsaltunwearable.request.HandleResponseInterface;
import tech.unwearable.magicsaltunwearable.request.Request;

public class MainActivity extends AppCompatActivity implements HandleResponseInterface {

    List<String> varSpinnerData;

    Spinner varSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view) {
        String messageToSend = ((EditText) findViewById(R.id.messageText)).getText().toString();

        Gson gson = new GsonBuilder().create();

        String jsonString = gson.toJson(new MessageRequest(messageToSend));
        try {
            JSONObject jsonMessage = new JSONObject(jsonString);
            Request request = new Request("message", jsonMessage, this, this);
            request.executePostRequest();
        } catch (JSONException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG);
            e.printStackTrace();
        }


    }

    public JSONObject getColorList() {
        List<String> myArraySpinner = new ArrayList<String>();

        myArraySpinner.add("red");
        myArraySpinner.add("green");
        myArraySpinner.add("blue");

        varSpinnerData = myArraySpinner;

        Spinner mySpinner = new Spinner(this);

        varSpinner = mySpinner;

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myArraySpinner);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        mySpinner.setAdapter(spinnerArrayAdapter);

        Request request = new Request("colors", null, this, this);
        request.executeGetRequest();

        return null;
    }

    @Override
    public void handleResponse(String response) throws JSONException {
        Log.d("", response);
        JSONObject jsonMessage = new JSONObject(response);
        ObjectMapper mapper = new ObjectMapper();
        try {
            GenericResponse responseObject = mapper.readValue(response, GenericResponse.class);
            // map response object to new object based on response type
            switch (responseObject.getEndpoint()){
                case "colors":
                    ColorsList colors = mapper.readValue(responseObject.getData(), ColorsList.class);
                    List<ColorsData> colorList = colors.getColorsList(); //.forEach(c -> c.getColor());
                    List<String> colorNames = new ArrayList<>();
                    for(ColorsData cd : colorList){
                        colorNames.add(cd.getColor());
                    }
                    varSpinnerData = colorNames;

                    Spinner mySpinner = new Spinner(this);

                    varSpinner = mySpinner;

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colorNames);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
                    mySpinner.setAdapter(spinnerArrayAdapter);

                    break;
                default:
                    Log.d("response", responseObject.getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleError() {
        Log.d("", "you done goofed");
        Toast.makeText(this, "error in making request", Toast.LENGTH_SHORT);
    }
}
