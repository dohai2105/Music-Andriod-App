package com.freecompany.chiasenhac;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PlayVideoActvity extends Activity  implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {
	public static  String URL = null;
    SurfaceView videoSurface;
    MediaPlayer player;
    VideoControllerView controller;
	private String TYPE_FOLDER = "m4a";
	private String TYPE = "mp4";
	private String streamURL_part1;
	private String streamURL_part2;
	boolean flag = false;
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playvideo);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		initUI();
		initCONTROL();
		new GetVideoStreamTask().execute();
    }
	
	private void initCONTROL() {
		// TODO Auto-generated method stub
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		player.setOnPreparedListener(this);
		player.setOnCompletionListener(new OnCompletionListener() {
			
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				if (player != null) {
					player.start();
				}
			}
		});
		player.setOnErrorListener(new OnErrorListener() {
			
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				switch (what) {
				/*case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
					Toast.makeText(PlayVideoActvity.this, "video lỗi" + extra,Toast.LENGTH_SHORT).show();
					player.pause();
					break;
				case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
					Toast.makeText(PlayVideoActvity.this, "video lỗi" + extra,Toast.LENGTH_SHORT).show();
					player.release();
					break;*/
				case MediaPlayer.MEDIA_ERROR_UNKNOWN:
					Toast.makeText(PlayVideoActvity.this, "video die - code :" + extra,Toast.LENGTH_SHORT).show();
					controller.hide();
					player.stop();
					player.release();
					player=null;
					finish();
					break;
				}
				return false;
			}
		});
		URL = getIntent().getStringExtra("url");
	}

	private void initUI() {
		// TODO Auto-generated method stub
		videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
		progressBar = (ProgressBar) findViewById(R.id.videoProgress);
		SurfaceHolder videoHolder = videoSurface.getHolder();
		videoHolder.addCallback(this);
		getActionBar().hide();
		player = new MediaPlayer();
		controller = new VideoControllerView(this);
	}

	public class GetVideoStreamTask extends AsyncTask<Void, Void, String> {

		private Pattern pattern;
		private Matcher matcher;
		private String streamURL;
		@Override
		protected String doInBackground(Void... params) {
			try {
				Document doc = Jsoup.connect(URL).get();
				Element streamE = doc.select("object").first();
				if (streamE!=null) {
				Element embedE = streamE.select("embed").first();
				String flashvarsURL = embedE.attr("flashvars");
				streamURL = "";
				pattern = Pattern.compile("file=(.*?)&type");
				matcher = pattern.matcher(flashvarsURL);
				if (matcher.find()) {
					streamURL = matcher.group(1);
				}
				streamURL = URLDecoder.decode(URLDecoder.decode(streamURL, "utf-8"), "utf-8");
				String[] str = streamURL.split("128");
				streamURL_part1 = str[0];
				streamURL_part2 = str[1].substring(0, str[1].length() - 3);
				streamURL = streamURL_part1 + TYPE_FOLDER + streamURL_part2 +TYPE;
				return streamURL;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "";
		}
		@Override
		protected void onPostExecute(String result) {
			Log.d("video", result);
			// mot app xem video to lam truoc do , to copy paste thoi cung ko kho lam .
			playVideo(result);
			URL = result;
			flag = true;
		}
	}
	public void playVideo(String URL){
		if (player != null) {

			player.reset();
			try {
				player.setDataSource(URL);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			player.prepareAsync();
		}
	}
	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (player != null) {
			controller.hide();
			player.stop();
			player.release();
		}
	}
	 @Override
	    public boolean onTouchEvent(MotionEvent event) {
	        controller.show();
	        return false;
	    }

	    // Implement SurfaceHolder.Callback
	    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	    }

	    public void surfaceCreated(SurfaceHolder holder) {
	    	if (player!=null) {
	    		player.setDisplay(holder);
		    	player.start();
			}
	    }

	    public void surfaceDestroyed(SurfaceHolder holder) {
	    	if (player!=null) {
	    		player.pause();
			}
	    	 
	    }
	    // End SurfaceHolder.Callback

	    // Implement MediaPlayer.OnPreparedListener
	    public void onPrepared(MediaPlayer mp) {
	    	if (mp!=null) {
	        controller.setMediaPlayer(this);
	        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
	        controller.updateInfo(getIntent().getStringExtra("singer"), getIntent().getStringExtra("title"));
	        player.start();
	        progressBar.setVisibility(View.GONE);
	    	}
	    }
	    // End MediaPlayer.OnPreparedListener

	    // Implement VideoMediaController.MediaPlayerControl
	    public boolean canPause() {
	        return true;
	    }

	    public boolean canSeekBackward() {
	        return true;
	    }

	    public boolean canSeekForward() {
	        return true;
	    }

	    public int getBufferPercentage() {
	        return 0;
	    }

	    public int getCurrentPosition() {
	        return player.getCurrentPosition();
	    }

	    public int getDuration() {
	        return player.getDuration();
	    }

	    public boolean isPlaying() {
	        return player.isPlaying();
	    }

	    public void pause() {
	        player.pause();
	    }

	    public void seekTo(int i) {
	        player.seekTo(i);
	    }
	    public void start() {
	        player.start();
	    }

	    public boolean isFullScreen() {
	        return false;
	    }

	    public void toggleFullScreen() {
	        
	    }
	    // End VideoMediaController.MediaPlayerControl


 
	public void spinnerSelect(int position) {
		// TODO Auto-generated method stub
		if (position==4&&getIntent().getStringExtra("type").equalsIgnoreCase("HD 1080p")) {
			TYPE_FOLDER = "flac";
			String URL = streamURL_part1 + TYPE_FOLDER + streamURL_part2 +TYPE;
			playVideo(URL);
		}else if (position==3) {
			TYPE_FOLDER = "m4a";
			String URL = streamURL_part1 + TYPE_FOLDER + streamURL_part2 +TYPE;
			playVideo(URL);
		}else if (position==2) {
			TYPE_FOLDER = "320";
			String URL = streamURL_part1 + TYPE_FOLDER + streamURL_part2 +TYPE;
			playVideo(URL);
		}else if (position==1) {
			TYPE_FOLDER = "128";
			String URL = streamURL_part1 + TYPE_FOLDER + streamURL_part2 +TYPE;
			playVideo(URL);
		}
	}
}
