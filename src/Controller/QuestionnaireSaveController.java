package Controller;

import Helper.FileWorker;
import Model.MainExam;
import Model.MasterAnswerSheet;
import Model.QuestionBank;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Created by Farhan on 20-07-2015.
 */
public class QuestionnaireSaveController {
    @FXML
    TextField password, timeMinutes, examTitle;

    @FXML
    CheckBox checkBoxPassword, checkBoxFile;

    @FXML
    Label passwordLabel, fileWarning, timeInvalidWarning, titleWarning, passwordWarning;

    @FXML
    Button fileButton, doneButton;

    private StringProperty stringProperty;
    private File file, fileSave;
    private MainExam mainExam;
    private MasterAnswerSheet masterAnswerSheet;

    public void initialize() {
        //Getting up textProperty from timeMinutes
        stringProperty = timeMinutes.textProperty();
        //Setting up Listener to String property
        setStringListener(stringProperty);
        file = null;
        mainExam = new MainExam();
    }

    public void setInitialize(QuestionBank questionBank, MasterAnswerSheet masterAnswerSheet) {
        this.masterAnswerSheet = masterAnswerSheet;
        if (questionBank.getClass().equals(MainExam.class)) {
            setMainExam((MainExam) questionBank);
        } else {
            this.mainExam = new MainExam(questionBank);
        }

    }

    public void setMainExam(MainExam mainExam) {
        this.mainExam = mainExam;
        updateScreen();
    }

