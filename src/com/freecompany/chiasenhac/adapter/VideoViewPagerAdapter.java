package com.freecompany.chiasenhac.adapter;

import com.freecompany.chiasenhac.fragment.ListVideoFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class VideoViewPagerAdapter extends FragmentStatePagerAdapter {

	public VideoViewPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		 
		return getListSongFragment(position);
	}
	
	Fragment getListSongFragment(int position){
		Bundle bundle = new Bundle();
			bundle.putInt("position", position);
			ListVideoFragment listVideoFragment = new ListVideoFragment();
			listVideoFragment.setArguments(bundle);
			return listVideoFragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
