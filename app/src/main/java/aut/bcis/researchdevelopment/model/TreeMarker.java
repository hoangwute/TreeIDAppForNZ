package aut.bcis.researchdevelopment.model;

/**
 * Created by admin on 03-Mar-17.
 */

public class TreeMarker {
    private int id;
    private String commonName;
    private String latinName;
    private String location;
    private String note;
    private String imagePath;
    private double latitude;
    private double longitude;

    public TreeMarker(String commonName, String latinName, String location, String note, String imagePath, double latitude, double longitude) {
        this.commonName = commonName;
        this.latinName = latinName;
        this.location = location;
        this.note = note;
        this.imagePath = imagePath;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public TreeMarker(String commonName, String latinName) {
        this.commonName = commonName;
        this.latinName = latinName;
    }

    public TreeMarker(String commonName, String latinName, String location) {
        this.commonName = commonName;
        this.latinName = latinName;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
}
