package com.mrehya;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        prefs = getApplicationContext().getSharedPreferences("ehya", 0);
        editor = prefs.edit();

        ArrayList<Integer> cartList = loadArray("cart_list",getApplicationContext());
        ArrayList<Integer> cartListCount = loadArray("cart_list_count",getApplicationContext());


    }
    public boolean saveArray(ArrayList<Integer> array, String arrayName, Context mContext) {
        editor.putInt(arrayName +"_size", array.size());
        for(int i=0;i<array.size();i++)
            editor.putInt(arrayName + "_" + i, array.get(i));
        editor.commit();
        return true;
    }
    public ArrayList<Integer> loadArray(String arrayName, Context mContext) {
        int size = prefs.getInt(arrayName + "_size", 0);
        ArrayList<Integer> array = new ArrayList<>();
        for(int i=0;i<size;i++)
            array.add(prefs.getInt(arrayName + "_" + i, 0));
        return array;
    }





}
