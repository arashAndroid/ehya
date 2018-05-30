package com.mrehya;

/**
 * Created by sdfsdfasf on 2/27/2018.
 */

public class Question {

    String question,correctAns,wrongAns1,wrongAns2,wrongAns3;
    int id,time;

    public Question(String question, String correctAns, String wrongAns1, String wrongAns2, String wrongAns3, int id, int time) {
        this.question = question;
        this.correctAns = correctAns;
        this.wrongAns1 = wrongAns1;
        this.wrongAns2 = wrongAns2;
        this.wrongAns3 = wrongAns3;
        this.id = id;
        this.time = time;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(String correctAns) {
        this.correctAns = correctAns;
    }

    public String getWrongAns1() {
        return wrongAns1;
    }

    public void setWrongAns1(String wrongAns1) {
        this.wrongAns1 = wrongAns1;
    }

    public String getWrongAns2() {
        return wrongAns2;
    }

    public void setWrongAns2(String wrongAns2) {
        this.wrongAns2 = wrongAns2;
    }

    public String getWrongAns3() {
        return wrongAns3;
    }

    public void setWrongAns3(String wrongAns3) {
        this.wrongAns3 = wrongAns3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
