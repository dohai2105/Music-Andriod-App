package com.freecompany.chiasenhac.fragment;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.freecompany.chiasenhac.R;
import com.freecompany.chiasenhac.StopServiceActivity;
import com.freecompany.chiasenhac.constant.AppController;
import com.freecompany.chiasenhac.constant.Constant;
import com.freecompany.chiasenhac.model.Song;
import com.freecompany.chiasenhac.service.MediaService;
import com.freecompany.chiasenhac.service.MediaService.MyLocalBinder;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PlayerControlFragment extends Fragment {
	private View view;
	private MediaPlayer mediaPlayer;
	private MediaService mediaService;
	protected boolean mBound=false;
	private ImageButton btnShuffle;
	private ImageButton btnPlay;
	private ImageButton btnPrevious;
	private ImageButton btnNext;
	private ImageButton btnRepeat;
	private SeekBar seekBar;
	private TextView songTotalDurationLabel;
	private TextView songCurrentDurationLabel;
	private String intentFilterBroadcast = "com.example.chiasenhac.intentBroadcast";
	private String URL = "";
	private boolean isBroadCastRunning=false;
	private String TYPE_FOLDER = "";
	private String TYPE = "";
	private String streamURL_part1="";
	private String streamURL_part2="";
	private ProgressBar waitingProgressBar;
	private ArrayList<Song> songLst;
	private int positionSong;
	private Song songToPlay;
	private boolean isRepeat=false;
	private boolean issuffle=false;
	private AppController application;
	private boolean isActive = true;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_playercontrol, container,false);
		Log.d("dohai", "onCreateView");
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		//setHasOptionsMenu(true);
		initUI();
		initCONTROL();
		initDATA();
	}

	private void initUI() {
		btnShuffle = (ImageButton) view.findViewById(R.id.btnShuffle);
		btnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
		btnPrevious = (ImageButton) view.findViewById(R.id.btnPrevious);
		btnNext = (ImageButton) view.findViewById(R.id.btnNext);
		btnRepeat = (ImageButton) view.findViewById(R.id.btnRepeat);
		seekBar = (SeekBar) view.findViewById(R.id.songProgressBar);
		songTotalDurationLabel = (TextView) view.findViewById(R.id.songTotalDurationLabel);
		songCurrentDurationLabel = (TextView) view.findViewById(R.id.songCurrentDurationLabel);
		waitingProgressBar = (ProgressBar) view.findViewById(R.id.waitingProgress);
		application = (AppController) getActivity().getApplication();
		typeTv = (TextView) view.findViewById(R.id.typeTv);
		
		spinner = (Spinner)view.findViewById(R.id.typeSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.planets_array, R.layout.spinner_textview);
		adapter.setDropDownViewResource(R.layout.spinner_textview_dropdown);
		spinner.setAdapter(adapter);
	}
	
	private void initCONTROL() {
		// TODO Auto-generated method stub
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if (fromUser) {
					 int seekPos = seekBar.getProgress();
						mediaService.updateSeekPos(seekPos);
				}
			}
		});
		
		btnPlay.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mediaPlayer.isPlaying()) {
					mediaService.pauseMedia();
					btnPlay.setImageResource(R.drawable.state_btn_play) ;
					mediaService.cancelNotification();
				}else  {
					mediaService.playMedia();
					btnPlay.setImageResource(R.drawable.state_btn_pause) ;
				}
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (positionSong<songLst.size()-1) {
					positionSong = positionSong+1;
					playSong(positionSong);
				}else
					playSong(0);
			}
		});
		btnPrevious.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (positionSong==0) {
					positionSong= songLst.size()-1;
					playSong(positionSong);
				}else{
					positionSong = positionSong-1;
					playSong(positionSong);
				}
			}
		});
		btnRepeat.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isRepeat) {
					if (issuffle) {
						issuffle=false ;
						btnShuffle.setImageResource(R.drawable.state_btn_suffle);
					}
				btnRepeat.setImageResource(R.drawable.state_btn_repeat_click);
				isRepeat=true;
				}else{
					btnRepeat.setImageResource(R.drawable.state_btn_repeat);
					isRepeat=false;
				}
			}
		});
		btnShuffle.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!issuffle) { 
					if (isRepeat) {
						isRepeat = false;
						btnRepeat.setImageResource(R.drawable.state_btn_repeat);
					}
					btnShuffle.setImageResource(R.drawable.state_btn_suffle_click);
					issuffle = true;
				}else{
					btnShuffle.setImageResource(R.drawable.state_btn_suffle);
					issuffle = false;
				}
			}
		});
	/*	listviewplay.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				// TODO Auto-generated method stub
				positionSong=position;
				playSong(position);
			}
		});*/
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) {
				// TODO Auto-generated method stub
				if (!streamURL_part1.equals("")&&!streamURL_part2.equals("")) {
				if (position==1) {
						TYPE_FOLDER="128";
						TYPE="mp3";
						String url =streamURL_part1+TYPE_FOLDER+ streamURL_part2+ TYPE;
						mediaService.playSong(url,songToPlay,positionSong,true,"128kbps");
						typeTv.setText("128kbps");
						spinner.setSelection(0);
				}else if (position==2) {
					if (songToPlay.getType().trim().equalsIgnoreCase("320kbps")||songToPlay.getType().trim().equalsIgnoreCase("lossless")) {
						TYPE_FOLDER="320";
						TYPE="mp3";
						String url =streamURL_part1+TYPE_FOLDER+ streamURL_part2+ TYPE;
						mediaService.playSong(url,songToPlay,positionSong,true,"320kbps");
						typeTv.setText("320kbps");
						spinner.setSelection(0);
					}
				}else if (position==3) {
					 if (songToPlay.getType().trim().equalsIgnoreCase("lossless")) {
						 	TYPE_FOLDER="m4a";
							TYPE="m4a";
							String url =streamURL_part1+TYPE_FOLDER+ streamURL_part2+ TYPE;
							mediaService.playSong(url,songToPlay,positionSong,true,"500kbps");
							typeTv.setText("500kpbs");
							spinner.setSelection(0);
					} 
				} else if (position==4) {
					if (songToPlay.getType().trim().equalsIgnoreCase("lossless")){
						TYPE_FOLDER="flac";
						TYPE="flac";
						String url =streamURL_part1+TYPE_FOLDER+ streamURL_part2+ TYPE;
						mediaService.playSong(url,songToPlay,positionSong,true,"Lossless");
						typeTv.setText("Lossless");
						spinner.setSelection(0);
					}
				}
			}
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		
	}
	
	public void initDATA(){
		int tabPosition = getActivity().getIntent().getIntExtra("tabPosition", 1000);
		songLst = new ArrayList<Song>();
		positionSong = getActivity().getIntent().getIntExtra("songPosition", 0);
		if (tabPosition==0) {
			Constant.playLst.clear();
			Constant.playLst.addAll(Constant.popLst);
		}else if (tabPosition==1) {
			Constant.playLst.clear();
			Constant.playLst.addAll(Constant.rapLst);
		}else if (tabPosition==2){
			Constant.playLst.clear();
			Constant.playLst.addAll(Constant.danceLst);
		}else if (tabPosition==3){
			Constant.playLst.clear();
			Constant.playLst.addAll(Constant.searchLst);
		}
		songLst.addAll(Constant.playLst);
		if (songLst.size()>0) {
			songToPlay = songLst.get(positionSong);
			Intent intent = new Intent(getActivity(), MediaService.class);
			if (!isMyServiceRunning()) {
				getActivity().startService(intent);
			}
			playSong(positionSong);
		}else{
			Log.d("dohai", "faiel_________");
		}
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			updateUI(intent);
		}
	};
	
	public void playSong(int position){
		songToPlay = songLst.get(position);
		URL=songToPlay.getUrl();
		if (songToPlay.getType()!=null&&application.isNetworkConnect()) { // mp3, flat ...
			Change_Type_TYPE_FOLDER(songToPlay.getType());
			new GetSongStreamTask().execute();
		} else {
			Toast.makeText(getActivity(), "Network not found",Toast.LENGTH_SHORT).show();
			waitingProgressBar.setVisibility(View.GONE);
		}
		if (isActive) {
		getActivity().getActionBar().setTitle(songToPlay.getTitle());
		getActivity().getActionBar().setSubtitle(songToPlay.getSinger());
		}
	}
	
	private void Change_Type_TYPE_FOLDER(String type) {
		  if (type.trim().equalsIgnoreCase("320kbps")) {
				TYPE = "mp3";
				TYPE_FOLDER= "320";
				typeTv.setText("320kbps");
			}else if(type.trim().equalsIgnoreCase("lossless")){
				TYPE="m4a";
				TYPE_FOLDER="m4a";
				typeTv.setText("500kbps");
			} else {
				TYPE = "mp3";
				TYPE_FOLDER = "128";
				typeTv.setText("128kbps");
			}
		}
	
	protected void updateUI(Intent intent) {
		// TODO Auto-generated method stub
		String songTotalDuration = intent.getStringExtra("songTotalDuration");
		String songCurrentDuration = intent.getStringExtra("songCurrentDuration");
		String seekBarProgress = intent.getStringExtra("seekBarProgress");
		String seekBarMax = intent.getStringExtra("seekBarMax");
		songTotalDurationLabel.setText(songTotalDuration);
		songCurrentDurationLabel.setText(songCurrentDuration);
		seekBar.setProgress(Integer.parseInt(seekBarProgress));
		seekBar.setMax(Integer.parseInt(seekBarMax));
		if (mediaPlayer.isPlaying()&&waitingProgressBar.getVisibility()==View.VISIBLE) {
			waitingProgressBar.setVisibility(View.GONE);
			btnPlay.setImageResource(R.drawable.state_btn_pause) ;
		}else if (!mediaPlayer.isPlaying()&&waitingProgressBar.getVisibility()==View.GONE) {
			waitingProgressBar.setVisibility(View.VISIBLE);
			btnPlay.setImageResource(R.drawable.state_btn_play) ;
		} 
	}
	
	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			mBound = false;
		}
		
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			MyLocalBinder binder = (MyLocalBinder) service;
			mediaService = binder.getService();
			mBound=true;
			mediaPlayer = mediaService.getMediaPlayer();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					if (isRepeat) {
						mediaService.playMedia();
					} else if (issuffle) {
						Random rand = new Random();
						positionSong = rand.nextInt((songLst.size() - 1) - 0 + 1) + 0;
						playSong(positionSong);
					} else {
						if (positionSong < songLst.size() - 1) {
							positionSong++;
						} else {
							positionSong = 0;
						}
						playSong(positionSong);
					}
				}
			});
			if (isMyServiceRunning() && !isBroadCastRunning) {
				getActivity().registerReceiver(mReceiver,new IntentFilter(intentFilterBroadcast));
			}
			isBroadCastRunning = true;
		}
	};
	private TextView typeTv;
	private Spinner spinner;
 
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		isActive=true;
		if (songToPlay!=null&&Constant.playLst.size()>0) {
		getActivity().getActionBar().setTitle(songToPlay.getTitle());
		getActivity().getActionBar().setSubtitle(songToPlay.getSinger());
		Intent intent = new Intent(getActivity(), MediaService.class);
		getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		// save type of spinner 
		if (getActivity().getIntent().getStringExtra("saveType") != null) {
			if (!getActivity().getIntent().getStringExtra("saveType").equals("")) {
				typeTv.setText(getActivity().getIntent().getStringExtra("saveType"));
			}
		}
		}
		if (Constant.playLst.size()==0) {
			Intent dismissIntent = new Intent(getActivity(),StopServiceActivity.class);
			startActivity(dismissIntent);
			getActivity().finish();
		}
		Log.d("dohai", "onStart_Fagment");
	} 
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		isActive=false;
		if (mBound) {
			getActivity().unbindService(mConnection);
		}
		if (isBroadCastRunning) {
			getActivity().unregisterReceiver(mReceiver);
			isBroadCastRunning = false;
		}
		Log.d("dohai", "onStop_Fragment");
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onDestroy() {
		Log.d("dohai", "onDestroy");
		super.onDestroy();
	}
	
	public class GetSongStreamTask extends AsyncTask<Void, Void, String> {

		private Pattern pattern;
		private Matcher matcher;
		private String streamURL="";
		@Override
		protected void onPreExecute() {
			Constant.lyric="";
			Constant.songImg="";
		};
		protected String doInBackground(Void... params) {
			try {
				Document doc = Jsoup.connect(URL).get();
				Element streamE = doc.select("object").first();
				Element lyricE = doc.select("div#fulllyric").first();
				if (lyricE.select("img")!=null&&!lyricE.select("img").isEmpty()) {
					if (lyricE.select("img").get(0).attr("src") != null) {
						Constant.songImg  = lyricE.select("img").get(0).attr("src");
					}
				}
				if (lyricE.select("p[class =genmed ]") != null&&!lyricE.select("p[class =genmed ]").isEmpty()) {
					Constant.lyric = lyricE.select("p[class =genmed ]").html();
					Constant.lyric.replace("on ChiaSeNhac.com", "");
				}
				if (streamE != null) {
					Element embedE = streamE.select("embed").first();
					String srcURL = embedE.attr("src");
					streamURL = "";
					pattern = Pattern.compile("audioUrl=(.*?)&autoPlay");
					matcher = pattern.matcher(srcURL);
					if (matcher.find()) {
						streamURL = matcher.group(1);
					}
					streamURL = URLDecoder.decode(URLDecoder.decode(streamURL, "utf-8"), "utf-8");
					String[] str = streamURL.split("128");
					if (streamURL.length() > 1) {
						streamURL_part1 = str[0];
						streamURL_part2 = str[1].substring(0,str[1].length() - 3);
						streamURL = streamURL_part1 + TYPE_FOLDER+ streamURL_part2 + TYPE;
						return streamURL;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "";
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (!result.equals("")&&songToPlay!=null&&mediaService!=null&&mediaPlayer!=null) {
				mediaService.playSong(result,songToPlay,positionSong,false,"");
			}
			if (isActive) {
			TextView lyric = (TextView) getActivity().findViewById(R.id.lyric_content);	
			lyric.setText(Html.fromHtml(Constant.lyric));
			ImageView img = (ImageView) getActivity().findViewById(R.id.songImg);
				if (!Constant.songImg.equals("")) {
					ImageLoader iml = ImageLoader.getInstance();
					iml.displayImage(Constant.songImg, img);
				} else {
					img.setImageResource(R.drawable.bgsong);
				}
			}
			super.onPostExecute(result);
		}
	}
	
	 private boolean isMyServiceRunning() {
	    	try{
			    ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
			    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			        if (com.freecompany.chiasenhac.service.MediaService.class.getName().equals(service.service.getClassName())) {
			            return true;
			        }
			    }
	    	} catch(NullPointerException e){
	    		e.printStackTrace();
	    	} catch (Exception e){
	    		e.printStackTrace();
	    	}
		    
	    return false;
		}  
}
