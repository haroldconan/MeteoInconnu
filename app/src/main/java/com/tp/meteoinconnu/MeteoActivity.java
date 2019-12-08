package com.tp.meteoinconnu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tp.meteoinconnu.objects.Meteo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MeteoActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    finish();
                    return true;
            }
            return false;
        }
    };

    private Location location = null;
    private double longitude = 0D;
    private double latitude = 0D;
    JsonObject jsonObject = null;
    private final static String URLLISTMETEOPART1 = "api.openweathermap.org/data/2.5/weather?";
    TextView txttemp,txthumi,txtmain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteo);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_dashboard);
        txthumi = findViewById(R.id.txthumidimeteometeo);
        txtmain = findViewById(R.id.txtmainmeteo);
        txttemp = findViewById(R.id.txttemperaturemeteometeo);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        checkLocationPermission();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }

        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                new AsyncTask<String, Integer, Meteo>() {

                    private ProgressDialog dialog;
                    int progressentier = 0;

                    @Override
                    protected void onPreExecute() {
                        dialog = new ProgressDialog(MeteoActivity.this);
                        dialog.setMessage("Chargement...  " + progressentier + "%");
                        dialog.setCancelable(false);
                        dialog.show();
                    }

                    @Override
                    protected void onProgressUpdate(Integer... progress) {
                        Log.d("progression : ", progress[0].toString());
                        progressentier = progress[0];
                        dialog.setMessage("Chargement...  " + progressentier + "%");
                    }

                    @Override
                    protected Meteo doInBackground(String[] objects) {
                        HttpURLConnection connection = null;
                        BufferedReader reader = null;
                        JsonParser parser = new JsonParser();
                        Meteo meteo = null;

                        try {
                            URL url = null;

                            url = new URL(URLLISTMETEOPART1 + "lat="+latitude+"&lon="+longitude);
                            Log.d("meteo", URLLISTMETEOPART1 + "lat="+latitude+"&lon="+longitude);
                            connection = (HttpURLConnection) url.openConnection();
                            connection.connect();


                            InputStream stream = connection.getInputStream();

                            reader = new BufferedReader(new InputStreamReader(stream));

                            StringBuffer buffer = new StringBuffer();
                            String line = "";

                            while ((line = reader.readLine()) != null) {
                                buffer.append(line + "\n");

                            }
                            Log.d("object", buffer.toString());
                            jsonObject = (JsonObject) parser.parse(buffer.toString());
                            if (jsonObject.get("cod").getAsString().equals("404")) {
                                return null;
                            }
                            Log.d("meteo", jsonObject.getAsJsonObject("main").toString());
                            meteo = new Meteo(Double.parseDouble(jsonObject.getAsJsonObject("main").get("temp").getAsString()), Integer.parseInt(jsonObject.getAsJsonObject("main").get("humidity").getAsString()), jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString());
                            publishProgress(100);

                        } catch (Exception e) {
                            Log.d("Erreur async", e.getMessage());
                        } finally {
                            if (connection != null)
                                connection.disconnect();
                        }
                        return meteo;
                    }

                    @Override
                    protected void onPostExecute(Meteo meteo) {
                        dialog.dismiss();
                        if (meteo == null) {
                            txttemp.setText("La ville n'est pas référencée");
                            txtmain.setText("");
                            txthumi.setText("");
                            return;
                        }
                        String txt = "Il y a " + meteo.getHumidity() + "% d'humiditée";
                        txthumi.setText(txt);
                        txt = "La température est de " + meteo.getTemp() + "°C";
                        txttemp.setText(txt);
                        txt = "Le temp est " + meteo.getMain();
                        txtmain.setText(txt);
                        Log.d("pass", "fini METEO");
                    }
                }.execute();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long)2000, (float)10, locationListener);
        List<String> providers = lm.getProviders(true);
        Location loca = lm.getLastKnownLocation(providers.get(0));
        longitude = loca.getLongitude();
        latitude = loca.getLatitude();
        new AsyncTask<String, Integer, Meteo>() {

            private ProgressDialog dialog;
            int progressentier = 0;

            @Override
            protected void onPreExecute() {
                dialog = new ProgressDialog(MeteoActivity.this);
                dialog.setMessage("Chargement...  " + progressentier + "%");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onProgressUpdate(Integer... progress) {
                Log.d("progression : ", progress[0].toString());
                progressentier = progress[0];
                dialog.setMessage("Chargement...  " + progressentier + "%");
            }

            @Override
            protected Meteo doInBackground(String[] objects) {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                JsonParser parser = new JsonParser();
                Meteo meteo = null;

                try {
                    URL url = null;

                    url = new URL(URLLISTMETEOPART1 + "lat="+latitude+"&lon="+longitude);
                    Log.d("meteo", URLLISTMETEOPART1 + "lat="+latitude+"&lon="+longitude);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();


                    InputStream stream = connection.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");

                    }
                    Log.d("object", buffer.toString());
                    jsonObject = (JsonObject) parser.parse(buffer.toString());
                    if (jsonObject.get("cod").getAsString().equals("404")) {
                        return null;
                    }
                    Log.d("meteo", jsonObject.getAsJsonObject("main").toString());
                    meteo = new Meteo(Double.parseDouble(jsonObject.getAsJsonObject("main").get("temp").getAsString()), Integer.parseInt(jsonObject.getAsJsonObject("main").get("humidity").getAsString()), jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString());
                    publishProgress(100);

                } catch (Exception e) {
                    Log.d("Erreur async", e.getMessage());
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }
                return meteo;
            }

            @Override
            protected void onPostExecute(Meteo meteo) {
                dialog.dismiss();
                if (meteo == null) {
                    txttemp.setText("La ville n'est pas référencée");
                    txtmain.setText("");
                    txthumi.setText("");
                    return;
                }
                String txt = "Il y a " + meteo.getHumidity() + "% d'humiditée";
                txthumi.setText(txt);
                txt = "La température est de " + meteo.getTemp() + "°C";
                txttemp.setText(txt);
                txt = "Le temp est " + meteo.getMain();
                txtmain.setText(txt);
                Log.d("pass", "fini METEO");
            }
        }.execute();
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}

