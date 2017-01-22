package org.vqhcode.megabornes.route;

import org.vqhcode.megabornes.MegaBorne;
import org.vqhcode.megabornes.model.Joueur;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by jbcotard on 21/01/2017.
 */
public class JoueurRoute {


    public List<Joueur> getJoueurs(Request request, Response response) {

        List<Joueur> listeJoueurs  = MegaBorne.INSTANCE.getListeJoueur();
        return listeJoueurs;
    }

    public Joueur getJoueur(Request request, Response response) {
        String jid = request.params("jid");
        HashMap<String, Joueur> map = MegaBorne.INSTANCE.getListeJoueur().stream().collect(HashMap<String, Joueur>::new, (m, c) -> m.put(c.getId(), c),
                (m, u) -> {
                });

        Joueur joueur = map.get(jid);
        return joueur;
    }

    public String genJoueur(Request request, Response response) {
        Random random = new Random();
        int i1 = random.nextInt(100);
        String idJoueur = "joueur" + i1;
        Joueur joueur = new Joueur(idJoueur,idJoueur);
        List<Joueur> listeJoueurs = MegaBorne.INSTANCE.getListeJoueur();
        if (MegaBorne.INSTANCE.getListeJoueur() == null || MegaBorne.INSTANCE.getListeJoueur().size() == 0) {
            listeJoueurs = new ArrayList<Joueur>();
        }
        if (!MegaBorne.INSTANCE.getListeJoueur().contains(joueur)) {
            listeJoueurs.add(joueur);
        }
        MegaBorne.INSTANCE.setListeJoueur(listeJoueurs);

        return idJoueur;
    }
}
