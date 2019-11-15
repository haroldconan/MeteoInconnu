package com.tp.meteoinconnu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AjouterActivity extends AppCompatActivity {
    public EditText nom, prenom, age, ville;
    public Spinner sexe;
    public Button btnValider;
    public AlertDialog.Builder alertDialogBuilder;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return alertDialog();
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };

    private boolean alertDialog() {
        final boolean[] quitter = {false};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Etes vous sur de vouloir quitter ? vous aller perdre vos données ");
        alertDialogBuilder.setPositiveButton("oui",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent request = new Intent(AjouterActivity.this, MainActivity.class);
                        setResult(AjouterActivity.RESULT_CANCELED, request);
                        finish();
                        quitter[0] = true;

                    }
                });
        alertDialogBuilder.setNegativeButton("non",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return quitter[0];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getData();
        btnValider = findViewById(R.id.btnFinishAdd);
        alertDialogBuilder = new AlertDialog.Builder(this);
        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAndFinishIntent();
            }
        });
    }

    private void addAndFinishIntent() {
        if(TextUtils.isEmpty(nom.getText().toString())){
            nom.setError("Ne peut pas être vide.");
            return;
        }
        if(TextUtils.isEmpty(prenom.getText().toString())){
            prenom.setError("Ne peut pas être vide.");
            return;
        }
        if(TextUtils.isEmpty(age.getText().toString())){
            age.setError("Ne peut pas être vide.");
            return;
        }
        if(TextUtils.isEmpty(ville.getText().toString())){
            ville.setError("Ne peut pas être vide.");
            return;
        }
        Intent request = new Intent(AjouterActivity.this, MainActivity.class);
        request.putExtra(MainActivity.KEY_NOM, nom.getText().toString());
        request.putExtra(MainActivity.KEY_PRENOM, prenom.getText().toString());
        request.putExtra(MainActivity.KEY_AGE, age.getText().toString());
        request.putExtra(MainActivity.KEY_VILLE, ville.getText().toString());
        request.putExtra(MainActivity.KEY_SEXE, sexe.getSelectedItem().toString());
        setResult(AjouterActivity.RESULT_OK, request);
        finish();
    }

    private void getData() {
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        age = findViewById(R.id.age);
        ville = findViewById(R.id.ville);
        sexe = findViewById(R.id.sexe);
    }

}
