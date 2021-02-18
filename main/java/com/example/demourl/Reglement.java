package com.example.demourl;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Reglement extends AppCompatActivity { //activite permettant l'affichage du reglement du jeu

    private static TextView reglement;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reglement_page);
        reglement = findViewById(R.id.Reglement);
        reglement.setText("Bonjour et bienvenue sur \"Quizz Down\", le but du jeu ici est d'aller " +
                "le plus loin possible dans les questions sans se tromper plus de 3 fois. \n" +
                "De nombreuses questions concernant la formation R&T vont vous être posées !! ");
    }

}
