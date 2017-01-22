package org.vqhcode.megabornes.model;

/**
 * Created by jbcotard on 22/01/2017.
 */
public class Commerce {

    private String type;
    private double latitude;
    private double longitude;
    private double distance;

    public Commerce(String typeCommerce, String longitudeCommerce, String latitudeCommercce, String distanceCommerce) {
        this.type = typeCommerce;
        this.latitude = Double.valueOf(latitudeCommercce);
        this.longitude = Double.valueOf(longitudeCommerce);
        this.distance = Double.valueOf(distanceCommerce);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
