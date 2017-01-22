package org.vqhcode.megabornes.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jbcotard on 21/01/2017.
 */
public class Joueur {


    private EtatJoueur etat;
    private String nom;
    private Position position;
    private List<Carte> listeCartesEnMain;
    private String id;
    private int nbBornesParcourrues;

    public Joueur(String i, String nom) {
        this.id = i;
        this.nom = nom;
        this.nbBornesParcourrues = 0;

        // positionnement initial
        this.position = new Position("Le Mans",48.00367,0.19788);

        // etat courant
        this.etat = EtatJoueur.enAttente;

        listeCartesEnMain = new ArrayList<Carte>();
    }

    public EtatJoueur getEtat() {
        return etat;
    }

    public void setEtat(EtatJoueur etat) {
        this.etat = etat;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addCartesEnMain(Carte carte) {
        listeCartesEnMain.add(carte);

    }

    public void setListeCartesEnMain(List<Carte> listeCartesEnMain) {
        this.listeCartesEnMain = listeCartesEnMain;
    }

    public List<Carte> getListeCartesEnMain() {
        return listeCartesEnMain;
    }

    public String getId() {
        return id;
    }

    public void delCartesEnMain(Carte carte) {
        listeCartesEnMain.remove(carte);
    }

    public void addBornes(int valeur) {
        this.nbBornesParcourrues = valeur;
    }


    public int getNbBornesParcourrues() {
        return nbBornesParcourrues;
    }

    public void setNbBornesParcourrues(int nbBornesParcourrues) {
        this.nbBornesParcourrues = nbBornesParcourrues;
    }
    public String getNom() {
        return nom;
    }
}
