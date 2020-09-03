package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Student;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DialogStudentExistsController {
    @FXML
    private TextField tfIdStudent;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfDateOfBirth;
    @FXML
    private TextField tfSex;
    @FXML
    private TextField tfHometown;
    @FXML
    private TextField tfGpa;
    @FXML
    private TextField tfRank;
    @FXML
    private TextField tfLicenseDate;
    @FXML
    private TextField tfGraduationYear;
    @FXML
    private TextField tfRegisterNumber;
    @FXML
    private TextField tfMajor;
    @FXML
    private TextField tfModeOfStudy;
    @FXML
    private TextField tfTitle;
    @FXML
    private TextField tfClass;
    @FXML
    private TextField tfNote;

    @FXML
    private Button btUpdateStudent;
    @FXML
    private Button btIgnore;

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    Student studentDialog = new Student();
    boolean checkUpdate = false;

    public void setStudentDialog(Student student){
        tfIdStudent.setText(student.getIdStudent());
        tfName.setText(student.getName());
        tfDateOfBirth.setText(student.getDateOfBirth());
        tfSex.setText(student.getSex());
        tfHometown.setText(student.getHometown());
        tfGpa.setText(String.valueOf(student.getGpa()));
        tfRank.setText(student.getRank());
        tfLicenseDate.setText(dateTimeFormatter.format(student.getLicenseDate()));
        tfGraduationYear.setText(String.valueOf(student.getGraduationYear()));
        tfRegisterNumber.setText(student.getRegisterNumber());
        tfMajor.setText(student.getMajor());
        tfModeOfStudy.setText(student.getModeOfStudy());
        tfTitle.setText(student.getTitle());
        tfClass.setText(student.getStrClass());
        tfNote.setText(student.getNote());
    }

    public Student getStudentDialog(){
        return studentDialog;
    }

    public void updateStudent(MouseEvent mouseEvent){
        studentDialog.setIdStudent(tfIdStudent.getText());
        studentDialog.setName(tfName.getText());
        studentDialog.setDateOfBirth(tfDateOfBirth.getText());
        studentDialog.setSex(tfSex.getText());
        studentDialog.setHometown(tfHometown.getText());
        studentDialog.setGpa(Float.parseFloat(tfGpa.getText()));
        studentDialog.setRank(tfRank.getText());
        studentDialog.setLicenseDate(LocalDate.parse(tfLicenseDate.getText(),dateTimeFormatter));
        studentDialog.setGraduationYear(Integer.parseInt(tfGraduationYear.getText()));
        studentDialog.setRegisterNumber(tfRegisterNumber.getText());
        studentDialog.setMajor(tfMajor.getText());
        studentDialog.setModeOfStudy(tfModeOfStudy.getText());
        studentDialog.setTitle(tfTitle.getText());
        studentDialog.setStrClass(tfClass.getText());
        studentDialog.setNote(tfNote!=null?tfNote.getText():"");

        checkUpdate = true;

        // close dialog after update textfield note
        Node source = (Node)  mouseEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();

        System.out.println("Update student method");
    }

    public void ignoreStudent(MouseEvent mouseEvent){
        checkUpdate = false;
        Node source = (Node)  mouseEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
        System.out.println("Ignore student method");
    }

    public boolean isCheckUpdate() {
        return checkUpdate;
    }

    public void setCheckUpdate(boolean checkUpdate) {
        this.checkUpdate = checkUpdate;
    }
}
