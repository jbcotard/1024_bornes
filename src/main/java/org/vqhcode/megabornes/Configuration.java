package org.vqhcode.megabornes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by jbcotard on 21/01/2017.
 */
public enum Configuration {
    INSTANCE;
    public Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }
}