    //Opening up Instruction Files
    @FXML
    private void chooseFile(Event e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text File", "*.txt")
        );
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.setTitle("Select an instruction File");
        file = fileChooser.showOpenDialog(getStage((Node) e.getSource()));
        if (file == null) {
            //If no file selected
            return;
        }
        String fileName = file.getName();
        if (!fileName.contains(".txt")) {
            //If TEXT file not selected
            fileButton.setText("Choose .txt File");
            fileWarning.setOpacity(1.0);
            file = null;
        } else {
            fileButton.setText("Change Instruction File");
        }
    }

    //Setting up stuff to make sure we can go back properly
    @FXML
    public void previousStep(Event event) {
        try {
            //updateMainExam();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("EditQuestion.fxml"));
            Parent parent = loader.load();
            EditQuestionController controller = loader.getController();
            controller.initialiseSetup(mainExam, masterAnswerSheet);
            Stage stage = getStage((Node) event.getSource());
            stage.setScene(new Scene(parent));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Called if CheckBoxChangedFile is changed
    @FXML
    public void checkBoxChangedFile() {
        if (checkBoxFile.isSelected()) {
            fileButton.setDisable(false);
        } else {
            file = null;
            fileButton.setText("Choose .txt File");
            fileWarning.setOpacity(0);
            fileButton.setDisable(true);
        }
    }

    //Called if checkBoxPassword is changed
    @FXML
    public void checkBoxChangedPassword() {
        if (checkBoxPassword.isSelected()) {
            passwordLabel.setDisable(false);
            password.setDisable(false);
        } else {
            passwordLabel.setDisable(true);
            password.setDisable(true);
        }
    }

    //Called when save button Clicked
    @FXML
    public void saveExam(Event e) {
        //This is going to be a very long function
        //Step by step checking all the details
        //Setting all warnings to non-visible
        titleWarning.setOpacity(0);
        timeInvalidWarning.setOpacity(0);
        passwordWarning.setOpacity(0);
        fileWarning.setOpacity(0);

        //Check Title of Exam
        if (examTitle.getText().equals("")) {
            titleWarning.setOpacity(1);
            return;
        }

        //Check whether time provided or not
        if (timeMinutes.getText().equals("")) {
            timeInvalidWarning.setText("Cannot be Empty");
            timeInvalidWarning.setOpacity(1);
            return;
        }
        //Check if FileCheckBox selected? if yes then see if file selected or not
        if (checkBoxFile.isSelected()) {
            if (file == null) {
                fileWarning.setText("Please select a file");
                fileWarning.setOpacity(1);
                return;
            }
        }

        updateMainExam();

        //Last and Final step
        //Check if Password Selected
        if (checkBoxPassword.isSelected()) {
            //If not set then ask the user to set a password and matches minimum length
            if (password.getText().equals("") || password.getText().length() < 8) {
                passwordWarning.setOpacity(1);
                return;
            }
            //Save Encrypted File
            fileSaver(e);
            if (fileSave == null)
                return;
            if (saveMasterAnswerSheet(e) == -1)
                return;
            FileWorker.writeToFile(mainExam, fileSave, password.getText());
            Stage stage = getStage(fileButton);
            stage.setTitle("Exam File and Answer Key Saved");
            doneButton.setDisable(false);
            return;
        } else {
            //Save file normally
            fileSaver(e);
            if (fileSave == null)
                return;
            if (saveMasterAnswerSheet(e) == -1)
                return;
            FileWorker.writeToFile(mainExam, fileSave, null);
            Stage stage = getStage(fileButton);
            stage.setTitle("Exam File and Answer Key Saved");
            doneButton.setDisable(false);
        }

    }

    //Open up a SaveDialogBox and save the location to fileSave
    private void fileSaver(Event e) {
        FileChooser chooser = new FileChooser();
        MainExam.examFileSaveSettings(chooser);

        fileSave = chooser.showSaveDialog(getStage((Node) e.getSource()));
    }

    //Returns Stage from the Node
    private Stage getStage(Node node) {
        return (Stage) node.getScene().getWindow();
    }

    //Setting up a listener on TimeTextBox to make sure that only number are entered
    public void setStringListener(StringProperty stringProperty) {
        stringProperty.addListener((observable, oldValue, newValue) -> {
            try {
                Integer.parseInt(newValue);//Will send an exception if not a String
                timeInvalidWarning.setOpacity(0);
            } catch (Exception e) {
                timeMinutes.setText("");
                timeInvalidWarning.setText("Not a Valid Input!");
                timeInvalidWarning.setOpacity(1);
            }
        });
    }

    public void updateScreen() {
        if (mainExam.getExamTitle() != null)
            examTitle.setText(mainExam.getExamTitle());
        if (mainExam.getTimeMinutes() != 0)
            timeMinutes.setText("" + mainExam.getTimeMinutes());
        if (!mainExam.getExamInstructions().equals("0"))
            fileButton.setText("Change Instruction File");
    }

    //update MainExam with current values
    public void updateMainExam() {
        //update title in mainExam
        if (examTitle.getText() != "") {
            mainExam.setExamTitle(examTitle.getText());
        }

        //update instructions
        if (checkBoxFile.isSelected()) {
            if (file != null) {
                mainExam.setExamInstructions(FileWorker.readText(file));
                return;
            }
        } else {
            //Setting 0 if FileCheckBox not selected
            mainExam.setExamInstructions("0");
        }

        //update Time in exam
        if (!timeMinutes.getText().equals("")) {
            mainExam.setTimeMinutes(Integer.parseInt(timeMinutes.getText()));
        }

    }

    private int saveMasterAnswerSheet(Event e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Master Answer Key");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Master Answer Key", "*.mak")
        );
        File f = fileChooser.showSaveDialog(getStage((Node) e.getSource()));
        if (f == null)
            return -1;
        FileWorker.writeMasterAnswerSheet(masterAnswerSheet, f);
        return 1;
    }

    @FXML
    public void showAbout(Event e) {
        AboutUsLoader aboutUsLoader = new AboutUsLoader();
        aboutUsLoader.showAboutUs();
    }

    @FXML
    public void doneSaveExam(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("Welcome.fxml"));
            getStage((Node) event.getSource()).setScene(new Scene(parent));
        } catch (Exception exception) {

        }
    }
}
