package com.freecompany.chiasenhac.adapter;

import com.freecompany.chiasenhac.fragment.ListSongPlayerFragment;
import com.freecompany.chiasenhac.fragment.LyricFragment;
import com.freecompany.chiasenhac.fragment.PlayerControlFragment;
import com.viewpagerindicator.IconPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.freecompany.chiasenhac.R;

public class PlayControlPagerAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter{
	protected static final int[] ICONS = new int[] {
		R.drawable.indicator_player_song_list,
		R.drawable.indicator_player_main_control,
		R.drawable.indicator_player_lyric };

	public PlayControlPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 0) {
			return new ListSongPlayerFragment();
		} else if (position == 1) {
			return new PlayerControlFragment();
		} else {
			return new LyricFragment();
		}
	}

	@Override
	public int getCount() {
		return 3;
	}

	public int getIconResId(int index) {
		// TODO Auto-generated method stub
		return ICONS[index % ICONS.length];
	}
}
