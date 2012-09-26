package com.station.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

//��ǩ�ؼ�Tab
public class TabDemoActivity extends TabActivity {

	
	protected static final int TAKE_PICTURE = 0;
	private int lac;
	private int ci;
	private int networktype;
	private String networkoperator;
	private String device_id;
	private int phonetype;
	private MyPhoneStateListener MyListener;
	private TelephonyManager Tel;
	List <GsmCell> gsmCells=new ArrayList<GsmCell>();
	StringBuffer tab3string=new StringBuffer();
    private int mYear; 
    private int mMonth; 
    private int mDay; 
    private ImageView imageView=null;
    private Bitmap bm;
    private String tower_id;
    private Spinner tower_type;
    private EditText build_date;
    private EditText tower_address;
    private ContentValues cv = null;
    private String session_id=null;
	private EditText tv_factoryValue=null;
	
	
	
	
	
//	��̬XYͼ
	private Timer timer = new Timer();
    private TimerTask task;
    private Handler handler;
    private String title = "Signal Strength";
    private XYSeries series;
    private XYMultipleSeriesDataset mDataset;
    private GraphicalView chart;
    private XYMultipleSeriesRenderer renderer;
    private Context context;
    private int addX = -1, addY;
    private int lastSignal;
	
    
//    һ��ʼ���õ�stacke chart
    public static final int WIDTH = 280;
	public static final int HEIGHT = 250;
	private ConfigurationView view, view1, view2, view3, view4,view5;
//    ������Ϣ����ʾ����
	TextView lac1;
	TextView ci1;
	TextView strength1;
	TextView lac2;
	TextView ci2;
	TextView strength2;
	TextView lac3;
	TextView ci3;
	TextView strength3;
	TextView lac4;
	TextView ci4;
	TextView strength4;
	TextView lac5;
	TextView ci5;
	TextView strength5;
	TextView lac6;
	TextView ci6;
	TextView strength6;
	

    
    int[] xv = new int[100];
	int[] yv = new int[100];
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TabHost tabHost=getTabHost();
		LayoutInflater.from(this).inflate(R.layout.tab, tabHost.getTabContentView(),true);
		tabHost.addTab(tabHost.newTabSpec("biaoqian1").setIndicator("���߲���").setContent(R.id.tab1));
		tabHost.addTab(tabHost.newTabSpec("biaoqian2").setIndicator("������Ϣ").setContent(R.id.tab2));
		tabHost.addTab(tabHost.newTabSpec("biaoqian3").setIndicator("������Ϣ").setContent(R.id.tab3));
        
		
		session_id = getIntent().getStringExtra("session_id");
		cv = new ContentValues();
		
