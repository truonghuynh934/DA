package controller;

import database.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Major;
import model.ModeOfStudy;
import model.Rank;
import model.Student;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ManualDataEntryController implements Initializable {
    @FXML
    private Label lbTitleManualDataEntry;

    @FXML
    private TextField tfIdStudent;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfDay;
    @FXML
    private TextField tfMonth;
    @FXML
    private TextField tfYear;
    @FXML
    private RadioButton rbMale;
    @FXML
    private RadioButton rbFemale;
    @FXML
    private TextField tfHometown;
    @FXML
    private TextField tfGpa;
    @FXML
    private ComboBox<String> cbbRank;

    @FXML
    private DatePicker dpLicenseDate;
    @FXML
    private TextField tfGraduationYear;
    @FXML
    private TextField tfRegisterNumber;
    @FXML
    private TextField tfClass;
    @FXML
    private ComboBox<String> cbbMajor;
    @FXML
    private ComboBox<String> cbbModeOfStudy;
    @FXML
    private ComboBox<String> cbbTitle;

    @FXML
    private Button btAddStudent;
    @FXML
    private Button btUpdateStudent;
    @FXML
    private Button btRefresh;
    @FXML
    private Button btSaveListStudentsManualDataEntry;

    @FXML
    private TableView<Student> tvStudent;
    @FXML
    private TableColumn<Student, Button> tcDelete;
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

    MajorDAO majorDAO = new MajorDAO();
    ModeOfStudyDAO modeOfStudyDAO = new ModeOfStudyDAO();
    RankDAO rankDAO = new RankDAO();
    List<Major> listMajors = FXCollections.observableArrayList();
    List<ModeOfStudy> listModeOfStudies = FXCollections.observableArrayList();
    List<Rank> listRanks = FXCollections.observableArrayList();
    List<String> listMajorsVietnam = FXCollections.observableArrayList();
    List<String> listModeOfStudiesVietnam = FXCollections.observableArrayList();
    List<String> listRanksVietnam = FXCollections.observableArrayList();
    List<String> listTitles = FXCollections.observableArrayList("Cử nhân", "Kỹ sư");

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static List<Student> listStudentsManualDataEntry = FXCollections.observableArrayList();

    Calendar calendar= Calendar.getInstance();

    File file = new File("register_number.txt");

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        /**** TEST ****/
//        tfIdStudent.setText("5751071029");
//        tfName.setText("Nguyễn Duy Cư");
//        tfDay.setText("28");
//        tfMonth.setText("02");
//        tfYear.setText("1998");
//        tfHometown.setText("Nha Trang");
//        tfGpa.setText("9.68");
//        dpLicenseDate.setValue(LocalDate.of(2020,8,22));
//        tfRegisterNumber.setText("1223");
//        tfClass.setText("K57");
//        /********/
        tfGraduationYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        btAddStudent.setGraphic(new ImageView("/images/add.png"));
        btUpdateStudent.setGraphic(new ImageView("/images/update.png"));
        btRefresh.setGraphic(new ImageView("/images/refresh.png"));
        btSaveListStudentsManualDataEntry.setGraphic(new ImageView("/images/save.png"));

        // configure combobox
        listMajors = majorDAO.readJsonFile();
        for (Major major : listMajors) listMajorsVietnam.add(major.getVietnam());
        listModeOfStudies = modeOfStudyDAO.readJsonFile();
        for (ModeOfStudy modeOfStudy : listModeOfStudies) listModeOfStudiesVietnam.add(modeOfStudy.getVietnam());
        listRanks = rankDAO.readJsonFile();
        for (Rank rank : listRanks) listRanksVietnam.add(rank.getVietnam());

        cbbMajor.setItems((ObservableList<String>) listMajorsVietnam);
        if (!listMajorsVietnam.isEmpty()) cbbMajor.setValue(listMajorsVietnam.get(0));
        cbbModeOfStudy.setItems((ObservableList<String>) listModeOfStudiesVietnam);
        if (!listModeOfStudiesVietnam.isEmpty()) cbbModeOfStudy.setValue(listModeOfStudiesVietnam.get(0));
        cbbRank.setItems((ObservableList<String>) listRanksVietnam);
        if (!listRanksVietnam.isEmpty()) cbbRank.setValue(listRanksVietnam.get(0));
        cbbTitle.setItems((ObservableList<String>) listTitles);
        if (!listTitles.isEmpty()) cbbTitle.setValue(listTitles.get(0));

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
        dpLicenseDate.setConverter(converter);

        // configure tableview and its data
        tvStudent.setPlaceholder(new Label("Không có dữ liệu"));
        loadTableView();
        configureTableView();

        // configure other
        /** validate textfield **/
        validateTextFieldNumber(tfDay, "\\d+");
        validateTextFieldNumber(tfMonth, "\\d+");
        validateTextFieldNumber(tfYear, "\\d+");
        validateTextFieldNumber(tfGraduationYear, "\\d+");
        validateTextFieldNumber(tfRegisterNumber, "\\d+");
        validateGpa(tfGpa,"\\d{0,7}([\\.]\\d{0,4})?");

        checkLengthTextField(tfDay,2);
        checkLengthTextField(tfMonth,2);
        checkLengthTextField(tfYear,4);
        checkLengthTextField(tfGpa,5);
        checkLengthTextField(tfGraduationYear,4);
        checkLengthTextField(tfRegisterNumber,20);
        checkLengthTextField(tfClass,6);
        /****/
        btUpdateStudent.setDisable(true);

        // read register number
        tfRegisterNumber.setText(HandlerRegisterNumber.readFile(file));
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
        tvStudent.setItems((ObservableList<Student>) listStudentsManualDataEntry);

        if (tvStudent.getItems().isEmpty()) btSaveListStudentsManualDataEntry.setDisable(true);
        else btSaveListStudentsManualDataEntry.setDisable(false);
    }

    public void configureTableView(){
        // configure table column and table row
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
                    if (empty) {
                        setText("");
                    } else {
                        this.setText(dateTimeFormatter.format(item));
                    }
                }
            };
