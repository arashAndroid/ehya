package com.mrehya.Resume;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.mrehya.Job;
import com.mrehya.ListAdapterJobs;
import com.mrehya.Reserv.PersianCalendar;
import com.mrehya.R;
import com.mrehya.Reserv.Persian_Date_Methods;
import com.mrehya.SessionManager;
import com.mrehya.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResumeJobexpActivity extends AppCompatActivity {
    private String Language;
    private SessionManager session;
    private Resources resources;
    private Resume resume;
    //ELEMENTS
    private Button btn_saveresume,btn_back;
    private TextView txttitle;
    private ListView listViewJobs;
    private ImageButton btnAddJobs;
    private ArrayList<Job> listJobs, oldJobs;
    private ListAdapterJobs listAdapterJobs;
    private ArrayList<String> years, month;
    private ArrayList<Integer> ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_jobexp);

        session = new SessionManager(getApplicationContext());
        Intent intent = getIntent();
        Language = intent.getStringExtra("Language");
        findviews();
        setLists();
        setOnclicks();
        updateView();
        try {
            setDefaults();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void findviews(){
        txttitle = (TextView) findViewById(R.id.txttitle);
        listViewJobs = (ListView) findViewById(R.id.listViewJobs);
        btnAddJobs = (ImageButton) findViewById(R.id.btnAddJobs);
        listViewJobs.setAdapter(listAdapterJobs);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_saveresume = (Button) findViewById(R.id.btn_saveresume);
        setJobsList();
    }
    private void setLists(){
        ids = new ArrayList<>();
        resume= new Resume(ResumeJobexpActivity.this);
        Persian_Date_Methods pd = new Persian_Date_Methods(Language);
        if(Language.equals("fa")){
            years = pd.get_persian_years();
            month = pd.get_persian_month();
        }
        else{
            years = pd.get_gregorian_years();
            month = pd.get_gregorian_months();
        }
    }
    public void setOnclicks(){
        btnAddJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddJobs(listViewJobs);
            }
        });

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
        if(Language.equals("fa")){
            txttitle.setText("سوابق شغلی");
        }
        else{
            txttitle.setText("Job Experiences");
        }
        listViewJobs.setAdapter(listAdapterJobs);
        btn_back.setText(resources.getString(R.string.Back));
        btn_saveresume.setText(resources.getString(R.string.save));
    }
    public void setDefaults() throws JSONException {
        JSONArray array = new JSONArray(session.get_Resume().getExperiences().toString());
        oldJobs = new ArrayList<>();
        for (int i=0 ; i<array.length();i++)
        {
            JSONObject c= array.getJSONObject(i);
            Job job = new Job(c.getString("title"), c.getString("start_year"), c.getString("last"), "",
                    c.getString("company"), "", "1", "1");
            job.setId(c.getInt("id"));
            listJobs.add(job);
            oldJobs.add(job);
            listAdapterJobs.notifyDataSetChanged();
            justifyListViewHeightBasedOnChildren(listViewJobs);
        }
    }
    private void save(){
        if(Language.equals("fa")){
            Toast.makeText(this, "در حال ارتباط با سرور...", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Connecting to server...", Toast.LENGTH_SHORT).show();
        }
        ids.clear();
        String resumeid = session.getUserDetails().getResume();
        for(final Job item: listJobs){
            ids.add(item.getId());
        }
        for(final Job item:oldJobs){
            String req="";
            if(item.getId() == 0){
                if (ids.contains(item.getId()))
                {
                    req="https://api.mrehya.com/v1/user/experience";
                }
                else{
                    continue;
                }
            }
            else if(item.getId() != 0){
                if (!ids.contains(item.getId()))
                {
                    resumeid="-1";
                }
                else{
                    for (Job item2: listJobs) {
                        if(item2.getId() == item.getId()){
                            item.setCompany(item2.getCompany());
                            item.setFrom(item2.getFrom());
                            item.setTo(item2.getTo());
                            item.setJobtitle(item2.getJobtitle());
                            break;
                        }
                    }
                }
                req = "https://api.mrehya.com/v1/user/experience?id="+item.getId();
            }
            //Log.e("item" , req);
            String tag_string_req = "req_resumeexperience";
            final String finalResumeid = resumeid;
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    req, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.e("req_resumeexperience", response.toString());
                    try {
                        JSONObject jObj = new JSONObject(response);
                        String success = jObj.getString("success");
                        // Check for error node in json
                        if (success.equalsIgnoreCase("true")) {
                            if(Language.equals("fa")){
                                Toast.makeText(ResumeJobexpActivity.this, "با موفقیت بروزرسانی شد", Toast.LENGTH_SHORT).show(); }
                            else{
                                Toast.makeText(ResumeJobexpActivity.this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                            }
                            resume.get_resume_api();
                        } else {
                            //Log.e("req_resumeexperience", "failed to set experience");
                        }
                    } catch (JSONException e) {
                        // JSON error
                        Log.e("req_resumeexperience", "failed to set experience error 1 ");
                        e.printStackTrace();
                        if(Language.equals("fa")){
                            Toast.makeText(ResumeJobexpActivity.this, "مشکلی در بروزرسانی رزومه تان بوجود آمده است!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ResumeJobexpActivity.this, "Problem on updating Resume!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("req_resumeexperience",  "failed to set experience  error2 : " + error.getMessage());
                    if(error.networkResponse.statusCode==403)
                    {
                        User u = new User(ResumeJobexpActivity.this);
                        u.checkLogin(session.getUserDetails().getEmail(), session.getUserDetails().getPassword());
                        if(Language.equals("fa")){
                            Toast.makeText(ResumeJobexpActivity.this, "اشکال اتصال به شبکه(در بروزرسانی رزومه) کمی بعد دوباره امتحان کنید", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(ResumeJobexpActivity.this, "Network Connection or Resume Server failed! try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        if(Language.equals("fa")){
                            Toast.makeText(ResumeJobexpActivity.this, "اشکال اتصال به شبکه(در بروزرسانی رزومه)", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(ResumeJobexpActivity.this, "Network Connection or Resume Server failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Experience[job_title]", item.getJobtitle());
                    params.put("Experience[company]", item.getCompany());
                    params.put("Experience[start_year]" , fa_toen_num(item.getFrom()));
                    params.put("Experience[end_year]", fa_toen_num(item.getTo()));
                    params.put("Experience[resumeId]", finalResumeid);
                    params.put("auth", session.getUserDetails().getToken());

                    if(item.getId()==0){
                        params.put("Experience[end_month]",item.getTomonth());
                        params.put("Experience[start_month]", item.getFrommonth());
                        params.put("Experience[working]",item.getstillworking());
                        params.put("Experience[description]", item.getRole());
                    }

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

    //methods
    private void setJobsList(){
        listJobs = new ArrayList<>();
        listAdapterJobs = new ListAdapterJobs(listJobs,getApplicationContext(),ResumeJobexpActivity.this,listViewJobs);
    }
    private void showDialogAddJobs(final ListView listView){
        final Dialog dialog = new Dialog(ResumeJobexpActivity.this);
        dialog.setContentView(R.layout.list_dialog_jobs);
        final EditText txtEditJobDesc = dialog.findViewById(R.id.txtEditDialogJobDesc);
        final EditText txtEditCompany = dialog.findViewById(R.id.txtEditDialogJobCompany);
        final Spinner txtEditFrom = dialog.findViewById(R.id.spinnerFromJob);
        final Spinner txtEditTo = dialog.findViewById(R.id.spinnerToJob);

        final Spinner txtEditFrommonth = dialog.findViewById(R.id.spinnerFromJobmonth);
        final Spinner txtEditTomonth = dialog.findViewById(R.id.spinnerToJobmonth);

        final EditText txtEditJobtitle = dialog.findViewById(R.id.txtEditDialogJobtitle);
        final CheckBox chkStillWorking = (CheckBox)  dialog.findViewById(R.id.chkStillWorking);


        final LinearLayout LinearLayoutresumedialogJob = dialog.findViewById(R.id.LinearLayoutresumedialogJob);
        final LinearLayout LinearLayoutresumedialogJob2 = dialog.findViewById(R.id.LinearLayoutresumedialogJob2);
        final LinearLayout LinearLayoutresumedialogJob3 = dialog.findViewById(R.id.LinearLayoutresumedialogJob3);

        final TextView btnEditd = dialog.findViewById(R.id.btnEditd);
        final TextView txtjobtitle= dialog.findViewById(R.id.txtjobtitle);
        final TextView txtdesc = dialog.findViewById(R.id.txtdesc);
        final TextView txtcompany = dialog.findViewById(R.id.txtcompany);
        final TextView txtFrom = dialog.findViewById(R.id.txtFrom);
        final TextView txtuntil = dialog.findViewById(R.id.txtuntil);
        Context context = LocaleHelper.setLocale(getApplicationContext(), Language);
        resources = context.getResources();
        if(Language.equals("fa"))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayoutresumedialogJob.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutresumedialogJob2.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutresumedialogJob3.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            btnEditd.setText("اضافه کردن");
            chkStillWorking.setText("من هنوز مشغول کار هستم");
            ArrayAdapter<String> From = new ArrayAdapter<String>(
                    context,
                    R.layout.z_simple_spinner_item_rtl,
                    years
            );
            From.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_rtl);
            txtEditFrom.setAdapter(From);

            ArrayAdapter<String> To = new ArrayAdapter<String>(
                    context,
                    R.layout.z_simple_spinner_item_rtl,
                    years
            );
            To.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_rtl);
            txtEditTo.setAdapter(To);


            ArrayAdapter<String> Frommonth = new ArrayAdapter<String>(
                    context,
                    R.layout.z_simple_spinner_item_rtl,
                    month
            );
            From.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_rtl);
            txtEditFrommonth.setAdapter(Frommonth);

            ArrayAdapter<String> Tomonth = new ArrayAdapter<String>(
                    context,
                    R.layout.z_simple_spinner_item_rtl,
                    month
            );
            To.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_rtl);
            txtEditTomonth.setAdapter(Tomonth);

        }

        else
        {
            ArrayAdapter<String> From = new ArrayAdapter<String>(
                    context,
                    R.layout.z_simple_spinner_item_ltr,
                    years
            );
            From.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_ltr);
            txtEditFrom.setAdapter(From);

            ArrayAdapter<String> To = new ArrayAdapter<String>(
                    context,
                    R.layout.z_simple_spinner_item_ltr,
                    years
            );


            ArrayAdapter<String> Frommonth = new ArrayAdapter<String>(
                    context,
                    R.layout.z_simple_spinner_item_ltr,
                    month
            );
            From.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_ltr);
            txtEditFrommonth.setAdapter(Frommonth);

            ArrayAdapter<String> Tomonth = new ArrayAdapter<String>(
                    context,
                    R.layout.z_simple_spinner_item_ltr,
                    month
            );
            To.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_ltr);
            txtEditTomonth.setAdapter(Tomonth);

            To.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_ltr);
            txtEditTo.setAdapter(To);
            chkStillWorking.setText("Still Working");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayoutresumedialogJob.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutresumedialogJob2.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutresumedialogJob3.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            btnEditd.setText("Add");
        }
        txtjobtitle.setText(resources.getString(R.string.JobTitle2));
        txtdesc.setText(resources.getString(R.string.desc));
        txtcompany.setText(resources.getString(R.string.Company));
        txtFrom.setText(resources.getString(R.string.From));
        txtuntil.setText(resources.getString(R.string.until));



        ImageButton btnEdit = dialog.findViewById(R.id.btnEditDialogJob);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(existsexperience(txtEditFrom.getSelectedItem().toString(),txtEditTo.getSelectedItem().toString(),txtEditJobDesc.getText().toString(),txtEditCompany.getText().toString())))
                {
                    Job job = new Job(txtEditJobtitle.getText().toString(),txtEditFrom.getSelectedItem().toString(),txtEditTo.getSelectedItem().toString(),
                            txtEditJobDesc.getText().toString(),txtEditCompany.getText().toString(),
                            chkStillWorking.isSelected()?"1":"0", (month.indexOf(txtEditFrommonth.getSelectedItem().toString())+1)+""
                            , (month.indexOf(txtEditTomonth.getSelectedItem().toString())+1)+"" );
                    job.setId(0);
                    listJobs.add(job);
                    oldJobs.add(job);
                    listAdapterJobs.notifyDataSetChanged();
                    justifyListViewHeightBasedOnChildren(listView);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout((6 * width)/7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    public boolean existsexperience(String from, String to, String role, String name){
        for (Job item: listJobs) {
            if(item.getFrom().equals(from) && item.getTo().equals(to) && item.getRole().equals(role)
                    && item.getCompany().equals(name))
                return true;
        }
        return false;
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
    private String fa_toen_num(String number){
        return number.replace("۱","1").replace("۲","2")
                .replace("۳","3").replace("۴","4")
                .replace("۵","5").replace("۶","6")
                .replace("۷","7").replace("۸","8")
                .replace("۹","9").replace("۰","0");
    }
}