		MyListener = new MyPhoneStateListener();//��ʼ������   
        Tel = ( TelephonyManager )getSystemService(Context.TELEPHONY_SERVICE);//Return the handle to a system-level service by name.ͨ�����ֻ��һ��ϵͳ������   
        Tel.listen(MyListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        //Registers a listener object to receive notification of changes in specified telephony states.���ü����������ض��¼���״̬ 
        
        
        
        
        
        
        
        
        
//        ������Щ���������߲��� ʱ���ǵ�ǰ���ɵ�
        
        
        final Calendar c = Calendar.getInstance(); 
        mYear = c.get(Calendar.YEAR); //��ȡ��ǰ��� 
        mMonth = c.get(Calendar.MONTH);//��ȡ��ǰ�·� 
        mDay = c.get(Calendar.DAY_OF_MONTH);//��ȡ��ǰ�·ݵ����ں��� 
		
	    Log.v("ʱ����ʾ","�����"+mYear);
        
        
        
        
        //��enteractivity������һ���������¼��һ��lac��ci��Ŀǰ�ҵ�������˳�һ����վ�������½�������Ӧ�ò������
        lac = DatabaseHelper.getLac();
	    ci = DatabaseHelper.getCi();
	    networktype = DatabaseHelper.getNetworkType();
	    device_id = DatabaseHelper.getDeviceId();
	    networkoperator = DatabaseHelper.getNetworkoperator();
	    DatabaseHelper.getNetworkoperatorname();
	    phonetype = DatabaseHelper.getPhonetype();

	    
	    
	    
//	    ע�����view�Ļ���
	    TextView time_value=(TextView)findViewById(R.id.time_value);
	    time_value.setText(mYear+"��"+mMonth+"��"+mDay+"��");
	    
	    
	          
	    
	    TextView cgi_value=(TextView)findViewById(R.id.cgi_value);
	    cgi_value.setText(networkoperator+" "+lac+" "+ci);
	    
	    
	    
	    
	    TextView networkoperatorname_value=(TextView)findViewById(R.id.networkoperatorname_value);
	    networkoperatorname_value.setText(DatabaseHelper.getNetworkoperatorname());
	    //���������ʣ�Ϊʲô����networkoperatornameΪʲô��������
	    
	    
	    TextView deviceid_value=(TextView)findViewById(R.id.deviceid_value);
	    deviceid_value.setText(device_id);
	    
	    
//	    networktype�����ֻ���ʾ
	    TextView networktype_value=(TextView)findViewById(R.id.networktype_value);
	    
	    if (networktype == TelephonyManager.NETWORK_TYPE_1xRTT) {  
	    	networktype_value.setText("1xRTT");  
	    } else if (networktype == TelephonyManager.NETWORK_TYPE_CDMA) {  
	    	networktype_value.setText("CDMA");  
	    } else if (networktype == TelephonyManager.NETWORK_TYPE_EDGE) {  
	    	networktype_value.setText("EDGE");  
	    } 
	    else if (networktype == TelephonyManager.NETWORK_TYPE_EVDO_0) {  
	    	networktype_value.setText("EVD0_0");  
	    } else if (networktype == TelephonyManager.NETWORK_TYPE_EVDO_A) {  
	    	networktype_value.setText("EVD0_A");  
	    }  
	    else if (networktype == TelephonyManager.NETWORK_TYPE_GPRS) {  
	    	networktype_value.setText("GPRS");  
	    } else if (networktype == TelephonyManager.NETWORK_TYPE_HSDPA) {  
	    	networktype_value.setText("HSDPA");  
	    } else if (networktype == TelephonyManager.NETWORK_TYPE_HSPA) {  
	    	networktype_value.setText("HSPA");  
	    } 	    
	    else if (networktype == TelephonyManager.NETWORK_TYPE_HSUPA) {  
	    	networktype_value.setText("HSUPA");  
	    } 
	    else if (networktype == TelephonyManager.NETWORK_TYPE_IDEN) {  
	    	networktype_value.setText("IDEN");  
	    }  
	    else if (networktype == TelephonyManager.NETWORK_TYPE_UMTS) {  
	    	networktype_value.setText("UMTS");  
	    }
	    
	    
//	    phonetype�����ֻ���ʾ
	    TextView phonetype_value=(TextView)findViewById(R.id.phonetype_value);
	    if (phonetype == 2) {  
	    	phonetype_value.setText("CDMA");  
	    } else if (phonetype == 1) {  
	    	phonetype_value.setText("GSM");  
	    } 

	    
	    
        
	    
	    
//       ��������������ʾ��̬��xyͼ
	    context = getApplicationContext();
        
        //������main�����ϵĲ��֣�������ͼ���������������
        LinearLayout layoutLine = (LinearLayout)findViewById(R.id.linearLayout1);
        
        //������������������ϵ����е㣬��һ����ļ��ϣ�������Щ�㻭������
        series = new XYSeries(title);
        
        //����һ�����ݼ���ʵ����������ݼ�������������ͼ��
        mDataset = new XYMultipleSeriesDataset();
        
        //���㼯��ӵ�������ݼ���
        mDataset.addSeries(series);
        
        //���¶������ߵ���ʽ�����Եȵȵ����ã�renderer�൱��һ��������ͼ������Ⱦ�ľ��
        int color = Color.GREEN;
        PointStyle style = PointStyle.CIRCLE;
        renderer = buildRenderer(color, style, true);
        
        //���ú�ͼ�����ʽ
        setChartSettings(renderer, "X", "Y", 0, 100, -120, -40, Color.WHITE, Color.WHITE);
        
        //����ͼ��view
		chart = ChartFactory.getLineChartView(context, mDataset, renderer);
        
        //��ͼ����ӵ�������ȥ
		layoutLine.addView(chart, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        //�����Handlerʵ������������Timerʵ������ɶ�ʱ����ͼ��Ĺ���
		handler = new Handler() {
        	@Override
        	public void handleMessage(Message msg) {
        		//ˢ��ͼ��
        		updateChart();
        		super.handleMessage(msg);
        	}
        };
        
        task = new TimerTask() {
        	@Override
        	public void run() {
        		Message message = new Message();
        	    message.what = 1;
        	    handler.sendMessage(message);
        	}
        };
        
        timer.schedule(task, 500, 500);
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
//	    tab2������
        build_date = (EditText) this.findViewById(R.id.tv_towerValue);
        build_date.setInputType(InputType.TYPE_NULL);
        build_date.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				final Calendar c = Calendar.getInstance();
				new DatePickerDialog(TabDemoActivity.this, new OnDateSetListener(){
					public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						c.set(Calendar.YEAR, arg1);
						c.set(Calendar.MONTH, arg2);
						c.set(Calendar.DAY_OF_MONTH, arg3);
						build_date.setText(dateFormat.format(c.getTime()));
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
        
        
        
        tower_type = (Spinner) findViewById(R.id.tv_towerTypeValue);
        ArrayAdapter<String> tower_type_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"δ֪����", "�Ǹ���", "������","������","������"});
        tower_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tower_type.setAdapter(tower_type_adapter);
	    
	    
	    
	    
        tower_address=(EditText)findViewById(R.id.tv_towerValue);
	    
	    
        tv_factoryValue=(EditText)findViewById(R.id.tv_factoryValue);
	    
	    
	    
	    
	    
	    
	    
//	    ��վ��Ƭ�Ļ�ȡ����
	    imageView=(ImageView) findViewById(R.id.imageView);
	    imageView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
		    	tower_id = session_id + System.currentTimeMillis();
			    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			    File photo = new File(Environment.getExternalStorageDirectory() + "/com.enice.station/",  tower_id + ".jpg");
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
			    startActivityForResult(intent, TAKE_PICTURE);
			    }
			    
			});
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    //tab3�漰����������Ϣ����ʵ����Ҳ��������ע���������������tab�е�Ԫ��ͳͳע���������oncreate������
    	List<NeighboringCellInfo> infos=Tel.getNeighboringCellInfo();
	    try
    	{
    	LinearLayout layoutBar = (LinearLayout) findViewById(R.id.configLayout);
        
		view = new ConfigurationView(TabDemoActivity.this, infos.get(0).getRssi(), "����1", true);
		layoutBar.addView(view, new LayoutParams(108, LayoutParams.FILL_PARENT));
		
		view1 = new ConfigurationView(TabDemoActivity.this, infos.get(1).getRssi(), "����2", true);
		layoutBar.addView(view1, new LayoutParams(108, LayoutParams.FILL_PARENT));
		
		
		view2 = new ConfigurationView(TabDemoActivity.this, infos.get(2).getRssi(), "����3", true);
		layoutBar.addView(view2, new LayoutParams(108, LayoutParams.FILL_PARENT));
		
		
		view3 = new ConfigurationView(TabDemoActivity.this, infos.get(3).getRssi(), "����4", true);
		layoutBar.addView(view3, new LayoutParams(108, LayoutParams.FILL_PARENT));
		
		
		view4 = new ConfigurationView(TabDemoActivity.this, infos.get(4).getRssi(), "����5", true);
		layoutBar.addView(view4, new LayoutParams(108, LayoutParams.FILL_PARENT));
		
		view5 = new ConfigurationView(TabDemoActivity.this, infos.get(5).getRssi(), "����6", true);
		layoutBar.addView(view5, new LayoutParams(108, LayoutParams.FILL_PARENT));
		
    	}
    	catch (Exception e) {
			// TODO: handle exception
//    		���Լ������toast������źű仯
//    		Toast.makeText(TabDemoActivity.this, "����ֻ���֧�ֻ�ȡ������Ϣ" , Toast.LENGTH_SHORT).show();
		}

		
	}
	
	
	
	
	
	
