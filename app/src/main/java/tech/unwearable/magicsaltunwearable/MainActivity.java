package tech.unwearable.magicsaltunwearable;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tech.unwearable.magicsaltunwearable.contract.ColorRequest;
import tech.unwearable.magicsaltunwearable.contract.ColorsData;
import tech.unwearable.magicsaltunwearable.contract.MessageRequest;
import tech.unwearable.magicsaltunwearable.request.HandleResponseInterface;
import tech.unwearable.magicsaltunwearable.request.Request;
import tech.unwearable.magicsaltunwearable.services.NotificationListener;
import tech.unwearable.magicsaltunwearable.services.SmsReceiver;

public class MainActivity extends AppCompatActivity implements HandleResponseInterface {

    List<String> varSpinnerData;

    Spinner varSpinner;

    private NotificationReceiver nReceiver;
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    private AlertDialog enableNotificationListenerAlertDialog;
    private SmsReceiver imageChangeBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getColorList();
        enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
        enableNotificationListenerAlertDialog.show();
        setContentView(R.layout.activity_main);
        varSpinner = (Spinner) findViewById(R.id.spinner);

        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver,filter);

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
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void sendColor(View view) {
        String messageToSend = ((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString();

        Gson gson = new GsonBuilder().create();

        String jsonString = gson.toJson(new ColorRequest(messageToSend));
        try {
            JSONObject jsonMessage = new JSONObject(jsonString);
            Request request = new Request("color", jsonMessage, this, this);
            request.executePostRequest();
        } catch (JSONException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    public JSONObject getColorList() {
        Request request = new Request("colors", null, this, this);
        request.executeGetRequest();

        return null;
    }

    public void updateColors(View view) {
        getColorList();
    }

    @Override
    public void handleResponse(String response) throws JSONException {
        Log.d("", response);
        JSONObject jsonMessage = new JSONObject(response);
        ObjectMapper mapper = new ObjectMapper();
        try {
            response = response.replace("[", "\"[");
            response = response.replace("]", "]\"");
//            GenericResponse responseObject = mapper.readValue(response, GenericResponse.class);
            // map response object to new object based on response type
            switch (jsonMessage.getString("endpoint")) {
                case "get/colors":
                    Log.d("", jsonMessage.getString("Data"));
                    ColorsData[] colors = mapper.readValue(jsonMessage.getString("Data"), ColorsData[].class);
                    List<String> colorNames = new ArrayList<>();
                    for (ColorsData cd : colors) {
                        colorNames.add(cd.getName());
                    }
                    varSpinnerData = colorNames;

                    Spinner mySpinner = new Spinner(this);

                    //varSpinner = mySpinner;

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colorNames);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
                    //mySpinner.setAdapter(spinnerArrayAdapter);
                    varSpinner.setAdapter(spinnerArrayAdapter);

                    break;
                default:
                    Log.d("response", jsonMessage.getString("error"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleError() {
        Log.d("", "you done goofed");
        Toast.makeText(this, "error in making request", Toast.LENGTH_SHORT).show();
    }

    public void enableNotifications(View view) {
        enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
        enableNotificationListenerAlertDialog.show();
    }
    private AlertDialog buildNotificationServiceAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }
                });
        return(alertDialogBuilder.create());
    }

    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event");
            Log.d("log", temp);
        }
    }
}
