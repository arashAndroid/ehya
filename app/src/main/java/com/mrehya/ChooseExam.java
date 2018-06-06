package com.mrehya;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mrehya.Helper.LocaleHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.paperdb.Paper;

public class ChooseExam extends AppCompatActivity {

    ArrayList<Exam> listExams;
    ListAdapterExam listAdapterExams;

    RecyclerView listViewExams ;

    MyTextView mytextExams;
    private String Language;
    private MyTextView txtEmptyExams;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_exam);
        pDialog = new ProgressDialog(this);
        mytextExams = (MyTextView) findViewById (R.id.mytextExams);
        txtEmptyExams = (MyTextView) findViewById (R.id.txtEmptyExams);
        //new
        Paper.init(getApplicationContext());
        updateView(updatelanguage(this));
        Language = updatelanguage(this);
        listViewExams = (RecyclerView) findViewById(R.id.listViewExams);
        listExams = new ArrayList<>();
        listAdapterExams = new ListAdapterExam(listExams,getApplicationContext(),ChooseExam.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listViewExams.setLayoutManager(mLayoutManager);
        listViewExams.setItemAnimator(new DefaultItemAnimator());
        listViewExams.setAdapter(listAdapterExams);
        //setExamsList();
        Exam_Api(updatelanguage(this));
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


    private void Exam_Api(final String Language){
        startDialog();
        String tag_string_req = "req_Exams";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_DashExam+"2", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Exams Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject data = jObj.getJSONObject("data");
                    if(data.length()>0){
                        txtEmptyExams.setVisibility(View.GONE);
                        Show_Hide_Exams(true);
                        set_Content(data);
                        listAdapterExams.notifyDataSetChanged();
                    }
                    else{
                        Show_Hide_Exams(false);
                        if(Language.equals("fa"))
                            txtEmptyExams.setText("لیست آزمون\u200Cها خالی است!\"");
                        else
                            txtEmptyExams.setText("Empty Hire Advertisements!");
                        txtEmptyExams.setVisibility(View.VISIBLE);
                    }
                    Log.d("TAG", "No Object recieved!");
                    hideDialog();
                } catch (JSONException e) {
                    // JSON error
                    Log.d("TAG", "error 1 " + e.getMessage());
                    //e.printStackTrace();
                    Show_Hide_Exams(false);
                    if(Language.equals("fa")){
                        txtEmptyExams.setText("لیست آزمون\u200Cها خالی است");
                        Toast.makeText(getApplicationContext(), "مشکلی در اتصال به سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        txtEmptyExams.setText("Empty Hire Advertisements!");
                        Toast.makeText(getApplicationContext(), "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                    }
                    txtEmptyExams.setVisibility(View.VISIBLE);

                    hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Show_Hide_Exams(false);
                Log.e("TAG",  "error2");
                if(Language.equals("fa")){
                    txtEmptyExams.setText("لیست آزمون\u200Cها خالی است");
                    Toast.makeText(getApplicationContext(), "مشکلی در اتصال به سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                }

                else{
                    txtEmptyExams.setText("Empty Hire Advertisements!");
                    Toast.makeText(getApplicationContext(), "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                }
                txtEmptyExams.setVisibility(View.VISIBLE);
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
    public void set_Content(JSONObject data) throws JSONException {


        for (int i = 0; i < data.length()-1; i++) {
            JSONObject c = data.getJSONObject(i + "");
            Log.d("Fragment part String: ", c.toString());

            if (Language.equals("fa")) {
                listExams.add(new Exam(c.getInt("id"),c.getString("title"),
                        c.getString("content"), c.getString("price"),c.getJSONObject("image").getString("thumb")
                        , c.getString("paymentType")));
            }else {
                listExams.add(new Exam(c.getInt("id"),c.getString("title_en"),
                        c.getString("content_en"), c.getString("price"),c.getJSONObject("image").getString("thumb")
                        , c.getString("paymentType")));
            }


        }

    }
    //SHOW EXAMS OR GONE THEM
    public void Show_Hide_Exams(boolean show){
        if(show){
            listViewExams.setVisibility(View.VISIBLE);

        }
        else{
            listViewExams.setVisibility(View.GONE);
        }
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