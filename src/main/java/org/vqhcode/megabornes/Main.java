package org.vqhcode.megabornes;

import com.google.gson.Gson;
import org.vqhcode.megabornes.model.*;
import org.vqhcode.megabornes.route.JoueurRoute;
import org.vqhcode.megabornes.route.PartieRoute;
import spark.ResponseTransformer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static spark.Spark.*;

/**
 * Created by jbcotard on 21/01/2017.
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        // initialisation technique pour services web
        Configuration configuration = Configuration.INSTANCE;
        ResponseTransformer encoder = object -> configuration.getGson().toJson(object);
        MegaBorne megaBorne = MegaBorne.INSTANCE;
        megaBorne.setListeJoueur(new ArrayList<Joueur>());
        megaBorne.setPartie(null);

        // initialisation circuit
        Gson gson = new Gson();
        URL resourceCircuit = Main.class.getClassLoader().getResource("circuit.json");
        Position[] positions = gson.fromJson(new FileReader(resourceCircuit.getPath()), Position[].class);
        megaBorne.setListePosition(Arrays.asList(positions));

        // initialisation cartes
        URL resourceCartes = Main.class.getClassLoader().getResource("cartes.json");
        Carte[] cartes = gson.fromJson(new FileReader(resourceCartes.getPath()), Carte[].class);
        JeuCarte jeuCartes = new JeuCarte();
        jeuCartes.setListeCartes(Arrays.asList(cartes));
        megaBorne.setJeuCartes(jeuCartes);

        // initialisation api joueurs
        JoueurRoute joueurRoute = new JoueurRoute();
        get("/api/joueurs",joueurRoute::getJoueurs,encoder);
        get("/api/joueurs/:jid",joueurRoute::getJoueur,encoder);
        get("/api/joueurs/generate/",joueurRoute::genJoueur,encoder);

        // initialisation api parties
        PartieRoute partieRoute = new PartieRoute();
        get("/api/parties",partieRoute::getParties,encoder);
        get("/api/parties/:pid",partieRoute::getPartie,encoder);
        get("/api/parties/inscrire/:jid",partieRoute::inscrireJoueur,encoder);
        get("/api/parties/joueurs/",partieRoute::getJoueursInscrits,encoder);
        get("/api/parties/joueurs/:jid",partieRoute::getJoueur,encoder);



        // CORS
        options("/*", (request, response) -> "");
        after((request, response) -> {
            response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
            response.header("Access-Control-Allow-Credentials", "true");
        });

    }




    public void toto () {


        //initialisation du jeu de cartes (distribution fixe)
        JeuCarte jeuCarte = MegaBornesJeuCarte.buildJeuCartes();

        // creation d'une partie

        //Partie partie = new Partie.Build().build();
        Partie partie = new Partie();

        // ajout du jeu de cartes à la partie
        partie.addJeuCartes(jeuCarte);

        // ajout des joueurs
        Joueur joueur1 = new Joueur("1","toto");
        Joueur joueur2 = new Joueur("2","bozo");;
        partie.addJoueur(joueur1);
        partie.addJoueur(joueur2);

        // demarrage d'une partie
        partie.start();

        while (partie.isTerminee()) {



            // pour chaque joueur

            Carte carte = null;

            partie.actionJoueur(partie.getJoueurCourant(),carte);



            partie.joueurSuivant();


        }

        partie.stop();


    }
}
