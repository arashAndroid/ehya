package com.mrehya.Resume;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mrehya.Helper.LocaleHelper;
import com.mrehya.R;
import com.mrehya.SessionManager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ResumeBenefitsActivity extends AppCompatActivity {
    private String Language;
    private SessionManager session;
    private Resources resources;
    private Context context;
    //ELEMENTS
    LinearLayout LinearLayoutmain;
    CheckBox chkPromo,chkInsur,chkTrainingCourse,chkTransporting,chkFood,chkFlexiableHours;
    private Button btn_back, btn_saveresume;


    HashMap<String, Integer> JobcatsMap = new HashMap<String, Integer>();
    List<String> Seniority_arrays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_benefits);
        session = new SessionManager(getApplicationContext());
        Intent intent = getIntent();
        Language = intent.getStringExtra("Language");
        findviews();
        setOnclicks();
        updateView();
        try {
            setDefaults();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void findviews(){
        LinearLayoutmain=(LinearLayout) findViewById(R.id.LinearLayoutmain);
        //checkbox
        chkPromo = (CheckBox) findViewById(R.id.chkPromo);
        chkInsur = (CheckBox) findViewById(R.id.chkInsur);
        chkTrainingCourse = (CheckBox) findViewById(R.id.chkTrainingCourse);
        chkFlexiableHours = (CheckBox) findViewById(R.id.chkFlexiableHours);
        chkTransporting = (CheckBox) findViewById(R.id.chkTransporting);
        chkFood = (CheckBox) findViewById(R.id.chkFood);


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
        context = LocaleHelper.setLocale(getApplicationContext(), Language);
        resources = context.getResources();

        if(Language.equals("fa")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayoutmain.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            chkFood.setGravity(Gravity.RIGHT);
            chkInsur.setGravity(Gravity.RIGHT);
            chkPromo.setGravity(Gravity.RIGHT);
            chkTrainingCourse.setGravity(Gravity.RIGHT);
            chkTransporting.setGravity(Gravity.RIGHT);
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayoutmain.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            chkFood.setGravity(Gravity.LEFT);
            chkInsur.setGravity(Gravity.LEFT);
            chkPromo.setGravity(Gravity.LEFT);
            chkTrainingCourse.setGravity(Gravity.LEFT);
            chkTransporting.setGravity(Gravity.LEFT);
        }

        chkPromo.setText(resources.getString(R.string.chkPromo));
        chkInsur.setText(resources.getString(R.string.chkInsur));
        chkTrainingCourse.setText(resources.getString(R.string.chkTrainingCourse));
        chkTransporting.setText(resources.getString(R.string.chkTransporting));
        chkFood.setText(resources.getString(R.string.chkFood));
        chkFlexiableHours.setText(resources.getString(R.string.chkFlexiableHours));
        btn_back.setText(resources.getString(R.string.Back));
        btn_saveresume.setText(resources.getString(R.string.save));
    }
    public void setDefaults() throws JSONException {
        String s1 = session.get_Resume().getBenefits();
        String[] words=s1.split(",");
        for (String item:words
                ) {
//            BenefitsArray.contains(item); .replace("ي","ی")
            if (item.replace("ي","ی").equals("امکان ترفیع سمت".replace("ي","ی"))){
                chkPromo.setChecked(true);
            }else if(item.replace("ي","ی").equals("بیمه".replace("ي","ی"))){
                chkInsur.setChecked(true);
            }else if(item.replace("ي","ی").equals("دوره های آموزشی".replace("ي","ی"))){
                chkTrainingCourse.setChecked(true);
            } else if(item.replace("ي","ی").equals("ساعت کاری منعطف".replace("ي","ی"))){
                //Log.e("hour", "hourin");
                chkFlexiableHours.setChecked(true);
            } else if(item.replace("ي","ی").equals("سرویس رفت و آمد".replace("ي","ی"))){
                //Log.e("سرویس", "سرویس");
                chkTransporting.setChecked(true);
            } else if(item.replace("ي","ی").equals("غذا به عهده شرکت".replace("ي","ی"))){
                chkFood.setChecked(true);
            }

        }
    }
    public void save(){
        if(Language.equals("fa")){
            Toast.makeText(context, "در حال ارتباط با سرور...", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Connecting to server...", Toast.LENGTH_SHORT).show();
        }
        String result = session.set_Resume_Benefits(MakeCheckString());
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
    public String MakeCheckString(){
        String chks="";
        if (chkPromo.isChecked())
            chks+="1"+",";
        if (chkInsur.isChecked())
            chks+="2"+",";
        if (chkTrainingCourse.isChecked())
            chks+="3"+",";
        if (chkFlexiableHours.isChecked())
            chks+="4"+",";
        if (chkTransporting.isChecked())
            chks+="5"+",";
        if (chkFood.isChecked())
            chks+="6"+",";
        //Log.e("chks", chks);
        if(chks.length()>0)
            return chks.substring(0,chks.length()-1);
        else
            return "";
    }
}
