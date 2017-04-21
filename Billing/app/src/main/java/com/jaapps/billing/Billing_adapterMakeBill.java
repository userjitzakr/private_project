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

public class Billing_adapterMakeBill extends BaseAdapter {



    List<Billing_ListMakeBill> itemList;
    Context context;

    Billing_adapterMakeBill(Context C, List<Billing_ListMakeBill> list)
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
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_list_billing, null, false);

            viewHolder.itemText = (TextView)convertView.findViewById(R.id.txt_list_bill_item);
            viewHolder.itemDescription = (TextView)convertView.findViewById(R.id.txt_list_bill_description);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.txt_list_bill_amount);
            viewHolder.slNo = (TextView) convertView.findViewById(R.id.txt_list_bill_slno);

            Typeface font = Typeface.createFromAsset(context.getAssets(), "Tahoma.ttf");

            viewHolder.itemText.setTypeface(font);
            viewHolder.itemDescription.setTypeface(font);
            viewHolder.amount.setTypeface(font);
            viewHolder.slNo.setTypeface(font);


            viewHolder.itemText.setText(itemList.get(position).getItemName());
            viewHolder.itemDescription.setText((itemList.get(position).getDescription()+" "));
            viewHolder.amount.setText((itemList.get(position).getAmount()+" "));
            viewHolder.slNo.setText(itemList.get(position).getSlNo());

            convertView.setTag(viewHolder);

        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.itemText.setText(itemList.get(position).getItemName());
        viewHolder.itemDescription.setText((itemList.get(position).getDescription()+" "));
        viewHolder.amount.setText((itemList.get(position).getAmount()+" "));
        viewHolder.slNo.setText(itemList.get(position).getSlNo());

        return convertView;
    }
    class  ViewHolder{
        TextView slNo;
        TextView itemText;
        TextView itemDescription;
        TextView amount;
    }
}
