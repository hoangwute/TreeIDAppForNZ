package aut.bcis.researchdevelopment.model;

/**
 * Created by VS9 X64Bit on 26/08/2016.
 */
public class Tree {
    private int id;
    private String commonName;
    private String maoriName;
    private String latinName;
    private String family;
    private String structuralClass;
    private String mainPicture;
    private int liked;

    public Tree(int id, String commonName, String maoriName, String latinName, String family, String structuralClass, String mainPicture, int liked) {
        this.id = id;
        this.commonName = commonName;
        this.maoriName = maoriName;
        this.latinName = latinName;
        this.family = family;
        this.structuralClass = structuralClass;
        this.mainPicture = mainPicture;
        this.liked = liked;
    }

    public Tree(int id, String commonName, String maoriName, String latinName, String mainPicture) {
        this.id = id;
        this.commonName = commonName;
        this.maoriName = maoriName;
        this.latinName = latinName;
        this.mainPicture = mainPicture;
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

    public String getStructuralClass() {
        return structuralClass;
    }

    public void setStructuralClass(String structuralClass) {
        this.structuralClass = structuralClass;
    }

    public String getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(String mainPicture) {
        this.mainPicture = mainPicture;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public String getMaoriName() {
        return maoriName;
    }

    public void setMaoriName(String maoriName) {
        this.maoriName = maoriName;
    }

}
