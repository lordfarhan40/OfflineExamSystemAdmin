package Model;

import javafx.stage.FileChooser;

import java.util.ArrayList;

/**
 * Created by Farhan on 06-08-2015.
 */
public class AnswerSheet implements Comparable<AnswerSheet> {
    private ArrayList<Integer> answers;
    private String studentName;
    private String studRoll;

    public void setFileChooser(FileChooser fileChooser) {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text File", "*.txt")
        );
        fileChooser.setTitle("Save AnswerSheet");
    }

    // public void setFileChooser(FileChooser)
    public AnswerSheet() {
        answers = new ArrayList<>();
        studentName = "";
        studRoll = "";
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudRoll() {
        return studRoll;
    }

    public void setStudRoll(String studRoll) {
        this.studRoll = studRoll;
    }

    public AnswerSheet(ArrayList<Integer> answers) {
        this.answers = answers;
    }

    public void setSize(int size) {
        for (int i = 0; i < size; ++i) {
            answers.add(-1);
        }
    }

    public void setAnswer(int position, int option) {
        answers.set(position, option);
    }

    public int getAnswer(int position) {
        return answers.get(position);
    }

    public int getNumberAnswered() {
        int count = 0;
        for (int index = 0; index < answers.size(); ++index) {
            if (getAnswer(index) != -1)
                ++count;
        }
        return count;
    }

    public int getUnanswered() {
        int count = 0;
        for (int index = 0; index < answers.size(); ++index) {
            if (getAnswer(index) == -1)
                ++count;
        }
        return count;
    }

    @Override
    public int compareTo(AnswerSheet o) {
        return studRoll.compareTo(o.getStudRoll());
    }
}
