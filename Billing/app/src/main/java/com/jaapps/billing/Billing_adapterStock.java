package com.jaapps.billing;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jithin suresh on 29-01-2017.
 */

public class Billing_adapterStock extends BaseAdapter {

    List<Billing_ListStock> itemList;
    Context context;

    Billing_adapterStock(Context C, List<Billing_ListStock> list)
    {
        itemList = list;
        context = C;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }
    @Override
    public Object getItem(int position) {

        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;

        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_list_billing_stock, null, false);

            viewHolder.itemText = (TextView)convertView.findViewById(R.id.txt_stock_list_itemText);
            viewHolder.total_available = (TextView)convertView.findViewById(R.id.txt_list_stock_total_available);
            viewHolder.total_sold = (TextView) convertView.findViewById(R.id.txt_list_stock_total_sold);

            Typeface font = Typeface.createFromAsset(context.getAssets(), "Tahoma.ttf");
            TextView txt_total_available_label = (TextView)convertView.findViewById(R.id.txt_list_stock_itemAvailable);
            TextView txt_total_sold_label = (TextView)convertView.findViewById(R.id.txt_list_stock_sold_label);

            viewHolder.itemText.setTypeface(font,Typeface.BOLD);
            viewHolder.total_available.setTypeface(font);
            viewHolder.total_sold.setTypeface(font);

            txt_total_available_label.setTypeface(font);
            txt_total_sold_label.setTypeface(font);

            viewHolder.itemText.setText(itemList.get(position).getItemName());
            viewHolder.total_available.setText((itemList.get(position).getTotal_available()+" "));
            viewHolder.total_sold.setText((itemList.get(position).getTotal_sold()+" "));

            convertView.setTag(viewHolder);

        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.itemText.setText(itemList.get(position).getItemName());
        viewHolder.total_available.setText((itemList.get(position).getTotal_available()+" "));
        viewHolder.total_sold.setText((itemList.get(position).getTotal_sold()+" "));

        return convertView;
    }
    class  ViewHolder{
        //    ImageView img;
        TextView itemText;
        TextView total_available;
        TextView total_sold;

    }
}
