package com.dutchrudder.leaf.listAdapter;

import android.graphics.Bitmap;

public class ContactListItem {

	public String name;
	public Bitmap image;
	public String fullName;
	public String facebookID;

	
	public ContactListItem (String name, Bitmap image, String fullname, String facebook){
		this.name = name;
		this.image = image;
		this.fullName = fullname;
		this.facebookID = facebook;
	}
	
}
