package com.example.demourl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class Difficulte extends AppCompatActivity { //activite correspondant au choix du niveau des questions auquel on veut se confronter

    public static ArrayList<String> requeteListe = new ArrayList<>();


    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dif_page);
    }

    public void difficulte_choix(View v) {

        switch (v.getId()) { //en fonction du bouton de difficulte choisi

            case R.id.DUT:
                requete_id_question_difficulte(0);
                startActivity(new Intent(this, Quizz.class));
                break;

            case R.id.DUT1:
                requete_id_question_difficulte(1);
                startActivity(new Intent(this, Quizz.class));
                break;

            case R.id.DUT2:
                requete_id_question_difficulte(2);
                startActivity(new Intent(this, Quizz.class));
                break;
        }


        }

        public void requete_id_question_difficulte(int niveau){ //methode permettant d'effectuer une requete HTTP en vue d'obtenir l'ensemble des questions au niveau de difficulte selectionne (ex : DUT2)

            requeteListe.clear(); //vide l'arrayList contenant d'anciens ID de questions valides

            URL recupe_liste_id_question = null;

            try {
                recupe_liste_id_question = new URL("http://infort.gautero.fr/index.php?action=get&obj=question&niv="+niveau);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            HTTP_Get(recupe_liste_id_question);
        }

    public void HTTP_Get (URL url){ //methode creant une requete HTTP asynchrone et l'executant en lui passant l'URL de destination

        Requete une_tache_de_requete = new Requete(this);
        une_tache_de_requete.execute(url);
    }

    public void recup_id_question (String[] id){ //ajoute le tableau des ID des questions dans une ArrayList
        Collections.addAll(requeteListe, id);

    }


}
