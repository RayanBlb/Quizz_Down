package com.example.demourl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity { //toute premiere activite affichee au demarrage de l'app, avec boutons "Voir réglement" et "Jouer"


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

    }

    public void onClickGO (View view) {
        startActivity(new Intent(this, Difficulte.class));
    }

    public void onClickReglement (View view) {
        startActivity(new Intent(this, Reglement.class));
    }
}


