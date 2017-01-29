package org.vqhcode.megabornes.model;

import java.util.*;

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

    public void ajoutPioche(Carte carte) {
        listeCartes.stream().collect(HashMap<String, Carte>::new, (m, c) -> m.put(c.getIdCarte(),c),
                (m, u) -> {
                }).get(carte.getIdCarte()).majEtat(EtatCarte.dansLaPioche);
    }

    public boolean isCartePioche(String s) {
        boolean carteTrouve= false ;

        for (Carte carte:
                listeCartes) {
            if (carte.getValeur().equals(s)) {
                carteTrouve = true;
            }
        }

        return carteTrouve;
    }

    public Carte getCarte(String s) {

        //System.out.println("    carte piochee : " + s);


        Carte carteTrouve = null;
        boolean nonTrouve = false;
        int compteur = 0;
        while(!nonTrouve && compteur < listeCartes.size()){
            Carte carte = listeCartes.get(compteur);
            //System.out.println(carte.getValeur());
            if (carte.getValeur().equals(s)) {
                carteTrouve = carte;
                nonTrouve = true;
            }
            compteur++;
        }


        //System.out.println("    carte à mettre en main : " + carteTrouve);

        return carteTrouve;
    }

    public Carte getCarteHasard(List<String> listeCartesHasard) {

        //System.out.println("    cartes possibles de la pioche : " );
        //listeCartesHasard.stream().forEach(System.out::println);

        List<Carte> listeCartesFiltrees = new ArrayList<Carte>();
        for (Carte carte:
                listeCartes) {
            if (listeCartesHasard.contains(carte.getValeur())) {
                listeCartesFiltrees.add(carte);
            }
        }

        Random random = new Random();
        int i1 = random.nextInt(listeCartesFiltrees.size());


        //System.out.println("    carte à mettre en main : " + listeCartesFiltrees.get(i1));

        return listeCartesFiltrees.get(i1);
    }

    public void supprimeDansPioche(Carte carte) {
        listeCartes.stream().collect(HashMap<String, Carte>::new, (m, c) -> m.put(c.getIdCarte(),c),
                (m, u) -> {
                }).get(carte.getIdCarte()).majEtat(EtatCarte.enMainJoueur);
    }
}
