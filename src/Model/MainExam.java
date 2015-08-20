package Model;

import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by Farhan on 24-07-2015.
 * My Master Piece, Extends questionBank so that it can be used with classes that were created
 * before this flawlessly :D
 */
public class MainExam extends QuestionBank {
    private String examTitle;
    private String timeMinutes;
    private String examInstructions;

    public static void examFileChooserSettings(FileChooser fileChooser) {
        fileChooser.setTitle("Choose an Exam File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Exam File", "*.exm")
        );
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }

    public static void examFileSaveSettings(FileChooser fileChooser) {
        fileChooser.setTitle("Select save location");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Exam File", "*.exm")
        );
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }

    public MainExam() {
        examTitle = null;
        timeMinutes = "0";
        examInstructions = "0";
    }

    public MainExam(QuestionBank b) {

        super(b);
        examTitle = null;
        timeMinutes = "0";
        examInstructions = "0";
    }


    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public int getTimeMinutes() {
        return Integer.parseInt(timeMinutes);
    }

    public void setTimeMinutes(int timeMinutes) {
        this.timeMinutes = "" + timeMinutes;
    }

    public String getExamInstructions() {
        return examInstructions;
    }

    public void setExamInstructions(String examInstructions) {
        this.examInstructions = examInstructions;
    }

}
