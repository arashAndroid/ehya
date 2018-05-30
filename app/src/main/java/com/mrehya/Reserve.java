package com.mrehya;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.horizontalpicker.HorizontalPicker;
import com.mrehya.Helper.LocaleHelper;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import me.anwarshahriar.calligrapher.Calligrapher;

public class Reserve extends AppCompatActivity {

    private int month;
    private int year;
    private int day;

    private int cuurent_month;
    private int cuurent_year;
    private int cuurent_day;

    List<HorizontalPicker.PickerItem> day_textItems;
    HorizontalPicker hpText;
    HorizontalScrollView hsv;

    Button btn1;Button btn2;Button btn3;
    Button btn4;Button btn5;Button btn6;
    Button btn7;Button btn8;Button btn9;
    Button btn10;Button btn11;Button btn12;
    Button btn13;Button btn14;Button btn15;

    TextView tv_time_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);


        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "BHoma.ttf", true);

        calligrapher.setFont(getCurrentFocus(), "BHoma.ttf");
        PersianCalendar pc = new PersianCalendar();
        cuurent_year    = pc.year;
        cuurent_month   = pc.month;
        cuurent_day     = pc.day;

        month = cuurent_month;
        year  =  cuurent_year;
        day   =   cuurent_day;

        //HOURS
        btn1 = (Button) findViewById(R.id.btn_H_1);  btn2 = (Button) findViewById(R.id.btn_H_2);
        btn3 = (Button) findViewById(R.id.btn_H_3);  btn4 = (Button) findViewById(R.id.btn_H_4);
        btn5 = (Button) findViewById(R.id.btn_H_5);  btn6 = (Button) findViewById(R.id.btn_H_6);
        btn7 = (Button) findViewById(R.id.btn_H_7);  btn8 = (Button) findViewById(R.id.btn_H_8);
        btn9 = (Button) findViewById(R.id.btn_H_9);  btn10 = (Button) findViewById(R.id.btn_H_10);
        btn11 = (Button) findViewById(R.id.btn_H_11);btn12 = (Button) findViewById(R.id.btn_H_12);
        btn13 = (Button) findViewById(R.id.btn_H_13);btn14 = (Button) findViewById(R.id.btn_H_14);
        btn15 = (Button) findViewById(R.id.btn_H_15);

        tv_time_message = (TextView) findViewById(R.id.tv_time_message);

        //default month view
        final Persian_Date_Methods dm = new Persian_Date_Methods();
        TextView tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(year + " "+dm.get_month(cuurent_month).toString());

        //DAY
        int daycount = 29;
        if(cuurent_month<=6)
            daycount=30;
        else if(cuurent_month==12)
            daycount=28;

        set_hours();
