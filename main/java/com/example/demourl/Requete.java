package com.example.demourl;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Requete extends AsyncTask <URL, Void, JSONObject> { //classe permettant d'effectuer des requetes HTTP vers une URL specifiee et de traiter un resultat retourne sous la forme d'un object JSON

    private Quizz main;
    private Difficulte main2;


    public Requete(Quizz m) {
        main= m;
    }

    public Requete(Difficulte difficulte) {
        main2 = difficulte;
    }

    @Override
    protected JSONObject doInBackground(URL... urls) { //effectue la requete HTTP vers l'URL specifiee en parametre et passe le resultat a OnPostExecute
        URL u;
        URLConnection c;
        String inputline;
        StringBuilder codeHTML = new StringBuilder("");

        // On récupère l'url passée en paramètre de la méthode exécute
        u= urls[0];

        try {
            c= u.openConnection();  //temps maximun alloué pour se connecter
            c.setConnectTimeout(60000); //temps maximun alloué pour lire
            c.setReadTimeout(60000);    //flux de lecture avec l'encodage des caractères UTF-8
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(c.getInputStream(),"UTF-8"));
            while((inputline = in.readLine())!=null){   //concaténation+retour à la ligne avec \n
                codeHTML.append(inputline+"\n");
            }   //il faut bien fermer le flux de lecture
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return decodeJSON(codeHTML.toString() );
    }

    public JSONObject decodeJSON (String codeHTML) { //convertit une chaine de caractere (data de requete HTPP) en JSONObject
        String s;
        JSONObject jobj;
        JSONObject tobj = null;
        int vi;
        StringBuilder r;

        r= new StringBuilder("");

        try {

            jobj= new JSONObject( codeHTML );

            if (jobj.length() < 1){
                return new JSONObject();
            }

            try {

                String keys = jobj.keys().next();
                tobj = jobj.getJSONObject(keys);
                tobj.put("num", keys); // si l'object JSON de resultat est une question, alors on lui ajoute un champ pour stocker son ID, ce qui permettra de l'indiquer a l'affichage de la question

            }catch(JSONException e){ //On identifie le cas où un tableau d'ID de réponses est retourne
                return new JSONObject( codeHTML ) ;
            }



        } catch (JSONException e) {
            e.printStackTrace();
            tobj= new JSONObject();
        }
        return tobj ;
    }


    protected void onPostExecute (JSONObject question) { //methode executee une fois que DoInBackground est terminee

        String[] resultat = null;

        String num = null;
        String titre = null;
        String domaine = null;
        String intitule = null;
        String niv = null;

        String id_reponse = null;

        try { //Dans le cas d'une requete portant sur les ID de questions associees à une difficultee : on recupere les donnees du JSONObject
            id_reponse = question.getString("questions");
            String result = id_reponse.substring(id_reponse.indexOf("[") + 1, id_reponse.indexOf("]")); //retire les crochés
            String[] id = result.split(",");    //split la liste des réponses

            main2.recup_id_question(id);


        } catch (JSONException b) {


            try {   //Dans le cas d'une requete portant sur les ID de reponses associees à une question : on recupere les donnees du JSONObject
                id_reponse = question.getString("reponses"); //recupere la liste des id des réponses
                String result = id_reponse.substring(id_reponse.indexOf("[") + 1, id_reponse.indexOf("]")); //enleve les crochés
                String[] id = result.split(",");//split la liste des réponse

                main.affiche_bouton(id);

            } catch (JSONException | MalformedURLException e) {

                try { //Dans le cas d'une requete portant sur une question : on recupere les donnees du JSONObject
                    num = question.getString("num");
                    titre = question.getString("titre");
                    domaine = question.getString("domaine");
                    intitule = question.getString("enonce");

                    if(titre.equals(null) || titre.equals(" ")  || titre.equals("") ){ //pour prévoir les cas où il n'y a pas de titre
                        titre ="Sans titre";
                    }

                    resultat = new String[]{num,titre, intitule, domaine};

                    Quizz.affiche_question(resultat);

                } catch (JSONException c) { //Dans le cas d'une requete portant sur une reponse : on recupere les donnees du JSONObject

                    try {
                        Quizz.affiche_reponse(question.getString("texte"), question.getInt("juste"));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        }
    }
}

