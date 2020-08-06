package in.dailycrunch.quiztion;

/**
 * Created by Adarsh Sodagudi(025281709).
 * Mansi Koul(018761247)
 */

// Class to hold the questions data of quiz
public class QuestionData {

    private String QUESTION;
    private String CHOICE1;
    private String CHOICE2;
    private String CHOICE3;
    private String CHOICE4;
    private String ANSWER;

    public QuestionData(String QUESTION, String CHOICE1, String CHOICE2, String CHOICE3, String CHOICE4, String ANSWER) {
        this.QUESTION = QUESTION;
        this.CHOICE1 = CHOICE1;
        this.CHOICE2 = CHOICE2;
        this.CHOICE3 = CHOICE3;
        this.CHOICE4 = CHOICE4;
        this.ANSWER = ANSWER;
    }

    /*getters and setters*/
    public String getQUESTION() {
        return QUESTION;
    }

    public void setQUESTION(String QUESTION) {
        this.QUESTION = QUESTION;
    }

    public String getCHOICE1() {
        return CHOICE1;
    }

    public void setCHOICE1(String CHOICE1) {
        this.CHOICE1 = CHOICE1;
    }

    public String getCHOICE2() {
        return CHOICE2;
    }

    public void setCHOICE2(String CHOICE2) {
        this.CHOICE2 = CHOICE2;
    }

    public String getCHOICE3() {
        return CHOICE3;
    }

    public void setCHOICE3(String CHOICE3) {
        this.CHOICE3 = CHOICE3;
    }

    public String getCHOICE4() {
        return CHOICE4;
    }

    public void setCHOICE4(String CHOICE4) {
        this.CHOICE4 = CHOICE4;
    }

    public String getANSWER() {
        return ANSWER;
    }

    public void setANSWER(String ANSWER) {
        this.ANSWER = ANSWER;
    }
}
