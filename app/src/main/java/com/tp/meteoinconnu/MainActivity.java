package com.tp.meteoinconnu;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
    public static final int METEO_REQUEST_CODE = 2;
    public static final int PARAMETRE_REQUEST_CODE = 3;
    public static final String KEY_NOM = "NOM";
    public static final String KEY_PRENOM = "PRENOM";
    public static final String KEY_AGE = "AGE";
    public static final String KEY_VILLE = "VILLE";
    public static final String KEY_SEXE = "SEXE";
    public static final String KEY_IMG = "IMG";
    public static final String KEY_PAYS = "PAYS";
    public static final String KEY_SEXE_PARAM_H = "SEXEH";
    public static final String KEY_SEXE_PARAM_F = "SEXEF";

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
            }
            return false;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menumeteo) {
            Intent intent = new Intent(MainActivity.this, MeteoActivity.class);
            startActivityForResult(intent, METEO_REQUEST_CODE);
            return true;
        }
        if ( id == R.id.menuparam){
            Intent intent = new Intent(MainActivity.this, ParametreActivity.class);
            startActivityForResult(intent, PARAMETRE_REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        monAdapteur = new AdaptateurListe(this, usersActuel);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        listView = findViewById(R.id.listinconnu);
        listView.setAdapter(monAdapteur);
        LancerRecherche();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(KEY_NOM, usersActuel.get(i).getNom());
                intent.putExtra(KEY_PRENOM,usersActuel.get(i).getPrenom());
                intent.putExtra(KEY_AGE,usersActuel.get(i).getAge());
                intent.putExtra(KEY_IMG,usersActuel.get(i).getImgURL());
                intent.putExtra(KEY_PAYS,usersActuel.get(i).getPays());
                intent.putExtra(KEY_SEXE,usersActuel.get(i).getSexe());
                intent.putExtra(KEY_VILLE,usersActuel.get(i).getVille());
                startActivity(intent);

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
                    }break;
                    case PARAMETRE_REQUEST_CODE:{
                        LancerRecherche();
                    }
                    break;
                }
            }
            navView.setSelectedItemId(R.id.navigation_home);

        } catch (Exception e) {
            Log.d("Erreur result : ", e.getMessage());
        }
    }
    public void LancerRecherche(){
        AsyncTask<String, Integer, List<Users>> async = null;
        final int[] progressentier = {0};
        final ProgressDialog dialog;
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Chargement...  " + progressentier[0] + "%");
        dialog.setCancelable(true);

        async = new AsyncTask<String, Integer, List<Users>>() {




            @Override
            protected void onPreExecute() {

                dialog.show();
            }

            @Override
            protected void onProgressUpdate(Integer... progress) {
                Log.d("progression : ", progress[0].toString());
                progressentier[0] = progress[0];
                dialog.setMessage("Chargement...  " + progressentier[0] + "%");
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
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                        Users user = new Users(jsonArray.get(0).getAsJsonObject().getAsJsonObject("name").get("first").getAsString(), jsonArray.get(0).getAsJsonObject().getAsJsonObject("name").get("last").getAsString(), jsonArray.get(0).getAsJsonObject().getAsJsonObject("location").get("city").getAsString(), jsonArray.get(0).getAsJsonObject().getAsJsonObject("location").get("country").getAsString(), jsonArray.get(0).getAsJsonObject().get("gender").getAsString(), jsonArray.get(0).getAsJsonObject().getAsJsonObject("dob").get("age").getAsInt(), jsonArray.get(0).getAsJsonObject().getAsJsonObject("picture").get("large").getAsString());
                        Log.i("hey","\n"+pref.getBoolean(KEY_SEXE_PARAM_F,true)+"\n"+pref.getString(KEY_VILLE, "")+"\n"+pref.getString(KEY_PAYS, "")+"\n"+pref.getString(KEY_NOM, "")+"\n"+pref.getString(KEY_PRENOM, "")+"\n"+pref.getString(KEY_AGE, ""));
                        if((!pref.getBoolean(KEY_SEXE_PARAM_F,true) && pref.getBoolean(KEY_SEXE_PARAM_H,true))&& user.getSexe() == "male") {
                            if ((pref.getString(KEY_VILLE, "") != "" && pref.getString(KEY_VILLE, "") == user.getVille()) || pref.getString(KEY_VILLE, "") == "") {
                                if ((pref.getString(KEY_PAYS, "") != "" && pref.getString(KEY_PAYS, "") == user.getPays())|| pref.getString(KEY_PAYS, "") == "") {
                                    if ((pref.getString(KEY_NOM, "") != "" && pref.getString(KEY_NOM, "") == user.getNom()) || pref.getString(KEY_NOM, "") == "") {
                                        if ((pref.getString(KEY_PRENOM, "") != "" && pref.getString(KEY_PRENOM, "") == user.getPrenom())|| pref.getString(KEY_PRENOM, "") == "") {
                                            if ((pref.getString(KEY_AGE, "") != "" && Integer.parseInt(pref.getString(KEY_AGE, "")) == user.getAge())||pref.getString(KEY_AGE, "") == "") {
                                                listencour.add(user);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if((pref.getBoolean(KEY_SEXE_PARAM_F,true) && !pref.getBoolean(KEY_SEXE_PARAM_H,true))&& user.getSexe() == "female") {
                            if ((pref.getString(KEY_VILLE, "") != "" && pref.getString(KEY_VILLE, "") == user.getVille()) || pref.getString(KEY_VILLE, "") == "") {
                                if ((pref.getString(KEY_PAYS, "") != "" && pref.getString(KEY_PAYS, "") == user.getPays())|| pref.getString(KEY_PAYS, "") == "") {
                                    if ((pref.getString(KEY_NOM, "") != "" && pref.getString(KEY_NOM, "") == user.getNom()) || pref.getString(KEY_NOM, "") == "") {
                                        if ((pref.getString(KEY_PRENOM, "") != "" && pref.getString(KEY_PRENOM, "") == user.getPrenom())|| pref.getString(KEY_PRENOM, "") == "") {
                                            if ((pref.getString(KEY_AGE, "") != "" && Integer.parseInt(pref.getString(KEY_AGE, "")) == user.getAge())||pref.getString(KEY_AGE, "") == "") {
                                                listencour.add(user);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if((pref.getBoolean(KEY_SEXE_PARAM_F,true) && pref.getBoolean(KEY_SEXE_PARAM_H,true)) || (!pref.getBoolean(KEY_SEXE_PARAM_F,true) && !pref.getBoolean(KEY_SEXE_PARAM_H,true))) {
                            if ((pref.getString(KEY_VILLE, "") != "" && pref.getString(KEY_VILLE, "") == user.getVille()) || pref.getString(KEY_VILLE, "") == "") {
                                if ((pref.getString(KEY_PAYS, "") != "" && pref.getString(KEY_PAYS, "") == user.getPays())|| pref.getString(KEY_PAYS, "") == "") {
                                    if ((pref.getString(KEY_NOM, "") != "" && pref.getString(KEY_NOM, "") == user.getNom()) || pref.getString(KEY_NOM, "") == "") {
                                        if ((pref.getString(KEY_PRENOM, "") != "" && pref.getString(KEY_PRENOM, "") == user.getPrenom())|| pref.getString(KEY_PRENOM, "") == "") {
                                            if ((pref.getString(KEY_AGE, "") != "" && Integer.parseInt(pref.getString(KEY_AGE, "")) == user.getAge())||pref.getString(KEY_AGE, "") == "") {
                                                listencour.add(user);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        publishProgress(((i * 100) / 30));
                    }
                } catch (Exception e) {
                    Log.i("Erreur async", e.getMessage());
                }
                finally {
                    if(connection != null   )
                        connection.disconnect();
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
        final AsyncTask<String, Integer, List<Users>> finalAsync = async;
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                finalAsync.cancel(true);
            }
        });
    }
}
