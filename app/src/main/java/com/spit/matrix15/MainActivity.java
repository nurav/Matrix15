package com.spit.matrix15;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> eventsList;
    JSONParser jParser = new JSONParser();

//    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DBVer", 0);

    //url to get all events
    private static String url_all_events = "http://matrixthefest.org/get_all_events.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_EVENTS = "event";
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

    JSONObject jsonObject = null;
    JSONArray events = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventsList = new ArrayList<HashMap<String, String>>();

        new LoadAllEvents().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadAllEvents extends AsyncTask<String, String, String>   {

        private EventsDataSource eventsDataSource;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Refreshing events. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = jParser.makeHttpRequest(url_all_events, "GET", params);
            Log.d("All Products: ", json.toString());

            try {
                jsonObject = new JSONObject(json.toString());

                // Getting Array of Products
                events = jsonObject.getJSONArray(TAG_EVENTS);

                // looping through All Events
                for (int i = 0; i < events.length(); i++) {
                    JSONObject c = events.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(TAG_ID);
                    String name = c.getString(TAG_NAME);
                    String desc = c.getString(TAG_DESC);
                    String email = c.getString(TAG_EMAIL);
                    String contact1 = c.getString(TAG_CONTACT1);
                    String contact2 = c.getString(TAG_CONTACT2);
                    String fee = c.getString(TAG_FEE);
                    String venue = c.getString(TAG_VENUE);
                    String eventhightlight1 = c.getString(TAG_EVENTHIGHLIGHT1);
                    String eventhightlight2 = c.getString(TAG_EVENTHIGHLIGHT2);
                    String eventhightlight3 = c.getString(TAG_EVENTHIGHLIGHT3);
                    String category = c.getString(TAG_CATEGORY);
                    String postername = c.getString(TAG_POSTERNAME);


                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_ID, id);
                    map.put(TAG_NAME, name);
                    map.put(TAG_DESC, desc);
                    map.put(TAG_EMAIL, email);
                    map.put(TAG_CONTACT1, contact1);
                    map.put(TAG_CONTACT2, contact2);
                    map.put(TAG_FEE, fee);
                    map.put(TAG_VENUE, venue);
                    map.put(TAG_EVENTHIGHLIGHT1, eventhightlight1);
                    map.put(TAG_EVENTHIGHLIGHT2, eventhightlight2);
                    map.put(TAG_EVENTHIGHLIGHT3, eventhightlight3);
                    map.put(TAG_CATEGORY, category);
                    map.put(TAG_POSTERNAME, postername);

                    // adding HashList to ArrayList
                    eventsList.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

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

                    eventsDataSource = new EventsDataSource(getApplicationContext(), 1);
                    eventsDataSource.open();

                    eventsDataSource.insertListIntoSQLite(eventsList);

                    startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
                }
            });

        }

    }
}
