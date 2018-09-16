package com.mrehya.Hire;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mrehya.AppConfig;
import com.mrehya.AppController;
import com.mrehya.R;
import com.mrehya.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class ShowHireStatus extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HireAdapter adapter;
    private List<HireCompanies> companyList;
    private SessionManager session;
    //new
    private ProgressDialog pDialog;
    private TextView txtEmptyCompaniesReqs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_hire_status);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        session = new SessionManager(getApplicationContext());
        txtEmptyCompaniesReqs = (TextView) findViewById(R.id.txtEmptyCompaniesReqs);

        companyList = new ArrayList<>();
        adapter = new HireAdapter(getApplicationContext(), companyList, session);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        //new
        Paper.init(this);
        String Language = updateLanguage();
        startDialog(Language, this);
    }

    private void Hire_Api(final String Language){

        String tag_string_req = "req_Province";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Hires, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Company Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject data = jObj.getJSONObject("data");
                    if(data.length()>0){
                        txtEmptyCompaniesReqs.setVisibility(View.GONE);
                        for (int i = 0; i < data.length()-1; i++) {
                            JSONObject c = data.getJSONObject(i+"");
                            Log.d("TAG", c.toString());
                            HireCompanies a = new HireCompanies(c.getInt("id"),c.getString("image"),c.getString("brand_name"),c.getString("brand_name"),c.getString("job_title"),c.getString("province")
                            ,c.getString("job_description"));
                            companyList.add(a);
                        }

                        adapter.notifyDataSetChanged();
                    }
                    else{
                        if(Language.equals("fa"))
                            txtEmptyCompaniesReqs.setText("لیست شرکت\u200Cها خالی است");
                        else
                            txtEmptyCompaniesReqs.setText("No Comapanies Loaded!");
                        txtEmptyCompaniesReqs.setVisibility(View.VISIBLE);
                    }
                    Log.d("TAG", "No Object recieved!");
                    hideDialog();
                } catch (JSONException e) {
                    // JSON error
                    Log.d("TAG", "error 1 " + e.getMessage());
                    //e.printStackTrace();
                    if(Language.equals("fa")){
                        txtEmptyCompaniesReqs.setText("لیست شرکت\u200Cها خالی است");
                        Toast.makeText(getApplicationContext(), "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        txtEmptyCompaniesReqs.setText("No Comapanies Loaded!");
                        Toast.makeText(getApplicationContext(), "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                    }
                    txtEmptyCompaniesReqs.setVisibility(View.VISIBLE);

                    hideDialog();
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",  "error2");
                if(Language.equals("fa")){
                    txtEmptyCompaniesReqs.setText("لیست شرکت\u200Cها خالی است");
                    Toast.makeText(getApplicationContext(), "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                }

                else{
                    txtEmptyCompaniesReqs.setText("No Comapanies Loaded!");
                    Toast.makeText(getApplicationContext(), "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                }
                txtEmptyCompaniesReqs.setVisibility(View.VISIBLE);
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
    private void startDialog(String Language, Context contextt){
        pDialog.setCancelable(false);
        pDialog.setMessage("گرفتن لیست شرکت\u200Cها...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showDialog(Language);
    }

    private void showDialog(final String Language) {
            if(!(pDialog.isShowing()))
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.show();
                        Hire_Api(Language);
                    }
                }, 1000);

    }

    private void hideDialog() {
        if(pDialog.isShowing())
            pDialog.dismiss();
    }


    private String updateLanguage(){
        //Default language is fa
        String language = Paper.book().read("language");
        if(language==null)
            Paper.book().write("language", "fa");
        return language;
    }

}
