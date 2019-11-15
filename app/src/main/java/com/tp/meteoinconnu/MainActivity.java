package com.tp.meteoinconnu;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tp.meteoinconnu.objects.Users;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MainActivity extends AppCompatActivity {
    public static final int AJOUT_PERS_REQUEST_CODE = 1;
    public static final String KEY_NOM = "NOM";
    public static final String KEY_PRENOM = "PRENOM";
    public static final String KEY_AGE = "AGE";
    public static final String KEY_VILLE = "VILLE";
    public static final String KEY_SEXE = "SEXE";
    public static final String KEY_IMG = "IMG";
    public static final String KEY_PAYS = "PAYS";

    private static final String URLLISTUSER = "https://randomuser.me/api/";
    private ListView listView;
    AdaptateurListe monAdapteur;
    JsonObject jsonObject = null;
    List<Users> usersActuel = new ArrayList<>();
    BottomNavigationView navView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_dashboard:
                    Intent intent = new Intent(MainActivity.this, AjouterActivity.class);
                    startActivityForResult(intent, AJOUT_PERS_REQUEST_CODE);
                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        monAdapteur = new AdaptateurListe(this, usersActuel);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        listView = findViewById(R.id.listinconnu);
        listView.setAdapter(monAdapteur);
        new AsyncTask<String, Integer, List<Users>>() {

            private ProgressDialog dialog;
            int progressentier = 0;

            @Override
            protected void onPreExecute() {
                dialog = new ProgressDialog(MainActivity.this);
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
            protected List<Users> doInBackground(String[] objects) {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                List<Users> listencour = new ArrayList<>();
                JsonParser parser = new JsonParser();
                JsonArray jsonArray = new JsonArray();

                try {
                    URL url = null;

                    url = new URL(URLLISTUSER);
                    for (int i = 0; i < 30; i++) {
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
                        jsonArray = jsonObject.getAsJsonArray("results");
                        Log.d("array", jsonArray.get(0).getAsJsonObject().getAsJsonObject("name").toString());
                        listencour.add(new Users(jsonArray.get(0).getAsJsonObject().getAsJsonObject("name").get("first").getAsString(), jsonArray.get(0).getAsJsonObject().getAsJsonObject("name").get("last").getAsString(), jsonArray.get(0).getAsJsonObject().getAsJsonObject("location").get("city").getAsString(), jsonArray.get(0).getAsJsonObject().getAsJsonObject("location").get("country").getAsString(), jsonArray.get(0).getAsJsonObject().get("gender").getAsString(), jsonArray.get(0).getAsJsonObject().getAsJsonObject("dob").get("age").getAsInt(), jsonArray.get(0).getAsJsonObject().getAsJsonObject("picture").get("large").getAsString()));
                        publishProgress(((i * 100) / 30));
                    }
                } catch (Exception e) {
                    Log.i("Erreur async", e.getMessage());
                }
                return listencour;
            }

            @Override
            protected void onPostExecute(List<Users> users) {
                monAdapteur.addAll(users);
                usersActuel = users;
                dialog.dismiss();
                Log.d("2", "d");
            }
        }.execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case AJOUT_PERS_REQUEST_CODE: {
                        Log.d("\n\n\nResult", data.getStringExtra(KEY_NOM) + data.getStringExtra(KEY_PRENOM) + data.getStringExtra(KEY_VILLE) + "none" + data.getStringExtra(KEY_SEXE) + Integer.parseInt(data.getStringExtra(KEY_AGE)) + "https://randomuser.me/api/portraits/men/88.jpg" + " \n\n\n");
                        Users users = new Users(data.getStringExtra(KEY_NOM), data.getStringExtra(KEY_PRENOM), data.getStringExtra(KEY_VILLE), "none", data.getStringExtra(KEY_SEXE), Integer.parseInt(data.getStringExtra(KEY_AGE)), "https://randomuser.me/api/portraits/men/88.jpg");
                        monAdapteur.add(users);
                        usersActuel.add(users);
                        monAdapteur.notifyDataSetChanged();
                    }
                    break;
                }
            }
            navView.setSelectedItemId(R.id.navigation_home);

        } catch (Exception e) {
            Log.d("Erreur result : ", e.getMessage());
        }
    }
}
