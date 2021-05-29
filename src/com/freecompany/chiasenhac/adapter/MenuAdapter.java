package com.freecompany.chiasenhac.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.freecompany.chiasenhac.R;
import com.freecompany.chiasenhac.model.SlideMenuItem;

public class MenuAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<SlideMenuItem> menuitemLst;
	LayoutInflater layoutInflater;

	public MenuAdapter(Context mContext, ArrayList<SlideMenuItem> menuitemLst) {
		super();
		this.mContext = mContext;
		this.menuitemLst = menuitemLst;
		layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 3;
	}
	
	public int getItemViewType(int position) {

	    //we have an image so we are using an ImageRow
	    if (position==0) {return 0;}
	    else if (position==1||position==8||position==15) {
			return 1;
		}
	    //we don't have an image so we are using a Description Row
	    else return 2;
	}
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		
		if (position==0) {
			View view = convertView;
			if (view ==null) {
				view = layoutInflater.inflate(R.layout.adapter_menu_header,viewGroup, false);
			}
			return view ;
		}else if (position==1||position==8||position==15) {
			View view = convertView;
			if (view ==null) {
				view = layoutInflater.inflate(R.layout.adapter_menu_hightlight,viewGroup, false);
			}
			TextView menuTitle = (TextView) view.findViewById(R.id.menuNameTv);
			menuTitle.setText(menuitemLst.get(position).getName());
			return view ;
		}else {
			View view = convertView;
			if (view == null) {
				view = layoutInflater.inflate(R.layout.adapter_menu, viewGroup,false);
			}
			TextView menuTitle = (TextView) view.findViewById(R.id.menuNameTv);
			menuTitle.setText(menuitemLst.get(position).getName());
			return view;
		}
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return menuitemLst.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return menuitemLst.get(arg0);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
