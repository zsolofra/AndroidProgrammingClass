package edu.cvtc.mobile.grocerylist;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GroceryTable {

public static final String DATABASE_TABLE_GROCERY = "grocery_table";
	
	public static final String GROCERY_KEY_ID = "_id";
	public static final int GROCERY_COL_ID = 0;
	
	public static final String GROCERY_KEY_TEXT = "grocery_text";
	public static final int GROCERY_COL_TEXT = GROCERY_COL_ID + 1;
	
	public static final String GROCERY_KEY_RATING = "grocery_rating";
	public static final int GROCERY_COL_RATING = GROCERY_COL_ID + 2;
	
	public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE_GROCERY + " (" + 
			GROCERY_KEY_ID + " integer primary key autoincrement, " + 
			GROCERY_KEY_TEXT	+ " text not null, " + 
			GROCERY_KEY_RATING	+ " integer not null);";
	
	public static final String DATABASE_DROP = "drop table if exists " + DATABASE_TABLE_GROCERY;
	

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(GroceryTable.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion);
		database.execSQL(DATABASE_DROP);
		onCreate(database);
	}
}
