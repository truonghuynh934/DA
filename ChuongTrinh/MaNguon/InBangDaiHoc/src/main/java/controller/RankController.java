package controller;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import database.RankDAO;
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
import model.Rank;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RankController implements Initializable {
    @FXML
    private Label lbTitleRank;
    @FXML
    private TableColumn<Rank, Button> tcDelete;
    @FXML
    private TableColumn<Rank, Integer> tcId;
    @FXML
    private TableColumn<Rank, String> tcIdRank;
    @FXML
    private TableColumn<Rank, String> tcVietnam;
    @FXML
    private TableColumn<Rank, String> tcEnglish;
    @FXML
    private TableView<Rank> tvRank;
    @FXML
    private TextField tfIdRank;
    @FXML
    private TextField tfVietnam;
    @FXML
    private TextField tfEnglish;
    @FXML
    private Button btAddNewRank;
    @FXML
    private Button btUpdateRank;
    @FXML
    private Button btRefresh;

    List<Rank> listRanks = FXCollections.observableArrayList();
    RankDAO rankDAO = new RankDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btAddNewRank.setGraphic(new ImageView("/images/add.png"));
        btUpdateRank.setGraphic(new ImageView("/images/update.png"));
        btRefresh.setGraphic(new ImageView("/images/refresh.png"));
        tvRank.setPlaceholder(new Label("Không có dữ liệu"));
        // configure table column and table row
        tcDelete.setCellFactory(new Callback<TableColumn<Rank, Button>, TableCell<Rank, Button>>() {
            @Override
            public TableCell<Rank, Button> call(TableColumn<Rank, Button> param) {
                final TableCell<Rank, Button> cell = new TableCell<Rank, Button>() {
                    Button btDelete = new Button("Xóa");
//                    Button btDelete = new Button("",new ImageView("images/delete.png"));

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
        tcId.setCellFactory(new Callback<TableColumn<Rank, Integer>, TableCell<Rank, Integer>>() {
            @Override
            public TableCell<Rank, Integer> call(TableColumn<Rank, Integer> param) {
                return new TableCell<Rank, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && (this.getTableRow().getIndex() < tvRank.getItems().size())) {
                            setText((this.getTableRow().getIndex() + 1) + "");
//                            System.out.println(this.getTableRow().getIndex()+1);
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
        tcIdRank.setCellValueFactory(new PropertyValueFactory<Rank, String>("idRank"));
        tcVietnam.setCellValueFactory(new PropertyValueFactory<Rank, String>("vietnam"));
        tcEnglish.setCellValueFactory(new PropertyValueFactory<Rank, String>("english"));

        loadTableView();

        tvRank.setRowFactory(new Callback<TableView<Rank>, TableRow<Rank>>() {
            @Override
            public TableRow<Rank> call(TableView<Rank> param) {
                TableRow<Rank> rowSelected = new TableRow<>();

                rowSelected.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() == 1 && !rowSelected.isEmpty()) {
                            Rank rank = rowSelected.getItem();
                            tfIdRank.setText(rank.getIdRank());
                            tfVietnam.setText(rank.getVietnam());
                            tfEnglish.setText(rank.getEnglish());

                            tfIdRank.setDisable(true);
                            btAddNewRank.setDisable(true);
                            btUpdateRank.setDisable(false);
                        }
                    }
                });

                return rowSelected;
            }
        });

        // configure button
        btUpdateRank.setDisable(true);
//        btAddRank.setGraphic(new ImageView("table_editable_project/images/icon.png"));
    }

    public void deleteRow(int indexRow) {
        for (int i = 0; i < listRanks.size(); i++) {
            if (i == indexRow) {
                // delete row
                Rank RankDelete = listRanks.get(i);
                listRanks.remove(RankDelete);
                rankDAO.writeUpdateJsonFile(listRanks);

                tfIdRank.clear();
                tfVietnam.clear();
                tfEnglish.clear();
                tfIdRank.setDisable(false);
                btAddNewRank.setDisable(false);
                btUpdateRank.setDisable(true);

                System.out.println("Đã delete");
                break;
            }
        }
        System.out.println("Delete method");
    }

    public void addNewRank(MouseEvent mouseEvent) {
        if (tfIdRank.getText().isEmpty() || tfVietnam.getText().isEmpty() || tfEnglish.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thêm xếp loại tốt nghiệp");
            alert.setHeaderText(null);
            alert.setContentText("Bạn chưa điền đủ thông tin!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
            alert.showAndWait();
            System.out.println("chưa điền đủ thông tin");
        } else {
            // insert
            Rank newRank = new Rank(tfIdRank.getText(), tfVietnam.getText(), tfEnglish.getText());
            listRanks.add(newRank);

            rankDAO.writeUpdateJsonFile(listRanks);

            tfIdRank.clear();
            tfVietnam.clear();
            tfEnglish.clear();

            System.out.println("Đã add");
        }
        System.out.println("Add new rank method");
    }

    public void updateRank(MouseEvent mouseEvent) {
        for (int i = 0; i < listRanks.size(); i++) {
            System.out.println("index: " + i);
            if (listRanks.get(i).getIdRank().equals(tfIdRank.getText())) {
                if (tfVietnam.getText().isEmpty() || tfEnglish.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thêm xếp loại tốt nghiệp");
                    alert.setHeaderText(null);
                    alert.setContentText("Bạn chưa điền đủ thông tin!");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
                    alert.showAndWait();
                }else {
                    // update
                    listRanks.get(i).setVietnam(tfVietnam.getText());
                    listRanks.get(i).setEnglish(tfEnglish.getText());

                    rankDAO.writeUpdateJsonFile(listRanks);
                    loadTableView(); // load tabelview again to update data

                    System.out.println("Đã update");
                }
                break;
            }
        }
        System.out.println("Update rank method");
    }

    public void refresh(MouseEvent mouseEvent) {
        tfIdRank.clear();
        tfVietnam.clear();
        tfEnglish.clear();

        tfIdRank.setDisable(false);
        btAddNewRank.setDisable(false);
        btUpdateRank.setDisable(true);
        loadTableView();

        System.out.println("Refresh method");
    }

    public void loadTableView(){
        listRanks = rankDAO.readJsonFile();
        tvRank.setItems((ObservableList<Rank>) listRanks);
    }


}