//	��������������
    public void onDestroy() {
    	//����������ʱ�ص�Timer
    	timer.cancel();
    	super.onDestroy();
    }
	protected void onPause() {  
        // TODO Auto-generated method stub   
        super.onPause();  
        Tel.listen(MyListener, PhoneStateListener.LISTEN_NONE);  
    }  
    @Override  
    protected void onResume() {  
        // TODO Auto-generated method stub   
        super.onResume();  
        Tel.listen(MyListener,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);  
    }  

	
	
	
	
	
//	 �����ź�ǿ�ȵı仯������
	 private class MyPhoneStateListener extends PhoneStateListener{  
		//��������   
		        /*�õ��źŵ�ǿ����ÿ��tiome��Ӧ��,�и���*/  
		        TextView signalstrenghth_value = (TextView)findViewById(R.id.signalstrenghth_value);
		        private int asu;
		        
		        @Override  
		  
		        public void onSignalStrengthsChanged(SignalStrength signalStrength){  
		  
		        super.onSignalStrengthsChanged(signalStrength);//���ó���ĸ÷������������źű仯ʱ�õ��ش��ź�
		        asu = signalStrength.getGsmSignalStrength();
		        lastSignal=-113+2*asu;
		        /*
			    if (signalStrength.getCdmaDbm()==(-1)) {  
			        signalstrenghth_value.setText("û��CDMA�ź�"+"\r\n"+"GSM�ź�ǿ��Ϊ"+ String.valueOf(signalStrength.getGsmSignalStrength())+"asu"+"\r\n"+"����"+"\r\n"+lastSignal+"dBm");
			    } else{  
			    	signalstrenghth_value.setText("CDMA�ź�ǿ��Ϊ"+ String.valueOf(signalStrength.getCdmaDbm())+"\r\n"+"GSM�ź�ǿ��Ϊ"+ String.valueOf(signalStrength.getGsmSignalStrength())+"asu"+"\r\n"+"����"+"\r\n"+lastSignal+"dBm"); 
			    }
			    ��������֮���Է������Ϊ�����������û�ȫ�����ƶ�����
			    */
		    	signalstrenghth_value.setText(lastSignal+"dBm"); 
			    
		    	
		    	
//		    	������������������Ϣ�ĸ���
		    	List<NeighboringCellInfo> infos=Tel.getNeighboringCellInfo();
		    	/**
		    	for (int i = 0; i < 8; i++) {  
		    		 try {  
		    			 NeighboringCellInfo thisCell = infos.get(i);  
		    		     int thisNeighCID = thisCell.getCid();  
		    		     int thisNeighLAC = thisCell.getLac();
		    		     int thisNeighRSSI = thisCell.getRssi();
		    		 } 
		    		 catch (NumberFormatException e) {  
		    		   e.printStackTrace();
		    		   Log.v("��������",infos.toString());  
		    		   }
		    		
		        }
		    	**/
		    	
//		    	����һ����ֻ���ȡ����������Ϣ�Ĵ���취��try��catch����
		    	try{
		    	
		    	lac1=(TextView)findViewById(R.id.lac1);
		    	ci1=(TextView)findViewById(R.id.ci1);
		    	strength1=(TextView)findViewById(R.id.strength1);
		    	lac1.setText(""+infos.get(0).getLac());
		    	ci1.setText(""+infos.get(0).getCid());
		    	strength1.setText(-113+(2*infos.get(0).getRssi())+"dbm");
		    	}
		    	catch (java.lang.IndexOutOfBoundsException e) {
					// TODO: handle exception
				}

		    	
		    	try{
		    	lac2=(TextView)findViewById(R.id.lac2);
		    	ci2=(TextView)findViewById(R.id.ci2);
		    	strength2=(TextView)findViewById(R.id.strength2);
		    	lac2.setText(""+infos.get(1).getLac());
		    	ci2.setText(""+infos.get(1).getCid());
		    	strength2.setText(-113+(2*infos.get(1).getRssi())+"dbm");
		    	}
		    	catch (java.lang.IndexOutOfBoundsException e) {
					// TODO: handle exception
				}
		        
		    	try{
		    	lac3=(TextView)findViewById(R.id.lac3);
		    	ci3=(TextView)findViewById(R.id.ci3);
		    	strength3=(TextView)findViewById(R.id.strength3);
		    	lac3.setText(""+infos.get(2).getLac());
		    	ci3.setText(""+infos.get(2).getCid());
		    	strength3.setText(-113+(2*infos.get(2).getRssi())+"dbm");
		    	}
		    	catch (java.lang.IndexOutOfBoundsException e) {
					// TODO: handle exception
				}
		        
		    	try{
		    	lac4=(TextView)findViewById(R.id.lac4);
		    	ci4=(TextView)findViewById(R.id.ci4);
		    	strength4=(TextView)findViewById(R.id.strength4);
		    	lac4.setText(""+infos.get(3).getLac());
		    	ci4.setText(""+infos.get(3).getCid());
		    	strength4.setText(-113+(2*infos.get(3).getRssi())+"dbm");
		    	}
		    	catch (java.lang.IndexOutOfBoundsException e) {
					// TODO: handle exception
				}
		    	
		    	
		    	try{
		    	lac5=(TextView)findViewById(R.id.lac5);
		    	ci5=(TextView)findViewById(R.id.ci5);
		    	strength5=(TextView)findViewById(R.id.strength5);
		    	lac5.setText(""+infos.get(4).getLac());
		    	ci5.setText(""+infos.get(4).getCid());
		    	strength5.setText(-113+(2*infos.get(4).getRssi())+"dbm");
		    	}
		    	catch (java.lang.IndexOutOfBoundsException e) {
					// TODO: handle exception
				}
		    	
		    	try{
		    	lac6=(TextView)findViewById(R.id.lac6);
		    	ci6=(TextView)findViewById(R.id.ci6);
		    	strength6=(TextView)findViewById(R.id.strength6);
		    	lac6.setText(""+infos.get(5).getLac());
		    	ci6.setText(""+infos.get(5).getCid());
		    	strength6.setText(-113+(2*infos.get(5).getRssi())+"dbm");
		    	}
		    	catch (java.lang.IndexOutOfBoundsException e) {
					// TODO: handle exception
				}
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	/*
		    	
		    	������������ʦ�����ֻ�����ִ���
		    	
		    	
		    	
		    	view.invalidate();
		    	view1.invalidate();
		    	view2.invalidate();
		    	view3.invalidate();
		    	view4.invalidate();
		    	view5.invalidate();
			    try
		    	{
		    	LinearLayout layoutBar = (LinearLayout) findViewById(R.id.configLayout);
		        
				view = new ConfigurationView(TabDemoActivity.this, infos.get(0).getRssi(), "����1", true);
				layoutBar.addView(view, new LayoutParams(108, LayoutParams.FILL_PARENT));
				
				view1 = new ConfigurationView(TabDemoActivity.this, infos.get(1).getRssi(), "����2", true);
				layoutBar.addView(view1, new LayoutParams(108, LayoutParams.FILL_PARENT));
				
				
				view2 = new ConfigurationView(TabDemoActivity.this, infos.get(2).getRssi(), "����3", true);
				layoutBar.addView(view2, new LayoutParams(108, LayoutParams.FILL_PARENT));
				
				
				view3 = new ConfigurationView(TabDemoActivity.this, infos.get(3).getRssi(), "����4", true);
				layoutBar.addView(view3, new LayoutParams(108, LayoutParams.FILL_PARENT));
				
				
				view4 = new ConfigurationView(TabDemoActivity.this, infos.get(4).getRssi(), "����5", true);
				layoutBar.addView(view4, new LayoutParams(108, LayoutParams.FILL_PARENT));
				
				view5 = new ConfigurationView(TabDemoActivity.this, infos.get(5).getRssi(), "����6", true);
				layoutBar.addView(view5, new LayoutParams(108, LayoutParams.FILL_PARENT));
				
		    	}
		    	catch (Exception e) {
					// TODO: handle exception
//		    		���Լ������toast������źű仯
//		    		Toast.makeText(TabDemoActivity.this, "����ֻ���֧�ֻ�ȡ������Ϣ" , Toast.LENGTH_SHORT).show();
				}
		    	
		    	*/
		    	
		        }
		  
		    }
	 
	 
