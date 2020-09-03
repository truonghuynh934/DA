package model;

import java.time.LocalDate;

public class Student {
    private boolean select;

    private String idStudent;
    private String name;
    private String dateOfBirth;
    private String sex;
    private String hometown;
    private float gpa;
    private String rank;

    private LocalDate licenseDate;
    private int graduationYear;
    private String registerNumber;
    private String major;
    private String modeOfStudy;
    private String title;
    private String strClass;
    private String note;

    public Student() {
    }

    public Student(String idStudent, String name, String dateOfBirth, String sex, String hometown, float gpa, String rank) {
        this.idStudent = idStudent;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.hometown = hometown;
        this.gpa = gpa;
        this.rank = rank;
    }

    public Student(String idStudent, String name, String dateOfBirth, String sex, String hometown, float gpa, String rank, LocalDate licenseDate, int graduationYear, String registerNumber, String major, String modeOfStudy, String title, String strClass) {
        this.idStudent = idStudent;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.hometown = hometown;
        this.gpa = gpa;
        this.rank = rank;
        this.licenseDate = licenseDate;
        this.graduationYear = graduationYear;
        this.registerNumber = registerNumber;
        this.major = major;
        this.modeOfStudy = modeOfStudy;
        this.title = title;
        this.strClass = strClass;
    }

    public Student(String name, String dateOfBirth, String hometown, String major,  String strClass,String modeOfStudy,String registerNumber, String note) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.hometown = hometown;
        this.registerNumber = registerNumber;
        this.major = major;
        this.modeOfStudy = modeOfStudy;
        this.strClass = strClass;
        this.note = note;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public float getGpa() {
        return gpa;
    }

    public void setGpa(float gpa) {
        this.gpa = gpa;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public LocalDate getLicenseDate() {
        return licenseDate;
    }

    public void setLicenseDate(LocalDate licenseDate) {
        this.licenseDate = licenseDate;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getModeOfStudy() {
        return modeOfStudy;
    }

    public void setModeOfStudy(String modeOfStudy) {
        this.modeOfStudy = modeOfStudy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStrClass() {
        return strClass;
    }

    public void setStrClass(String strClass) {
        this.strClass = strClass;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Student{" +
                "idStudent='" + idStudent + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", sex='" + sex + '\'' +
                ", hometown='" + hometown + '\'' +
                ", gpa=" + gpa +
                ", rank='" + rank + '\'' +
                ", licenseDate=" + licenseDate +
                ", graduationYear=" + graduationYear +
                ", registerNumber=" + registerNumber +
                ", major='" + major + '\'' +
                ", modeOfStudy='" + modeOfStudy + '\'' +
                ", title='" + title + '\'' +
                ", strClass='" + strClass + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
