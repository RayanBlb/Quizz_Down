package com.example.demourl;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import static com.example.demourl.Difficulte.requeteListe;


public class Quizz extends AppCompatActivity { //activite correspondant a la page de jeu où sont affichees la question et les reponses

    static int Score_int = 0;
    static int nombre_id_Reponse = 0;
    static int Nombre_reponse = 0;
    static int vie_int = 3;

    static ArrayList<String> Reponse_Juste = new ArrayList<String>();

    private static TextView num_q;
    private static TextView titre_q;
    private static TextView intitule_q;
    private static TextView domaine_q;
    private static TextView niveau_q;
    private static TextView resultat_correction;
    private static TextView Score_affiche;
    private static TextView Vie_affiche;

    private static Button btn1;
    private static Button btn2;
    private static Button btn3;
    private static Button btn4;
    private static Button repAucune;
    private static Button bouton_suivant;

    private Requete une_tache_de_requete;



    protected void onCreate (Bundle savedInstanceState) { //initialisation des widget present sur cet affichage
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vie_int = 3;
        Score_int = 0;

        bouton_suivant = findViewById(R.id.bouton_suivant);
        num_q= findViewById(R.id.num_q);
        titre_q= findViewById(R.id.titre_q);
        intitule_q= findViewById(R.id.intitule_q);
        domaine_q= findViewById(R.id.domaine_q);
        btn1 = findViewById(R.id.Rep1);
        btn2 = findViewById(R.id.Rep2);
        btn3 =  findViewById(R.id.Rep3);
        btn4 = findViewById(R.id.Rep4);
        repAucune = findViewById(R.id.RepAucune);
        resultat_correction = findViewById(R.id.resultat_correction);
        Score_affiche = findViewById(R.id.Score);
        Vie_affiche = findViewById(R.id.Vie);
    }

    public void question_suivante (View v){ //methode permettant la requete & l'initialisation de l'affichage d'une nouvelle question

        bouton_suivant.setText("QUESTION SUIVANTE"); //permet de renommer le bouton "QUESTION SUIVANTE" en "QUESTION SUIVANTE" lorsqu'il se transforme en bouton "REJOUER"

        View score_affiche = findViewById(R.id.Score); //affiche & met a jour le score
        score_affiche.setVisibility(View.VISIBLE);
        Score_affiche.setText("Score : "+String.valueOf(Score_int));

        View vie_affiche = findViewById(R.id.Vie); // affiche & met a jour le compteur de vie
        vie_affiche.setVisibility(View.VISIBLE);
        Vie_affiche.setText("Vie : "+String.valueOf(vie_int));

        View resultat_affiche = findViewById(R.id.resultat_correction); //permet de cacher le carré qui affiche pour indiquer si la reponse est juste ou fausse
        resultat_affiche.setVisibility(View.INVISIBLE);

        Nombre_reponse=0; //permet de reinitialiser le compteur du nombre de réponse qui s'incrémente apres l'affichage de chaque réponse
        Reponse_Juste.clear(); //permet de clear la liste des reponses juste d'une question


        URL recup_question = null;
        URL recupe_liste_id_reponse = null;

        Random rand = new Random();
        String id = requeteListe.get(rand.nextInt(requeteListe.size()));

        try { //url relative a la requete d'une question
            recup_question = new URL("http://infort.gautero.fr/index.php?action=get&obj=question&id="+id);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try { //url relative a la requete des id des reponses correspondants a une question
            recupe_liste_id_reponse = new URL("http://infort.gautero.fr/index.php?action=get&obj=reponse&idQuestion="+id);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        HTTP_Get(recup_question);

        HTTP_Get(recupe_liste_id_reponse);
    }

    public void HTTP_Get (URL url){     //methode creant une requete HTTP asynchrone et l'executant en lui passant l'URL de destination
        une_tache_de_requete = new Requete(this);
        une_tache_de_requete.execute(url);
    }


    public static void affiche_question(String[] resultat) { //methode permettant de remplir les differentes zones de texte pour afficher la question
        num_q.setText("n°" + resultat[0]);
        num_q.setVisibility(View.VISIBLE);
        intitule_q.setText(resultat[2]);
        titre_q.setText("Titre : "+resultat[1]);
        domaine_q.setText("Domaine : "+resultat[3]);
    }


    public void affiche_bouton(String[] id) throws MalformedURLException { //methode permettant de rendre visible le nombre de bouton en fonction du nombre de reponses possibles, et de requeter chacune des reponses en vue de les afficher

        nombre_id_Reponse = id.length;

        //on rend tout les boutons invisibles au cas ou ceux-ci ne l'etaient pas suite a une precedente question
        btn1.setVisibility(View.GONE);
        btn2.setVisibility(View.GONE);
        btn3.setVisibility(View.GONE);
        btn4.setVisibility(View.GONE);
        repAucune.setVisibility(View.GONE);


        if(nombre_id_Reponse == 1){
            btn1.setVisibility(View.VISIBLE);
            repAucune.setVisibility(View.VISIBLE);


        }else if(nombre_id_Reponse == 2){
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            repAucune.setVisibility(View.VISIBLE);

        }else if(nombre_id_Reponse == 3){
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.VISIBLE);
            repAucune.setVisibility(View.VISIBLE);

        }else if(nombre_id_Reponse == 4){

            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.VISIBLE);
            btn4.setVisibility(View.VISIBLE);
            repAucune.setVisibility(View.VISIBLE);
        }

        for(String id_reponse : id){ //pour chaque ID de reponse possible pour la question
            URL recup_reponse = new URL("http://infort.gautero.fr/index.php?action=get&obj=reponse&id="+id_reponse); //on prepare une requete pour obtenir les donnees de la reponse (texte et validite)
            une_tache_de_requete = new Requete(this);
            une_tache_de_requete.execute(recup_reponse); //on execute cette requete de maniere asynchrone
        }

    }


