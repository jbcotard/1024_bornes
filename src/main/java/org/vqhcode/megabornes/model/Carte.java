package org.vqhcode.megabornes.model;

/**
 * Created by jbcotard on 21/01/2017.
 */
public class Carte {


    private EtatCarte etat;
    private String valeur;
    private TypeCarte typeCarte;

    public Carte() {
        etat = EtatCarte.dansLaPioche;
    }

    public void majEtat(EtatCarte etatCarte) {
        this.etat = etatCarte;
    }

    public boolean isDisponible() {
        return EtatCarte.dansLaPioche == etat;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }


    public TypeCarte getTypeCarte() {
        return typeCarte;
    }

    public void setTypeCarte(TypeCarte typeCarte) {
        this.typeCarte = typeCarte;
    }
}
