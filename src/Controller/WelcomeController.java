package Controller;

import Helper.FileWorker;
import Model.MainExam;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Farhan on 16-07-2015.
 */
public class WelcomeController {
    @FXML
    Button newExam, loadExam;

    MainExam mainExam;

    private boolean passwordProtected;

    @FXML
    private void newExam() {
        try {
            Stage stage = (Stage) newExam.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("EditQuestion.fxml"));
            Parent parent = fxmlLoader.load();
            stage.setScene(new Scene(parent));
        } catch (Exception e) {
            System.out.print("Error while calling EditQuestion");
            e.printStackTrace();
        }
    }

    @FXML
    private void loadExam(Event e) {
        FileChooser fileChooser = new FileChooser();
        MainExam.examFileChooserSettings(fileChooser);
        File file = fileChooser.showOpenDialog(getStage((Node) e.getSource()));
        if (file == null)
            return;
        if (FileWorker.readFirst(file).equals(FileWorker.PASSWORD_PROTECTED)) {
            passwordProtected = true;
            EnterPasswordWindow enterPasswordWindow = new EnterPasswordWindow(file);
            mainExam = enterPasswordWindow.showWindow();
            if (mainExam != null)
                System.out.print(mainExam.toString());
            else {
                //add an alert box here
                System.out.print("Shadowness is angry");
                return;
            }
        } else {
            passwordProtected = false;
            mainExam = FileWorker.readFromFile(file, null);
        }
        setInformationPage(e);
    }

    private Stage getStage(Node node) {
        return (Stage) node.getScene().getWindow();
    }

    public void setInformationPage(Event e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ExamInfo.fxml"));
            Parent parent = fxmlLoader.load();
            ExamInfoController examInfoController = fxmlLoader.getController();
            examInfoController.init(mainExam, passwordProtected);
            Stage stage = getStage((Node) e.getSource());
            stage.setScene(new Scene(parent));
        } catch (Exception exception) {
            System.out.println("Exam info window fucked up");
            exception.printStackTrace();
        }
    }

    @FXML
    public void evaluateExam(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("EvaluateAnswer.fxml"));
            Stage stage = getStage((Node) event.getSource());
            stage.setScene(new Scene(parent));
        } catch (Exception exception) {

        }
    }

    @FXML
    public void showAbout(Event e) {
        AboutUsLoader aboutUsLoader = new AboutUsLoader();
        aboutUsLoader.showAboutUs();
    }

}
