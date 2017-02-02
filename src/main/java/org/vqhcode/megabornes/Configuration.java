package org.vqhcode.megabornes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by jbcotard on 21/01/2017.
 */
public enum Configuration {
    INSTANCE;
    private Integer nbParties;
    private Integer nbJoueurs;
    private Integer nbBornes;

    public Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public void setNbParties(Integer nbParties) {
        this.nbParties = nbParties;
    }

    public Integer getNbParties() {
        return nbParties;
    }

    public void setNbJoueurs(Integer nbJoueurs) {
        this.nbJoueurs = nbJoueurs;
    }

    public Integer getNbJoueurs() {
        return nbJoueurs;
    }

    public void setNbBornes(Integer nbBornes) {
        this.nbBornes = nbBornes;
    }

    public Integer getNbBornes() {
        return nbBornes;
    }
}
