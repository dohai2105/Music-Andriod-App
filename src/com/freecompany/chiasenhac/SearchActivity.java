package com.freecompany.chiasenhac;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import com.freecompany.chiasenhac.constant.Constant;
import com.freecompany.chiasenhac.fragment.SearchSongFragment;
import com.freecompany.chiasenhac.fragment.SearchVideoFragment;

public class SearchActivity extends FragmentActivity implements OnQueryTextListener {
	private SearchView searchView;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		handlerIntent(getIntent());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.search_menu, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) searchItem.getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setOnQueryTextListener(this);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		handlerIntent(intent);
	}

	private void handlerIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("vao search", "vao search");
		if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
			String query = intent.getStringExtra(SearchManager.QUERY);
		/*	 SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
		                MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
		        suggestions.saveRecentQuery(query, null);*/
			if (Constant.searchType<=1) {
			Constant.searchLst.clear();
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			SearchSongFragment frag = new SearchSongFragment();
			frag.setQuery(query);
			ft.replace(R.id.searchContainer, frag);
			ft.commit();
			}
			else {
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				SearchVideoFragment frag = new SearchVideoFragment();
				frag.setQuery(query);
				ft.replace(R.id.searchContainer, frag);
				ft.commit();
			}
			//new GetSearchResult().execute();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.search_setting:
			showDialogSearchSetting();
			break;
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showDialogSearchSetting() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(this);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_download);
		final TextView titleTv = (TextView) dialog.findViewById(R.id.titleDown);
		 
		if (Constant.searchType==0) {
			titleTv.setText("Bạn đang tìm theo : #Tên bài hát");
		}else if (Constant.searchType==1) {
			titleTv.setText("Bạn đang tìm theo : #Tên ca sĩ");
		}else{
			titleTv.setText("Bạn đang tìm theo : #Tên video");
		}
		ListView typeLv = (ListView) dialog.findViewById(R.id.listView1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type_search,R.layout.spinner_textview_dropdown);
		typeLv.setAdapter(adapter);
		typeLv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapterView, View view, int position,long l) {
				// TODO Auto-generated method stub
				Constant.searchType=position;
				if (position==0) {
					titleTv.setText("Bạn đang tìm theo : #Tên bài hát");
					searchView.setQueryHint("#Tên bài hát");
				}else if (position==1) {
					titleTv.setText("Bạn đang tìm theo : #Tên ca sĩ");
					searchView.setQueryHint("#Tên ca sĩ");
				}else{
					titleTv.setText("Bạn đang tìm theo : #Tên video");
					searchView.setQueryHint("#Tên video");
				}
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
/*		searchView.setQuery("", false);
		searchView.setIconified(false); 
		searchView.setIconifiedByDefault(true);*/
		return false;
	}
	
}
