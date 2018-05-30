package com.mrehya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Test extends AppCompatActivity {
    ArrayList<Exam> listExams;
    ArrayList<String> userAns;
    RadioButton option1, option2, option3 ,option4;

    TextView txtQuestion;
    Button btnNext,btnBack;
    CircularProgressBar progressBar;

    Exam ex1;

    int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        counter=0;
        txtQuestion =(TextView) findViewById(R.id.txtQuestions);
        option1 =(RadioButton) findViewById(R.id.option1);
        option2 =(RadioButton) findViewById(R.id.option2);
        option3 =(RadioButton) findViewById(R.id.option3);
        option4 =(RadioButton) findViewById(R.id.option4);

        btnBack =(Button) findViewById(R.id.btnBack);
        btnNext =(Button) findViewById(R.id.btnNext);

        progressBar = (CircularProgressBar) findViewById(R.id.progressBar);

        btnBack.setVisibility(View.GONE);

        userAns =new ArrayList<>(Collections.nCopies(100, " "));


        // static list
        listExams = new ArrayList<>();
        listExams.add(new Exam(0,10,"زناشویی","ارتقااعتماداعتماداعتماداعتماد"));
        listExams.add(new Exam(1,10,"اعتماد به نفس","نفس اعتماداعتماداعتماد "));
        listExams.add(new Exam(2,10,"هوش","ارتقا هوش هوش هوشهوش هوش"));

        // getting exam from examslist by id
        Intent intent = getIntent();
        int examId = intent.getIntExtra("examId",0);
        ex1 = listExams.get(examId);
        if (examId==0) {
            ex1.add_Q(new Question("1u are donkey?", "بله", "نه", "q1", "q", 1, 1500));
            ex1.add_Q(new Question("2u are donkey?", "بله", "نه", "q1", "q", 2, 1500));
            ex1.add_Q(new Question("3u are dq123onkey?", "بله", "نه", "q1", "q", 3, 1500));
            ex1.add_Q(new Question("4u are donkey?", "بله", "نه", "q1", "q", 4, 1500));
            ex1.add_Q(new Question("5u are 123donkey?", "بله", "نه", "q1", "q", 5, 1500));
            ex1.add_Q(new Question("6u are 12donkey?", "بله", "نه", "q1", "q", 6, 1500));
            ex1.add_Q(new Question("7u are 2dwonkey?", "بله", "نه", "q1", "q", 7, 1500));
            ex1.add_Q(new Question("8u are doqwnkey?", "بله", "نه", "q1", "q", 8, 1500));
            set_question(ex1.getQuestion(counter));
        }else if (examId==1) {
            ex1.add_Q(new Question("1", "بله", "نه", "q1", "q", 1, 1500));
            ex1.add_Q(new Question("2", "بله", "نه", "q1", "q", 2, 1500));
            set_question(ex1.getQuestion(counter));

        }else if (examId==2){
            ex1.add_Q(new Question("123", "بله", "نه", "q1", "q", 1, 1500));
            ex1.add_Q(new Question("123", "بله", "نه", "q1", "q", 2, 1500));
            set_question(ex1.getQuestion(counter));
        }
        setRadioBtn();
        setBtnOnClick();

    }

    private void setBtnOnClick(){
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((counter)>=(ex1.getqCount()-1)){
                    //new activity
                    Toast.makeText(getApplicationContext(),"سوالات اتمام رسید!",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Test.this,ExamEnd.class);
                    startActivity(intent);
                    finish();
                }else {

                    if (option1.isChecked()){
                        userAns.set(counter,option1.getText().toString());
                        counter++;
                        progressBar.setProgress(((float) counter/ex1.getqCount())*100);
                        if (userAns.get(counter).equalsIgnoreCase(" ")){
                            unCheck();
                        }else{
                            if (userAns.get(counter).equalsIgnoreCase(option1.getText().toString())){
                                unCheck();
                                option1.setChecked(true);
                            }else if (userAns.get(counter).equalsIgnoreCase(option2.getText().toString())){
                                unCheck();
                                option2.setChecked(true);
                            }else if (userAns.get(counter).equalsIgnoreCase(option3.getText().toString())){
                                unCheck();
                                option3.setChecked(true);
                            }else if (userAns.get(counter).equalsIgnoreCase(option4.getText().toString())){
                                unCheck();
                                option4.setChecked(true);
                            }
                        }

                        set_question(ex1.getQuestion(counter));
                        btnBack.setVisibility(View.VISIBLE);
                    }else if (option2.isChecked()){
                        userAns.set(counter,option2.getText().toString());
                        counter++;
                        progressBar.setProgress(((float) counter/ex1.getqCount())*100);
                        if (userAns.get(counter).equalsIgnoreCase(" ")){
                            unCheck();
                        }else{
                            if (userAns.get(counter).equalsIgnoreCase(option1.getText().toString())){
                                unCheck();
                                option1.setChecked(true);
                            }else if (userAns.get(counter).equalsIgnoreCase(option2.getText().toString())){
                                unCheck();
                                option2.setChecked(true);
                            }else if (userAns.get(counter).equalsIgnoreCase(option3.getText().toString())){
                                unCheck();
                                option3.setChecked(true);
                            }else if (userAns.get(counter).equalsIgnoreCase(option4.getText().toString())){
                                unCheck();
                                option4.setChecked(true);
                            }
                        }

                        set_question(ex1.getQuestion(counter));
                        btnBack.setVisibility(View.VISIBLE);
                    }else if (option3.isChecked()){
                        userAns.set(counter,option3.getText().toString());
                        counter++;
                        progressBar.setProgress(((float) counter/ex1.getqCount())*100);
                        if (userAns.get(counter).equalsIgnoreCase(" ")){
                            unCheck();
                        }else{
                            if (userAns.get(counter).equalsIgnoreCase(option1.getText().toString())){
                                unCheck();
                                option1.setChecked(true);
                            }else if (userAns.get(counter).equalsIgnoreCase(option2.getText().toString())){
                                unCheck();
                                option2.setChecked(true);
                            }else if (userAns.get(counter).equalsIgnoreCase(option3.getText().toString())){
                                unCheck();
                                option3.setChecked(true);
                            }else if (userAns.get(counter).equalsIgnoreCase(option4.getText().toString())){
                                unCheck();
                                option4.setChecked(true);
                            }
                        }

                        set_question(ex1.getQuestion(counter));
                        btnBack.setVisibility(View.VISIBLE);
                    }else if (option4.isChecked()){
                        userAns.set(counter,option4.getText().toString());
                        counter++;
                        progressBar.setProgress(((float) counter/ex1.getqCount())*100);
                        if (userAns.get(counter).equalsIgnoreCase(" ")){
                            unCheck();
                        }else{
                            if (userAns.get(counter).equalsIgnoreCase(option1.getText().toString())){
                                unCheck();
                                option1.setChecked(true);
                            }else if (userAns.get(counter).equalsIgnoreCase(option2.getText().toString())){
                                unCheck();
                                option2.setChecked(true);
                            }else if (userAns.get(counter).equalsIgnoreCase(option3.getText().toString())){
                                unCheck();
                                option3.setChecked(true);
                            }else if (userAns.get(counter).equalsIgnoreCase(option4.getText().toString())){
                                unCheck();
                                option4.setChecked(true);
                            }
                        }

                        set_question(ex1.getQuestion(counter));
                        btnBack.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(getApplicationContext(),"لطفا یکی از گرینه‌ها را انتخاب کنید",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (counter<=0){
                    counter++;
                    btnBack.setVisibility(View.GONE);

                }else {
                    unCheck();
                    counter--;
                    progressBar.setProgress(((float) counter/ex1.getqCount())*100);
                    set_question(ex1.getQuestion(counter));
                    Log.e(userAns.get(counter)+": ", option1.getText().toString());
                    if (userAns.get(counter).equalsIgnoreCase(option1.getText().toString())){
                        option1.setChecked(true);
                    }else if (userAns.get(counter).equalsIgnoreCase(option2.getText().toString())){
                        option2.setChecked(true);
                    }else if (userAns.get(counter).equalsIgnoreCase(option3.getText().toString())){
                        option3.setChecked(true);
                    }else if (userAns.get(counter).equalsIgnoreCase(option4.getText().toString())){
                        option4.setChecked(true);
                    }
                    if (counter<=0){
                        btnBack.setVisibility(View.GONE);
                    }
                }
            }
        });

    }
    private void unCheck(){
        option1.setChecked(false);
        option2.setChecked(false);
        option3.setChecked(false);
        option4.setChecked(false);
    }
    private void setRadioBtn(){
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option2.setChecked(false);
                option3.setChecked(false);
                option4.setChecked(false);

            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option1.setChecked(false);
                option3.setChecked(false);
                option4.setChecked(false);

            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option2.setChecked(false);
                option1.setChecked(false);
                option4.setChecked(false);

            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option2.setChecked(false);
                option3.setChecked(false);
                option1.setChecked(false);

            }
        });
    }

    public void set_question(Question question){
        txtQuestion.setText(question.getQuestion());
        String[] shuffleAns =new String[4];
        shuffleAns[0]= question.getCorrectAns();
        shuffleAns[1]= question.getWrongAns1();
        shuffleAns[2]= question.getWrongAns2();
        shuffleAns[3]= question.getWrongAns3();

//        shuffleArray(shuffleAns);

        option1.setText(shuffleAns[0]);
        option2.setText(shuffleAns[1]);
        option3.setText(shuffleAns[2]);
        option4.setText(shuffleAns[3]);

    }
    public static void shuffleArray(String[] a) {
        int n = a.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(a, i, change);
        }
    }
    private static void swap(String[] a, int i, int change) {
        String helper = a[i];
        a[i] = a[change];
        a[change] = helper;
    }




}
