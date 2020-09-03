package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Student;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

public class ImportExcelFileController implements Initializable {

    // index columns in excel file (start 0)
    public static final int COLUMN_INDEX_ID = 2;
    public static final int COLUMN_INDEX_ID_STUDENT = 3;
    public static final int COLUMN_INDEX_FIRST_NAME = 4;
    public static final int COLUMN_INDEX_LAST_NAME = 5;
    public static final int COLUMN_INDEX_SEX = 6;
    public static final int COLUMN_INDEX_DATE_OF_BIRTH = 7;
    public static final int COLUMN_INDEX_HOMETOWN = 8;
    public static final int COLUMN_INDEX_GPA = 9;
    public static final int COLUMN_INDEX_RANK = 10;

    @FXML
    private TableColumn<Student, Integer> tcIdExcel;
    @FXML
    private TableColumn<Student, String> tcIdStudentExcel;
    @FXML
    private TableColumn<Student, String> tcNameExcel;
    @FXML
    private TableColumn<Student, String> tcSexExcel;
    @FXML
    private TableColumn<Student, String> tcDateOfBirthExcel;
    @FXML
    private TableColumn<Student, String> tcHometownExcel;
    @FXML
    private TableColumn<Student, Float> tcGPAExcel;
    @FXML
    private TableColumn<Student, String> tcRankExcel;
    @FXML
    private TableView<Student> tvInformationExcel;

    @FXML
    private Button btImportExcel;
    @FXML
    private Button btRefresh;
    @FXML
    private Label lbTitleImportExcel;

    public static List<Student> listStudentsFromExcel = FXCollections.observableArrayList();

    private File file = null;

//    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btImportExcel.setGraphic(new ImageView("/images/import_excel.png"));
        btRefresh.setGraphic(new ImageView("/images/refresh.png"));
        tvInformationExcel.setPlaceholder(new Label("Không có dữ liệu"));

        if (listStudentsFromExcel != null && listStudentsFromExcel.size() == 0)
            tvInformationExcel.getItems().clear();
        if (listStudentsFromExcel != null && listStudentsFromExcel.size() > 0)
            loadTableExcel(listStudentsFromExcel);
        else System.out.println("Load lại khởi tạo: Không có dữ liệu được import!");

        // test
        if (listStudentsFromExcel != null)
            System.out.println("KÍCH THƯỚC: " + listStudentsFromExcel + ", " + listStudentsFromExcel.size());
        else System.out.println("KÍCH THƯỚC: NULL " + listStudentsFromExcel);
    }

    public void importExcel(MouseEvent mouseEvent) throws IOException, ParseException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Excel File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel files", "*.xls;*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        Stage stageChooseFile = new Stage();
        file = fileChooser.showOpenDialog(stageChooseFile);

        // tạm thời lấy trực tiếp file để test cho nhanh
