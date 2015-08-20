package Controller;

import Helper.FileWorker;
import Model.AnswerSheet;
import Model.MasterAnswerSheet;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Farhan on 11-08-2015.
 */
public class EvaluateAnswerController {

    @FXML
    private Button selectFolder;

    @FXML
    private Label alertForMasterKey, answerKeyError, alertForCorrectMarks, alertForNegativeMarks;

    @FXML
    private TextField negativeMarks, textBoxMarks;

    private int marksForCorrect, marksForWrong;

    private ArrayList<AnswerSheet> answerSheets;
    private ArrayList<Integer> correctAttempted, wrongAttempted;
    private MasterAnswerSheet masterAnswerSheet;

    public EvaluateAnswerController() {
        correctAttempted = new ArrayList<>();
        wrongAttempted = new ArrayList<>();
        marksForCorrect = 0;
        marksForWrong = 0;
    }

    @FXML
    public void selectMasterKey(Event event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Master Answer Key", "*.mak")
        );
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            masterAnswerSheet = FileWorker.readMasterAnswerSheet(file);
            Button button = (Button) event.getSource();
            button.setText("Master key set!");
        }
    }

    @FXML
    private void goBack(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("../View/Welcome.fxml"));
            getStage().setScene(new Scene(parent));
        } catch (Exception exception) {

        }
    }

    @FXML
    public void selectFolder(Event event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(getStage());
        if (file == null)
            return;
        File[] listOfFiles = file.listFiles();
        answerSheets = readAnswerSheets(listOfFiles);
        Collections.sort(answerSheets);
    }

    public int evaluateAnswerSheet(int index) {
        int size = masterAnswerSheet.size();
        int correctAnswers = 0;
        for (int i = 0; i < size; ++i) {
            if (answerSheets.get(index).getAnswer(i) != -1) {
                if (answerSheets.get(index).getAnswer(i) == masterAnswerSheet.getCorrectAnswer(i)) {
                    correctAnswers++;
                }
            }
        }
        return correctAnswers;
    }

    public void evaluateAnswerSheets() {
        for (int i = 0; i < answerSheets.size(); ++i) {
            correctAttempted.add(evaluateAnswerSheet(i));
            wrongAttempted.add(masterAnswerSheet.size() - (correctAttempted.get(i) + answerSheets.get(i).getUnanswered()));
        }
    }

    public void generateExcel(File f) {
        try {
            int size = answerSheets.size();
            Workbook workbook = new HSSFWorkbook();
            Sheet worksheet = workbook.createSheet("Students Evaluated");
            Row row = worksheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("Roll Number");
            cell = row.createCell(1);
            cell.setCellValue("Name");
            cell = row.createCell(2);
            cell.setCellValue("Total Attempted");
            cell = row.createCell(3);
            cell.setCellValue("Total Correct");
            cell = row.createCell(4);
            cell.setCellValue("Total Wrong");
            cell = row.createCell(5);
            cell.setCellValue("Marks (Max Marks " + size * marksForCorrect + ")");
            for (int i = 0; i < size; ++i) {
                row = worksheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellValue(answerSheets.get(i).getStudRoll());
                cell = row.createCell(1);
                cell.setCellValue(answerSheets.get(i).getStudentName());
                cell = row.createCell(2);
                cell.setCellValue(answerSheets.get(i).getNumberAnswered());
                cell = row.createCell(3);
                cell.setCellValue(correctAttempted.get(i));
                cell = row.createCell(4);
                cell.setCellValue("" + wrongAttempted.get(i));
                cell = row.createCell(5);
                cell.setCellValue("" + ((correctAttempted.get(i) * marksForCorrect) - (wrongAttempted.get(i) * marksForWrong)));
            }
            System.out.println(workbook);
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public ArrayList<AnswerSheet> readAnswerSheets(File[] files) {
        ArrayList<AnswerSheet> answerSheets = new ArrayList<>();
        AnswerSheet answerSheet;
        for (File file : files) {
            answerSheet = FileWorker.readAnswerSheet(file);
            answerSheets.add(answerSheet);
        }
        return answerSheets;
    }

    @FXML
    public void onClickEvaluation(Event event) {
        if (checkForErrors() == false)
            return;
        marksForCorrect = Integer.parseInt(textBoxMarks.getText());
        marksForWrong = Integer.parseInt(negativeMarks.getText());
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel File", "*.xls")
        );
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        File file = fileChooser.showSaveDialog(getStage());
        if (file == null)
            return;
        evaluateAnswerSheets();
        generateExcel(file);
    }

    private boolean checkForErrors() {
        alertForMasterKey.setOpacity(0);
        answerKeyError.setOpacity(0);
        alertForCorrectMarks.setOpacity(0);
        alertForNegativeMarks.setOpacity(0);
        if (masterAnswerSheet == null) {
            alertForMasterKey.setOpacity(1);
            return false;
        }
        if (answerSheets == null) {
            answerKeyError.setOpacity(1);
            return false;
        }
        if ((!isNumeric(textBoxMarks)) || (Integer.parseInt(textBoxMarks.getText()) < 1)) {
            alertForCorrectMarks.setOpacity(1);
            return false;
        }
        if ((!isNumeric(negativeMarks)) || (Integer.parseInt(negativeMarks.getText()) < 0)) {
            alertForNegativeMarks.setOpacity(1);
            return false;
        }
        return true;
    }

    public boolean isNumeric(TextField textField) {
        try {
            Integer.parseInt(textField.getText());
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public Stage getStage() {
        return (Stage) selectFolder.getScene().getWindow();
    }

    @FXML
    public void showAbout(Event e) {
        AboutUsLoader aboutUsLoader = new AboutUsLoader();
        aboutUsLoader.showAboutUs();
    }
}
