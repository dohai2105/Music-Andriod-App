package com.freecompany.chiasenhac.fragment;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.freecompany.chiasenhac.PlayVideoActvity;
import com.freecompany.chiasenhac.R;
import com.freecompany.chiasenhac.adapter.ListVideoAdapter;
import com.freecompany.chiasenhac.adapter.ListVideoSearchAdapter;
import com.freecompany.chiasenhac.constant.Constant;
import com.freecompany.chiasenhac.fragment.ListVideoFragment.getVideoData;
import com.freecompany.chiasenhac.model.Song;
import com.freecompany.chiasenhac.model.Video;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class SearchVideoFragment extends Fragment{
	private View view;
	private String SEARCH_URL =  "http://search.chiasenhac.com/search.php?s=";
	private String SEARCH_REQUEST ="";
	private String SEARCH_VIDEO= "&cat=video&page=";
	private ArrayList<Video> videoLst;
	private ListVideoSearchAdapter videoAdp;
	private ListView videoLv;
	private int index=1;
	private boolean isLoading=true;
	private View mLoadMoreFooter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_listvideo, container, false);
		mLoadMoreFooter = inflater.inflate(R.layout.footer_loadmore, null);
		return view;
 	}
	
	public void setQuery(String query){
		SEARCH_REQUEST=query;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initUI();
		initCONTROL();
		new GetSearchResult().execute();
	}
	
	private void initUI() {
		// TODO Auto-generated method stub
		videoLv = (ListView) view.findViewById(R.id.videoLv);
		videoLst = new ArrayList<Video>();
		videoAdp = new ListVideoSearchAdapter(videoLst, getActivity());
		videoLv.addFooterView(mLoadMoreFooter);
		videoLv.setAdapter(videoAdp);
	}
	
	private void initCONTROL() {
		// TODO Auto-generated method stub
		videoLv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),PlayVideoActvity.class);
				intent.putExtra("url", videoLst.get(position).getUrl());
				intent.putExtra("singer", videoLst.get(position).getSinger());
				intent.putExtra("title", videoLst.get(position).getTitle());
				intent.putExtra("type", videoLst.get(position).getType());
				startActivity(intent);
			}
		});
		videoLv.setOnScrollListener(new OnScrollListener() {

			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
			}
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (isLoading) {
					return;
				}
				int total = firstVisibleItem + visibleItemCount;
				if (totalItemCount == total && !isLoading) {
					isLoading=true;
					index++;
					new GetSearchResult().execute();
				}
			}
		});

	}
	
	public class GetSearchResult extends AsyncTask<Void, Void, ArrayList<Video>>{
		ArrayList<Video> arr;
		@Override
		protected ArrayList<Video> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			arr = new ArrayList<Video>();
			try {
				SEARCH_REQUEST=SEARCH_REQUEST.replace(' ', '+');
				Document doc = Jsoup.connect(SEARCH_URL+SEARCH_REQUEST+SEARCH_VIDEO+index).get();
				Element tableContainerE = doc.select("table[class = tbtable]").first();
				Elements divEs = tableContainerE.select("div[class = tenbh]");
				Elements spanEs = tableContainerE.select("span[class =gen]");
				for(int i = 0 ;i<divEs.size();i++){
					Element aE = divEs.get(i).select("a").first();
					String url = "http://chiasenhac.com/"+aE.attr("href");
					String title = aE.text();
					String tile_singer =  divEs.get(i).text();
					String singer = tile_singer.replace(title+" ", "");
					if (url.equals("")||title.equals("")||tile_singer.equals("")||singer.equals("")) {
						break;
					}
					String time_type = spanEs.get(i).text();
					if (time_type.contains("HD 720p")|| time_type.contains("HD 1080p")) {
						String type="";
						if (time_type.contains("HD 720p")) {
							type= "HD 720p";
						}else{
							type= "HD 1080p";
						}
						Video video = new Video(title, singer, type, url, "");
						arr.add(video);
					}
				}
				Log.d("dohai", arr.size()+"");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return arr;
		}
		protected void onPostExecute(ArrayList<Video> result) {
			// TODO Auto-generated method stub
			isLoading=false;
			videoLst.addAll(arr);
			videoAdp.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}
}
