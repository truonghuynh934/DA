package controller;

import database.HandlerRegisterNumber;
import database.MajorDAO;
import database.ModeOfStudyDAO;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Major;
import model.ModeOfStudy;
import model.Student;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class AssignInformationController implements Initializable {
    @FXML
    private Label lbTitleAssignInformation;
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
    private Button btAssignSelected;
    @FXML
    private Button btAssignAll;
    @FXML
    private Button btRefresh;
    @FXML
    private Button btSaveListStudentsAssign;

    @FXML
    private TableView<Student> tvStudent;
    @FXML
    private TableColumn<Student, Boolean> tcSelect;
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
    List<String> listTitles = FXCollections.observableArrayList("Cử nhân", "Kỹ sư");
    List<Major> listMajors = FXCollections.observableArrayList();
    List<ModeOfStudy> listModeOfStudies = FXCollections.observableArrayList();
    List<String> listMajorsVietnam = FXCollections.observableArrayList();
    List<String> listModeOfStudiesVietnam = FXCollections.observableArrayList();

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    List<Student> listStudentsAssignInformation = FXCollections.observableArrayList();
    List<Integer> listIndex= FXCollections.observableArrayList();

    Calendar calendar = Calendar.getInstance();

    File file = new File("register_number.txt");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfGraduationYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        // init checkbox column
        tvStudent.setEditable(true);
        for(int i=0;i<listStudentsAssignInformation.size();i++){
            listStudentsAssignInformation.get(i).setSelect(false);
        }
        //

        btAssignSelected.setGraphic(new ImageView("/images/assign_selected.png"));
        btAssignAll.setGraphic(new ImageView("/images/assign_all.png"));
        btRefresh.setGraphic(new ImageView("/images/refresh.png"));
        btSaveListStudentsAssign.setGraphic(new ImageView("/images/save.png"));

        // configure combobox
        listMajors = majorDAO.readJsonFile();
        for (Major major : listMajors) listMajorsVietnam.add(major.getVietnam());
        listModeOfStudies = modeOfStudyDAO.readJsonFile();
        for (ModeOfStudy modeOfStudy : listModeOfStudies) listModeOfStudiesVietnam.add(modeOfStudy.getVietnam());

        cbbMajor.setItems((ObservableList<String>) listMajorsVietnam);
        if (!listMajorsVietnam.isEmpty()) cbbMajor.setValue(listMajorsVietnam.get(0));
        cbbModeOfStudy.setItems((ObservableList<String>) listModeOfStudiesVietnam);
        if (!listModeOfStudiesVietnam.isEmpty()) cbbModeOfStudy.setValue(listModeOfStudiesVietnam.get(0));
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
        if (ImportExcelFileController.listStudentsFromExcel != null) {
            listStudentsAssignInformation = ImportExcelFileController.listStudentsFromExcel;
        }
        tvStudent.setPlaceholder(new Label("Không có dữ liệu"));
        loadTableView();
        configureTableView();
        findRowLackInformation();

        // configure other
        validateTextFieldNumber(tfGraduationYear, "\\d+");
        validateTextFieldNumber(tfRegisterNumber, "\\d+");

        checkLengthTextField(tfGraduationYear,4);
        checkLengthTextField(tfRegisterNumber,20);
        checkLengthTextField(tfClass,6);

        /****/
        tfRegisterNumber.setText(HandlerRegisterNumber.readFile(file));

    }

    public void loadTableView() {
        findRowLackInformation();

        tcSelect.setCellValueFactory(new PropertyValueFactory<Student, Boolean>("select"));
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
        tvStudent.setItems((ObservableList<Student>) listStudentsAssignInformation);

        if (assignedAll() && !tvStudent.getItems().isEmpty()) btSaveListStudentsAssign.setDisable(false);
        else btSaveListStudentsAssign.setDisable(true);

        if(tvStudent.getItems().isEmpty()){
            btAssignSelected.setDisable(true);
            btAssignAll.setDisable(true);
        }
    }

    public void configureTableView(){
        // configure table column and table row
        tcSelect.setCellFactory(new Callback<TableColumn<Student, Boolean>, TableCell<Student, Boolean>>() {
            @Override
            public TableCell<Student, Boolean> call(TableColumn<Student, Boolean> param) {
                return new CheckBoxTableCell<Student, Boolean>();
            }
        });
        tcSelect.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student, Boolean>, ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Student, Boolean> param) {
                Student student = param.getValue();

                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(student.isSelect());

                // Khi cột "CheckBox" thay đổi
                booleanProp.addListener(new ChangeListener<Boolean>() {

                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                                        Boolean newValue) {
                        student.setSelect(newValue);
                    }
                });
                return booleanProp;
            }
        });

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
                        this.setText(dateTimeFormatter.format(item));
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
                    if (empty || item == null || item == 0) {
                        setText("");
                    } else {
                        setText(item+"");
                    }
                }
            };
            return tableCell;
        });
