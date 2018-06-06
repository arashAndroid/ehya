package com.mrehya;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.icu.text.NumberFormat;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mrehya.Helper.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.paperdb.Paper;

public class MakeResume extends AppCompatActivity {
    String Language;
    private SessionManager session;

    ArrayList<String> listProvince;
    ArrayList<String> listJobcat;
    ArrayList<String> listTerms;
    ArrayList<String> listSkill;


    ArrayList<String> listProes;
    ArrayList<Lang> listLang;
    ArrayList<Job> listJobs;
    ArrayList<Education> listEducation;
    ListAdapterProes listAdapterProes;
    ListAdapterLang listAdapterLang;
    ListAdapterJobs listAdapterJobs;
    ListAdapterProvince listAdapterProvince;
    ListAdapterProvince listAdapterJobcat;
    ListAdapterTerms listAdapterTerms;
    ListAdapterTerms listAdapterSkill;
    ListAdapterEducation listAdapterEducation;

    //some maps
    HashMap<String, Integer> JobcatsMap = new HashMap<String, Integer>();


    TextView txtSalary,txtAboutMe;
    ImageButton btnAddProes,btnAddJobs,btnAddEducation,btnAddLang ,
            btnAddProvince,btnAddJobcat,btnAddTerms,btnAddSkill;
    ListView listViewProes,listViewJobs,listViewEducation,listViewLang,
            listViewProvince,listViewJobcat,listViewTerms,listViewSkill;
    Button btnSavechanges,btnSaveAboutMe, btnEditPersonal;

    //new

    TextView txtjob_status,txtJobTitleTitle,txtLastDegreeTitle
            ,txtPrivateInfo,txtEmailAddress,txtEmailAddressTitle,txtPhone,txtPhoneTitle,txtProvinceResume,
            txtProvinceResumeTitle,txtMarriageResume,txtMarriageResumeTitle,txtBirthYearResume,txtBirthYearResumeTitle,
            txtDutyResume,txtDutyResumeTitle,txtAddressResume,txtAddressResumeTitle,txtTalents,txtJobExp,txtGraduateExp,
            txtLanguages,txtJobFavorites,txtJobCategory,txtSelectedProvinces,txtAcceptedContract,txtActivityLevel,
            txtActualRights,txtJobBenefits,txtAboutMe2,txtGenderResumeTitle,txtGenderResume, txtNotloggedin,txtSlugTitle;

    EditText txtJobTitle,EdittxtSlug;
    Spinner spinnerjob_status, spinnerLastDegree;


    LinearLayout LinearLayoutresume1,LinearLayoutresume2,LinearLayoutresume8,
            LinearLayoutresume11,LinearLayoutresume12,LinearLayoutresume13;

    LinearLayout LinearLayoutMakeResume, LinearLayoutresumepart1,LinearLayoutresumepart2,LinearLayoutresumepart3
            ,LinearLayoutresumepart4,LinearLayoutresumepart5
            ,LinearLayoutresumepart6,LinearLayoutresumepart7,LinearLayoutresumepart8,
            LinearLayoutresumepart9,LinearLayoutresumepart10,LinearLayoutresumepart11
            ,LinearLayoutresumepart12, LinearLayoutresumepart13,LinearLayoutresumepart14;
    ImageView resumeProfilePic;


    CheckBox chkPromo,chkInsur,chkTrainingCourse,chkTransporting,chkFood,chkFlexiableHours;


    Spinner spinnerDesiredIncome;

    //DIALOG PERSONAL INFO
    TextView txtPIEditPersonalInfo, txtPIEmailAddress,
            txtPIMobile, txtPIProvince, txtPIBirthYear, txtPIAddress, txtPIMarital, txtPIGender, txtPIMilitaryservice;
    EditText txtPIEditEmailAddress,txtPIEditMobile,txtPIEditProvince,txtPIEditBirthYear,txtPIEditAddress;
    Spinner spinnerPIMarital ,spinnerPIGender ,spinnerPIMilitaryservice, spinnerPIProvince;
    Button btnPI_save_editpersoalinfo;


    /////global arraylist

    List<String> Militaryservice_arrays;
    List<String> Marital_arrays;
    List<String> Gender_arrays ;
    List<String> Province;
    List<String> Seniority_arrays;
    List<String> JobStatus_arrays;
    List<String> Salary_arrays;
    List<String> BenefitsArray;

    //progress
    private ProgressDialog StartDialog;


