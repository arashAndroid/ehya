package com.mrehya.Reserv;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.sbahmani.jalcal.util.DateException;
import com.github.sbahmani.jalcal.util.JalCal;
import com.goodiebag.horizontalpicker.HorizontalPicker;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.icu.impl.text.RbnfScannerProviderImpl;
import com.ibm.icu.text.Edits;
import com.ibm.icu.text.NumberFormat;
import com.mrehya.AppConfig;
import com.mrehya.AppController;
import com.mrehya.Helper.LocaleHelper;
import com.mrehya.Job;
import com.mrehya.Login;
import com.mrehya.MainActivity;
import com.mrehya.R;
import com.mrehya.Resume.ResumeJobexpActivity;
import com.mrehya.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.paperdb.Paper;
import me.anwarshahriar.calligrapher.Calligrapher;

public class Reserve extends AppCompatActivity {
    private String Language="fa";
    private Resources resources;
    private Context context;
    private SessionManager session;

    private ProgressDialog addbtnDialog;
    private ProgressDialog firstdayDialog;
    private ProgressDialog reservedayDialog;
    private Dialog dialog;

    private int cuurent_month, cuurent_year, cuurent_day;
    private Persian_Date_Methods dm;
    private PersianCalendar pc;
    private Day day;

    HashMap<Integer, String> reqs;
    //ELEMENTS
    private TableLayout tableButtons;
    private Spinner spinnerRequest;
    private List<HorizontalPicker.PickerItem> day_textItems;
    private HorizontalPicker hpText;
    private HorizontalScrollView hsv;
    private TextView tv_time_message, tv_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        session = new SessionManager(getApplicationContext());
        updatelanguage();
        addbtnDialog = new ProgressDialog(this);
        addbtnDialog.setTitle("در حال بارگزاری...");
        firstdayDialog = new ProgressDialog(this);
        firstdayDialog.setTitle("در حال بارگزاری...");
        reservedayDialog = new ProgressDialog(this);
        reservedayDialog.setTitle("بررسی نتیجه رزرو...");

