package aut.bcis.researchdevelopment.model;

/**
 * Created by VS9 X64Bit on 26/08/2016.
 */
public class Tree {
    private String commonName;
    private String latinName;
    private String family;
    private String genus;
    private String firstPicture;
    private int liked;
    private int id;

    public Tree(int id, String commonName, String latinName, String family, String genus, String firstPicture, int liked) {
        this.id = id;
        this.commonName = commonName;
        this.latinName = latinName;
        this.family = family;
        this.genus = genus;
        this.firstPicture = firstPicture;
        this.liked = liked;
    }
    public Tree(String commonName, String latinName, String family, String genus, int liked) {
        this.commonName = commonName;
        this.latinName = latinName;
        this.family = family;
        this.genus = genus;
        this.liked = liked;
    }

    public Tree() {
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

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getFirstPicture() {
        return firstPicture;
    }

    public void setFirstPicture(String firstPicture) {
        this.firstPicture = firstPicture;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }
}
