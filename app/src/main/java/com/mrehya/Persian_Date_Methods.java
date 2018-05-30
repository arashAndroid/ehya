package com.mrehya;

import android.content.Context;
import android.widget.Button;

import com.goodiebag.horizontalpicker.HorizontalPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashke on 2/14/2018.
 */

public class Persian_Date_Methods {

    public List<Button> day_btn(int day_counts, Context context){
        ArrayList<Button> daysbtns = new ArrayList<Button> ();
        ArrayList<String> days = new ArrayList<String> ();
        days.add("۱" ); days.add("۲" ); days.add("۳"); days.add("۴");
        days.add("۵" ); days.add("۶" ); days.add("۷"); days.add("۸");
        days.add("۹" ); days.add("۱۰"); days.add("۱۱");days.add("۱۲");
        days.add("۱۳"); days.add("۱۴"); days.add("۱۵");days.add("۱۶");
        days.add("۱۷"); days.add("۱۸"); days.add("۱۹");days.add("۲۰");
        days.add("۲۱"); days.add("۲۲"); days.add("۲۳");days.add("۲۴");
        days.add("۲۵"); days.add("۲۶"); days.add("۲۷");days.add("۲۸");
        days.add("۲۹"); days.add("۳۰"); days.add("۳۱");
        for(int day=0;day<=day_counts;day++) {
            Button btn = new Button(context);
            btn.setText(days.get(day));
            daysbtns.add(btn);
        }
        return daysbtns;
    }


    public List<HorizontalPicker.PickerItem> day(int day_counts){
        ArrayList<String> days = new ArrayList<String> ();
        days.add("۱" ); days.add("۲" ); days.add("۳"); days.add("۴");
        days.add("۵" ); days.add("۶" ); days.add("۷"); days.add("۸");
        days.add("۹" ); days.add("۱۰"); days.add("۱۱");days.add("۱۲");
        days.add("۱۳"); days.add("۱۴"); days.add("۱۵");days.add("۱۶");
        days.add("۱۷"); days.add("۱۸"); days.add("۱۹");days.add("۲۰");
        days.add("۲۱"); days.add("۲۲"); days.add("۲۳");days.add("۲۴");
        days.add("۲۵"); days.add("۲۶"); days.add("۲۷");days.add("۲۸");
        days.add("۲۹"); days.add("۳۰"); days.add("۳۱");
        List<HorizontalPicker.PickerItem> day_textItems = new ArrayList<>();
        for(int day=0;day<=day_counts;day++) {
            day_textItems.add(new HorizontalPicker.TextItem(days.get(day)));
        }
        return day_textItems;
    }

    public String get_month(int index){
        index-=1;
        ArrayList<String> month = new ArrayList<String> ();
        month.add("فروردین" ); month.add("اردیبهشت" ); month.add("خرداد"); month.add("تیر");
        month.add("مرداد" ); month.add("شهریور" ); month.add("مهر"); month.add("آبان");
        month.add("آذر" ); month.add("دی"); month.add("بهمن");month.add("اسفند");
        return month.get(index);
    }

    public int get_month_number(String month_name){
        ArrayList<String> month = new ArrayList<String> ();
        month.add("فروردین" ); month.add("اردیبهشت" ); month.add("خرداد"); month.add("تیر");
        month.add("مرداد" ); month.add("شهریور" ); month.add("مهر"); month.add("آبان");
        month.add("آذر" ); month.add("دی"); month.add("بهمن");month.add("اسفند");
        return month.indexOf(month_name);
    }


    public String get_day(int index){
        ArrayList<String> days = new ArrayList<String> ();
        days.add("۱" ); days.add("۲" ); days.add("۳"); days.add("۴");
        days.add("۵" ); days.add("۶" ); days.add("۷"); days.add("۸");
        days.add("۹" ); days.add("۱۰"); days.add("۱۱");days.add("۱۲");
        days.add("۱۳"); days.add("۱۴"); days.add("۱۵");days.add("۱۶");
        days.add("۱۷"); days.add("۱۸"); days.add("۱۹");days.add("۲۰");
        days.add("۲۱"); days.add("۲۲"); days.add("۲۳");days.add("۲۴");
        days.add("۲۵"); days.add("۲۶"); days.add("۲۷");days.add("۲۸");
        days.add("۲۹"); days.add("۳۰"); days.add("۳۱");
        return days.get(index);
    }
    public int day_counts(int month_number, int year){
        int daycount = 29;
        if(month_number<=6)
            daycount=30;
        else if(month_number==12)
            daycount=28;
        if(daycount==29 && year%4==0)
            daycount=30;
        return  daycount;
    }

    public ArrayList<String> get_hours(){
        ArrayList<String> hours = new ArrayList<String> ();
        hours.add("۸:۳۰" ); hours.add("۹:۰۰" ); hours.add("۹:۳۰"); hours.add("۱۰:۰۰");
        hours.add("۱۰:۳۰" ); hours.add("۱۱:۰۰" ); hours.add("۱۱:۳۰"); hours.add("۱۲:۰۰");
        hours.add("۱۶:۰۰"); hours.add("۱۶:۳۰");hours.add("۱۷:۰۰");
        hours.add("۱۷:۳۰"); hours.add("۱۸:۰۰"); hours.add("۱۸:۳۰");hours.add("۱۹:۰۰");
        return hours;
    }


}
