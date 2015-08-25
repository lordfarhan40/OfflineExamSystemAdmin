package Controller;

import Model.MasterAnswerSheet;
import Model.Question;
import Model.QuestionBank;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Created by Farhan on 16-07-2015.
 */
public class EditQuestionController {
    @FXML
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    @FXML
    TextField textField1, textField2, textField3, textField4;
    @FXML
    TextArea question;
    @FXML
    Label questionNumber, warningLabel;

    TextField[] textFields;
    RadioButton[] radioButtons;
    int counter;
    QuestionBank questionBank;
    private MasterAnswerSheet masterAnswerSheet;

    //Called after all the required variables linked to the FXML file
    @FXML
    public void initialize() {
        textFields = new TextField[]{textField1, textField2, textField3, textField4};
        radioButtons = new RadioButton[]{radioButton1, radioButton2, radioButton3, radioButton4};
        counter = 0;
        questionBank = new QuestionBank();
        masterAnswerSheet = new MasterAnswerSheet();
        initialiseSetup();
    }

    public void setCounter(int n) {
        if (questionBank.get(n) == null)
            counter = 0;
        else
            counter = n;
        initialiseSetup();
    }

    //Called when a blank page is to be loaded properly
    private void initialiseSetup() {
        if (questionBank.get(counter) != null) {
            radioButtons[masterAnswerSheet.getCorrectAnswer(counter)].setSelected(true);
            Question currentQuestion = questionBank.get(counter);
            question.setText(currentQuestion.getQuestion());
            ArrayList<String> currentOptions = currentQuestion.getOptions();
            System.out.println(currentOptions + "" + currentOptions.size());
            System.out.println("Correct Answer = " + masterAnswerSheet.getCorrectAnswer(counter));
            for (int i = 0; i < currentOptions.size(); ++i) {
                textFields[i].setText(currentOptions.get(i));
            }
            radioButtonClicked();
        } else {
            for (int i = 0; i < 4; ++i) {
                textFields[i].setText("");
                question.setText("");
            }
            radioButtonClicked();
            addCurrent();
        }
        questionNumber.setText("Question number : " + (counter + 1));
    }

    @FXML
    private void previousStep() {
        if (counter == 0) {
            try {
                Stage stage = getStage();
                Parent parent = FXMLLoader.load(getClass().getResource("Welcome.fxml"));
                stage.setScene(new Scene(parent));
            } catch (Exception e) {
                System.out.print("Error while calling Welcome");
                e.printStackTrace();
            }
        } else {
            previousQuestion();
        }
    }

    //Load the previous question.
    private void previousQuestion() {
        if (question.getText().equals("")) {
            deleteCurrent();
            --counter;
        } else {
            if (setCurrent() == -1)
                return;
            --counter;
        }
        initialiseSetup();
    }

    //Method to show next question (called by Next button)
    @FXML
    private void nextQuestion() {
        if (question.getText().equals("")) {
            deleteCurrent();
        } else {
            if (setCurrent() == -1)
                return;
            ++counter;
        }
        initialiseSetup();
    }


    @FXML
    private void nextStep() {

        if (question.getText().equals("")) {
            questionBank.delete(counter);
        } else {
            if (setCurrent() == -1)
                return;
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("QuestionnaireSave.fxml"));
            Parent parent = loader.load();
            QuestionnaireSaveController questionnaireSaveController = loader.getController();
            questionnaireSaveController.setInitialize(questionBank, masterAnswerSheet);
            Stage stage = getStage();
            stage.setScene(new Scene(parent));
        } catch (Exception e) {
            System.out.println("Cannot load Questionnaire Save");
            e.printStackTrace();
        }
    }

    //Adds a new question to the question Bank
    private void addCurrent() {
        ArrayList<String> options = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            if (!textFields[i].getText().equals("")) {
                options.add(textFields[i].getText());
            }
        }
        String question = this.question.getText();
        questionBank.addQuestion(question, options);
    }

    //replaces a previously added question
    private int setCurrent() {

        ArrayList<String> options = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            if (!textFields[i].getText().equals("")) {
                options.add(textFields[i].getText());
            }
        }
        if (textFields[masterAnswerSheet.getCorrectAnswer(counter)].getText().equals("")) {
            warningLabel.setOpacity(1);
            return -1;
        } else {
            warningLabel.setOpacity(0);
        }
        String question = this.question.getText();
        questionBank.addQuestion(counter, question, options);
        return 0;
    }

    //Method to Enable and Disable TextFields as needed (called when check box touched)
    @FXML
    private void radioButtonClicked() {
        for (int i = 0; i < 4; ++i) {
            if (radioButtons[i].isSelected()) {
                textFields[i].setStyle("-fx-border-color: #5FB404;-fx-background-color: #D0F5A9");
                masterAnswerSheet.setAnswer(counter, i);
            } else
                textFields[i].setStyle("");
        }
    }

    //Delete the current Question
    private void deleteCurrent() {
        questionBank.delete(counter);
        masterAnswerSheet.delete(counter);
    }

    //Helper method to return the stage
    private Stage getStage() {
        return (Stage) textField4.getScene().getWindow();
    }

    public void initialiseSetup(QuestionBank questionBank, MasterAnswerSheet masterAnswerSheet) {
        this.questionBank = questionBank;
        this.masterAnswerSheet = masterAnswerSheet;
        setCounter(this.questionBank.size() - 1);
    }

    @FXML
    public void showAbout(Event e) {
        AboutUsLoader aboutUsLoader = new AboutUsLoader();
        aboutUsLoader.showAboutUs();
    }

}

