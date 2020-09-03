package controller;

import database.MajorDAO;
import database.ModeOfStudyDAO;
import database.RankDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sample.Main;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainController{
    @FXML
    private BorderPane bpMainView;
    @FXML
    private MenuItem exit;

    // list students print = (list students import excel and assign information) + (list students manual data entry)
    public static List<Student> listStudentsPrint = FXCollections.observableArrayList();
//    public static List<Student> lisStudentsPrinted = FXCollections.observableArrayList();

    public void changeLayout(ActionEvent actionEvent) throws IOException {
        String menuItemID = ((MenuItem) actionEvent.getSource()).getId();

        FXMLLoader loader = new FXMLLoader(getClass().getResource( "/"+menuItemID+".fxml"));
        loader.setController(loader.getController());
        bpMainView.setCenter((Node) loader.load());
    }

    public void closeApp(ActionEvent actionEvent){
        Platform.exit();
    }

}
