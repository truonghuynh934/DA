package controller;

import database.HandlerExcelFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Student;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class SearchCertificateController implements Initializable {
    @FXML
    private TableView<Student> tvStudent;
    @FXML
    private TableColumn<Student, Integer> tcId;
    @FXML
    private TableColumn<Student, String> tcIdStudent;
    @FXML
    private TableColumn<Student, String> tcName;
    @FXML
    private TableColumn<Student, String> tcDateOfBirth;
    @FXML
    private TableColumn<Student, String> tcSex;
    @FXML
    private TableColumn<Student, String> tcHometown;
    @FXML
    private TableColumn<Student, Float> tcGpa;
    @FXML
    private TableColumn<Student, String> tcRank;
    @FXML
    private TableColumn<Student, LocalDate> tcLicenseDate;
    @FXML
    private TableColumn<Student, Integer> tcGraduationYear;
    @FXML
    private TableColumn<Student, String> tcRegisterNumber;
    @FXML
    private TableColumn<Student, String> tcMajor;
    @FXML
    private TableColumn<Student, String> tcModeOfStudy;
    @FXML
    private TableColumn<Student, String> tcTitle;
    @FXML
    private TableColumn<Student, String> tcClass;
    @FXML
    private TableColumn<Student, String> tcNote;

    @FXML
    private DatePicker dpExportExcelFrom;
    @FXML
    private DatePicker dpExportExcelTo;
    @FXML
    private Button btExportExcel;
    @FXML
    private Button btReplaceData;
    @FXML
    private TextField tfIdStudent;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfDateOfBirth;

    @FXML
    private Label lbIdStudent;
    @FXML
    private Label lbName;
    @FXML
    private Label lbDateOfBirth;
    @FXML
    private Label lbSex;
    @FXML
    private Label lbHometown;
    @FXML
    private Label lbGpa;
    @FXML
    private Label lbRank;
    @FXML
    private Label lbLicenseDate;
    @FXML
    private Label lbGraduationYear;
    @FXML
    private Label lbRegisterNumber;
    @FXML
    private Label lbMajor;
    @FXML
    private Label lbModeOfStudy;
    @FXML
    private Label lbTitle;
    @FXML
    private Label lbClass;
    @FXML
    private Label lbNote;


    HandlerExcelFile handlerExcelFile = new HandlerExcelFile();
    List<Student> listStudents = FXCollections.observableArrayList();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    List<Student> listTest = FXCollections.observableArrayList(
            new Student("msv1", "name 1 na1", "12/01/1996", "nam", "dsds", 4, "khá", LocalDate.parse("12/02/2024", dateTimeFormatter), 2023, "115", "cntt", "chính quy", "kỹ sư", "1"),
            new Student("msv2", "name 1 na1", "12/01/1996", "nam", "dsds", 4, "khá", LocalDate.parse("12/02/2024", dateTimeFormatter), 2023, "115", "cntt", "chính quy", "kỹ sư", "1"),
            new Student("msv3", "name 1 na1", "12/01/1996", "nam", "dsds", 4, "khá", LocalDate.parse("12/02/2024", dateTimeFormatter), 2023, "115", "cntt", "chính quy", "kỹ sư", "1"),
            new Student("msv4", "name 1 na1", "12/01/1996", "nam", "dsds", 4, "khá", LocalDate.parse("12/02/2024", dateTimeFormatter), 2023, "115", "cntt", "chính quy", "kỹ sư", "1")
    );

    File file = new File("history_student_printed.xlsx");

    Calendar calendar = Calendar.getInstance();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // read file excel contain history
            listStudents = handlerExcelFile.readExcel(file);
            System.out.println("LIST HISTORY: ");
            for(Student item: listStudents){
                System.out.println(item);
            }
            if (listStudents.size() > 0) System.out.println("last row: " + handlerExcelFile.getIndexRowLast(file));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load lịch sử in bằng");
            alert.setHeaderText(null);
            alert.setContentText("Có lỗi khi load lịch sử in bằng!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
            alert.showAndWait();
            System.out.println("Không tìm thấy file excel");
            e.printStackTrace();
        }
        loadTableView();
        configureTableView();

        tvStudent.setPlaceholder(new Label("Không có dữ liệu"));

        // configure datepicker
        // Converter
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        dpExportExcelFrom.setConverter(converter);
        dpExportExcelTo.setConverter(converter);
        dpExportExcelTo.setValue(LocalDate.of(calendar.get(Calendar.YEAR),(calendar.get(Calendar.MONTH)+1),calendar.get(Calendar.DAY_OF_MONTH)));
    }

    public void loadTableView() {
        tcIdStudent.setCellValueFactory(new PropertyValueFactory<Student, String>("idStudent"));
        tcName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        tcDateOfBirth.setCellValueFactory(new PropertyValueFactory<Student, String>("dateOfBirth"));
        tcSex.setCellValueFactory(new PropertyValueFactory<Student, String>("sex"));
        tcHometown.setCellValueFactory(new PropertyValueFactory<Student, String>("hometown"));
        tcGpa.setCellValueFactory(new PropertyValueFactory<Student, Float>("gpa"));
        tcRank.setCellValueFactory(new PropertyValueFactory<Student, String>("rank"));
        tcLicenseDate.setCellValueFactory(new PropertyValueFactory<Student, LocalDate>("licenseDate"));
        tcGraduationYear.setCellValueFactory(new PropertyValueFactory<Student, Integer>("graduationYear"));
        tcRegisterNumber.setCellValueFactory(new PropertyValueFactory<Student, String>("registerNumber"));
        tcMajor.setCellValueFactory(new PropertyValueFactory<Student, String>("major"));
        tcModeOfStudy.setCellValueFactory(new PropertyValueFactory<Student, String>("modeOfStudy"));
        tcTitle.setCellValueFactory(new PropertyValueFactory<Student, String>("title"));
        tcClass.setCellValueFactory(new PropertyValueFactory<Student, String>("strClass"));
        tcNote.setCellValueFactory(new PropertyValueFactory<Student, String>("note"));
        tvStudent.setItems((ObservableList<Student>) listStudents);
    }

    public void configureTableView() {
        tcId.setCellFactory(new Callback<TableColumn<Student, Integer>, TableCell<Student, Integer>>() {
            @Override
            public TableCell<Student, Integer> call(TableColumn<Student, Integer> param) {
                return new TableCell<Student, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && (this.getTableRow().getIndex() < tvStudent.getItems().size())) {
                            setText((this.getTableRow().getIndex() + 1) + "");
//                            System.out.println(this.getTableRow().getIndex()+1);
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });

        tcLicenseDate.setCellFactory(column -> {
            TableCell<Student, LocalDate> tableCell = new TableCell<Student, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("");
                    } else {
                        setText(dateTimeFormatter.format(item));
                    }

                }
            };
            return tableCell;
        });

        tcGpa.setCellFactory(column -> {
            TableCell<Student, Float> tableCell = new TableCell<Student, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == 0) {
                        setText("");
                    }else{
                        setText(String.valueOf(item));
                    }
                }
            };
            return tableCell;
        });

        tcGraduationYear.setCellFactory(column -> {
            TableCell<Student, Integer> tableCell = new TableCell<Student, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == 0) {
                        setText("");
                    }else{
                        setText(String.valueOf(item));
                    }
                }
            };
            return tableCell;
        });

        tvStudent.setRowFactory(new Callback<TableView<Student>, TableRow<Student>>() {
            @Override
            public TableRow<Student> call(TableView<Student> param) {
                TableRow<Student> rowSelected = new TableRow<>();

                rowSelected.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() == 1 && !rowSelected.isEmpty()) {
                            Student student = rowSelected.getItem();
//                            // show information student
//                            lbIdStudent.setText(student.getIdStudent());
//                            lbName.setText(student.getName());
//                            lbDateOfBirth.setText(student.getDateOfBirth());
//                            lbSex.setText(student.getSex());
//                            lbHometown.setText(student.getHometown());
//                            lbGpa.setText(String.valueOf(student.getGpa()));
//                            lbRank.setText(student.getRank());
//                            lbLicenseDate.setText(dateTimeFormatter.format(student.getLicenseDate()));
//                            lbGraduationYear.setText(String.valueOf(student.getGraduationYear()));
//                            lbRegisterNumber.setText(student.getRegisterNumber());
//                            lbMajor.setText(student.getMajor());
//                            lbModeOfStudy.setText(student.getModeOfStudy());
//                            lbTitle.setText(student.getTitle());
//                            lbClass.setText(student.getStrClass());
//                            lbNote.setText(student.getNote());
////                            lbTestShowInformationStudentSelected.setText(student.toString());
//                            System.out.println("STUDENT CLICKED: " + student);
                            showInformationStudent(student);
                        }
                    }
                });

                return rowSelected;
            }
        });
    }

    public void showInformationStudent(Student studentSelected){
        if (studentSelected.getIdStudent() != null) {
            lbIdStudent.setText(studentSelected.getIdStudent());
        }else{
            lbIdStudent.setText("");
        }
        if (studentSelected.getName() != null) {
            lbName.setText(studentSelected.getName());
        }else{
            lbName.setText("");
        }
        if (studentSelected.getDateOfBirth() != null){
            lbDateOfBirth.setText(studentSelected.getDateOfBirth());
        }else{
            lbDateOfBirth.setText("");
        }
        if (studentSelected.getSex() != null) {
            lbSex.setText(studentSelected.getSex());
        }else{
            lbSex.setText("");
        }
        if (studentSelected.getHometown() != null) {
            lbHometown.setText(studentSelected.getHometown());
        }else{
            lbHometown.setText("");
        }
        if (studentSelected.getGpa() != 0) {
            lbGpa.setText(String.valueOf(studentSelected.getGpa()));
        }else{
            lbGpa.setText("");
        }
        if (studentSelected.getRank() != null) {
            lbRank.setText(studentSelected.getRank());
        }else{
            lbRank.setText("");
        }
        if (studentSelected.getLicenseDate() != null) {
            lbLicenseDate.setText(dateTimeFormatter.format(studentSelected.getLicenseDate()));
        }else{
            lbLicenseDate.setText("");
        }
        if (studentSelected.getGraduationYear() != 0){
            lbGraduationYear.setText(String.valueOf(studentSelected.getGraduationYear()));
        }else{
            lbGraduationYear.setText("");
        }
        if (studentSelected.getRegisterNumber() != null) {
            lbRegisterNumber.setText(studentSelected.getRegisterNumber());
        }else{
            lbRegisterNumber.setText("");
        }
        if (studentSelected.getMajor() != null) {
            lbMajor.setText(studentSelected.getMajor());
        }else{
            lbMajor.setText("");
        }
        if (studentSelected.getModeOfStudy() != null) {
            lbModeOfStudy.setText(studentSelected.getModeOfStudy());
        }else{
            lbModeOfStudy.setText("");
        }
        if (studentSelected.getTitle() != null) {
            lbTitle.setText(studentSelected.getTitle());
        }else{
            lbTitle.setText("");
        }
        if (studentSelected.getStrClass() != null) {
            lbClass.setText(studentSelected.getStrClass());
        }else{
            lbClass.setText("");
        }
        if (studentSelected.getNote() != null) {
            lbNote.setText(studentSelected.getNote());
        }else{
            lbNote.setText("");
        }
//        System.out.println("studentSelected CLICKED: " + studentSelected);
    }

