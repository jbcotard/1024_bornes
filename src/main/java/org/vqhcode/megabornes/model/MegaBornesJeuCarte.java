package org.vqhcode.megabornes.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jbcotard on 21/01/2017.
 */
public class MegaBornesJeuCarte  {


    public static JeuCarte buildJeuCartes() {

        List<Carte> listeCartes = new ArrayList<Carte>();



        return new JeuCarte().listeCartes(listeCartes);
    }
}
