package com.tp.meteoinconnu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetailActivity extends AppCompatActivity {

    BottomNavigationView navView;
    Bundle bundle;
    TextView txtnom, txtprenom, txtville, txtage, txtsexe, txttemperature, txthumidite, txtpays;

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

        txtage.setText(bundle.getString(MainActivity.KEY_AGE));
        txtnom.setText(bundle.getString(MainActivity.KEY_NOM));
        txtpays.setText(bundle.getString(MainActivity.KEY_PAYS));
        txtprenom.setText(bundle.getString(MainActivity.KEY_PRENOM));
        txtsexe.setText(bundle.getString(MainActivity.KEY_SEXE));
        txtville.setText(bundle.getString(MainActivity.KEY_VILLE));
    }
}
