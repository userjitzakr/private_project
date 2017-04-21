package com.jaapps.billing;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.security.AccessController.getContext;

/**
 * Created by jithin suresh on 29-01-2017.
 */

public class Billing_data  extends SQLiteOpenHelper {

    Context mContext;
    SQLiteDatabase sqldb;

    Billing_data(Context c)
    {
        super(c, "Billing_data", null, 1);
        mContext = c;
    }


    private void print(String str){
//        Log.d("JKS",str);
    }

    public String getUserId()
    {
        SharedPreferences prefs = mContext.getSharedPreferences("USER_ID", mContext.MODE_PRIVATE);
        String restoredText = prefs.getString("userid", null);

        if (restoredText != null) {
           return restoredText;
        }
        else
            return "00001";
    }

    public  void updateUserID(String user)
    {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("USER_ID", mContext.MODE_PRIVATE).edit();
        editor.putString("userid",user);
        editor.apply();
        editor.commit();
    }
    public void openConnection() {
        sqldb = mContext.openOrCreateDatabase("Billing_Db", Context.MODE_PRIVATE, null);
        createTables();
    }

    public void closeConnection() {
        sqldb.close();
    }

    public Cursor getAllItemsForFirebase()
    {
        String query = "SELECT * FROM tbl_item";
        return sqldb.rawQuery(query,null);
    }

    public Cursor getAllPdtsForFirebase()
    {
        String query = "SELECT * FROM tbl_pdts";
        return sqldb.rawQuery(query,null);
    }

    public Cursor getAllBillsForFirebase()
    {
        String query = "SELECT * FROM tbl_bill";
        return sqldb.rawQuery(query,null);
    }
    public Cursor getAllBillItemsForFirebase()
    {
        String query = "SELECT * FROM tbl_bill_items";
        return sqldb.rawQuery(query,null);
    }

    public void clearItemTable()
    {
        String query = "delete from  tbl_item";
        sqldb.execSQL(query);
    }

    public void clearPdtTable()
    {
        String query = "delete from  tbl_pdts";
        sqldb.execSQL(query);
    }

    public void clearbillTable()
    {
        String query = "delete from  tbl_bill";
        sqldb.execSQL(query);
    }


    public void clearbillItemTable()
    {
        String query = "delete from  tbl_bill_items";
        sqldb.execSQL(query);
    }



    void printAll()
    {
        print("=======DATABASE DUMP===========================================");
        print("===table tbl_item");
        String query = "SELECT * FROM tbl_item";
        Cursor data = sqldb.rawQuery(query,null);
        while(data.moveToNext())
        {
            print("itemname = "+data.getString(0)+" item description = "+data.getString(1));
        }
        print("==============================================================");
        print("===table tbl_pdts");
        query = "SELECT * FROM tbl_pdts";
        Cursor data1 = sqldb.rawQuery(query,null);
        while(data1.moveToNext())
        {
            print("serialNumber ="+data1.getString(1)+" id="+data1.getString(0)+" itemId = "+data1.getString(2)+" description="+data1.getString(3)+" price="+data1.getString(4)+" selling_price="+
                    data1.getString(5)+" qty="+data1.getString(6)+" soldCount="+data1.getString(7)+" barcode="+data1.getString(8)+" stockDate="+data1.getString(9));
        }
        print("============================================================");

        print("===table tbl_bill");
        query = "SELECT * FROM tbl_bill";
        Cursor data2 = sqldb.rawQuery(query,null);
        while(data2.moveToNext())
        {
            print("bill_id ="+data2.getString(0)+" date="+data2.getString(1)+" discount = "+data2.getString(2)+" total="+data2.getString(3)+" total_tax="+data2.getString(4)+" grant_total="+
                    data2.getString(5));
        }
        print("============================================================");

        print("===table tbl_bill_items");
        query = "SELECT * FROM tbl_bill_items";
        Cursor data3 = sqldb.rawQuery(query,null);
        while(data3.moveToNext())
        {
            print("bill_id ="+data3.getString(0)+" item_Id="+data3.getString(1));
        }
        print("============================================================");

        print("============================================================");
    }

