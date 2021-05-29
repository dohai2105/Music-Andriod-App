package com.freecompany.chiasenhac.fragment;

import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.freecompany.chiasenhac.PlaySongActivity;
import com.freecompany.chiasenhac.R;
import com.freecompany.chiasenhac.adapter.ListSongAdapter;
import com.freecompany.chiasenhac.constant.AppController;
import com.freecompany.chiasenhac.constant.Constant;
import com.freecompany.chiasenhac.model.Song;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ListSongFragment extends Fragment{
	private View view;
	public String URL;
	private int position;
	ArrayList<Song> songLst;
	private ListSongAdapter adapter;
	private ListView listsongLv;
	private ProgressBar progressBar;
	private AppController application;
	Bundle mSavedInstanceState;
	int index = 2;
	Handler handler = new Handler();
	public ListSongFragment() {
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_listsong, container,false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initUI();
		initDATA();
		initCONTROL();
		application = (AppController) getActivity().getApplication();
		// Load firsttime from server and later time from preference
		mSavedInstanceState = savedInstanceState;
		if (position==0||position==2) {
			getData();
		}else if (position==1) {
			handler.postDelayed(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					getData();
				}
			}, 1300);
		} 
	}
	
	public void getData(){
		if (mSavedInstanceState==null) {
			if (application.isNetworkConnect()) {
				new GetRankTask().execute();
			}else{
				Toast.makeText(getActivity(), "Network not found",Toast.LENGTH_SHORT).show();
				progressBar.setVisibility(View.GONE);
			}
		}else {
			LoadFromPreference();
			progressBar.setVisibility(View.GONE);
		}
	}
	
	private void LoadFromPreference() {
		// TODO Auto-generated method stub
		songLst.clear();
		if (position==0) {
			songLst.addAll(Constant.popLst);
		}else if (position==1) {
			songLst.addAll(Constant.rapLst);
		}else{
			songLst.addAll(Constant.danceLst);
		}
	}

	public void initUI(){
		songLst = new ArrayList<Song>();
		position = getArguments().getInt("position");
		listsongLv = (ListView) view.findViewById(R.id.listsongLv);
		adapter = new ListSongAdapter(songLst, getActivity());
		listsongLv.setAdapter(adapter);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
	}
	
	public void initDATA(){
		if (position==0) {
			URL=Constant.URL1;
		}else if(position==1){
			URL = Constant.URL2;
		}else 
			URL = Constant.URL3;
	}
	
	public void initCONTROL(){
		listsongLv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> view, View arg1, int positionitem,
					long arg3) {
				// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), PlaySongActivity.class);
					intent.putExtra("songPosition",positionitem );
					intent.putExtra("tabPosition",position);
					startActivity(intent);
			}
		});
	}
	
	
	public class GetRankTask extends AsyncTask<Void, Void, ArrayList<Song>> {

		private ArrayList<Song> arr;
		@Override
		protected ArrayList<Song> doInBackground(Void... arg0) {
			arr = new ArrayList<Song>();
			try {
				Document doc = Jsoup.connect(URL).get();
				Element containerEs = doc.select("div[class = h-main4]").first();
				Elements itemEs = containerEs.select("div[class = list-r list-1]");
				for(int i = 0 ;i<itemEs.size();i++){
					Element aE = itemEs.get(i).select("a").first();
					String titleTag1 = aE.attr("title");
					if (!titleTag1.equals(" - ")) {
						String []titleTag = aE.attr("title").split(" - ");
						String title = titleTag[0];
						String singer = titleTag[1];
						String url = aE.attr("href");
						Element typeE = itemEs.get(i).select("div[class = texte2]").first();
						String []typeTag = typeE.text().split(" ");
						String time = typeTag[0];
						String type = typeTag[1];
						Song song = new Song(title,singer,time,type,url);
						arr.add(song);
					}else{
						break;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return arr;
		}

		@Override
		protected void onPostExecute(ArrayList<Song> result) {
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);//Toast.makeText(getActivity(), "xong"+position+ " "+ arr.size(), Toast.LENGTH_SHORT).show();
			songLst.clear();
			songLst.addAll(arr);
			adapter.notifyDataSetChanged();
			progressBar.setVisibility(View.GONE);
			
			if (position==0) {
				Constant.popLst.addAll(arr);
			}else if (position==1) {
				Constant.rapLst.addAll(arr);
			}else{
				Constant.danceLst.addAll(arr);
			}
		}
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
/*	public void GetOneTable() throws IOException{
		Document doc = Jsoup.connect(URL).get();
		//h-center
		Element containerEs = doc.select("div[class = h-main4]").first();
		Elements itemEs = containerEs.select("div[class = list-r list-1]");
		for(int i = 0 ;i<itemEs.size();i++){
			Element typeE = itemEs.get(i).select("div[class = texte2]").first();
			String type = typeE.text();
			Element aE = itemEs.get(i).select("a").first();
			String url = aE.attr("href");
			String title = aE.attr("title");
		}
	}*/
}
