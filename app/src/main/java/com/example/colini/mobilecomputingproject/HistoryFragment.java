package com.example.colini.mobilecomputingproject;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;
import android.database.sqlite.*;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Yang on 16-03-08.
 */
public class HistoryFragment extends Fragment {

    SQLiteDatabase mydatabase;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_history, container, false);

        mydatabase = getActivity().openOrCreateDatabase("scanAndShop", Context.MODE_PRIVATE, null);

        LinearLayout LL = (LinearLayout) rootView.findViewById(R.id.mainContainer);

        Cursor transactions = mydatabase.rawQuery("select * from payment order by id desc",null);
        if (transactions.getCount()==0)
        {
            LL.addView(creatRow("\tYou haven't purchased any item yet!",""));
            return rootView;
        }
        transactions.moveToFirst();
        do {
            Cursor dbElements = mydatabase.rawQuery("select * from history where transaction_id='"+transactions.getString(0)+"'", null);
            dbElements.moveToFirst();
            if (dbElements.getCount()==0) continue;

            LinearLayout hdr=createHeader(""+transactions.getString(0), transactions.getString(1), transactions.getString(2), "");
            LL.addView(hdr);
            do {
                LinearLayout row = creatRow(dbElements.getString(2), "");
                LL.addView(row);
            }while(dbElements.moveToNext());

        }while(transactions.moveToNext());


        return rootView;
    }
    public LinearLayout creatRow(String productName, String price)
    {
        LinearLayout Row= new LinearLayout(getActivity());
        Row.setOrientation(LinearLayout.VERTICAL);

        TextView product= new TextView(getActivity()), Price = new TextView(getActivity());

        product.setText(productName);
        Price.setText(price);

        product.setPadding(20, 20, 20, 20);
        product.setTextSize(16);
        Price.setPadding(30, 10, 0, 10);

        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(1, Color.GRAY); //black border with full opacity
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Row.setBackgroundDrawable(border);
        } else {
            Row.setBackground(border);
        }


        Row.addView(product);
        //Row.addView(Price);

        return Row;
    }


    public LinearLayout createHeader(String id, String storeName, String dateAndTime, String total)
    {
        LinearLayout Header=new LinearLayout(getActivity());
        Header.setOrientation(LinearLayout.HORIZONTAL);
        Header.setBackgroundColor(Color.GRAY);


        LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        headerParams.setMargins(0,20,0,0);
        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.GRAY); //white background
        border.setStroke(1, Color.GRAY); //black border with full opacity
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Header.setBackgroundDrawable(border);
        } else {
            Header.setBackground(border);
        }


        LinearLayout innerHeaderLeft= new LinearLayout(getActivity()),
                innerHeaderRight= new LinearLayout(getActivity());

        innerHeaderLeft.setOrientation(LinearLayout.VERTICAL);
        innerHeaderRight.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.width=400;
        params.height=LinearLayout.LayoutParams.WRAP_CONTENT;

        TextView ID, store, dAt,Total;
        ID=new TextView(getActivity());
        store=new TextView(getActivity());
        dAt=new TextView(getActivity());
        Total=new TextView(getActivity());

        ID.setText(id);
        store.setText(storeName);
        dAt.setText(dateAndTime);
        Total.setText(total);

        ID.setTextColor(Color.WHITE);
        store.setTextColor(Color.WHITE);
        dAt.setTextColor(Color.WHITE);
        Total.setTextColor(Color.WHITE);

        ID.setTextSize(16);
        store.setTextSize(16);
        dAt.setTextSize(16);
        Total.setTextSize(16);

        innerHeaderLeft.addView(ID);
        innerHeaderLeft.addView(store);
        innerHeaderRight.addView(dAt);
        innerHeaderRight.addView(Total);

        innerHeaderLeft.setPadding(10, 10, 10, 10);
        innerHeaderRight.setPadding(10,10,10,10);

        Header.addView(innerHeaderLeft,params);
        Header.addView(innerHeaderRight,params);
        Header.setLayoutParams(headerParams);

        return Header;


    }





}
