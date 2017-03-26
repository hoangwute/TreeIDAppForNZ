package aut.bcis.researchdevelopment.model;

/**
 * Created by admin on 04-Mar-17.
 */

public class FilterEntry {
    private String commonName;
    private String latinName;
    private int count;
    private int filtered;
    private int id;

    public FilterEntry(String commonName, String latinName, int count, int filtered, int id) {
        this.commonName = commonName;
        this.latinName = latinName;
        this.count = count;
        this.filtered = filtered;
        this.id = id;
    }

    public FilterEntry(String commonName, String latinName, int count) {
        this.commonName = commonName;
        this.latinName = latinName;
        this.count = count;
    }

    public FilterEntry() {

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int isFiltered() {
        return filtered;
    }

    public void setFiltered(int filtered) {
        this.filtered = filtered;
    }
}