        findViews();
        setViews();
        removeTableViews();
        tv_time_message.setVisibility(View.GONE);
        //new GetbtnsTask().execute(0);
        scrolltoday();
    }
    private void findViews(){
        context = Reserve.this;
        dm = new Persian_Date_Methods();
        pc = new PersianCalendar();
        makedialog("در حال بارگزاری..."  , addbtnDialog);
        makedialog("در حال بارگزاری..."  , firstdayDialog);
        showDialog(addbtnDialog);
        day = new Day(pc.year+"",pc.month+"",pc.day+"");
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "BHoma.ttf", true);
        calligrapher.setFont(getCurrentFocus(), "BHoma.ttf");
        cuurent_year    = pc.year;
        cuurent_month   = pc.month;
        cuurent_day     = pc.day;

        tableButtons = (TableLayout) findViewById(R.id.tableButtons);
        tv_time_message = (TextView) findViewById(R.id.tv_time_message);
        tv_month = (TextView) findViewById(R.id.tv_month);
        spinnerRequest = (Spinner) findViewById(R.id.spinnerRequest);
        reqs = new HashMap<Integer, String>();
    }
    private void setViews(){
        tv_month.setText(day.getYear() + " "+dm.get_month(cuurent_month).toString());
        hpText = (HorizontalPicker) findViewById(R.id.hpicker);
        HorizontalPicker.OnSelectionChangeListener listener = new HorizontalPicker.OnSelectionChangeListener() {
            @Override
            public void onItemSelect(HorizontalPicker picker, int index) {
                day = new Day(day.getYear(), day.getMonth(), index+1+"");
                removeTableViews();
                showDialog(addbtnDialog);
                tv_time_message.setVisibility(View.GONE);
                new GetbtnsTask().execute(0);
            }
        };
        new GetreqsTask().execute(0);
        //DAY
        int daycount = 29;
        if(cuurent_month<=6)
            daycount=30;
        else if(cuurent_month==12)
            daycount=28;
        day_textItems = dm.day(daycount);
        hpText.setItems(day_textItems,day.getintDay()-1);
        hpText.setChangeListener(listener);
        hsv = (HorizontalScrollView) findViewById(R.id.hsv);
    }
    private void updatelanguage(){
        Paper.init(Reserve.this);
        //Default language is fa
        Language = Paper.book().read("language");
        if(Language==null)
            Paper.book().write("language", "fa");
    }

    //methods
    class GetreqsTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                Thread.sleep(2000);
                get_reqs();
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
    private void get_reqs(){

        String tag_string_req = "req_reqs";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Get_RESERVE_REQUEST+Language, new Response.Listener<String>() {
            //day.getdate()
            @Override
            public void onResponse(String response) {
                Log.d("reqs", "Get reqs: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");
                    if (success.equalsIgnoreCase("true")) {
                        JSONArray c = jObj.getJSONArray("data");
                        for(int i=0;i<c.length();i++){
                            reqs.put(c.getJSONObject(i).getInt("code"), c.getJSONObject(i).getString("name"));
                        }
                        update_reqs();
                    }
                } catch (JSONException e) {
                    Log.e("reqs", "error 1 " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("reqs",  "eror 2: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void update_reqs(){
        String[] urlArray = new String[reqs.size()];
        if(reqs.size()>0)
            reqs.values().toArray(urlArray);
        ArrayAdapter<String> reqdapter = new ArrayAdapter<String>(
                this,
                R.layout.z_simple_spinner_item_center,
                urlArray
        );
        reqdapter.setDropDownViewResource(R.layout.z_simple_spinner_dropdown_item_center);
        spinnerRequest.setAdapter(reqdapter);
    }
    class GetbtnsTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                Thread.sleep(2000);
                get_set_btns();
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
            if(Language.equals("fa")){
                Toast.makeText(Reserve.this, "در حال دریافت ساعات خالی", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(Reserve.this, "Getting Free Hours", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
    private void get_set_btns(){
        String tag_string_req = "req_freeHourstoReserve";
        //"2018-03-11 day.getdate()
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Get_FreeHours+day.getdate(), new Response.Listener<String>() {
            //day.getdate()
            @Override
            public void onResponse(String response) {
                //Log.d("Reserve", "Get Free Hours: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");
                    if (success.equalsIgnoreCase("true")) {
                        JSONObject c = jObj.getJSONObject("data");
                        Day.setDayName(c.getString("dayName"));
                        Day.setStartTime(c.getString("startTime"));
                        Day.setEndTime(c.getString("endTime"));
                        Day.setId(c.getString("id"));

                        JSONObject hourobj = c.getJSONObject("hours");
                        Iterator keys =  hourobj.keys();
                        if(keys.hasNext())
                        {
                            removeTableViews();
                            tv_time_message.setVisibility(View.GONE);
                            TableRow tr= new TableRow(Reserve.this);
                            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            int i = 0;
                            while (keys.hasNext()){
                                final String time = (String)keys.next();
                                day.setHour(time);
                                if((i)%3==0){
                                    tableButtons.addView(tr);
                                    tr = new TableRow(Reserve.this);
                                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                }
                                Button b = new Button(Reserve.this);
                                b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 3));
                                b.setTextSize(20);
                                b.setText(topersian(time));
                                b.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        reserve_time(time);
                                    }
                                });
                                tr.addView(b);
                                i++;
                                if(!keys.hasNext()){
                                        tableButtons.addView(tr);
                                }
                            }
                        }
                        else{
                            ////////////////////////////////show no hour found
                        }
                        hideDialog(addbtnDialog);
                    } else {
                        showMessage("وقت خالی برای این روز یافت نشد");
                        if(Language.equals("fa")){
                            Toast.makeText(getApplicationContext(), "وقت خالی در این روز موجود نمی باشد", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "No Free_time found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    hideDialog(addbtnDialog);
                } catch (JSONException e) {
                    Log.e("Reserve", "error 1 " + e.getMessage());
                    e.printStackTrace();
                    showMessage("وقت خالی برای این روز یافت نشد");
                    if(Language.equals("fa")){
                        Toast.makeText(getApplicationContext(), "مشکلی در در داده ها پیش آمده است", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Problem on hours!", Toast.LENGTH_SHORT).show();
                    }
                }
                hideDialog(addbtnDialog);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Reserve",  "eror 2: " + error.getMessage());
                showMessage("وقت خالی برای این روز یافت نشد");
//                if(Language.equals("fa")){
//                    Toast.makeText(getApplicationContext(), "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
//                }
//
//                else{
//                    Toast.makeText(getApplicationContext(), "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
//                }
                hideDialog(addbtnDialog);
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    public void prev_month(View view){
        if(reqs.size()==0){
            new GetreqsTask().execute(0);
        }
        day.setMonth((day.getintMonth()-1)+"");

        if(day.getintMonth() < 1)
        {
            day.setMonth(12+"");
            day.setYear((day.getintYear()-1)+"");
        }
        TextView tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(day.getintYear() + " "+ dm.get_month(day.getintMonth()).toString());

        day_textItems.clear();
        hpText.removeAllViews();
        for(int d=0;d<=dm.day_counts(day.getintMonth(), day.getintYear());d++) {
            day_textItems.add(new HorizontalPicker.TextItem(dm.get_day(d)));
        }
        HorizontalPicker hpText = (HorizontalPicker) findViewById(R.id.hpicker);
//        if(!(is_today())){
//            day.setDay(1+"");
//        }
        hpText.setItems(day_textItems,day.getintDay()-1);
        hsv.postDelayed(new Runnable() {
            @Override
            public void run() {
                hsv.smoothScrollTo(day.getintDay()-1, 0);
                //hsv.fullScroll(HorizontalScrollView.FOCUS);
            }
        }, 100L);
    }
    public void next_month(View view){
        if(reqs.size()==0){
            new GetreqsTask().execute(0);
        }
        day.setMonth(day.getintMonth ()+ 1+"");
        if(day.getintMonth() >12) {
            day.setMonth("1");
            day.setYear(day.getintYear()+1+"");
        }
        TextView tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(day.getYear() + " "+ dm.get_month(day.getintMonth()).toString());

        day_textItems.clear();
        hpText.removeAllViews();
        for(int d=0;d<=dm.day_counts(day.getintMonth(), day.getintYear());d++) {
            day_textItems.add(new HorizontalPicker.TextItem(dm.get_day(d)));
        }
        HorizontalPicker hpText = (HorizontalPicker) findViewById(R.id.hpicker);

        if(!(is_today())){
            day.setDay("1");
        }
        hpText.setItems(day_textItems,day.getintDay()-1);
        hsv.postDelayed(new Runnable() {
            @Override
            public void run() {
                hsv.smoothScrollTo(day.getintDay()-1, 0);
                //hsv.fullScroll(HorizontalScrollView.Focus);
            }
        }, 100L);
    }
    private boolean is_today() {
        if(day.getintYear() != cuurent_year)
            return false;
        if(day.getintMonth() != cuurent_month)
            return false;
        if(day.getintDay() != cuurent_day)
            return false;
        return true;
    }
    public void go_today(View view){
        day.setYear(cuurent_year+"");
        day.setMonth(cuurent_month+"");
        day.setDay(cuurent_day+"");
        tv_month.setText(day.getYear() + " "+ dm.get_month(day.getintMonth()).toString());
        removeTableViews();
        showDialog(addbtnDialog);
        tv_time_message.setVisibility(View.GONE);
        //new GetbtnsTask().execute(0);
        day_textItems.clear();
        hpText.removeAllViews();
        for(int d=0;d<=dm.day_counts(day.getintMonth(), day.getintYear());d++) {
            day_textItems.add(new HorizontalPicker.TextItem(dm.get_day(d)));
        }
        hpText.setItems(day_textItems,day.getintDay()-1);

        if (day.getintDay()>24){
            hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        }
        else if(day.getintDay()<=7)
            hsv.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        else if(day.getintDay()>7 && day.getintDay()<=15)
            hsv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hsv.smoothScrollTo(hsv.getChildAt(0).getWidth()/4, 0);
                }
            }, 100L);
        else if(day.getintDay()>15 && day.getintDay()<=22)
            hsv.postDelayed(new Runnable() {
            @Override
            public void run() {
                hsv.smoothScrollTo(hsv.getChildAt(0).getWidth()/3, 0);
            }
        }, 100L);
        else
            hsv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Double width = hsv.getChildAt(0).getWidth()*0.6;
                    hsv.smoothScrollTo(width.intValue() , 0);
                }
            }, 100L);
    }
    public void go_firsttime(View view){
        int temp_year = cuurent_year;
        int temp_month = cuurent_month;
        int temp_day = cuurent_day;
        showDialog(firstdayDialog);
        tv_time_message.setVisibility(View.GONE);
        removeTableViews();
        get_firstday(temp_year,temp_month,temp_day,0);
    }
    private void get_firstday(final int temp_year,final int temp_month,final int temp_day,final int counter){
        if(counter!=7){
        String tag_string_req = "req_freeHourstoReserve";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Get_FreeHours+jal_to_greg(temp_year, temp_month, temp_day), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("ReserveFirsthour", "Get FIRST Free Hours: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");
                    if (success.equalsIgnoreCase("true")) {

                        day = new Day(temp_year+"",temp_month+"",temp_day+"");
                        tv_time_message.setVisibility(View.GONE);
                        removeTableViews();
                        //new GetbtnsTask().execute(0);
                        tv_month.setText(day.getYear() + " "+dm.get_month(day.getintMonth()).toString());
                        day_textItems.clear();
                        hpText.removeAllViews();
                        for(int d=0;d<=dm.day_counts(day.getintMonth(), day.getintYear());d++) {
                            day_textItems.add(new HorizontalPicker.TextItem(dm.get_day(d)));
                        }
                        hpText.setItems(day_textItems,day.getintDay()-1);

                        if (day.getintDay()>24){
                            hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        }
                        else if(day.getintDay()<=7)
                            hsv.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                        else if(day.getintDay()>7 && day.getintDay()<=15)
                            hsv.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hsv.smoothScrollTo(hsv.getChildAt(0).getWidth()/4, 0);
                                }
                            }, 100L);
                        else if(day.getintDay()>15 && day.getintDay()<=22)
                            hsv.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hsv.smoothScrollTo(hsv.getChildAt(0).getWidth()/3, 0);
                                }
                            }, 100L);
                        else
                            hsv.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Double width = hsv.getChildAt(0).getWidth()*0.6;
                                    hsv.smoothScrollTo(width.intValue() , 0);
                                }
                            }, 100L);

                    } else {
                        int newtemp_day=temp_day;
                        int newtemp_month=temp_month;
                        int newtemp_year=temp_year;
                        newtemp_day+=1;
                            if(newtemp_month<=6){
                                if (newtemp_day==31){
                                    newtemp_day=1;
                                    newtemp_month+=1;
                                }
                            }
                            if(newtemp_month<=7 && newtemp_month<12){
                                if (newtemp_day==32){
                                    newtemp_day=1;
                                    newtemp_month+=1;
                                }
                            }
                            if(newtemp_month==12){
                                if(newtemp_day==30){
                                    newtemp_day=1;
                                    newtemp_month=1;
                                    newtemp_year+=1;
                                }
                            }
                        get_firstday(newtemp_year, newtemp_month, newtemp_day, counter+1);
                    }
                } catch (JSONException e) {
                    //Log.e("Reserve first time", "error 1 " + e.getMessage());
                    int newtemp_day=temp_day;
                    int newtemp_month=temp_month;
                    int newtemp_year=temp_year;
                    newtemp_day+=1;
                    if(newtemp_month<=6){
                        if (newtemp_day==31){
                            newtemp_day=1;
                            newtemp_month+=1;
                        }
                    }
                    if(newtemp_month<=7 && newtemp_month<12){
                        if (newtemp_day==32){
                            newtemp_day=1;
                            newtemp_month+=1;
                        }
                    }
                    if(newtemp_month==12){
                        if(newtemp_day==30){
                            newtemp_day=1;
                            newtemp_month=1;
                            newtemp_year+=1;
                        }
                    }
                    get_firstday(newtemp_year, newtemp_month, newtemp_day, counter+1);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int newtemp_day=temp_day;
                int newtemp_month=temp_month;
                int newtemp_year=temp_year;
                newtemp_day+=1;
                if(newtemp_month<=6){
                    if (newtemp_day==31){
                        newtemp_day=1;
                        newtemp_month+=1;
                    }
                }
                if(newtemp_month<=7 && newtemp_month<12){
                    if (newtemp_day==32){
                        newtemp_day=1;
                        newtemp_month+=1;
                    }
                }
                if(newtemp_month==12){
                    if(newtemp_day==30){
                        newtemp_day=1;
                        newtemp_month=1;
                        newtemp_year+=1;
                    }
                }
                get_firstday(newtemp_year, newtemp_month, newtemp_day, counter+1);
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    else{
            //FIRST DAY NOT FOUND
            removeTableViews();
            showMessage("اولین وقت خالی تنظیم نشده است");
            hideDialog(firstdayDialog);
        }
    }
    private void removeTableViews(){
            tableButtons.removeAllViews();
    }
    ////////////////////////////////////////////////////////////////////////////MAIN ONE IS HERE/////////////////////////////////
    private void reserve_time(final String time) {
        dialog = new Dialog(Reserve.this);
        dialog.setContentView(R.layout.dialog_reserve);

        final TextView txtExamTitle = dialog.findViewById(R.id.txtExamTitle);
        final TextView txtdate = dialog.findViewById(R.id.txtdate);
        final TextView txthour = dialog.findViewById(R.id.txthour);
        final LinearLayout LinearLayoutday = dialog.findViewById(R.id.LinearLayoutday);
        final LinearLayout LinearLayouthour = dialog.findViewById(R.id.LinearLayouthour);
        final Button btn_doreserve = (Button) dialog.findViewById(R.id.btn_doreserve);
        final Button btn_close = (Button) dialog.findViewById(R.id.btn_close);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        btn_doreserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(reservedayDialog);
                do_reserve(time, day.getId(), getkey(spinnerRequest.getSelectedItem().toString()));
            }
        });
        dialog.show();
        dialog.getWindow().setLayout((6 * width)/7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    private void do_reserve(final String time, final String dayId, final String request){

        String tag_string_req = "req_do_reserve";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEND_RESERVE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("do_reserve", "do_reserve Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");
                    if (success.equalsIgnoreCase("true")) {
                        JSONObject c = jObj.getJSONObject("data");
                        if(c.getString("message").equals("This time is taken by someone else")){
                            hideDialog(reservedayDialog);
                            Toast.makeText(context, "متاسفانه این وقت پر می باشد", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else{
                            hideDialog(reservedayDialog);
                            Toast.makeText(context, "ساعت "+ time + " برای شما رزرو شد", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    } else {
                        // Error in login. Get the error message
                        Toast.makeText(getApplicationContext(), "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                        hideDialog(reservedayDialog);
                    }
                } catch (JSONException e) {
                    // JSON error
                    Log.d("do_reserve", "error 1 " + e.getMessage());
                    e.printStackTrace();
                    if(Language.equals("fa")){
                        Toast.makeText(getApplicationContext(), "مشکلی در داده ها وجود دارد", Toast.LENGTH_SHORT).show();
                    }
                    hideDialog(reservedayDialog);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("do_reserve",  "error 2" + error.getMessage());
                 Toast.makeText(getApplicationContext(), "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                hideDialog(reservedayDialog);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("auth", session.getUserDetails().getToken());
                params.put("SaveSelectUser[select_date_id]", dayId);
                params.put("SaveSelectUser[enterTime]", time);
                params.put("SaveSelectUser[mobile]", session.getUserDetails().getMobile());
                params.put("SaveSelectUser[fullname]", session.getUserDetails().getFirstname() + " " + session.getUserDetails().getLastname());
                params.put("SaveSelectUser[request]", request);

                return params;
            }
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
    private void showMessage(String message){
        tv_time_message.setVisibility(View.VISIBLE);
        tv_time_message.setText(message);
    }
    private String topersian(String input){
        return input.replace("1","١").replace("2","٢").replace("3","٣")
                .replace("4","۴").replace("5","۵").replace("6","۶")
                .replace("7","۷").replace("8","۸").replace("9","۹")
                .replace("0","۰");
    }
    private void  scrolltoday(){
        day_textItems.clear();
        hpText.removeAllViews();
        for(int d=0;d<=dm.day_counts(day.getintMonth(), day.getintYear());d++) {
            day_textItems.add(new HorizontalPicker.TextItem(dm.get_day(d)));
        }
        hpText.setItems(day_textItems,day.getintDay()-1);

        if (day.getintDay()>24){
            hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        }
        else if(day.getintDay()<=7)
            hsv.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        else if(day.getintDay()>7 && day.getintDay()<=15)
            hsv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hsv.smoothScrollTo(hsv.getChildAt(0).getWidth()/4, 0);
                }
            }, 100L);
        else if(day.getintDay()>15 && day.getintDay()<=22)
            hsv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hsv.smoothScrollTo(hsv.getChildAt(0).getWidth()/3, 0);
                }
            }, 100L);
        else
            hsv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Double width = hsv.getChildAt(0).getWidth()*0.6;
                    hsv.smoothScrollTo(width.intValue() , 0);
                }
            }, 100L);
    }
    private void makedialog(String text, ProgressDialog dialog){
        dialog = new ProgressDialog(this);
        dialog.setTitle(text);
    }
    private void updatedialog(String text,  ProgressDialog dialog)
    {
        dialog.setTitle(text);
    }
    private String jal_to_greg(int year, int month, int day){
        //E/date: Tue Aug 07 00:00:00 GMT+04:30 2018
        try {
            //get_month_number2(Aug);
            String datetime =JalCal.jalaliToGregorian(year, month, day, 0, 0, 0).toString();
            return  datetime.split(" ")[5]+"-"+dm.get_month_number2(datetime.split(" ")[1])+"-"+datetime.split(" ")[2];
            // Log.e("date", date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private String getkey(String value){
        for (int o:
             reqs.keySet()) {
            if(reqs.get(o).equals(value)){
                return o+"";
            }
        }
        return 1+"";
    }
    private void showDialog( ProgressDialog dialog) {

        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("در حال بارگزاری...");
            dialog.setIndeterminate(true);
            dialog.show();
        }
        else{
            if (!dialog.isShowing())
                dialog.show();
        }
    }
    private void hideDialog( ProgressDialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }
}