//        file = new File("D:\\Im_Learning\\CNTT\\Ky8_DeTaiTotNghiep\\Source\\1_InBang\\mau_excel_in_bang_tot_nghiep.xls");
//        file = new File("D:\\Im_Learning\\CNTT\\Ky8_DeTaiTotNghiep\\Source\\1_InBang\\excel\\test_it_row.xls");

        if (file != null) {
            // CASE: choose file
            chooseExcelFile();
            // load data table
            if (listStudentsFromExcel != null && listStudentsFromExcel.size() == 0)
                tvInformationExcel.getItems().clear();
            else if (listStudentsFromExcel != null && listStudentsFromExcel.size() > 0)
                loadTableExcel(listStudentsFromExcel);
            else System.out.println("Không có dữ liệu được import!");
        } else {
            // CASE: cancel choose file
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Import Excel");
            alert.setHeaderText(null);
            alert.setContentText("Bạn chưa chọn file excel!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
            alert.showAndWait();
        }
    }

    public void chooseExcelFile() throws IOException, ParseException {
        int numberRowImported = 0;

        listStudentsFromExcel = readExcel(file.getPath());
        numberRowImported = listStudentsFromExcel.size();

        // in list Student thử
        System.out.println("******* INFORMATION DETAIL ********");
        for (int i = 0; i < listStudentsFromExcel.size(); i++) {
            System.out.println(i + ": " + listStudentsFromExcel.get(i).toString());
        }

        if (numberRowImported > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Import Excel");
            alert.setHeaderText(null);
            alert.setContentText("Đã import thành công " + numberRowImported + " hàng!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Import Excel");
            alert.setHeaderText(null);
            alert.setContentText("Không có dữ liệu được import!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
            alert.showAndWait();
        }
    }

    public List<Student> readExcel(String excelFilePath) throws IOException, ParseException {
        List<Student> listStudents = FXCollections.observableArrayList();

        // Get file
        InputStream inputStream = new FileInputStream(new File(excelFilePath));
        // Get workbook
        Workbook workbook = getWorkbook(inputStream, excelFilePath);
        // Get sheet
        Sheet sheet = workbook.getSheetAt(0);

        // Get all rows in a sheet
        Iterator<Row> iteratorRow = sheet.iterator();
        while (iteratorRow.hasNext()) {
            Row nextRow = iteratorRow.next();
            // ignore 9 row first
            if (nextRow.getRowNum() >= 0 && nextRow.getRowNum() <= 8) continue;
//            System.out.println("- Đang duyệt dòng thứ: " + nextRow.getRowNum());

            // get all cells in a row
            Student student = new Student();
            String first_name = "", last_name = "";
            Iterator<Cell> iteratorCell = nextRow.cellIterator();
            while (iteratorCell.hasNext()) {
                Cell nextCell = iteratorCell.next();
                // ignore 2 cell first
                if (nextCell.getColumnIndex() == 0 || nextCell.getColumnIndex() == 1) continue;
//                System.out.println("\tĐang đọc ô thứ: " + nextCell.getAddress());
//                System.out.println("\t\tCell type: "+nextCell.getCellType() + ", cell type num: " + nextCell.getCellTypeEnum());

                Object cellValue = getCellValue(nextCell);
                // ignore cell null
                if (cellValue == null) continue;
                switch (nextCell.getColumnIndex()) {
                    case COLUMN_INDEX_ID:
                        break;
                    case COLUMN_INDEX_ID_STUDENT:
                        student.setIdStudent((String) cellValue);
                        break;
                    case COLUMN_INDEX_FIRST_NAME:
                        first_name = (String) cellValue;
                        break;
                    case COLUMN_INDEX_LAST_NAME:
                        last_name = (String) cellValue;
                        student.setName(first_name + " " + last_name);
                        break;
                    case COLUMN_INDEX_SEX:
                        student.setSex((String) cellValue);
                        break;
                    case COLUMN_INDEX_DATE_OF_BIRTH:
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                        Date date = simpleDateFormat.parse((String) cellValue);
//                        student.setDateOfBirth(date);

//                        LocalDate localDate = LocalDate.parse(String.valueOf(cellValue), dateTimeFormatter);
//                        student.setDateOfBirth(localDate);

                        student.setDateOfBirth((String) cellValue);
                        break;
                    case COLUMN_INDEX_HOMETOWN:
                        student.setHometown((String) cellValue);
                        break;
                    case COLUMN_INDEX_GPA:
                        student.setGpa(Float.valueOf(cellValue.toString()));
                        break;
                    case COLUMN_INDEX_RANK:
                        student.setRank((String) cellValue);
                        break;
                    default:
                        break;
                }

            }
            if (student.getIdStudent() != null) listStudents.add(student);
        }

        workbook.close();
        inputStream.close();
        return listStudents;
    }

    private Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException {
        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }

    private Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        Object cellValue = null;
        switch (cellType) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case _NONE:
            case BLANK:
            case ERROR:
                break;
            default:
                break;
        }
        return cellValue;
    }

    public void loadTableExcel(List<Student> listStudents) {
        tcIdStudentExcel.setCellValueFactory(new PropertyValueFactory<Student, String>("idStudent"));
        tcNameExcel.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        tcSexExcel.setCellValueFactory(new PropertyValueFactory<Student, String>("sex"));
        tcDateOfBirthExcel.setCellValueFactory(new PropertyValueFactory<Student, String>("dateOfBirth"));
        tcHometownExcel.setCellValueFactory(new PropertyValueFactory<Student, String>("hometown"));
        tcGPAExcel.setCellValueFactory(new PropertyValueFactory<Student, Float>("gpa"));
        tcRankExcel.setCellValueFactory(new PropertyValueFactory<Student, String>("rank"));
//        tvInformationExcel.setItems(FXCollections.<Student>observableArrayList(listStudents));
        tvInformationExcel.setItems((ObservableList<Student>)listStudents);

//        tcDateOfBirthExcel.setCellFactory(column -> {
//            TableCell<Student, LocalDate> tableCell = new TableCell<Student, LocalDate>() {
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//                @Override
//                protected void updateItem(LocalDate item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty) {
//                        setText("");
//                    } else {
////                        this.setText(simpleDateFormat.format(item));
//                        this.setText(dateTimeFormatter.format(item));
//                    }
//                }
//            };
////            System.out.println("Đã vào date");
//            return tableCell;
//        });

        tcIdExcel.setCellFactory(new Callback<TableColumn<Student, Integer>, TableCell<Student, Integer>>() {
            @Override
            public TableCell<Student, Integer> call(TableColumn<Student, Integer> param) {
                return new TableCell<Student, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && (this.getTableRow().getIndex() < tvInformationExcel.getItems().size())) {
                            setText((this.getTableRow().getIndex() + 1) + "");
//                            System.out.println(this.getTableRow().getIndex()+1);
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });

//        tvInformationExcel.getSelectionModel().setSelectionMode(
//                SelectionMode.MULTIPLE
//        );

        System.out.println("Load table method");
    }

    public void refresh(MouseEvent mouseEvent) {
        listStudentsFromExcel.clear();
        tvInformationExcel.refresh();
        System.out.println("Refresh method");
    }
}
