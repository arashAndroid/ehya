package com.mrehya;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mrehya.Helper.LocaleHelper;

import java.util.ArrayList;

import io.paperdb.Paper;

/**
 * Created by sdfsdfasf on 2/27/2018.
 */

public class ListAdapterExam extends RecyclerView.Adapter<ListAdapterExam.MyViewHolder> {

    //dialog
    ImageView imgShowImg;
    TextView txtExamTitle,txtExamContentShowexam,txtExamTypeTitle,txtExamType
            ,txtExamPriceTitle,txtExamPrice,txtJobPlaceShowhire,txtJobPlaceShowhire2;
    LinearLayout LinearLayoutdialog_show_exam,LinearLayoutdialog_show_exam2
            ,LinearLayoutdialog_show_exam3;
    Button btn_Openexam,btn_close;

    private ArrayList<Exam> list = new ArrayList<Exam>();
    private Context context;
    private Activity activity;
    public ImageButton imgbtnLogo;
    public RelativeLayout LinearLayoutchooseexam1;
    public TextView txtTitle,txtPrice;
    private String Language;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public ImageButton imgbtnLogo;
        public RelativeLayout LinearLayoutchooseexam1;
        public TextView txtTitle,txtPrice;
        public MyViewHolder(View view) {
            super(view);
            setIsRecyclable(false);
            imgbtnLogo =view.findViewById(R.id.imgbtnLogo);
            LinearLayoutchooseexam1= view.findViewById(R.id.LinearLayoutchooseexam1);
            txtTitle=view.findViewById(R.id.txtTitle);
            txtPrice=view.findViewById(R.id.txtPrice);
        }
    }
    //new


    public ListAdapterExam(ArrayList<Exam> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public long getItemId(int pos) {
        return pos;
        //just return 0 if your list items do not have an Id variable.
    }
    @Override
    public ListAdapterExam.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_exam, parent, false);


        //new
        Paper.init(context);
        imgbtnLogo =itemView.findViewById(R.id.imgbtnLogo);
        LinearLayoutchooseexam1=itemView.findViewById(R.id.LinearLayoutchooseexam1);
        txtTitle=itemView.findViewById(R.id.txtTitle);
        txtPrice=itemView.findViewById(R.id.txtPrice);
        Language = updatelanguage(context);
        updateView(Language);

        return new ListAdapterExam.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final ListAdapterExam.MyViewHolder holder, final int position) {
        final Exam exam = list.get(position);

        //Handle TextView and display string from your list
        holder.txtTitle.setText(exam.getName());
        holder.txtPrice.setText(exam.getPrice());
        holder.imgbtnLogo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                show_dialog(exam);
            }
        });
        holder.txtTitle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                show_dialog(exam);
            }
        });
        Glide.with(context)
                .load(list.get(position).getImage())
                .placeholder(R.drawable.logo_persian)
                .error(R.drawable.logo_persian_placeholder)
                .into(holder.imgbtnLogo);
    }


    public void show_dialog(Exam exam){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_show_exam);

        imgShowImg                      = dialog.findViewById(R.id.imgShowImg);

        txtExamTitle                    = dialog.findViewById(R.id.txtExamTitle);
        txtExamContentShowexam          = dialog.findViewById(R.id.txtExamContentShowexam);
        txtExamType                     = dialog.findViewById(R.id.txtExamType);
        txtExamTypeTitle                = dialog.findViewById(R.id.txtExamTypeTitle);
        txtExamPrice                    = dialog.findViewById(R.id.txtExamPrice);
        txtExamPriceTitle               = dialog.findViewById(R.id.txtExamPriceTitle);

        LinearLayoutdialog_show_exam    = dialog.findViewById(R.id.LinearLayoutdialog_show_exam);
        LinearLayoutdialog_show_exam2   = dialog.findViewById(R.id.LinearLayoutdialog_show_exam2);
        LinearLayoutdialog_show_exam3   = dialog.findViewById(R.id.LinearLayoutdialog_show_exam3);

        btn_Openexam                    = dialog.findViewById(R.id.btn_Openexam);
        btn_close                       = dialog.findViewById(R.id.btn_close);


        updateView_dialog();


        //set inputs
        Glide.with(context)
                .load(exam.getImage())
                .placeholder(R.drawable.logo_persian)
                .error(R.drawable.logo_persian_placeholder)
                .into(imgShowImg);

        txtExamTitle.setText(exam.getName());
        txtExamType.setText(exam.getType());
        txtExamPrice.setText(exam.getPrice());
        txtExamContentShowexam.setText(exam.getDescription());


        final int id = exam.getId();
        btn_Openexam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something
                Intent intent = new Intent(activity,Test.class);
                intent.putExtra("examId",id);
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        updateView_dialog();
        dialog.show();
        dialog.getWindow().setLayout((12 * width)/13, ViewGroup.LayoutParams.WRAP_CONTENT);
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

        //txtMinute.setTextSize(@dimen/textSize1);
    }



    private void updateView_dialog() {
        Context context2 = LocaleHelper.setLocale(context, Language);
        Resources resources = context2.getResources();

        if(Language.equals("fa")){
            txtExamTypeTitle.setText(resources.getString(R.string.examtype));
            txtExamPriceTitle.setText(resources.getString(R.string.examtype));
        }
        else{
            txtExamTypeTitle.setText(resources.getString(R.string.examtype));
            txtExamPriceTitle.setText(resources.getString(R.string.examtype));
        }
        btn_close.setText(resources.getString(R.string.Close));
        btn_Openexam.setText(resources.getString(R.string.examstart));
    }
}