//	 ����������������������չ��ܺ����·��ص����ҳ��
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        if (resultCode == Activity.RESULT_OK && requestCode == TAKE_PICTURE) {
	        	cv.put("photo_name", tower_id);
	        	try{
	        	BitmapFactory.Options opts = new BitmapFactory.Options();
	        	opts.inSampleSize = 10 ;
	        	bm=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/com.enice.station/" + tower_id + ".jpg",opts);
	        	imageView.setImageBitmap(bm);
	        	}
	        	catch (Exception e) {
	        	    e.printStackTrace();
	        	    System.out.println(e);
	        	}
	        }

	    }
	    
	    
	    
	    
	    
	    
//	    ��ʾ���˷��ؼ�֮��ĶԻ���
		private void showTips(){
			Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("��鵽���ݱ仯");
			alertDialog.setMessage("��ѡ���������еĲ���");
			alertDialog.setPositiveButton("���沢����", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which)
				{
					//���ȶԻ���������÷�
					ProgressDialog progressdialog = new ProgressDialog(TabDemoActivity.this);  

					progressdialog.setMessage("���Ժ򡭡�"); 
					
					progressdialog.show();
					cv.put("tower_type", String.valueOf(tower_type.getSelectedItem()));
					cv.put("tower_address", tower_address.getText().toString());
					String date = build_date.getText().toString();
					cv.put("build_date", date);
					cv.put("producer", tv_factoryValue.getText().toString());
					DatabaseHelper.getInstance().update("tower_info", cv, "session_id=?", new String[]{session_id});
					finish();
				}
			});
			alertDialog.setNegativeButton("����",new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which)
				{
					finish();
				}
			});
			
	        alertDialog.setNeutralButton("ȡ��", new DialogInterface.OnClickListener() { 
	            
	            public void onClick(DialogInterface dialog, int which) { 
	                // TODO Auto-generated method stub  
	            	return;
	            } 
	        });
			
			
			alertDialog.create().show();;  //�����Ի���
			
			}
		
			
		
		
