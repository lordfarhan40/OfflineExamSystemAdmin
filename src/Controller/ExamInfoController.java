package Controller;

import Helper.FileWorker;
import Model.MainExam;
import Model.MasterAnswerSheet;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Farhan on 25-07-2015.
 */
public class ExamInfoController {
    @FXML
    Label examName, noOfQuestions, examTime, instructionAttached, passwordProtected, masterKeyAttached;

    private MainExam mainExam;
    private MasterAnswerSheet masterAnswerSheet;

    public void init(MainExam exam, boolean passwordProtected) {
        mainExam = exam;
        setLabel(examName, exam.getExamTitle());
        setLabel(noOfQuestions, "" + exam.size());
        setLabel(examTime, "" + exam.getTimeMinutes());
        if (mainExam.getExamInstructions().equals("0")) {
            setLabel(instructionAttached, "No");
        } else
            setLabel(instructionAttached, "Yes");
        if (passwordProtected) {
            setLabel(this.passwordProtected, "Yes");
        } else
            setLabel(this.passwordProtected, "No");
        initMasterAnswerSheet();
    }

    public void editQuestions(Event e) {
        try {
            Stage stage = getStage((Node) e.getSource());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../View/EditQuestion.fxml"));
            Parent parent = fxmlLoader.load();
            EditQuestionController editQuestionController = fxmlLoader.getController();
            editQuestionController.initialiseSetup(mainExam, masterAnswerSheet);
            stage.setScene(new Scene(parent));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void editExamInformation(Event event) {
        try {
            Stage stage = getStage((Node) event.getSource());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../View/QuestionnaireSave.fxml"));
            Parent parent = fxmlLoader.load();
            QuestionnaireSaveController questionnaireSaveController = fxmlLoader.getController();
            questionnaireSaveController.setInitialize(mainExam, masterAnswerSheet);
            stage.setScene(new Scene(parent));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickBack(Event event) {
        try {
            Stage stage = getStage((Node) event.getSource());
            Parent parent = FXMLLoader.load(getClass().getResource("../View/Welcome.fxml"));
            stage.setScene(new Scene(parent));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Stage getStage(Node node) {
        return (Stage) node.getScene().getWindow();
    }

    private void setLabel(Label label, String text) {
        label.setText(text);
    }

    @FXML
    public void showAbout(Event e) {
        AboutUsLoader aboutUsLoader = new AboutUsLoader();
        aboutUsLoader.showAboutUs();
    }

    private void initMasterAnswerSheet() {
        masterAnswerSheet = new MasterAnswerSheet();
        for (int i = 0; i < mainExam.size(); ++i) {
            masterAnswerSheet.setAnswer(i, 0);
        }
    }

    @FXML
    public void loadAnswerKey(Event e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Master Answer Key", "*.mak")
        );
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        File f = fileChooser.showOpenDialog(getStage());
        if (f == null)
            return;
        MasterAnswerSheet masterAnswerSheet = FileWorker.readMasterAnswerSheet(f);
        if (masterAnswerSheet.size() == mainExam.size()) {
            this.masterAnswerSheet = masterAnswerSheet;
            passwordProtected.setText("Set");
        }
        masterKeyAttached.setText("Yes");
    }

    private Stage getStage() {
        return (Stage) noOfQuestions.getScene().getWindow();
    }
}
