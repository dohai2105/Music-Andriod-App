package com.freecompany.chiasenhac;
import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.freecompany.chiasenhac.adapter.PlayControlPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

public class PlaySongActivity extends FragmentActivity {
	private ViewPager viewpager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playcontrol);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		initData();
	}
	
	@SuppressLint("NewApi")
	private void initData() {
		// TODO Auto-generated method stub
		viewpager = (ViewPager) findViewById(R.id.control_viewpager);
		FragmentManager fm = getSupportFragmentManager();
		PlayControlPagerAdapter pagerAdapter = new PlayControlPagerAdapter(fm);
		viewpager.setAdapter(pagerAdapter);
		CirclePageIndicator iconIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		iconIndicator.setViewPager(viewpager);
		iconIndicator.setSnap(true);
		viewpager.setCurrentItem(1);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
	}
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.playsongmenu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
 
}
