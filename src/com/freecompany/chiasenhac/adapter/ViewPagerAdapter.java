package com.freecompany.chiasenhac.adapter;

import com.freecompany.chiasenhac.fragment.ListSongFragment;
import com.freecompany.chiasenhac.fragment.NewListSongFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
	int flag =0;
	
	public ViewPagerAdapter(FragmentManager fm, int flag) {
		super(fm);
		this.flag=flag;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return getListSongFragment(position);
	}
	Fragment getListSongFragment(int position){
		Bundle bundle = new Bundle();
		if (flag ==0) {
			bundle.putInt("position", position);
			ListSongFragment listSongFragment = new ListSongFragment();
			listSongFragment.setArguments(bundle);
			return listSongFragment;
		}else{
			bundle.putInt("position", position);
			NewListSongFragment newListSongFragment = new NewListSongFragment();
			newListSongFragment.setArguments(bundle);
			return newListSongFragment;
		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

}
