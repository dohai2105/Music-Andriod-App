package com.freecompany.chiasenhac.adapter;

import java.util.ArrayList;

import com.freecompany.chiasenhac.R;
import com.freecompany.chiasenhac.model.Video;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListVideoSearchAdapter extends BaseAdapter {
	LayoutInflater inflater;
	ArrayList<Video> videoLst;
	Context mContext;
	private MyHolder mHolder;

	public ListVideoSearchAdapter(ArrayList<Video> videoLst, Context mContext) {
		super();
		this.videoLst = videoLst;
		this.mContext = mContext;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

		public MyHolder(View v) {
			videoTitle = (TextView) v.findViewById(R.id.videoTitle);
			singerName = (TextView) v.findViewById(R.id.singerName);
			videoType = (TextView) v.findViewById(R.id.videoType);
			videoImg = (ImageView) v.findViewById(R.id.videoImg);
		}
	}

	public View getView(int positon, View converView, ViewGroup parent) {
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
		mHolder.videoImg.setVisibility(View.GONE);
		return view;
	}
}
