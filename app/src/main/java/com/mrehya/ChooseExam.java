package com.mrehya;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.mrehya.Helper.LocaleHelper;

import java.util.ArrayList;

import io.paperdb.Paper;

public class ChooseExam extends AppCompatActivity {

    ArrayList<Exam> listExams;
    ListAdapterExam listAdapterExams;

    MyTextView mytextExams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_exam);

        final ListView listViewExams = (ListView) findViewById(R.id.listViewExams);
        setExamsList();
        listViewExams.setAdapter(listAdapterExams);

        //new
        mytextExams = (MyTextView) findViewById (R.id.mytextExams);
        Paper.init(this);
        updateView(updatelanguage(this));

    }

    //****Setting the List Items****//
    private void setExamsList(){
//        load exams fromdatabase
        listExams = new ArrayList<>();
        listExams.add(new Exam(0,10,"زناشویی111","ارتقااعتماداعتماداعتماداعتماد"));
        listExams.add(new Exam(1,10,"اعتماد به نفس","نفس اعتماداعتماداعتماد "));
        listExams.add(new Exam(2,10,"هوش","ارتقا هوش هوش هوشهوش هوش"));

        listAdapterExams = new ListAdapterExam(listExams,getApplicationContext(),ChooseExam.this);
    }


    private String updatelanguage(Context context){
        Paper.init(context);
        //Default language is fa
        String language = Paper.book().read("language");
        if(language==null)
            Paper.book().write("language", "fa");

        return language;
    }

    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();
        mytextExams.setText(R.string.Exams);
    }
}
