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
                    getData();
                    addAndFinishIntent();
                    return true;
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };

    private void alertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Vous ");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(AjouterActivity.this, "You clicked yes button", Toast.LENGTH_LONG).show();
                    }
                });
        alertDialogBuilder.setNegativeButton("no",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(AjouterActivity.this, "You clicked yes button", Toast.LENGTH_LONG).show();
                        return;
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
                addAndFinishIntent();
            }
        });
    }

    private void addAndFinishIntent() {
        Intent request = new Intent(AjouterActivity.this, MainActivity.class);
        request.putExtra("KEY_NOM", nom.toString());
        request.putExtra("KEY_PRENOM", prenom.toString());
        request.putExtra("KEY_AGE", age.toString());
        request.putExtra("KEY_VILLE", ville.toString());
        request.putExtra("KEY_SEXE", sexe.getSelectedItem().toString());
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
