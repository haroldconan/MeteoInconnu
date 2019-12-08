package com.tp.meteoinconnu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.tp.meteoinconnu.objects.Meteo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    private final static String URLLISTMETEOPART1 = "http://api.openweathermap.org/data/2.5/weather?q=";
    private final static String URLLISTMETEOPART2 = "&appid=b9051f6686ca2b698edac223faec52f7";
    BottomNavigationView navView;
    Bundle bundle;
    ImageView imageView;
    TextView txtnom, txtprenom, txtville, txtage, txtsexe, txttemperature, txthumidite, txtpays, txtmain;
    JsonObject jsonObject = null;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_dashboard);
        bundle = getIntent().getExtras();
        txtage = findViewById(R.id.txtageuserdetail);
        txtprenom = findViewById(R.id.txtprenomuserdetail);
        txtnom = findViewById(R.id.txtnomuserdetail);
        txtville = findViewById(R.id.txtvilleuserdetail);
        txtsexe = findViewById(R.id.txtsexeuserdetail);
        txttemperature = findViewById(R.id.txttemperaturemeteodetail);
        txthumidite = findViewById(R.id.txthumidimeteodetail);
        txtpays = findViewById(R.id.txtpaysuserdetail);
        imageView = findViewById(R.id.imguserdetail);
        txtmain = findViewById(R.id.txtmaindetail);

        txtage.setText("Age : "+bundle.getString(MainActivity.KEY_AGE));
        txtnom.setText("Nom : "+bundle.getString(MainActivity.KEY_NOM));
        txtpays.setText("Pays : "+bundle.getString(MainActivity.KEY_PAYS));
        txtprenom.setText("Prenom : "+bundle.getString(MainActivity.KEY_PRENOM));
        txtsexe.setText("Sexe : "+bundle.getString(MainActivity.KEY_SEXE));
        txtville.setText("Ville : "+bundle.getString(MainActivity.KEY_VILLE));
        Picasso.get().load(bundle.getString(MainActivity.KEY_IMG)).into(imageView);

        new AsyncTask<String, Integer, Meteo>() {

            private ProgressDialog dialog;
            int progressentier = 0;

            @Override
            protected void onPreExecute() {
                dialog = new ProgressDialog(DetailActivity.this);
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

                    url = new URL(URLLISTMETEOPART1 + bundle.getString(MainActivity.KEY_VILLE) + URLLISTMETEOPART2);
                    Log.d("meteo", URLLISTMETEOPART1 + bundle.getString(MainActivity.KEY_VILLE) + URLLISTMETEOPART2);
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
                    txttemperature.setText("La ville n'est pas référencée");
                    txtmain.setText("");
                    txthumidite.setText("");
                    return;
                }
                String txt = "Il y a " + meteo.getHumidity() + "% d'humiditée";
                txthumidite.setText(txt);
                txt = "La température est de " + meteo.getTemp() + "°C";
                txttemperature.setText(txt);
                txt = "Le temp est " + meteo.getMain();
                txtmain.setText(txt);
                Log.d("pass", "fini METEO");
            }
        }.execute();
    }
}
