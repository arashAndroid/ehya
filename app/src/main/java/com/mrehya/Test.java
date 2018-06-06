package com.mrehya;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.mrehya.Helper.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.paperdb.Paper;

public class Test extends AppCompatActivity {
    ArrayList<Exam> listExams;
    ArrayList<String> userAns;
    RadioButton option1, option2, option3 ,option4,option5;

    TextView txtQuestion;
    Button btnNext,btnBack;
    CircularProgressBar progressBar;
    private String Language;
    private ProgressDialog pDialog;
    int counter;

    private Exam exam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Paper.init(this);
        Language=updateLanguage();
        updateView(Language);

        pDialog = new ProgressDialog(this);
        counter=0;
        txtQuestion =(TextView) findViewById(R.id.txtQuestions);
        option1 =(RadioButton) findViewById(R.id.option1);
        option2 =(RadioButton) findViewById(R.id.option2);
        option3 =(RadioButton) findViewById(R.id.option3);
        option4 =(RadioButton) findViewById(R.id.option4);
        option5 =(RadioButton) findViewById(R.id.option5);

        btnBack =(Button) findViewById(R.id.btnBack);
        btnNext =(Button) findViewById(R.id.btnNext);

        progressBar = (CircularProgressBar) findViewById(R.id.progressBar);

        btnBack.setVisibility(View.GONE);

        userAns =new ArrayList<>(Collections.nCopies(100, " "));

