package com.mrehya.Resume;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mrehya.AppConfig;
import com.mrehya.AppController;
import com.mrehya.Helper.LocaleHelper;
import com.mrehya.Login;
import com.mrehya.MainActivity;
import com.mrehya.R;
import com.mrehya.SessionManager;
import com.mrehya.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class ResumeMainActivity extends AppCompatActivity {
    //GLOBALS
    private String Language;
    private SessionManager session;
    private Resources resources;
    private ProgressDialog pDialog;
    //ELEMENTS
    Button btn_UserInfo, btn_UserActs, btn_UserAboutMe,
    btn_UserBenefits, btn_ShowResume, btn_gotologin, btn_UserLanguages,
            btn_UserDegrees, btn_UserJobexp, btn_Back;

    TextView txtNotloggedin;

    LinearLayout LinearLayoutLogged, LinearLayoutNotlogged;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_main);
        session = new SessionManager(getApplicationContext());
        pDialog = new ProgressDialog(this);
        updateLanguage();
        findviews();
        setOnclicks();
        updateView();
        User u = new User(this);
        u.checkLogin(session.getUserDetails().getEmail(), session.getUserDetails().getPassword());
    }
    public void findviews(){
        btn_UserInfo = (Button) findViewById(R.id.btn_UserInfo);
        btn_UserActs = (Button) findViewById(R.id.btn_UserActs);
        btn_UserAboutMe = (Button) findViewById(R.id.btn_UserAboutMe);
        btn_UserBenefits = (Button) findViewById(R.id.btn_UserBenefits);
        btn_ShowResume = (Button) findViewById(R.id.btn_ShowResume);
        btn_UserLanguages = (Button) findViewById(R.id.btn_UserLanguages);
        btn_UserDegrees = (Button) findViewById(R.id.btn_UserDegrees);
        btn_UserJobexp = (Button) findViewById(R.id.btn_UserJobexp);
        btn_gotologin = (Button) findViewById(R.id.btn_gotologin);
        btn_Back = (Button) findViewById(R.id.btn_Back);
        txtNotloggedin = (TextView) findViewById(R.id.txtNotloggedin);
        LinearLayoutLogged = (LinearLayout) findViewById(R.id.LinearLayoutLogged);
        LinearLayoutNotlogged = (LinearLayout) findViewById(R.id.LinearLayoutNotlogged);

        if(session.isLoggedIn()){
            LinearLayoutNotlogged.setVisibility(View.GONE);
            LinearLayoutLogged.setVisibility(View.VISIBLE);
            //Log.e("ISLOGED IN FOR RESUME", "WE ARE IN");
            if(session.getUserDetails().getResume()==null || session.getUserDetails().getResume()=="0")
            {
                //Log.e("NO resmue", session.getUserDetails().getResume());
                //new Resume_tokenAsync.ResumeAsyncTask(this, Language).execute(0);
                startDialog();
                make_resume(session, Language, this);
            }
            else
            {
                //Log.e("Has resmue", session.getUserDetails().getResume());
                Resume res = new Resume(this);
                res.get_resume_api();
            }
        }
        else{
            LinearLayoutLogged.setVisibility(View.GONE);
            LinearLayoutNotlogged.setVisibility(View.VISIBLE);
        }
    }
    public void setOnclicks(){

        btn_UserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ResumeUserInfoActivity.class);
                intent.putExtra("Language", Language);
                startActivity(intent);
            }
        });

        btn_UserActs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ResumeActivitiesActivity.class);
                intent.putExtra("Language", Language);
                startActivity(intent);
            }
        });

        btn_gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Login.class);
                intent.putExtra("Language", Language);
                startActivity(intent);
            }
        });

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.putExtra("frgToLoad", "0");
                startActivity(intent);
            }
        });

        btn_UserLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ResumeLanguagesActivity.class);
                intent.putExtra("Language", Language);
                startActivity(intent);
            }
        });

        btn_UserJobexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ResumeJobexpActivity.class);
                intent.putExtra("Language", Language);
                startActivity(intent);
            }
        });

        btn_UserDegrees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ResumeEducationActivity.class);
                intent.putExtra("Language", Language);
                startActivity(intent);
            }
        });

        btn_UserBenefits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ResumeBenefitsActivity.class);
                intent.putExtra("Language", Language);
                startActivity(intent);
            }
        });

        btn_ShowResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ResumeViewActivity.class);
                intent.putExtra("Language", Language);
                startActivity(intent);
            }
        });

        btn_UserAboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogEditAbout();
            }
        });
    }
    private String updateLanguage(){
        //Default language is fa
        Language = Paper.book().read("language");
        if(Language==null)
            Paper.book().write("language", "fa");
        return Language;
    }
    private void updateView() {
        Context context = LocaleHelper.setLocale(getApplicationContext(), Language);
        resources = context.getResources();
        updateView_4btn();
    }
    private void updateView_4btn(){
        if(Language.equals("fa")){

            //TEXTS
            btn_UserInfo.setText("اطلاعات کاربری من");
            btn_UserActs.setText("فعالیت ها و علاقه مندی\u200Cهای شغلی من");
            btn_UserAboutMe.setText("درباره من");
            btn_UserBenefits.setText("مزایای شغلی مورد نظرم");
            btn_ShowResume.setText("مشاهده رزومه");
            btn_gotologin.setText("ورود یا ثبت نام");
            btn_UserLanguages.setText("زبان ها");
            btn_UserDegrees.setText("سوابق تحصیلی");
            btn_UserJobexp.setText("سوابق شغلی");
            txtNotloggedin.setText("برای ایجاد رزومه باید لاگین کنید");
            btn_Back.setText("برگشت");
        }
        else{
            //TEXTS
            btn_UserInfo.setText("User Private Information");
            btn_UserActs.setText("User Activities");
            btn_UserAboutMe.setText("About Me");
            btn_UserBenefits.setText("Desired Job Benefits");
            btn_ShowResume.setText("View Total Resume");
            btn_gotologin.setText("Login/Regsiter");
            btn_UserLanguages.setText("Languages");
            btn_UserDegrees.setText("Educational records");
            btn_UserJobexp.setText("Work experiences");
            txtNotloggedin.setText("You need to login in order to build resume");
            btn_Back.setText("Back to list");
        }
    }
    private void save(String aboutme){
        if(Language.equals("fa")){
            Toast.makeText(this, "در حال ارتباط با سرور...", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Connecting to server...", Toast.LENGTH_SHORT).show();
        }
        String result = session.set_Resume_Aboutme(aboutme);
        if(result.equals("ok")){
            Resume resume = new Resume(this,
                    Arrays.asList(resources.getStringArray(R.array.Province)),
                    Arrays.asList(resources.getStringArray(R.array.Terms)),
                    Arrays.asList(resources.getStringArray(R.array.contract_arrays)),
                    Arrays.asList(resources.getStringArray(R.array.Senority_arrays)));
            resume.set_resume_api();
        }
    }
    //methods
    private void showDialogEditAbout(){
        final Dialog dialog = new Dialog(ResumeMainActivity.this);
        dialog.setContentView(R.layout.list_dialog_about);
        final EditText txtEditDialogAbout = dialog.findViewById(R.id.txtEditDialogAbout);
        final Button btn_back, btn_saveresume;
        //txtEditDialogAbout.setText(oldAbout);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        TextView btnEditd = dialog.findViewById(R.id.btnEditd);
        btn_back = dialog.findViewById(R.id.btn_back);
        btn_saveresume = dialog.findViewById(R.id.btn_saveresume);
        if(Language.equals("fa"))
        {
            btn_back.setText("برگشت");
            btn_saveresume.setText("ذخیره");
            btnEditd.setText("ویرایش");
        }
        else{
            btn_back.setText("Back");
            btn_saveresume.setText("Save");
            btnEditd.setText("Edit");
        }
        try {
            txtEditDialogAbout.setText(session.get_Resume().getAbout_me());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_saveresume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(txtEditDialogAbout.getText().toString());
                //txtAboutMe.setText(txtEditDialogAbout.getText());
                dialog.dismiss();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //txtAboutMe.setText(txtEditDialogAbout.getText());
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout((13 * width)/14, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    //DAFAULTS RESUME
    private void make_resume(final SessionManager session, final String Language, final Context context){

        String tag_string_req = "req_make_resume";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MAKE_RESUME, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.e("make_resume", ": " + response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");
                    if (success.equalsIgnoreCase("true")) {
                        JSONObject c = jObj.getJSONObject("data");
                        session.setResumeId(c.getInt("id")+"");
                        Resume resume = new Resume(context);
                        resume.get_resume_api();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    Log.d("TAG", "error 1, creating first resume");
                    hideDialog();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",  "error 2,creating first resume " + " " + error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("auth",session.getUserDetails().getToken() );
                if(Language.equals("fa")){
                    params.put("Resume[job_title]", "عنوان شغلی");
                    params.put("Resume[about_me]","درباره من");
                }
                else{
                    params.put("Resume[job_title]", "Job title");
                    params.put("Resume[about_me]","About me");
                }
                params.put("Resume[job_status]","1");
                params.put("Resume[provinceId] ","1");
                params.put("Resume[year_birth]", "0000");
                params.put("Resume[martial]", "1");
                params.put("Resume[sex]","1");
                params.put("Resume[levels]","1");
                params.put("Resume[slug]",myrandom());
                params.put("categories","1");
                params.put("Resume[salary]","1");
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
    public static String myrandom(){
        Date date = new Date();
        return date.getYear()+""+date.getMonth()+""+date.getDay()+""+date.getMinutes()+""+date.getSeconds()+""+(int)(Math.random()*6);
    }

    private void startDialog(){
        pDialog.setCancelable(false);
        if(Language.equals("fa"))
            pDialog.setMessage("بارگزاری رزومه(نخستین مراجعه)");
        else
            pDialog.setMessage("Logging in...");
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
}
