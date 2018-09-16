package com.mrehya;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mrehya.DashboardPackage.DashboardLists;
import com.mrehya.Helper.LocaleHelper;
import com.mrehya.Hire.HireAdapter;
import com.mrehya.Hire.HireCompanies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;


public class HireFragment extends Fragment {

    private SessionManager session;
    private RecyclerView recyclerView;
    private HireAdapter adapter;
    private List<HireCompanies> companyList;

    //new
    private String Language;
    MyTextView mytextHirePerson;

    //filters
    private Button btnNavigation,btnJobCats,btnIntouch;
    boolean hasnav=false, hasjobcat=false, hastouch=false;
    boolean[] CheckedNavigations,CheckedIntouchs,CheckedJobcats;
    ArrayList<Integer> UserNavigations =  new ArrayList<>();
    ArrayList<Integer> UserIntouchs =  new ArrayList<>();
    ArrayList<Integer> UserJobcats  =  new ArrayList<>();

    AlertDialog.Builder mbuilderNav,mbuilderjobCat,mbuilderIntouchs;
    private ProgressDialog pDialog;
    private TextView txtEmptyHires;
    View view;
    Context context;

    private String jobcats="", contracts="", provinces="", status ="0", hireStatus="0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_hire, container, false);
        context =view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        session = new SessionManager(this.getContext());
        pDialog = new ProgressDialog(context);

        //Filters
        mbuilderNav = new AlertDialog.Builder(getActivity());
        mbuilderjobCat = new AlertDialog.Builder(getActivity());
        mbuilderIntouchs = new AlertDialog.Builder(getActivity());

