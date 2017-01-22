package org.vqhcode.megabornes.model;

import java.util.List;

/**
 * Created by jbcotard on 22/01/2017.
 */
public class Circuit {
    private List<Position> listePositions;

    public void setListePositions(List<Position> listePositions) {
        this.listePositions = listePositions;
    }

    public List<Position> getListePositions() {
        return listePositions;
    }
}
