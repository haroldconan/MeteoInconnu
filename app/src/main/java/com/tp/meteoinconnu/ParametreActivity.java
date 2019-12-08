package com.tp.meteoinconnu;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

public class ParametreActivity extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setResult(MainActivity.RESULT_OK);
                    finish();
                    return true;
            }
            return false;
        }
    };

    EditText txtPays, txtVille, txtAge, txtNom, txtPrenom;
    ToggleButton tgSexeh, tgSexef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_dashboard);
        txtAge = findViewById(R.id.txtageparam);
        txtNom = findViewById(R.id.txtnomparam);
        txtPays = findViewById(R.id.txtpaysparam);
        txtPrenom = findViewById(R.id.txtprenomparam);
        txtVille = findViewById(R.id.txtVilleparam);
        tgSexeh = findViewById(R.id.tgsexeparamh);
        tgSexeh.setText("homme");
        tgSexeh.setTextOn("HOMME");
        tgSexef = findViewById(R.id.tgsexeparamf);
        tgSexef.setTextOn("FEMME");
        tgSexef.setText("femme");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        txtAge.setText(pref.getString(MainActivity.KEY_AGE, ""));
        txtVille.setText(pref.getString(MainActivity.KEY_VILLE, ""));
        txtPrenom.setText(pref.getString(MainActivity.KEY_PRENOM, ""));
        txtPays.setText(pref.getString(MainActivity.KEY_PAYS, ""));
        txtNom.setText(pref.getString(MainActivity.KEY_NOM, ""));
        tgSexef.setChecked(pref.getBoolean(MainActivity.KEY_SEXE_PARAM_F, true));
        tgSexeh.setChecked(pref.getBoolean(MainActivity.KEY_SEXE_PARAM_H, true));
    }

    @Override
    protected void onStop() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(MainActivity.KEY_PAYS, txtPays.getText().toString());
        editor.putString(MainActivity.KEY_AGE, txtPays.getText().toString());
        editor.putString(MainActivity.KEY_NOM, txtPays.getText().toString());
        editor.putString(MainActivity.KEY_PRENOM, txtPays.getText().toString());
        editor.putString(MainActivity.KEY_VILLE, txtPays.getText().toString());
        editor.putBoolean(MainActivity.KEY_SEXE_PARAM_F, tgSexef.isChecked());
        editor.putBoolean(MainActivity.KEY_SEXE_PARAM_H, tgSexeh.isChecked());
        editor.apply();
        super.onStop();
    }
}