//            System.out.println("Đã vào date");
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
                            tfIdStudent.setText(student.getIdStudent());
                            tfName.setText(student.getName());
                            // split day/month/year
                            String[] date = student.getDateOfBirth().split("/");
                            if (date.length == 3) {
                                tfDay.setText(date[0]);
                                tfMonth.setText(date[1]);
                                tfYear.setText(date[2]);
                            } else {
                                tfDay.setText("");
                                tfMonth.setText("");
                                tfYear.setText(student.getDateOfBirth());
                            }
                            //
                            if (student.getSex().equals("Nam")) rbMale.setSelected(true);
                            else rbFemale.setSelected(true);
                            tfHometown.setText(student.getHometown());
                            tfGpa.setText(String.valueOf(student.getGpa()));
                            cbbRank.setValue(student.getRank());
                            dpLicenseDate.setValue(student.getLicenseDate());
                            tfGraduationYear.setText(String.valueOf(student.getGraduationYear()));
                            tfRegisterNumber.setText(student.getRegisterNumber());
                            cbbMajor.setValue(student.getMajor());
                            cbbModeOfStudy.setValue(student.getModeOfStudy());
                            cbbTitle.setValue(student.getTitle());
                            tfClass.setText(student.getStrClass());

                            tfIdStudent.setDisable(true);
                            btAddStudent.setDisable(true);
                            btUpdateStudent.setDisable(false);
                        }
                    }
                });

                return rowSelected;
            }
        });

        tcDelete.setCellFactory(new Callback<TableColumn<Student, Button>, TableCell<Student, Button>>() {
            @Override
            public TableCell<Student, Button> call(TableColumn<Student, Button> param) {
                final TableCell<Student, Button> cell = new TableCell<Student, Button>() {
                    Button btn = new Button("Xóa");

                    @Override
                    public void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!this.isEmpty()) {
                            setGraphic(btn);
                            this.setStyle("-fx-alignment:center");
//                            System.out.println("not empty");
                            btn.setOnAction(event -> {
                                System.out.println("Index row: " + getTableRow().getIndex());
                                deleteRow(getTableRow().getIndex());
                            });
                        } else {
                            setGraphic(null);
                        }
                    }
                };
                return cell;
            }
        });
    }

    public void addStudent(MouseEvent mouseEvent) {
        if (!isFullInformation()) {
            showPopup("Thêm sinh viên", "Bạn chưa điền đủ thông tin!", Alert.AlertType.ERROR);
        } else {
            /** information student **/
            String idStudent = tfIdStudent.getText();
            if (duplicateIdStudent(idStudent)){
                showPopup("Thêm sinh viên", "Mã sinh viên đã tồn tại!", Alert.AlertType.ERROR);
                return;
            }
            String name = tfName.getText();
            // date of birth
            String dateOfBirth = "";
            if (checkDate()) {
                if (tfDay.getText().isEmpty() || tfMonth.getText().isEmpty()) {
                    dateOfBirth = tfYear.getText();
                } else {
                    dateOfBirth = tfDay.getText() + "/" + tfMonth.getText() + "/" + tfYear.getText();
                }
            } else {
                showPopup("Thêm sinh viên", "Dữ liệu ngày sinh không hợp lệ!", Alert.AlertType.ERROR);
                return;
            }
            //
            String sex = "";
            if (rbMale.isSelected()) sex = "Nam";
            else if (rbFemale.isSelected()) sex = "Nữ";
            String hometown = tfHometown.getText();
            float gpa = Float.parseFloat(tfGpa.getText());
            if (gpa <0 || gpa>10){
                showPopup("Thêm sinh viên","Dữ liệu điểm TBC không hợp lệ!", Alert.AlertType.ERROR);
                return;
            }
            String rank = cbbRank.getValue();
            /** information gradutaion **/
            LocalDate licenseDate = dpLicenseDate.getValue();
            int graduationYear = Integer.parseInt(tfGraduationYear.getText());
            String registerNumber = tfRegisterNumber.getText();
            String strClass = tfClass.getText();
            String major = cbbMajor.getValue();
            String modeOfStudy = cbbModeOfStudy.getValue();
            String title = cbbTitle.getValue();

            Student student = new Student(idStudent, name, dateOfBirth, sex, hometown, gpa, rank, licenseDate, graduationYear, registerNumber, major, modeOfStudy, title, strClass);
            listStudentsManualDataEntry.add(student);

            int registerNumberNext = Integer.parseInt(registerNumber) + 1;
            HandlerRegisterNumber.writeFile(file, String.valueOf(registerNumberNext));
            tfRegisterNumber.setText(String.valueOf(registerNumberNext));

            loadTableView();
        }
//        System.out.println(listStudentsManualDataEntry);
        System.out.println("Add student method");
    }

    public void updateStudent(MouseEvent mouseEvent) {
        for (int i = 0; i < listStudentsManualDataEntry.size(); i++) {
            if (listStudentsManualDataEntry.get(i).getIdStudent().equals(tfIdStudent.getText())) {
                if (!isFullInformation()) {
                    showPopup("Cập nhật sinh viên", "Bạn chưa điền đủ thông tin!", Alert.AlertType.ERROR);
                } else {
                    listStudentsManualDataEntry.get(i).setName(tfName.getText());
                    // date of birth
                    String dateOfBirth = "";
                    if (checkDate()) {
                        if (tfDay.getText().isEmpty() || tfMonth.getText().isEmpty()) {
                            dateOfBirth = tfYear.getText();
                        } else {
                            dateOfBirth = tfDay.getText() + "/" + tfMonth.getText() + "/" + tfYear.getText();
                        }
                    } else {
                        showPopup("Cập nhật sinh viên", "Dữ liệu ngày sinh không hợp lệ!", Alert.AlertType.ERROR);
                        return;
                    }
                    //
                    listStudentsManualDataEntry.get(i).setDateOfBirth(dateOfBirth);
                    if (rbMale.isSelected()) listStudentsManualDataEntry.get(i).setSex("Nam");
                    else listStudentsManualDataEntry.get(i).setSex("Nữ");
                    listStudentsManualDataEntry.get(i).setHometown(tfHometown.getText());
                    float gpa = Float.parseFloat(tfGpa.getText());
                    if (gpa <0 || gpa>10){
                        showPopup("Thêm sinh viên","Dữ liệu điểm TBC không hợp lệ!", Alert.AlertType.ERROR);
                        return;
                    }
                    listStudentsManualDataEntry.get(i).setGpa(gpa);
                    listStudentsManualDataEntry.get(i).setRank(cbbRank.getValue());
                    listStudentsManualDataEntry.get(i).setLicenseDate(dpLicenseDate.getValue());
                    listStudentsManualDataEntry.get(i).setGraduationYear(Integer.parseInt(tfGraduationYear.getText()));
                    listStudentsManualDataEntry.get(i).setRegisterNumber(tfRegisterNumber.getText());
                    listStudentsManualDataEntry.get(i).setMajor(cbbMajor.getValue());
                    listStudentsManualDataEntry.get(i).setModeOfStudy(cbbModeOfStudy.getValue());
                    listStudentsManualDataEntry.get(i).setTitle(cbbTitle.getValue());
                    listStudentsManualDataEntry.get(i).setStrClass(tfClass.getText());
                }
                break;
            }
        }

        refresh(mouseEvent);
        System.out.println("Update student method");
    }

    public void refresh(MouseEvent mouseEvent) {
        tfIdStudent.clear();
        tfName.clear();
        tfDay.clear();
        tfMonth.clear();
        tfYear.clear();
        rbMale.setSelected(true);
        tfHometown.clear();
        tfGpa.clear();
        dpLicenseDate.setValue(null);
        tfGraduationYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
//        tfRegisterNumber.clear();
        tfClass.setText("K");

        tfIdStudent.setDisable(false);
        btAddStudent.setDisable(false);
        btUpdateStudent.setDisable(true);
        loadTableView();

        System.out.println("Refresh method");
    }

    public void saveListStudentsManualDataEntry(MouseEvent mouseEvent) {
        // save list students manual data entry INTO list students print
        if (listStudentsManualDataEntry!= null && !listStudentsManualDataEntry.isEmpty()) MainController.listStudentsPrint.addAll(listStudentsManualDataEntry);

        // after save
        listStudentsManualDataEntry.clear();
        refresh(mouseEvent);
        System.out.println("Save information method");
    }

    public void deleteRow(int indexRow) {
        for (int i = 0; i < listStudentsManualDataEntry.size(); i++) {
            if (i == indexRow) {
                // delete row
                listStudentsManualDataEntry.remove(i);
//                loadTableView();
                System.out.println("Đã delete");
                loadTableView();
                break;
            }
        }
        System.out.println("delete method");
    }

    public void checkLengthTextField(TextField textField, int maxLength){
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > maxLength){
                    textField.setText(oldValue);
                }
            }
        });
    }

    public void validateTextFieldNumber(TextField textField, String regex) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches(regex)) {
//                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                    textField.setText(newValue.replaceAll("[^"+regex+"]", ""));
                }
            }
        });
    }
    public void validateGpa(TextField textField, String regex){
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches(regex)) {
                    textField.setText(oldValue);
                }
            }
        });
    }

    public boolean isFullInformation() {
        if (!tfIdStudent.getText().isEmpty() && !tfName.getText().isEmpty() && !tfYear.getText().isEmpty()
                && !tfHometown.getText().isEmpty() && !tfGpa.getText().isEmpty() && dpLicenseDate.getValue() != null
                && !tfGraduationYear.getText().isEmpty() && !tfRegisterNumber.getText().isEmpty() && !tfClass.getText().isEmpty())
            return true;
        return false;
    }

    public boolean checkDate() {
        String patternYear = "(\\d{4})";
        String patternMonth = "((0[1-9])|(1[0-2]))";
        String patternDay1 = "((0[1-9])|(1[0-9])|(2[0-8]))";
        String patternDay2 = "((0[1-9])|(1[0-9])|(2[0-9]))";
        String patternDay3 = "((0[1-9])|(1[0-9])|(2[0-9])|(30))";
        String patternDay4 = "((0[1-9])|(1[0-9])|(2[0-9])|(3[0-1]))";

        if (!tfDay.getText().isEmpty() && !tfMonth.getText().isEmpty() && !tfYear.getText().isEmpty()) {
            System.out.println("==================================");
            // check year
            if (!checkPattern(patternYear, tfYear, "1900")) return false;
            // check month
            if (!checkPattern(patternMonth, tfMonth, "01")) return false;
            // check day
            if (tfMonth.getText().equals("02")) {
                int year = Integer.parseInt(tfYear.getText());
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    if (!checkPattern(patternDay2, tfDay, "01")) return false;
                } else {
                    if (!checkPattern(patternDay1, tfDay, "01")) return false;
                }
            } else if (tfMonth.getText().equals("04") || tfMonth.getText().equals("06") || tfMonth.getText().equals("09") || tfMonth.getText().equals("11")) {
                if (!checkPattern(patternDay3, tfDay, "01")) return false;
            } else {
                if (!checkPattern(patternDay4, tfDay, "01")) return false;
            }
            return true;

            // case only year
        } else if (!tfYear.getText().isEmpty()) {
            // check year
            if (!checkPattern(patternYear, tfYear, "1900")) return false;
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPattern(String strPattern, TextField textField, String strDefault) {
        Pattern patternYear = Pattern.compile(strPattern);
        if (patternYear.matcher(textField.getText()).matches()) {
            System.out.println(textField.getId() + " standar");
            return true;
        } else {
            System.out.println(textField.getId() + " no standar");
            textField.setText(strDefault);
            return false;
        }
    }

    public void showPopup(String title, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
        alert.showAndWait();
    }

    public boolean duplicateIdStudent(String idStudent){
        for(Student student: listStudentsManualDataEntry){
            if (student.getIdStudent().equals(idStudent)){
                return true;
            }
        }
        return false;
    }
}
