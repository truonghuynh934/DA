package model;

public class Rank {
    private String idRank;
    private String vietnam;
    private String english;

    public Rank() {
    }

    public Rank(String idRank, String vietnam, String english) {
        this.idRank = idRank;
        this.vietnam = vietnam;
        this.english = english;
    }

    public String getIdRank() {
        return idRank;
    }

    public void setIdRank(String idRank) {
        this.idRank = idRank;
    }

    public String getVietnam() {
        return vietnam;
    }

    public void setVietnam(String vietnam) {
        this.vietnam = vietnam;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}
