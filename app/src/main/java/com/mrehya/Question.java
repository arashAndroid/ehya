package com.mrehya;

/**
 * Created by sdfsdfasf on 2/27/2018.
 */

public class Question {

    String question;
    answer ans1,ans2,ans3,ans4,ans5;
    int id,time;

    public Question(String question, answer ans1, answer ans2, answer ans3, answer ans4, answer ans5, int id, int time) {
        this.question = question;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ans4 = ans4;
        this.ans5 = ans5;
        this.id = id;
        this.time = time;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public answer getans1() {
        return ans1;
    }
    public void setans1(answer ans1) {
        this.ans1 = ans1;
    }

    public answer getans2() {
        return ans2;
    }
    public void setans2(answer ans2) {
        this.ans2 = ans2;
    }

    public answer getans3() {
        return ans3;
    }
    public void setans3(answer ans3) {
        this.ans3 = ans3;
    }

    public answer getans4() {
        return ans4;
    }
    public void setans4(answer ans4) {
        this.ans4 = ans4;
    }

    public answer getans5() {
        return ans5;
    }
    public void setans5(answer ans5) {
        this.ans5 = ans5;
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

    public class answer
    {
        public int id,questionId,point;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getQuestionId() {
            return questionId;
        }

        public void setQuestionId(int questionId) {
            this.questionId = questionId;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTexten() {
            return texten;
        }

        public void setTexten(String texten) {
            this.texten = texten;
        }

        public String text,texten;

    }
}
