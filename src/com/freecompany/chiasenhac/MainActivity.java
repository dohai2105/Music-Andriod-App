package com.freecompany.chiasenhac;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.SearchManager;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;
import com.freecompany.chiasenhac.adapter.VideoViewPagerAdapter;
import com.freecompany.chiasenhac.adapter.ViewPagerAdapter;
import com.freecompany.chiasenhac.constant.Constant;
import com.freecompany.chiasenhac.fragment.MenuFragment;
import com.freecompany.chiasenhac.makefolder.SimpleFileDialog;
import com.freecompany.chiasenhac.service.MediaService;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements OnMenuClickListener, OnQueryTextListener{
	private ViewPager viewpager;
	private FragmentManager fm;
	private ViewPagerAdapter vAdapter;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle toggle;
	private boolean TwoTimeBackClikc = false;
	private SlidingMenu menu;
	private SearchView searchView;
	private boolean isVideo;
	private ActionBar.TabListener tabListener;
	protected String m_chosen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//new GetRankTask().execute();
		unitUI();
		initCONTROL();
		initDATA();
		initSlideMenu();
	}
	public void initSlideMenu(){
		// configure the SlidingMenu
		MenuFragment mf = new MenuFragment();
		mf.setPointer(MainActivity.this);
		menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.content_fram);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mf)
		.commit();
	}
	public void unitUI(){
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		fm = getSupportFragmentManager();
		vAdapter = new ViewPagerAdapter(fm,1);
		viewpager.setAdapter(vAdapter);
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		toggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer_light, R.string.app_name, R.string.app_name) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};
		drawerLayout.setDrawerListener(toggle);
	}

	public void initDATA() {
		SharedPreferences saveFolder = getSharedPreferences("SAVE_FOLDER", Context.MODE_PRIVATE);
		Editor editor = saveFolder.edit();
		if (saveFolder.getString("SAVE", "").equals("")) {
			editor.putString("SAVE", Environment.getExternalStorageDirectory().getAbsolutePath()+"/ChiaSeNhac");
			editor.commit();
		}
	}
	 
	public void initCONTROL() {
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				 getActionBar().setSelectedNavigationItem(position);
			 
			}
			
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		tabListener = new ActionBar.TabListener() {
			public void onTabReselected(Tab arg0,
					android.app.FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				
			}

			public void onTabSelected(Tab tab,
					android.app.FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(tab.getPosition());
			}

			public void onTabUnselected(Tab arg0,
					android.app.FragmentTransaction arg1) {
				// TODO Auto-generated method stub
			}
	    };
	    initActionBarTab();
	}
	public void initActionBarTab(){
		 final ActionBar actionBar = getActionBar();
		    if (!isVideo) {
		    	actionBar.removeAllTabs();
				for (int i = 0; i < 3; i++) {
					if (i == 0) {
						actionBar.addTab(actionBar.newTab().setText("POP-ROCK").setTabListener(tabListener));
					} else if (i == 1) {
						actionBar.addTab(actionBar.newTab().setText("RAP-HIPHOP").setTabListener(tabListener));
					} else
						actionBar.addTab(actionBar.newTab().setText("DANCE-REMIX").setTabListener(tabListener));
				}
			} else {
				actionBar.removeAllTabs();
				for (int i = 0; i < 2; i++) {
					if (i == 0) {
						actionBar.addTab(actionBar.newTab().setText("New Download").setTabListener(tabListener));
					} else
						actionBar.addTab(actionBar.newTab().setText("New Share").setTabListener(tabListener));
				}
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) searchItem.getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setOnQueryTextListener(this);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			menu.toggle();
			break;
		case R.id.folder_config:
			showFolderConfig();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showFolderConfig() {
		// TODO Auto-generated method stub

		/////////////////////////////////////////////////////////////////////////////////////////////////
		//Create FileOpenDialog and register a callback
		/////////////////////////////////////////////////////////////////////////////////////////////////
		SimpleFileDialog FolderChooseDialog =  new SimpleFileDialog(MainActivity.this, "FolderChoose",
				new SimpleFileDialog.SimpleFileDialogListener()
		{
			public void onChosenDir(String chosenDir) 
			{
				// The code in this function will be executed when the dialog OK button is pushed
						m_chosen = chosenDir;
						Log.d("dohai", m_chosen);
						SharedPreferences saveFolder = getSharedPreferences("SAVE_FOLDER", Context.MODE_PRIVATE);
						Editor editor = saveFolder.edit();
						editor.putString("SAVE", m_chosen);
						editor.commit();
			}
		});
		
		FolderChooseDialog.chooseFile_or_Dir();
		
		/////////////////////////////////////////////////////////////////////////////////////////////////
		
	
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		toggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		toggle.onConfigurationChanged(newConfig);
	}
 
	public void onMenuClick(int position) {
		// TODO Auto-generated method stub
			getActionBar().setTitle(Constant.menuTitle.get(position));
			setURL(position);
			menu.toggle();
			viewpager.setCurrentItem(0);
			FragmentManager newFragmentManager = getSupportFragmentManager();
				Constant.popLst.clear();
				Constant.rapLst.clear();
				Constant.danceLst.clear();
			if (position>=9&&position<15) {
				vAdapter = new ViewPagerAdapter(newFragmentManager,0);
				viewpager.removeAllViews();
				viewpager.setAdapter(vAdapter);
				isVideo=false;
				Constant.isSearchVideo=false;
				initActionBarTab();
				searchView.setQueryHint("#Tên bài hát");
				Constant.searchType=0;
			}else if (position>15) {
				VideoViewPagerAdapter videoPagerAdapter = new VideoViewPagerAdapter(newFragmentManager);
				viewpager.removeAllViews();
				viewpager.setAdapter(videoPagerAdapter);
				isVideo=true;
				Constant.isSearchVideo=true;
				initActionBarTab();
				searchView.setQueryHint("#Tên video");
				Constant.searchType=2;
			} else if (position<8) {
				viewpager.removeAllViews();
				vAdapter = new ViewPagerAdapter(newFragmentManager, 1);
				viewpager.setAdapter(vAdapter);
				isVideo=false;
				Constant.isSearchVideo=false;
				initActionBarTab();
				searchView.setQueryHint("#Tên bài hát");
				Constant.searchType=0;
			}
	}
	private void setURL(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 9:
			setConstantURL(Constant.URL_VETNAM_POP,Constant.URL_VETNAM_HIPHOP,Constant.URL_VETNAM_DANCE);
			break;
		case 10:
			setConstantURL(Constant.URL_THUYNGA_POP,Constant.URL_THUYNGA_HIPHOP,Constant.URL_THUYNGA_DANCE);
			break;
		case 11:
			setConstantURL(Constant.URL_AUMY_POP,Constant.URL_AUMY_HIPHOP,Constant.URL_AUMY_DANCE);
			break;
		case 12:
			setConstantURL(Constant.URL_HOA_POP,Constant.URL_HOA_HIPHOP,Constant.URL_HOA_DANCE);
			break;
		case 13:
			setConstantURL(Constant.URL_HAN_POP,Constant.URL_HAN_HIPHOP,Constant.URL_HAN_DANCE);
			break;
		case 14:
			setConstantURL(Constant.URL_OTHER_POP,Constant.URL_OTHER_HIPHOP,Constant.URL_OTHER_DANCE);
			break;
			//----------
		case 2:
			setConstantURL(Constant.URL_VETNAM_POP+"down.html",Constant.URL_VETNAM_HIPHOP+"down.html",Constant.URL_VETNAM_DANCE+"down.html");
			break;
		case 3:
			setConstantURL(Constant.URL_THUYNGA_POP+"down.html",Constant.URL_THUYNGA_HIPHOP+"down.html",Constant.URL_THUYNGA_DANCE+"down.html");
			break;
		case 4:
			setConstantURL(Constant.URL_AUMY_POP+"down.html",Constant.URL_AUMY_HIPHOP+"down.html",Constant.URL_AUMY_DANCE+"down.html");
			break;
		case 5:
			setConstantURL(Constant.URL_HOA_POP+"down.html",Constant.URL_HOA_HIPHOP+"down.html",Constant.URL_HOA_HIPHOP+"down.html");
			break;
		case 6:
			setConstantURL(Constant.URL_HAN_POP+"down.html",Constant.URL_HAN_HIPHOP+"down.html",Constant.URL_HAN_DANCE+"down.html");
			break;
		case 7:
			setConstantURL(Constant.URL_OTHER_POP+"down.html",Constant.URL_OTHER_HIPHOP+"down.html",Constant.URL_OTHER_DANCE+"down.html");
			break;
			//------
		case 16:
			setVideoURL(Constant.URL_VIDEO_VN1,Constant.URL_VIDEO_VN2);
			break;
		case 17:
			setVideoURL(Constant.URL_VIDEO_AU1,Constant.URL_VIDEO_AU2);
			break;
		case 18:
			setVideoURL(Constant.URL_VIDEO_HOA1,Constant.URL_VIDEO_HOA2);
			break;
		case 19:
			setVideoURL(Constant.URL_VIDEO_HAN1,Constant.URL_VIDEO_HAN2);
			break;
		case 20:
			setVideoURL(Constant.URL_VIDEO_OTHER1,Constant.URL_VIDEO_OTHER2);
			break;
		default:
			break;
		}
	}
	private void setVideoURL (String url1 , String url2){
		Constant.URL_VIDEO1 = url1;
		Constant.URL_VIDEO2 = url2;
	}
	private void setConstantURL(String url1 , String url2 , String url3){
		Constant.URL1=url1;
		Constant.URL2=url2;
		Constant.URL3=url3;
	}
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		/*searchView.setQuery("", false);
		searchView.setIconified(false); */
		return false;
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	protected void onStop() {
		super.onStop();
	};
	@Override
	public void onDestroy() {
		//unbindService(mConnection);
		super.onDestroy();
		stopService(new Intent(this, MediaService.class));
		Constant.reset();
	 
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (TwoTimeBackClikc) {
			super.onBackPressed();
			return;
		}
		TwoTimeBackClikc = true;
		Toast.makeText(MainActivity.this, "Nhấn quay lại lần nữa để thoát ",Toast.LENGTH_SHORT).show();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			public void run() {
				TwoTimeBackClikc = false;
			}
		}, 2000);
	}

}
