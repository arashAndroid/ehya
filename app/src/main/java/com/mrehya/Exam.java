package com.mrehya;

import java.util.ArrayList;

/**
 * Created by sdfsdfasf on 2/27/2018.
 */

public class Exam {
    private int qCount;
    private int id;
    private int time;

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

    private String name;

    public int getqCount() {
        return questions.size();
    }

    public void setqCount() {
        this.qCount = questions.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    private String description;
    private ArrayList<Question> questions =new ArrayList<Question>();


    public Exam(int id, int time, String name, String description) {
        this.id = id;
        this.time = time;
        this.name = name;
        this.description = description;
    }


    public void add_Q(Question q) {
        questions.add(q);
    }

    public Question getQuestion(int index){
        return questions.get(index);
    }


}
