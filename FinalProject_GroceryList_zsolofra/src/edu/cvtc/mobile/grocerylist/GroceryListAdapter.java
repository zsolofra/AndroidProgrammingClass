/**
 * 
 */
package edu.cvtc.mobile.grocerylist;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author Zach
 *
 */
public class GroceryListAdapter extends BaseAdapter{

	private Context m_context;
	private List<Grocery> m_groceryList;
	
	public GroceryListAdapter(Context context, List<Grocery> groceryList) {
		this.m_context = context;
		this.m_groceryList = groceryList;
	}
	
	@Override
	public int getCount() {
		return this.m_groceryList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.m_groceryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GroceryView groceryView = null;
		
		if(convertView == null){
			groceryView = new GroceryView(m_context, this.m_groceryList.get(position));
		}else {
			groceryView = (GroceryView)convertView;
		}
		groceryView.setGrocery(this.m_groceryList.get(position));
		return groceryView;
	}

}
