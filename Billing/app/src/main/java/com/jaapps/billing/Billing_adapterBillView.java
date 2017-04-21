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
 * Created by jithin suresh on 04-02-2017.
 */

public class Billing_adapterBillView extends BaseAdapter {

    List<Billing_ListBillView> itemList;
    Context context;

    Billing_adapterBillView(Context C, List<Billing_ListBillView> list)
    {
        context = C;
        itemList = list;
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
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_list_billview, null, false);

            viewHolder.date = (TextView)convertView.findViewById(R.id.txt_list_billview_date);
            viewHolder.billNo = (TextView)convertView.findViewById(R.id.txt_list_billview_billno);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.txt_list_billview_amt);


            Typeface font = Typeface.createFromAsset(context.getAssets(), "Tahoma.ttf");

            viewHolder.date .setTypeface(font);
            viewHolder.billNo.setTypeface(font);
            viewHolder.amount.setTypeface(font);



            viewHolder.date .setText(itemList.get(position).getbillDate());
            viewHolder.billNo.setText((itemList.get(position).getbilId()+" "));
            viewHolder.amount.setText((itemList.get(position).getbillAmount()+" "));


            convertView.setTag(viewHolder);

        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.date .setText(itemList.get(position).getbillDate());
        viewHolder.billNo.setText((itemList.get(position).getbilId()+" "));
        viewHolder.amount.setText((itemList.get(position).getbillAmount()+" "));


        return convertView;
    }
    class  ViewHolder{
        TextView date;
        TextView billNo;
        TextView amount;
    }
}
