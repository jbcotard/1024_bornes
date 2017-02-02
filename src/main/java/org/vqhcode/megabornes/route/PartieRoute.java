package org.vqhcode.megabornes.route;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.vqhcode.megabornes.Configuration;
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

    public List<Partie> getParties(Request request, Response response) {
        //List<Partie> listeParties = new ArrayList<Partie>();
        //listeParties.add(MegaBorne.INSTANCE.getPartie());
        //return listeParties;
        return MegaBorne.INSTANCE.getListeParties();
    }

    public Partie getPartie(Request request, Response response) {
        String pid = request.params("pid");
        Partie partie = MegaBorne.INSTANCE.getListeParties().get(Integer.parseInt(pid));
        return partie;
    }

    public Partie inscrireJoueur(Request request, Response response) {


        //Partie partie = MegaBorne.INSTANCE.getPartie();

        Partie partie = MegaBorne.INSTANCE.getPartieDisponible();


        String jid = request.params("jid");

        // si elle n'existe pas et que le nombre de parties n'a pas atteint le max,
        // on en crée une nouvelle
        if (partie == null && MegaBorne.INSTANCE.getListeParties().size() < Configuration.INSTANCE.getNbParties()) {
            partie = new Partie();
            partie.setPartieId(MegaBorne.INSTANCE.getListeParties().size());

            // initialisation du jeu de cartes (distribution fixe)
            partie.addJeuCartes(MegaBorne.INSTANCE.getJeuCartes());

            // configuration de la partie
            partie.setNbBornesAParcourrir(Configuration.INSTANCE.getNbBornes());
            partie.setNbJoueursMax(Configuration.INSTANCE.getNbJoueurs());

            // initialisation du circuit
            Circuit circuit = new Circuit();
            circuit.setListePositions(MegaBorne.INSTANCE.getListePosition());
            partie.setCircuit(circuit);

            MegaBorne.INSTANCE.addPartie(partie);

            System.out.println("Création de la partie [" + partie.getPartieId() + "]" );
        } else if (partie != null) {

            Joueur joueur1 = new Joueur(jid, jid);
            //System.out.println(joueur1);
            //MegaBorne.INSTANCE.getListeJoueur().stream().forEach(System.out::println);
            if (!partie.getListeJoueurs().contains(joueur1)
                    && MegaBorne.INSTANCE.getListeJoueur().stream().filter(joueur -> joueur.getId().contains(joueur1.getId())).findFirst().orElse(null) != null) {
                // ajout du joueur s'il n'existe pas
                // et qu'il existe bien dans la liste des joueurs generes
                partie.addJoueur(joueur1);
                System.out.println("Inscription du joueur [" + jid + "]");
            }

            // si la partie n'est pas demarrée et si le nombre de joueur est > 2
            // la partie démarre
            if (!partie.isEnCours()
                    && partie.getListeJoueurs().size() == Configuration.INSTANCE.getNbJoueurs()) {
                partie.getListeJoueurs().forEach(joueur -> joueur.setEtat(EtatJoueur.inactif));
                System.out.println("Start de la partie [" + 1 + "]");
                partie.start();
                System.out.println("Tour du joueur [" + partie.getJoueurCourant().getId() + "]");
            }
        }

        return partie;
    }

    public List<Joueur> getJoueursInscrits(Request request, Response response) {

        String pid = request.params("pid");
        // si aucun pid alors on prend la partie 0
        if (pid == null) {
            pid = "0";
        }

        return MegaBorne.INSTANCE.getListeParties().get(Integer.parseInt(pid)).getListeJoueurs();
    }

    public Joueur getJoueur(Request request, Response response) {

        String jid = request.params("jid");
        String pid = request.params("pid");
        // si aucun pid alors on prend la partie 0
        if (pid == null) {
            pid = "0";
        }
        return MegaBorne.INSTANCE.getListeParties().get(Integer.parseInt(pid)).getListeJoueurs().stream().collect(HashMap<String, Joueur>::new, (m, c) -> m.put(c.getId(), c),
                (m, u) -> {
                }).get(jid);
    }

    public String resetPartie(Request request, Response response) {

        String pid = request.params("pid");
        // si aucun pid alors on prend la partie 0
        if (pid == null) {
            pid = "0";
        }
        MegaBorne megaBorne = MegaBorne.INSTANCE;
        megaBorne.setListeJoueur(new ArrayList<Joueur>());
        megaBorne.setListeParties(new ArrayList<Partie>());
        System.out.println("Reset de la partie [" + pid + "]" );
        return "reset";
    }

    public int processActionJoueur(Request request, Response response) {

        // input carte + joueur + adversaire + defausse
        String jid = request.params("jid");
        String jidAdversaire = request.params("jidAdversaire");
        String idCarte = request.params("idCarte");
        boolean defausse = Boolean.valueOf(request.params("defausse"));
        String pid = request.params("pid");
        // si aucun pid alors on prend la partie 0
        if (pid == null) {
            pid = "0";
        }


        int resultat = MegaBorne.INSTANCE.getListeParties().get(Integer.parseInt(pid)).processActionJoueur(jid,idCarte,defausse,jidAdversaire);

        if (resultat == 0) {
            //System.out.println(" >> action validée ! ");

            System.out.printf(" >> joueur [%s] a parcourru %d km - %s%n",
                    MegaBorne.INSTANCE.getListeParties().get(Integer.parseInt(pid)).getJoueurCourant().getId(),
                    MegaBorne.INSTANCE.getListeParties().get(Integer.parseInt(pid)).getJoueurCourant().getNbBornesParcourues(),
                    MegaBorne.INSTANCE.getListeParties().get(Integer.parseInt(pid)).getJoueurCourant().getPosition().toStringWithoutListeCommerces());

            MegaBorne.INSTANCE.getListeParties().get(Integer.parseInt(pid)).joueurSuivant();
            System.out.println("Partie [" + pid + "] Tour du joueur [" + MegaBorne.INSTANCE.getListeParties().get(Integer.parseInt(pid)).getJoueurCourant().getId() + "]" );
        } else {
            System.out.println(" >> action non validée : même joueur joue encore ! ");
        }



        return resultat;
    }

    public Carte processPioche(Request request, Response response) {
        String jid = request.params("jid");
        String typeCommerce = request.params("typeCommerce");
        String pid = request.params("pid");
        // si aucun pid alors on prend la partie 0
        if (pid == null) {
            pid = "0";
        }

        Carte carte = MegaBorne.INSTANCE.getListeParties().get(Integer.parseInt(pid)).processPioche(jid,typeCommerce);

        System.out.println("  > Partie [" + pid + "] joueur [" + jid + "] pioche chez [" + typeCommerce + "] la carte [" + carte + "]" );

        return carte;
    }
}