//		���˷��ؼ��ļ�����
			public boolean onKeyDown(int keyCode, KeyEvent event) {
				if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
				this.showTips();
				return false;
				}
				return false;
				}
			
			
			
			
			
//			���ڶ�̬ͼ�Ĺ���ģ��
			protected XYMultipleSeriesRenderer buildRenderer(int color, PointStyle style, boolean fill) {
		    	XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		    	
		    	//����ͼ�������߱������ʽ��������ɫ����Ĵ�С�Լ��ߵĴ�ϸ��
		    	XYSeriesRenderer r = new XYSeriesRenderer();
		    	r.setColor(color);
		    	r.setPointStyle(style);
		    	r.setFillPoints(fill);
		    	r.setLineWidth(3);
		    	renderer.addSeriesRenderer(r);
		    	
		    	return renderer;
		    }
		    
		    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String xTitle, String yTitle,
		    								double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
		    	//�йض�ͼ�����Ⱦ�ɲο�api�ĵ�
		    	renderer.setChartTitle(title);
		    	renderer.setXTitle(xTitle);
		    	renderer.setYTitle(yTitle);
		    	renderer.setXAxisMin(xMin);
		    	renderer.setXAxisMax(xMax);
		    	renderer.setYAxisMin(yMin);
		    	renderer.setYAxisMax(yMax);
		    	renderer.setAxesColor(axesColor);
		    	renderer.setLabelsColor(labelsColor);
		    	renderer.setShowGrid(true);
		    	renderer.setGridColor(Color.GREEN);
		    	renderer.setXLabels(20);
		    	renderer.setYLabels(10);
		    	renderer.setXTitle("Time");
		    	renderer.setYTitle("dBm");
		    	renderer.setYLabelsAlign(Align.RIGHT);
		    	renderer.setPointSize((float) 2);
		    	renderer.setShowLegend(false);
		    }
		    
		    
		    
		    
		    
		    
