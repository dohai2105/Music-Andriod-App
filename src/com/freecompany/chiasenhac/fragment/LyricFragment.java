package com.freecompany.chiasenhac.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freecompany.chiasenhac.R;
import com.freecompany.chiasenhac.constant.Constant;

public class LyricFragment extends Fragment {
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_lyric, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	public void getLyric(String lyric){
		TextView lyricTv = (TextView) getActivity().findViewById(R.id.lyric_content);	
		lyricTv.setText(Html.fromHtml(Constant.lyric));
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getLyric(Constant.lyric);
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
	}
}
