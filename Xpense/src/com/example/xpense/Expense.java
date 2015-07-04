package com.example.xpense;

public class Expense {
	int _id;
	int _type;
	int _date;
	String _name;
	
	public Expense(){}
	public Expense(int id,int type,int date,String name){
		this._id = id;
		this._type = type;
		this._date = date;
		this._name = name;
	}
	
	public Expense(int type,int date,String name){
		this._type = type;
		this._date = date;
		this._name = name;
	}
	
	public int getId(){
		return this._id;
	}
	
	public void setId(int id){
		this._id = id;
	}
	
	public int getType(){
		return this._type;
	}
	
	public void setType(int type){
		this._type = type;
	}
	
	public int getDate(){
		return this._date;
	}
	
	public void setDate(int date){
		this._date = date;
	}
	
	public String getName(){
		return this._name;
	}
	
	public void setName(String name){
		this._name = name;
	}
}
