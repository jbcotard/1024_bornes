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

    public static final int PARTIE_NB_JOUEUR = 3;
    public static final int NB_PARTIES = 1;
    public static final int NB_BORNES = 1024;

    public static void main(String[] args) throws IOException {

        // configuration
        Configuration configuration = Configuration.INSTANCE;
        ResponseTransformer encoder = object -> configuration.getGson().toJson(object);
        configure(args, configuration);


        // initialisation technique pour services web
        MegaBorne megaBorne = MegaBorne.INSTANCE;
        megaBorne.setListeJoueur(new ArrayList<Joueur>());
        megaBorne.setListeParties(new ArrayList<Partie>());


        // Frontend
        staticFileLocation("/public");

        // initialisation circuit
        Gson gson = new Gson();
        // FIX lecture ressource dans jar
        InputStream resourceCircuit = ClassLoader.getSystemResourceAsStream("circuitcomplet.json");
        Position[] positions = gson.fromJson(new BufferedReader(new InputStreamReader(resourceCircuit)), Position[].class);
        //URL resourceCircuit = Main.class.getClassLoader().getResource("circuitcomplet.json");
        //Position[] positions = gson.fromJson(new FileReader(resourceCircuit.getPath()), Position[].class);
        megaBorne.setListePosition(Arrays.asList(positions));

        // initialisation cartes
        // FIX lecture ressource dans jar
        InputStream resourceCartes = ClassLoader.getSystemResourceAsStream("cartes.json");
        Carte[] cartes = gson.fromJson(new BufferedReader(new InputStreamReader(resourceCartes)), Carte[].class);
        //URL resourceCartes = Main.class.getClassLoader().getResource("cartes.json");
        //Carte[] cartes = gson.fromJson(new FileReader(resourceCartes.getPath()), Carte[].class);
        JeuCarte jeuCartes = new JeuCarte();
        jeuCartes.setListeCartes(Arrays.asList(cartes));
        megaBorne.setJeuCartes(jeuCartes);

        // initialisation api joueurs
        JoueurRoute joueurRoute = new JoueurRoute();
        get("/api/joueurs", joueurRoute::getJoueurs, encoder);
        get("/api/joueurs/:jid", joueurRoute::getJoueur, encoder);
        get("/api/joueurs/generate/", joueurRoute::genJoueur, encoder);

        // initialisation api parties
        PartieRoute partieRoute = new PartieRoute();
        get("/api/parties", partieRoute::getParties, encoder);
        get("/api/parties/:pid", partieRoute::getPartie, encoder);
        get("/api/parties/reset/", partieRoute::resetPartie, encoder);
        get("/api/parties/inscrire/:jid", partieRoute::inscrireJoueur, encoder);
        get("/api/parties/joueurs/", partieRoute::getJoueursInscrits, encoder);
        get("/api/parties/joueurs/:jid", partieRoute::getJoueur, encoder);
        get("/api/parties/joueurs/:jid/action/:idCarte/:defausse/:jidAdversaire", partieRoute::processActionJoueur, encoder);
        get("/api/parties/joueurs/:jid/pioche/:typeCommerce", partieRoute::processPioche, encoder);


        // api v2 : mode multi parties
        get("/apiv2/parties/:pid/reset/", partieRoute::resetPartie, encoder);
        get("/apiv2/parties/:pid/inscrire/:jid", partieRoute::inscrireJoueur, encoder);
        get("/apiv2/parties/:pid/joueurs/", partieRoute::getJoueursInscrits, encoder);
        get("/apiv2/parties/:pid/joueurs/:jid", partieRoute::getJoueur, encoder);
        get("/apiv2/parties/:pid/joueurs/:jid/action/:idCarte/:defausse/:jidAdversaire", partieRoute::processActionJoueur, encoder);
        get("/apiv2/parties/:pid/joueurs/:jid/pioche/:typeCommerce", partieRoute::processPioche, encoder);



        // CORS
        options("/*", (request, response) -> "");
        after((request, response) -> {
            response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
            response.header("Access-Control-Allow-Credentials", "true");
        });

    }

    private static void configure(String[] args, Configuration configuration) {
        // si 3 arguments sont renseignés
        // alors il s'agit du format : <nb parties> <nb joueurs> <nb bornes>
        if (args != null && args.length == 3) {
            //nbParties = args[0];
            try {
                configuration.setNbParties(Integer.valueOf(args[0]));
                //nbJoueurs = args[1];
                configuration.setNbJoueurs(Integer.valueOf(args[1]));
                //nbBornes = args[2];
                configuration.setNbBornes(Integer.valueOf(args[2]));
            } catch (NumberFormatException e) {


            }
        }
        // Par défaut
        configuration.setNbParties(configuration.getNbParties() == null || configuration.getNbParties() == 0 ? NB_PARTIES : configuration.getNbParties());
        configuration.setNbJoueurs(configuration.getNbJoueurs() == null || configuration.getNbJoueurs() == 0 ? PARTIE_NB_JOUEUR : configuration.getNbJoueurs());
        configuration.setNbBornes(configuration.getNbBornes() == null || configuration.getNbBornes() == 0 ? NB_BORNES : configuration.getNbBornes());

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++ configuration Moteur : \n\tnb Parties simultanées [" + configuration.getNbParties() + "], \n\tnb joueurs max par partie [" + configuration.getNbJoueurs() + "], \n\tnb bornes to win [" + configuration.getNbBornes() + "]");

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    private void initDataPagesJaunes() {
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
    }
}