//		    ����view
		    private void updateChart() {
		    	
		    	//���ú���һ����Ҫ���ӵĽڵ�
		    	addX = 0;
				addY = lastSignal;
				
				//�Ƴ����ݼ��оɵĵ㼯
				mDataset.removeSeries(series);
				
				//�жϵ�ǰ�㼯�е����ж��ٵ㣬��Ϊ��Ļ�ܹ�ֻ������100�������Ե���������100ʱ��������Զ��100
//				����������ǿ��ǱȽ��ܵ����ˣ���ʵҲ���Բ�����Щ�����������Ƶ������
				int length = series.getItemCount();
				if (length > 100) {
					length = 100;
				}
				
				//���ɵĵ㼯��x��y����ֵȡ��������backup�У����ҽ�x��ֵ��1�������������ƽ�Ƶ�Ч��
				for (int i = 0; i < length; i++) {
					xv[i] = (int) series.getX(i) + 1;
					yv[i] = (int) series.getY(i);
				}
				
				//�㼯����գ�Ϊ�������µĵ㼯��׼��
				series.clear();
				
				//���²����ĵ����ȼ��뵽�㼯�У�Ȼ����ѭ�����н�����任���һϵ�е㶼���¼��뵽�㼯��
				//�����������һ�°�˳��ߵ�������ʲôЧ������������ѭ���壬������²����ĵ�
				series.add(addX, addY);
				for (int k = 0; k < length; k++) {
		    		series.add(xv[k], yv[k]);
		    	}
				
				//�����ݼ�������µĵ㼯
				mDataset.addSeries(series);
				
				//��ͼ���£�û����һ�������߲�����ֶ�̬
				//����ڷ�UI���߳��У���Ҫ����postInvalidate()������ο�api
				chart.invalidate();
		    }

}