        btnNavigation = (Button)view.findViewById(R.id.btnNavigation);
        //btnNavigation.setVisibility(view.GONE);
        btnNavigation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(hasjobcat){
                    AlertDialog navdialog = mbuilderNav.create();
                    navdialog.show();
                }
                else{
                    if(Language.equals("fa"))
                    {
                        if(!(hasnav))
                            Toast.makeText(context, "در حال بارگزاری لیست استان\u200Cها...", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(!(hasnav))
                            Toast.makeText(context, "Loading Provinces...", Toast.LENGTH_SHORT).show();
                    }
                    Province_Api(Language);
                }
            }
        });
        btnJobCats = view.findViewById(R.id.btnJobCats);
        //btnJobCats.setVisibility(view.GONE);
        btnJobCats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasjobcat){
                    AlertDialog jobcatdialog = mbuilderjobCat.create();
                    jobcatdialog.show();
                }
                else{
                    if(Language.equals("fa")){
                        if (!(hasjobcat))
                            Toast.makeText(context, "در حال بارگزاری لیست دسته\u200Cبندی\u200Cهای شغلی...", Toast.LENGTH_SHORT).show();
                    }
                     else
                    {
                        if(!(hasjobcat))
                            Toast.makeText(context, "Loading JobCategories...", Toast.LENGTH_SHORT).show();
                    }
                    Jobcat_Api(Language);
                }

            }
        });
        btnIntouch = view.findViewById(R.id.btnIntouch);
        //btnIntouch.setVisibility(view.GONE);
        btnIntouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hastouch){
                    AlertDialog Intouchsdialog = mbuilderIntouchs.create();
                    Intouchsdialog.show();
                }
                else{
                    if(Language.equals("fa"))
                    {
                        if(!(hastouch))
                            Toast.makeText(context, "در حال بارگزاری لیست قراردادها...", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(!(hastouch))
                            Toast.makeText(context, "Loading Corporations...", Toast.LENGTH_SHORT).show();
                    }
                    Intouch_Api(Language);
                }
            }
        });
        //new
        Paper.init(getActivity());
        mytextHirePerson = view.findViewById(R.id.mytextHirePerson);
        txtEmptyHires = view.findViewById(R.id.txtEmptyHires);
        Language = updateLanguage();
        //new

        reloadlist();
        fastList();
        new LoadAllTask().execute(0);
        updateView(Language);
        return view;
    }

    private void fastList(){
        if(jobcats.equals("") && contracts.equals("") && provinces.equals("") && DashboardLists.Companiesdata.length()>0){
            JSONObject data = DashboardLists.Companiesdata;
            try {
                if(data.getInt("totalPage")==0){
                    if(Language.equals("fa"))
                        txtEmptyHires.setText("تبلیغی یافت نشد");
                    else
                        txtEmptyHires.setText("Empty Hire Advertisements!");
                    txtEmptyHires.setVisibility(View.VISIBLE);
                }
                else{

                    if(data.length()>0){
                        txtEmptyHires.setVisibility(View.GONE);
                        for(int i = 0; i < data.length()-1; i++) {
                            JSONObject c = data.getJSONObject(i+"");
                            //Log.d("TAG", c.toString());
                            HireCompanies a = new HireCompanies(c.getInt("id"),c.getString("image"),c.getString("job_title"),c.getString("brand_name"),c.getString("job_title"),c.getString("province")
                                    ,c.getString("job_description"));
                            companyList.add(a);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        if(Language.equals("fa"))
                            txtEmptyHires.setText("لیست تبلیغ\u200Cها خالی است!\"");
                        else
                            txtEmptyHires.setText("Empty Hire Advertisements!");
                        txtEmptyHires.setVisibility(View.VISIBLE);
                    }
                }
                hideDialog();
            } catch (JSONException e) {
                hideDialog();
                e.printStackTrace();
            }
            hireStatus="1";
            hideDialog();
        }
        else{
            new LoadAllTask().execute(0);
        }
    }
    class LoadAllTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                Thread.sleep(1000);
                if(!(jobcats.equals("") && contracts.equals("") && provinces.equals("") && DashboardLists.Companiesdata.length()>0)) {
                    Hire_Api(Language);
                }
                if(status.equals("0")){
                    Intouch_Api(Language);
                    Province_Api(Language);
                    Jobcat_Api(Language);
                    status="1";
                }
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
            // do whatever needs to be done next
        }
        @Override
        protected void onPreExecute() {
            if(Language.equals("fa")){
                Toast.makeText(context, "در حال بارگزاری...", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
    private void Hire_Api(final String Language){
            String tag_string_req = "req_hires";
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_Hires+Language, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    //Log.d("req_hires", "Hires Response: " + response.toString());
                    try {
                        JSONObject jObj = new JSONObject(response);
                        JSONObject data = jObj.getJSONObject("data");
                        if(data.getInt("totalPage")==0){
                            if(Language.equals("fa"))
                                txtEmptyHires.setText("تبلیغی یافت نشد");
                            else
                                txtEmptyHires.setText("Empty Hire Advertisements!");
                            txtEmptyHires.setVisibility(View.VISIBLE);
                        }
                        else{

                            if(data.length()>0){
                                txtEmptyHires.setVisibility(View.GONE);
                                for(int i = 0; i < data.length()-1; i++) {
                                    JSONObject c = data.getJSONObject(i+"");
                                    //Log.d("TAG", c.toString());
                                    HireCompanies a = new HireCompanies(c.getInt("id"),c.getString("image"),c.getString("job_title"),c.getString("brand_name"),c.getString("job_title"),c.getString("province")
                                            ,c.getString("job_description"));
                                    companyList.add(a);
                                }
                                adapter.notifyDataSetChanged();
                            }
                            else{
                                if(Language.equals("fa"))
                                    txtEmptyHires.setText("لیست تبلیغ\u200Cها خالی است!\"");
                                else
                                    txtEmptyHires.setText("Empty Hire Advertisements!");
                                txtEmptyHires.setVisibility(View.VISIBLE);
                            }
                        }

                        //Log.d("req_hires", "No Object recieved!");
                        hideDialog();
                    } catch (JSONException e) {
                        // JSON error
                        Log.d("req_hires", "error 1 " + e.getMessage());
                        //e.printStackTrace();
                        if(Language.equals("fa")){
                            txtEmptyHires.setText("لیست تبلیغ\u200Cها خالی است");
                            Toast.makeText(context, "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            txtEmptyHires.setText("Empty Hire Advertisements!");
                            Toast.makeText(context, "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                        }
                        txtEmptyHires.setVisibility(View.VISIBLE);
                        hideDialog();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("req_hires",  "error2"+" "+ error.getMessage());
                    if(Language.equals("fa")){
                        txtEmptyHires.setText("لیست تبلیغ\u200Cها خالی است");
                        Toast.makeText(context, "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        txtEmptyHires.setText("Empty Hire Advertisements!");
                        Toast.makeText(context, "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                    }
                    txtEmptyHires.setVisibility(View.VISIBLE);
                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();


                    params.put("provinces", provinces);
                    params.put("corporations", contracts);
                    params.put("categories", jobcats);

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
    private void Province_Api(final String Language){
        String tag_string_req = "req_Province";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Provinces, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d("Navigations", "Provinces Response: " + response.toString());
                try {

                    JSONObject jObj = new JSONObject(response);
                    JSONArray resarray = jObj.getJSONArray("data");
                    if(resarray.length()>0){
                        String[][] ListNavigations = new String[resarray.length()][resarray.length()];
                        hasnav=true;
                        if(Language.equals("fa")) {
                            for (int i = 0; i < resarray.length(); i++) {
                                ListNavigations[0][i] = resarray.getJSONObject(i).getString("name");
                                ListNavigations[1][i] = resarray.getJSONObject(i).getString("id");
                            }
                        }else{
                            for (int i = 0; i < resarray.length(); i++) {
                                ListNavigations[0][i] = resarray.getJSONObject(i).getString("name_en");
                                ListNavigations[1][i] = resarray.getJSONObject(i).getString("id");
                            }
                        }

                        CheckedNavigations = new boolean[ListNavigations.length];
                        setFilters(Language, "province", ListNavigations);
                        //Log.d("Navigations", resarray.get(0).toString());
                    }
                    else{
                        hasnav=false;
                        String[][] ListNavigations = new String[0][0];
                        setFilters(Language, "province", ListNavigations);
                    }

                } catch (JSONException e) {
                    // JSON error
                    Log.d("Navigations", "error 1 ");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Navigations",  "error2");
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
    private void Jobcat_Api(final String Language){
        String tag_string_req = "req_Province";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_JobCats + Language, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               // Log.d("JobCats", "Jobcats Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray resarray = jObj.getJSONArray("data");
                    if(resarray.length()>0){
                        hasjobcat=true;
                        String[][] ListNavigations = new String[resarray.length()][resarray.length()];
                            for (int i = 0; i < resarray.length(); i++) {
                                ListNavigations[0][i] = resarray.getJSONObject(i).getString("title");
                                ListNavigations[1][i] = resarray.getJSONObject(i).getString("id");
                            }
                        CheckedJobcats = new boolean[ListNavigations.length];
                        setFilters(Language, "jobcats", ListNavigations);
                        //Log.d("JobCats", resarray.get(0).toString());
                    }
                    else {
                        hasjobcat=false;
                        String[][] ListNavigations = new String[0][0];
                        setFilters(Language, "jobcats", ListNavigations);
                    }

                } catch (JSONException e) {
                    // JSON error
                    Log.d("JobCats", "error 1 ");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("JobCats",  "error2");
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
    private void Intouch_Api(final String Language){
        String tag_string_req = "req_Province";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Corporations + Language, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d("Corporations", "Intouchs Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray resarray = jObj.getJSONArray("data");
                    if(resarray.length()>0){
                        hastouch=true;
                        String[][] ListNavigations = new String[resarray.length()][resarray.length()];
                        if(Language.equals("fa")){
                            for (int i = 0; i < resarray.length(); i++) {
                                ListNavigations[0][i] = resarray.getJSONObject(i).getString("name");
                                ListNavigations[1][i] = resarray.getJSONObject(i).getString("code");
                            }
                        }
                        else {
                            for (int i = 0; i < resarray.length(); i++) {
                                ListNavigations[0][i] = resarray.getJSONObject(i).getString("name_other");
                                ListNavigations[1][i] = resarray.getJSONObject(i).getString("code");
                            }
                        }
                        CheckedIntouchs = new boolean[ListNavigations.length];
                        setFilters(Language, "intouch", ListNavigations);
                        //Log.d("Corporations", resarray.get(0).toString());
                    }
                    else{
                        hastouch=false;
                        String[][] ListNavigations = new String[0][0];
                        setFilters(Language, "intouch", ListNavigations);
                    }
                } catch (JSONException e) {
                    // JSON error
                    Log.d("Corporations", "error 1 " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",  "error2");
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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void reloadlist(){
        companyList = new ArrayList<>();

        adapter = new HireAdapter(getContext(), companyList, session);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        startDialog();
    }

    //Hire Api , transactions
    private void setFilters(String Language, String filtername, final String[][] listfilter){

        if(Language.equals("fa")) {

            if (filtername.equals("province")) {
                //NAVIGATION

                        if(listfilter.length>0)
                        {
                        mbuilderNav.setTitle("لیست استان\u200Cها");
                        mbuilderNav.setMultiChoiceItems(listfilter[0], CheckedNavigations, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                if (b) {
                                    if (!UserNavigations.contains(i)) {
                                        UserNavigations.add(i);
                                    }
                                } else if (UserNavigations.contains(i)) {
                                    UserNavigations.remove(UserNavigations.indexOf(i));
                                }
                            }
                        });
                        mbuilderNav.setCancelable(false);
                        mbuilderNav.setPositiveButton("اعمال کن", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                provinces="";
                                String item = "";
                                for (int p = 0; p < UserNavigations.size(); p++) {
                                    item = item + listfilter[1][UserNavigations.get(p)];
                                    if (p != UserNavigations.size() - 1)
                                        item = item + ",";
                                }
                                //Log.d("FILTERS OF CITY", item);
                                provinces = item;
                                reloadlist();
                                fastList();
                            }
                        });
                        mbuilderNav.setNegativeButton("بستن", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        mbuilderNav.setNeutralButton("پاک کردن همه", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for (int p = 0; p < CheckedNavigations.length; p++) {
                                    CheckedNavigations[p] = false;
                                    UserNavigations.clear();
                                }
                                provinces="";
                                reloadlist();
                                fastList();
                            }
                        });
                    }
            } else if (filtername.equals("jobcats")) {
                //JobCats

                        if (listfilter.length > 0) {
                            mbuilderjobCat.setTitle("لیست دسته\u200Cبندی\u200Cهای شغلی");
                            mbuilderjobCat.setMultiChoiceItems(listfilter[0], CheckedJobcats, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                    if (b) {
                                        if (!UserJobcats.contains(i)) {
                                            UserJobcats.add(i);
                                        }
                                    } else if (UserJobcats.contains(i)) {
                                        UserJobcats.remove(UserJobcats.indexOf(i));
                                    }
                                }
                            });
                            mbuilderjobCat.setCancelable(false);
                            mbuilderjobCat.setPositiveButton("اعمال کن", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    jobcats="";
                                    String item = "";
                                    for (int p = 0; p < UserJobcats.size(); p++) {
                                        item = item + listfilter[1][UserJobcats.get(p)];
                                        if (p != UserJobcats.size() - 1)
                                            item = item + ",";
                                    }
                                    Log.d("FILTERS OF JobCats", "onClick: " + item);
                                    jobcats=item;
                                    reloadlist();
                                    fastList();
                                }
                            });
                            mbuilderjobCat.setNegativeButton("بستن", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            mbuilderjobCat.setNeutralButton("پاک کردن همه", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    for (int p = 0; p < CheckedJobcats.length; p++) {
                                        CheckedJobcats[p] = false;
                                        UserJobcats.clear();
                                    }
                                    jobcats="";
                                    reloadlist();
                                    fastList();
                                }
                            });
                        }
            }
            else if(filtername.equals("intouch")) {

                //Intouch
                        if(listfilter.length>0) {
                            mbuilderIntouchs.setTitle("لیست قراردادها");
                            mbuilderIntouchs.setMultiChoiceItems(listfilter[0], CheckedIntouchs, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                    if (b) {
                                        if (!UserIntouchs.contains(i)) {
                                            UserIntouchs.add(i);
                                        }
                                    } else if (UserIntouchs.contains(i)) {
                                        UserIntouchs.remove(UserIntouchs.indexOf(i));
                                    }
                                }
                            });
                            mbuilderIntouchs.setCancelable(false);
                            mbuilderIntouchs.setPositiveButton("اعمال کن", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    contracts="";
                                    String item = "";
                                    for (int p = 0; p < UserIntouchs.size(); p++) {
                                        item = item + listfilter[1][UserIntouchs.get(p)];
                                        if (p != UserIntouchs.size() - 1)
                                            item = item + ",";
                                    }
                                    Log.d("FILTERS OF JobCats", "onClick: " + item);
                                    contracts=item;
                                    reloadlist();
                                    fastList();
                                }
                            });
                            mbuilderIntouchs.setNegativeButton("بستن", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            mbuilderIntouchs.setNeutralButton("پاک کردن همه", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    for (int p = 0; p < CheckedIntouchs.length; p++) {
                                        CheckedIntouchs[p] = false;
                                        UserIntouchs.clear();
                                    }
                                    contracts="";
                                    reloadlist();
                                    fastList();
                                }
                            });
                        }
            }
        }
        else{
            //ENGLISH
            if (filtername.equals("province")) {
                //NAVIGATIOM
                        if(listfilter.length>0) {
                            mbuilderNav.setTitle("List of provinces");
                            mbuilderNav.setMultiChoiceItems(listfilter[0], CheckedNavigations, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                    if (b) {
                                        if (!UserNavigations.contains(i)) {
                                            UserNavigations.add(i);
                                        }
                                    } else if (UserNavigations.contains(i)) {
                                        UserNavigations.remove(UserNavigations.indexOf(i));
                                    }
                                }
                            });
                            mbuilderNav.setCancelable(false);
                            mbuilderNav.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String item = "";
                                    provinces="";
                                    for (int p = 0; p < UserNavigations.size(); p++) {
                                        item = item + listfilter[1][UserNavigations.get(p)];
                                        if (p != UserNavigations.size() - 1)
                                            item = item + ",";
                                    }
                                    Log.d("FILTERS OF CITY", "onClick: " + item);
                                    provinces=item;
                                    reloadlist();
                                    fastList();
                                }
                            });
                            mbuilderNav.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            mbuilderNav.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    for (int p = 0; p < CheckedNavigations.length; p++) {
                                        CheckedNavigations[p] = false;
                                        UserNavigations.clear();
                                    }
                                    provinces="";
                                    reloadlist();
                                    fastList();
                                }
                            });
                        }
            }
            else if (filtername.equals("jobcats")) {
                //JobCats
                        if(listfilter.length>0) {
                            mbuilderjobCat.setTitle("Job Categories");
                            mbuilderjobCat.setMultiChoiceItems(listfilter[0], CheckedJobcats, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                    if (b) {
                                        if (!UserJobcats.contains(i)) {
                                            UserJobcats.add(i);
                                        }
                                    } else if (UserJobcats.contains(i)) {
                                        UserJobcats.remove(UserJobcats.indexOf(i));
                                    }
                                }
                            });
                            mbuilderjobCat.setCancelable(false);
                            mbuilderjobCat.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    jobcats="";
                                    String item = "";
                                    for (int p = 0; p < UserJobcats.size(); p++) {
                                        item = item + listfilter[1][UserJobcats.get(p)];
                                        if (p != UserJobcats.size() - 1)
                                            item = item + ",";
                                    }
                                    Log.d("FILTERS OF JobCats", "onClick: " + item);
                                    jobcats=item;
                                    reloadlist();
                                    fastList();
                                }
                            });
                            mbuilderjobCat.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            mbuilderjobCat.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    for (int p = 0; p < CheckedJobcats.length; p++) {
                                        CheckedJobcats[p] = false;
                                        UserJobcats.clear();
                                    }
                                    jobcats="";
                                    reloadlist();
                                    fastList();
                                }
                            });
                        }
            }
            else if (filtername.equals("intouch")) {
                //Intouch
                        if(listfilter.length>0) {
                            mbuilderIntouchs.setTitle("Corporations");
                            mbuilderIntouchs.setMultiChoiceItems(listfilter[0], CheckedIntouchs, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                    if (b) {
                                        if (!UserIntouchs.contains(i)) {
                                            UserIntouchs.add(i);
                                        }
                                    } else if (UserIntouchs.contains(i)) {
                                        UserIntouchs.remove(UserIntouchs.indexOf(i));
                                    }
                                }
                            });
                            mbuilderIntouchs.setCancelable(false);
                            mbuilderIntouchs.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    contracts="";
                                    String item = "";
                                    for (int p = 0; p < UserIntouchs.size(); p++) {
                                        item = item + listfilter[1][UserIntouchs.get(p)];
                                        if (p != UserIntouchs.size() - 1)
                                            item = item + ",";
                                    }
                                    Log.d("FILTERS OF Intouchs", "onClick: " + item);
                                    contracts=item;
                                    reloadlist();
                                    fastList();
                                }
                            });
                            mbuilderIntouchs.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            mbuilderIntouchs.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    for (int p = 0; p < CheckedIntouchs.length; p++) {
                                        CheckedIntouchs[p] = false;
                                        UserIntouchs.clear();
                                    }
                                    contracts="";
                                    reloadlist();
                                    fastList();
                                }
                            });
                        }
            }
        }
    }
    //Progress Dialog
    private void startDialog(){
        pDialog.setCancelable(true);
        if(Language.equals("fa"))
            pDialog.setMessage("گرفتن لیست آگهی\u200Cهای استخدام...");
        else
            pDialog.setMessage("Loading Hiring Advertisements...");
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
    //Translation
    private String updateLanguage(){
        //Default language is fa
        String language = Paper.book().read("language");
        if(language==null)
            Paper.book().write("language", "fa");
        return language;
    }
    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(getActivity(), language);
        Resources resources = context.getResources();
        mytextHirePerson.setText(resources.getString(R.string.HirePerson));

        //condition to check language
        if(Language.equals("fa")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                recyclerView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                recyclerView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }
        btnNavigation.setText(resources.getString(R.string.Province));
        btnJobCats.setText(resources.getString(R.string.JobCategory2));
        btnIntouch.setText(resources.getString(R.string.Cooperation));
    }

}
