package com.example.xpense;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "expenseManager";
	private static final String TABLE_EXPENSES = "expenses";
	private static final String KEY_ID = "id";
	private static final String KEY_TYPE = "type";
	private static final String KEY_DATE = "date";
	private static final String KEY_NAME = "name";
	
	public DBHandler(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//3rd argument to be passed is CursorFactory instance
	}
	
	// Creating Tables 
	@Override
	public void onCreate(SQLiteDatabase db){
		String CREATE_EXPENSES_TABLE = "CREATE TABLE " 
										+ TABLE_EXPENSES 
										+ "(" +KEY_ID+ " INTEGER PRIMARY KEY," +KEY_TYPE+ " INTEGER," +KEY_DATE+ " INTEGER," +KEY_NAME+ " TEXT" + ")"; 
		db.execSQL(CREATE_EXPENSES_TABLE);
	}
	
	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
		// Drop older table if existed 
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
		// Create tables again 
		onCreate(db);
	}
	
	// code to add the new expense
	void addExpense(Expense expense){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TYPE, expense.getType());
		values.put(KEY_DATE, expense.getDate());
		values.put(KEY_NAME, expense.getName());
		
		// Inserting Row 
		db.insert(TABLE_EXPENSES, null, values);
		// Closing database connection
		db.close();
	}
	
	// code to get the single contact
	Expense getExpense(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_EXPENSES, new String[]{KEY_ID,KEY_TYPE,KEY_DATE,KEY_NAME}, KEY_ID+"=?", new String[]{String.valueOf(id)}, null, null, null, null);
		if(cursor != null)
			cursor.moveToFirst();
		Expense expense = new Expense(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), cursor.getString(3));
		//return expense
		return expense;
	}
	
	// code to get all contacts in a list view
	public List<Expense> getAllExpenses(){
		List<Expense> expenseList = new ArrayList<Expense>();
		// Select All Query 
		String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if(cursor.moveToFirst()){
			do{
				Expense expense = new Expense();
				expense.setId(Integer.parseInt(cursor.getString(0)));
				expense.setType(Integer.parseInt(cursor.getString(1)));
				expense.setDate(Integer.parseInt(cursor.getString(2)));
				expense.setName(cursor.getString(3));
				// Adding contact to list
				expenseList.add(expense);
			}while(cursor.moveToNext());
		}
		return expenseList;
	}
	
	// code to update the single contact
	public int updateExpense(Expense expense){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TYPE, expense.getType());
		values.put(KEY_DATE, expense.getDate());
		values.put(KEY_NAME, expense.getName());
		// updating row
		return db.update(TABLE_EXPENSES, values, KEY_ID + "=?", new String[]{String.valueOf(expense.getId())});
	}
	
	// Deleting single contact
	public void deleteExpense(Expense expense){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_EXPENSES, KEY_ID + "=?", new String[]{String.valueOf(expense.getId())});
		db.close();
	}
	
	// Getting contacts Count
	public int getExpensesCount(){
		String countQuery = "SELECT  * FROM " + TABLE_EXPENSES;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		// return count
		return cursor.getCount();
	}
	
	
}
