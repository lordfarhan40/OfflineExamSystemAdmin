package Controller;

import Helper.FileWorker;
import Model.MainExam;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Farhan on 25-07-2015.
 */
public class EnterPasswordWindow {
    private File file;
    private MainExam mainExam;

    public EnterPasswordWindow(File f) {
        file = f;
        mainExam = null;
    }

    public MainExam showWindow() {
        Stage stage = new Stage();
        stage.setTitle("Password Protected Document");
        stage.initModality(Modality.APPLICATION_MODAL);
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(15));
        Label label = new Label("Enter the password");
        HBox hBox = new HBox();
        PasswordField passwordField = new PasswordField();
        Button button = new Button("Ok");
        hBox.getChildren().addAll(passwordField, button);
        vBox.getChildren().addAll(label, hBox);
        stage.setScene(new Scene(vBox));

        button.setOnAction(event -> {

            if ((mainExam = readAndValidate(file, passwordField.getText())) == null) ;
            {
                stage.close();
            }
        });
        stage.showAndWait();
        return mainExam;
    }

    public MainExam readAndValidate(File file, String password) {
        try {
            mainExam = FileWorker.readFromFile(file, password);
            return mainExam;
        } catch (Exception e) {
            return null;
        }
    }


}
