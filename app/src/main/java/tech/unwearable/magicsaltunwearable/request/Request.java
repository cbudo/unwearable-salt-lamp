package tech.unwearable.magicsaltunwearable.request;

/**
 * Created by solys on 1/4/2016.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

public class Request {
    private String ip = "192.168.12.7/";
    private String url;
    private JSONObject json;
    private String[] jsonKeys;
    private Activity activity;
    private HandleResponseInterface handleResponseInterface;


    public Request (String urlAddon, JSONObject json, Activity activity, HandleResponseInterface handleResponseInterface) {
        //this.ip = db.getIp();
        this.url = this.ip + urlAddon;
        this.json = json;
        this.jsonKeys = null;
        this.activity = activity;
        this.handleResponseInterface = handleResponseInterface;
    }

    public Request (String urlAddon, JSONObject json, String[] jsonKeys, Activity activity, HandleResponseInterface handleResponseInterface) {
        //this.ip = db.getIp();
        this.url = this.ip + urlAddon;
        this.json = json;
        this.jsonKeys = jsonKeys;
        this.activity = activity;
        this.handleResponseInterface = handleResponseInterface;
    }

    public void executePostRequest() {
        DoPost doPost = new DoPost();
        doPost.execute();
    }

    public void executeGetRequest() {
        DoGet doGet = new DoGet();
        doGet.execute();
    }

    private void handleResponse(final String info) {
        final String information = info;
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    handleResponseInterface.handleResponse(information);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void errorOccurred() {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Request.this.activity, "Error connecting to your Artik.", Toast.LENGTH_LONG).show();
            }
        });
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handleResponseInterface.handleError();
            }
        });
    }

    private class DoPost extends AsyncTask<Void, Void, String> {
        private static final String TAG = "doPost";
        public final String serverURL = Request.this.url;
        public final JSONObject dataToSend = Request.this.json;

        @Override
        protected String doInBackground(Void... params) {
            try {

                URL obj = new URL(serverURL);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Mozilla/5.0");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                System.out.println(dataToSend);
                if(dataToSend != null) {
                    con.setDoInput(true);
                    con.setDoOutput(true);

                    OutputStream os = con.getOutputStream();
                    BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    wr.write(dataToSend.toString());
                    wr.flush();
                    wr.close();
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                final StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                handleResponse(response.toString());

            } catch(Exception ex) {
                Log.e(TAG, "Failed to send HTTP POST request due to: " + ex);
                errorOccurred();
            }
            return null;
        }
    }

    private class DoGet extends AsyncTask<Void, Void, String> {
        private static final String TAG = "doGet";
        public String serverURL = Request.this.url;
        public String[] dataToSendKeys = Request.this.jsonKeys;
        public final JSONObject dataToSend = Request.this.json;

        private void putDataInURL() throws JSONException {
            if(dataToSend != null && dataToSendKeys != null) {
                for(int keyIndex =0; keyIndex < dataToSend.length(); keyIndex++) {
                    this.serverURL += "?" + this.dataToSendKeys[keyIndex] + "=" + dataToSend.get(dataToSendKeys[keyIndex]);
                }
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                putDataInURL();
                //  Debug!
                System.out.println(serverURL);

                URL obj = new URL(serverURL);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", "Mozilla/5.0");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                final StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                handleResponse(response.toString());

            } catch(Exception ex) {
                Log.e(TAG, "Failed to send HTTP POST request due to: " + ex);
                errorOccurred();
            }
            return null;
        }
    }
}
