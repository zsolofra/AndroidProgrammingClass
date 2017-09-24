package edu.cvtc.mobile.grocerylist;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.ActionMode.Callback;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.cvtc.mobile.grocerylist.GroceryView.OnGroceryChangeListener;

public class GroceryListActivity extends SherlockFragmentActivity implements OnGroceryChangeListener, LoaderCallbacks<Cursor> {
	

	protected EditText m_vwGroceryEditText;
	
	protected Button m_vwGroceryButton;
	
	protected ListView m_vmGroceryLayout;
	
	protected GroceryCursorAdapter m_groceryAdapter;
	
	protected Menu m_vwMenu;
	
	protected int m_nFilter;
	
	protected static final String SAVED_FILTER_VALUE = "m_nFilter";
	
	public static final String SAVED_EDIT_TEXT = "m_vmGroceryEditText";
	
	protected static final int FILTER_SHOW_ALL = Menu.FIRST;
	
	private Callback mActionModeCallback;
	private ActionMode mActionMode;
	private int selected_position;
	private static final int LOADER_ID = 1;
	
	public static final String SHOW_ALL_FILTER_STRING = "" + FILTER_SHOW_ALL;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.m_nFilter = FILTER_SHOW_ALL;
		
		this.m_groceryAdapter = new GroceryCursorAdapter(this, null, 0);
		this.m_groceryAdapter.setOnGroceryChangeListener(this);
		initLayout();
		
		initAddGroceryListeners();
		
		this.getSupportLoaderManager().initLoader(LOADER_ID, null, this);
		
		final SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		m_vwGroceryEditText.setText(preferences.getString(SAVED_EDIT_TEXT, ""));
	
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		this.m_vwMenu = menu;
		return true;
    }

	

	
	protected void initLayout(){
		
		this.setContentView(R.layout.advanced);
		this.m_vmGroceryLayout = (ListView)this.findViewById(R.id.groceryListViewGroup);
		this.m_vmGroceryLayout.setAdapter(m_groceryAdapter);
		
		this.m_vmGroceryLayout.setOnItemLongClickListener (new OnItemLongClickListener() {
			
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if (mActionMode != null) {
		            return false;
		        }
		        mActionMode = getSherlock().startActionMode(mActionModeCallback);
		        selected_position = position;
		        return true;
		  }
		});
		
		this.m_vwGroceryEditText = (EditText)this.findViewById(R.id.newGroceryEditText);
		this.m_vwGroceryButton = (Button)this.findViewById(R.id.addGroceryButton);
		
		mActionModeCallback = new Callback() {
		    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		        MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.actionmenu, menu);
		        return true;
		    }

		    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		        return false;
		    }

		    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		        switch (item.getItemId()) {
		            case R.id.menu_remove:
		            	removeGrocery(((GroceryView)m_vmGroceryLayout.getChildAt(selected_position)).getGrocery());
		            	mode.finish();
		                return true;
		                
		            default:
		                return false;
		        }
		    }

		    public void onDestroyActionMode(ActionMode mode) {
		        mActionMode = null;
		    }
		};
	}


	protected void initAddGroceryListeners() {
		this.m_vwGroceryButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				String groceryText = m_vwGroceryEditText.getText().toString();
				if(groceryText != null && !groceryText.equals(""))
				{
					addGrocery(new Grocery(groceryText));
					m_vwGroceryEditText.setText("");
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(m_vwGroceryEditText.getWindowToken(), 0);
				}
			}
		});
		
		this.m_vwGroceryEditText.setOnKeyListener(new OnKeyListener()
		{
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER))
				{
					String groceryText = m_vwGroceryEditText.getText().toString();
					if(groceryText != null && !groceryText.equals(""))
					{
						addGrocery(new Grocery(groceryText));
						m_vwGroceryEditText.setText("");
						return true;
					}
				}
				if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
				{
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(m_vwGroceryEditText.getWindowToken(), 0);
					return true;
				}
				return false;
			}
		});
	}

	protected void addGrocery(Grocery grocery) {
		Uri uri = Uri.parse(GroceryContentProvider.CONTENT_URI + "/grocery/" + grocery.getID());
		
		final ContentValues contentValues = new ContentValues();
		contentValues.put(GroceryTable.GROCERY_KEY_TEXT, grocery.getGrocery());
		contentValues.put(GroceryTable.GROCERY_KEY_RATING, grocery.getRating());
		
		final Uri insertResult = this.getContentResolver().insert(uri, contentValues);
		long id = Long.parseLong(insertResult.getLastPathSegment());
		grocery.setID(id);
		
		fillData();
	}
	
	private void filterGroceryList(int filter){
		this.m_nFilter = filter;
		fillData();
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SAVED_FILTER_VALUE, this.m_nFilter);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if ( null != savedInstanceState && savedInstanceState.containsKey(SAVED_FILTER_VALUE)){
			filterGroceryList(savedInstanceState.getInt(SAVED_FILTER_VALUE));
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.getItem(0).setTitle(getMenuTitleChange());
		this.m_vwMenu = menu;
		return super.onPrepareOptionsMenu(menu);
	}
	
	private String getMenuTitleChange(){
		switch(this.m_nFilter) {
		default:
		return this.getResources().getString(R.string.show_all_menuitem);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		final SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		final Editor editor = preferences.edit();
		editor.putString(SAVED_EDIT_TEXT, m_vwGroceryEditText.getText().toString());
		editor.commit();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		Uri uri = Uri.parse(GroceryContentProvider.CONTENT_URI + "/filters/" + getFilterString());
		String[] projection = { GroceryTable.GROCERY_KEY_ID, 
				GroceryTable.GROCERY_KEY_TEXT};
		final CursorLoader loader = new CursorLoader(this, uri, projection, null, null, null);
		return loader;
	}
	
	private String getFilterString() {
		switch (this.m_nFilter) {
			default:
			return SHOW_ALL_FILTER_STRING;
			
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		this.m_groceryAdapter.swapCursor(cursor);
		this.m_groceryAdapter.setOnGroceryChangeListener(this);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		this.m_groceryAdapter.swapCursor(null);
	}
	
	private void fillData(){
		this.getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
		this.m_vmGroceryLayout.setAdapter(this.m_groceryAdapter);
	}

	@Override
	public void onGroceryChanged(GroceryView view, Grocery grocery) {
		Uri uri = Uri.parse(GroceryContentProvider.CONTENT_URI + "/grocery/" + grocery.getID());
		
		final ContentValues contentValues = new ContentValues();
		contentValues.put(GroceryTable.GROCERY_KEY_TEXT, grocery.getGrocery());
		
		this.getContentResolver().update(uri, contentValues, null, null);
		this.m_groceryAdapter.setOnGroceryChangeListener(null);
		fillData();
	}
	
	protected void removeGrocery(Grocery grocery){
		Uri uri = Uri.parse(GroceryContentProvider.CONTENT_URI + "/grocery/" + grocery.getID());
		this.getContentResolver().delete(uri, null, null);
		fillData();
	}

}
	
