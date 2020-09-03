package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import database.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Major;
import model.ModeOfStudy;
import model.Rank;
import model.Student;
import org.jpedal.examples.viewer.OpenViewerFX;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class PrintController implements Initializable {
    @FXML
    private TableView tvInformationPrint;
    @FXML
    private TableColumn<Student, Boolean> tcSelect;
    @FXML
    private TableColumn<Student, Integer> tcId;
    @FXML
    private TableColumn<Student, String> tcName;
    @FXML
    private TableColumn<Student, String> tcDateOfBirth;
    @FXML
    private TableColumn<Student, String> tcSex;
    @FXML
    private TableColumn<Student, String> tcHometown;
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
    private Label lbTitlePrint;

    @FXML
    private RadioButton rbCertificateBachelor;
    @FXML
    private RadioButton rbCertificateEngineer;
    @FXML
    private RadioButton rbStyle1;
    @FXML
    private RadioButton rbStyle2;
    @FXML
    private RadioButton rbStyle3;
    @FXML
    private RadioButton rbStyle4;

    @FXML
    private Button btDelete;
    @FXML
    private Button btPrintPreview;
    @FXML
    private Button btPrint;


    Stage stage = new Stage();
    OpenViewerFX openViewerFX;
    // take and format date print certificate
//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Calendar calendar = Calendar.getInstance();
//    Date date = calendar.getTime();

    MajorDAO majorDAO = new MajorDAO();
    List<Major> listMajors = majorDAO.readJsonFile();
    ModeOfStudyDAO modeOfStudyDAO = new ModeOfStudyDAO();
    List<ModeOfStudy> listModeOfStudies = modeOfStudyDAO.readJsonFile();
    RankDAO rankDAO = new RankDAO();
    List<Rank> listRanks = rankDAO.readJsonFile();
    /**
     * how to save listStudentsPrinted
     **/
    // Way 1: save into json file
//    StudentPrintedDAO studentPrintedDAO = new StudentPrintedDAO();
//    List<Student> listStudentsPrinted = studentPrintedDAO.readJsonFile();
    // Way 2: save into excel file
    HandlerExcelFile handlerExcelFile = new HandlerExcelFile();
    File file = new File("history_student_printed.xlsx");

//    List<Student> listStudentsPrinted = FXCollections.observableArrayList();
    private List<Student> listStudents = FXCollections.observableArrayList();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public void initialize(URL location, ResourceBundle resources) {
        // init checkbox column
        tvInformationPrint.setEditable(true);
        for(int i=0;i<listStudents.size();i++) {
            listStudents.get(i).setSelect(false);
        }
        //

        btDelete.setGraphic(new ImageView("/images/delete.png"));
        btPrintPreview.setGraphic(new ImageView("/images/print_preview.png"));
        btPrint.setGraphic(new ImageView("/images/print.png"));

        listStudents = MainController.listStudentsPrint;

        tvInformationPrint.setPlaceholder(new Label("Không có dữ liệu"));
        // load data table
        if (listStudents != null && listStudents.size() > 0)
            loadTablePrint(listStudents);
        else System.out.println("Không có dữ liệu được import!");

        // test
        if (listStudents != null) System.out.println("KÍCH THƯỚC: " + listStudents + ", " + listStudents.size());
        else System.out.println("KÍCH THƯỚC: NULL " + listStudents);
    }

    public void loadTablePrint(List<Student> listStudents) {
        tcSelect.setCellValueFactory(new PropertyValueFactory<Student, Boolean>("select"));
        tcName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        tcDateOfBirth.setCellValueFactory(new PropertyValueFactory<Student, String>("dateOfBirth"));
        tcSex.setCellValueFactory(new PropertyValueFactory<Student, String>("sex"));
        tcHometown.setCellValueFactory(new PropertyValueFactory<Student, String>("hometown"));
        tcRank.setCellValueFactory(new PropertyValueFactory<Student, String>("rank"));
        tcLicenseDate.setCellValueFactory(new PropertyValueFactory<Student, LocalDate>("licenseDate"));
        tcGraduationYear.setCellValueFactory(new PropertyValueFactory<Student, Integer>("graduationYear"));
        tcRegisterNumber.setCellValueFactory(new PropertyValueFactory<Student, String>("registerNumber"));
        tcMajor.setCellValueFactory(new PropertyValueFactory<Student, String>("major"));
        tcModeOfStudy.setCellValueFactory(new PropertyValueFactory<Student, String>("modeOfStudy"));
        tcTitle.setCellValueFactory(new PropertyValueFactory<Student, String>("title"));
        tcClass.setCellValueFactory(new PropertyValueFactory<Student, String>("strClass"));
        tvInformationPrint.setItems(FXCollections.<Student>observableArrayList(listStudents));

        // configure table column
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

                        if (this.getTableRow() != null && (this.getTableRow().getIndex() < tvInformationPrint.getItems().size())) {
                            setText((this.getTableRow().getIndex() + 1) + "");
//                            System.out.println(this.getTableRow().getIndex()+1);
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
//        tcDateOfBirth.setCellFactory(column -> {
//            TableCell<Student, LocalDate> tableCell = new TableCell<Student, LocalDate>() {
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
        tcLicenseDate.setCellFactory(column -> {
            TableCell<Student, LocalDate> tableCell = new TableCell<Student, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("");
                    } else {
//                        this.setText(simpleDateFormat.format(item));
                        this.setText(dateTimeFormatter.format(item));
                    }
                }
            };
//            System.out.println("Đã vào date license");
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
                        setText(item + "");
                    }
                }
            };