        // getting exam from examslist by id
        Intent intent = getIntent();
        Exam_api_with_Questions(intent.getIntExtra("examId",0));
        setRadioBtn();
        setBtnOnClick();

    }

    private void setBtnOnClick(){
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((counter)>=(exam.getqCount()-1)){
                    //new activity
                    Toast.makeText(getApplicationContext(),"سوالات اتمام رسید!",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Test.this,ExamEnd.class);
                    startActivity(intent);
                    finish();
                }else {

                    if (option1.isChecked()){
                        userAns.set(counter,option1.getText().toString());
                        counter++;
                        progressBar.setProgress(((float) counter/exam.getqCount())*100);
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
                            }else if (userAns.get(counter).equalsIgnoreCase(option5.getText().toString())){
                                unCheck();
                                option5.setChecked(true);
                            }
                        }

                        set_question(exam.getQuestion(counter));
                        btnBack.setVisibility(View.VISIBLE);
                    }else if (option2.isChecked()){
                        userAns.set(counter,option2.getText().toString());
                        counter++;
                        progressBar.setProgress(((float) counter/exam.getqCount())*100);
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
                            }else if (userAns.get(counter).equalsIgnoreCase(option5.getText().toString())){
                                unCheck();
                                option5.setChecked(true);
                            }
                        }

                        set_question(exam.getQuestion(counter));
                        btnBack.setVisibility(View.VISIBLE);
                    }else if (option3.isChecked()){
                        userAns.set(counter,option3.getText().toString());
                        counter++;
                        progressBar.setProgress(((float) counter/exam.getqCount())*100);
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
                            }else if (userAns.get(counter).equalsIgnoreCase(option5.getText().toString())){
                                unCheck();
                                option5.setChecked(true);
                            }
                        }

                        set_question(exam.getQuestion(counter));
                        btnBack.setVisibility(View.VISIBLE);
                    }else if (option4.isChecked()){
                        userAns.set(counter,option4.getText().toString());
                        counter++;
                        progressBar.setProgress(((float) counter/exam.getqCount())*100);
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
                            }else if (userAns.get(counter).equalsIgnoreCase(option5.getText().toString())){
                                unCheck();
                                option5.setChecked(true);
                            }
                        }

                        set_question(exam.getQuestion(counter));
                        btnBack.setVisibility(View.VISIBLE);
                    }else if (option5.isChecked()){
                        userAns.set(counter,option5.getText().toString());
                        counter++;
                        progressBar.setProgress(((float) counter/exam.getqCount())*100);
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
                            }else if (userAns.get(counter).equalsIgnoreCase(option5.getText().toString())){
                                unCheck();
                                option5.setChecked(true);
                            }
                        }

                        set_question(exam.getQuestion(counter));
                        btnBack.setVisibility(View.VISIBLE);
                    }
                    else {
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
                    progressBar.setProgress(((float) counter/exam.getqCount())*100);
                    set_question(exam.getQuestion(counter));
                    Log.e(userAns.get(counter)+": ", option1.getText().toString());
                    if (userAns.get(counter).equalsIgnoreCase(option1.getText().toString())){
                        option1.setChecked(true);
                    }else if (userAns.get(counter).equalsIgnoreCase(option2.getText().toString())){
                        option2.setChecked(true);
                    }else if (userAns.get(counter).equalsIgnoreCase(option3.getText().toString())){
                        option3.setChecked(true);
                    }else if (userAns.get(counter).equalsIgnoreCase(option4.getText().toString())){
                        option4.setChecked(true);
                    }else if (userAns.get(counter).equalsIgnoreCase(option5.getText().toString())){
                        option5.setChecked(true);
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
        option5.setChecked(false);
    }
    private void setRadioBtn(){
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option2.setChecked(false);
                option3.setChecked(false);
                option4.setChecked(false);
                option5.setChecked(false);

            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option1.setChecked(false);
                option3.setChecked(false);
                option4.setChecked(false);
                option5.setChecked(false);
            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option2.setChecked(false);
                option1.setChecked(false);
                option4.setChecked(false);
                option5.setChecked(false);
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option2.setChecked(false);
                option3.setChecked(false);
                option1.setChecked(false);
                option5.setChecked(false);
            }
        });

        option5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option2.setChecked(false);
                option3.setChecked(false);
                option1.setChecked(false);
                option4.setChecked(false);
            }
        });
    }

    public void set_question(Question question){
        txtQuestion.setText(question.getQuestion());
        String[] shuffleAns =new String[4];

        if(Language.equals("fa")){
            shuffleAns[0]= question.getans1().getText();
            shuffleAns[1]= question.getans2().getText();
            shuffleAns[2]= question.getans3().getText();
            shuffleAns[3]= question.getans4().getText();
            shuffleAns[4]= question.getans5().getText();
        }
        else{
            shuffleAns[0]= question.getans1().getTexten();
            shuffleAns[1]= question.getans2().getTexten();
            shuffleAns[2]= question.getans3().getTexten();
            shuffleAns[3]= question.getans4().getTexten();
            shuffleAns[4]= question.getans5().getTexten();
        }

        option1.setText(shuffleAns[0]);
        option2.setText(shuffleAns[1]);
        option3.setText(shuffleAns[2]);
        option4.setText(shuffleAns[3]);
        option5.setText(shuffleAns[4]);

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

    private void Exam_api_with_Questions(final int ExamId){
        startDialog();

        String tag_string_req = "req_Exams";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Get_Exam+ExamId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Exam Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject data = jObj.getJSONObject("data");
                    if(data.length()>0){

                        exam = new Exam(
                            ExamId,data.getString("quiezTime"),data.getString("title"),data.getString("content"),
                                data.getString("price"),null
                        );
                        JSONArray Questions = data.getJSONArray("questions");

                        for(int i=0;i<Questions.length();i++){
                            JSONObject q = Questions.getJSONObject(i);
                            JSONArray Answers = q.getJSONArray("answers");
                            Question.answer[] ans = new Question.answer[Answers.length()];
                            for(int j=0;j<Answers.length();j++) {
                                JSONObject a = Questions.getJSONObject(j);
                                ans[j].setId(a.getInt("id"));
                                ans[j].setText(a.getString("answer"));
                                ans[j].setTexten(a.getString("answer_en"));
                                ans[j].setPoint(a.getInt("point"));
                                ans[j].setQuestionId(a.getInt("questionId"));
                            }
                            exam.add_Q(new Question(q.getString("question"), ans[0], ans[1], ans[2], ans[3], ans[5],q.getInt("id"), data.getInt("quiezTime")));
                        }
                    }
                    else{

                    }
                    Log.d("TAG", "No Object recieved!");
                    hideDialog();
                } catch (JSONException e) {
                    // JSON error
                    Log.d("TAG", "error 1 " + e.getMessage());
                    //e.printStackTrace();
                    hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",  "error2");
                hideDialog();
            }
        }) {
            //basic auth
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<String, String>();
                // add headers <key,value>
                String credentials = AppConfig.AUTH_USERNAME+":"+AppConfig.AUTH_PASS;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.URL_SAFE|Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



    //Progress Dialog
    private void startDialog(){
        pDialog.setCancelable(true);
        if(Language.equals("fa"))
            pDialog.setMessage("در حال اتصال...");
        else
            pDialog.setMessage("Loading Companies...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showDialog();
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private String updateLanguage(){
        //Default language is fa
        String language = Paper.book().read("language");
        if(language==null)
            Paper.book().write("language", "fa");
        return language;
    }
    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();

        btnNext.setText(resources.getString(R.string.Next));
        btnBack.setText(resources.getString(R.string.Back));
    }
}
