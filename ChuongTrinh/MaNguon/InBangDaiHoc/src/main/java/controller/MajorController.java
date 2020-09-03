package controller;

import database.MajorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import model.Major;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MajorController implements Initializable {
    @FXML
    private Label lbTitleMajor;
    @FXML
    private TableColumn<Major, Button> tcDelete;
    @FXML
    private TableColumn<Major, Integer> tcId;
    @FXML
    private TableColumn<Major, String> tcIdMajor;
    @FXML
    private TableColumn<Major, String> tcVietnam;
    @FXML
    private TableColumn<Major, String> tcEnglish;
    @FXML
    private TableView<Major> tvMajor;
    @FXML
    private TextField tfIdMajor;
    @FXML
    private TextField tfVietnam;
    @FXML
    private TextField tfEnglish;
    @FXML
    private Button btAddNewMajor;
    @FXML
    private Button btUpdateMajor;
    @FXML
    private Button btRefresh;

    List<Major> listMajors = FXCollections.observableArrayList();
    MajorDAO majorDAO = new MajorDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btAddNewMajor.setGraphic(new ImageView("/images/add.png"));
        btUpdateMajor.setGraphic(new ImageView("/images/update.png"));
        btRefresh.setGraphic(new ImageView("/images/refresh.png"));
        tvMajor.setPlaceholder(new Label("Không có dữ liệu"));
        // configure table column and table row
        tcDelete.setCellFactory(new Callback<TableColumn<Major, Button>, TableCell<Major, Button>>() {
            @Override
            public TableCell<Major, Button> call(TableColumn<Major, Button> param) {
                final TableCell<Major, Button> cell = new TableCell<Major, Button>() {
                    Button btDelete = new Button("Xóa");

                    @Override
                    protected void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!this.isEmpty()) {
                            setGraphic(btDelete);
                            btDelete.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    System.out.println("Index row: " + getTableRow().getIndex());
                                    deleteRow(getTableRow().getIndex());
                                }
                            });
                        } else {
                            setGraphic(null);
                        }
                    }
                };
                return cell;
            }
        });
        tcId.setCellFactory(new Callback<TableColumn<Major, Integer>, TableCell<Major, Integer>>() {
            @Override
            public TableCell<Major, Integer> call(TableColumn<Major, Integer> param) {
                return new TableCell<Major, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && (this.getTableRow().getIndex() < tvMajor.getItems().size())) {
                            setText((this.getTableRow().getIndex() + 1) + "");
//                            System.out.println(this.getTableRow().getIndex()+1);
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
        tcIdMajor.setCellValueFactory(new PropertyValueFactory<Major, String>("idMajor"));
        tcVietnam.setCellValueFactory(new PropertyValueFactory<Major, String>("vietnam"));
        tcEnglish.setCellValueFactory(new PropertyValueFactory<Major, String>("english"));

        loadTableView();

        tvMajor.setRowFactory(new Callback<TableView<Major>, TableRow<Major>>() {
            @Override
            public TableRow<Major> call(TableView<Major> param) {
                TableRow<Major> rowSelected = new TableRow<>();

                rowSelected.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() == 1 && !rowSelected.isEmpty()) {
                            Major Major = rowSelected.getItem();
                            tfIdMajor.setText(Major.getIdMajor());
                            tfVietnam.setText(Major.getVietnam());
                            tfEnglish.setText(Major.getEnglish());

                            tfIdMajor.setDisable(true);
                            btAddNewMajor.setDisable(true);
                            btUpdateMajor.setDisable(false);
                        }
                    }
                });

                return rowSelected;
            }
        });

        // configure button
        btUpdateMajor.setDisable(true);
//        btAddMajor.setGraphic(new ImageView("table_editable_project/images/icon.png"));
    }

    public void deleteRow(int indexRow) {
        for (int i = 0; i < listMajors.size(); i++) {
            if (i == indexRow) {
                // delete row
                Major MajorDelete = listMajors.get(i);
                listMajors.remove(MajorDelete);
                majorDAO.writeUpdateJsonFile(listMajors);

                tfIdMajor.clear();
                tfVietnam.clear();
                tfEnglish.clear();
                tfIdMajor.setDisable(false);
                btAddNewMajor.setDisable(false);
                btUpdateMajor.setDisable(true);

                System.out.println("Đã delete");
                break;
            }
        }
        System.out.println("Delete method");
    }

    public void addNewMajor(MouseEvent mouseEvent) {
        if (tfIdMajor.getText().isEmpty() || tfVietnam.getText().isEmpty() || tfEnglish.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thêm ngành đào tạo");
            alert.setHeaderText(null);
            alert.setContentText("Bạn chưa điền đủ thông tin!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
            alert.showAndWait();
            System.out.println("chưa điền đủ thông tin");
        } else {
            // insert
            Major newMajor = new Major(tfIdMajor.getText(), tfVietnam.getText(), tfEnglish.getText());
            listMajors.add(newMajor);

            majorDAO.writeUpdateJsonFile(listMajors);

            tfIdMajor.clear();
            tfVietnam.clear();
            tfEnglish.clear();

            System.out.println("Đã add");
        }
        System.out.println("Add new major method");
    }

    public void updateMajor(MouseEvent mouseEvent) {
        for (int i = 0; i < listMajors.size(); i++) {
            System.out.println("index: " + i);
            if (listMajors.get(i).getIdMajor().equals(tfIdMajor.getText())) {
                if (tfVietnam.getText().isEmpty() || tfEnglish.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thêm ngành đào tạo");
                    alert.setHeaderText(null);
                    alert.setContentText("Bạn chưa điền đủ thông tin!");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
                    alert.showAndWait();
                }else {
                    // update
                    listMajors.get(i).setVietnam(tfVietnam.getText());
                    listMajors.get(i).setEnglish(tfEnglish.getText());

                    majorDAO.writeUpdateJsonFile(listMajors);
                    loadTableView(); // load tabelview again to update data

                    System.out.println("Đã update");
                }
                break;
            }
        }
        System.out.println("Update major method");
    }

    public void refresh(MouseEvent mouseEvent) {
        tfIdMajor.clear();
        tfVietnam.clear();
        tfEnglish.clear();

        tfIdMajor.setDisable(false);
        btAddNewMajor.setDisable(false);
        btUpdateMajor.setDisable(true);
        loadTableView();

        System.out.println("Refresh method");
    }

    public void loadTableView(){
        listMajors = majorDAO.readJsonFile();
        tvMajor.setItems((ObservableList<Major>) listMajors);
    }


}