//            System.out.println("Đã vào date license");
            return tableCell;
        });
//        tcRegisterNumber.setCellFactory(column -> {
//            TableCell<Student, Integer> tableCell = new TableCell<Student, Integer>() {
//                @Override
//                protected void updateItem(Integer item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty || item == null || item == 0) {
//                        setText("");
//                    } else {
//                        setText(item + "");
//                    }
//                }
//            };
////            System.out.println("Đã vào date license");
//            return tableCell;
//        });

        tvInformationPrint.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
    }

    public void deleteStudent(MouseEvent mouseEvent) {
        if (tvInformationPrint.getItems().size() > 0) {
//            List<Student> studentListSelected = tvInformationPrint.getSelectionModel().getSelectedItems();

            List<Student> listStudentsSelected = findStudentsSelected(listStudents);

            System.out.println("Sinh viên được chọn để xóa: " + listStudentsSelected);

            if (listStudentsSelected.size() > 0) {
//                for (int i = 0; i < studentListSelected.size(); i++) {
//                    Student studentSelected = studentListSelected.get(i);
//                    listStudents.remove(studentSelected);
//                    System.out.println("Đã xóa: " + studentListSelected.get(i).toString());
//                }
                listStudents.removeAll(listStudentsSelected);
                // load table after delete
                loadTablePrint(listStudents);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Xóa");
                alert.setHeaderText(null);
                alert.setContentText("Bạn chưa chọn người để xóa!");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add((new Image(this.getClass().getResourceAsStream("/images/logo.png"))));
                alert.showAndWait();
            }
        } else {
            System.out.println("Không có hàng nào hết");
        }
    }

    public void printPreview(MouseEvent mouseEvent) throws IOException, DocumentException {
        if (tvInformationPrint.getItems().size() > 0) {
            String fileCertificateTemplate = "";
            if (rbCertificateBachelor.isSelected()) {
                fileCertificateTemplate = "phoibang_cunhan.pdf";
            } else if (rbCertificateEngineer.isSelected()) {
                fileCertificateTemplate = "phoibang_kysu.pdf";
            }
            //Read file using PdfReader
            PdfReader pdfReader = new PdfReader(fileCertificateTemplate);
            //Modify file using PdfReader
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("phoibang-printpreview.pdf"));
            // set Fonts
            String fontUrl = "fonts/unicode_nuptiasi.ttf";
            if (rbStyle1.isSelected()) {
                fontUrl = "fonts/unicode_nuptiasi.ttf";
            } else if (rbStyle2.isSelected()) {
                fontUrl = "fonts/conthuy.ttf";
            } else if (rbStyle3.isSelected()) {
                fontUrl = "fonts/times_new_roman_italic.ttf";
            } else {
                fontUrl = "fonts/kythuat.ttf";
            }
            BaseFont bf = BaseFont.createFont(fontUrl, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            // get Students selected
//            List<Student> studentListSelected = tvInformationPrint.getSelectionModel().getSelectedItems(); // version old
            List<Student> studentListSelected = findStudentsSelected(listStudents);

            System.out.println("Sinh viên được chọn để print preview: " + studentListSelected);

            // if have Student selected
            if (studentListSelected.size() > 0) {
                // number page start from 1
                for (int i = 1; i <= studentListSelected.size(); i++) {
                    PdfContentByte cb = pdfStamper.getOverContent(i);
                    Student StudentSelected = studentListSelected.get(i - 1); //i-1 because list start from 0

                    printInformation(StudentSelected, cb, bf);
                    System.out.println("Đã in prinpreview: " + studentListSelected.get(i - 1).toString());

                    // add new page with size same previous page
                    pdfStamper.insertPage(i + 1, pdfReader.getPageSize(1));
                    // add content to new page with content is content of previous page, add content start from coordinate (0,0) of new page
                    pdfStamper.getOverContent(i + 1).addTemplate(pdfStamper.getImportedPage(pdfReader, 1), 0, 0);
                }

                // delete page when initial PdfStamper (page no data, just only template)
                pdfStamper.getReader().selectPages("1-" + studentListSelected.size());
                pdfStamper.close();

                // open viewer to view
                openViewerFX = new OpenViewerFX(stage, null);
                openViewerFX.setupViewer();
                openViewerFX.openDefaultFile("phoibang-printpreview.pdf");
                openViewerFX.exitOnClose = false;
                stage.setMaximized(true);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Xem trước");
                alert.setHeaderText(null);
                alert.setContentText("Bạn chưa chọn người để xem trước!");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
                alert.showAndWait();
            }
        } else {
            System.out.println("Print preview method: Không có hàng nào");
        }
    }

    public void printCertificate(MouseEvent mouseEvent) throws Exception {
        List<Student> listNewStudents = FXCollections.observableArrayList();
        List<Student> listOldStudents = FXCollections.observableArrayList();
        List<Student> listStudentsUpdate = FXCollections.observableArrayList();
        List<Student> listStudentsDelete = FXCollections.observableArrayList();
        List<Student> listStudentsExcelHistory = FXCollections.observableArrayList();

        if (file.exists()){
            System.out.println("File chứa lịch sử đã tồn tại!");
        }else{
            System.out.println("File chứa lịch sử chưa tồn tại!");
            file.createNewFile();
        }

        if (tvInformationPrint.getItems().size() > 0) {
//            List<Student> studentListSelected = tvInformationPrint.getSelectionModel().getSelectedItems(); // version old
            List<Student> studentListSelected = findStudentsSelected(listStudents);

            System.out.println("Sinh viên được chọn: " + studentListSelected);

            // if have Student selected
            if (studentListSelected.size() > 0) {
                // create document with pagesize your custom
                Rectangle newPageSize = new Rectangle(820.8f, 597.6f); // width, height
//                Rectangle newPageSize = new Rectangle(499.68f, 360.72f); // width:6.94 in, height: 5.01
                Document document = new Document(newPageSize);

                // create object PdfWriter
                PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("certificate.pdf"));
                document.open();

                PdfContentByte cb = pdfWriter.getDirectContent();
                // set Fonts
                String fontUrl = "fonts/unicode_nuptiasi.ttf";
                if (rbStyle1.isSelected()) {
                    fontUrl = "fonts/unicode_nuptiasi.ttf";
                } else if (rbStyle2.isSelected()) {
                    fontUrl = "fonts/conthuy.ttf";
                } else if (rbStyle3.isSelected()) {
                    fontUrl = "fonts/times_new_roman_italic.ttf";
                } else {
                    fontUrl = "fonts/kythuat.ttf";
                }
                BaseFont bf = BaseFont.createFont(fontUrl, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

                for (int i = 0; i < studentListSelected.size(); i++) {
                    Student studentSelected = studentListSelected.get(i);

                    // print certificate and write excel
                    /********
                     * nếu sinh viên trước đây chưa từng in: in bằng, xóa khỏi danh sách, ghi vào file history
                     * nếu sinh viên trước đây đã từng in:
                     *      hiển thị thông báo:
                     *          nếu cập nhật: in bằng, xóa khỏi danh sách, cập nhật thông tin vào file history
                     *          nếu không cập nhật: không in bằng, không xóa khỏi danh sách, không cập nhật
                     * ********/
                    int indexRow = -1;
                    if(file.length() >0) indexRow = handlerExcelFile.getIndexRowStudentUpdate(studentSelected.getIdStudent(),file);
                    if (indexRow == -1){
                        // print certificate
                        document.newPage();
                        printInformation(studentSelected, cb, bf);
                        System.out.println("Đã in: " + studentListSelected.get(i).toString());
                        // là sinh viên mới chưa từng in bằng, không tìm thấy dữ liệu trong file history excel, lát ghi sau
                        listNewStudents.add(studentListSelected.get(i));
//                        listStudents.remove(studentSelected);
                        listStudentsDelete.add(studentSelected);
                    }else{
                        //// là sinh viên đã từng in bằng, đã lưu dữ liệu trong file history excel => cần cập nhật field NOTE, cần ghi ngay
                        listStudentsExcelHistory = handlerExcelFile.readExcel(file);
                        for(Student student: listStudentsExcelHistory){
                            if (student.getIdStudent().equals(studentSelected.getIdStudent())){

                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialog_student_exists.fxml"));
                                openDialog(fxmlLoader,student);
                                DialogStudentExistsController dialogStudentExistsController = fxmlLoader.<DialogStudentExistsController>getController();
                                if (dialogStudentExistsController.isCheckUpdate()) {
                                    // case update note and print
                                    listStudentsUpdate.add(student);
                                    listStudentsUpdate.get(listStudentsUpdate.size()-1).setNote(dialogStudentExistsController.getStudentDialog().getNote());
//                                    student.setNote(dialogStudentExistsController.getStudentDialog().getNote());
                                    // print certificate
                                    document.newPage();
                                    printInformation(studentSelected, cb, bf);
                                    System.out.println("Đã in: " + studentListSelected.get(i).toString());

//                                    listStudents.remove(studentSelected);
                                    listStudentsDelete.add(studentSelected);
                                    System.out.println("updated student exists");
                                } else {
                                    // case ignore, not print
                                    System.out.println("not update student exists");
                                }

//                                // cập nhật cột note trước khi ghi vào file
//                                listOldStudents.clear();
//                                listOldStudents.add(student);
//                                handlerExcelFile.writeExcel(file.getPath(), listOldStudents, indexRow, false);
                                break;
                            }
                        }
                    }
                    // delete Student printed and add student printed into listStudentsPrinted
//                    MainController.lisStudentsPrinted.add(studentSelected);
//                    listStudentsPrinted.add(studentSelected);
//                    listStudents.remove(studentSelected);

//                    System.out.println("Đã xóa: " + studentListSelected.get(i).toString());
                }
//                // ghi những sinh viên mới chưa từng in bằng trước đây
//                handlerExcelFile.writeExcel(file.getPath(), listNewStudents, handlerExcelFile.getIndexRowLast(file.getPath()) + 1, false);

//                document.close();
    //                System.out.println("Number page of document: " + document.getPageNumber());
    //                System.out.println("Number page of pdfWriter: " + pdfWriter.getPageNumber());
                if (!pdfWriter.isPageEmpty()) {
                    document.close();

                    // connect printer and print
                    ConnectPrinter connectPrinter = new ConnectPrinter();
                    boolean checkPrinted = connectPrinter.printPaper("certificate.pdf");
                    if (checkPrinted) {
                        listStudents.removeAll(listStudentsDelete);
                        // cập nhật những sinh viên đã từng in bằng
                        for (Student studentUpdate : listStudentsUpdate) {
                            int index = handlerExcelFile.getIndexRowStudentUpdate(studentUpdate.getIdStudent(), file);
                            handlerExcelFile.writeExcel(file, FXCollections.observableArrayList(studentUpdate), index, false);
                        }
                        // ghi những sinh viên mới chưa từng in bằng trước đây
                        if (file.length() > 0) {
                            handlerExcelFile.writeExcel(file, listNewStudents, handlerExcelFile.getIndexRowLast(file) + 1, false);
                        } else {
                            // trường hợp lần đầu ghi lịch sử
                            handlerExcelFile.writeExcel(file, listNewStudents, 1, true);
                        }
                        System.out.println("PRINT OK");
                    }
                }
                // load table after delete
                loadTablePrint(listStudents);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("In bằng");
                alert.setHeaderText(null);
                alert.setContentText("Bạn chưa chọn người để in bằng!");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
                alert.showAndWait();
            }
        } else {
            System.out.println("Print method: Không có hàng nào");
        }
    }

