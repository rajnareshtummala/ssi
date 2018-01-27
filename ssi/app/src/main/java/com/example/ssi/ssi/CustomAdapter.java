package com.example.ssi.ssi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList<DataModel> data;
    private static LayoutInflater inflater=null;
    DataModel tempValues=null;
    int i=0;

    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter(Activity a, ArrayList<DataModel> d) {

        /********** Take passed values **********/
        activity = a;
        data=d;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.list_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text = vi.findViewById(R.id.text);
            holder.text = vi.findViewById(R.id.text);
            holder.tvRising = vi.findViewById(R.id.tvRising);
            holder.tvDuration = vi.findViewById(R.id.tvDuration);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.list_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text = vi.findViewById(R.id.text);
            holder.tvRising = vi.findViewById(R.id.tvRising);
            holder.tvDuration = vi.findViewById(R.id.tvDuration);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.text.setVisibility(View.VISIBLE);

        }
        else
        {
            holder.text.setVisibility(View.GONE);

            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( DataModel ) data.get( i );

            /************  Set Model values in Holder elements ***********/

            holder.tvRising.setText(tempValues.getRiseTime());
            holder.tvDuration.setText(tempValues.getDuration());

        }
        return vi;
    }


    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView tvRising, tvDuration, text;
    }

    public void clearData(){
        data.clear();
    }

}
