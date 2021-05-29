package com.freecompany.chiasenhac.adapter;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.freecompany.chiasenhac.R;
import com.freecompany.chiasenhac.model.Song;
import com.freecompany.chiasenhac.model.Video;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ListVideoAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<Video> videoLst;
	private Context mContext;
	private MyHolder mHolder;
	private int mPostion;
	private String TYPE_FOLDER = "m4a";
	private String TYPE = "mp4";
	private String streamURL_part1;
	private String streamURL_part2;

	public ListVideoAdapter(ArrayList<Video> videoLst, Context mContext) {
		super();
		this.videoLst = videoLst;
		this.mContext = mContext;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		streamURL_part1 ="";
		streamURL_part2 ="";
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return videoLst.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	} 

	class MyHolder {
		TextView videoTitle;
		TextView singerName;
		TextView videoType;
		ImageView videoImg;
		ImageView videodownload;
		
		public MyHolder(View v) {
			videoTitle = (TextView) v.findViewById(R.id.videoTitle);
			singerName = (TextView) v.findViewById(R.id.singerName);
			videoType = (TextView) v.findViewById(R.id.videoType);
			videoImg = (ImageView) v.findViewById(R.id.videoImg);
			videodownload = (ImageView) v.findViewById(R.id.videodownload);
		}
	}

	public View getView(final int positon, View converView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = converView;
		mHolder = null;
		if (view == null) {
			view = inflater.inflate(R.layout.adapter_itemvideo, parent, false);
			mHolder = new MyHolder(view);
			view.setTag(mHolder);
		} else {
			mHolder = (MyHolder) view.getTag();
		}
		mHolder.videoTitle.setText(videoLst.get(positon).getTitle());
		mHolder.singerName.setText(videoLst.get(positon).getSinger());
		mHolder.videoType.setText(videoLst.get(positon).getType());
		ImageLoader iml = ImageLoader.getInstance();
		iml.displayImage(videoLst.get(positon).getImgurl(), mHolder.videoImg,getObtion());
		mHolder.videodownload.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPostion=positon;
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("Bạn muốn down video : "+videoLst.get(mPostion).getTitle() + " - 720p");
				builder.setPositiveButton("OK",new android.content.DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0,
									int arg1) {
								// TODO Auto-generated method stub
								new GetVideoStreamTask().execute();
							}
						});
				builder.setNegativeButton("No",new android.content.DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
				builder.show();
			}
		}); 
		return view;
	}
	
	private DisplayImageOptions getObtion() {
		// TODO Auto-generated method stub
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true) .build();
		return options;
	}
	
	public class GetVideoStreamTask extends AsyncTask<Void, Void, String> {

		private Pattern pattern;
		private Matcher matcher;
		private String streamURL;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			streamURL="";
		}
		
		@Override
		protected String doInBackground(Void... params) {
			try {
				Document doc = Jsoup.connect(videoLst.get(mPostion).getUrl()).get();
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
			if (!streamURL_part1.equals("")&&!streamURL_part2.equals("")) {
				Video video = videoLst.get(mPostion);
				DownloadSong(video.getTitle(), video.getType(), streamURL);
				Toast.makeText(mContext,"down" + mPostion,Toast.LENGTH_SHORT).show();
				Log.d("dohai", streamURL);
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
			String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
			URL = Uri.encode(URL, ALLOWED_URI_CHARS);
			Log.d("dohai", URL);
			Request request = new Request(Uri.parse(URL));
             
            request.setAllowedOverRoaming(false);
            
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
