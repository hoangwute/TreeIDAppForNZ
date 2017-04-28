package aut.bcis.researchdevelopment.model;

/**
 * Created by admin on 24-Apr-17.
 */

public class Sighting {
    private int id;
    private String commonName;
    private String maoriName;
    private String latinName;
    private String sightingPicture;
    private String location;
    private String date;

    public Sighting(int id, String commonName, String maoriName, String latinName, String sightingPicture, String location, String date) {
        this.id = id;
        this.commonName = commonName;
        this.maoriName = maoriName;
        this.latinName = latinName;
        this.sightingPicture = sightingPicture;
        this.location = location;
        this.date = date;
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

    public String getMaoriName() {
        return maoriName;
    }

    public void setMaoriName(String maoriName) {
        this.maoriName = maoriName;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getSightingPicture() {
        return sightingPicture;
    }

    public void setSightingPicture(String sightingPicture) {
        this.sightingPicture = sightingPicture;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
