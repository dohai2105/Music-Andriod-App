package com.freecompany.chiasenhac.fragment;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.freecompany.chiasenhac.PlayVideoActvity;
import com.freecompany.chiasenhac.R;
import com.freecompany.chiasenhac.adapter.ListVideoAdapter;
import com.freecompany.chiasenhac.constant.Constant;
import com.freecompany.chiasenhac.model.Video;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class ListVideoFragment  extends Fragment {
	public String URL="http://chiasenhac.com/hd/video/v-video/down";
	String tailPath = ".html";
	private ArrayList<Video> videoLst;
	private ListVideoAdapter videoAdp;
	private ListView videoLv;
	private boolean isLoading=true;
	private View view;
	Handler handler = new Handler();
	private int position;
	private View mLoadMoreFooter;
	private int index=2;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_listvideo, container, false);
		mLoadMoreFooter = inflater.inflate(R.layout.footer_loadmore, null);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initUI();
		initDATA();
		initCONTROL();
		if (position==0) {
		new getVideoData().execute();
		}else {
			handler.postDelayed(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					new getVideoData().execute();
				}
			}, 1000);
		}
	}
	private void initCONTROL() {
		// TODO Auto-generated method stub
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
					new getVideoData().execute();
				}
			}
		});
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

	}
	private void initDATA() {
		// TODO Auto-generated method stub
		if (position==0) {
			URL=Constant.URL_VIDEO1;
		}else 
			URL = Constant.URL_VIDEO2;
		 
	}
	private void initUI() {
		// TODO Auto-generated method stub
		videoLv = (ListView) view.findViewById(R.id.videoLv);
		videoLst = new ArrayList<Video>();
		videoAdp = new ListVideoAdapter(videoLst, getActivity());
		videoLv.addFooterView(mLoadMoreFooter);
		videoLv.setAdapter(videoAdp);
		position = getArguments().getInt("position");
	}
	
	public class getVideoData extends AsyncTask<Void, Void, Void>{
		ArrayList<Video> arr ;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Document doc;
			arr = new ArrayList<Video>();
			try {
			doc = Jsoup.connect(URL+index+tailPath).get();
			Element videoE = doc.select("div[class = h-main3]").first();
			Elements gensmallEs = videoE.select("div[class = img-l]");
			for(int i = 2; i<gensmallEs.size()-2;i++){
					Element typeE = gensmallEs.get(i).nextElementSibling();
					Element spanE = typeE.select("span").first();
					String type = spanE.text();
					if (!type.trim().equalsIgnoreCase("hd 1080p")&& !type.trim().equalsIgnoreCase("hd 720p")) {
						continue;
					}
					Element aE = gensmallEs.get(i).select("a").first();
					String url = "http://chiasenhac.com/" + aE.attr("href");
					String[] title_singer = aE.attr("title").split(" - ");
					String title = title_singer[0];
					String singer = title_singer[1];
					Element imgE = aE.children().first();
					String imgurl = imgE.attr("src");

					Video video = new Video(title, singer, type, url, imgurl);
					arr.add(video);
					
			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			isLoading = false;
			/*if (videoLst.size() > 0) {
				savepage  = videoLv.getFirstVisiblePosition() ;
			}*/
			videoLst.addAll(arr);
			videoAdp.notifyDataSetChanged();
			//videoLv.setSelection(savepage);
		}
	}
	
}
