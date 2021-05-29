package com.freecompany.chiasenhac;

import com.freecompany.chiasenhac.service.MediaService;
import com.freecompany.chiasenhac.service.MediaService.MyLocalBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class StopServiceActivity extends Activity {
	private ServiceConnection conn= new ServiceConnection() {
		
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			MyLocalBinder localBinder = (MyLocalBinder) service;
			MediaService mediaService= localBinder.getService();
			mediaService.StopALll();
			finish();
		}
	};
	protected void onStop() {
		super.onStop();
		unbindService(conn);
	};

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stopservice);
		Intent intent = new Intent(this, MediaService.class);
		bindService(intent, conn, BIND_AUTO_CREATE);
	};
}
