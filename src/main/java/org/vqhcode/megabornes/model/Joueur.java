package org.vqhcode.megabornes.model;

import org.vqhcode.megabornes.MegaBorne;

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
    private List<Carte> listeCartesExposees;
    private Carte derniereCarteJouee;
    private String id;
    private int nbBornesParcourues;

    public Joueur(String i, String nom) {
        this.id = i;
        this.nom = nom;
        this.nbBornesParcourues = 0;

        // positionnement initial
        this.position = MegaBorne.INSTANCE.getListePosition().get(0);

        // etat courant
        this.etat = EtatJoueur.enAttente;

        listeCartesEnMain = new ArrayList<Carte>();
        listeCartesExposees = new ArrayList<Carte>();
    }

    public Carte getDerniereCarteJouee() {
        return derniereCarteJouee;
    }

    public void setDerniereCarteJouee(Carte derniereCarteJouee) {
        this.derniereCarteJouee = derniereCarteJouee;
    }

    public List<Carte> getListeCartesExposees() {
        return listeCartesExposees;
    }

    public void setListeCartesExposees(List<Carte> listeCartesExposees) {
        this.listeCartesExposees = listeCartesExposees;
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
        this.nbBornesParcourues = valeur;
    }


    public int getNbBornesParcourues() {
        return nbBornesParcourues;
    }

    public void setNbBornesParcourues(int nbBornesParcourrues) {
        this.nbBornesParcourues = nbBornesParcourrues;
    }
    public String getNom() {
        return nom;
    }

    public Carte isCarteExposee(String s) {

        Carte carteTrouve= null ;

        for (Carte carte:
        listeCartesExposees) {
            if (carte.getValeur().equals(s)) {
                carteTrouve = carte;
            }
        }
        return carteTrouve;
    }

    public void delCartesExposees(Carte carte) {
        listeCartesExposees.remove(carte);
    }

    public void addCartesExposees(Carte carte) {
        listeCartesExposees.add(carte);
    }

    @Override
    public String toString() {
        return "Joueur{" +
                "etat=" + etat +
                ", nom='" + nom + '\'' +
                ", position=" + position +
                ", listeCartesEnMain=" + listeCartesEnMain +
                ", listeCartesExposees=" + listeCartesExposees +
                ", derniereCarteJouee=" + derniereCarteJouee +
                ", id='" + id + '\'' +
                ", nbBornesParcourues=" + nbBornesParcourues +
                '}';
    }
}
