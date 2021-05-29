package com.freecompany.chiasenhac.fragment;

import java.util.ArrayList;
import com.freecompany.chiasenhac.OnMenuClickListener;
import com.freecompany.chiasenhac.R;
import com.freecompany.chiasenhac.adapter.MenuAdapter;
import com.freecompany.chiasenhac.constant.Constant;
import com.freecompany.chiasenhac.model.SlideMenuItem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MenuFragment extends Fragment{
	private View view;
	private ArrayList<SlideMenuItem> menuLst;
	private ListView menuLv;
	private MenuAdapter menuAdp;
	OnMenuClickListener mainActivityPointer;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_meunu, container, false);
		menuLv = (ListView) view.findViewById(R.id.left_menu);
		menuLst = new ArrayList<SlideMenuItem>();
		initDATA();
		menuAdp = new MenuAdapter(getActivity(), menuLst );
		menuLv.setAdapter(menuAdp);
		Log.d("dohai", "onCreateView");
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initCONTROL();
	}
	public void initDATA(){
		for(int i =0;i<21 ;i++){
			SlideMenuItem slideMenuItem = new SlideMenuItem(Constant.menuTitle.get(i));
			menuLst.add(slideMenuItem);
		}
	}
	public void setPointer(OnMenuClickListener pointer){
		mainActivityPointer=pointer;
	}
	public void initCONTROL(){
		menuLv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				// TODO Auto-generated method stub
				if ( position!=0&&position!=1 &&position !=8&&position!=15) {
					mainActivityPointer.onMenuClick(position);
				}
			}
		});
	}
}
