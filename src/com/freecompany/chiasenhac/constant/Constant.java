package com.freecompany.chiasenhac.constant;

import java.util.ArrayList;
import java.util.Arrays;

import com.freecompany.chiasenhac.model.Song;
public class Constant {
	
	public static final String REGULAR_EXPRESION = "zxy";
	public static boolean isSearchVideo =false;
	//public static String m_SaveFolder = Environment.getExternalStorageDirectory().getAbsolutePath()+"/BANHANG";
	public static ArrayList<Song> popLst = new ArrayList<Song>();
	public static ArrayList<Song> rapLst = new ArrayList<Song>();
	public static ArrayList<Song> danceLst = new ArrayList<Song>();
	public static ArrayList<Song> searchLst = new ArrayList<Song>();
	public static ArrayList<Song> playLst = new ArrayList<Song>();
	
	
	public static String lyric ="";
	public static String songImg="";
	
	public static int searchType = 0;
	public static String URL1 = "http://chiasenhac.com/mp3/vietnam/v-pop/down.html";
	public static String URL2 = "http://chiasenhac.com/mp3/vietnam/v-rap-hiphop/down.html";
	public static String URL3 = "http://chiasenhac.com/mp3/vietnam/v-dance-remix/down.html";
	
	public static void reset(){
		URL1 = "http://chiasenhac.com/mp3/vietnam/v-pop/down.html";
		URL2 = "http://chiasenhac.com/mp3/vietnam/v-rap-hiphop/down.html";
		URL3 = "http://chiasenhac.com/mp3/vietnam/v-dance-remix/down.html";
	}
	/*
	 * 1.VIETNAM
	 */
	public static String URL_VETNAM_POP = "http://chiasenhac.com/mp3/vietnam/v-pop/";
	public static String URL_VETNAM_HIPHOP = "http://chiasenhac.com/mp3/vietnam/v-rap-hiphop/";
	public static String URL_VETNAM_DANCE = "http://chiasenhac.com/mp3/vietnam/v-dance-remix/";
	/*
	 * 2.THUYNGA
	 */
	public static String URL_THUYNGA_POP = "http://chiasenhac.com/mp3/thuy-nga/tn-nhac-tre/";
	public static String URL_THUYNGA_HIPHOP = "http://chiasenhac.com/mp3/thuy-nga/tn-tru-tinh/";
	public static String URL_THUYNGA_DANCE = "http://chiasenhac.com/mp3/thuy-nga/tn-que-huong/";
	/*
	 * 3.AUMY
	 */
	public static String URL_AUMY_POP = "http://chiasenhac.com/mp3/us-uk/u-pop/";
	public static String URL_AUMY_HIPHOP = "http://chiasenhac.com/mp3/us-uk/u-rap-hiphop/";
	public static String URL_AUMY_DANCE = "http://chiasenhac.com/mp3/us-uk/u-dance-remix/";
	/*
	 *4.HOA 
	 */

	public static String URL_HOA_POP = "http://chiasenhac.com/mp3/chinese/c-pop/";
	public static String URL_HOA_HIPHOP = "http://chiasenhac.com/mp3/chinese/c-rap-hiphop/";
	public static String URL_HOA_DANCE = "http://chiasenhac.com/mp3/chinese/c-dance-remix/";
	/*
	 * 5.HAN
	 */
	public static String URL_HAN_POP = "http://chiasenhac.com/mp3/korea/k-pop/";
	public static String URL_HAN_HIPHOP = "http://chiasenhac.com/mp3/korea/k-rap-hiphop/";
	public static String URL_HAN_DANCE = "http://chiasenhac.com/mp3/korea/k-dance-remix/";
	/*
	 * 6.NUOCKHAC
	 */
	public static String URL_OTHER_POP = "http://chiasenhac.com/mp3/other/o-pop/";
	public static String URL_OTHER_HIPHOP = "http://chiasenhac.com/mp3/other/o-rap-hiphop/";
	public static String URL_OTHER_DANCE = "http://chiasenhac.com/mp3/other/o-dance-remix/";
	
	public static ArrayList<String> menuTitle = new ArrayList(Arrays.asList("","Mới nhất", "Việt Nam", "Thúy Nga","Âu Mỹ","Nhạc Hoa","Nhạc Hàn","Nước Khác","Hay nhất","Việt Nam", "Thúy Nga","Âu Mỹ","Nhạc Hoa","Nhạc Hàn","Nước Khác","Video","Việt Nam","Âu Mỹ","Hoa","Hàn","Nước Khác"));
	 
	public static String URL_VIDEO1 = "http://chiasenhac.com/hd/video/v-video/down";
	public static String URL_VIDEO2 = "http://chiasenhac.com/hd/video/v-video/new";
	
	/*
	 * Video VN
	 */
	public static String URL_VIDEO_VN1 = "http://chiasenhac.com/hd/video/v-video/down";
	public static String URL_VIDEO_VN2 = "http://chiasenhac.com/hd/video/v-video/new";
	
	/*
	 * Video AU MY
	 */
	public static String URL_VIDEO_AU1 = "http://chiasenhac.com/hd/video/u-video/down";
	public static String URL_VIDEO_AU2 = "http://chiasenhac.com/hd/video/u-video/new";
	
	/*
	 * Video AU MY
	 */
	public static String URL_VIDEO_HOA1 = "http://chiasenhac.com/hd/video/c-video/down";
	public static String URL_VIDEO_HOA2 = "http://chiasenhac.com/hd/video/c-video/new";
	
	/*
	 * Video HAN
	 */
	public static String URL_VIDEO_HAN1 = "http://chiasenhac.com/hd/video/k-video/down";
	public static String URL_VIDEO_HAN2 = "http://chiasenhac.com/hd/video/k-video/new";
	
	/*
	 * Video OTHER
	 */
	public static String URL_VIDEO_OTHER1 = "http://chiasenhac.com/hd/video/o-video/down";
	public static String URL_VIDEO_OTHER2 = "http://chiasenhac.com/hd/video/o-video/new";
	 
 
}
