package org.vqhcode.megabornes.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jbcotard on 21/01/2017.
 */
public class Position {
    private String nom;
    private double latitude;
    private double longitude;
    private int positionBorne;
    private List<Commerce> listeCommerces;

    public int getPositionBorne() {
        return positionBorne;
    }

    public void setPositionBorne(int positionBorne) {
        this.positionBorne = positionBorne;
    }

    public List<Commerce> getListeCommerces() {
        return listeCommerces;
    }

    public void setListeCommerces(List<Commerce> listeCommerces) {
        this.listeCommerces = listeCommerces;
    }

    public Position(String nom, double latitude, double longitude) {
        this.nom = nom;
        this.latitude = latitude;
        this.longitude = longitude;
        this.listeCommerces = new ArrayList<Commerce>();
    }

    public Position() {
        this.listeCommerces = new ArrayList<Commerce>();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void addCommerce(Commerce commerce) {
        listeCommerces.add(commerce);
    }
}