//    public void addData(MouseEvent mouseEvent) {
//        String excelFilePath = file.getPath();
//        try {
//            handlerExcelFile.writeExcel(excelFilePath, listTest, handlerExcelFile.getIndexRowLast(excelFilePath) + 1, false);
//            // after write, read file to display on tableview
//            liststudentSelecteds.clear();
//            listStudents = handlerExcelFile.readExcel(excelFilePath);
//
//            loadTableView();
//            System.out.println("last row: " + handlerExcelFile.getIndexRowLast(excelFilePath));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("Add data method");
//    }

    public void exportExcel(MouseEvent mouseEvent){
        List<Student> listStudentsHistory = FXCollections.observableArrayList();
        List<Student> listStudentsExport = FXCollections.observableArrayList();

        try {
            if (dpExportExcelFrom.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Nhập ngày cấp bằng");
                alert.setHeaderText(null);
                alert.setContentText("Bạn chưa chọn ngày để xuất dữ liệu!");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
                alert.showAndWait();
            } else {
                LocalDate localDateFrom = dpExportExcelFrom.getValue();
                LocalDate localDateTo = dpExportExcelTo.getValue();
                listStudentsHistory = handlerExcelFile.readExcel(file);

                for(Student student: listStudentsHistory){
                    if ((student.getLicenseDate().compareTo(localDateFrom) >= 0) && (student.getLicenseDate().compareTo(localDateTo) <= 0)){
                        listStudentsExport.add(student);
                    }
                }
                File file = new File("list_students_export.xlsx");
                file.createNewFile();
                handlerExcelFile.writeExcelExport(file,listStudentsExport,1,true);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Xuất dữ liệu thành công");
                alert.setHeaderText(null);
                alert.setContentText("Xuất dữ liệu ra file Excel thành công!");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
                alert.showAndWait();
            }
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xuất dữ liệu thất bại");
            alert.setHeaderText(null);
            alert.setContentText("Có lỗi trong quá trình xuất dữ liệu!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
            alert.showAndWait();
            e.printStackTrace();
        }

        System.out.println("Export excel file!");
    }

//    public void replaceData(MouseEvent mouseEvent) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Add New Excel File");
//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("Excel files", "*.xls;*.xlsx"),
//                new FileChooser.ExtensionFilter("All Files", "*.*"));
//        Stage stageChooseFile = new Stage();
//        File newFile = fileChooser.showOpenDialog(stageChooseFile);
//
//        if (newFile != null) {
//            try {
//                /** TEST **/
////            String excelFilePath = "D:\\projects\\eclipse-workspace\\ProjectSchool\\ProjectSchool\\write.xlsx";
////            handlerExcelFile.writeExcel(excelFilePath, listTest, 10, true);
////            // after write, read file to display again on tableview
////            listStudents.clear();
////            listStudents = handlerExcelFile.readExcel(excelFilePath);
//                /****/
//                listStudents.clear();
//                listStudents = handlerExcelFile.readExcel(newFile.getPath());
//                // read new data and write out file excel contain history
//                handlerExcelFile.writeExcel(file.getPath(),listStudents,1,true);
//                loadTableView();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            // CASE: cancel choose file
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Choose Excel");
//            alert.setHeaderText(null);
//            alert.setContentText("Bạn chưa chọn file excel!");
//            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
//            alert.showAndWait();
//        }
//        System.out.println("Replace data method");
//    }

    public void findIdStudent(KeyEvent keyEvent) {
        List<Student> listPersonsDisplay = FXCollections.observableArrayList();
        if (!tfIdStudent.getText().isEmpty()) {
            for (Student student : listStudents) {
                if (student.getIdStudent().toLowerCase().contains(tfIdStudent.getText().toLowerCase())) {
                    listPersonsDisplay.add(student);
                }
            }
            tvStudent.setItems((ObservableList<Student>) listPersonsDisplay);
        } else {
            System.out.println("empty");
            tvStudent.setItems((ObservableList<Student>) listStudents);
        }

        if (tvStudent.getItems().isEmpty()) {
            tfIdStudent.setStyle("-fx-background-color: red");
        } else {
            tfIdStudent.setStyle(null);
        }
        System.out.println("Find Id student method");
    }

    public void findName(KeyEvent keyEvent) {
        List<Student> listPersonsDisplay = FXCollections.observableArrayList();
        if (!tfName.getText().isEmpty()) {
            for (Student student : listStudents) {
                if (student.getName().toLowerCase().contains(tfName.getText().toLowerCase())) {
                    listPersonsDisplay.add(student);
                }
            }
            tvStudent.setItems((ObservableList<Student>) listPersonsDisplay);
        } else {
            System.out.println("empty");
            tvStudent.setItems((ObservableList<Student>) listStudents);
        }

        if (tvStudent.getItems().isEmpty()) {
            tfName.setStyle("-fx-background-color: red");
        } else {
            tfName.setStyle(null);
        }
        System.out.println("Find name method");
    }

    public void findDateOfBirth(KeyEvent keyEvent) {
        List<Student> listPersonsDisplay = FXCollections.observableArrayList();
        if (!tfDateOfBirth.getText().isEmpty()) {
            for (Student student : listStudents) {
                if (student.getDateOfBirth().toLowerCase().contains(tfDateOfBirth.getText().toLowerCase())) {
                    listPersonsDisplay.add(student);
                }
            }
            tvStudent.setItems((ObservableList<Student>) listPersonsDisplay);
        } else {
            System.out.println("empty");
            tvStudent.setItems((ObservableList<Student>) listStudents);
        }

        if (tvStudent.getItems().isEmpty()) {
            tfDateOfBirth.setStyle("-fx-background-color: red");
        } else {
            tfDateOfBirth.setStyle(null);
        }
        System.out.println("Find date of birth method");
    }
}
