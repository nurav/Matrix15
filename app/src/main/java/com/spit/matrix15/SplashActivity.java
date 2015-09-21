package com.spit.matrix15;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.spit.matrix15.R;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar pDialog;
    ArrayList<Event> eventsList;
    JSONParser jParser = new JSONParser();

//    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DBVer", 0);

    //url to get all events
    private static String url_all_events = "http://matrixthefest.org/get_all_events.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "event";
    private static final String TAG_ID = "event_id";
    private static final String TAG_NAME = "event_name";
    private static final String TAG_DESC = "event_desciption";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_CONTACT1 = "contact1";
    private static final String TAG_CONTACT2 = "contact2";
    private static final String TAG_FEE = "fee";
    private static final String TAG_VENUE = "venue";
    private static final String TAG_EVENTHIGHLIGHT1 = "eventhighlight1";
    private static final String TAG_EVENTHIGHLIGHT2 = "eventhighlight2";
    private static final String TAG_EVENTHIGHLIGHT3 = "eventhighlight3";
    private static final String TAG_POSTERNAME = "eventposter";

    JSONArray events = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences preferences = getSharedPreferences("app_start", 0);
        boolean isFirstStart = preferences.getBoolean("isFirstStart", true);

        if (isFirstStart) {
            if (!isNetworkAvailable()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Can't proceed!")
                        .setMessage("You don't have a data connection. We need a data connection the first time this app is started.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                System.exit(0);
                            }
                        })
                        .create()
                        .show();
            }
            else {
                new LoadAllEventsTask().execute();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isFirstStart", false);
                editor.commit();
            }
        }

        if(isNetworkAvailable() && !isFirstStart)
            new LoadAllEventsTask().execute();
        else if (!isFirstStart)
            new WaitTask().execute();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    class WaitTask extends AsyncTask<String, String, String> {
        protected  void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String ... strings) {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String file_url) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
                }
            });
        }
    }

    class LoadAllEventsTask extends AsyncTask<String, String, String> {

        private EventsDataSource eventsDataSource;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            eventsList = new ArrayList<Event>();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = jParser.makeHttpRequest(url_all_events, "GET", params);
            if(json == null) {
                System.exit(0);
            }
            Log.d("All Products: ", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // Getting Array of Products
                    events = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Events
                    for (int i = 0; i < events.length(); i++) {
                        JSONObject c = events.getJSONObject(i);

                        Event newEvent = new Event();
                        // Storing each json item in variable
                        newEvent.eventId = c.getString(TAG_ID);
                        newEvent.eventName = c.getString(TAG_NAME);
                        newEvent.eventDescription = c.getString(TAG_DESC);
                        newEvent.email = c.getString(TAG_EMAIL);
                        newEvent.contact1 = c.getString(TAG_CONTACT1);
                        newEvent.contact2 = c.getString(TAG_CONTACT2);
                        newEvent.fee = c.getString(TAG_FEE);
                        newEvent.venue = c.getString(TAG_VENUE);
                        newEvent.eventHighlight1 = c.getString(TAG_EVENTHIGHLIGHT1);
                        newEvent.eventHighlight2 = c.getString(TAG_EVENTHIGHLIGHT2);
                        newEvent.eventHighlight3 = c.getString(TAG_EVENTHIGHLIGHT3);
                        newEvent.eventCategory = c.getString(TAG_CATEGORY);
                        newEvent.eventPoster = c.getString(TAG_POSTERNAME);

                        eventsList.add(newEvent);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {

            Event.executeQuery("delete from event");

            Event.deleteAll(Event.class);
            for (Event event : eventsList) {
                boolean nameInDb = false;
                for (Event event1 : Event.listAll(Event.class)) {
                    if (event1.eventName.equals(event.eventName)) {
                        nameInDb = true;
                        break;
                    }
                }
                if (!nameInDb)
                    event.save();
            }

            runOnUiThread(new Runnable() {
                public void run() {
                    /*
                    SET LIST ADAPTER HERE
                    USE eventsList list in the adapter
                     */
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putInt("ver", sharedPreferences.getInt("ver", 1) + 1);
//                    Do this when refreshing
//                    int version = sharedPreferences.getInt("ver", 1);


                    startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
                }
            });

        }

    }
}