//        tcRegisterNumber.setCellFactory(column -> {
//            TableCell<Student, Integer> tableCell = new TableCell<Student, Integer>() {
//                @Override
//                protected void updateItem(Integer item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty || item == null || item==0) {
//                        setText("");
//                    }else{
//                        setText(item+"");
//                    }
//                }
//            };
//            return tableCell;
//        });
        tvStudent.setRowFactory(new Callback<TableView<Student>, TableRow<Student>>() {
            @Override
            public TableRow<Student> call(TableView<Student> param) {
                return new TableRow<Student>(){
                    @Override
                    protected void updateItem(Student item, boolean empty) {
                        super.updateItem(item, empty);
                        if (listIndex.contains(getIndex())) {
                            setStyle("-fx-background-color: #b0e0e6");
                        }else{
                            setStyle("-fx-background-color: white");
                            setStyle("-fx-font-style: italic");
                        }
                    }
                };
            }
        });
    }

    public void assignSelected(MouseEvent mouseEvent) {
        if (!isFullInformation()) {
            showPopup("Gán thông tin", "Bạn chưa điền đầy đủ thông tin!", Alert.AlertType.ERROR);
        } else {
            int registerNumber = Integer.parseInt(HandlerRegisterNumber.readFile(file));
            for (int i = 0; i < listStudentsAssignInformation.size(); i++) {
                if (listStudentsAssignInformation.get(i).isSelect()) {
                    listStudentsAssignInformation.get(i).setSelect(false);
                    listStudentsAssignInformation.get(i).setLicenseDate(dpLicenseDate.getValue());
                    listStudentsAssignInformation.get(i).setGraduationYear(Integer.parseInt(tfGraduationYear.getText()));
                    listStudentsAssignInformation.get(i).setRegisterNumber(tfRegisterNumber.getText());
                    listStudentsAssignInformation.get(i).setMajor(cbbMajor.getValue());
                    listStudentsAssignInformation.get(i).setModeOfStudy(cbbModeOfStudy.getValue());
                    listStudentsAssignInformation.get(i).setTitle(cbbTitle.getValue());
                    listStudentsAssignInformation.get(i).setStrClass(tfClass.getText());
                    System.out.println(i + " ==> " + listStudentsAssignInformation.get(i).toString());

                    registerNumber = Integer.parseInt(tfRegisterNumber.getText()) + 1;
                    tfRegisterNumber.setText(String.valueOf(registerNumber));
                }
            }
            HandlerRegisterNumber.writeFile(file, String.valueOf(registerNumber));
            loadTableView();
            configureTableView();
        }
        System.out.println("Assign list selected method");
    }

    public void assignAll(MouseEvent mouseEvent) {
        if (!isFullInformation()) {
            showPopup("Gán thông tin", "Bạn chưa điền đầy đủ thông tin!", Alert.AlertType.ERROR);
        } else {
            int registerNumber = Integer.parseInt(HandlerRegisterNumber.readFile(file));
            for (int i = 0; i < listStudentsAssignInformation.size(); i++) {
                listStudentsAssignInformation.get(i).setSelect(false);
                listStudentsAssignInformation.get(i).setLicenseDate(dpLicenseDate.getValue());
                listStudentsAssignInformation.get(i).setGraduationYear(Integer.parseInt(tfGraduationYear.getText()));
                listStudentsAssignInformation.get(i).setRegisterNumber(tfRegisterNumber.getText());
                listStudentsAssignInformation.get(i).setMajor(cbbMajor.getValue());
                listStudentsAssignInformation.get(i).setModeOfStudy(cbbModeOfStudy.getValue());
                listStudentsAssignInformation.get(i).setTitle(cbbTitle.getValue());
                listStudentsAssignInformation.get(i).setStrClass(tfClass.getText());
                System.out.println(i + " ==> " + listStudentsAssignInformation.get(i).toString());

                registerNumber = Integer.parseInt(tfRegisterNumber.getText()) + 1;
                tfRegisterNumber.setText(String.valueOf(registerNumber));
            }
            HandlerRegisterNumber.writeFile(file,String.valueOf(registerNumber));
            loadTableView();
            configureTableView();
        }
        System.out.println("Assign list method");
    }

    public void refresh(MouseEvent mouseEvent) {
        dpLicenseDate.setValue(null);
        tfGraduationYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
//        tfRegisterNumber.clear();
        tfClass.setText("K");

        loadTableView();
        System.out.println("Refresh method");
    }

    public void saveListStudentsAssign(MouseEvent mouseEvent) {
        if (listStudentsAssignInformation != null && !listStudentsAssignInformation.isEmpty())
            MainController.listStudentsPrint.addAll(listStudentsAssignInformation);

        // after save all
        listStudentsAssignInformation.clear();
        refresh(mouseEvent);
        System.out.println("Save list students assign");
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

    public boolean isFullInformation() {
        if (dpLicenseDate.getValue() != null && !tfGraduationYear.getText().isEmpty() && !tfRegisterNumber.getText().isEmpty()
                && !tfClass.getText().isEmpty()) {
            return true;
        }
        return false;
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

    public void findRowLackInformation(){
        listIndex = FXCollections.observableArrayList();
        for(int i=0;i<listStudentsAssignInformation.size();i++){
            boolean checkNull = false;
            // ignore 2 cell each row
            for(int j=2;j<tvStudent.getColumns().size();j++){
                if (tvStudent.getColumns().get(j).getCellData(i) == null) {
                    checkNull = true;
                    if (!listIndex.contains(new Integer(i))) listIndex.add(new Integer(i));
                    break;
                }
            }
            if (!checkNull) listIndex.remove(new Integer(i));
        }
        System.out.println("LIST INDEX NULL: " + listIndex.toString());
    }

    public boolean assignedAll(){
        for(int i=0;i<listStudentsAssignInformation.size();i++){
            // ignore 2 cell each row
            for(int j=2;j<tvStudent.getColumns().size();j++){
                if (tvStudent.getColumns().get(j).getCellData(i) == null) {
                    return false;
                }
            }
        }
        return true;
    }
}
