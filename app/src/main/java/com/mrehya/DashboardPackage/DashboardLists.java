package com.mrehya.DashboardPackage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mrehya.AppConfig;
import com.mrehya.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hgjhghgjh on 8/9/2018.
 */

public class DashboardLists {
    public static JSONObject Companiesdata = new JSONObject();
    public static JSONObject Testsdata = new JSONObject();
    public static JSONObject Productsdata = new JSONObject();
    private ProgressDialog pDialog;
    private Boolean show;
    public DashboardLists(Context context, Boolean show){
        if(Companiesdata.length()==0 || Testsdata.length()==0 || Productsdata.length()==0)
        {
            this.show = show;
            if(show)
            {
                pDialog = new ProgressDialog(context);
                startDialog();
            }
            new ApiTask().execute(0);
        }
    }

    public DashboardLists(String Listname){
        if(Listname.equals("Products") || Productsdata.length()==0)
            new ApiTask().execute(0);
    }

    //APIS
    class ApiTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                Thread.sleep(500);
                // some long running task will run here. We are using sleep as a dummy to delay execution
                if(Testsdata.length()==0)
                    Exam_Api();

                if(Companiesdata.length()==0)
                    prepareCompanies();

                if(Productsdata.length()==0)
                    Product_Api();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
    private void Exam_Api(){
        String tag_string_req = "req_Exams";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_DashExam+"2", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d("TAG", "Exams Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    Testsdata = jObj.getJSONObject("data");
                    hideDialog();
                } catch (JSONException e) {
                    Log.d("TAG", "error 1 " + e.getMessage());
                    hideDialog();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",  "error2: " + error.getMessage());
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
    private void prepareCompanies() {
        String tag_string_req = "req_Companies";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Hires, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Log.d("TAG", "Company Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    Companiesdata = jObj.getJSONObject("data");
                } catch (JSONException e) {
                    Log.d("TAG", "error 1 " + e.getMessage());
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",  "error2: "+ error.getMessage());
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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void Product_Api() {
        String tag_string_req = "req_Products";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Products, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    Productsdata = jObj.getJSONObject("data");
                } catch (JSONException e) {
                    Log.d("Productsdata", "error 1 " + e.getMessage());
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Productsdata",  "error2: "+ error.getMessage());
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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    //methods
    public void setProductsdata(JSONObject data){
        Productsdata = data;
    }
    public int getCompanyListCount(){
        return Companiesdata.length();
    }
    public int getTestsdataCount(){
        return Testsdata.length();
    }
    private void startDialog(){
        pDialog.setCancelable(false);
        pDialog.setMessage("Welcome to Ehya");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showDialog();
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if(show)
            if (pDialog.isShowing())
                pDialog.dismiss();
    }
}
