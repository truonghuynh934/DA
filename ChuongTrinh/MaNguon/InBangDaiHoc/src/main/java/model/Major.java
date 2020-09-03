package model;

public class Major {
    private String idMajor;
    private String vietnam;
    private String english;

    public Major() {
    }

    public Major(String idMajor, String vietnam, String english) {
        this.idMajor = idMajor;
        this.vietnam = vietnam;
        this.english = english;
    }

    public Major(String idMajor, String vietnam) {
        this.idMajor = idMajor;
        this.vietnam = vietnam;
    }

    public String getIdMajor() {
        return idMajor;
    }

    public void setIdMajor(String idMajor) {
        this.idMajor = idMajor;
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

    @Override
    public String toString() {
        return "Major{" +
                "idMajor='" + idMajor + '\'' +
                ", vietnam='" + vietnam + '\'' +
                ", english='" + english + '\'' +
                '}';
    }
}
