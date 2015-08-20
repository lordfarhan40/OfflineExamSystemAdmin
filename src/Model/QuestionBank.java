package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Farhan on 16-07-2015.
 */
public class QuestionBank {
    private List<Question> questionBank;

    public QuestionBank(QuestionBank questionBank) {
        this.questionBank = questionBank.getQuestionBank();
    }

    public List<Question> getQuestionBank() {
        return questionBank;
    }

    public QuestionBank() {
        questionBank = new ArrayList<>();
    }

    public QuestionBank(String location) {
        //Load Questions from a JSON File
    }

    public void addQuestion(String question, ArrayList<String> options) {
        Question questionAdd = new Question(question, options);
        questionBank.add(questionAdd);
    }

    public void addQuestion(int position, String question, ArrayList<String> options) {
        Question questionAdd = new Question(question, options);
        questionBank.set(position, questionAdd);
    }

    public void delete(int position) {
        questionBank.remove(position);
    }

    public Question get(int position) {
        if ((position < questionBank.size()) && position > -1)
            return questionBank.get(position);
        else
            return null;
    }

    public int size() {
        return questionBank.size();
    }


}
