package org.vqhcode.megabornes.model;

/**
 * Created by jbcotard on 21/01/2017.
 */
public class Carte {


    private EtatCarte etat;
    private String valeur;
    private TypeCarte typeCarte;
    private String idCarte;

    public Carte() {
        etat = EtatCarte.dansLaPioche;
    }

    public EtatCarte getEtat() {
        return etat;
    }

    public void setEtat(EtatCarte etat) {
        this.etat = etat;
    }

    public String getIdCarte() {
        return idCarte;
    }

    public void setIdCarte(String idCarte) {
        this.idCarte = idCarte;
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

    @Override
    public String toString() {
        return "Carte{" +
                "etat=" + etat +
                ", valeur='" + valeur + '\'' +
                ", typeCarte=" + typeCarte +
                ", idCarte='" + idCarte + '\'' +
                '}';
    }
}
