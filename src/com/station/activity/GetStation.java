package com.station.activity;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GetStation extends ListActivity {
	private ArrayList<HashMap<String, Object>> stationList = null;

	public void onCreate(Bundle savedInstanceState) {
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_station);
		setTitle("当前获取的基站");
		parseStationInfo(DatabaseHelper.getStationList());
		Log.v("body", DatabaseHelper.getStationList().toString());
		setListAdapter(new StationListAdapter());
		// 此句代码结束后形成了按了新建任务之后的界面，包括一个LACCI来的基站和小葛给的三个随机的基站
		// Toast
		// toast=Toast.makeText(AntennaWrite.this,"别忘记上传数据哟！亲",Toast.LENGTH_SHORT);
		// toast.show();
	}

	private void parseStationInfo(byte[] body) {
		stationList = new ArrayList<HashMap<String, Object>>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(new ByteArrayInputStream(body));
			Log.v("body", body.toString());
			Element root = dom.getDocumentElement();
			if (root.getTagName().equals("station_info")) {
				NodeList stationNodeList = root.getElementsByTagName("station");
				for (int i = 0; i < stationNodeList.getLength(); i++) {
					HashMap<String, Object> stationInfo = new HashMap<String, Object>();
					NamedNodeMap stationAttr = stationNodeList.item(i)
							.getAttributes();
					stationInfo.put("name", ((Attr) stationAttr
							.getNamedItem("name")).getValue());
					NodeList antennaNodeList = stationNodeList.item(i)
							.getChildNodes();
					ArrayList<HashMap<String, Object>> antennaList = new ArrayList<HashMap<String, Object>>();
					for (int j = 0; j < antennaNodeList.getLength(); j++) {
						HashMap<String, Object> antennaInfo = new HashMap<String, Object>();
						NamedNodeMap antennaAttr = antennaNodeList.item(j)
								.getAttributes();
						antennaInfo.put("name", ((Attr) antennaAttr
								.getNamedItem("name")).getValue());
						antennaInfo.put("frequency", ((Attr) antennaAttr
								.getNamedItem("frequency")).getValue());
						antennaInfo.put("azimuth", ((Attr) antennaAttr
								.getNamedItem("azimuth")).getValue());
						antennaList.add(antennaInfo);
						// System.out.println(antennaInfo.get("name").toString());
						// System.out.println(antennaInfo.get("frequency").toString());
						// System.out.println(antennaInfo.get("azimuth").toString());
						// 可以显示具体接受包的信息
					}
					stationInfo.put("antenna", antennaList);
					stationList.add(stationInfo);
					// stationlist里面有一些解析的数据，可是然后哪？他们怎么处理提取出来那？

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
//	这个环节已经立刻把与服务器交互的内容写入了数据库
	protected void onListItemClick(ListView l, View v, int position, long id) {
		SimpleDateFormat timestampFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String device_id = DatabaseHelper.getDeviceId();
		long session_time = System.currentTimeMillis();
		// 手机的session_id是什么一个玩意?下面有解释,基本可以看做是一个流水号
		String session_id = device_id + session_time;
		SQLiteDatabase db = DatabaseHelper.getInstance();
		ContentValues cv = new ContentValues();
		cv.put("device_id", device_id);
		cv.put("session_id", session_id);
		cv.put("session_time", timestampFormat.format(session_time));
		cv.put("station_name", (String) stationList.get(position).get("name"));
		db.insert("tower_info", null, cv);
		cv.clear();
		@SuppressWarnings("unchecked")
		ArrayList<HashMap<String, Object>> antennaList = (ArrayList<HashMap<String, Object>>) stationList
				.get(position).get("antenna");
		for (HashMap<String, Object> antennaInfo : antennaList) {
			cv.put("antenna_name", (String) antennaInfo.get("name"));
			// 这里我目测就是应该加入天线信息的地方，从交互处获得信息的方式
			cv.put("frequency", (String) antennaInfo.get("frequency"));
			cv.put("azimuth", (String) antennaInfo.get("azimuth"));
			cv.put("session_id", session_id);
			db.insert("antenna_info", null, cv);
		}
		// 这里通往铁塔信息页面
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("session_id", session_id);
		startActivity(intent);
	}

	private class StationListAdapter extends BaseAdapter {
		private LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		public int getCount() {
			return stationList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		@SuppressWarnings("unchecked")
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = inflater.inflate(R.layout.task_item, null);
			TextView station_name = (TextView) view
					.findViewById(R.id.station_name);
			station_name
					.setText((String) stationList.get(position).get("name"));
			TextView extended_info = (TextView) view
					.findViewById(R.id.extended_info);
			extended_info
					.setText(((ArrayList<HashMap<String, Object>>) stationList
							.get(position).get("antenna")).size() + "根天线");
			return view;
		}

	}
	
	
	
	
	
	
	protected void onStart() { 
		super.onStart(); 
		MyApplication mApp = (MyApplication)getApplication(); 
		if (mApp.isExit()) { 
		finish(); 
		mApp.setExit(false);
		} 
		}

	
	
	
	

}
