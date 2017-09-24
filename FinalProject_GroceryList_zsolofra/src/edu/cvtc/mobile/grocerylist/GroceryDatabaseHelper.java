package edu.cvtc.mobile.grocerylist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class GroceryDatabaseHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "grocery.db";
	
	public static final int DATABASE_VERSION = 1;
	
	public GroceryDatabaseHelper(Context context, String name, CursorFactory factory, int version){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		GroceryTable.onCreate(database);
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		GroceryTable.onUpgrade(database, oldVersion, newVersion);
		
	}

}