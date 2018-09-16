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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.mrehya.Lang;
import com.mrehya.ListAdapterLang;
import com.mrehya.R;
import com.mrehya.SessionManager;
import com.mrehya.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResumeLanguagesActivity extends AppCompatActivity {
    private Context context;
    private String Language;
    private SessionManager session;
    private Resources resources;
    private Resume resume;
    //ELEMENTS
    private Button btn_saveresume,btn_back;
    private TextView txttitle;
    private ListView listViewLang;
    private ImageButton btnAddLang;
    private ArrayList<Lang> listLang, oldLang;
    private ListAdapterLang listAdapterLang;
    private ArrayList<Integer> ids;

    private List<String> levels, names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_languages);

        session = new SessionManager(getApplicationContext());
        Intent intent = getIntent();
        Language = intent.getStringExtra("Language");

        context = LocaleHelper.setLocale(getApplicationContext(), Language);
        resources = context.getResources();
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
        txttitle = (TextView) findViewById(R.id.txttitle);
        listViewLang = (ListView) findViewById(R.id.listViewLang);
        btnAddLang = (ImageButton) findViewById(R.id.btnAddLang);
        listViewLang.setAdapter(listAdapterLang);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_saveresume = (Button) findViewById(R.id.btn_saveresume);
        resume= new Resume(ResumeLanguagesActivity.this);
        ids = new ArrayList<>();

        levels = Arrays.asList(resources.getStringArray(R.array.langLevels));
        names =   Arrays.asList(resources.getStringArray(R.array.langs));
        setLangList();
    }
    public void setOnclicks(){
        btnAddLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddLang(listViewLang);
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
            txttitle.setText("زبان ها");
        }
        else{
            txttitle.setText("Languages");
        }
        listViewLang.setAdapter(listAdapterLang);
    }
    public void setDefaults() throws JSONException {
        JSONArray array = new JSONArray(session.get_Resume().getLanguages().toString());
        oldLang = new ArrayList<>();
        for (int i=0 ; i<array.length();i++)
        {
            JSONObject c= array.getJSONObject(i);
            Log.e("default", c.getString("language")+","+ c.getString("level"));
            Lang lang = new Lang((names.indexOf(c.getString("language").replace("ي","ی"))+1)+""
                    ,(levels.indexOf(c.getString("level").replace("ي","ی"))+1)+"" );
            lang.setId(c.getInt("id"));
            listLang.add(lang);
            oldLang.add(lang);
            listAdapterLang.notifyDataSetChanged();
            justifyListViewHeightBasedOnChildren(listViewLang);
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
        for(final Lang item: listLang){
            ids.add(item.getId());
        }

        for (final Lang item : oldLang) {
            String req = "";
            if(item.getId() == 0){
                if (ids.contains(item.getId()))
                {
                    req="https://api.mrehya.com/v1/user/language";
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
                    for (Lang item2: listLang) {
                        if(item2.getId() == item.getId()){
                            item.setLevel(item2.getLevel());
                            item.setName(item2.getName());
                            break;
                        }
                    }
                }
                req = "https://api.mrehya.com/v1/user/language?id="+item.getId();
            }

            String tag_string_req = "req_resumelanguage";
            final String finalResumeid = resumeid;
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    req, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("req_resumelanguage", "resumelanguage Response: " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        String success = jObj.getString("success");

                        // Check for error node in json
                        if (success.equalsIgnoreCase("true")) {
                            if(Language.equals("fa")){
                                Toast.makeText(ResumeLanguagesActivity.this, "با موفقیت بروزرسانی شد", Toast.LENGTH_SHORT).show(); }
                            else{
                                Toast.makeText(ResumeLanguagesActivity.this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("req_resumelanguage", "failed to set language");
                        }
                    } catch (JSONException e) {
                        // JSON error
                        Log.d("req_resumelanguage", "failed to set language error 1 ");
                        e.printStackTrace();
                        if(Language.equals("fa")){
                            Toast.makeText(ResumeLanguagesActivity.this, "مشکلی در بروزرسانی رزومه تان بوجود آمده است!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ResumeLanguagesActivity.this, "Problem on updating Resume!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("req_resumelanguage", "failed to set language  error2 : " + error.getMessage());
                    if(error.networkResponse.statusCode==403)
                    {
                        User u = new User(ResumeLanguagesActivity.this);
                        u.checkLogin(session.getUserDetails().getEmail(), session.getUserDetails().getPassword());
                        if(Language.equals("fa")){
                            Toast.makeText(ResumeLanguagesActivity.this, "اشکال اتصال به شبکه(در بروزرسانی رزومه) کمی بعد دوباره امتحان کنید", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(ResumeLanguagesActivity.this, "Network Connection or Resume Server failed! try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        if(Language.equals("fa")){
                            Toast.makeText(ResumeLanguagesActivity.this, "اشکال اتصال به شبکه(در بروزرسانی رزومه)", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(ResumeLanguagesActivity.this, "Network Connection or Resume Server failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    Log.e("default", item.getName()+","+ item.getLevel());
                    params.put("LangResume[language]", item.getName());
                    params.put("LangResume[level]", item.getLevel());
                    params.put("LangResume[resumeId]", finalResumeid);
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

    //methods
    private void setLangList(){
        listLang = new ArrayList<>();
        listAdapterLang = new ListAdapterLang(listLang,getApplicationContext(),ResumeLanguagesActivity.this,listViewLang);
    }
    public boolean existslang(String name, String level){
        for (Lang item: listLang) {
            if(item.getName().equals(name) && item.getLevel().equals(level))
                return true;
        }
        return false;
    }
    private void showDialogAddLang(final ListView listView){
        final Dialog dialog = new Dialog(ResumeLanguagesActivity.this);
        dialog.setContentView(R.layout.list_dialog_lang);
        //final EditText txtEdit = dialog.findViewById(R.id.txtEditDialogLang);
        final Spinner spinnerLevel = dialog.findViewById(R.id.spinnerLangLevel);
        final Spinner spinnerLang = dialog.findViewById(R.id.spinnerLang);

        LinearLayout LinearLayout1 = dialog.findViewById(R.id.LinearLayoutmain);

        TextView btnEditd = dialog.findViewById(R.id.btnEditd);


        if(Language.equals("fa"))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayout1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                spinnerLang.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                spinnerLang.setGravity(Gravity.RIGHT);
                spinnerLang.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            }
            btnEditd.setText("اضافه کردن");

            ArrayAdapter<String> Leveladapter = new ArrayAdapter<String>(
                    context,
                    android.R.layout.simple_spinner_dropdown_item,
                    resources.getStringArray(R.array.langLevels)
            );
            Leveladapter.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_rtl);
            spinnerLevel.setAdapter(Leveladapter);

            ArrayAdapter<String> Langadapter = new ArrayAdapter<String>(
                    context,
                    R.layout.z_simple_spinner_item_rtl,
                    resources.getStringArray(R.array.langs)
            );
            Langadapter.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_rtl);
            spinnerLang.setAdapter(Langadapter);
        }

        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayout1.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                spinnerLang.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            btnEditd.setText("Add");

            ArrayAdapter<String> Leveladapter = new ArrayAdapter<String>(
                    context,
                    R.layout.z_simple_spinner_item_ltr,
                    levels
            );
            Leveladapter.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_ltr);
            spinnerLevel.setAdapter(Leveladapter);

            ArrayAdapter<String> Langadapter = new ArrayAdapter<String>(
                    context,
                    R.layout.z_simple_spinner_item_ltr,
                    names
            );
            Langadapter.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_ltr);
            spinnerLang.setAdapter(Langadapter);
        }

        ImageButton btnEdit = dialog.findViewById(R.id.btnEditDialogLang);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e( "add"
                        ,names.indexOf( spinnerLang.getSelectedItem().toString())+"," +levels.indexOf(spinnerLevel.getSelectedItem().toString()));
                if(!(existslang((names.indexOf( spinnerLang.getSelectedItem().toString()))+""
                        ,(levels.indexOf(spinnerLevel.getSelectedItem().toString())+1)+""))){

                   Lang lang = new Lang((names.indexOf( spinnerLang.getSelectedItem().toString())+1)+""
                            ,(levels.indexOf(spinnerLevel.getSelectedItem().toString())+1)+"");
                    lang.setId(0);
                    listLang.add(lang);
                    oldLang.add(lang);
                    listAdapterLang.notifyDataSetChanged();
                    justifyListViewHeightBasedOnChildren(listView);
                    dialog.dismiss();
                }
                else
                    Log.e("LANG", "Exists");
            }
        });
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
}
