package edu.cvtc.mobile.grocerylist;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class GroceryContentProvider extends ContentProvider{

	private GroceryDatabaseHelper database;
	
	private static final int GROCERY_ID = 1;
	private static final int GROCERY_FILTER = 2;

	private static final String AUTHORITY = "edu.cvtc.mobile.grocerylist.contentprovider";
	
	private static final String BASE_PATH = "grocery_table";
	

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/grocery/#", GROCERY_ID);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/filters/#", GROCERY_FILTER);
	}
	
	@Override
	public boolean onCreate() {
		this.database = new GroceryDatabaseHelper(getContext(), 
				GroceryDatabaseHelper.DATABASE_NAME,
				null, 
				GroceryDatabaseHelper.DATABASE_VERSION);
		return false;
	}

	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
	
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		

		checkColumns(projection);
		
		queryBuilder.setTables(GroceryTable.DATABASE_TABLE_GROCERY);
		
		int uriType = sURIMatcher.match(uri);
		
		switch(uriType) {
		case GROCERY_FILTER:
			
			String filter = uri.getLastPathSegment();
		
			
			if(!filter.equals(GroceryListActivity.SHOW_ALL_FILTER_STRING)) {
				queryBuilder.appendWhere(GroceryTable.GROCERY_KEY_RATING + "=" + filter);
			} else {
				selection = null;
			}
		break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = this.database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, null, null, 
		null, null);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	
	}
	
	
	@Override
	public String getType(Uri uri) {
		return null;
	}
	

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		SQLiteDatabase sqlDB = this.database.getWritableDatabase();
		
		long id = 0;
		
		int uriType = sURIMatcher.match(uri);
		
		switch(uriType)	{
		
		case GROCERY_ID:
			
			id = sqlDB.insert(GroceryTable.DATABASE_TABLE_GROCERY, null, values);
			break;
			
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		final SQLiteDatabase sqlDB = this.database.getWritableDatabase();
		int numRowsDeleted = 0;
		final int uriType = sURIMatcher.match(uri);
		switch (uriType) {
			case GROCERY_ID:
				final String id = uri.getLastPathSegment();
				numRowsDeleted = sqlDB.delete(GroceryTable.DATABASE_TABLE_GROCERY, GroceryTable.GROCERY_KEY_ID + "=" + id, null);
				break;
			default:
				throw new IllegalArgumentException("Unkown URI: " + uri);
		}
		
		if (numRowsDeleted > 0){
			getContext().getContentResolver().notifyChange(uri, null);
		}
		
		return numRowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		final SQLiteDatabase sqlDB = this.database.getWritableDatabase();
		final int uriType = sURIMatcher.match(uri);
		
		int numRowsUpdated = 0;
		
		switch (uriType) {
			case GROCERY_ID:
				final String id = uri.getLastPathSegment();
				numRowsUpdated = sqlDB.update(GroceryTable.DATABASE_TABLE_GROCERY, values,  GroceryTable.GROCERY_KEY_ID + "=" + id, null);
				break;
			default:
				throw new IllegalArgumentException("Unkown URI: " + uri);
		}
		
		if (numRowsUpdated > 0){
			getContext().getContentResolver().notifyChange(uri, null);
		}
		
		return numRowsUpdated;
	}

	private void checkColumns(String[] projection) {
		String[] available = { GroceryTable.GROCERY_KEY_ID, GroceryTable.GROCERY_KEY_TEXT };
		
		if(projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			
			if(!availableColumns.containsAll(requestedColumns))	{
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	 }
}