    public Cursor getListItemDescription()
    {
        String query = "SELECT serialNumber,description FROM tbl_pdts";
        return sqldb.rawQuery(query,null);
    }
    private void createTables() {
        sqldb.execSQL("CREATE TABLE IF NOT EXISTS tbl_item(name VARCHAR(50) ," +
                        "description VARCHAR(100),"+
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId VARCHAR(50));");

        sqldb.execSQL("CREATE TABLE IF NOT EXISTS tbl_pdts(id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "serialNumber INTEGER,"+
                "itemId INTEGER,"+
                "description VARCHAR(100) ,"+
                "price INTEGER,"+
                "selling_price INTEGER,"+
                "qty INTEGER,"+
                "soldCount INTEGER,"+
                "barcode VARCHAR(100), "+
                "stockDate DATETIME NOT NULL," +
                "userId VARCHAR(50));");

        sqldb.execSQL("CREATE TABLE IF NOT EXISTS tbl_bill(bill_id INTEGER PRIMARY KEY ," +
                "date  DATETIME NOT NULL," +
                "discount INTEGER,"+
                "total INTEGER,"+
                "total_tax INTEGER,"+
                "grant_total INTEGER,"+
                "customerName VARCHAR(100),"+
                "customerAddress VARCHAR(200),"+
                "userId VARCHAR(50));");

        sqldb.execSQL("CREATE TABLE IF NOT EXISTS tbl_bill_items(bill_id INTEGER , " +
                "item_Id INTEGER,"+
                "userId VARCHAR(50));");

        //shared preference for serial number
        SharedPreferences prefs = mContext.getSharedPreferences("SERIAL_NUMBER", mContext.MODE_PRIVATE);
        String restoredText = prefs.getString("serial_number", null);
        if (restoredText != null) {
            int serialNumber = Integer.parseInt(restoredText);
        }
        else {
            SharedPreferences.Editor editor = mContext.getSharedPreferences("SERIAL_NUMBER", mContext.MODE_PRIVATE).edit();
            editor.putString("serial_number", "1000");
            editor.apply();
            editor.commit();
        }

    }

    public boolean isRepeatedBarcode(String barcode)
    {
        String query = "SELECT barcode FROM tbl_pdts WHERE barcode LIKE '"+barcode+"' AND userId LIKE '"+getUserId() +"'";
        Cursor data = sqldb.rawQuery(query, null);
        if(data.getCount() == 0)
        {
            return  false;
        }
        else
        {
            return  true;
        }
    }

    public int getSerialNumberFromBarcode(String barcode)
    {
        int no = 0;
        String query = "SELECT serialNumber FROM tbl_pdts WHERE barcode LIKE '"+barcode+"' AND userId LIKE '"+getUserId() +"'";
        Cursor serialNum = sqldb.rawQuery(query, null);

        while (serialNum.moveToNext())
        {
            no = serialNum.getInt(0);
        }
        return no;
    }

    public String getItemName(int itemId)
    {
        String query = "SELECT name FROM tbl_item WHERE id="+itemId +" AND userId LIKE '"+getUserId() +"'";

        Cursor itemName = sqldb.rawQuery(query,null);

        while (itemName.moveToNext())
        {
            return itemName.getString(0);
        }
        return "";
    }

    public Cursor getPdtInfoFromId(String id)
    {
        String query = "SELECT * FROM tbl_pdts WHERE id="+id+" AND userId LIKE '"+getUserId() +"'";
        print( query);
        return  sqldb.rawQuery(query,null);
    }