    public static void affiche_reponse(String texte, int validite){ //après chaque affichage d'une réponse la variable "Nombre_reponse" s'incrémente de 1 ce qui permet d'afficher toutes les réponses d'une question
        Nombre_reponse++;
        if (validite == 1){ //si la reponse affichee est une bonne reponse elle est ajoute dans un tableau qui les contient toutes
            Reponse_Juste.add(texte);
        }

        if(Nombre_reponse==1){
            btn1.setText(texte);
        }else if(Nombre_reponse==2){
            btn2.setText(texte);
        }else if(Nombre_reponse==3){
            btn3.setText(texte);
        }else if(Nombre_reponse==4){
            btn4.setText(texte);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void correction_reponse(View v) { //permet de savoir si la reponse choisie par le joueur est bonne ou non

        switch (v.getId()) { //en fonction de l'ID du bouton choisit
            case R.id.Rep1:
                if(Reponse_Juste.size() == 0){

                    invisible_bouton();
                    reponse_fausse();
                } else {
                    for (String resultat : Reponse_Juste) {
                        if (btn1.getText() == resultat) { //si la réponse choisi est contenu dans le tableau Réponse_juste alors on fait disparaître les différentes réponses de la question et on affiche un message afin de faire comprendre que le joueur a choisi la bonne réponse

                            invisible_bouton();
                            reponse_juste();
                        } else {

                            invisible_bouton();
                            reponse_fausse();
                        }
                    }
                }

                break;

            case R.id.Rep2:
                if(Reponse_Juste.size() == 0){
                    invisible_bouton();
                    reponse_fausse();

                } else {
                    for (String resultat : Reponse_Juste) {
                        if (btn2.getText() == resultat) {
                            invisible_bouton();
                            reponse_juste();

                        } else {
                            invisible_bouton();

                            reponse_fausse();
                        }
                    }
                }

                break;

            case R.id.Rep3:
                if(Reponse_Juste.size() == 0){

                    invisible_bouton();

                    reponse_fausse();

                }else {

                    for (String resultat : Reponse_Juste) {
                        if (btn3.getText() == resultat) {
                            invisible_bouton();
                            reponse_juste();

                        } else {
                            invisible_bouton();
                            reponse_fausse();
                        }
                    }
                }
                break;

            case R.id.Rep4:
                if(Reponse_Juste.size() == 0){
                    invisible_bouton();
                    reponse_fausse();

                }else {

                    for (String resultat : Reponse_Juste) {
                        if (btn4.getText() == resultat) {
                            invisible_bouton();
                            reponse_juste();

                        } else {

                            invisible_bouton();
                            reponse_fausse();
                        }
                    }
                }

                break;

            case R.id.RepAucune:
                    if(Reponse_Juste.size() == 0){
                        invisible_bouton();
                        reponse_juste();

                    }else{
                        invisible_bouton();
                        reponse_fausse();

                    }
                break;

        }

        if(vie_int<=0){ //s'il ne reste plus de vie au joueur alors la partie se termine et propose de rejouer
            invisible_bouton();
            bouton_suivant.setText("Rejouer !!");

            View resultat_affiche = findViewById(R.id.resultat_correction);
            resultat_affiche.setVisibility(View.VISIBLE);
            resultat_correction.setText("merci au revoir, tu n'as plus de vies :'(\n"+
                    "Ton score : "+Score_int);

            vie_int=3;
            Score_int=0;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void reponse_fausse(){ // methode faisant apparaitre un message d'echec et retirant une vie au joueur

        View resultat_affiche = findViewById(R.id.resultat_correction);
        resultat_affiche.setVisibility(View.VISIBLE);
        if (Reponse_Juste.size() == 0)
            resultat_correction.setText("Misère de misère tu t'es trompé, il n'y avait pas de bonne réponse...");
        else
            resultat_correction.setText("Misère de misère tu t'es trompé, la bonne réponse était : "+String.join(" ou ", Reponse_Juste));
        vie_int--; //decremente le nombre de vies
    }

    public void reponse_juste(){ // methode faisant apparaitre un message de succes et ajoutant un point au joueur

        View resultat_affiche = findViewById(R.id.resultat_correction);
        resultat_affiche.setVisibility(View.VISIBLE);
        resultat_correction.setText("Bien joué tu gagnes 1 point !!");
        Score_int++; //incremente le score
    }

    public void invisible_bouton(){ //methode permettant de rendre invisible les 4 boutons de réponses + le dernier "Aucune Reponse"

        btn1.setVisibility(View.INVISIBLE);
        btn2.setVisibility(View.INVISIBLE);
        btn3.setVisibility(View.INVISIBLE);
        btn4.setVisibility(View.INVISIBLE);
        repAucune.setVisibility(View.INVISIBLE);
    }



}
