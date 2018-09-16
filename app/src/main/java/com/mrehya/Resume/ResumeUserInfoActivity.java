package com.mrehya.Resume;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mrehya.Helper.LocaleHelper;
import com.mrehya.R;
import com.mrehya.SessionManager;
import com.mrehya.User;

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

public class ResumeUserInfoActivity extends AppCompatActivity {
    private String Language;
    private SessionManager session;
    private Resources resources;
    //ELEMENTS
    private ImageView imgUserImg ;
    private EditText
            EdittxtJob ,
            EdittxtBirth_Year;
    private TextView
            txtUserInfo ,
            txtJobTitle ,
            txtBirth_YearTitle,
            txtJob_StatusTitle,
            txtProvinceTitle,
            txtMarriedTitle,
            txtGenderTitle;

    private Button btn_back, btn_saveresume;


    private Spinner
            spinnerJob_Status,
            spinnerProvince,
            spinnerMarried,
            spinnerGender;

    /////global arraylist
    private List<String>
            Marital_arrays,
            Gender_arrays ,
            Province,
            JobStatus_arrays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_user_info);
        session = new SessionManager(getApplicationContext());
        Intent intent = getIntent();
        Language = intent.getStringExtra("Language");
        findviews();
        setOnclicks();
        updateView();
        try {
            setDefaults();
        } catch (JSONException e) {
            Log.e("FAILED"," TO SET DEFAULT OF RESUME INFO USERS: " + e.getMessage() );
            e.printStackTrace();
        }
        User u = new User(this);
        u.checkLogin(session.getUserDetails().getEmail(), session.getUserDetails().getPassword());
    }
    public void findviews(){
        imgUserImg = (ImageView)  findViewById(R.id.imgUserImg);
        txtUserInfo =  (TextView)  findViewById(R.id.txtUserInfo);

        EdittxtJob          =  (EditText) findViewById(R.id.EdittxtJob);
        EdittxtBirth_Year   =  (EditText) findViewById(R.id.EdittxtBirth_Year);

        txtJobTitle             =  (TextView)   findViewById(R.id.txtJobTitle);
        txtBirth_YearTitle      =  (TextView) findViewById(R.id.txtBirth_YearTitle);
        txtJob_StatusTitle      =  (TextView) findViewById(R.id.txtJob_StatusTitle);
        txtProvinceTitle        =  (TextView) findViewById(R.id.txtProvinceTitle);
        txtMarriedTitle         =  (TextView) findViewById(R.id.txtMarriedTitle);
        txtGenderTitle          =  (TextView) findViewById(R.id.txtGenderTitle);

        spinnerJob_Status   =  (Spinner) findViewById(R.id.spinnerJob_Status);
        spinnerProvince            =  (Spinner) findViewById(R.id.spinnerProvince);
        spinnerMarried             =  (Spinner) findViewById(R.id.spinnerMarried);
        spinnerGender              =  (Spinner) findViewById(R.id.spinnerGender);

        btn_back            = (Button) findViewById(R.id.btn_back);
        btn_saveresume      = (Button) findViewById(R.id.btn_saveresume);
    }
    public void setOnclicks(){

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ResumeMainActivity.class);
                startActivity(intent);
            }
        });

        btn_saveresume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

    }
    public void updateView(){
        Context context = LocaleHelper.setLocale(getApplicationContext(), Language);
        resources = context.getResources();
        Marital_arrays = Arrays.asList(resources.getStringArray(R.array.Marital_arrays));
        Gender_arrays = Arrays.asList(resources.getStringArray(R.array.Gender_arrays));
        Province = Arrays.asList(resources.getStringArray(R.array.Province));
        JobStatus_arrays= Arrays.asList(resources.getStringArray(R.array.job_status_arrays));

        //job_status_arrays
        ArrayAdapter<String> jobadapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.job_status_arrays)
        );
        jobadapter.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_ltr);
        spinnerJob_Status.setAdapter(jobadapter);

        //PROVINCE ARRAYS
        ArrayAdapter<String> provadapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.Province)
        );
        provadapter.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_ltr);
        spinnerProvince.setAdapter(provadapter);

        //spinnerMarried_arrays
        ArrayAdapter<String> mariadapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.Marital_arrays)
        );
        mariadapter.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_ltr);
        spinnerMarried.setAdapter(mariadapter);

        //spinnerGender_arrays
        ArrayAdapter<String> genadapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.Gender_arrays)
        );
        genadapter.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_ltr);
        spinnerGender.setAdapter(genadapter);


        btn_back.setText(resources.getString(R.string.Back));
        btn_saveresume.setText(resources.getString(R.string.SaveChanges));
        txtJobTitle.setText(resources.getString(R.string.JobTitle2));
        txtBirth_YearTitle.setText(resources.getString(R.string.BirthYearResumeTitle));
        txtUserInfo.setText(resources.getString(R.string.UserInfo));
        txtJob_StatusTitle.setText(resources.getString(R.string.job_status));
        txtProvinceTitle.setText(resources.getString(R.string.ProvinceResumeTitle));
        txtMarriedTitle.setText(resources.getString(R.string.MarriageResumeTitle));
        txtGenderTitle.setText(resources.getString(R.string.GenderResumeTitle));
    }
    public void setDefaults() throws JSONException {
        EdittxtJob.setText(session.get_Resume().getJob_title());
        EdittxtBirth_Year.setText(session.get_Resume().getYear_birth());

        if(session.get_Resume().getJob_status()!=null)
        {
            spinnerJob_Status.setSelection(Integer.parseInt(session.get_Resume().getJob_status())-1);
        }
        if(session.get_Resume().getProvince()!=null)
        {
            //spinnerProvince.setSelection(Integer.parseInt(session.get_Resume().getProvince()));
            spinnerProvince.setSelection(Province.indexOf(session.get_Resume().getProvince().replace('ي', 'ی') ));
        }
        if(session.get_Resume().getMartial()!=null)
        {
            //spinnerMarried.setSelection(Integer.parseInt(session.get_Resume().getMartial()));
            spinnerMarried.setSelection(Marital_arrays.indexOf(session.get_Resume().getMartial().replace('ي', 'ی') ));
        }
        if(session.get_Resume().getSex()!=null)
        {
            //spinnerGender.setSelection(Integer.parseInt(session.get_Resume().getSex()));
            spinnerGender.setSelection(Gender_arrays.indexOf(session.get_Resume().getSex().replace('ي', 'ی') ));
        }
        //session.get_Resume().getImage() imgUserImg
    }
    public void save(){
        if(Language.equals("fa")){
            Toast.makeText(this, "در حال ارتباط با سرور...", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Connecting to server...", Toast.LENGTH_SHORT).show();
        }
        String result = session.set_Resume_UserInfo(
                EdittxtJob.getText().toString(),
                EdittxtBirth_Year.getText().toString(),
                JobStatus_arrays.indexOf(spinnerJob_Status.getSelectedItem().toString())+1+"",
                Province.indexOf(spinnerProvince.getSelectedItem().toString())+1+"",
                Marital_arrays.indexOf(spinnerMarried.getSelectedItem().toString())+1+"",
                Gender_arrays.indexOf(spinnerGender.getSelectedItem().toString())+1+"",
                "image");
        if(result.equals("ok")){
            Resume resume = new Resume(this, Province,
                    Arrays.asList(resources.getStringArray(R.array.job_status_arrays)),
                    Arrays.asList(resources.getStringArray(R.array.contract_arrays)),
                    Arrays.asList(resources.getStringArray(R.array.Senority_arrays)));
            resume.set_resume_api();
        }
    }
}
