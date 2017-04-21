package com.jaapps.billing;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Billing_stock extends AppCompatActivity {

    ListView lv_stock;
    List<Billing_ListStock> list;
    Billing_adapterStock adapterStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_stock);

        fillStockData();
    }

    void print(String str){
        Log.d("JKS",str);}

    private void fillStockData() {
        lv_stock = (ListView) findViewById(R.id.lv_stock);
        list = new ArrayList<>();

        Billing_data data = new Billing_data(this);
        data.openConnection();
        Cursor itemData = data.getItemList();

        while (itemData.moveToNext()) {
            print("item = " + itemData.getString(0) + "  decription = " + itemData.getString(1) + "  id = " + itemData.getInt(2));
            Billing_ListStock item = new Billing_ListStock();
            int id = itemData.getInt(2);
            int totalItems = data.getTotalItem(id);
            int soldItems = data.getTotalSold(id);

            item.setItemId(itemData.getString(2));
            item.setItemName(itemData.getString(0));
            item.setTotal_available((totalItems));
            item.setTotal_sold(soldItems);
            list.add(item);
        }

        data.closeConnection();


        adapterStock = new Billing_adapterStock(this, list);
        lv_stock.setAdapter(adapterStock);

        lv_stock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Billing_ListStock item = list.get(position);
                Intent stock = new Intent(Billing_stock.this, Billing_stock_item_info.class);
                stock.putExtra("id",item.getItemId());
                stock.putExtra("obj",item);
                startActivity(stock);
            }
        });
    }
}
