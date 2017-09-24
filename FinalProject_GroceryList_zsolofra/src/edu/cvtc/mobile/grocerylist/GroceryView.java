package edu.cvtc.mobile.grocerylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 */

/**
 * @author Zach
 *
 */
public class GroceryView extends LinearLayout {
	
	
	private TextView m_vwGroceryText;
	private Grocery m_grocery;
	private OnGroceryChangeListener m_onGroceryChangeListener;

	public GroceryView(Context context, Grocery grocery) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.grocery_view, this, true);
		this.m_vwGroceryText = (TextView)findViewById(R.id.groceryTextView);
		this.setGrocery(grocery);
	}
	
	public Grocery getGrocery(){
		return m_grocery;
	}
	
	public void setGrocery(Grocery grocery){
		this.m_grocery = grocery;
		this.m_vwGroceryText.setText(m_grocery.getGrocery());
	}
	
	public void setOnGroceryChangeListener(OnGroceryChangeListener listener) {
		//TODO
		this.m_onGroceryChangeListener = listener;
	}
	
	protected void notifyOnGroceryChangeListener() {
		if (null != m_onGroceryChangeListener) {
			m_onGroceryChangeListener.onGroceryChanged(this, this.m_grocery);
		}
	}
	
	public static interface OnGroceryChangeListener {

		
		public void onGroceryChanged(GroceryView view, Grocery grocery);
	}

}
