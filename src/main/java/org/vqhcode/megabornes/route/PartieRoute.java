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
        }
        Joueur joueur1 = new Joueur(jid, jid);
        if (!partie.getListeJoueurs().contains(joueur1)) {
            // ajout du joueur s'il n'existe pas
            partie.addJoueur(joueur1);
        }

        // si la partie n'est pas demarrée et si le nombre de joueur est > 2
        // la partie démarre
        if (!partie.isEnCours() && partie.getListeJoueurs().size() > 2) {
            partie.getListeJoueurs().forEach(joueur -> joueur.setEtat(EtatJoueur.inactif));
            partie.start();
        }

        return partie;
    }

    public List<Joueur> getJoueursInscrits(Request request, Response response) {
        return MegaBorne.INSTANCE.getPartie().getListeJoueurs();
    }

    public Joueur getJoueur(Request request, Response response) {

        String jid = request.params("jid");
        return MegaBorne.INSTANCE.getListeJoueur().stream().collect(HashMap<String, Joueur>::new, (m, c) -> m.put(c.getId(), c),
                (m, u) -> {
                }).get(jid);
    }
}