//    public void print(Node node){
//        Stage stage = (Stage) btPrint.getParent().getScene().getWindow();
//
//        // Create the PrinterJob
//        PrinterJob printerJob = PrinterJob.createPrinterJob();
//        if (printerJob == null) {
//            return;
//        }
//
//        // Show the print setup dialog
//        boolean proceed = printerJob.showPrintDialog(stage);
//        if (proceed) {
//            // Print the node
//            boolean printed = printerJob.printPage(node);
//            if (printed) {
//                printerJob.endJob();
//            }
//        }
//
//        System.out.println("Print method!");
//    }

    public void printInformation(Student studentSelected, PdfContentByte cb, BaseFont bf) {
        float xEnglish = 60.5f, yEnglish = 176.5f;
        float xVietnam = xEnglish + 244, yVietnam = yEnglish;
        float xCenterEnglish = 124.0f;
        cb.saveState();
        // english
        setTextTitle(cb, bf, majorEnglish(studentSelected.getMajor()), xCenterEnglish, yEnglish+32);
        setText(cb, bf,
                ((studentSelected.getSex().equalsIgnoreCase("nam"))?"Mr ":"Ms ")+
                nonUnicode(studentSelected.getName()), xEnglish, yEnglish);
        setText(cb, bf,
                getDateEnglish(studentSelected.getDateOfBirth())
                    + "   In " + nonUnicode(studentSelected.getHometown()), xEnglish + 22, yEnglish-15);
        setText(cb, bf, studentSelected.getGraduationYear() + "", xEnglish + 40, yEnglish-30);
        setText(cb, bf, rankEnglish(studentSelected.getRank()), xEnglish + 48, yEnglish-45);
        setText(cb, bf, modeOfStudyEnglish(studentSelected.getModeOfStudy()), xEnglish + 30, yEnglish-60);
        String date = studentSelected.getLicenseDate().getDayOfMonth()+ " " + studentSelected.getLicenseDate().getYear();
        setText(cb, bf, "Ha Noi, " + getDateEnglish(dateTimeFormatter.format(studentSelected.getLicenseDate())),
                xEnglish + 50, yEnglish-80);
        setText(cb,bf,studentSelected.getStrClass() + "/" +studentSelected.getRegisterNumber(), xEnglish+10,yEnglish-147);

        // vietnam
        setTextTitle(cb, bf, studentSelected.getMajor(), xCenterEnglish+252, yVietnam + 32);
        setText(cb, bf,
                ((studentSelected.getSex().equalsIgnoreCase("nam"))?"Ông ":"Bà ") +
                studentSelected.getName(), xVietnam, yVietnam);
        setText(cb, bf,
                getDateVietnam(studentSelected.getDateOfBirth())
                + "   Tại " + studentSelected.getHometown(), xVietnam+22, yVietnam-15);
        setText(cb, bf, studentSelected.getGraduationYear() + "", xVietnam + 40, yVietnam - 30);
        setText(cb, bf, studentSelected.getRank(), xVietnam + 48, yVietnam - 45);
        setText(cb, bf, studentSelected.getModeOfStudy(), xVietnam+44, yVietnam-60);
        setText(cb, bf, studentSelected.getLicenseDate().getDayOfMonth()+ "", xVietnam+86, yVietnam-80);
        setText(cb, bf, studentSelected.getLicenseDate().getMonthValue() + "", xVietnam+120, yVietnam-80);
        setText(cb, bf, studentSelected.getLicenseDate().getYear()+ "", xVietnam+144, yVietnam-80);
        setText(cb,bf,studentSelected.getStrClass() + "/" +studentSelected.getRegisterNumber(), xVietnam+50, yVietnam-147);
        cb.restoreState();
    }

    public void setText(PdfContentByte cb, BaseFont bf, String text, float x, float y) {
        cb.beginText();
        cb.moveText(x, y); // move text to x,y coordinate
        cb.setFontAndSize(bf, 13);
        cb.showText(text);
        cb.endText();
    }

    public void setTextTitle(PdfContentByte cb, BaseFont bf, String text, float x, float y) {
        cb.beginText();
//        cb.moveText(x, y); // move text to x,y coordinate
        cb.setFontAndSize(bf, 20);
//        cb.showText(text);
        cb.showTextAligned(1,text,x,y,0);
        cb.endText();
    }

    public List<Student> findStudentsSelected(List<Student> list){
        // find all student selected
        List<Student> listStudentsSelected = FXCollections.observableArrayList();
        for(int i=0;i<list.size();i++){
            if(list.get(i).isSelect()){
                listStudentsSelected.add(list.get(i));
            }
        }
        return listStudentsSelected;
    }

    public String getMonthEnglish(int month) {
        String[] strMonths = {"January","February","March","April","May","June","July",
                "August","September","October","November","December"};
        return strMonths[month-1];
    }

    public String getDateEnglish(String date){
        String[] arrDate = date.split("/");
        if(arrDate.length == 1)return arrDate[0];
        else{
            return arrDate[0] + " " + getMonthEnglish(Integer.parseInt(arrDate[1])) + " " + arrDate[2];
        }
    }

    public String getDateVietnam(String date){
        String[] arrDate = date.split("/");
        if(arrDate.length == 1)return arrDate[0];
        else{
            return arrDate[0] + "-" + arrDate[1]+ "-" + arrDate[2];
        }
    }

    public String majorEnglish(String majorVietnam) {
        for (Major major : listMajors) {
            if (major.getVietnam().equals(majorVietnam)) {
                return major.getEnglish();
            }
        }
        return "";
    }

    public String modeOfStudyEnglish(String modeOfStudyVietnam) {
        for (ModeOfStudy modeOfStudy : listModeOfStudies) {
            if (modeOfStudy.getVietnam().equals(modeOfStudyVietnam)) {
                return modeOfStudy.getEnglish();
            }
        }
        return "";
    }

    public String rankEnglish(String rankVietnam) {
        for (Rank rank : listRanks) {
            if (rank.getVietnam().equals(rankVietnam)) {
                return rank.getEnglish();
            }
        }
        return "";
    }

    public void openDialog(FXMLLoader fxmlLoader, Student student) throws IOException {
        Parent parent = fxmlLoader.load();
        DialogStudentExistsController dialogStudentExistsController = fxmlLoader.<DialogStudentExistsController>getController();
        dialogStudentExistsController.setStudentDialog(student);

        Scene scene = new Scene(parent, 600, 400);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
        stage.showAndWait();
    }

    public String nonUnicode(String str) {
        String strNonUnicode = str;
        String[] arrOrigin = new String[]{"á", "à", "ả", "ã", "ạ", "â", "ấ", "ầ", "ẩ", "ẫ", "ậ", "ă", "ắ", "ằ", "ẳ",
                "ẵ", "ặ", "đ", "é", "è", "ẻ", "ẽ", "ẹ", "ê", "ế", "ề", "ể", "ễ", "ệ", "í", "ì", "ỉ", "ĩ", "ị", "ó", "ò",
                "ỏ", "õ", "ọ", "ô", "ố", "ồ", "ổ", "ỗ", "ộ", "ơ", "ớ", "ờ", "ở", "ỡ", "ợ", "ú", "ù", "ủ", "ũ", "ụ", "ư",
                "ứ", "ừ", "ử", "ữ", "ự", "ý", "ỳ", "ỷ", "ỹ", "ỵ"};

        String[] arrReplace = new String[]{"a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a",
                "a", "a", "d", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "i", "i", "i", "i", "i", "o", "o",
                "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "u", "u", "u", "u", "u", "u",
                "u", "u", "u", "u", "u", "y", "y", "y", "y", "y"};

        for (int i = 0; i < arrOrigin.length; i++) {
            strNonUnicode = strNonUnicode.replace(arrOrigin[i], arrReplace[i]);
            strNonUnicode = strNonUnicode.replace(arrOrigin[i].toUpperCase(), arrReplace[i].toUpperCase());
        }
        return strNonUnicode;
    }

}
