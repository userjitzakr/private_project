package com.jaapps.billing;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Billing_stock_item_info extends AppCompatActivity {

    void print(String str){ Log.d("JKS",str);}
    AlphaAnimation buttonClick;
    String itemId;

    ListView lv_stock;
    List<Billing_ListStockItem> list;
    Billing_StockItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_stock_item_info);

        Intent argument = getIntent();
         itemId = argument.getStringExtra("id");

        //print("item = "+itemId);
        Billing_ListStock item = (Billing_ListStock) argument.getSerializableExtra("obj");

        //print("data 1 = "+item.getItemName() +" data2 ="+item.getTotal_available());

        TextView txt_nameLabel = (TextView)findViewById(R.id.txt_stockItem_item_name_label);
        TextView txt_name = (TextView)findViewById(R.id.txt_stockItem_name);
        TextView txt_sold_label = (TextView)findViewById(R.id.txt_stockItem_totalAvailable_label);
        TextView txt_sold = (TextView)findViewById(R.id.txt_stockItem_totalAvailable);

        Typeface font = Typeface.createFromAsset(getAssets(), "Tahoma.ttf");

        txt_name.setTypeface(font);
        txt_nameLabel.setTypeface(font,Typeface.BOLD);
        txt_sold.setTypeface(font);
        txt_sold_label.setTypeface(font,Typeface.BOLD);

        txt_name.setText(item.getItemName());
        txt_sold.setText(""+item.getTotal_available());

        Button edit_item = (Button) findViewById(R.id.btn_stockItem_editItem);

        buttonClick = new AlphaAnimation(1F, 0.8F);

        edit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent intent = new Intent(Billing_stock_item_info.this, Billing_addItem.class);
                intent.putExtra("edit","true");
                intent.putExtra("id",itemId);
                startActivityForResult(intent,101);
            }
        });

        lv_stock = (ListView)findViewById(R.id.lv_stockItem);
        list = new ArrayList<>();
        adapter = new Billing_StockItemAdapter(this,list);

        Billing_data data = new Billing_data(this);
        data.openConnection();
        Cursor stocks = data.getItemsOf(itemId);

        while(stocks.moveToNext())
        {
            Billing_ListStockItem dataItem = new Billing_ListStockItem();

            dataItem.setId(stocks.getString(0));
            dataItem.setSerialNumber(stocks.getString(1));
            dataItem.setDecription(stocks.getString(3));
            dataItem.setActual_price(stocks.getString(4));
            dataItem.setSelling_price(stocks.getString(5));
            dataItem.setAvailable(stocks.getString(6));
            dataItem.setSold(stocks.getString(7));
            dataItem.setDate(stocks.getString(9));

            list.add(dataItem);
        }

        lv_stock.setAdapter(adapter);

        lv_stock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Billing_ListStockItem item = list.get(position);

                Intent purchaseEdit = new Intent(Billing_stock_item_info.this, Billing_purchase.class);
                purchaseEdit.putExtra("edit","true");
                purchaseEdit.putExtra("id",item.getId());
                startActivity(purchaseEdit);
            }
        });
    }


    private class Billing_ListStockItem{

        private  String serialNumber;
        private  String date;
        private  String decription;
        private  String actual_price;
        private  String selling_price;
        private  String available;
        private  String sold;
        private  String id;


        Billing_ListStockItem()
        {

        }

        public void setId(String str){id = str;}
        public void setSerialNumber(String str) {serialNumber = str;}
        public void setDate(String str) {date = str;}
        public void setDecription(String str){decription = str;}
        public void setActual_price(String  str){actual_price = str;}
        public void setSelling_price(String str){selling_price=str;}
        public void setAvailable(String str){available = str;}
        public void setSold(String str){sold = str;}

        public String getSerialNumber(){return  serialNumber;}
        public String getDate(){return date;}
        public String getDecription() { return decription;}
        public String getActual_price() { return  actual_price;}
        public String getSelling_price() { return  selling_price;}
        public String getAvailable() { return  available;}
        public String getSold() { return sold;}
        public String getId() {return  id;}

    }
    class  Billing_StockItemAdapter extends BaseAdapter {

        List<Billing_ListStockItem> itemList;
        Context context;

        Billing_StockItemAdapter(Context C, List<Billing_ListStockItem> list)
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
                convertView= LayoutInflater.from(context).inflate(R.layout.layout_stock_item, null, false);

                viewHolder.date = (TextView)convertView.findViewById(R.id.txt_item_date);
                viewHolder.serialNum = (TextView)convertView.findViewById(R.id.txt_item_serial);
                viewHolder.decription = (TextView)convertView.findViewById(R.id.txt_item_description);
                viewHolder.actual_price = (TextView)convertView.findViewById(R.id.txt_item_price);
                viewHolder.selling_price = (TextView)convertView.findViewById(R.id.txt_item_selling_price);
                viewHolder.available = (TextView)convertView.findViewById(R.id.txt_item_available);
                viewHolder.sold = (TextView)convertView.findViewById(R.id.txt_item_sold);


                Typeface font = Typeface.createFromAsset(context.getAssets(), "Tahoma.ttf");

                viewHolder.date.setTypeface(font);
                viewHolder.serialNum.setTypeface(font);
                viewHolder.decription.setTypeface(font);
                viewHolder.actual_price.setTypeface(font);
                viewHolder.selling_price.setTypeface(font);
                viewHolder.available.setTypeface(font);
                viewHolder.sold.setTypeface(font);

                TextView txt1 = (TextView) convertView.findViewById(R.id.textView6);
                TextView txt2 = (TextView) convertView.findViewById(R.id.textView18);
                TextView txt3 = (TextView) convertView.findViewById(R.id.textView14);
                TextView txt4 = (TextView) convertView.findViewById(R.id.textView11);
                TextView txt5 = (TextView) convertView.findViewById(R.id.textView10);
                TextView txt6 = (TextView) convertView.findViewById(R.id.textView15);

                txt1.setTypeface(font,Typeface.BOLD);
                txt2.setTypeface(font,Typeface.BOLD);
                txt3.setTypeface(font,Typeface.BOLD);
                txt4.setTypeface(font,Typeface.BOLD);
                txt5.setTypeface(font,Typeface.BOLD);
                txt6.setTypeface(font,Typeface.BOLD);

                viewHolder.date.setText(itemList.get(position).getDate());
                viewHolder.selling_price.setText(itemList.get(position).getSelling_price());
                viewHolder.serialNum.setText(itemList.get(position).getSerialNumber());
                viewHolder.decription.setText(itemList.get(position).getDecription());
                viewHolder.actual_price.setText(itemList.get(position).getActual_price());
                viewHolder.available.setText(itemList.get(position).getAvailable());
                viewHolder.sold.setText(itemList.get(position).getSold());

                convertView.setTag(viewHolder);

            }
            else{
                viewHolder=(ViewHolder)convertView.getTag();
            }
            viewHolder.date.setText(itemList.get(position).getDate());
            viewHolder.selling_price.setText(itemList.get(position).getSelling_price());
            viewHolder.serialNum.setText(itemList.get(position).getSerialNumber());
            viewHolder.decription.setText(itemList.get(position).getDecription());
            viewHolder.actual_price.setText(itemList.get(position).getActual_price());
            viewHolder.available.setText(itemList.get(position).getAvailable());
            viewHolder.sold.setText(itemList.get(position).getSold());


            return convertView;
        }

    }
    class  ViewHolder{

        TextView date;
        TextView serialNum;
        TextView decription;
        TextView actual_price;
        TextView selling_price;
        TextView available;
        TextView sold;
    }
}
