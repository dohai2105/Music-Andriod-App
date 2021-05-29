package com.freecompany.chiasenhac.fragment;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.freecompany.chiasenhac.PlaySongActivity;
import com.freecompany.chiasenhac.R;
import com.freecompany.chiasenhac.SearchActivity;
import com.freecompany.chiasenhac.adapter.ListSongAdapter;
import com.freecompany.chiasenhac.constant.Constant;
import com.freecompany.chiasenhac.model.Song;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchSongFragment extends Fragment{
	
	private String SEARCH_URL =  "http://search.chiasenhac.com/search.php?s=";
	private String SEARCH_REQUEST ="";
	private String SEARCH_ARTIST= "";
	ArrayList<Song> songLst;
	private ListSongAdapter searchAdp;
	private ProgressBar searchProgressBar;
	private ListView searchLv;
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_searchsong, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initUI();
		initCONTROL();
		if (Constant.searchType<=1) {
			new GetSearchResult().execute();
		}
	}
	public void setQuery(String query){
		SEARCH_REQUEST=query;
	}
	private void initCONTROL() {
		// TODO Auto-generated method stub
		searchLv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int positionitem, long arg3) {
				// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), PlaySongActivity.class);
					intent.putExtra("songPosition",positionitem );
					intent.putExtra("sharepreference", "SEARCHSONG");
					intent.putExtra("tabPosition",3);
					startActivity(intent);
				 
			}
		});
	}

	@SuppressLint("NewApi")
	public void initUI() {
		searchLv = (ListView) view.findViewById(R.id.searchLv);
		songLst = new ArrayList<Song>();
		searchAdp = new ListSongAdapter(songLst, getActivity());
		searchLv.setAdapter(searchAdp);
		searchProgressBar = (ProgressBar) view.findViewById(R.id.searchProgressBar);
		if (Constant.searchType==1) {
			SEARCH_ARTIST="&mode=artist";
		}
	}
	
	public class GetSearchResult extends AsyncTask<Void, Void, ArrayList<Song>>{
		ArrayList<Song> arr;
		int index = 0;
		@Override
		protected ArrayList<Song> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			arr = new ArrayList<Song>();
			try {
				SEARCH_REQUEST=SEARCH_REQUEST.replace(' ', '+');
				Document doc = Jsoup.connect(SEARCH_URL+SEARCH_REQUEST+SEARCH_ARTIST).get();
				Element tableContainerE = doc.select("table[class = tbtable]").first();
				Elements divEs = tableContainerE.select("div[class = tenbh]");
				Elements spanEs = tableContainerE.select("span[class =gen]");
				for(int i = 0 ;i<divEs.size();i++){
					Element aE = divEs.get(i).select("a").first();
					String url = "http://chiasenhac.com/"+aE.attr("href");
					String title = aE.text();
					String tile_singer =  divEs.get(i).text();
					String singer = tile_singer.replace(title+" ", "");
					String []time_type = spanEs.get(i).text().split(" ");
					String time = time_type[0];
					String type = time_type[1];
					if (url.equals("")||title.equals("")||tile_singer.equals("")||singer.equals("")||time.equals("")||type.equals("")) {
						break;
					}
					if (!type.trim().equalsIgnoreCase("HD")&&!type.trim().equalsIgnoreCase("MV")) {
						Song song = new Song(title, singer, time, type, url);
						arr.add(song);
					}
				}
				Log.d("dohai", arr.size()+"");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return arr;
		}
		@Override
		protected void onPostExecute(ArrayList<Song> result) {
			// TODO Auto-generated method stub
			songLst.clear();
			songLst.addAll(arr);
			searchAdp.notifyDataSetChanged();
			searchProgressBar.setVisibility(View.GONE);
			Constant.searchLst.addAll(arr);
			super.onPostExecute(result);
		}
		
	}
}
