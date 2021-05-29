package com.freecompany.chiasenhac.service;

import java.io.IOException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.freecompany.chiasenhac.PlaySongActivity;
import com.freecompany.chiasenhac.R;
import com.freecompany.chiasenhac.StopServiceActivity;
import com.freecompany.chiasenhac.model.Song;

public class MediaService extends Service implements OnPreparedListener, OnErrorListener, OnBufferingUpdateListener, OnSeekCompleteListener, OnInfoListener {
	private static final int NOTIFICATION_ID = 2105;
	private final IBinder myBinder = new MyLocalBinder();
	private MediaPlayer mediaPlayer;
	Handler mainHandler = new Handler();
	protected boolean releaseThread=false;
	private String songname = "";
	private String type = "";
	private Song songToPlay;
	private int songPositon;
	private String saveType;
	private boolean isPausedInCall = false;
	private PhoneStateListener phoneStateListener;
	private TelephonyManager telephonyManager;
	
	//private Equalizer mEqualizer;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return myBinder;
	}

	public class MyLocalBinder extends Binder {
		public MediaService getService() {
			return MediaService.this;
		}
	}
	public void releaseThread(){
		releaseThread=true;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnErrorListener(this);
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnSeekCompleteListener(this);
		mediaPlayer.setOnInfoListener(this);
		mediaPlayer.reset();
		MThread mThread = new MThread();
		mThread.start();
		PauseWhenCall();
	}
 
	public void PauseWhenCall(){
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		phoneStateListener = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				// String stateString = "N/A";
				switch (state) {
				case TelephonyManager.CALL_STATE_OFFHOOK:
				case TelephonyManager.CALL_STATE_RINGING:
					if (mediaPlayer != null) {
						pauseMedia();
						//isPausedInCall = true;
					}
					break;
				//case TelephonyManager.CALL_STATE_IDLE:
					// Phone idle. Start playing.
					/*if (mediaPlayer != null) {
						if (isPausedInCall) {
							isPausedInCall = false;
							playMedia();
						}
					}
					break;*/
				}

			}
		};
		telephonyManager.listen(phoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
	/*	if (intent != null) {
			if (intent.getStringExtra("Notification") != null) {
				if (intent.getStringExtra("Notification").equals("pause")) {
					if (mediaPlayer.isPlaying()) {
						pauseMedia();
						cancelNotification();
					}
				}
			}
		}*/
		return Service.START_NOT_STICKY;
	}
	
	public class MThread extends Thread {
		public void run() {
			while (true) {
				if (releaseThread) {
					return;
				}
				updateMediaControl();	
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	};
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d("dohai", "service__________________destroy");
		releaseThread();
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
		 	mediaPlayer.release();
		}
		//mEqualizer.release();
		cancelNotification();
		super.onDestroy();
	}
	protected void updateMediaControl() {
		// TODO Auto-generated method stub
		if (mediaPlayer != null) {

			if (mediaPlayer.isPlaying()) {
					long songTotalDuration = mediaPlayer.getDuration();
					long songCurrentDuration = mediaPlayer.getCurrentPosition();
					String seekBarProgress = mediaPlayer.getCurrentPosition()+"";
					String seekBarMax = mediaPlayer.getDuration()+"";
					Intent intent = new Intent("com.example.chiasenhac.intentBroadcast");
					intent.putExtra("songTotalDuration", milliSecondsToTimer(songTotalDuration));
					intent.putExtra("songCurrentDuration",  milliSecondsToTimer(songCurrentDuration));
					intent.putExtra("seekBarProgress", seekBarProgress);
					intent.putExtra("seekBarMax", seekBarMax);
					sendBroadcast(intent);
			}
		}
	//		}
	//	});
	}
	
	
	 public String milliSecondsToTimer(long milliseconds){
	        String finalTimerString = "";
	        String secondsString = "";
	 
	        // Convert total duration into time
	           int hours = (int)( milliseconds / (1000*60*60));
	           int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
	           int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
	           // Add hours if there
	           if(hours > 0){
	               finalTimerString = hours + ":";
	           }
	 
	           // Prepending 0 to seconds if it is one digit
	           if(seconds < 10){
	               secondsString = "0" + seconds;
	           }else{
	               secondsString = "" + seconds;}
	 
	           finalTimerString = finalTimerString + minutes + ":" + secondsString;
	 
	        // return timer string
	        return finalTimerString;
	    }
	 
	
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	
	public void playMedia() {
		if (mediaPlayer != null) {
			if (!mediaPlayer.isPlaying()) {
				// initNotification(_songTitle, _singer, _songId, _songIndex);
				initNotification(songToPlay.getTitle(), songToPlay.getSinger(),songPositon);
				mediaPlayer.start();
			}
		}
	}
	
	public void stopMedia() {
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
		}
	}
	
	public void pauseMedia() {
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
			}
		}
	}
	
	public void updateSeekPos(int seekPos) {	
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.seekTo(seekPos);
			}
		}
	}
	
	public void StopALll() {
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				pauseMedia();
				cancelNotification();
			}
		}
	}
	
	public void playSong(String streamURL,Song songToPlay, int songPositon,boolean canChangeType , String saveType){
		if (!songname.equals(songToPlay.getTitle()) || !type.equals(songToPlay.getType())||canChangeType) {
			if (mediaPlayer!=null) {
			Log.d("dohaixxx", streamURL);
			this.songToPlay = songToPlay;
			this.songPositon = songPositon;
			this.saveType=saveType;
			songname = songToPlay.getTitle();
			type = songToPlay.getType();
			mediaPlayer.reset();
			try {
				mediaPlayer.setDataSource(streamURL);
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
			mediaPlayer.prepareAsync();
			Intent intent = new Intent("com.example.chiasenhac.intentBroadcast");
			intent.putExtra("songTotalDuration", "");
			intent.putExtra("songCurrentDuration", "");
			intent.putExtra("seekBarProgress", "0");
			intent.putExtra("seekBarMax", "0");
			sendBroadcast(intent);
			//mEqualizer.release();
			//initEquaLizer();
			}
		}
	}
	
	public void initNotification(String songTitle, String singer, int songPosition ) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		int icon = R.drawable.ic_mm_song;
		CharSequence tickerText = (CharSequence)songTitle;
		long when = System.currentTimeMillis();
		// Notification notification = new Notification(icon, tickerText, when);
		// Context context = getApplicationContext();
		// notification.flags = Notification.FLAG_ONGOING_EVENT;
		Intent dismissIntent = new Intent(this,StopServiceActivity.class);
		PendingIntent piDismiss = PendingIntent.getActivity(this, 0, dismissIntent, 0);
		CharSequence contentTitle = (CharSequence)songTitle;
		CharSequence contentText = (CharSequence)singer;
		Bitmap remote_picture = BitmapFactory.decodeResource(getResources(), R.drawable.logo_notification);
		Intent notificationIntent = new Intent(this, PlaySongActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		notificationIntent.putExtra("songPosition", songPosition);
		notificationIntent.putExtra("saveType", saveType);
		notificationIntent.putExtra("flagNotification", "fromNotification");
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this);
		builder.setContentIntent(contentIntent).setLargeIcon(remote_picture).setSmallIcon(icon)
				.setTicker(tickerText).setWhen(when).setAutoCancel(true)
				.setContentTitle(contentTitle).setContentText(contentText).addAction(R.drawable.stop_service_icon, "", piDismiss);

		Notification notification = builder.build();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		//notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		mNotificationManager.notify(NOTIFICATION_ID, notification);
	}

	// Cancel Notification
	public void cancelNotification() {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		mNotificationManager.cancel(NOTIFICATION_ID);
	}

	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		playMedia();
	}
	public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}
	public void onSeekComplete(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		if (!mediaPlayer.isPlaying()) {
			playMedia();
		}
//----------------		
	}
	public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
