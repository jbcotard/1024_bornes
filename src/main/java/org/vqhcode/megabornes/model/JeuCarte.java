package org.vqhcode.megabornes.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jbcotard on 21/01/2017.
 */
public class JeuCarte {

    private List<Carte> listeCartes;

    public JeuCarte listeCartes(List<Carte> listeCartes) {

        this.listeCartes = listeCartes;
        return this;
    }

    public List<Carte> getListeCartes() {
        return listeCartes;
    }

    public void distribueCarte(int i1, String id) {
        listeCartes.get(i1).majEtat(EtatCarte.enMainJoueur);
    }

    public void setListeCartes(List<Carte> listeCartes) {
        this.listeCartes = listeCartes;
    }

}
