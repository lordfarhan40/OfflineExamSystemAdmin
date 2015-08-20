package Model;

import java.util.ArrayList;

/**
 * Created by Farhan on 31-07-2015.
 */
public class MasterAnswerSheet {

    private ArrayList<Integer> answer;

    public MasterAnswerSheet() {
        answer = new ArrayList<>();
    }

    public void setAnswer(int position, int correctOption) {
        if (position <= (answer.size() - 1)) {
            answer.set(position, correctOption);
        } else {
            answer.add(correctOption);
        }
    }

    public int getCorrectAnswer(int position) {
        return answer.get(position);
    }

    public int size() {
        return answer.size();
    }

    public boolean delete(int position) {
        if (position <= (size() - 1)) {
            answer.remove(position);
            return true;
        }
        return false;
    }


}
