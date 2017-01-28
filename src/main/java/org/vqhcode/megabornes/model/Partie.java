package org.vqhcode.megabornes.model;

import org.vqhcode.megabornes.MegaBorne;

import java.awt.*;
import java.util.*;
import java.util.List;


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
    private boolean joueurPrecedentAyantJoue;


    public Partie() {
        listeJoueurs = new ArrayList<Joueur>();
        etat = EtatPartie.enAttenteJoueur;
        joueurPrecedentAyantJoue = false;
        indexJoueurCourant = 0;
    }

    public void addJoueur(Joueur joueur) {
        // si le joueur n'existe pas encore
        // il est ajouté
        if ( !listeJoueurs.contains(joueur)) {
            listeJoueurs.add(joueur);
        }

    }

    public boolean isJoueurPrecedentAyantJoue() {
        return joueurPrecedentAyantJoue;
    }

    public void setJoueurPrecedentAyantJoue(boolean joueurPrecedentAyantJoue) {
        this.joueurPrecedentAyantJoue = joueurPrecedentAyantJoue;
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
            while (nbCartesDistribues != 7) {
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
        if (indexJoueurCourant >= listeJoueurs.size()) {
            indexJoueurCourant = 0;
        }

        joueurCourant = listeJoueurs.get(indexJoueurCourant);
        
        
        for (Joueur joueur: listeJoueurs) {
        	if (joueur.getEtat() == EtatJoueur.actif)
        		joueur.setEtat(EtatJoueur.inactif);
        }
        joueurCourant.setEtat(EtatJoueur.actif);

        List<Commerce> listeCommercesJoueurCourant = joueurCourant.getPosition().getListeCommerces();
        List<Commerce> listeCommercesFiltres = new ArrayList<>();
        for (Commerce commerce:
        listeCommercesJoueurCourant) {
            if (commerce.getDistance() < 10000) {
                listeCommercesFiltres.add(commerce);
            }
        }
        List<Commerce> listeCommercesAvecCartes = rechercherExistenceCartesDansCommerce(listeCommercesFiltres);

        if(listeCommercesAvecCartes == null || listeCommercesAvecCartes.size() == 0) {
            listeCommercesAvecCartes = rechercherExistenceCartesDansCommerce(listeCommercesJoueurCourant);
        }

        joueurCourant.getPosition().setListeCommerces(listeCommercesAvecCartes);
    }

    private List<Commerce> rechercherExistenceCartesDansCommerce(List<Commerce> listeCommercesFiltres) {
        List<Commerce> listeResultat = new ArrayList<Commerce>();
        for (Commerce commerce:listeCommercesFiltres) {
           if ("Hopital".equals(commerce.getType()) && jeuCarte.isCartePioche("Accident de la route")) {
               listeResultat.add(commerce);
           } else if ("Gare".equals(commerce.getType()) && jeuCarte.isCartePioche("Panne d'essence")) {
               listeResultat.add(commerce);
           }else if ("Gendarmerie".equals(commerce.getType()) && jeuCarte.isCartePioche("Crevaison")) {
               listeResultat.add(commerce);
           }else if ("Police".equals(commerce.getType()) && jeuCarte.isCartePioche("Limitation de vitesse")) {
               listeResultat.add(commerce);
           }else if ("Prefecturee".equals(commerce.getType()) && jeuCarte.isCartePioche("Feu rouge")) {
               listeResultat.add(commerce);
           }else if ("Carrossier".equals(commerce.getType()) &&
                   (jeuCarte.isCartePioche("Reparation") ||
                           (jeuCarte.isCartePioche("As du volant")))) {
                listeResultat.add(commerce);

           } else if ("Station essence".equals(commerce.getType()) &&
                    (jeuCarte.isCartePioche("Essence") ||
                            (jeuCarte.isCartePioche("Camion-citerne")))) {
                listeResultat.add(commerce);
            }else if ("Garagiste".equals(commerce.getType()) &&
                    (jeuCarte.isCartePioche("Roue de secours") ||
                            (jeuCarte.isCartePioche("Increvable")))) {
                listeResultat.add(commerce);
            }else if ("DDE".equals(commerce.getType()) &&
                    (jeuCarte.isCartePioche("Fin de limitation de vitesse") ||
                            (jeuCarte.isCartePioche("Prioritaire")))) {
                listeResultat.add(commerce);
            }
                else if ("Mairie".equals(commerce.getType()) && jeuCarte.isCartePioche("Feu vert")) {
               listeResultat.add(commerce);
           }else if ("Bar".equals(commerce.getType()) &&
                    (jeuCarte.isCartePioche("32") ||
                            (jeuCarte.isCartePioche("64")) ||
                            (jeuCarte.isCartePioche("96")) ||
                            (jeuCarte.isCartePioche("128")) ||
                            (jeuCarte.isCartePioche("256")))) {
                listeResultat.add(commerce);
            }

        }
        return listeResultat;
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

    public int processActionJoueur(String jid, String idCarte, boolean defausse, String jidAdversaire) {

        //System.out.println("action " + jid + " " + idCarte + " " + defausse + " " + jidAdversaire);

        Joueur joueur = listeJoueurs.stream().collect(HashMap<String, Joueur>::new, (m, c) -> m.put(c.getId(), c),
                (m, u) -> {
                }).get(jid);
        Joueur joueurAdversaire = listeJoueurs.stream().collect(HashMap<String, Joueur>::new, (m, c) -> m.put(c.getId(), c),
                (m, u) -> {
                }).get(jidAdversaire);
        Carte carte = jeuCarte.getListeCartes().stream().collect(HashMap<String, Carte>::new, (m,c) -> m.put(c.getIdCarte(),c),
                (m, u) -> {
                }).get(idCarte);

        //System.out.println(carte.getValeur() + " " + carte.getIdCarte()
        //+ " " +carte.getEtat());

        int resultat = 0;

        if (defausse) {
            joueur.delCartesEnMain(carte);
            jeuCarte.ajoutPioche(carte);

        } else {
            switch(carte.getValeur()) {
                case "Accident de la route" :

                    if (joueurAdversaire.isCarteExposee("As du volant") != null||
                            joueurAdversaire.isCarteExposee("Accident de la route") != null) {
                        resultat = 1;
                    } else {
                        Carte carteAdversaire = joueurAdversaire.isCarteExposee("Feu vert");
                        if (carteAdversaire != null) {
                            jeuCarte.ajoutPioche(carteAdversaire);
                            joueurAdversaire.delCartesExposees(carteAdversaire);

                        }
                        joueurAdversaire.addCartesExposees(carte);
                    }
                    break;
                case "Panne d’essence" :

                    if (joueurAdversaire.isCarteExposee("Camion-citerne") != null||
                            joueurAdversaire.isCarteExposee("Panne d’essence") != null) {
                        resultat = 1;
                    } else {
                        Carte carteAdversaire = joueurAdversaire.isCarteExposee("Feu vert");
                        if (carteAdversaire != null) {
                            jeuCarte.ajoutPioche(carteAdversaire);
                            joueurAdversaire.delCartesExposees(carteAdversaire);

                        }
                        joueurAdversaire.addCartesExposees(carte);
                    }
                    break;
                case "Crevaison" :

                    if (joueurAdversaire.isCarteExposee("Increvable") != null||
                            joueurAdversaire.isCarteExposee("Crevaison") != null) {
                        resultat = 1;
                    } else {
                        Carte carteAdversaire = joueurAdversaire.isCarteExposee("Feu vert");
                        if (carteAdversaire != null) {
                            jeuCarte.ajoutPioche(carteAdversaire);
                            joueurAdversaire.delCartesExposees(carteAdversaire);

                        }
                        joueurAdversaire.addCartesExposees(carte);
                    }
                    break;
                case "Limitation de vitesse" :

                    if (joueurAdversaire.isCarteExposee("Prioritaire") != null||
                            joueurAdversaire.isCarteExposee("Limitation de vitesse") != null) {
                        resultat = 1;
                    } else {

                        joueurAdversaire.addCartesExposees(carte);
                    }
                    break;
                case "Feu rouge" :

                    if (joueurAdversaire.isCarteExposee("Prioritaire") != null||
                            joueurAdversaire.isCarteExposee("Feu rouge") != null) {
                        resultat = 1;
                    } else {
                        Carte carteAdversaire = joueurAdversaire.isCarteExposee("Feu vert");
                        if (carteAdversaire != null) {
                            jeuCarte.ajoutPioche(carteAdversaire);
                            joueurAdversaire.delCartesExposees(carteAdversaire);

                        }
                        joueurAdversaire.addCartesExposees(carte);
                    }
                    break;
                case "Réparation" :

                    Carte carteAccident =joueur.isCarteExposee("Accident de la route");
                    if (carteAccident == null) {
                        resultat = 1;
                    } else {
                        joueur.delCartesExposees(carteAccident);
                        jeuCarte.ajoutPioche(carteAccident);
                        joueur.delCartesEnMain(carte);
                        jeuCarte.ajoutPioche(carte);

                    }
                    break;
                case "Essence" :

                    Carte cartePanneEssence =joueur.isCarteExposee("Panne d’essence");
                    if (cartePanneEssence == null) {
                        resultat = 1;
                    } else {
                        joueur.delCartesExposees(cartePanneEssence);
                        jeuCarte.ajoutPioche(cartePanneEssence);
                        joueur.delCartesEnMain(carte);
                        jeuCarte.ajoutPioche(carte);

                    }
                    break;
                case "Roue de secours" :

                    Carte carteCrevaison =joueur.isCarteExposee("Crevaison");
                    if (carteCrevaison == null) {
                        resultat = 1;
                    } else {
                        joueur.delCartesExposees(carteCrevaison);
                        jeuCarte.ajoutPioche(carteCrevaison);
                        joueur.delCartesEnMain(carte);
                        jeuCarte.ajoutPioche(carte);

                    }
                    break;
                case "Fin de limitation de vitesse" :

                    Carte carteLimitation =joueur.isCarteExposee("Limitation de vitesse");
                    if (carteLimitation == null) {
                        resultat = 1;
                    } else {
                        joueur.delCartesExposees(carteLimitation);
                        jeuCarte.ajoutPioche(carteLimitation);
                        joueur.delCartesEnMain(carte);
                        jeuCarte.ajoutPioche(carte);

                    }
                    break;
                case "Feu vert" :

                    if (joueur.isCarteExposee("Feu vert") == null ||
                            joueur.isCarteExposee("Prioritaire") == null ||
                            joueur.isCarteExposee("Accident de la route") == null ||
                            joueur.isCarteExposee("Crevaison") == null ||
                            joueur.isCarteExposee("Panne d'essence") == null) {
                        joueur.addCartesExposees(carte);
                        joueur.delCartesEnMain(carte);
                    } else {
                        resultat = 1;
                    }
                    break;
                case "As du volant" :
                    Carte carteAccident2 = joueur.isCarteExposee("Accident");
                    if ( carteAccident2 != null) {
                        joueur.delCartesExposees(carteAccident2);
                        jeuCarte.ajoutPioche(carteAccident2);
                    }
                    joueur.addCartesExposees(carte);
                    joueur.delCartesEnMain(carte);
                    break;
                case "Camion-citerne" :
                    Carte cartePanneEssence2 = joueur.isCarteExposee("Panne d’essence");
                    if ( cartePanneEssence2 != null) {
                        joueur.delCartesExposees(cartePanneEssence2);
                        jeuCarte.ajoutPioche(cartePanneEssence2);
                    }
                    joueur.addCartesExposees(carte);
                    joueur.delCartesEnMain(carte);
                    break;
                case "Increvable" :
                    Carte carteCrevaison2 = joueur.isCarteExposee("Crevaison");
                    if ( carteCrevaison2 != null) {
                        joueur.delCartesExposees(carteCrevaison2);
                        jeuCarte.ajoutPioche(carteCrevaison2);
                    }
                    joueur.addCartesExposees(carte);
                    joueur.delCartesEnMain(carte);
                    break;
                case "Prioritaire" :
                    Carte carteLimitationVitesse2 = joueur.isCarteExposee("Limitation de vitesse");
                    if ( carteLimitationVitesse2 != null) {
                        joueur.delCartesExposees(carteLimitationVitesse2);
                        jeuCarte.ajoutPioche(carteLimitationVitesse2);
                    }
                    Carte carteFeuRouge = joueur.isCarteExposee("Feu rouge");
                    if ( carteFeuRouge != null) {
                        joueur.delCartesExposees(carteFeuRouge);
                        jeuCarte.ajoutPioche(carteFeuRouge);
                    }
                    Carte carteFeuVert = joueur.isCarteExposee("Feu vert");
                    if ( carteFeuVert != null) {
                        joueur.delCartesExposees(carteFeuVert);
                        jeuCarte.ajoutPioche(carteFeuVert);
                    }
                    joueur.addCartesExposees(carte);
                    joueur.delCartesEnMain(carte);
                    break;
                case "32" :
                case "64" :
                case "96" :
                case "128" :
                case "256" :
                    if (joueur.isCarteExposee("Feu vert") == null &&
                            joueur.isCarteExposee("Prioritaire") == null ) {
                        resultat = 1;
                    }
                    resultat = calculAvancement(joueur,carte.getValeur());
                    if (resultat == 0) {
                        joueur.delCartesEnMain(carte);
                        jeuCarte.ajoutPioche(carte);
                    }
                    break;

            }
        }

        return resultat;
    }

    private int calculAvancement(Joueur joueur, String valeur) {
        int nbBornes = Integer.valueOf(valeur);
        int resultat = 0;
        if (joueur.getNbBornesParcourues() + nbBornes > 1024) {
             resultat = 1;
        }
        if (joueur.isCarteExposee("Limitation de vitesse") != null && nbBornes > 64) {
            resultat = 1;
        }
        joueur.setNbBornesParcourues(joueur.getNbBornesParcourues() + nbBornes);
        int indexPosition = joueur.getNbBornesParcourues() / 32;
        joueur.setPosition(circuit.getListePositions().get(indexPosition));
        if (joueur.getNbBornesParcourues() == 1024) {
            joueur.setEtat(EtatJoueur.winner);
            for (Joueur joueur2 :listeJoueurs
                 ) {
                if (joueur2 != joueur) {
                    joueur2.setEtat(EtatJoueur.looser);
                }
            }
            etat = EtatPartie.termine;

        }
        return resultat;
    }

    public Carte processPioche(String jid, String typeCommerce) {

        //System.out.println("pioche carte " + jid + " " + typeCommerce);

        Carte carte = null;

        switch(typeCommerce) {
            case "Hopital" :
                carte = jeuCarte.getCarte("Accident de la route");
            break;
            case "Gare" :
                carte = jeuCarte.getCarte("Panne d'essence");
                break;
            case "Police" :
                carte = jeuCarte.getCarte("Limitation de vitesse");
                break;
            case "Prefecture" :
                carte = jeuCarte.getCarte("Feu rouge");
                break;
            case "Mairie" :
                carte = jeuCarte.getCarte("Feu vert");
                break;
            case "Gendarmerie" :
                carte = jeuCarte.getCarte("Crevaison");
                break;
            case "Carrossier" :

                carte = jeuCarte.getCarteHasard(Arrays.asList("Reparation","As du volant"));

                break;
            case "Station%20essence" :

                carte = jeuCarte.getCarteHasard(Arrays.asList("Essence","Camion-citerne"));
                break;
            case "Garagiste" :

                carte = jeuCarte.getCarteHasard(Arrays.asList("Roue de secours","Increvable"));
                break;
            case "DDE" :

                carte = jeuCarte.getCarteHasard(Arrays.asList("Fin de limitation de vitesse","Prioritaire"));
                break;
            case "Bar" :

                carte = jeuCarte.getCarteHasard(Arrays.asList("32","64","96","128","256"));
                break;
        }

        if (carte != null) {
            joueurCourant.addCartesEnMain(carte);
            jeuCarte.supprimeDansPioche(carte);
        }

        //System.out.println("carte piochee = " + carte);
        return carte;
    }
}
