package com.example.ssi.ssi;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity{
    public static final int READ_LOC_DATA = 1001;
    RfsPreferences mPreferences;
    ArrayList<DataModel> dataModel = new ArrayList<>();
    ListView list_view;
    ProgressDialog pDialog;
    CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPreferences = new RfsPreferences(this);
        pDialog = new ProgressDialog(this);
        initializeComponents();
        setToolbar();
        isStoragePermissionGranted();
        startService(new Intent(this, LocationTrackingService.class));

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rising times");
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            makeJSONRequest();
            if(dataModel.size() > 0){
                adapter.clearData();
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter(LocationTrackingService.LOCATION_INTENT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void initializeComponents() {
        list_view = (ListView) findViewById(R.id.list_view);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                makeJSONRequest();
                if(dataModel.size() > 0){
                    adapter.clearData();
                    adapter.notifyDataSetChanged();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeJSONRequest() {
    // Tag used to cancel the request
        String  tag_string_req = "string_req";

        String url = "http://api.open-notify.org/iss-pass.json?lat="+mPreferences.getLatitude()+"&lon="+mPreferences.getLongitude();
//        String url = "http://api.open-notify.org/iss-pass.json?lat=17.385044&lon=78.486671";
        Log.v("URL : ", url);

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.hide();
                Log.v("Res : ", response);
                parseJsonData(response);
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void parseJsonData(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String duration = object.getString("duration");
                String riseTime = object.getString("risetime");

                DataModel model = new DataModel();
                model.setDuration(duration);
                model.setRiseTime(getDate(Long.parseLong(riseTime), "MM/dd/yyyy HH:mm:ss"));

                dataModel.add(model);
            }

            updateUI(dataModel);
            stopService(new Intent(this, LocationTrackingService.class));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private void updateUI(ArrayList<DataModel> model) {
        adapter = new CustomAdapter(this, model);
        list_view.setAdapter(adapter);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions(new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION}, READ_LOC_DATA);
                return false;
            }
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_LOC_DATA) {
            //Checking for both the permissions for this request code.
            if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED) &&
                    (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permissions to continue", Toast.LENGTH_LONG).show();
                isStoragePermissionGranted();
            }
        }
    }


}
