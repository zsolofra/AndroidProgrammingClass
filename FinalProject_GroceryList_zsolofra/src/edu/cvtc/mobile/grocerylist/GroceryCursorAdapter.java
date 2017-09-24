package edu.cvtc.mobile.grocerylist;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import edu.cvtc.mobile.grocerylist.GroceryView.OnGroceryChangeListener;

public class GroceryCursorAdapter extends CursorAdapter {

	private OnGroceryChangeListener m_listener;

	public GroceryCursorAdapter(Context context, Cursor groceryCursor, int flags) {
		super(context, groceryCursor, flags);
		this.m_listener = null;
	}

	
	public void setOnGroceryChangeListener(OnGroceryChangeListener mListener) {
		this.m_listener = mListener;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final Grocery grocery = new Grocery(cursor.getString(GroceryTable.GROCERY_COL_TEXT));
		((GroceryView)view).setOnGroceryChangeListener(null);
		((GroceryView)view).setGrocery(grocery);
		((GroceryView)view).setOnGroceryChangeListener(this.m_listener);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup vg) {
		final Grocery grocery = new Grocery(cursor.getString(GroceryTable.GROCERY_COL_TEXT));
		final GroceryView groceryView = new GroceryView(context, grocery);
		groceryView.setOnGroceryChangeListener(this.m_listener);
		return groceryView;
	}

}