    //Linear Layouts
    LinearLayout LinearLayoutDegree;
    LinearLayout LinearLayoutJobEduLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_resume);

        session = new SessionManager(getApplicationContext());
        Language = updateLanguage();

        StartDialog = new ProgressDialog(this);
        updateResource(Language);
        LinearLayoutMakeResume = (LinearLayout) findViewById(R.id.LinearLayoutMakeResume);
        txtNotloggedin = (TextView) findViewById(R.id.txtNotloggedin);

        //LOAD USERS DEFAULT RESUME
        if(session.isLoggedIn())
        {
            txtNotloggedin.setVisibility(View.GONE);
            LinearLayoutMakeResume.setVisibility(View.VISIBLE);
            startDialog(StartDialog,"بارگذاری اطلاعات کاربری","Loading user information",1,false);
            StartDialog.setProgress(10);
            get_credentials(session.getUserDetails().getToken(), session.getUserDetails().getEmail(), session.getUserDetails().getPassword());
        }
        else{
            LinearLayoutMakeResume.setVisibility(View.GONE);
            txtNotloggedin.setVisibility(View.VISIBLE);
        }
    }

    public void updateResource(String language){
        Context context = LocaleHelper.setLocale(getApplicationContext(), language);
        Resources resources = context.getResources();

        Militaryservice_arrays = Arrays.asList(resources.getStringArray(R.array.Militaryservice_arrays));
        Marital_arrays = Arrays.asList(resources.getStringArray(R.array.Marital_arrays));
        Gender_arrays = Arrays.asList(resources.getStringArray(R.array.Gender_arrays));
        Province = Arrays.asList(resources.getStringArray(R.array.Province));
        Seniority_arrays= Arrays.asList(resources.getStringArray(R.array.Senority_arrays));
        JobStatus_arrays= Arrays.asList(resources.getStringArray(R.array.job_status_arrays));
        Salary_arrays= Arrays.asList(resources.getStringArray(R.array.DesiredIncome_arrays));
        BenefitsArray= Arrays.asList(resources.getStringArray(R.array.benefitArray));


    }
    public void start(){
        StartDialog.setProgress(60);
        setViews();
        setProesList();
        setLangList();
        setJobsList();
        StartDialog.setProgress(70);
        setEducationList();
        setProvinceList();
        setJobcatList();
        StartDialog.setProgress(80);
        setTermsList();
        setSkillList();



        listViewProes.setAdapter(listAdapterProes);
        listViewLang.setAdapter(listAdapterLang);
        listViewJobs.setAdapter(listAdapterJobs);
        listViewEducation.setAdapter(listAdapterEducation);
        listViewProvince.setAdapter(listAdapterProvince);
        listViewJobcat.setAdapter(listAdapterJobcat);
        listViewTerms.setAdapter(listAdapterTerms);
        listViewSkill.setAdapter(listAdapterSkill);


        justifyListViewHeightBasedOnChildren(listViewProes);
        justifyListViewHeightBasedOnChildren(listViewJobs);
        justifyListViewHeightBasedOnChildren(listViewEducation);
        justifyListViewHeightBasedOnChildren(listViewLang);
        justifyListViewHeightBasedOnChildren(listViewProvince);
        justifyListViewHeightBasedOnChildren(listViewJobcat);
        justifyListViewHeightBasedOnChildren(listViewTerms);
        justifyListViewHeightBasedOnChildren(listViewSkill);

        btnAddProes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddProes(listViewProes);
            }
        });
        btnAddJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddJobs(listViewJobs);
            }
        });
        btnAddEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddEducation(listViewEducation);
            }
        });
        btnAddLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddLang(listViewLang);
            }
        });

        btnAddProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddProvince(listViewProvince);
            }
        });

        btnAddJobcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddJobcat(listViewJobcat);
            }
        });

        btnAddTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddTerms(listViewTerms);
            }
        });

        btnAddSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddSkill(listViewSkill);
            }
        });

        btnSaveAboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogEditAbout(txtAboutMe.getText().toString());
            }
        });
        btnEditPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MakeResume.this);
                dialog.setContentView(R.layout.dialog_editpersonal_info);

                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;


                //get elements
                txtPIEditPersonalInfo       = (TextView) dialog.findViewById(R.id.txtEditPersonalInfo);
                txtPIEmailAddress           = (TextView) dialog.findViewById(R.id.txtEmailAddress);
                txtPIMobile                 = (TextView) dialog.findViewById(R.id.txtMobile);
                txtPIProvince               = (TextView) dialog.findViewById(R.id.txtProvince);
                txtPIBirthYear              = (TextView) dialog.findViewById(R.id.txtBirthYear);
                txtPIAddress                = (TextView) dialog.findViewById(R.id.txtAddress);
                txtPIMarital                = (TextView) dialog.findViewById(R.id.txtMarital);
                txtPIGender                 = (TextView) dialog.findViewById(R.id.txtGender);
                txtPIMilitaryservice        = (TextView) dialog.findViewById(R.id.txtMilitaryservice);
                txtPIEditEmailAddress       = (EditText) dialog.findViewById(R.id.txtEditEmailAddress);
                txtPIEditMobile             = (EditText) dialog.findViewById(R.id.txtEditMobile);
                txtPIEditBirthYear          = (EditText) dialog.findViewById(R.id.txtEditBirthYear);
                txtPIEditAddress            = (EditText) dialog.findViewById(R.id.txtEditAddress);
                spinnerPIMarital            = (Spinner) dialog.findViewById(R.id.spinnerMarital);
                spinnerPIGender             = (Spinner) dialog.findViewById(R.id.spinnerGender);
                spinnerPIMilitaryservice    = (Spinner) dialog.findViewById(R.id.spinnerMilitaryservice);
                spinnerPIProvince            = (Spinner) dialog.findViewById(R.id.spinnerPIProvince);
                btnPI_save_editpersoalinfo  = (Button) dialog.findViewById(R.id.btn_save_editpersoalinfo);

                StartDialog.setProgress(90);
                update_personal_info_dialog(Language);

                //set elements
                txtPIEditEmailAddress.setText(txtEmailAddress.getText());
                txtPIEditMobile.setText(txtPhone.getText());
                txtPIEditBirthYear.setText(txtBirthYearResume.getText());
                txtPIEditAddress.setText(txtAddressResume.getText());

                spinnerPIProvince.post(new Runnable() {
                    @Override
                    public void run() {
                        spinnerPIProvince.setSelection(Province.indexOf(txtProvinceResume.getText()));
                    }
                });
                spinnerPIMarital.post(new Runnable() {
                    @Override
                    public void run() {
                        spinnerPIMarital.setSelection(Marital_arrays.indexOf(txtMarriageResume.getText()));
                    }
                });
                spinnerPIMilitaryservice.post(new Runnable() {
                    @Override
                    public void run() {
                        spinnerPIMilitaryservice.setSelection(Militaryservice_arrays.indexOf(txtDutyResume.getText()));
                    }
                });
                spinnerPIGender.post(new Runnable() {
                    @Override
                    public void run() {
                        spinnerPIGender.setSelection(Gender_arrays.indexOf(txtGenderResume.getText()));
                    }
                });

                btnPI_save_editpersoalinfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //set Texts
                        txtPhone.setText(txtPIEditMobile.getText());
                        txtEmailAddress.setText(txtPIEditEmailAddress.getText());
                        txtBirthYearResume.setText(txtPIEditBirthYear.getText());
                        txtAddressResume.setText(txtPIEditAddress.getText());
                        txtProvinceResume.setText(spinnerPIProvince.getSelectedItem().toString());
                        txtDutyResume.setText(spinnerPIMilitaryservice.getSelectedItem().toString());
                        txtMarriageResume.setText(spinnerPIMarital.getSelectedItem().toString());
                        txtGenderResume.setText(spinnerPIGender.getSelectedItem().toString());

                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout((6 * width)/7, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        btnSavechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                make_resume_api();
            }
        });
        StartDialog.setProgress(100);
        updateView(Language);
        get_resume_api();
        hideDialog(StartDialog);


    }
    private void setViews(){
        //Linear layouts
        LinearLayoutDegree = (LinearLayout) findViewById(R.id.LinearLayoutDegree);
        LinearLayoutJobEduLang = (LinearLayout) findViewById(R.id.LinearLayoutJobEduLang);

        int resume = Integer.getInteger(session.getUserDetails().getResume());
        if(session.getUserDetails().getResume()== null || resume==0)
        {
            Log.d("resume1", "resume1: " + resume);
            hideshow(1);
        }
        else{
            Log.d("resume2",resume+"");
            hideshow(2);
        }
        //ListViews
        listViewProes = (ListView) findViewById(R.id.listViewProes);
        listViewJobs = (ListView) findViewById(R.id.listViewJobs);
        listViewEducation = (ListView) findViewById(R.id.listViewEducation);
        listViewLang = (ListView) findViewById(R.id.listViewLang);
        listViewProvince = (ListView) findViewById(R.id.listViewProvince);
        listViewJobcat = (ListView) findViewById(R.id.listViewJobcat);
        listViewTerms = (ListView) findViewById(R.id.listViewTerms);
        listViewSkill = (ListView) findViewById(R.id.listViewSkill);


        //checkbox
        chkPromo = (CheckBox) findViewById(R.id.chkPromo);
        chkInsur = (CheckBox) findViewById(R.id.chkInsur);
        chkTrainingCourse = (CheckBox) findViewById(R.id.chkTrainingCourse);
        chkTransporting = (CheckBox) findViewById(R.id.chkTransporting);
        chkFood = (CheckBox) findViewById(R.id.chkFood);
        chkFlexiableHours = (CheckBox) findViewById(R.id.chkFlexiableHours);

        //Buttons
        btnAddProes = (ImageButton) findViewById(R.id.btnAddProes);
        btnAddJobs = (ImageButton) findViewById(R.id.btnAddJobs);
        btnAddEducation= (ImageButton) findViewById(R.id.btnAddEducation);
        btnAddLang= (ImageButton) findViewById(R.id.btnAddLang);
        btnAddProvince = (ImageButton) findViewById(R.id.btnAddProvince);
        btnAddJobcat = (ImageButton) findViewById(R.id.btnAddJobcat);
        btnAddTerms = (ImageButton) findViewById(R.id.btnAddTerms);
        btnAddSkill = (ImageButton) findViewById(R.id.btnAddSkill);
        btnSavechanges = (Button) findViewById(R.id.btnSavechanges);
        btnSaveAboutMe = (Button) findViewById(R.id.btnSaveAboutMe);
        btnEditPersonal= (Button) findViewById(R.id.btnEditPersonal);
        // txtSalary = (TextView) findViewById(R.id.txtSalary);
        txtAboutMe = (TextView) findViewById(R.id.txtAboutMe);


        //new
        txtJobTitle= (EditText) findViewById(R.id.txtJobTitle);

        spinnerLastDegree= (Spinner) findViewById(R.id.spinnerLastDegree);
        spinnerjob_status= (Spinner) findViewById(R.id.spinnerjob_status);

        txtJobTitleTitle= (TextView) findViewById(R.id.txtJobTitleTitle);
        txtjob_status= (TextView) findViewById(R.id.txtjob_status);
        txtLastDegreeTitle= (TextView) findViewById(R.id.txtLastDegreeTitle);
        txtPrivateInfo= (TextView) findViewById(R.id.txtPrivateInfo);
        txtEmailAddress= (TextView) findViewById(R.id.txtEmailAddress);
        txtEmailAddressTitle= (TextView) findViewById(R.id.txtEmailAddressTitle);
        txtPhone= (TextView) findViewById(R.id.txtPhone);
        txtPhoneTitle= (TextView) findViewById(R.id.txtPhoneTitle);
        txtProvinceResume= (TextView) findViewById(R.id.txtProvinceResume);
        txtProvinceResumeTitle= (TextView) findViewById(R.id.txtProvinceResumeTitle);
        txtMarriageResume= (TextView) findViewById(R.id.txtMarriageResume);
        txtMarriageResumeTitle= (TextView) findViewById(R.id.txtMarriageResumeTitle);
        txtBirthYearResume= (TextView) findViewById(R.id.txtBirthYearResume);
        txtBirthYearResumeTitle= (TextView) findViewById(R.id.txtBirthYearResumeTitle);
        txtDutyResume= (TextView) findViewById(R.id.txtDutyResume);
        txtDutyResumeTitle= (TextView) findViewById(R.id.txtDutyResumeTitle);
        txtAddressResume= (TextView) findViewById(R.id.txtAddressResume);
        txtAddressResumeTitle= (TextView) findViewById(R.id.txtAddressResumeTitle);
        txtTalents= (TextView) findViewById(R.id.txtTalents);
        txtJobExp= (TextView) findViewById(R.id.txtJobExp);
        txtGraduateExp= (TextView) findViewById(R.id.txtGraduateExp);
        txtLanguages= (TextView) findViewById(R.id.txtLanguages);
        txtJobFavorites= (TextView) findViewById(R.id.txtJobFavorites);
        txtSelectedProvinces= (TextView) findViewById(R.id.txtSelectedProvinces);
        txtJobCategory= (TextView) findViewById(R.id.txtJobCategory);
        txtAcceptedContract= (TextView) findViewById(R.id.txtAcceptedContract);
        txtActivityLevel= (TextView) findViewById(R.id.txtActivityLevel);
        txtActualRights= (TextView) findViewById(R.id.txtActualRights);
        txtJobBenefits= (TextView) findViewById(R.id.txtJobBenefits);
        txtAboutMe2= (TextView) findViewById(R.id.txtAboutMe2);
        txtGenderResumeTitle= (TextView) findViewById(R.id.txtGenderResumeTitle);
        txtGenderResume= (TextView) findViewById(R.id.txtGenderResume);
        txtSlugTitle= (TextView) findViewById(R.id.txtSlugTitle);

        EdittxtSlug = (EditText) findViewById(R.id.EdittxtSlug);

        //LinearLayout
        LinearLayoutresume1 = (LinearLayout) findViewById(R.id.LinearLayoutresume1);
        LinearLayoutresume2 = (LinearLayout) findViewById(R.id.LinearLayoutresume2);

        //LinearLayoutparts
        LinearLayoutresumepart1 =(LinearLayout) findViewById(R.id.LinearLayoutresumepart1);
        LinearLayoutresumepart2 =(LinearLayout) findViewById(R.id.LinearLayoutresumepart2);
        LinearLayoutresumepart3 =(LinearLayout) findViewById(R.id.LinearLayoutresumepart3);
        LinearLayoutresumepart4 =(LinearLayout) findViewById(R.id.LinearLayoutresumepart4);
        LinearLayoutresumepart4 =(LinearLayout) findViewById(R.id.LinearLayoutresumepart4);
        LinearLayoutresumepart5 =(LinearLayout) findViewById(R.id.LinearLayoutresumepart5);
        LinearLayoutresumepart6 =(LinearLayout) findViewById(R.id.LinearLayoutresumepart6);
        LinearLayoutresumepart7=(LinearLayout) findViewById(R.id.LinearLayoutresumepart7);
        LinearLayoutresumepart8=(LinearLayout) findViewById(R.id.LinearLayoutresumepart8);
        LinearLayoutresumepart9=(LinearLayout) findViewById(R.id.LinearLayoutresumepart9);
        LinearLayoutresumepart10=(LinearLayout) findViewById(R.id.LinearLayoutresumepart10);
        LinearLayoutresumepart11=(LinearLayout) findViewById(R.id.LinearLayoutresumepart11);
        LinearLayoutresumepart12=(LinearLayout) findViewById(R.id.LinearLayoutresumepart12);
        LinearLayoutresumepart13=(LinearLayout) findViewById(R.id.LinearLayoutresumepart13);
        LinearLayoutresumepart14=(LinearLayout) findViewById(R.id.LinearLayoutresumepart14);

        spinnerDesiredIncome=(Spinner) findViewById(R.id.spinnerDesiredIncome);


        //Image View
        resumeProfilePic = (ImageView) findViewById(R.id.resumeProfilePic);

        //set default values

//        txtPhoneTitle.setText(txtPIEditMobile.getText());
//        txtProvinceResume.setText(txtPIEditProvince.getText());
//        txtEmailAddress.setText(txtPIEditEmailAddress.getText());
//        txtBirthYearResume.setText(txtPIEditBirthYear.getText());
//        txtAddressResume.setText(txtPIEditAddress.getText());
//        txtDutyResume.setText(spinnerPIMilitaryservice.getSelectedItem().toString());
//        txtMarriageResume.setText(spinnerPIMarital.getSelectedItem().toString());
//        txtGenderResume.setText(spinnerPIGender.getSelectedItem().toString());

//        listViewProes

//        Job job = new Job("۱۳۹۰","۱۳۹۶","مدیر شرکت","نما اندیشان");
//        listJobs.add(job);


//        Education edu = new Education("مهندس نرم‌افزار","دانشگاه زنجان","۱۳۹۲","۱۳۹۶");
//        listEducation.add(edu);


//        Lang lang = new Lang("نگلیسی","متوسط");
//        listLang.add(lang);

//        listProvince.add("زنگان");


        //       listJobcat.add("دسته");
        //listTerms.add("تماموقت۱");
        //     listSkill.add("مدیر");
    }

    //****Setting the List Items****//
    private void setProesList(){
        listProes = new ArrayList<>();
        listAdapterProes = new ListAdapterProes(listProes,getApplicationContext(),MakeResume.this,listViewProes);

    }
    private void setLangList(){
        listLang = new ArrayList<>();
//        Lang lang = new Lang("نگلیسی","متوسط");
//        listLang.add(lang);
        listAdapterLang = new ListAdapterLang(listLang,getApplicationContext(),MakeResume.this,listViewLang);

    }
    private void setJobsList(){
        listJobs = new ArrayList<>();
//        Job job = new Job("۱۳۹۰","۱۳۹۶","مدیر شرکت","نما اندیشان");
//        listJobs.add(job);
        listAdapterJobs = new ListAdapterJobs(listJobs,getApplicationContext(),MakeResume.this,listViewJobs);
    }
    private void setEducationList(){
        listEducation = new ArrayList<>();
//        Education edu = new Education("مهندس نرم‌افزار","دانشگاه زنجان","۱۳۹۲","۱۳۹۶");
//        listEducation.add(edu);
        listAdapterEducation = new ListAdapterEducation(listEducation,getApplicationContext(),MakeResume.this,listViewEducation);
    }
    private void setProvinceList(){
        listProvince = new ArrayList<>();
//        listProvince.add("زنگان");
        listAdapterProvince = new ListAdapterProvince(listProvince,getApplicationContext(),MakeResume.this,listViewProvince);

    }
    private void setJobcatList(){
        listJobcat = new ArrayList<>();
        //listJobcat.add("دسته");
        listAdapterJobcat = new ListAdapterProvince(listJobcat,getApplicationContext(),MakeResume.this,listViewJobcat);

    }
    private void setTermsList(){
        listTerms = new ArrayList<>();
//        listTerms.add("تماموقت۱");
        listAdapterTerms = new ListAdapterTerms(listTerms,getApplicationContext(),MakeResume.this,listViewTerms);

    }
    private void setSkillList(){
        listSkill = new ArrayList<>();
//        listSkill.add("مدیر");
        listAdapterSkill = new ListAdapterTerms(listSkill,getApplicationContext(),MakeResume.this,listViewSkill);

    }

    //****Show the Add Dialogs****//
    private void showDialogAddProes(final ListView listView){
        final Dialog dialog = new Dialog(MakeResume.this);
        dialog.setContentView(R.layout.list_dialog_proes);
        final EditText txtEdit = dialog.findViewById(R.id.txtEditDialogPro);
        ImageButton btnEdit = dialog.findViewById(R.id.btnEditDialogPro);
        TextView btnEditd = dialog.findViewById(R.id.btnEditd);
        if(Language.equals("fa"))
            btnEditd.setText("ویرایش");
        else
            btnEditd.setText("Edit");
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listProes.add(txtEdit.getText().toString());
                listAdapterProes.notifyDataSetChanged();
                justifyListViewHeightBasedOnChildren(listView);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout((6 * width)/7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    private void showDialogAddJobs(final ListView listView){
        final Dialog dialog = new Dialog(MakeResume.this);
        dialog.setContentView(R.layout.list_dialog_jobs);
        final EditText txtEditRole = dialog.findViewById(R.id.txtEditDialogJobRole);
        final EditText txtEditCompany = dialog.findViewById(R.id.txtEditDialogJobCompany);
        final Spinner txtEditFrom = dialog.findViewById(R.id.spinnerFromJob);
        final Spinner txtEditTo = dialog.findViewById(R.id.spinnerToJob);
        final EditText txtEditJobtitle = dialog.findViewById(R.id.txtEditDialogJobtitle);

        LinearLayout LinearLayoutresumedialogJob = dialog.findViewById(R.id.LinearLayoutresumedialogJob);
        LinearLayout LinearLayoutresumedialogJob2 = dialog.findViewById(R.id.LinearLayoutresumedialogJob2);
        LinearLayout LinearLayoutresumedialogJob3 = dialog.findViewById(R.id.LinearLayoutresumedialogJob3);

        TextView btnEditd = dialog.findViewById(R.id.btnEditd);
        TextView txtpost = dialog.findViewById(R.id.txtpost);
        TextView txtcompany = dialog.findViewById(R.id.txtcompany);
        TextView txtFrom = dialog.findViewById(R.id.txtFrom);
        TextView txtuntil = dialog.findViewById(R.id.txtuntil);


        Context context = LocaleHelper.setLocale(getApplicationContext(), Language);
        Resources resources = context.getResources();
        if(Language.equals("fa"))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayoutresumedialogJob.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutresumedialogJob2.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutresumedialogJob3.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            btnEditd.setText("ثبت و ذخیره");
        }

        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayoutresumedialogJob.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutresumedialogJob2.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutresumedialogJob3.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            btnEditd.setText("Save");
        }
        txtpost.setText(resources.getString(R.string.post));
        txtcompany.setText(resources.getString(R.string.Company));
        txtFrom.setText(resources.getString(R.string.From));
        txtuntil.setText(resources.getString(R.string.until));



        ImageButton btnEdit = dialog.findViewById(R.id.btnEditDialogJob);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(existsexperience(txtEditFrom.getSelectedItem().toString(),txtEditTo.getSelectedItem().toString(),txtEditRole.getText().toString(),txtEditCompany.getText().toString())))
                {
                    Job job = new Job(txtEditJobtitle.getText().toString(),txtEditFrom.getSelectedItem().toString(),txtEditTo.getSelectedItem().toString(),txtEditRole.getText().toString(),txtEditCompany.getText().toString());
                    job.setId(0);
                    listJobs.add(job);
                    listAdapterJobs.notifyDataSetChanged();
                    justifyListViewHeightBasedOnChildren(listView);
                }
            }
        });
        dialog.show();
        dialog.getWindow().setLayout((6 * width)/7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    private void showDialogAddLang(final ListView listView){
        final Dialog dialog = new Dialog(MakeResume.this);
        dialog.setContentView(R.layout.list_dialog_lang);
        //final EditText txtEdit = dialog.findViewById(R.id.txtEditDialogLang);
        final Spinner spinnerLevel = dialog.findViewById(R.id.spinnerLangLevel);
        final Spinner spinnerLang = dialog.findViewById(R.id.spinnerLang);

        LinearLayout LinearLayout1 = dialog.findViewById(R.id.LinearLayout1);

        TextView btnEditd = dialog.findViewById(R.id.btnEditd);
        if(Language.equals("fa"))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayout1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            btnEditd.setText("ثبت و ذخیره");
        }

        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayout1.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            btnEditd.setText("Save");
        }

        Context context = LocaleHelper.setLocale(getApplicationContext(), Language);
        Resources resources = context.getResources();
        ArrayAdapter<String> Leveladapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.langLevels)
        );
        spinnerLevel.setAdapter(Leveladapter);

        ArrayAdapter<String> Langadapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.langs)
        );
        spinnerLang.setAdapter(Langadapter);

        ImageButton btnEdit = dialog.findViewById(R.id.btnEditDialogLang);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(existslang(spinnerLang.getSelectedItem().toString(),spinnerLevel.getSelectedItem().toString()))){
                    Lang lang = new Lang(spinnerLang.getSelectedItem().toString(),spinnerLevel.getSelectedItem().toString());
                    lang.setId(0);
                    listLang.add(lang);
                    listAdapterLang.notifyDataSetChanged();
                    justifyListViewHeightBasedOnChildren(listView);
                }
            }
        });
        dialog.show();
        dialog.getWindow().setLayout((6 * width)/7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    private void showDialogAddEducation(final ListView listView){
        final Dialog dialog = new Dialog(MakeResume.this);
        dialog.setContentView(R.layout.list_dialog_education);
        final EditText txtEditField = dialog.findViewById(R.id.txtEditDialogEducationField);
        final EditText txtEditPlace = dialog.findViewById(R.id.txtEditDialogEducationPlace);
        final Spinner txtEditFrom = dialog.findViewById(R.id.spinnerFromEducation);
        final Spinner txtEditTo = dialog.findViewById(R.id.spinnerToEducation);
        final Spinner txtEditgrade = dialog.findViewById(R.id.spinnerFromEducation);

        TextView txtmajor = dialog.findViewById(R.id.txtmajor);
        TextView txteducationplace = dialog.findViewById(R.id.txteducationplace);
        TextView txtFrom = dialog.findViewById(R.id.txtFrom);
        TextView txtuntil = dialog.findViewById(R.id.txtunitl);
        LinearLayout LinearLayoutresumedialogEdu = dialog.findViewById(R.id.LinearLayoutresumedialogEdu);
        LinearLayout LinearLayoutresumedialogEdu2 = dialog.findViewById(R.id.LinearLayoutresumedialogEdu2);
        LinearLayout LinearLayoutresumedialogEdu3 = dialog.findViewById(R.id.LinearLayoutresumedialogEdu3);

        TextView btnEditd = dialog.findViewById(R.id.btnEditd);
        if(Language.equals("fa"))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayoutresumedialogEdu.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutresumedialogEdu2.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutresumedialogEdu3.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            btnEditd.setText("ثبت و ذخیره");
        }

        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayoutresumedialogEdu.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutresumedialogEdu2.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutresumedialogEdu3.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            btnEditd.setText("Save");
        }

        ImageButton btnEdit = dialog.findViewById(R.id.btnEditDialogEducation);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(existsacademic(txtEditgrade.getSelectedItem().toString(), txtEditField.getText().toString(),txtEditPlace.getText().toString(),txtEditFrom.getSelectedItem().toString(),txtEditTo.getSelectedItem().toString())))
                {
                    Education edu = new Education(txtEditgrade.getSelectedItem().toString(), txtEditField.getText().toString(),txtEditPlace.getText().toString(),txtEditFrom.getSelectedItem().toString(),txtEditTo.getSelectedItem().toString());
                    edu.setId(0);
                    listEducation.add(edu);
                    listAdapterEducation.notifyDataSetChanged();
                    justifyListViewHeightBasedOnChildren(listView);
                }

            }
        });

        Context context = LocaleHelper.setLocale(getApplicationContext(), Language);
        Resources resources = context.getResources();

        txtmajor.setText(resources.getString(R.string.major));
        txteducationplace.setText(resources.getString(R.string.educationplace));
        txtFrom.setText(resources.getString(R.string.From));
        txtuntil.setText(resources.getString(R.string.until));
        dialog.show();
        dialog.getWindow().setLayout((6 * width)/7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    private void showDialogAddJobcat(final ListView listView){
        final Dialog dialog = new Dialog(MakeResume.this);
        dialog.setContentView(R.layout.list_dialog_jobcat);
        //final TextView txtJobcat = dialog.findViewById(R.id.txtEditDialogJobcat);
        final ImageButton txtedit = dialog.findViewById(R.id.btnEditDialogJobcat);
        final Spinner spinnerJobcats = dialog.findViewById(R.id.spinnerJobcats);
        final LinearLayout LinearLayout1 = dialog.findViewById(R.id.LinearLayout1);

        TextView btnEditd = dialog.findViewById(R.id.btnEditd);
        if(Language.equals("fa"))
            btnEditd.setText("ویرایش");
        else
            btnEditd.setText("Edit");

        if(Language.equals("fa"))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayout1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            btnEditd.setText("ویرایش");
        }

        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayout1.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            btnEditd.setText("Edit");
        }

        if(JobcatsMap.size()<=0){
            JobCats_Api();
            String[] list = new String[1];
            list[0] =  "Empty";
            ArrayAdapter<String> Jobcatadapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    list
            );
            spinnerJobcats.setAdapter(Jobcatadapter);

        }
        else
        {
            String[] urlArray = new String[JobcatsMap.size()];
            JobcatsMap.keySet().toArray(urlArray);
            ArrayAdapter<String> Jobcatadapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    urlArray
            );
            spinnerJobcats.setAdapter(Jobcatadapter);
        }



        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;


        txtedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(listJobcat.isEmpty())) {
                    if (!(listJobcat.contains(spinnerJobcats.getSelectedItem().toString()))){
                        listJobcat.add(spinnerJobcats.getSelectedItem().toString());
                        Log.d("TAG", "spinners item"+spinnerJobcats.getSelectedItem());
                    }
                }
                else
                    listJobcat.add(spinnerJobcats.getSelectedItem().toString());
                Log.d("TAG", "spinners item"+spinnerJobcats.getSelectedItem());


                listAdapterJobcat.notifyDataSetChanged();
                justifyListViewHeightBasedOnChildren(listView);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout((6 * width)/7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    private void showDialogAddProvince(final ListView listView){
        final Dialog dialog = new Dialog(MakeResume.this);
        dialog.setContentView(R.layout.list_dialog_provice);
        final Spinner spinnerProvince = dialog.findViewById(R.id.spinnerProvince);
        final ImageButton txtedit = dialog.findViewById(R.id.btnEditDialogProvince);

        TextView btnEditd = dialog.findViewById(R.id.btnEditd);


        Context context = LocaleHelper.setLocale(getApplicationContext(), Language);
        Resources resources = context.getResources();
        if(Language.equals("fa"))
        {
            btnEditd.setText("ویرایش");
        }

        else
        {
            btnEditd.setText("Edit");
        }

        ArrayAdapter<String> Province_array = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.Province)
        );
        spinnerProvince.setAdapter(Province_array);



        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;


        txtedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String province = spinnerProvince.getSelectedItem().toString();
                listProvince.add(province);
                listAdapterProvince.notifyDataSetChanged();
                justifyListViewHeightBasedOnChildren(listView);
                dialog.dismiss();


            }
        });

        dialog.show();
        dialog.getWindow().setLayout((6 * width)/7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    private void showDialogAddTerms(final ListView listView){
        final Dialog dialog = new Dialog(MakeResume.this);
        dialog.setContentView(R.layout.list_dialog_terms);
        final Spinner spinnerTerms = dialog.findViewById(R.id.spinnerTerms);
        final ImageButton txtedit = dialog.findViewById(R.id.btnEditDialogTerms);

        TextView btnEditd = dialog.findViewById(R.id.btnEditd);

        Context context = LocaleHelper.setLocale(getApplicationContext(), Language);
        Resources resources = context.getResources();
        if(Language.equals("fa"))
        {
            btnEditd.setText("ویرایش");
        }

        else
        {
            btnEditd.setText("Edit");
        }

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;


        ArrayAdapter<String> contract_array = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.contract_arrays)
        );
        spinnerTerms.setAdapter(contract_array);

        txtedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean exists=false;

                String Terms = spinnerTerms.getSelectedItem().toString();

                for (int i=0 ;i<listTerms.size();i++){
                    if (Terms.equalsIgnoreCase(listTerms.get(i))){
                        exists=true;
                        break;
                    }
                }
                if(!exists) {
                    listTerms.add(Terms);
                    listAdapterTerms.notifyDataSetChanged();
                    justifyListViewHeightBasedOnChildren(listView);
                }
                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.getWindow().setLayout((6 * width)/7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    private void showDialogAddSkill(final ListView listView){
        final Dialog dialog = new Dialog(MakeResume.this);
        dialog.setContentView(R.layout.list_dialog_skill);
        final Spinner spinnerskill = dialog.findViewById(R.id.spinnerskill);
        //final EditText txtEdit = dialog.findViewById(R.id.txtEditDialogskill);

        TextView btnEditd = dialog.findViewById(R.id.btnEditd);
        if(Language.equals("fa"))
            btnEditd.setText("ویرایش");
        else
            btnEditd.setText("Edit");

        ImageButton btnEdit = dialog.findViewById(R.id.btnEditDialogSkill);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;


        Context context = LocaleHelper.setLocale(getApplicationContext(), Language);
        Resources resources = context.getResources();
        if(Language.equals("fa"))
        {
            btnEditd.setText("ویرایش");
        }

        else
        {
            btnEditd.setText("Edit");
        }

        ArrayAdapter<String> skill_array = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                Seniority_arrays
        );
        spinnerskill.setAdapter(skill_array);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(listSkill.isEmpty()))
                    listSkill.clear();
                listSkill.add(spinnerskill.getSelectedItem().toString());
                listAdapterSkill.notifyDataSetChanged();
                justifyListViewHeightBasedOnChildren(listView);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout((6 * width)/7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    private void showDialogEditSalary(String oldSalary){
        final Dialog dialog = new Dialog(MakeResume.this);
        dialog.setContentView(R.layout.list_dialog_salary);
        final EditText txtEditDialogSalary = dialog.findViewById(R.id.txtEditDialogSalary);
        final ImageButton txtedit = dialog.findViewById(R.id.btnEditDialogSalary);
        txtEditDialogSalary.setText(oldSalary);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        TextView btnEditd = dialog.findViewById(R.id.btnEditd);
        if(Language.equals("fa"))
            btnEditd.setText("ویرایش");
        else
            btnEditd.setText("Edit");

        txtedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtSalary.setText(txtEditDialogSalary.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout((6 * width)/7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    private void showDialogEditAbout(String oldAbout){
        final Dialog dialog = new Dialog(MakeResume.this);
        dialog.setContentView(R.layout.list_dialog_about);
        final EditText txtEditDialogAbout = dialog.findViewById(R.id.txtEditDialogAbout);
        final ImageButton txtedit = dialog.findViewById(R.id.btnEditDialogAbout);
        txtEditDialogAbout.setText(oldAbout);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        TextView btnEditd = dialog.findViewById(R.id.btnEditd);
        if(Language.equals("fa"))
            btnEditd.setText("ویرایش");
        else
            btnEditd.setText("Edit");

        txtedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // txtSalary.setText(txtEditDialogAbout.getText().toString());
                txtAboutMe.setText(txtEditDialogAbout.getText());
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout((13 * width)/14, ViewGroup.LayoutParams.WRAP_CONTENT);
    }



    //new
    private String updateLanguage(){
        //Default language is fa
        String language = Paper.book().read("language");
        if(language==null)
            Paper.book().write("language", "fa");
        return language;
    }
    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(getApplicationContext(), language);
        Resources resources = context.getResources();

        //CHECK ANDROID VERSION
        //condition to check language
        if(Paper.book().read("language").equals("fa")) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                LinearLayoutMakeResume.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                LinearLayoutresume1.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutresume1.setGravity(Gravity.RIGHT);

                LinearLayoutresume2.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutresume2.setGravity(Gravity.RIGHT);

                LinearLayoutresumepart12.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutresume2.setGravity(Gravity.RIGHT);

                chkFlexiableHours.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                chkFood.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                chkInsur.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                chkPromo.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                chkTrainingCourse.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                chkTransporting.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                ///////////////////////////////////RelativeLayout1//////////////////////////////////////
                //resumeProfilePic
                RelativeLayout.LayoutParams resumeProfilePicparams = (RelativeLayout.LayoutParams)resumeProfilePic.getLayoutParams();
                resumeProfilePicparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                resumeProfilePic.setLayoutParams(resumeProfilePicparams);

                //LinearLayoutresume1
                RelativeLayout.LayoutParams LinearLayoutresume1params = (RelativeLayout.LayoutParams)LinearLayoutresume1.getLayoutParams();
                LinearLayoutresume1params.addRule(RelativeLayout.LEFT_OF, R.id.resumeProfilePic);
                LinearLayoutresume1params.setMargins(1,0,10,0);
                LinearLayoutresume1.setLayoutParams(LinearLayoutresume1params);



                ///////////////////////////////////LinearLayoutresumeParts//////////////////////////////////////
                LinearLayout.LayoutParams LinearLayoutresumepartparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayoutresumepartparams.gravity=Gravity.RIGHT;
                LinearLayoutresumepart1.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart2.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart3.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart4.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart5.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart6.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart7.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart8.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart9.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart10.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart11.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart12.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart13.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart14.setLayoutParams(LinearLayoutresumepartparams);

                spinnerjob_status.setGravity(Gravity.RIGHT);
                spinnerDesiredIncome.setGravity(Gravity.RIGHT);

                Set_Texts("fa",resources,17);
            }
            //LANGUAGE IS FA
            //ELSE IF API IS UNDER 17
            else{
                spinnerjob_status.setGravity(Gravity.RIGHT);
                spinnerDesiredIncome.setGravity(Gravity.RIGHT);

                LinearLayoutresume1.setGravity(Gravity.RIGHT);
                LinearLayoutresume2.setGravity(Gravity.RIGHT);


                chkFlexiableHours.setGravity(Gravity.RIGHT);
                chkFood.setGravity(Gravity.RIGHT);
                chkInsur.setGravity(Gravity.RIGHT);
                chkPromo.setGravity(Gravity.RIGHT);
                chkTrainingCourse.setGravity(Gravity.RIGHT);
                chkTransporting.setGravity(Gravity.RIGHT);
                ///////////////////////////////////RelativeLayout1//////////////////////////////////////
                //resumeProfilePic
                RelativeLayout.LayoutParams resumeProfilePicparams = (RelativeLayout.LayoutParams)resumeProfilePic.getLayoutParams();
                resumeProfilePicparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                resumeProfilePic.setLayoutParams(resumeProfilePicparams);

                //LinearLayoutresume1
                RelativeLayout.LayoutParams LinearLayoutresume1params = (RelativeLayout.LayoutParams)LinearLayoutresume1.getLayoutParams();
                LinearLayoutresume1params.addRule(RelativeLayout.LEFT_OF, R.id.resumeProfilePic);
                LinearLayoutresume1params.setMargins(1,0,10,0);
                LinearLayoutresume1.setLayoutParams(LinearLayoutresume1params);



                ///////////////////////////////////LinearLayoutresumeParts//////////////////////////////////////
                LinearLayout.LayoutParams LinearLayoutresumepartparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayoutresumepartparams.gravity=Gravity.RIGHT;
                LinearLayoutresumepart1.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart2.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart3.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart4.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart5.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart6.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart7.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart8.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart9.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart10.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart11.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart12.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart13.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart14.setLayoutParams(LinearLayoutresumepartparams);
                Set_Texts("fa",resources,16);
            }


        }
        //ENGLISH LANGUAGE
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayoutMakeResume.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                LinearLayoutresume1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutresume1.setGravity(Gravity.LEFT);

                LinearLayoutresume2.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutresume2.setGravity(Gravity.LEFT);

                LinearLayoutresumepart12.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutresume2.setGravity(Gravity.LEFT);


                chkFlexiableHours.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                chkFood.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                chkInsur.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                chkPromo.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                chkTrainingCourse.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                chkTransporting.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                ///////////////////////////////////RelativeLayout1//////////////////////////////////////
                //resumeProfilePic
                RelativeLayout.LayoutParams resumeProfilePicparams = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.resumeProfilePic), getResources().getDimensionPixelSize(R.dimen.resumeProfilePic));
                resumeProfilePicparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                resumeProfilePic.setLayoutParams(resumeProfilePicparams);

                //LinearLayoutresume1
                RelativeLayout.LayoutParams LinearLayoutresume1params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayoutresume1params.addRule(RelativeLayout.RIGHT_OF, R.id.resumeProfilePic);
                LinearLayoutresume1params.setMargins(10, 0, 1, 0);
                LinearLayoutresume1.setLayoutParams(LinearLayoutresume1params);



                ///////////////////////////////////LinearLayoutresumeParts//////////////////////////////////////
                LinearLayout.LayoutParams LinearLayoutresumepartparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayoutresumepartparams.gravity = Gravity.LEFT;
                LinearLayoutresumepart1.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart2.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart3.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart4.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart5.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart6.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart7.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart8.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart9.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart10.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart11.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart12.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart13.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart14.setLayoutParams(LinearLayoutresumepartparams);

                spinnerjob_status.setGravity(Gravity.LEFT);
                spinnerDesiredIncome.setGravity(Gravity.LEFT);
                Set_Texts("en",resources,17);
            }
            //LANGUAGE IS EN
            //LANGUAGE IS API<17
            else {
                LinearLayoutresume1.setGravity(Gravity.LEFT);
                LinearLayoutresume2.setGravity(Gravity.LEFT);

                chkFlexiableHours.setGravity(Gravity.LEFT);
                chkFood.setGravity(Gravity.LEFT);
                chkInsur.setGravity(Gravity.LEFT);
                chkPromo.setGravity(Gravity.LEFT);
                chkTrainingCourse.setGravity(Gravity.LEFT);
                chkTransporting.setGravity(Gravity.LEFT);
                ///////////////////////////////////RelativeLayout1//////////////////////////////////////
                //resumeProfilePic
                RelativeLayout.LayoutParams resumeProfilePicparams = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.resumeProfilePic), getResources().getDimensionPixelSize(R.dimen.resumeProfilePic));
                resumeProfilePicparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                resumeProfilePic.setLayoutParams(resumeProfilePicparams);

                //LinearLayoutresume1
                RelativeLayout.LayoutParams LinearLayoutresume1params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayoutresume1params.addRule(RelativeLayout.RIGHT_OF, R.id.resumeProfilePic);
                LinearLayoutresume1params.setMargins(10, 0, 1, 0);
                LinearLayoutresume1.setLayoutParams(LinearLayoutresume1params);



                ///////////////////////////////////LinearLayoutresumeParts//////////////////////////////////////
                LinearLayout.LayoutParams LinearLayoutresumepartparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayoutresumepartparams.gravity = Gravity.LEFT;
                LinearLayoutresumepart1.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart2.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart3.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart4.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart5.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart6.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart7.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart8.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart9.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart10.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart11.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart12.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart13.setLayoutParams(LinearLayoutresumepartparams);
                LinearLayoutresumepart14.setLayoutParams(LinearLayoutresumepartparams);
                spinnerjob_status.setGravity(Gravity.LEFT);
                spinnerDesiredIncome.setGravity(Gravity.LEFT);
                Set_Texts("en",resources,16);
            }

        }
    }
    public void Set_Texts(String Language, Resources resources, int api){

        chkPromo.setText(resources.getString(R.string.chkPromo));
        chkInsur.setText(resources.getString(R.string.chkInsur));
        chkTrainingCourse.setText(resources.getString(R.string.chkTrainingCourse));
        chkTransporting.setText(resources.getString(R.string.chkTransporting));
        chkFood.setText(resources.getString(R.string.chkFood));
        chkFlexiableHours.setText(resources.getString(R.string.chkFlexiableHours));
        //Buttons
        btnSavechanges.setText(resources.getString(R.string.SaveChanges2));
        btnSaveAboutMe.setText(resources.getString(R.string.AboutMe));
        // txtSalary.setText(resources.getString(R.string.Salary));
        txtAboutMe.setText(resources.getString(R.string.AboutMe));
        txtSlugTitle.setText(resources.getString(R.string.SlugTitle));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(Language.equals("fa")){
                spinnerjob_status.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                spinnerLastDegree.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                spinnerDesiredIncome.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else{
                spinnerjob_status.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                spinnerLastDegree.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                spinnerDesiredIncome.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }

        //job_status_arrays
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.job_status_arrays)
        );
        spinnerjob_status.setAdapter(adapter);


        //certificate_arrays
        ArrayAdapter<String> certificateadapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.certificate_arrays)
        );
        spinnerLastDegree.setAdapter(certificateadapter);


        ArrayAdapter<String> DesiredIncomeadapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.DesiredIncome_arrays)
        );
        spinnerDesiredIncome.setAdapter(DesiredIncomeadapter);


        //new
        if(api==17  || Language.equals("fa")){
            txtJobTitleTitle.setText(resources.getString(R.string.JobTitle2));

            txtjob_status.setText(resources.getString(R.string.job_status));

            txtLastDegreeTitle.setText(resources.getString(R.string.LastDegree));

            //txtEmailAddress.setText(resources.getString(R.string.EmailAddress));
            txtEmailAddressTitle.setText(resources.getString(R.string.EmailAddressTitle));

            //txtPhone.setText(resources.getString(R.string.Phone));
            txtPhoneTitle.setText(resources.getString(R.string.PhoneTitle));

           // txtProvinceResume.setText(resources.getString(R.string.ProvinceResume));
            txtProvinceResumeTitle.setText(resources.getString(R.string.ProvinceResumeTitle));

           // txtMarriageResume.setText(resources.getString(R.string.MarriageResume));
            txtMarriageResumeTitle.setText(resources.getString(R.string.MarriageResumeTitle));


         //  txtBirthYearResume.setText(resources.getString(R.string.BirthYearResume));
            txtBirthYearResumeTitle.setText(resources.getString(R.string.BirthYearResumeTitle));

          //  txtDutyResume.setText(resources.getString(R.string.DutyResume));
            txtDutyResumeTitle.setText(resources.getString(R.string.DutyResumeTitle));

           // txtAddressResume.setText(resources.getString(R.string.AddressResume));
            txtAddressResumeTitle.setText(resources.getString(R.string.AddressResumeTitle));
        }
        else{
            txtJobTitleTitle.setText(resources.getString(R.string.JobTitle2));

            txtjob_status.setText(resources.getString(R.string.job_status));

            txtLastDegreeTitle.setText(resources.getString(R.string.LastDegree));

            txtEmailAddress.setText(resources.getString(R.string.EmailAddressTitle));
           // txtEmailAddressTitle.setText(resources.getString(R.string.EmailAddress));

            txtPhone.setText(resources.getString(R.string.PhoneTitle));
          //  txtPhoneTitle.setText(resources.getString(R.string.Phone));

            txtProvinceResume.setText(resources.getString(R.string.ProvinceResumeTitle));
          //  txtProvinceResumeTitle.setText(resources.getString(R.string.ProvinceResume));

            txtMarriageResume.setText(resources.getString(R.string.MarriageResumeTitle));
         ///   txtMarriageResumeTitle.setText(resources.getString(R.string.MarriageResume));

            txtBirthYearResume.setText(resources.getString(R.string.BirthYearResumeTitle));
        //    txtBirthYearResumeTitle.setText(resources.getString(R.string.BirthYearResume));

            txtDutyResume.setText(resources.getString(R.string.DutyResumeTitle));
           // txtDutyResumeTitle.setText(resources.getString(R.string.DutyResume));

            txtAddressResume.setText(resources.getString(R.string.AddressResumeTitle));
         //   txtAddressResumeTitle.setText(resources.getString(R.string.AddressResume));
        }


        btnEditPersonal.setText(resources.getString(R.string.Edit));
        txtPrivateInfo.setText(resources.getString(R.string.PrivateInfo));
        txtTalents.setText(resources.getString(R.string.Talents));
        txtJobExp.setText(resources.getString(R.string.JobExp));
        txtGraduateExp.setText(resources.getString(R.string.GraduateExp));
        txtLanguages.setText(resources.getString(R.string.Languages));
        txtJobFavorites.setText(resources.getString(R.string.JobFavorites));
        txtSelectedProvinces.setText(resources.getString(R.string.SelectedProvinces));
        txtJobCategory.setText(resources.getString(R.string.JobCategory));
        txtAcceptedContract.setText(resources.getString(R.string.AcceptedContract));
        txtActivityLevel.setText(resources.getString(R.string.ActivityLevel));
        txtActualRights.setText(resources.getString(R.string.ActualRights));
