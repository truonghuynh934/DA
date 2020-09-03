package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = loadFXML("/main.fxml").load();

        // add logo
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));

        primaryStage.setTitle("In bằng tốt nghiệp đại học - UTC2");
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();

        // disable maximize button
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        // disable resize stage
        primaryStage.setResizable(false);
    }

    public FXMLLoader loadFXML(String url) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(url));
        return loader;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
