package org.vqhcode.megabornes;

import com.google.gson.Gson;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.vqhcode.megabornes.model.*;
import org.vqhcode.megabornes.route.JoueurRoute;
import org.vqhcode.megabornes.route.PartieRoute;
import spark.ResponseTransformer;
import sun.net.www.protocol.file.Handler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static spark.Spark.*;

/**
 * Created by jbcotard on 21/01/2017.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        // initialisation technique pour services web
        Configuration configuration = Configuration.INSTANCE;
        ResponseTransformer encoder = object -> configuration.getGson().toJson(object);
        MegaBorne megaBorne = MegaBorne.INSTANCE;
        megaBorne.setListeJoueur(new ArrayList<Joueur>());
        megaBorne.setPartie(null);

        // initialisation circuit
        Gson gson = new Gson();
        URL resourceCircuit = Main.class.getClassLoader().getResource("circuitcomplet.json");
        Position[] positions = gson.fromJson(new FileReader(resourceCircuit.getPath()), Position[].class);
        megaBorne.setListePosition(Arrays.asList(positions));

        /*
        List<String> listeTypeCommerce = Arrays.asList("Hopital", "Gare", "Gendarmerie", "Police", "Prefecture", "Carrossier", "Station%20Essence", "Garagiste",
                "DDE", "Mairie", "Bar");

        for (Position position : megaBorne.getListePosition()) {
            for (String typeCommerce:listeTypeCommerce) {

                URL urlClient = new URL("https://api.apipagesjaunes.fr/pros/find?what=" + typeCommerce + "&where=cZ" + position.getLongitude() + "," + position.getLatitude() +"&app_id=d140a6f6&app_key=26452728b034374bccb462e880bfb0e5&return_urls=false&proximity=true&max=1&page=1");
                HttpURLConnection connection = (HttpURLConnection) urlClient.openConnection();
                connection.setRequestMethod("GET");
                //connection.setDoOutput(true);
                InputStream content = (InputStream) connection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = in.readLine()) != null) {
                    //System.out.println(line);
                    if (line.contains("inscriptions")) {
                        String substring = line.substring(line.indexOf("inscriptions"));
                        //System.out.println(substring);
                        String resultatIndexLatitude = substring.substring(substring.indexOf("latitude"));
                        String latitudeCommercce = resultatIndexLatitude.substring(resultatIndexLatitude.indexOf("latitude") + 10, resultatIndexLatitude.indexOf("longitude") - 2);


                        //System.out.println(latitudeCommercce);
                        String longitudeCommerce = resultatIndexLatitude.substring(resultatIndexLatitude.indexOf("longitude") + 11, resultatIndexLatitude.indexOf("distance") - 2);
                        //System.out.println(longitudeCommerce);
                        String distanceCommerce = resultatIndexLatitude.substring(resultatIndexLatitude.indexOf("distance") + 10);
                        distanceCommerce = distanceCommerce.substring(0, distanceCommerce.indexOf(","));
                        //System.out.println(distanceCommerce);
                        //System.out.println(typeCommerce);

                        Commerce commerce = new Commerce(typeCommerce, longitudeCommerce, latitudeCommercce, distanceCommerce);
                        position.addCommerce(commerce);
                    }
                }
            }
        }

*/
        
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
        get("/api/parties/reset/",partieRoute::resetPartie,encoder);
        get("/api/parties/inscrire/:jid",partieRoute::inscrireJoueur,encoder);
        get("/api/parties/joueurs/",partieRoute::getJoueursInscrits,encoder);
        get("/api/parties/joueurs/:jid",partieRoute::getJoueur,encoder);
        get("/api/parties/joueurs/:jid/action/:idCarte/:defausse/:jidAdversaire", partieRoute::processActionJoueur,encoder);
        get("/api/parties/joueurs/:jid/pioche/:typeCommerce", partieRoute::processPioche, encoder);

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