    public Cursor getPdtInfo(int serialNo)
    {
        String query = "SELECT itemId,description,selling_price FROM tbl_pdts WHERE serialNumber="+serialNo+" AND userId LIKE '"+getUserId() +"'";;
        print(query);
        return sqldb.rawQuery(query,null);
    }

    public Cursor getPdtInfo_byid(int item_id)
    {
        String query = "SELECT serialNumber,itemId,description,selling_price FROM tbl_pdts WHERE serialNumber="+item_id+" AND userId LIKE '"+getUserId() +"'";;
        print(query);
        return sqldb.rawQuery(query,null);
    }

    public Cursor getBillDate(String from, String to)
    {
        String querry = "SELECT * FROM tbl_bill WHERE date >= Datetime('"+from+"') AND date <= Datetime('"+to+"') AND userId LIKE '"+getUserId() +"'";;
        print(querry);
        return  sqldb.rawQuery(querry,null);
    }
    public void clearBillSerialNum()
    {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("BILL_SERIAL_NUMBER", mContext.MODE_PRIVATE).edit();
        editor.putString("bill_no", "1000");
        editor.apply();
        editor.commit();
    }

    public void addBillItemFromFireBase(String billId, String pdtId)
    {
        String query = "INSERT INTO tbl_bill_items (bill_id,item_Id,userId) values("+billId+","+pdtId+",'"+getUserId()+"')";
        sqldb.execSQL(query);
    }
    public void addBillFromFireBase(String billId, String Date, String discount, String total, String totalTax, String grantTotal, String customerName, String customerAddress)
    {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("BILL_SERIAL_NUMBER", mContext.MODE_PRIVATE).edit();

        editor.putString("bill_no", billId);
        editor.apply();
        editor.commit();

        String query = "INSERT INTO tbl_bill (bill_id,date,discount,total,total_tax,grant_total,customerName, customerAddress,userId) values ("+billId+",'"+Date+
                "',"+discount+","+total+","+totalTax+","+grantTotal+",'"+customerName+"','"+customerAddress+"','"+getUserId()+"')";
        sqldb.execSQL(query);
    }

    public void addBill(int billId, List<Billing_ListMakeBill> list, int discount, int total, int totalTax, int grantTotal, List<Billing_ListMakeBill> origList,String custommerName, String custommerAddress)
    {
        String query = "SELECT * from tbl_bill WHERE  bill_id="+billId+" AND userId LIKE '"+getUserId() +"'";;
        print(query);

        Cursor bq = sqldb.rawQuery(query,null);
        if(bq.getCount() == 0)
        {
            print("bill not present ; good");
            SharedPreferences.Editor editor = mContext.getSharedPreferences("BILL_SERIAL_NUMBER", mContext.MODE_PRIVATE).edit();

            editor.putString("bill_no", Integer.toString(billId));
            editor.apply();
            editor.commit();
        }
        else
        {
            print("bill present removing previous bill");
            query = "DELETE FROM tbl_bill_items WHERE  bill_id="+billId+" AND userId LIKE '"+getUserId() +"'";;
            sqldb.execSQL(query);
            query = "DELETE FROM tbl_bill WHERE bill_id="+billId+" AND userId LIKE '"+getUserId() +"'";;
            sqldb.execSQL(query);


            for (Billing_ListMakeBill item:origList) {

                query = "UPDATE tbl_pdts SET qty=qty+1 WHERE serialNumber="+item.getItemSerialNumber()+" AND userId LIKE '"+getUserId() +"'";;
                print(query);
                sqldb.execSQL(query);
                query = "UPDATE tbl_pdts SET soldCount=soldCount-1 WHERE serialNumber="+item.getItemSerialNumber()+" AND userId LIKE '"+getUserId() +"'";;
                print(query);
                sqldb.execSQL(query);
                printAll();
            }
        }

        query = "INSERT INTO tbl_bill (bill_id,date,discount,total,total_tax,grant_total,customerName, customerAddress,userId) values ("+billId+",'"+getDateTime()+
                "',"+discount+","+total+","+totalTax+","+grantTotal+",'"+custommerName+"','"+custommerAddress+"','"+getUserId()+"')";
        sqldb.execSQL(query);

        for (Billing_ListMakeBill item:list) {
            query = "INSERT INTO tbl_bill_items (bill_id,item_Id,userId) values("+billId+","+item.getItemSerialNumber()+",'"+getUserId()+"')";
            print(query);
            sqldb.execSQL(query);
            query = "UPDATE tbl_pdts SET qty=qty-1 WHERE serialNumber="+item.getItemSerialNumber()+" AND userId LIKE '"+getUserId() +"'";;
            print(query);
            sqldb.execSQL(query);
            query = "UPDATE tbl_pdts SET soldCount=soldCount+1 WHERE serialNumber="+item.getItemSerialNumber()+" AND userId LIKE '"+getUserId() +"'";;
            print(query);
            sqldb.execSQL(query);
            printAll();
        }

    }

