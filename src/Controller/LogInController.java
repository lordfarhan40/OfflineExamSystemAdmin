package Controller;

import Helper.EncryptDecrypter;
import Helper.FileWorker;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;

/**
 * Created by Farhan on 13-08-2015.
 */
public class LogInController {
    @FXML
    PasswordField passwordField, passwordConfirmField;

    @FXML
    TextField textFieldName;

    @FXML
    Label welcomeText, labelEnterName, labelPassword, labelConfirmPassword, labelWarning;

    @FXML
    Button button;

    private String userName, password;

    private final File file = new File("init.txt");

    public void initialize() {
        System.out.print("hi");
        if (readFile()) {
            setUpForLogin();
        }
    }

    public boolean readFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(fileInputStream));
            userName = bufferedInputStream.readLine();
            password = bufferedInputStream.readLine();
            bufferedInputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @FXML
    public void onSaveNew(Event event) {
        Button button = (Button) event.getSource();
        if (button.getText().equals("Login")) {
            onCheckDetails();
        } else {
            onSaveDetails();
        }
    }

    public boolean isTextEmpty(String string) {
        if (string.equals("")) {
            return true;
        } else
            return false;
    }

    public void setUpForLogin() {
        welcomeText.setText("Welcome, " + userName);
        button.setText("Login");
        passwordConfirmField.setOpacity(0);
        labelConfirmPassword.setOpacity(0);
        labelEnterName.setOpacity(0);
        textFieldName.setOpacity(0);

    }

    public void onCheckDetails() {
        readFile();
        password = EncryptDecrypter.decrypt(password, userName);
        if (password.equals(passwordField.getText())) {
            loadWelcomeScreen();
        } else {
            labelWarning.setOpacity(1);
            labelWarning.setText("Password Incorrect");
        }
    }

    public void onSaveDetails() {
        if (isTextEmpty(textFieldName.getText()) || isTextEmpty(passwordConfirmField.getText()) || isTextEmpty(passwordField.getText())) {
            labelWarning.setOpacity(1);
            labelWarning.setText("No field can be left EMPTY");
            return;
        }
        if (passwordField.getText().equals(passwordConfirmField.getText())) {
            userName = textFieldName.getText();
            password = passwordField.getText();
            password = EncryptDecrypter.encrypt(password, userName);
            FileWorker.writeFile(file, userName + "\n" + password);
            loadWelcomeScreen();
        } else {
            labelWarning.setOpacity(1);
            labelWarning.setText("Both Password Don't Match. Please check again.");
        }
    }

    private void loadWelcomeScreen() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("Welcome.fxml"));
            getStage().setScene(new Scene(parent));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private Stage getStage() {
        return (Stage) button.getScene().getWindow();
    }
}
