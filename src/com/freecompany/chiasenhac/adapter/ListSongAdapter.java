package com.freecompany.chiasenhac.adapter;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import com.freecompany.chiasenhac.R;
import com.freecompany.chiasenhac.model.Song;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ListSongAdapter extends BaseAdapter{
	LayoutInflater inflater ;
	ArrayList<Song> songLst ;
	Context mContext;
	public int chooseitem=500;
	private boolean isplay=false;
	private String TYPE_FOLDER = "";
	private String TYPE = "";
	private String streamURL_part1="";
	private String streamURL_part2="";
	int mPostion;
	
	
	public ListSongAdapter(ArrayList<Song> songLst, Context mContext) {
		super();
		this.songLst = songLst;
		this.mContext = mContext;
		inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return songLst.size();
	}

	public Object getItem(int positon) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return 0;
	}
	class MyHolder {
		TextView new_song_title;
		TextView new_song_singer;
		TextView new_song_time;
		TextView new_song_type;
		Spinner option_btn;
		TextView playing_state;
		public MyHolder(View v){
			new_song_title = (TextView) v.findViewById(R.id.new_song_title);
			new_song_singer = (TextView) v.findViewById(R.id.new_song_singer);
			new_song_time = (TextView) v.findViewById(R.id.new_song_time);
			new_song_type = (TextView) v.findViewById(R.id.new_song_type);
			playing_state = (TextView) v.findViewById(R.id.playingStateTv);
			option_btn = (Spinner) v.findViewById(R.id.option_btn);
		}
	}
	
	public View getView(final int  positon, View converView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = converView;
		mHolder = null;
		if (view == null) {
			view = inflater.inflate(R.layout.adapter_itemsong, parent,false);
			mHolder = new MyHolder(view);
			view.setTag(mHolder);
		}else{
			mHolder = (MyHolder) view.getTag();
		}
		if (songLst.get(positon).getType().equals("Lossless")) {
			mHolder.new_song_type.setTextColor(mContext.getResources().getColor(R.color.loss_lesscolor));
		}else {
			mHolder.new_song_type.setTextColor(mContext.getResources().getColor(R.color.normal_color));
		}
		if (isplay) {
		if (chooseitem == positon) {
			mHolder.playing_state.setVisibility(View.VISIBLE);
		} else {
			mHolder.playing_state.setVisibility(View.GONE);
		}
		}
		mHolder.new_song_title.setText(songLst.get(positon).getTitle());
		mHolder.new_song_singer.setText(songLst.get(positon).getSinger());
		mHolder.new_song_time.setText(songLst.get(positon).getTime());
		mHolder.new_song_type.setText(songLst.get(positon).getType());
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.menu_spinner, R.layout.spinner_textview);
		adapter.setDropDownViewResource(R.layout.spinner_textview_dropdown);
		mHolder.option_btn.setAdapter(adapter);
		mHolder.option_btn.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,int itemposition, long arg3) {
				// TODO Auto-generated method stub
				if (itemposition==1) {
					// TODO Auto-generated method stub'
					mPostion= positon;
					final Dialog dialog = new Dialog(mContext);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.dialog_download);
					ListView typeLv = (ListView) dialog.findViewById(R.id.listView1);
					TextView title = (TextView) dialog.findViewById(R.id.titleDown);
					title.setText("Download : "+ songLst.get(mPostion).getTitle()+" - "+songLst.get(mPostion).getSinger());
					ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.type_download,R.layout.spinner_textview_dropdown);
					typeLv.setAdapter(adapter);
					typeLv.setOnItemClickListener(new OnItemClickListener() {

								public void onItemClick(AdapterView<?> arg0,View arg1, int positionLv, long arg3) {
									// TODO Auto-generated method stub
									if (positionLv == 0) {
										TYPE = "mp3";
										TYPE_FOLDER = "128";
										new GetSongStreamTask().execute();
										dialog.dismiss();
									} else if (positionLv == 1) {
										if (songLst.get(mPostion).getType().trim().equalsIgnoreCase("320kbps")|| songLst.get(mPostion).getType().trim().equalsIgnoreCase("lossless")) {
											TYPE = "mp3";
											TYPE_FOLDER = "320";
											new GetSongStreamTask().execute();
											dialog.dismiss();
										}
									} else if (positionLv == 2) {
										if (songLst.get(mPostion).getType().trim().equalsIgnoreCase("lossless")) {
											TYPE = "m4a";
											TYPE_FOLDER = "m4a";
											new GetSongStreamTask().execute();
											dialog.dismiss();
										}
									} else if (positionLv == 3) {
										if (songLst.get(mPostion).getType().trim().equalsIgnoreCase("lossless")) {
											TYPE_FOLDER = "flac";
											TYPE = "flac";
											new GetSongStreamTask().execute();
											dialog.dismiss();
										}
									}
								}
							});
							notifyDataSetChanged();
							dialog.show();
						}
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		return view;
	}
	public void updateAdapter(int index){
		chooseitem=index;
		isplay = true;
		notifyDataSetChanged();
	}

	private MyHolder mHolder;
	
	public class GetSongStreamTask extends AsyncTask<Void, Void, String> {

		private Pattern pattern;
		private Matcher matcher;
		private String streamURL;
		@Override
		protected void onPreExecute() {
			streamURL="";
		};
		protected String doInBackground(Void... params) {
			try {
				Document doc = Jsoup.connect(songLst.get(mPostion).getUrl()).get();
				Element streamE = doc.select("object").first();
				if (streamE!=null) {
				Element embedE = streamE.select("embed").first();
				String srcURL = embedE.attr("src");
				streamURL = "";
				pattern = Pattern.compile("audioUrl=(.*?)&autoPlay");
				matcher = pattern.matcher(srcURL);
				if (matcher.find()) {
					streamURL = matcher.group(1);
				}
				
				String[] str = streamURL.split("128");
				streamURL_part1 = str[0];
				streamURL_part1 = URLDecoder.decode(URLDecoder.decode(streamURL_part1, "utf-8"), "utf-8");
				streamURL_part2 = str[1].substring(3, str[1].length() - 3);
				streamURL = streamURL_part1 + TYPE_FOLDER + "/"+streamURL_part2+ TYPE;
				Log.d("dohai", streamURL);
				return streamURL;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "";
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (!streamURL_part1.equals("")&&!streamURL_part2.equals("")) {
				Song song = songLst.get(mPostion);
				DownloadSong(song.getTitle(), song.getType(), streamURL);
			}
		}
	}
	
	public void DownloadSong(String title ,String type,String URL){
		if (!URL.equals("")) {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.GINGERBREAD) {
			// Do something for froyo and above versions
			BroadcastReceiver receiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					String action = intent.getAction();
					if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
						// long downloadId = intent.getLongExtra(
						// DownloadManager.EXTRA_DOWNLOAD_ID, 0);
						Query query = new Query();
						query.setFilterById(enqueue);
						Cursor c = dm.query(query);
						if (c.moveToFirst()) {
							int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
							if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                                /*ImageView view = (ImageView) findViewById(R.id.imageView1);
                                String uriString = c
                                        .getString(c
                                                .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                view.setImageURI(Uri.parse(uriString));*/
							}
						}
					}
				}
			};

			mContext.registerReceiver(receiver, new IntentFilter(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE));

			Environment.getExternalStoragePublicDirectory("ChiaSeNhac").mkdirs();
			dm = (DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
			Request request = new Request(Uri.parse(URL));
            /*try{
            	request.allowScanningByMediaScanner();
            }catch (Exception e){
            	e.printStackTrace();
            }*/
            //Set whether this download may proceed over a roaming connection.
            request.setAllowedOverRoaming(false);
            //Set the title of this download, to be displayed in notifications (if enabled).
			request.setTitle("Dowloading..." + title + "."+TYPE);
			String m_sdcardDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
			String m_save = mContext
					.getSharedPreferences("SAVE_FOLDER", Context.MODE_PRIVATE)
					.getString("SAVE", "").replace(m_sdcardDirectory, "");
			Log.d("m_save", m_save);
			request.setDestinationInExternalPublicDir(m_save, title + "-"+ TYPE_FOLDER + "."+TYPE);
			enqueue = dm.enqueue(request);
			}
		}
	}
	private long enqueue;
	private DownloadManager dm;
}
