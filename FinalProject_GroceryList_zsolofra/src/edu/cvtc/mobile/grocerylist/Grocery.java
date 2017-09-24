package edu.cvtc.mobile.grocerylist;

public class Grocery {
	
	private String m_strGrocery;
	public static final int UNRATED = 0;
	private int m_nRating;
	private long m_nID = 0;
	
	public Grocery() {
		
		this.m_strGrocery = "";
		this.m_nRating = UNRATED;
		this.m_nID = 0;
		
	}
	
	public Grocery(String strGrocery){
		this.m_strGrocery = strGrocery;
		this.m_nRating = UNRATED;
	}
	
	public Grocery(String strGrocery, int nRating){
		this.m_strGrocery = strGrocery;
		this.m_nRating = nRating;
	}
	
	public Grocery(String strGrocery, int nRating, long id){
		this.m_strGrocery = strGrocery;
		this.m_nRating = nRating;
		this.m_nID = id;
	}
	
	
	public String getGrocery() {
		
		return m_strGrocery;	
	}
	
	public void setGrocery(String strGrocery) {
		this.m_strGrocery = strGrocery;
	}
	
	public int getRating() {
		return this.m_nRating;
	}
	
	public void setRating(int rating) {
		this.m_nRating = rating;
	}
	
	public long getID(){
		return this.m_nID;
	}
	
	public void setID(long id){
		this.m_nID = 0;
	}
	
	public String toString(){
		
		return this.m_strGrocery;
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Grocery) {
			Grocery grocery2 = (Grocery)obj;
			return this.m_nID == grocery2.m_nID;
			
		}
		return false;
	}
}
