package com.dutchrudder.leaf.listAdapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dutchrudder.leaf.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	
	private Context context;
	private List<ContactListItem> contactList;
	
	public ExpandableListAdapter(Context context, List<ContactListItem> contactListItems){
		contactList = contactListItems;
		this.context = context;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return contactList.get(groupPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 1;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
			View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater infalInflater = (LayoutInflater) this.context
                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         	convertView = infalInflater.inflate(R.layout.expanded_contact, null);
		}
		
		TextView fullname = (TextView) convertView.findViewById(R.id.full_name);
		TextView facebook = (TextView) convertView.findViewById(R.id.facebook);
		
		fullname.setText(contactList.get(groupPosition).fullName);
		facebook.setText(contactList.get(groupPosition).facebookID);
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return contactList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return contactList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
			ViewGroup parent) {
		if(convertView == null){
			LayoutInflater infalInflater = (LayoutInflater) this.context
                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         	convertView = infalInflater.inflate(R.layout.contact, null);
		}
		
		TextView shortname = (TextView) convertView.findViewById(R.id.contact_shortname);
		ImageView image = (ImageView) convertView.findViewById(R.id.contact_image);
		
		shortname.setText(contactList.get(groupPosition).name);
		if((contactList.get(groupPosition).image) != null){
			image.setBackground(new BitmapDrawable(contactList.get(groupPosition).image));
		}
		
        return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		 return contactList.get(groupPosition).fullName == null;
	}



}
