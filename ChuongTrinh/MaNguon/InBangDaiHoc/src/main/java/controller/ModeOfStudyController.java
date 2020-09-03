package controller;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import database.ModeOfStudyDAO;
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
import model.ModeOfStudy;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ModeOfStudyController implements Initializable {
    @FXML
    private Label lbTitleModeOfStudy;
    @FXML
    private TableColumn<ModeOfStudy, Button> tcDelete;
    @FXML
    private TableColumn<ModeOfStudy, Integer> tcId;
    @FXML
    private TableColumn<ModeOfStudy, String> tcIdModeOfStudy;
    @FXML
    private TableColumn<ModeOfStudy, String> tcVietnam;
    @FXML
    private TableColumn<ModeOfStudy, String> tcEnglish;
    @FXML
    private TableView<ModeOfStudy> tvModeOfStudy;
    @FXML
    private TextField tfIdModeOfStudy;
    @FXML
    private TextField tfVietnam;
    @FXML
    private TextField tfEnglish;
    @FXML
    private Button btAddNewModeOfStudy;
    @FXML
    private Button btUpdateModeOfStudy;
    @FXML
    private Button btRefresh;

    List<ModeOfStudy> listModeOfStudies = FXCollections.observableArrayList();
    ModeOfStudyDAO modeOfStudyDAO = new ModeOfStudyDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btAddNewModeOfStudy.setGraphic(new ImageView("/images/add.png"));
        btUpdateModeOfStudy.setGraphic(new ImageView("/images/update.png"));
        btRefresh.setGraphic(new ImageView("/images/refresh.png"));
        tvModeOfStudy.setPlaceholder(new Label("Không có dữ liệu"));
        // configure table column and table row
        tcDelete.setCellFactory(new Callback<TableColumn<ModeOfStudy, Button>, TableCell<ModeOfStudy, Button>>() {
            @Override
            public TableCell<ModeOfStudy, Button> call(TableColumn<ModeOfStudy, Button> param) {
                final TableCell<ModeOfStudy, Button> cell = new TableCell<ModeOfStudy, Button>() {
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
        tcId.setCellFactory(new Callback<TableColumn<ModeOfStudy, Integer>, TableCell<ModeOfStudy, Integer>>() {
            @Override
            public TableCell<ModeOfStudy, Integer> call(TableColumn<ModeOfStudy, Integer> param) {
                return new TableCell<ModeOfStudy, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && (this.getTableRow().getIndex() < tvModeOfStudy.getItems().size())) {
                            setText((this.getTableRow().getIndex() + 1) + "");
//                            System.out.println(this.getTableRow().getIndex()+1);
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
        tcIdModeOfStudy.setCellValueFactory(new PropertyValueFactory<ModeOfStudy, String>("idModeOfStudy"));
        tcVietnam.setCellValueFactory(new PropertyValueFactory<ModeOfStudy, String>("vietnam"));
        tcEnglish.setCellValueFactory(new PropertyValueFactory<ModeOfStudy, String>("english"));

        loadTableView();

        tvModeOfStudy.setRowFactory(new Callback<TableView<ModeOfStudy>, TableRow<ModeOfStudy>>() {
            @Override
            public TableRow<ModeOfStudy> call(TableView<ModeOfStudy> param) {
                TableRow<ModeOfStudy> rowSelected = new TableRow<>();

                rowSelected.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() == 1 && !rowSelected.isEmpty()) {
                            ModeOfStudy modeOfStudy = rowSelected.getItem();
                            tfIdModeOfStudy.setText(modeOfStudy.getIdModeOfStudy());
                            tfVietnam.setText(modeOfStudy.getVietnam());
                            tfEnglish.setText(modeOfStudy.getEnglish());

                            tfIdModeOfStudy.setDisable(true);
                            btAddNewModeOfStudy.setDisable(true);
                            btUpdateModeOfStudy.setDisable(false);
                        }
                    }
                });

                return rowSelected;
            }
        });

        // configure button
        btUpdateModeOfStudy.setDisable(true);
//        btAddModeOfStudy.setGraphic(new ImageView("table_editable_project/images/icon.png"));
    }

    public void deleteRow(int indexRow) {
        for (int i = 0; i < listModeOfStudies.size(); i++) {
            if (i == indexRow) {
                // delete row
                ModeOfStudy modeOfStudyDelete = listModeOfStudies.get(i);
                listModeOfStudies.remove(modeOfStudyDelete);
                modeOfStudyDAO.writeUpdateJsonFile(listModeOfStudies);

                tfIdModeOfStudy.clear();
                tfVietnam.clear();
                tfEnglish.clear();
                tfIdModeOfStudy.setDisable(false);
                btAddNewModeOfStudy.setDisable(false);
                btUpdateModeOfStudy.setDisable(true);

                System.out.println("Đã delete");
                break;
            }
        }
        System.out.println("Delete method");
    }

    public void addNewModeOfStudy(MouseEvent mouseEvent) {
        if (tfIdModeOfStudy.getText().isEmpty() || tfVietnam.getText().isEmpty() || tfEnglish.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thêm hình thức đào tạo");
            alert.setHeaderText(null);
            alert.setContentText("Bạn chưa điền đủ thông tin!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
            alert.showAndWait();
            System.out.println("chưa điền đủ thông tin");
        } else {
            // insert
            ModeOfStudy newModeOfStudy = new ModeOfStudy(tfIdModeOfStudy.getText(), tfVietnam.getText(), tfEnglish.getText());
            listModeOfStudies.add(newModeOfStudy);

            modeOfStudyDAO.writeUpdateJsonFile(listModeOfStudies);

            tfIdModeOfStudy.clear();
            tfVietnam.clear();
            tfEnglish.clear();

            System.out.println("Đã add");
        }
        System.out.println("Add new mode of study method");
    }

    public void updateModeOfStudy(MouseEvent mouseEvent) {
        for (int i = 0; i < listModeOfStudies.size(); i++) {
            System.out.println("index: " + i);
            if (listModeOfStudies.get(i).getIdModeOfStudy().equals(tfIdModeOfStudy.getText())) {
                if (tfVietnam.getText().isEmpty() || tfEnglish.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thêm hình thức đào tạo");
                    alert.setHeaderText(null);
                    alert.setContentText("Bạn chưa điền đủ thông tin!");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
                    alert.showAndWait();
                }else {
                    // update
                    listModeOfStudies.get(i).setVietnam(tfVietnam.getText());
                    listModeOfStudies.get(i).setEnglish(tfEnglish.getText());

                    modeOfStudyDAO.writeUpdateJsonFile(listModeOfStudies);
                    loadTableView(); // load tabelview again to update data

                    System.out.println("Đã update");
                }
                break;
            }
        }
        System.out.println("Update mode of study method");
    }

    public void refresh(MouseEvent mouseEvent) {
        tfIdModeOfStudy.clear();
        tfVietnam.clear();
        tfEnglish.clear();

        tfIdModeOfStudy.setDisable(false);
        btAddNewModeOfStudy.setDisable(false);
        btUpdateModeOfStudy.setDisable(true);
        loadTableView();

        System.out.println("Refresh method");
    }

    public void loadTableView(){
        listModeOfStudies = modeOfStudyDAO.readJsonFile();
        tvModeOfStudy.setItems((ObservableList<ModeOfStudy>) listModeOfStudies);
    }


}
