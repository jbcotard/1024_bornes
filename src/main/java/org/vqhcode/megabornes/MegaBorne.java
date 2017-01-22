package org.vqhcode.megabornes;

import org.vqhcode.megabornes.model.JeuCarte;
import org.vqhcode.megabornes.model.Joueur;
import org.vqhcode.megabornes.model.Partie;
import org.vqhcode.megabornes.model.Position;

import java.util.List;

/**
 * Created by jbcotard on 21/01/2017.
 */
public enum MegaBorne {
    INSTANCE;

    private List<Joueur> listeJoueur;
    private Partie partie;
    private List<Position> listePosition;
    private JeuCarte jeuCartes;

    public List<Joueur> getListeJoueur() {
        return listeJoueur;
    }

    public void setListeJoueur(List<Joueur> listeJoueur) {
        this.listeJoueur = listeJoueur;
    }

    public Partie getPartie() {
        return partie;
    }

    public void setPartie(Partie partie) {
        this.partie = partie;
    }

    public List<Position> getListePosition() {
        return listePosition;
    }

    public void setListePosition(List<Position> listePosition) {
        this.listePosition = listePosition;
    }

    public void setJeuCartes(JeuCarte jeuCartes) {
        this.jeuCartes = jeuCartes;
    }

    public JeuCarte getJeuCartes() {
        return jeuCartes;
    }
}
