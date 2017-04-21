package com.jaapps.billing;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jithin suresh on 29-01-2017.
 */

public class Billing_adapterItem extends BaseAdapter {

    List<Billing_ListItem> itemList;
    Context context;

    Billing_adapterItem(Context C)
    {
        context = C;
    }

    Billing_adapterItem(Context C, List<Billing_ListItem> list)
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
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_list_billing_items, null, false);

            viewHolder.itemText = (TextView)convertView.findViewById(R.id.txt_stock_list_itemText);
            viewHolder.img_selected = (ImageView) convertView.findViewById(R.id.img_purchase_item_img_selected);

            Typeface font = Typeface.createFromAsset(context.getAssets(), "Tahoma.ttf");
            viewHolder.itemText.setTypeface(font);


            viewHolder.itemText.setText(itemList.get(position).getItemText());

            boolean selected = itemList.get(position).getSelected();

            if(selected == true)
            {
                viewHolder.img_selected.setImageResource(R.drawable.selecteditem);
            }
            else
            {
                viewHolder.img_selected.setImageResource(R.drawable.unselecteditem);
            }

            convertView.setTag(viewHolder);

        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.itemText.setText(itemList.get(position).getItemText());
        boolean selected = itemList.get(position).getSelected();

        if(selected == true)
        {
            viewHolder.img_selected.setImageResource(R.drawable.selecteditem);
        }
        else
        {
            viewHolder.img_selected.setImageResource(R.drawable.unselecteditem);
        }

        return convertView;
    }
    class  ViewHolder{
        //    ImageView img;
        TextView itemText;
        ImageView img_selected;

    }
}
