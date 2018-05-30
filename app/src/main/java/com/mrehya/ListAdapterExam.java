package com.mrehya;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.mrehya.Helper.LocaleHelper;

import java.util.ArrayList;

import io.paperdb.Paper;

/**
 * Created by sdfsdfasf on 2/27/2018.
 */

public class ListAdapterExam extends BaseAdapter implements ListAdapter {

    private ArrayList<Exam> list = new ArrayList<Exam>();
    private Context context;
    private Activity activity;

    //new
    LinearLayout LinearLayoutchooseexam1;
    TextView txtTitle,txtMinute,txtQuestion;

    public ListAdapterExam(ArrayList<Exam> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_exam, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.txtTitle);
        listItemText.setText(list.get(position).getName());


        TextView txtQCount = (TextView)view.findViewById(R.id.txtQCount);
        txtQCount.setText(String.valueOf( list.get(position).getqCount()));

        TextView txtTime = (TextView)view.findViewById(R.id.txtTime);
        txtTime.setText(String.valueOf( list.get(position).getTime()));

        //Handle buttons and add onClickListeners
        ImageButton imgbtnLogo =view.findViewById(R.id.imgbtnLogo);

        imgbtnLogo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Intent intent = new Intent(activity,Test.class);
                intent.putExtra("examId",list.get(position).getId());
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        txtTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Intent intent = new Intent(activity,Test.class);
                intent.putExtra("examId",list.get(position).getId());
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        txtQCount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Intent intent = new Intent(activity,Test.class);
                intent.putExtra("examId",list.get(position).getId());
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        listItemText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Intent intent = new Intent(activity,Test.class);
                intent.putExtra("examId",list.get(position).getId());
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });


        //new
        LinearLayoutchooseexam1=view.findViewById(R.id.LinearLayoutchooseexam1);
        txtMinute=view.findViewById(R.id.txtMinute);
        txtTitle=view.findViewById(R.id.txtTitle);
        txtQuestion=view.findViewById(R.id.txtQuestion);
        Paper.init(context);
        updateView(updatelanguage(context));




        return view;
    }
    private String updatelanguage(Context context){
        Paper.init(context);
        //Default language is fa
        String language = Paper.book().read("language");
        if(language==null)
            Paper.book().write("language", "fa");

        return language;
    }

    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this.context, language);
        Resources resources = context.getResources();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(language.equals("fa"))
                LinearLayoutchooseexam1.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            else
                LinearLayoutchooseexam1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        if(language.equals("fa")) {
            txtTitle.setGravity(Gravity.RIGHT | Gravity.CENTER);
            txtTitle.setPadding(0,15,10,15);
        }
        else {
            txtTitle.setGravity(Gravity.LEFT | Gravity.CENTER);
            txtTitle.setPadding(10,15,0,15);
        }
        txtTitle.setText(resources.getString(R.string.Exam));

        txtQuestion.setText(resources.getString(R.string.Question));
        txtMinute.setText(resources.getString(R.string.Minute));
        //txtMinute.setTextSize(@dimen/textSize1);
    }

}
