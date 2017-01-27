package org.vqhcode.megabornes.route;

import org.vqhcode.megabornes.MegaBorne;
import org.vqhcode.megabornes.model.*;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jbcotard on 21/01/2017.
 */
public class PartieRoute {

    public static final int PARTIE_NB_JOUEUR = 2;

    public List<Partie> getParties(Request request, Response response) {
        List<Partie> listeParties = new ArrayList<Partie>();
        listeParties.add(MegaBorne.INSTANCE.getPartie());
        return listeParties;
    }

    public Partie getPartie(Request request, Response response) {
        Partie partie = MegaBorne.INSTANCE.getPartie();
        return partie;
    }

    public Partie inscrireJoueur(Request request, Response response) {
        Partie partie = MegaBorne.INSTANCE.getPartie();
        String jid = request.params("jid");

        // si elle n'existe pas, on la crée
        if (partie == null) {
            partie = new Partie();

            // initialisation du jeu de cartes (distribution fixe)
            partie.addJeuCartes(MegaBorne.INSTANCE.getJeuCartes());

            // initialisation du circuit
            Circuit circuit = new Circuit();
            circuit.setListePositions(MegaBorne.INSTANCE.getListePosition());
            partie.setCircuit(circuit);

            MegaBorne.INSTANCE.setPartie(partie);

            System.out.println("Création de la partie [" + 1 + "]" );
        }
        Joueur joueur1 = new Joueur(jid, jid);
        //System.out.println(joueur1);
        //MegaBorne.INSTANCE.getListeJoueur().stream().forEach(System.out::println);
        if (!partie.getListeJoueurs().contains(joueur1)
                && MegaBorne.INSTANCE.getListeJoueur().stream().filter(joueur -> joueur.getId().contains(joueur1.getId())).findFirst().orElse(null) != null) {
            // ajout du joueur s'il n'existe pas
            // et qu'il existe bien dans la liste des joueurs generes
            partie.addJoueur(joueur1);
            System.out.println("Inscription du joueur [" + jid + "]" );
        }

        // si la partie n'est pas demarrée et si le nombre de joueur est > 2
        // la partie démarre
        if (!partie.isEnCours() && partie.getListeJoueurs().size() == PARTIE_NB_JOUEUR) {
            partie.getListeJoueurs().forEach(joueur -> joueur.setEtat(EtatJoueur.inactif));
            System.out.println("Start de la partie [" + 1 + "]" );
            partie.start();
            System.out.println("Tour du joueur [" + partie.getJoueurCourant().getId() + "]" );
        }

        return partie;
    }

    public List<Joueur> getJoueursInscrits(Request request, Response response) {
        return MegaBorne.INSTANCE.getPartie().getListeJoueurs();
    }

    public Joueur getJoueur(Request request, Response response) {

        String jid = request.params("jid");
        return MegaBorne.INSTANCE.getPartie().getListeJoueurs().stream().collect(HashMap<String, Joueur>::new, (m, c) -> m.put(c.getId(), c),
                (m, u) -> {
                }).get(jid);
    }

    public String resetPartie(Request request, Response response) {
        MegaBorne.INSTANCE.setPartie(null);
        MegaBorne.INSTANCE.setListeJoueur(null);
        System.out.println("Reset de la partie [" + 1 + "]" );
        return "reset";
    }

    public int processActionJoueur(Request request, Response response) {

        // input carte + joueur + adversaire + defausse
        String jid = request.params("jid");

        String jidAdversaire = request.params("jidAdversaire");
        String idCarte = request.params("idCarte");
        boolean defausse = Boolean.valueOf(request.params("defausse"));


        StringBuilder msgOut = new StringBuilder();
        msgOut.append("  >> joueur [").append(jid).append("] joue la carte [").append(idCarte).append("] ").toString();
        if (!"toto".equals(jidAdversaire)) {
            msgOut.append("  contre le joueur [").append(jidAdversaire).append("] ").toString();
        }
        if (defausse) {
            msgOut.append(" => defausse "  );
        }
        System.out.println(msgOut.toString()  );


        int resultat = MegaBorne.INSTANCE.getPartie().processActionJoueur(jid,idCarte,defausse,jidAdversaire);

        if (resultat == 0) {
            System.out.println(" >> action validée ! ");
            MegaBorne.INSTANCE.getPartie().joueurSuivant();
            System.out.println("Tour du joueur [" + MegaBorne.INSTANCE.getPartie().getJoueurCourant().getId() + "]" );
        } else {
            System.out.println(" >> action non validée : même joueur joue encore ! ");
        }



        return resultat;
    }

    public Carte processPioche(Request request, Response response) {
        String jid = request.params("jid");
        String typeCommerce = request.params("typeCommerce");

        Carte carte = MegaBorne.INSTANCE.getPartie().processPioche(jid,typeCommerce);

        System.out.println("  >> joueur [" + jid + "] pioche chez [" + typeCommerce + "] la carte [" + carte + "]" );

        return carte;
    }
}
