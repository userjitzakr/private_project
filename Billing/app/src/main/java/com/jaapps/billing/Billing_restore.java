package com.jaapps.billing;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.security.KeyStore;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Billing_restore extends AppCompatActivity {

    void print(String str)
    {
        Log.d("JKS",str);
    }

    List<Billing_ListBkup> list;
    ListView lv_bkupItem ;
    Billing_restoreAdapter adapter;
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                print("Permission is granted");
                return true;
            } else {

                print("Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("JKS","Permission is granted");
            return true;
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_restore);

        lv_bkupItem = (ListView)findViewById(R.id.lv_list_backup_restore);
        list = new ArrayList<>();

        isStoragePermissionGranted();


        File file = new File(getCacheDir()+"/billingBkup");
        if (file.exists())
        {
            print("Directory billinBkupDb exists");
            for (File f : file.listFiles()) {
                Billing_ListBkup item = new Billing_ListBkup();

                try {

                    print(""+f.toString());
                    item.sebkupText(f.toString());
                    String Date_src = f.toString().substring((file.toString().length() + 14));
                    Date_src = Date_src.substring(0, Date_src.length() - 3);
                    SimpleDateFormat sdfSource = new SimpleDateFormat("ddMMyyyy_HHmmss");
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd  yyyy HH:mm:ss");
                    Date date = sdfSource.parse(Date_src);
                    String newDate = sdf.format(date);
                    item.sebkupText(newDate);
                    item.setFileName(f.toString());
                    list.add(item);
                }
                catch (Exception e)
                {

                }
            }
            adapter = new Billing_restoreAdapter(this, list);
            lv_bkupItem.setAdapter(adapter);
        }
        else print("JKS directory doesnt exits"+file.toString());

        lv_bkupItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
            int position, long id) {

                Billing_ListBkup item = list.get(position);
                print("restore "+item.getFileName());
                Intent data = new Intent();
                data.putExtra("path",item.getFileName());
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }


    private class Billing_ListBkup{

        private  String bkupText;
        private  String fileName;


        Billing_ListBkup()
        {
            bkupText = "";
        }

        public void sebkupText(String str){bkupText = str;}
        public String getbkupText(){return bkupText;}

        public void setFileName(String str) { fileName = str;}
        public  String getFileName() { return  fileName;}

    }
    class  Billing_restoreAdapter extends BaseAdapter {

        List<Billing_ListBkup> itemList;
        Context context;

        Billing_restoreAdapter(Context C, List<Billing_ListBkup> list)
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
                convertView= LayoutInflater.from(context).inflate(R.layout.list_item_backup_restore, null, false);

                viewHolder.bkupText = (TextView)convertView.findViewById(R.id.txt_bkupRestore_bkupname);

                Typeface font = Typeface.createFromAsset(context.getAssets(), "Tahoma.ttf");

                viewHolder.bkupText.setTypeface(font);

                viewHolder.bkupText.setText(itemList.get(position).getbkupText());

                convertView.setTag(viewHolder);

            }
            else{
                viewHolder=(ViewHolder)convertView.getTag();
            }

            viewHolder.bkupText.setText(itemList.get(position).getbkupText());

            return convertView;
        }

    }
    class  ViewHolder{

        TextView bkupText;
    }
}

