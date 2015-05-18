package com.station.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "station.db";
	private static final int VERSION = 1;
	// �������ݿ⣬�������ݿ⣬�������ݿⶼ�漰��session_id
	private static final String CREATE_TABLE_ANTENNA = "CREATE TABLE antenna_info(producer TEXT, model TEXT, photo_name TEXT, antenna_name TEXT, session_id TEXT,frequency TEXT,azimuth TEXT);";
	private static final String CREATE_TABLE_ERROR = "CREATE TABLE tower_error(error_type TEXT, description TEXT, session_id TEXT, photo_name_before TEXT, photo_name_after TEXT,  completion TEXT);";
	private static final String CREATE_TABLE_TOWER = "CREATE TABLE tower_info(device_id TEXT, tower_type TEXT, producer TEXT, build_date DATE, tower_address TEXT, floor_count INTEGER, height REAL, pole_count TEXT, station_name TEXT, session_id TEXT, longitude REAL, latitude REAL, session_time TIMESTAMP, photo_name TEXT);";
	private static final String GET_STATION_URL = "http://218.94.107.4:4000/GetStationByLocation";
	private static final String UPLOAD_PHOTO_URL = "http://218.94.107.4000/UploadPhoto";
	private static final String UPLOAD_SESSION_URL = "http://218.94.107.4:4000/UploadTowerInfo";

	private static SQLiteDatabase db = null;
	private static String device_id = null;
	private static LocationManager locationManager = null;
	private static LocationListener locationListener = null;
	private static int gps_status = LocationProvider.OUT_OF_SERVICE;
	private static boolean first_time = true;
	private static double longitude = 0;
	private static double latitude = 0;
	private static int lac;
	private static int ci;
	private static GsmCellLocation location;
	private static String networkoperator;
	private static String networkoperatorname;
	private static int networktype;
	private static int phonetype;

	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_TOWER);
		db.execSQL(CREATE_TABLE_ANTENNA);
		db.execSQL(CREATE_TABLE_ERROR);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public static void createInstance(Context context) {
		db = (new DatabaseHelper(context)).getWritableDatabase();
		TelephonyManager tm = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE));

		device_id = tm.getDeviceId();
		location = (GsmCellLocation) tm.getCellLocation();
		ci = location.getCid();
		lac = location.getLac();
		Log.d("lac", "��" + lac);
		Log.d("ci", "��" + ci);
		// type=((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType();
		// ����MCC+MNC���� (SIM����Ӫ�̹��Ҵ������Ӫ���������)(IMSI)
		networkoperator = tm.getNetworkOperator();
		// �����ƶ�������Ӫ�̵�����(SPN)
		networkoperatorname = tm.getNetworkOperatorName();
		/**
		 * ��ȡ��������
		 * 
		 * NETWORK_TYPE_CDMA ��������ΪCDMA NETWORK_TYPE_EDGE ��������ΪEDGE
		 * NETWORK_TYPE_EVDO_0 ��������ΪEVDO0 NETWORK_TYPE_EVDO_A ��������ΪEVDOA
		 * NETWORK_TYPE_GPRS ��������ΪGPRS NETWORK_TYPE_HSDPA ��������ΪHSDPA
		 * NETWORK_TYPE_HSPA ��������ΪHSPA NETWORK_TYPE_HSUPA ��������ΪHSUPA
		 * NETWORK_TYPE_UMTS ��������ΪUMTS
		 * 
		 * ���й�����ͨ��3GΪUMTS��HSDPA���ƶ�����ͨ��2GΪGPRS��EGDE�����ŵ�2GΪCDMA�����ŵ�3GΪEVDO
		 */
		networktype = tm.getNetworkType();
		/**
		 * �����ƶ��ն˵�����
		 * 
		 * PHONE_TYPE_CDMA �ֻ���ʽΪCDMA������ PHONE_TYPE_GSM �ֻ���ʽΪGSM���ƶ�����ͨ
		 * PHONE_TYPE_NONE �ֻ���ʽδ֪
		 */
		phonetype = tm.getPhoneType();

		/*
		 * �����Ժ�Ҫ�����ô�������С��ģ��
		 * 
		 * List<NeighboringCellInfo> infos=tm.getNeighboringCellInfo();
		 * for(NeighboringCellInfo info:infos){ //��ȡ�ھ�С���� int cid=info.getCid();
		 * //��ȡ�ھ�С��LAC��LAC:
		 * λ�������롣Ϊ��ȷ���ƶ�̨��λ�ã�ÿ��GSM/PLMN�ĸ������������ֳ����λ������LAC�����ڱ�ʶ��ͬ��λ������
		 * info.getLac(); info.getNetworkType(); info.getPsc(); //��ȡ�ھ�С���ź�ǿ��
		 * info.getRssi(); }
		 
	    List<NeighboringCellInfo> infos=tm.getNeighboringCellInfo();
	    for(NeighboringCellInfo info:infos){
	        //��ȡ�ھ�С����
	        int cid=info.getCid();
	        //��ȡ�ھ�С��LAC��LAC: λ�������롣Ϊ��ȷ���ƶ�̨��λ�ã�ÿ��GSM/PLMN�ĸ������������ֳ����λ������LAC�����ڱ�ʶ��ͬ��λ������
	        info.getLac();
	        info.getNetworkType();
	        info.getPsc();
	        //��ȡ�ھ�С���ź�ǿ��  
	        info.getRssi();
	    }
*/
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				if (location != null) {
					longitude = location.getLongitude();
					latitude = location.getLatitude();
					if (first_time) {
						first_time = false;
						gps_status = LocationProvider.AVAILABLE;
					}
				}
			}

			public void onProviderDisabled(String provider) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				gps_status = status;
			}
		};
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 0, locationListener);
	}

	public static void destroyInstance() {
		db.close();
		db = null;
		locationManager.removeUpdates(locationListener);
	}

	public static SQLiteDatabase getInstance() {
		return db;
	}

	


	
	
	
	
	
	
	
	/*
	 * public static String getDeviceId() { return device_id; }��ȡһϵ�е�������tabҳ����ʾ
	 * ������Щget����
	 */

	public static int getLac() {
		return lac;
	}

	public static int getCi() {
		return ci;
	}

	public static int getNetworkType() {
		return networktype;
	}

	public static String getDeviceId() {
		return device_id;
	}

	public static String getNetworkoperator() {
		return networkoperator;
	}

	public static String getNetworkoperatorname() {
		return networkoperatorname;
	}

	public static int getPhonetype() {
		return phonetype;
	}

	// public static int getGpsStatus() {
	// return gps_status;
	// }
	//

	public static int getGpsStatus() {
		return gps_status;
	}

	// ���ͱ�����ǰ����Ϣ������������������Ӧ��վ
	public static byte[] getStationList() {
		String lon = String.format("%.4fE", ((int) longitude) * 100
				+ (longitude - (int) longitude) * 60);
		String lat = String.format("%.4fN", ((int) latitude) * 100
				+ (latitude - (int) latitude) * 60);
		lon = "11846.3211E";
		lat = "3212.3411N";
		lac = 20979;
		ci = 39932;
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(GET_STATION_URL);
		StringBuffer buf = new StringBuffer();
		buf.append("<?xml version='1.0' encoding='utf-8' ?>");
		buf.append("<handheld_get_station id='").append(device_id).append("'>");
		buf.append("<location longitude='").append(lon).append("' latitude='")
				.append(lat).append("' lac='").append(lac).append("' ci='")
				.append(ci).append("'/>");
		buf.append("</handheld_get_station>");
		// �����system��������ʾ���Ҵ��ݹ�ȥ�����ݵģ����ֵ�ȷ��ͬһ��λ�ã���Ȼ���ò�һ����LACCI���ʼ��м�¼
		System.out.println(buf.toString());

		try {
			byte[] body = buf.toString().getBytes();
			body = Security.encrypt(body, body.length,
					"55555555555555555555555555555555".getBytes());
			post.setEntity(new ByteArrayEntity(body));
			post.setHeader("DeviceID", device_id);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				InputStream istream = response.getEntity().getContent();
				body = toByteArray(istream);
				body = Security.decrypt(body, body.length,
						"55555555555555555555555555555555".getBytes());
				if (body != null) {
					System.out.println(body.toString());
					return body;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// �ϴ�ģ��3�����ݿ�ģ��
	public static void uploadSession(String session_id) {
		Cursor tower_cursor = db.query("tower_info", new String[] {
				"device_id", "station_name", "tower_type", "build_date",
				"tower_address", "session_time" }, "session_id=?",
				new String[] { session_id }, null, null, null);
		tower_cursor.moveToFirst();
		Cursor antenna_cursor = db.query("antenna_info", new String[] {
				"antenna_name", "photo_name", "producer", "model" },
				"session_id=?", new String[] { session_id }, null, null, null);
		Cursor error_cursor = db.query("tower_error", new String[] {
				"error_type", "description", "photo_name_before",
				"photo_name_after" }, "session_id=?",
				new String[] { session_id }, null, null, null);

		ArrayList<String> photoList = new ArrayList<String>();
		String device_id = tower_cursor.getString(0);
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(UPLOAD_SESSION_URL);
		StringBuffer buf = new StringBuffer();
		buf.append("<?xml version='1.0' encoding='utf-8' ?>");
		buf.append("<tower_info device_id='").append(device_id).append("' ");
		buf.append("station_name='").append(tower_cursor.getString(1))
				.append("' ");
		buf.append("tower_type='").append(tower_cursor.getString(2))
				.append("' ");
		buf.append("build_date='").append(tower_cursor.getString(3))
				.append("' ");
		buf.append("tower_address='").append(tower_cursor.getString(4))
				.append("' ");
		buf.append("session_time='").append(tower_cursor.getString(5))
				.append("' ");
		/*
		 * buf.append("floor_count='").append(producer).append("' ");
		 * buf.append("height='").append(producer).append("' ");
		 * buf.append("pole_count='").append(producer).append("' ");
		 * buf.append("longitude='").append(producer).append("' ");
		 * buf.append("latitude='").append(producer).append("' ");
		 */
		buf.append("session_id='").append(session_id).append("'>");
		while (antenna_cursor.moveToNext()) {
			buf.append("<antenna_info antenna_name='")
					.append(antenna_cursor.getString(0)).append("' ");
			String photo_name = antenna_cursor.getString(1);
			if (photo_name != null) {
				photoList.add(photo_name);
				buf.append("photo_name='").append(photo_name).append("' ");
			}
			buf.append("producer='").append(antenna_cursor.getString(2))
					.append("' ");
			buf.append("model='").append(antenna_cursor.getString(3))
					.append("' ");
			buf.append("session_id='").append(session_id).append("'/>");
		}
		while (error_cursor.moveToNext()) {
			buf.append("<tower_error error_type='")
					.append(error_cursor.getString(0)).append("' ");
			buf.append("description='").append(error_cursor.getString(1))
					.append("' ");
			String photo_name_before = error_cursor.getString(2);
			if (photo_name_before != null) {
				photoList.add(photo_name_before);
				buf.append("photo_name_before='").append(photo_name_before)
						.append("' ");
			}
			String photo_name_after = error_cursor.getString(3);
			if (photo_name_after != null) {
				photoList.add(photo_name_after);
				buf.append("photo_name_after='").append(photo_name_after)
						.append("' ");
			}
			buf.append("session_id='").append(session_id).append("'/>");
		}
		buf.append("</tower_info>");

		try {
			byte[] body = buf.toString().getBytes();
			body = Security.encrypt(body, body.length,
					"55555555555555555555555555555555".getBytes());
			post.setEntity(new ByteArrayEntity(body));
			post.setHeader("DeviceID", device_id);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				InputStream istream = response.getEntity().getContent();
				body = toByteArray(istream);
				body = Security.decrypt(body, body.length,
						"55555555555555555555555555555555".getBytes());
				if (body != null) {
					for (String photo_name : photoList) {
						uploadPhoto(photo_name);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	
	
	
	
	
	
	
	
	// �ϴ���Ƭģ��
	private static void uploadPhoto(String name)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(UPLOAD_PHOTO_URL);
		post.setHeader("PhotoName", name);
		InputStreamEntity reqEntity = new InputStreamEntity(
				new FileInputStream(Environment.getExternalStorageDirectory()
						+ "/com.enice.station/" + name + ".jpg"), -1);
		reqEntity.setContentType("binary/octet-stream");
		reqEntity.setChunked(true);
		post.setEntity(reqEntity);
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static byte[] toByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int read = 0;
		byte[] buffer = new byte[1024];
		while (read != -1) {
			read = in.read(buffer);
			if (read != -1)
				out.write(buffer, 0, read);
		}
		out.close();
		return out.toByteArray();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	//������ˮ��ɾ�����ݿ�����Ϣ
	public static void deleteSession(String session_id) {
		db.delete("tower_error", "session_id=?", new String[] { session_id });
		db.delete("antenna_info", "session_id=?", new String[] { session_id });
		db.delete("tower_info", "session_id=?", new String[] { session_id });
	}
}