    public  int getTotalPriceOfBillId(int billId)
    {
        Cursor item = getBillItems(billId);
        int total = 0;
        while (item.moveToNext())
        {
            String query = "SELECT price FROM tbl_pdts WHERE serialNumber="+item.getString(0)+" AND userId LIKE '"+getUserId() +"'";;
            Cursor c = sqldb.rawQuery(query, null);
            while (c.moveToNext())
            {
                total+= c.getInt(0);
            }

        }
        return total;
    }
    public Cursor getBillInfo(int billId)
    {
        String query = "SELECT * FROM tbl_bill WHERE bill_id="+billId+" AND userId LIKE '"+getUserId() +"'";;
        return sqldb.rawQuery(query,null);
    }

    public Cursor getBillItems(int billId)
    {
        String query = "SELECT item_Id FROM tbl_bill_items WHERE bill_id="+billId+" AND userId LIKE '"+getUserId() +"'";;
        return sqldb.rawQuery(query,null);
    }
    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public Cursor getItemsOf(String id)
    {
        String query = "SELECT * FROM tbl_pdts WHERE itemId="+id+" AND userId LIKE '"+getUserId() +"'";
        print(query);
        return sqldb.rawQuery(query,null);
    }

    public void addPdtFromFireBase(String itemId, String description, String price, String sellprice, String qty, String soldCount, String barcode,String stockDate)
    {

        SharedPreferences prefs = mContext.getSharedPreferences("SERIAL_NUMBER", mContext.MODE_PRIVATE);
        String restoredText = prefs.getString("serial_number", null);
        int serialNumber = 1000;
        if (restoredText != null) {
            serialNumber = Integer.parseInt(restoredText);
        }
        else {
            SharedPreferences.Editor editor = mContext.getSharedPreferences("SERIAL_NUMBER", mContext.MODE_PRIVATE).edit();
            editor.putString("serial_number", "1000");
            editor.apply();
            editor.commit();
        }
        serialNumber++;
        String query = "INSERT INTO tbl_pdts (serialNumber,itemId,description,price,selling_price,qty,soldCount,barcode,stockDate,userId) values("+
                serialNumber+","+
                itemId +",'"+
                description +"',"+
                price+","+
                sellprice+","+
                qty+","+
                soldCount+",'"+
                barcode+"','"+
                stockDate+"','"+getUserId()+"');";
        print(query);
        sqldb.execSQL(query);

        SharedPreferences.Editor editor = mContext.getSharedPreferences("SERIAL_NUMBER", mContext.MODE_PRIVATE).edit();

        editor.putString("serial_number", Integer.toString(serialNumber));
        editor.apply();
        editor.commit();
    }