//    txtSalary.setText(resources.getString(R.string.Salary));
        txtJobBenefits.setText(resources.getString(R.string.JobBenefits));
        txtAboutMe2.setText(resources.getString(R.string.AboutMe2));
        txtGenderResumeTitle.setText(resources.getString(R.string.GenderResumeTitle));
       // txtGenderResume.setText(resources.getString(R.string.GenderResume));
    }

    public void update_personal_info_dialog(String Language){

        Context context = LocaleHelper.setLocale(getApplicationContext(), Language);
        Resources resources = context.getResources();

        txtPIEditPersonalInfo.setText(resources.getString(R.string.PIEditPersonalInfo));
        txtPIEmailAddress.setText(resources.getString(R.string.PIEmailAddress));
        txtPIMobile.setText(resources.getString(R.string.PIMobile));
        txtPIProvince.setText(resources.getString(R.string.PIProvince));
        txtPIBirthYear.setText(resources.getString(R.string.PIBirthYear));
        txtPIAddress.setText(resources.getString(R.string.PIAddress));
        txtPIMarital.setText(resources.getString(R.string.PIMarital));
        txtPIGender.setText(resources.getString(R.string.PIGender));
        txtPIMilitaryservice.setText(resources.getString(R.string.PIMilitaryservice));
        btnPI_save_editpersoalinfo.setText(resources.getString(R.string.SaveChanges));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(Language.equals("fa")){
                spinnerPIMarital.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                spinnerPIGender.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                spinnerPIMilitaryservice.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else{
                spinnerPIMarital.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                spinnerPIGender.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                spinnerPIMilitaryservice.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }
        //job_status_arrays
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.Marital_arrays)
        );
        spinnerPIMarital.setAdapter(adapter);


        ArrayAdapter<String> PIProvinceadapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.Province)
        );
        spinnerPIProvince.setAdapter(PIProvinceadapter);

        ArrayAdapter<String> PIGender_array = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.Gender_arrays)
        );
        spinnerPIGender.setAdapter(PIGender_array);

        ArrayAdapter<String> PIMilitaryservice = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.Militaryservice_arrays)
        );
        spinnerPIMilitaryservice.setAdapter(PIMilitaryservice);

    }
    private void get_credentials(final String token, final String email, final String passwordd){
        StartDialog.setProgress(20);
        StartDialog.setProgress(30);
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VIEW_Profile, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "View Profile Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");

                    // Check for error node in json
                    if (success.equalsIgnoreCase("true")) {
                        JSONObject c = jObj.getJSONObject("data");
                        int id =0;
                        String firstname=" ",lastname=" " , email=" ", phone= " ", token=" ",
                                image =" ",mobile=" ",address=" ", zip=" ",password=" ", resume=" ";
                        id = c.getInt("id");
                        firstname = c.getString("first_name");
                        lastname = c.getString("last_name");
                        email = c.getString("email");
                        token = c.getString("auth_key");
                        phone = c.getString("tel_number");
                        image = c.getString("avatar");
                        mobile = c.getString("mobile");
                        address = c.getString("address");
                        zip = c.getString("postal_code");
                        if(c.has("resumeId"))
                            resume =c.getString("resumeId");
                        else
                            resume =c.getString(null);
                        password = passwordd;
                        session.setLogin(true);
                        session.setUserDetails(id,firstname,lastname,email,phone,token, image,mobile,address,zip,password
                                ,resume);

                    } else {
                        Log.d("TAG", "failed to get user");
                    }
                    JobCats_Api();
                    start();

                } catch (JSONException e) {
                    // JSON error
                    Log.d("TAG", "get creds error 1 ");
                    e.printStackTrace();
                    JobCats_Api();
                    start();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",  "get creds error2 : " + error.getMessage());
                JobCats_Api();
                start();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("auth", token);
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

    //GET LIST OF JOB CATEGORIES
    private void JobCats_Api(){
        StartDialog.setProgress(35);
        String tag_string_req = "req_CATEGORIES";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_JobCats + Language, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "CATEGORIES Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray data = jObj.getJSONArray("data");
                    if(data.length()>0){
                        //txtEmptyHires.setVisibility(View.GONE);
                        for (int i = 0; i < data.length(); i++) {
                            JobcatsMap.put(data.getJSONObject(i).getString("title")
                                    , data.getJSONObject(i).getInt("id"));
                        }
                    }
                    Log.d("TAG", "No Object recieved!");
                } catch (JSONException e) {
                    // JSON error
                    Log.d("TAG", "error 1 " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",  "error2"+error.getMessage());
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
                return headers;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



    //
    //    sending properties
    //    txtJobTitle EdittxtSlug spinnerjob_status spinnerLastDegree txtEmailAddress txtPhone
    //    txtProvinceResume txtMarriageResume txtGenderResume txtBirthYearResume txtDutyResume txtAddressResume
    //    skills:listProes
    //
    //
    //    sending properties
    //    txtJobTitle EdittxtSlug spinnerjob_status spinnerLastDegree txtEmailAddress txtPhone
    //    txtProvinceResume txtMarriageResume txtGenderResume txtBirthYearResume txtDutyResume txtAddressResume
    //    skills:listProes

    public String MakeCheckString(){
        String chks="";
        if (chkPromo.isChecked())
            chks+=chks+"1";
        if (chkInsur.isChecked())
            chks+=chks+"2";
        if (chkTrainingCourse.isChecked())
            chks+=chks+"3";
        if (chkTransporting.isChecked())
            chks+=chks+"4";
        if (chkFood.isChecked())
            chks+=chks+"5";
        if (chkFlexiableHours.isChecked())
            chks+=chks+"6";

        return chks;
    }
    public String  SplitTerms() {
        String splitetTerm="";
        String last="";
        for (String itme: listTerms) {
            splitetTerm+=itme+",";
        }

        if ((splitetTerm.length()>0)) {
            last = splitetTerm.substring(0, splitetTerm.length() - 1);
        }
        return last;
    }
    public String  SplitListProvince() {


        String splitetTerm="";
        String last="";
        for (String itme: listProvince
                ) {
            splitetTerm+=itme+",";
        }

        if ((splitetTerm.length()>0)) {
            last = splitetTerm.substring(0, splitetTerm.length() - 1);
        }

        return last;
    }
    private void make_resume_api(){

        String tag_string_req = "req_make_resume";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MAKE_RESUME, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Get Token Tag", "Token Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");

                    // Check for error node in json
                    if (success.equalsIgnoreCase("true")) {
                        // user successfully logged in
                        JSONObject c = jObj.getJSONObject("data");

                    }

                } catch (JSONException e) {
                    // JSON error
                    Log.d("TAG", "error 1 ");


                    e.printStackTrace();
                    if(Language.equals("fa")){
                        Toast.makeText(MakeResume.this, "در بعضی ورودی ها مشکل وجود دارد!", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(MakeResume.this, "Check your inputs!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",  "error 2 in getting token! " + " " + error.getMessage());

                if(Language.equals("fa")){
                    Toast.makeText(MakeResume.this, "مشکلی در اتصال با سرور رزومه پیش آمده است", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(MakeResume.this, "Network Connection or resume Server failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("auth",session.getUserDetails().getToken() );
                params.put("Resume[job_title]", txtJobTitleTitle.getText().toString());
                params.put("Resume[slug]",EdittxtSlug.getText().toString() );
                params.put("Resume[job_status]",JobStatus_arrays.indexOf( txtjob_status.getText().toString())+1+"");

                String skills  = null;
                for(String item: listSkill){
                    skills+=item+",";
                }
                params.put("Resume[skills]",skills.substring(0,skills.length()-1) );

                params.put("Resume[provinceId] ",Province.indexOf(txtProvinceResume.getText())+1+"");
                params.put("Resume[year_birth]", txtBirthYearResume.getText().toString());
                params.put("Resume[martial]", Marital_arrays.indexOf(txtMarriageResume.getText())+1+"");
                params.put("Resume[sex]",Gender_arrays.indexOf(txtGenderResume.getText())+1+"");
                if (!(txtDutyResume.getText()==null))
                    params.put("Resume[military_status]",Militaryservice_arrays.indexOf(txtDutyResume.getText())+1+"" );


                params.put("Resume[provinces][] ",SplitListProvince());
                params.put("Resume[contracts][]",SplitTerms() );
                params.put("Resume[benefits][]",MakeCheckString());

                if (listSkill.size()!=0)
                    params.put("Resume[levels]",Seniority_arrays.indexOf(listSkill.get(0))+1+"" );
                params.put("categories","1,2");
                params.put("Resume[salary]",Salary_arrays.indexOf(spinnerDesiredIncome.getSelectedItem().toString())+1+"" );
                //params.put("Resume[address]",txtAddressResume.getText().toString() );

                params.put("Resume[about_me]",txtAboutMe.getText().toString() );
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
    private void get_resume_api(){
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GETAPI_RESUME + session.getUserDetails().getResume() , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "View resume Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");

                    // Check for error node in json
                    if (success.equalsIgnoreCase("true")) {
                        JSONObject c = jObj.getJSONObject("data");
                        String id =" ";
                        String job_title=" ", year_birth=" " , about_me=" " ,job_status=" " ,
                                image=" " , fullname=" " , email=" " , mobile=" " , address=" " , link=" " , sex=" " ,
                                military_status=" " , martial=" " , province=" " , levels=" " , salary=" " , categories=" " ,
                                benefits=" " , provinces=" " , contracts=" " , experiences=" " , academics=" " ,
                                languages=" " ;
                        JSONArray skills;


                        id = c.getString("id");
                        job_title = c.getString("job_title");
                        year_birth = c.getString("year_birth");
                        about_me = c.getString("about_me");
                        job_status = c.getString("job_status");
                        image = c.getString("image");
                        fullname = c.getString("fullname");
                        email = c.getString("email");
                        mobile = c.getString("mobile");
                        address = c.getString("address");
                        link = c.getString("link");
                        sex = c.getString("sex");
                        military_status = c.getString("military_status");
                        link = c.getString("link");
                        martial = c.getString("martial");
                        province = c.getString("province");
                        levels = c.getString("levels");
                        salary = c.getString("salary");
                        categories = c.getString("categories");
                        benefits = c.getString("benefits");
                        provinces = c.getString("provinces");
                        contracts = c.getString("contracts");
                        skills = c.getJSONArray("skills");

                        experiences = c.getString("experiences");
                        academics = c.getString("academics");
                        languages = c.getString("languages");

                        setValues(id,
                                job_title,
                                year_birth,
                                about_me,
                                job_status,
                                image,
                                fullname,
                                email,
                                mobile,
                                address,
                                link,
                                sex,
                                military_status,
                                martial,
                                province,
                                levels,
                                salary,
                                categories,
                                benefits,
                                provinces,
                                contracts,
                                skills,
                                experiences,
                                academics,
                                languages);



                    } else {
                        Log.d("TAG", "failed to get user");
                    }
                    JobCats_Api();
                    start();

                } catch (JSONException e) {
                    // JSON error
                    Log.d("TAG", "get creds error 1 ");
                    e.printStackTrace();
                    JobCats_Api();
                    start();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",  "get creds error2 : " + error.getMessage());
                JobCats_Api();
                start();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

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
    public void setValues(String id,
                          String job_title,
                          String year_birth,
                          String about_me,
                          String job_status,
                          String image,
                          String fullname,
                          String email,
                          String mobile,
                          String address,
                          String link,
                          String sex,
                          String military_status,
                          String martial,
                          String province,
                          String levels,
                          String salary,
                          String categories,
                          String benefits,
                          String provinces,
                          String contracts,
                          JSONArray skills,
                          String experiences,
                          String academics,
                          String languages){

        Glide.with(getApplicationContext())
                .load(image)
                .placeholder(R.drawable.logo_eng)
                .error(R.drawable.logo_eng)
                .into(resumeProfilePic);

        txtEmailAddress.setText(email);
        txtPhone.setText(mobile);
        txtProvinceResume.setText(province);
        txtMarriageResume.setText(martial);
        txtBirthYearResume.setText(year_birth);
        txtDutyResume.setText(military_status);
        txtAddressResume.setText(address);
        txtJobTitle.setText(job_title);

        for (int i=0 ; i<skills.length();i++){
            try {
                listProes.add(skills.getString(i));
                listAdapterProes.notifyDataSetChanged();
                //justify what
                justifyListViewHeightBasedOnChildren(listViewProes);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        listAdapterJobs.notifyDataSetChanged();
//        justifyListViewHeightBasedOnChildren(listView);

        txtAboutMe.setText(about_me);
        int result = Integer.parseInt(job_status);
        spinnerjob_status.setSelection(result);


        int stringSalary = Integer.parseInt(salary);
        spinnerDesiredIncome.setSelection(stringSalary);

        //benefits checkboxes
        String s1 = benefits;
        String[] words=s1.split(",");

        for (String item:words
                ) {
//            BenefitsArray.contains(item);
            if (item == "امکان ترفیع سمت"){
                chkPromo.setChecked(true);
            }else if(item == "بیمه"){
                chkInsur.setChecked(true);
            }else if(item == "دوره\u200Cهای آموزشی"){
                chkTrainingCourse.setChecked(true);
            }else if(item == "غذا به عهده\u200Cی شرکت"){
                chkFood.setChecked(true);
            }else if(item == "ساعت کاری منعطف"){
                chkFlexiableHours.setChecked(true);
            }else if(item == "سرویس رفت و\u200Cآمد"){
                chkTransporting.setChecked(true);
            }
        }

        //Provinces
        String s2 = provinces;
        String[] words2=s1.split(",");

        for (int i=0 ; i< words2.length ;i++){
            try {
                listProvince.add(skills.getString(i));
                listAdapterProvince.notifyDataSetChanged();
                //justify what
                justifyListViewHeightBasedOnChildren(listViewProvince);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //contracts
        String s3 = contracts;
        String[] words3=s1.split(",");

        for (int i=0 ; i< words3.length ;i++){
            try {
                listTerms.add(skills.getString(i));
                listAdapterTerms.notifyDataSetChanged();
                //justify what
                justifyListViewHeightBasedOnChildren(listViewTerms);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    private void hideshow(int hide){
        if(hide==1){
            Log.d("TAG", "hideshow: " + "ok");
            LinearLayoutDegree.setVisibility(View.GONE);
            LinearLayoutJobEduLang.setVisibility(View.GONE);
        }
        else{
            LinearLayoutDegree.setVisibility(View.VISIBLE);
            LinearLayoutJobEduLang.setVisibility(View.VISIBLE);
        }
    }


   //NEW APIs
   public boolean existslang(String name, String level){
       for (Lang item: listLang) {
           if(item.getName().equals(name) && item.getLevel().equals(level))
               return true;
       }
       return false;
   }
    private void set_languages(final String resumeId){
        for (final Lang item : listLang) {
            String id = "";
            //check if its in old and updated or not
            if(item.getId()!=0)
            {
                id = item.getId()+"";
            }

            String tag_string_req = "req_resumelanguage";
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_ResumeLanguages + id, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("TAG", "resumelanguage Response: " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        String success = jObj.getString("success");

                        // Check for error node in json
                        if (success.equalsIgnoreCase("true")) {
                            finish();
                            startActivity(getIntent());
                        } else {
                            Log.d("TAG", "failed to set language");
                        }
                    } catch (JSONException e) {
                        // JSON error
                        Log.d("TAG", "failed to set language error 1 ");
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", "failed to set language  error2 : " + error.getMessage());
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("LangResume[language]", item.getName());
                    params.put("LangResume[level]", item.getLevel());
                    params.put("LangResume[resumeId]", "0");//session.getUserDetails().getResume()
                    params.put("auth", session.getUserDetails().getToken());
                    return params;
                }


                //basic auth
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    // add headers <key,value>
                    String credentials = AppConfig.AUTH_USERNAME + ":" + AppConfig.AUTH_PASS;
                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(),
                            Base64.URL_SAFE | Base64.NO_WRAP);
                    headers.put("Authorization", auth);
                    return headers;
                }

            };
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }


    // getting experiences from api : id,job_title,company,start_year,end_year
    // add this lang.setId(0); to listLang.add(lang);
    public boolean existsexperience(String from, String to, String role, String name){
        for (Job item: listJobs) {
            if(item.getFrom().equals(from) && item.getTo().equals(to) && item.getRole().equals(role)
                    && item.getCompany().equals(name))
                return true;
        }
        return false;
    }
    private void set_experiences(final String resumeId){

        for(final Job item:listJobs){
            String id = "";
            if(item.getId() != 0){
                id=item.getId()+"";
            }
            String tag_string_req = "req_resumeexperience";
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_ResumeExperiences+ id, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("TAG", "resumeexperience Response: " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        String success = jObj.getString("success");

                        // Check for error node in json
                        if (success.equalsIgnoreCase("true")) {
                            finish();
                            startActivity(getIntent());
                        } else {
                            Log.d("TAG", "failed to set experience");
                        }
                    } catch (JSONException e) {
                        // JSON error
                        Log.d("TAG", "failed to set experience error 1 ");
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG",  "failed to set experience  error2 : " + error.getMessage());
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Experience[job_title]", item.getJobtitle());
                    params.put("Experience[company]", item.getCompany());
                    params.put("Experience[start_month]", "1" );
                    params.put("Experience[start_year]",item.getFrom());
                    params.put("Experience[end_month]","1");
                    params.put("Experience[end_year]", item.getTo());
                    params.put("Experience[working]","0");
                    params.put("Experience[description]", "");
                    params.put("Experience[resumeId]","0");// session.getUserDetails().getResume()
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
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        }
    }

    // getting langs from api : id,language,level
    // add this lang.setId(0); to listLang.add(lang);
    public boolean existsacademic(String grade, String field, String Place, String from, String to){
        for (Education item: listEducation) {
            if(item.getGrade().equals(grade) && item.getFrom().equals(from) && item.getTo().equals(to) && item.getField().equals(field)
                    && item.getPlace().equals(Place))
                return true;
        }
        return false;
    }
    private void set_academics(final String resumeId)
    {

        for(final Education item:listEducation){
            String id = "";
            if(item.getId() != 0){
                id=item.getId()+"";
            }
            String tag_string_req = "req_resumeAcademic";
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_ResumeAcademics+ id, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("TAG", "resumeexperience Response: " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        String success = jObj.getString("success");

                        // Check for error node in json
                        if (success.equalsIgnoreCase("true")) {
                            finish();
                            startActivity(getIntent());
                        } else {
                            Log.d("TAG", "failed to set Academic");
                        }
                    } catch (JSONException e) {
                        // JSON error
                        Log.d("TAG", "failed to set Academic error 1 ");
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG",  "failed to set Academic  error2 : " + error.getMessage());
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Academic[field]", item.getField());
                    params.put("Academic[university]", item.getPlace());
                    params.put("Academic[grade]", item.getGrade());
                    params.put("Academic[start_year]",item.getFrom());
                    params.put("Academic[end_month]","1");
                    params.put("Academic[end_year]", item.getTo());
                    params.put("Academic[still]","0");
                    params.put("Academic[description]", "");
                    params.put("Academic[resumeId]","0");// session.getUserDetails().getResume()
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
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        }
    }

    //progres
    private void startDialog(ProgressDialog dialog, String famessage,String  enmessage,int type,boolean cancel){
        if (type==0) dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        else dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(cancel);
        if(Language.equals("fa"))
            dialog.setMessage(famessage);
        else
            dialog.setMessage(enmessage);

        showDialog(dialog);
    }
    private void showDialog(ProgressDialog dialog) {
        if (!dialog.isShowing())
            dialog.show();
    }
    private void hideDialog(ProgressDialog dialog) {
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