//        //If your picker needs to have text as items :
        hpText = (HorizontalPicker) findViewById(R.id.hpicker);
        HorizontalPicker.OnSelectionChangeListener listener = new HorizontalPicker.OnSelectionChangeListener() {
            @Override
            public void onItemSelect(HorizontalPicker picker, int index) {
                day = index+1;
                Log.e("date: ", "day/month/year: " + day+"/"+month+"/"+year);
                if(!(freetimes()))
                    tv_time_message.setVisibility(View.VISIBLE);
                else
                    tv_time_message.setVisibility(View.GONE);
            }
        };
        day_textItems = dm.day(daycount);
        hpText.setItems(day_textItems,day-1);
        hpText.setChangeListener(listener);




        hsv = (HorizontalScrollView) findViewById(R.id.hsv);
        if(!(freetimes()))
            tv_time_message.setVisibility(View.VISIBLE);
        else
            tv_time_message.setVisibility(View.GONE);

        //new
        //init paper
        updatelanguage(this);
        updateView((String) Paper.book().read("language"));
    }
    //FINISH ON CREATE

    //MONTH CAROUSEL LISTENERS
    public void prev_month(View view){
        final Persian_Date_Methods dm = new Persian_Date_Methods();
        month= month-1;

        if(month < 1)
        {
            month=12;
            year = year-1;
        }
        TextView tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(year + " "+ dm.get_month(month).toString());

        day_textItems.clear();
        hpText.removeAllViews();
        for(int day=0;day<=dm.day_counts(month, year);day++) {
            day_textItems.add(new HorizontalPicker.TextItem(dm.get_day(day)));
        }
        HorizontalPicker hpText = (HorizontalPicker) findViewById(R.id.hpicker);
        if(!(is_today())){
            day=1;
        }
        hpText.setItems(day_textItems,day-1);
        hsv.postDelayed(new Runnable() {
            @Override
            public void run() {
                hsv.smoothScrollTo(day-1, 0);
                //hsv.fullScroll(HorizontalScrollView.FOCUS);
            }
        }, 100L);
    }

    public void next_month(View view){
        final Persian_Date_Methods dm = new Persian_Date_Methods();
        month= month + 1;

        if(month >12) {
            month = 1;
            year = year+1;
        }
        TextView tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(year + " "+ dm.get_month(month).toString());

        day_textItems.clear();
        hpText.removeAllViews();
        for(int day=0;day<=dm.day_counts(month, year);day++) {
            day_textItems.add(new HorizontalPicker.TextItem(dm.get_day(day)));
        }
        HorizontalPicker hpText = (HorizontalPicker) findViewById(R.id.hpicker);

        if(!(is_today())){
            day=1;
        }
        hpText.setItems(day_textItems,day-1);
        hsv.postDelayed(new Runnable() {
            @Override
            public void run() {
                hsv.smoothScrollTo(day-1, 0);
                //hsv.fullScroll(HorizontalScrollView.Focus);
            }
        }, 100L);
    }

    public boolean is_today() {
        if(year != cuurent_year)
            return false;
        if(month != cuurent_month)
            return false;
        if(day != cuurent_day)
            return false;
        return true;
    }

    public void go_today(View view){
        year = cuurent_year;
        month = cuurent_month;
        day = cuurent_day;

        final Persian_Date_Methods dm = new Persian_Date_Methods();

        TextView tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(year + " "+ dm.get_month(month).toString());

        day_textItems.clear();
        hpText.removeAllViews();
        for(int day=0;day<=dm.day_counts(month, year);day++) {
            day_textItems.add(new HorizontalPicker.TextItem(dm.get_day(day)));
        }
        HorizontalPicker hpText = (HorizontalPicker) findViewById(R.id.hpicker);
        hpText.setItems(day_textItems,day-1);

        if (day>24){
            hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        }
        else if(day<=7)
            hsv.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        else if(day>7 && day<=15)
            hsv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hsv.smoothScrollTo(hsv.getChildAt(0).getWidth()/4, 0);
                }
            }, 100L);
        else if(day>15 && day<=22)
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


        //Toast.makeText(this, hsv.getChildAt(0).getWidth() + " is " + hsv.getChildAt(0).getWidth()/2, Toast.LENGTH_SHORT).show();
    }

    public void go_firsttime(View view){
        boolean is_firsttime = false;
        int temp_hour = 0;
        int temp_year = cuurent_year;
        int temp_month = cuurent_month;
        int temp_day = cuurent_day;
        while (is_firsttime==false){
            for(int hour=0;hour<15;hour++){
                if(is_first_freetime(temp_year,temp_month,temp_day,hour)){
                    temp_hour = hour;
                    is_firsttime=true;
                    break;
                }
            }
            if(is_firsttime==false)
            {
                temp_day+=1;
                if(temp_month<=6){
                    if (temp_day==31){
                        temp_day=1;
                        temp_month+=1;
                    }
                }
                if(temp_month<=7 && temp_month<12){
                    if (temp_day==32){
                        temp_day=1;
                        temp_month+=1;
                    }
                }
                if(temp_month==12){
                    if(temp_day==30){
                        temp_day=1;
                        temp_month=1;
                        temp_year+=1;
                    }
                }
            }
        }
        //go to first time
        year = temp_year;
        month = temp_month;
        day = temp_day;
        final Persian_Date_Methods dm = new Persian_Date_Methods();
        TextView tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(year + " "+dm.get_month(month).toString());


        day_textItems.clear();
        hpText.removeAllViews();
        for(int day=0;day<=dm.day_counts(month, year);day++) {
            day_textItems.add(new HorizontalPicker.TextItem(dm.get_day(day)));
        }
        HorizontalPicker hpText = (HorizontalPicker) findViewById(R.id.hpicker);
        hpText.setItems(day_textItems,day-1);


        if (day>24){
            hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        }
        else if(day<=7)
            hsv.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        else if(day>7 && day<=15)
            hsv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hsv.smoothScrollTo(hsv.getChildAt(0).getWidth()/4, 0);
                }
            }, 100L);
        else if(day>15 && day<=22)
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

    public boolean freetimes(){
        boolean is_allfalse = false;
        if(is_freetime(0)){
            btn1.setVisibility(View.VISIBLE);
            btn1.setEnabled(true);
            is_allfalse=true;
        }
        else {
            btn1.setVisibility(View.GONE);
            btn1.setEnabled(false);
        }

        if(is_freetime(1)){
            btn2.setVisibility(View.VISIBLE);
            btn2.setEnabled(true);
            is_allfalse=true;
        }
        else {
            btn2.setVisibility(View.GONE);
            btn2.setEnabled(false);
        }

        if(is_freetime(2)){
            btn3.setVisibility(View.VISIBLE);
            btn3.setEnabled(true);
            is_allfalse=true;
        }
        else {
            btn3.setVisibility(View.GONE);
            btn3.setEnabled(false);
        }

        if(is_freetime(3)){
            btn4.setVisibility(View.VISIBLE);
            btn4.setEnabled(true);
            is_allfalse=true;
        }
        else {
            btn4.setVisibility(View.GONE);
            btn4.setEnabled(false);
        }

        if(is_freetime(4)){
            btn5.setVisibility(View.VISIBLE);
            btn5.setEnabled(true);
            is_allfalse=true;
        }
        else {
            btn5.setVisibility(View.GONE);
            btn5.setEnabled(false);
        }

        if(is_freetime(5)){
            btn6.setVisibility(View.VISIBLE);
            btn6.setEnabled(true);
            is_allfalse=true;
        }
        else {
            btn6.setVisibility(View.GONE);
            btn6.setEnabled(false);
        }

        if(is_freetime(6)){
            btn7.setVisibility(View.VISIBLE);
            btn7.setEnabled(true);
            is_allfalse=true;
        }
        else {
            btn7.setVisibility(View.GONE);
            btn7.setEnabled(false);
        }

        if(is_freetime(7)){
            btn8.setVisibility(View.VISIBLE);
            btn8.setEnabled(true);
            is_allfalse=true;
        }
        else {
            btn8.setVisibility(View.GONE);
            btn8.setEnabled(false);
        }

        if(is_freetime(8)){
            btn9.setVisibility(View.VISIBLE);
            btn9.setEnabled(true);
            is_allfalse=true;
        }
        else {
            btn9.setVisibility(View.GONE);
            btn9.setEnabled(false);
        }


        if(is_freetime(9)){
            btn10.setVisibility(View.VISIBLE);
            btn10.setEnabled(true);
            is_allfalse=true;
        }
        else {
            btn10.setVisibility(View.GONE);
            btn10.setEnabled(false);
        }

        if(is_freetime(10)){
            btn11.setVisibility(View.VISIBLE);
            btn11.setEnabled(true);
            is_allfalse=true;
        }
        else {
            btn11.setVisibility(View.GONE);
            btn11.setEnabled(false);
        }

        if(is_freetime(11)){
            btn12.setVisibility(View.VISIBLE);
            btn12.setEnabled(true);
            is_allfalse=true;
        }
        else {
            btn12.setVisibility(View.GONE);
            btn12.setEnabled(false);
        }

        if(is_freetime(12)){
            btn13.setVisibility(View.VISIBLE);
            btn13.setEnabled(true);
            is_allfalse=true;
        }
        else {
            btn13.setVisibility(View.GONE);
            btn13.setEnabled(false);
        }

        if(is_freetime(13)){
            btn14.setVisibility(View.VISIBLE);
            btn14.setEnabled(true);
            is_allfalse=true;
        }
        else {
            btn14.setVisibility(View.GONE);
            btn14.setEnabled(false);
        }

        if(is_freetime(14)){
            btn15.setVisibility(View.VISIBLE);
            btn15.setEnabled(true);
            is_allfalse=true;
        }
        else {
            btn15.setVisibility(View.GONE);
            btn15.setEnabled(false);
        }
        return is_allfalse;
    }

    public boolean is_freetime(int hour){
        if(compare_today()==-1)
            return false;

        //year month day hour
        return true;
    }

    public int compare_today(){
        if(year<cuurent_year)
            return -1;
        if(month<cuurent_month && year == cuurent_year)
            return -1;
        if(day<cuurent_day && month==cuurent_month && year == cuurent_year)
            return -1;
        return 1;
    }

    public boolean is_first_freetime(int temp_year,int temp_month, int temp_day,int hour){
        if(temp_year<cuurent_year)
            return false;
        if(temp_month<cuurent_month && year == cuurent_year)
            return false;
        if(temp_day<cuurent_day && temp_month==cuurent_month && temp_year == cuurent_year)
            return false;
        //year month day hour
        return true;
    }

    public void reserve(View view){
        int hour = Integer.parseInt(view.getTag().toString());
        Toast.makeText(Reserve.this, "DATE " + year+"/"+month+"/"+day+"/"+hour+" reserved" , Toast.LENGTH_SHORT).show();
    }

    public void set_hours(){
        final Persian_Date_Methods dm = new Persian_Date_Methods();
        ArrayList<String> hours = dm.get_hours();
        int index=0;
        btn1.setText(hours.get(index));
        index++;
        btn2.setText(hours.get(index));
        index++;
        btn3.setText(hours.get(index));
        index++;
        btn4.setText(hours.get(index));
        index++;
        btn5 .setText(hours.get(index));
        index++;
        btn6.setText(hours.get(index));
        index++;
        btn7.setText(hours.get(index));
        index++;
        btn8.setText(hours.get(index));
        index++;
        btn9.setText(hours.get(index));
        index++;
        btn10.setText(hours.get(index));
        index++;
        btn11.setText(hours.get(index));
        index++;
        btn12.setText(hours.get(index));
        index++;
        btn13.setText(hours.get(index));
        index++;
        btn14.setText(hours.get(index));
        index++;
        btn15.setText(hours.get(index));
        index++;
    }

    private void updatelanguage(Context context){
        Paper.init(context);
        //Default language is fa
        String language = Paper.book().read("language");
        if(language==null)
            Paper.book().write("language", "fa");
    }

    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this, "en");
        Resources resources = context.getResources();
    }

}
