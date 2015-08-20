package Model;

import java.util.ArrayList;

/**
 * Created by Farhan on 16-07-2015.
 */
public class Question {
    private String question;
    private ArrayList<String> options;

    public Question(String question, ArrayList<String> options) {
        this.question = question;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getOptions() {
        return options;
    }
}
