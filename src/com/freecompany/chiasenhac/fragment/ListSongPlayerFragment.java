package com.freecompany.chiasenhac.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.freecompany.chiasenhac.PlaySongActivity;
import com.freecompany.chiasenhac.R;
import com.freecompany.chiasenhac.adapter.ListSongAdapter;
import com.freecompany.chiasenhac.constant.Constant;
import com.freecompany.chiasenhac.model.Song;

public class ListSongPlayerFragment extends Fragment {
	private View view;
	private ArrayList<Song> songLst;
	private ListSongAdapter adapter;
	private ListView songPlayerLv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_listsongplayer, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		songPlayerLv = (ListView)view.findViewById(R.id.listsongplayerLv);
		songLst = new ArrayList<Song>();
		songLst.addAll(Constant.playLst);
		adapter = new ListSongAdapter(songLst, getActivity());
		songPlayerLv.setAdapter(adapter);
		songPlayerLv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),PlaySongActivity.class);
				intent.putExtra("songPosition", position);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
				getActivity().finish();
			}
		});
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
	}
}
