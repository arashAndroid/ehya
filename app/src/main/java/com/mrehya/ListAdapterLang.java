package com.mrehya;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mrehya.Helper.LocaleHelper;

import java.util.ArrayList;

import io.paperdb.Paper;

/**
 * Created by Rubick on 2/15/2018.
 */

public class ListAdapterLang extends BaseAdapter implements ListAdapter {
    private ArrayList<Lang> list = new ArrayList<>();
    private Context context;
    private Activity activity;
    private ListView listView;


    //new
    LinearLayout LinearLayoutitemlang1;

    public ListAdapterLang(ArrayList<Lang> list, Context context, Activity activity, ListView listView) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.listView = listView;
    }

    @Override
    public int getCount() {
        if(list!=null)
            return list.size();
        else
            return 0;
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
            view = inflater.inflate(R.layout.list_item_lang, null);
        }
        Lang lang = list.get(position);
        //Handle TextView and display string from your list
        final TextView listItemText = (TextView)view.findViewById(R.id.txtLang);
        final TextView txtLangLevel = (TextView)view.findViewById(R.id.txtLangLevel);
        listItemText.setText(lang.getName());
        txtLangLevel.setText(lang.getLevel());
        //Handle buttons and add onClickListeners
        ImageButton btnDelete =view.findViewById(R.id.btnDeleteLang);
        ImageButton btnEdit = view.findViewById(R.id.btnEditLang);

        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something

                list.remove(position); //or some other task
                notifyDataSetChanged();
                justifyListViewHeightBasedOnChildren(listView);
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Lang lang = list.get(position);

                showDialog(lang.getName(),lang.getLevel(),position);
                notifyDataSetChanged();
            }
        });

        //new
        LinearLayoutitemlang1=view.findViewById(R.id.LinearLayoutitemlang1);
        //updateView(updateLanguage());
        return view;
    }
    private void showDialog(String oldItem,String oldItemLevel, final int index){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.list_dialog_lang);
        //final EditText txtEdit = dialog.findViewById(R.id.txtEditDialogLang);
        final Spinner spinner = dialog.findViewById(R.id.spinnerLangLevel);
        //txtEdit.setText(oldItem);
        final Spinner spinnerLang = dialog.findViewById(R.id.spinnerLang);
        ImageButton btnEdit = dialog.findViewById(R.id.btnEditDialogLang);
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(oldItemLevel)){
                spinner.setSelection(i,true);
                break;
            }
        }
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lang lang = new Lang(txtEdit.getText().toString(),spinner.getSelectedItem().toString());
                Lang lang = new Lang(spinnerLang.getSelectedItem().toString(),spinner.getSelectedItem().toString());
                list.set(index,lang);
                notifyDataSetChanged();
                dialog.dismiss();
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

    private String updateLanguage(){
        //Default language is fa
        String language = Paper.book().read("language");
        if(language==null)
            Paper.book().write("language", "fa");
        return language;
    }

    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this.context  , language);

        if(language.equals("fa")){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                LinearLayoutitemlang1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LinearLayoutitemlang1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
        else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                LinearLayoutitemlang1.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LinearLayoutitemlang1.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }
    }
}
