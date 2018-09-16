package com.mrehya.Exams;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.goodiebag.horizontalpicker.HorizontalPicker;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.mrehya.AppConfig;
import com.mrehya.AppController;
import com.mrehya.Helper.LocaleHelper;
import com.mrehya.Login;
import com.mrehya.R;
import com.mrehya.Reserv.Reserve;
import com.mrehya.SessionManager;
import com.mrehya.User;

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
    private String Language="fa";
    private Context context;
    private Resources resources;
    private ProgressDialog pDialog;
    private ProgressDialog aDialog;
    private SessionManager session;

    //ELEMENTS
    private LinearLayout LinearLayoutRadios, LinearLayouttextViews;
    private ArrayList<Exam> listExams;
    private TextView txtQuestion, txtNumber, tv_point, tv_answer, tv_message, tv_title;
    private Button btnNext,btnBack, btn_Back;
    private CircularProgressBar progressBar;
    private RadioGroup radioGroup;


    //LISTS VARS
    private Exam exam;
    int counter;
    ArrayList<Integer> UserAnswers;
    HashMap<String, String> UserAns;
    ArrayList<String> userAnswered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        context = this;
        Paper.init(this);
        Language=updateLanguage();
        pDialog = new ProgressDialog(this);
        aDialog = new ProgressDialog(this);
        session = new SessionManager(getApplicationContext());
        findViews();
        setViews();
        // getting exam from examslist by id
        Intent intent = getIntent();
        Exam_api_with_Questions(intent.getIntExtra("examId",0));
        updateView();
    }
    private void findViews(){
        txtQuestion =(TextView) findViewById(R.id.txtQuestions);
        btnBack =(Button) findViewById(R.id.btnBack);
        btnNext =(Button) findViewById(R.id.btnNext);
        btn_Back =(Button) findViewById(R.id.btn_Back);
        txtNumber = (TextView) findViewById(R.id.txtNumber);
        tv_point = (TextView) findViewById(R.id.tv_point);
        tv_message = (TextView) findViewById(R.id.tv_message);
        tv_answer= (TextView) findViewById(R.id.tv_answer);
        tv_title= (TextView) findViewById(R.id.tv_title);
        progressBar = (CircularProgressBar) findViewById(R.id.progressBar);
        LinearLayouttextViews = (LinearLayout) findViewById(R.id.LinearLayouttextViews);
        LinearLayoutRadios = (LinearLayout) findViewById(R.id.LinearLayoutRadios);
    }
    private void setViews(){
        counter=0;
        btnBack.setVisibility(View.GONE);
        userAnswered =new ArrayList<>();
        UserAnswers=new ArrayList<>();
        UserAns = new HashMap<>();
        if(Language.equals("fa")){
            txtNumber.setText("۱");
        }
        else{
            txtNumber.setText("1");
        }
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Test.this, ChooseExam.class);
                startActivity(intent);
            }
        });
        setOnclicks();
    }
    //methods
    private void Exam_api_with_Questions(final int ExamId){
        startDialog();
        String tag_string_req = "req_Exams";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Get_Exam+ExamId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("TAG", "Exam Response: " + response.toString());
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
                            Question newquestion = new Question(q.getString("question"),q.getInt("id"), data.getString("quiezTime"));
                            for(int j=0;j<Answers.length();j++) {
                                JSONObject a = Answers.getJSONObject(j);
                                newquestion.setAnswer(new Question.answer(a.getInt("id"), a.getInt("questionId")
                                        , a.getInt("point"),  a.getString("answer"), a.getString("answer_en")));
                            }
                           // exam.add_Q(new Question(q.getString("question"), ans[0], ans[1], ans[2], ans[3], ans[4],q.getInt("id"), data.getInt("quiezTime")));
                            exam.add_Q(newquestion);
                        }
                        UserAnswers.add(-1000);
                        setQuestion();
                        //updateView(Language);
                    }
                    else{
                        Log.d("TAG", "No Object recieved!");
                        if(Language.equals("fa")){
                            Toast.makeText(context, "طراحی آزمون کامل نشده است", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, "Exam not ready", Toast.LENGTH_SHORT).show();
                        }
                    }
                    hideDialog();
                } catch (JSONException e) {
                    // JSON error
                    Log.d("TEST EXAM", "error 1 " + e.getMessage());
                    //e.printStackTrace();
                    if(Language.equals("fa")){
                        Toast.makeText(context, "طراحی آزمون کامل نشده است", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "Exam not ready", Toast.LENGTH_SHORT).show();
                    }
                    hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TEST EXAM",  "error2"+ error.getMessage());
                if(Language.equals("fa")){
                    Toast.makeText(context, "طراحی آزمون کامل نشده است", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "Exam not ready", Toast.LENGTH_SHORT).show();
                }
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
    private void setQuestion(){
        Question question = exam.getQuestion(counter);
        if(question!=null)
        {
            txtQuestion.setText(question.getQuestion());
            setRadiobtns(question.getAnswers(),question.getId());
        }
    };
    private void setRadiobtns(ArrayList<Question.answer> answers, final int questionId){
        LinearLayoutRadios.removeAllViews();
        radioGroup = new RadioGroup(this);
        for(int i=0;i<answers.size();i++){
            final Question.answer ans = answers.get(i);
            final String ansid = ans.getId()+"";
            final RadioButton radiobtn = new RadioButton(this);
            radiobtn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3));
            radiobtn.setPadding(10,10,10,10);
            radiobtn.setText(ans.getText());
            radiobtn.setId(ans.getId());
            radiobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radiobtn.setChecked(true);
                    UserAnswers.set(counter, ans.getPoint());
                    UserAns.put(questionId+"", ansid);
                    sendAnswer(questionId+"", ansid);
                }
            });
            if(ans.getPoint() == UserAnswers.get(counter)){
                radiobtn.setChecked(true);
            }
            radioGroup.addView(radiobtn);
        }
        LinearLayoutRadios.addView(radioGroup);
    }
    private void setOnclicks(){
        //NEXT
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserAnswers.get(counter) == -1000){
                    Toast.makeText(context, "گزینه ای برای این سوال انتخاب نشده است", Toast.LENGTH_SHORT).show();
                }
                else{
                    counter++;
                    progressBar.setProgress(((float) counter/exam.getqCount())*100);
                    if((counter)>=(exam.getqCount())){
                        //setPoints();
                        save();
                    }
                    else{
                        if(Language.equals("fa")){
                            txtNumber.setText(topersian(counter+1+""));
                        }
                        else{
                            txtNumber.setText(counter+1+"");
                        }
                        btnBack.setVisibility(View.VISIBLE);
                        if(counter == UserAnswers.size()){
                            UserAnswers.add(-1000);
                        }
                        setQuestion();
                    }
                }
            }
        });
        //PREVIOUS
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNext.setVisibility(View.VISIBLE);
                counter--;
                progressBar.setProgress(((float) counter/exam.getqCount())*100);
                if(Language.equals("fa")){
                    txtNumber.setText(topersian(counter+1+""));
                }
                else{
                    txtNumber.setText(counter+1);
                }
                if(counter==0){
                    btnBack.setVisibility(View.GONE);
                }
                setQuestion();
            }
        });
    }
    private void sendAnswer(final String questionId, final String answerId){
        if(!userAnswered.contains(answerId))
        {
            String tag_string_req = "req_sendAnswer";
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_SEND_ANSWER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.e("sendAnswer", "View sendAnswer Response: " + response.toString());
                    try {
                        JSONObject jObj = new JSONObject(response);
                        String success = jObj.getString("success");
                        if (success.equalsIgnoreCase("true")) {
                            JSONObject c = jObj.getJSONObject("data");
                            userAnswered.add(answerId);
                        } else {
                            Log.e("sendAnswer", "failed to get sendAnswer");;
                        }
                    } catch (JSONException e) {
                        // JSON error
                        Log.e("sendAnswer", "error 1 " + e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("sendAnswer",  "error2 : " + error.getMessage());;
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("auth", session.getUserDetails().getToken());
                    params.put("question", questionId);
                    params.put("answer", answerId);
                    return params;
                }
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
    }
    private void save(){
        btnNext.setVisibility(View.GONE);
        btnBack.setVisibility(View.GONE);
        txtQuestion.setVisibility(View.GONE);
        txtNumber.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        LinearLayoutRadios.removeAllViews();
        int sum=0;
        for (int point:
             UserAnswers) {
                sum+=point;
        }
        Log.e( "SUm", sum+"");
        if(UserAns.size() == userAnswered.size())
            new TestResultTask().execute(0);
        else{
            for (String item:
                 UserAns.keySet()) {
                sendAnswer(item, UserAns.get(item));
            }

            new TestResultTask().execute(0);
        }

        Toast.makeText(context, "آزمون به پایان رسید", Toast.LENGTH_SHORT).show();
    }
    class TestResultTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                Thread.sleep(2000);
                // some long running task will run here. We are using sleep as a dummy to delay execution
                getExamCode();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
            // do whatever needs to be done next
            //Log.e("result", "Connecting to server..." );
        }
        @Override
        protected void onPreExecute() {
            startaDialog();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
    private void getExamCode(){
        String tag_string_req = "req_getExamCode";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_Get_Exam_CODE+exam.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getExamCode", "View ExamCode Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");
                    if (success.equalsIgnoreCase("true")) {
                        JSONObject c = jObj.getJSONObject("data");
                        //GET RESULTS AND SEND USER GOT THIS EXAM
                        get_show_Results(c.getString("code"));
                    } else {
                        Log.e("getExamCode", "failed to get getExamCode");
                        hideaDialog();
                    }
                } catch (JSONException e) {
                    // JSON error
                    Log.e("getExamCode", "error 1 " + e.getMessage());
                    hideaDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("examcode",  "error2 : " + error.getMessage());
                hideaDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("auth", session.getUserDetails().getToken());
                return params;
            }

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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void get_show_Results(final String code){
        String tag_string_req = "req_get_show_Results";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_Get_Exam_RESULT+exam.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("get_show_Results", "View get_show_Results Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");
                    if (success.equalsIgnoreCase("true")) {
                        JSONArray data = jObj.getJSONArray("data");
                        //GET RESULTS AND SEND USER GOT THIS EXAM
                        for(int i=0;i<data.length();i++){
                            JSONObject c = data.getJSONObject(i);
                            JSONArray messages = c.getJSONArray("messages");
                            JSONObject message = messages.getJSONObject(0);
                            int min  = message.getInt("pointMin");
                            int max  = message.getInt("pointMax");
                            int point = c.getInt("point");
                           //Log.e("points", min+","+point+","+max);
                            if(point>min && point<max)
                            {
                                Log.e("RESULTs","hi");
                                tv_title.setText(c.getString("title"));
                                tv_title.setVisibility(View.VISIBLE);

                                tv_point.setText("امتیاز شما: " + c.getString("point"));
                                tv_point.setVisibility(View.VISIBLE);

                                tv_answer.setText("جواب تست: " + message.getString("resultShow"));
                                tv_answer.setVisibility(View.VISIBLE);

                                tv_message.setText(message.getString("message"));
                                tv_answer.setVisibility(View.VISIBLE);

                                btn_Back.setVisibility(View.VISIBLE);
                                if(Language.equals("fa")){
                                    btn_Back.setText("برگشت");
                                }
                                else{
                                    btn_Back.setText("back");
                                }
                            }
                        }
                    } else {
                        Log.e("get_show_Results", "failed to get get_show_Results");;
                    }
                    hideaDialog();
                } catch (JSONException e) {
                    // JSON error
                    Log.e("get_show_Results", "error 1: " + e.getMessage());
                    hideaDialog();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("get_show_Results",  "error2 : " + error.getMessage());
                hideaDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("auth", session.getUserDetails().getToken());
                params.put("code", code);
                params.put("lang", Language);
                return params;
            }
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

    //methods
    private String updateLanguage(){
        String language = Paper.book().read("language");
        if(language==null)
            Paper.book().write("language", "fa");
        return language;
    }
    private void updateView() {
        context = LocaleHelper.setLocale(this, Language);
        resources = context.getResources();
        btnNext.setText(resources.getString(R.string.Next));
        btnBack.setText(resources.getString(R.string.Back));
    }
    private String topersian(String input){
        return input.replace("1","١").replace("2","٢").replace("3","٣")
                .replace("4","۴").replace("5","۵").replace("6","۶")
                .replace("7","۷").replace("8","۸").replace("9","۹")
                .replace("0","۰");
    }
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
    private void startaDialog(){
        aDialog.setCancelable(true);
        if(Language.equals("fa"))
            aDialog.setMessage("استخراج جواب آزمون...");
        else
            aDialog.setMessage("Loading Result...");
        aDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showaDialog();
    }
    private void showaDialog() {
        if (!aDialog.isShowing())
            aDialog.show();
    }
    private void hideaDialog() {
        if (aDialog.isShowing())
            aDialog.dismiss();
    }
}
