package com.freecompany.chiasenhac.fragment;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.freecompany.chiasenhac.PlaySongActivity;
import com.freecompany.chiasenhac.R;
import com.freecompany.chiasenhac.adapter.ListSongAdapter;
import com.freecompany.chiasenhac.constant.AppController;
import com.freecompany.chiasenhac.constant.Constant;
import com.freecompany.chiasenhac.model.Song;

public class NewListSongFragment extends Fragment {
	private View view;
	private ArrayList<Song> songLst;
	private int position;
	private ListView listsongLv;
	private ListSongAdapter adapter;
	private ProgressBar progressBar;
	private String URL="";
	private AppController appController;
	private Bundle mSavedInstanceState;
	Handler handler = new Handler();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_listsong, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Constant.searchType=0;
		initUI();
		initCONTROL();
		initDATA();
		appController = (AppController) getActivity().getApplication();
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
	
	private void getData() {
		if (mSavedInstanceState==null) {
			if (appController.isNetworkConnect()) {
				new getRateSong().execute();
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
		 adapter.notifyDataSetChanged();
	}
	
	private void initUI() {
		songLst = new ArrayList<Song>();
		position = getArguments().getInt("position");
		listsongLv = (ListView) view.findViewById(R.id.listsongLv);
		adapter = new ListSongAdapter(songLst, getActivity());
		listsongLv.setAdapter(adapter);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
	}
	

	private void initCONTROL() {
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
	
	private void initDATA() {
		if (position==0) {
			URL=Constant.URL1;
		}else if(position==1){
			URL = Constant.URL2;
		}else 
			URL = Constant.URL3;
	}
	
	public class getRateSong extends AsyncTask<Void, Void,  Void>{
			ArrayList<Song> arr;
		@Override
		protected  Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			arr = new ArrayList<Song>();
			try {
				Document doc = Jsoup.connect(URL).get();
				Elements tableRow1 = doc.select("tr[class=1]");
				Elements tableRow2 =  doc.select("tr[class=2]");
				for(int i = 0;i<tableRow1.size();i++){
				Song song1 = new Song();
				Song song2 = new Song();
				AddSongData(song1,tableRow1.get(i));
				AddSongData(song2,tableRow2.get(i));
				arr.add(song1);
				arr.add(song2);
				}
				 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		@Override
		protected void onPostExecute( Void result) {
		// TODO Auto-generated method stub
			super.onPostExecute(result);
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

		private void AddSongData(Song song,Element tableRow) {
			// TODO Auto-generated method stub
			Element a = tableRow.select("a").first();
			Element span = tableRow.select("span[class=gen]").get(1);
			String tilte_singer = span.text();
			String url =  a.attr("href");
			String title = a.text();
			String singer =tilte_singer.replace(title, "");
			String time_type = tableRow.select("span[class = gensmall]").get(0).text();
			String time = time_type.split(" ")[0];
			String type = time_type.split(" ")[1];
			song.setSinger(singer);
			song.setTime(time);
			song.setTitle(title);
			song.setUrl("http://chiasenhac.com/"+url);
			song.setType(type);
		}
		
	}

}	
