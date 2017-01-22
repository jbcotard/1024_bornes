package org.vqhcode.megabornes.model;

import org.vqhcode.megabornes.MegaBorne;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * Created by jbcotard on 21/01/2017.
 */
public class Partie {
    private List<Joueur> listeJoueurs;
    private Joueur joueurCourant;
    private int indexJoueurCourant;
    private EtatPartie etat;
    private JeuCarte jeuCarte;
    private Circuit circuit;


    public Partie() {
        listeJoueurs = new ArrayList<Joueur>();
        etat = EtatPartie.enAttenteJoueur;
    }

    public void addJoueur(Joueur joueur) {
        // si le joueur n'existe pas encore
        // il est ajouté
        if ( !listeJoueurs.contains(joueur)) {
            listeJoueurs.add(joueur);
        }

    }

    public void start() {

        // changement etat partie
        etat = EtatPartie.enCours;

        // distribution des cartes initiales
        // on prend 6 cartes au hasard qui sont disponible
        // on les met dans la main du joueur
        // on les met en indispo dans la pioche
        listeJoueurs.stream().forEach((Joueur j) -> {
            int nbCartesDistribues = 1;
            while (nbCartesDistribues != 6) {
                Random random = new Random();
                int i1 = random.nextInt(106);

                if (jeuCarte.getListeCartes().get(i1).isDisponible()){
                    j.addCartesEnMain(jeuCarte.getListeCartes().get(i1));
                    jeuCarte.distribueCarte(i1,j.getId());
                    nbCartesDistribues++;
                }


            }
        });

        // le joueur 1 commence
        listeJoueurs.get(0).setEtat(EtatJoueur.actif);
        joueurCourant = listeJoueurs.get(0);


    }

    public boolean isTerminee() {
        return etat == EtatPartie.termine;
    }

    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    public void setJoueurCourant(Joueur joueurCourant) {
        this.joueurCourant = joueurCourant;
    }

    public void joueurSuivant() {
        indexJoueurCourant++;
        if (indexJoueurCourant > listeJoueurs.size()) {
            indexJoueurCourant = 0;
        }
    }

    public void actionJoueur(Joueur joueurCourant, Carte carte) {
// action  1 : choix d'une carte (en fonction état)
        // soit une carte distance
        // soit une carte attaque
        // soit une carte defense
        // soit une carte feu vert
        // soit une botte
        // soit defausse

        // on retire la carte du jeu du joueur
        joueurCourant.delCartesEnMain(carte);



        // action 2 : effet carte
        // => nb cartes -1
        // soit mouvement (position, nb bornes)
        // soit effet attaque => changement état adversaire
        // soit effet defense (reparation, etc...) => chgt état
        // soit feu vert => changement état
        // soit botte => changement état


        switch (carte.getTypeCarte()) {
            case km :
                // mouvement : maj position et maj nb bornes
                joueurCourant.addBornes(Integer.valueOf(carte.getValeur()));

                break;
            //case TypeCarte.attac :
                // effet attaque => changement état adversaire

                //break;

        }

        // action 3 : pioche une carte
        // => nb cartes +1


    }

    public void stop() {
etat = EtatPartie.termine;
    }

    public void addJeuCartes(JeuCarte jeuCarte) {
        this.jeuCarte = jeuCarte;
    }

    public boolean isEnCours() {
        return etat == EtatPartie.enCours;
    }


    public List<Joueur> getListeJoueurs() {
        return listeJoueurs;
    }

    public void setListeJoueurs(List<Joueur> listeJoueurs) {
        this.listeJoueurs = listeJoueurs;
    }

    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }

    public Circuit getCircuit() {
        return circuit;
    }
}