    public void clearSerialNum()
    {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("SERIAL_NUMBER", mContext.MODE_PRIVATE).edit();
        editor.putString("serial_number", "1000");
        editor.apply();
        editor.commit();
    }
    public int updateProduct(int itemId, String description, int price, int sellprice, int qty, String barcode, String id)
    {
        String query = "UPDATE tbl_pdts set description='"+description+"', price= "+
                price+", selling_price="+
                sellprice+", qty="+
                qty+", barcode='"+
                barcode+"', stockDate='"+
                getDateTime()+"',itemId="+itemId+" WHERE id="+id + " AND userId LIKE '"+getUserId() +"'";
        sqldb.execSQL(query);
        return 0;
    }
    public int addPdt(int itemId, String description, int price, int sellprice, int qty, String barcode)
    {
        SharedPreferences prefs = mContext.getSharedPreferences("SERIAL_NUMBER", mContext.MODE_PRIVATE);
        String restoredText = prefs.getString("serial_number", null);
        int serialNumber = 1000;
        if (restoredText != null) {
            serialNumber = Integer.parseInt(restoredText);
        }
        else {
            SharedPreferences.Editor editor = mContext.getSharedPreferences("SERIAL_NUMBER", mContext.MODE_PRIVATE).edit();
            editor.putString("serial_number", "1000");
            editor.apply();
            editor.commit();
        }
        serialNumber++;

        String query = "INSERT INTO tbl_pdts (serialNumber,itemId,description,price,selling_price,qty,soldCount,barcode,stockDate,userId) values("+
                serialNumber+","+
                itemId +",'"+
                description +"',"+
                price+","+
                sellprice+","+
                qty+",0,'"+
                barcode+"','"+
                getDateTime()+"','"+getUserId()+"');";
        print(query);
        sqldb.execSQL(query);

        SharedPreferences.Editor editor = mContext.getSharedPreferences("SERIAL_NUMBER", mContext.MODE_PRIVATE).edit();

        editor.putString("serial_number", Integer.toString(serialNumber));
        editor.apply();
        editor.commit();
        return serialNumber;
    }

    public int getTotalItem(int id)
    {
        int available = 0;
        String query="SELECT SUM(qty) from tbl_pdts WHERE itemId="+id+" AND userId LIKE '"+getUserId() +"'";;
        print(query);
        Cursor c = sqldb.rawQuery(query,null);

        if(c.getCount() != 0)
        {
            while(c.moveToNext())
            {
                print("SUM(qty) = "+c.getInt(0));
                available = c.getInt(0);
            }
        }
        else available = 0;
        return available;
    }
    public int getTotalSold(int id)
    {
        int available = 0;
        String query="SELECT SUM(soldCount) from tbl_pdts WHERE itemId="+id+" AND userId LIKE '"+getUserId() +"'";;
        print(query);
        Cursor c = sqldb.rawQuery(query,null);

        if(c.getCount() != 0)
        {
            while(c.moveToNext())
            {
                print("SUM(soldCount) = "+c.getInt(0));
                available = c.getInt(0);
            }
        }
        else available = 0;
        return available;
    }
    public void updateItem(String itemName, String description,String itemId)
    {
        String query = "UPDATE tbl_item SET name='"+itemName+"', description='"+description+"' WHERE id="+itemId +" AND userId LIKE '"+getUserId() +"'";
        sqldb.execSQL(query);
    }
    public void addItem(String itemName, String description)
    {
        String query = "INSERT INTO tbl_item (name,description,userId) values('"+itemName +"','"+description+"','"+getUserId()+"');";
        print(query);
        sqldb.execSQL(query);
    }

    public Cursor getItemList()
    {
        String query = "SELECT * FROM tbl_item WHERE userId LIKE '"+getUserId() +"'";;
        Cursor c = sqldb.rawQuery(query,null);
        return  c;
    }

    public Cursor getItem(String itemId)
    {
        String query = "SELECT * FROM tbl_item WHERE id ='"+itemId+"' AND userId LIKE '"+getUserId() +"'";;
        Cursor c = sqldb.rawQuery(query,null);
        return  c;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
