package model;

public class ModeOfStudy {
    private String idModeOfStudy;
    private String vietnam;
    private String english;

    public ModeOfStudy() {
    }

    public ModeOfStudy(String idModeOfStudy, String vietnam, String english) {
        this.idModeOfStudy = idModeOfStudy;
        this.vietnam = vietnam;
        this.english = english;
    }

    public ModeOfStudy(String idModeOfStudy, String vietnam) {
        this.idModeOfStudy = idModeOfStudy;
        this.vietnam = vietnam;
    }

    public String getIdModeOfStudy() {
        return idModeOfStudy;
    }

    public void setIdModeOfStudy(String idModeOfStudy) {
        this.idModeOfStudy = idModeOfStudy;
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
                "idModeOfStudy='" + idModeOfStudy + '\'' +
                ", vietnam='" + vietnam + '\'' +
                ", english='" + english + '\'' +
                '}';
    }
}
