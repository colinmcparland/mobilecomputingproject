package com.example.colini.mobilecomputingproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by Yang on 16-03-08.
 */
public class ListFragment extends Fragment {

    SQLiteDatabase mydatabase;
    Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_main,container,false);

        mydatabase = getActivity().openOrCreateDatabase("scanAndShop", Context.MODE_PRIVATE,null);
        LinearLayout LL = (LinearLayout) rootView.findViewById(R.id.list);
        //mydatabase.execSQL("DROP TABLE list;");
        initDB();
        button = (Button) rootView.findViewById(R.id.search_list_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.mainContainer, new searchFragment()).addToBackStack("SearchFragment").commit();
            }
        });



        Cursor cursor = mydatabase.rawQuery("select * from list",null);
        if (cursor.getCount()==0)
        {
            TextView empty = new TextView(getActivity());
            empty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            empty.setText("The list is empty!");
            LL.addView(empty);
        }

        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            LL.addView(createRow(Integer.parseInt(cursor.getString(0)),cursor.getString(2), Integer.parseInt(cursor.getString(3))));
            while(cursor.moveToNext())
            {
                LL.addView(createRow(Integer.parseInt(cursor.getString(0)),cursor.getString(2),Integer.parseInt(cursor.getString(3))));
            }
        }



        Button clear=(Button) rootView.findViewById(R.id.clear);
        clear.setTextColor(Color.RED);
        clear.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        mydatabase.execSQL("delete from list");
                        Snackbar.make(v, "List has been successfully cleared!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        getFragmentManager().beginTransaction().replace(R.id.mainContainer, new ListFragment()).commit();
                    }
                }
        );

        return rootView;
    }

    public LinearLayout createRow(int id,final String productName, int i)
    {
        final LinearLayout Row= new LinearLayout(getActivity());
        Row.setOrientation(LinearLayout.HORIZONTAL);
        TextView product= new TextView(getActivity());
        LinearLayout.LayoutParams prams =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView img= new ImageView(getActivity());
        ImageView cancel=new ImageView(getActivity());


        cancel.setImageResource(R.drawable.cancel);
        img.setImageResource(i == 0 ? R.drawable.unchecked : R.drawable.checked);


        cancel.setPadding(0, 0, 10, 0);

        prams.setMargins(0, 2, 0, 2);

        product.setText(productName);
        product.setTextSize(16);
        product.setWidth(570);
        product.setPadding(20, 20, 20, 20);


        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(1, Color.GRAY); //black border with full opacity
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Row.setBackgroundDrawable(border);
        } else {
            Row.setBackground(border);
        }

        final int ii=id;

        cancel.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mydatabase.execSQL("delete from list where id = "+ii);
                        Snackbar.make(v, productName + " has been removed from the list!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Row.removeAllViews();
                        LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(0,0);
                        Row.setLayoutParams(par);

                    }
                }
        );

        product.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("ID", ii);
                        DetailFragment fragobj = new DetailFragment();
                        fragobj.setArguments(bundle);

                        getFragmentManager().beginTransaction().replace(R.id.mainContainer, fragobj).addToBackStack("listView").commit();
                    }
                }
        );

        Row.addView(product);
        Row.addView(img);
        Row.addView(cancel);
        Row.setLayoutParams(prams);
        Row.setGravity(Gravity.CENTER_VERTICAL);
        return Row;
    }

    public void initDB()
    {
        mydatabase.execSQL("create table if not exists list (" +
                "id INTEGER PRIMARY KEY   AUTOINCREMENT ," +
                "barcode text,"+
                "product_name char(255)," +
                "scanned int" +
                ")");
    }
}
