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

//标签控件Tab
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
	
	
	
	
	
//	动态XY图
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
	
    
//    一开始采用的stacke chart
    public static final int WIDTH = 280;
	public static final int HEIGHT = 250;
	private ConfigurationView view, view1, view2, view3, view4,view5;
//    邻区信息的显示部分
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
		tabHost.addTab(tabHost.newTabSpec("biaoqian1").setIndicator("无线参数").setContent(R.id.tab1));
		tabHost.addTab(tabHost.newTabSpec("biaoqian2").setIndicator("铁塔信息").setContent(R.id.tab2));
		tabHost.addTab(tabHost.newTabSpec("biaoqian3").setIndicator("邻区信息").setContent(R.id.tab3));
        
		
		session_id = getIntent().getStringExtra("session_id");
		cv = new ContentValues();
		
		MyListener = new MyPhoneStateListener();//初始化对象   
        Tel = ( TelephonyManager )getSystemService(Context.TELEPHONY_SERVICE);//Return the handle to a system-level service by name.通过名字获得一个系统级服务   
        Tel.listen(MyListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        //Registers a listener object to receive notification of changes in specified telephony states.设置监听器监听特定事件的状态 
        
        
        
        
        
        
        
        
        
//        不像那些本区的无线参数 时间是当前生成的
        
        
        final Calendar c = Calendar.getInstance(); 
        mYear = c.get(Calendar.YEAR); //获取当前年份 
        mMonth = c.get(Calendar.MONTH);//获取当前月份 
        mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码 
		
	    Log.v("时间显示","年份是"+mYear);
        
        
        
        
        //从enteractivity得来，一进入程序便记录了一个lac，ci，目前我的设计是退出一个基站必须重新进，所以应该不会出错
        lac = DatabaseHelper.getLac();
	    ci = DatabaseHelper.getCi();
	    networktype = DatabaseHelper.getNetworkType();
	    device_id = DatabaseHelper.getDeviceId();
	    networkoperator = DatabaseHelper.getNetworkoperator();
	    DatabaseHelper.getNetworkoperatorname();
	    phonetype = DatabaseHelper.getPhonetype();

	    
	    
	    
//	    注册各个view的环节
	    TextView time_value=(TextView)findViewById(R.id.time_value);
	    time_value.setText(mYear+"年"+mMonth+"月"+mDay+"日");
	    
	    
	          
	    
	    TextView cgi_value=(TextView)findViewById(R.id.cgi_value);
	    cgi_value.setText(networkoperator+" "+lac+" "+ci);
	    
	    
	    
	    
	    TextView networkoperatorname_value=(TextView)findViewById(R.id.networkoperatorname_value);
	    networkoperatorname_value.setText(DatabaseHelper.getNetworkoperatorname());
	    //这里有疑问，为什么我用networkoperatorname为什么不可以那
	    
	    
	    TextView deviceid_value=(TextView)findViewById(R.id.deviceid_value);
	    deviceid_value.setText(device_id);
	    
	    
//	    networktype的文字化显示
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
	    
	    
//	    phonetype的文字化显示
	    TextView phonetype_value=(TextView)findViewById(R.id.phonetype_value);
	    if (phonetype == 2) {  
	    	phonetype_value.setText("CDMA");  
	    } else if (phonetype == 1) {  
	    	phonetype_value.setText("GSM");  
	    } 

	    
	    
        
	    
	    
//       下面内容用于显示动态的xy图
	    context = getApplicationContext();
        
        //这里获得main界面上的布局，下面会把图表画在这个布局里面
        LinearLayout layoutLine = (LinearLayout)findViewById(R.id.linearLayout1);
        
        //这个类用来放置曲线上的所有点，是一个点的集合，根据这些点画出曲线
        series = new XYSeries(title);
        
        //创建一个数据集的实例，这个数据集将被用来创建图表
        mDataset = new XYMultipleSeriesDataset();
        
        //将点集添加到这个数据集中
        mDataset.addSeries(series);
        
        //以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄
        int color = Color.GREEN;
        PointStyle style = PointStyle.CIRCLE;
        renderer = buildRenderer(color, style, true);
        
        //设置好图表的样式
        setChartSettings(renderer, "X", "Y", 0, 100, -120, -40, Color.WHITE, Color.WHITE);
        
        //生成图表，view
		chart = ChartFactory.getLineChartView(context, mDataset, renderer);
        
        //将图表添加到布局中去
		layoutLine.addView(chart, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        //这里的Handler实例将配合下面的Timer实例，完成定时更新图表的功能
		handler = new Handler() {
        	@Override
        	public void handleMessage(Message msg) {
        		//刷新图表
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
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
//	    tab2的内容
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
        ArrayAdapter<String> tower_type_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"未知塔型", "角钢塔", "单管塔","三管塔","拉线塔"});
        tower_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tower_type.setAdapter(tower_type_adapter);
	    
	    
	    
	    
        tower_address=(EditText)findViewById(R.id.tv_towerValue);
	    
	    
        tv_factoryValue=(EditText)findViewById(R.id.tv_factoryValue);
	    
	    
	    
	    
	    
	    
	    
//	    基站照片的获取部分
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
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    //tab3涉及的是邻区信息，其实意义也就是在于注册监听器，给所有tab中的元素统统注册监听器，oncreate的作用
    	List<NeighboringCellInfo> infos=Tel.getNeighboringCellInfo();
	    try
    	{
    	LinearLayout layoutBar = (LinearLayout) findViewById(R.id.configLayout);
        
		view = new ConfigurationView(TabDemoActivity.this, infos.get(0).getRssi(), "邻区1", true);
		layoutBar.addView(view, new LayoutParams(108, LayoutParams.FILL_PARENT));
		
		view1 = new ConfigurationView(TabDemoActivity.this, infos.get(1).getRssi(), "邻区2", true);
		layoutBar.addView(view1, new LayoutParams(108, LayoutParams.FILL_PARENT));
		
		
		view2 = new ConfigurationView(TabDemoActivity.this, infos.get(2).getRssi(), "邻区3", true);
		layoutBar.addView(view2, new LayoutParams(108, LayoutParams.FILL_PARENT));
		
		
		view3 = new ConfigurationView(TabDemoActivity.this, infos.get(3).getRssi(), "邻区4", true);
		layoutBar.addView(view3, new LayoutParams(108, LayoutParams.FILL_PARENT));
		
		
		view4 = new ConfigurationView(TabDemoActivity.this, infos.get(4).getRssi(), "邻区5", true);
		layoutBar.addView(view4, new LayoutParams(108, LayoutParams.FILL_PARENT));
		
		view5 = new ConfigurationView(TabDemoActivity.this, infos.get(5).getRssi(), "邻区6", true);
		layoutBar.addView(view5, new LayoutParams(108, LayoutParams.FILL_PARENT));
		
    	}
    	catch (Exception e) {
			// TODO: handle exception
//    		可以加入这个toast来检测信号变化
//    		Toast.makeText(TabDemoActivity.this, "你的手机不支持获取邻区信息" , Toast.LENGTH_SHORT).show();
		}

		
	}
	
	
	
	
	
	
//	生命周期三部曲
    public void onDestroy() {
    	//当结束程序时关掉Timer
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

	
	
	
	
	
//	 监听信号强度的变化监听器
	 private class MyPhoneStateListener extends PhoneStateListener{  
		//监听器类   
		        /*得到信号的强度由每个tiome供应商,有更新*/  
		        TextView signalstrenghth_value = (TextView)findViewById(R.id.signalstrenghth_value);
		        private int asu;
		        
		        @Override  
		  
		        public void onSignalStrengthsChanged(SignalStrength signalStrength){  
		  
		        super.onSignalStrengthsChanged(signalStrength);//调用超类的该方法，在网络信号变化时得到回答信号
		        asu = signalStrength.getGsmSignalStrength();
		        lastSignal=-113+2*asu;
		        /*
			    if (signalStrength.getCdmaDbm()==(-1)) {  
			        signalstrenghth_value.setText("没有CDMA信号"+"\r\n"+"GSM信号强度为"+ String.valueOf(signalStrength.getGsmSignalStrength())+"asu"+"\r\n"+"或者"+"\r\n"+lastSignal+"dBm");
			    } else{  
			    	signalstrenghth_value.setText("CDMA信号强度为"+ String.valueOf(signalStrength.getCdmaDbm())+"\r\n"+"GSM信号强度为"+ String.valueOf(signalStrength.getGsmSignalStrength())+"asu"+"\r\n"+"或者"+"\r\n"+lastSignal+"dBm"); 
			    }
			    上述代码之所以否掉是因为用这个软件的用户全部是移动号码
			    */
		    	signalstrenghth_value.setText(lastSignal+"dBm"); 
			    
		    	
		    	
//		    	下面内容用于邻区信息的更新
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
		    		   Log.v("测试邻区",infos.toString());  
		    		   }
		    		
		        }
		    	**/
		    	
//		    	关于一般的手机获取不到邻区信息的处理办法，try，catch方法
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
		    	
		    	当初给除了余师傅的手机会出现错误
		    	
		    	
		    	
		    	view.invalidate();
		    	view1.invalidate();
		    	view2.invalidate();
		    	view3.invalidate();
		    	view4.invalidate();
		    	view5.invalidate();
			    try
		    	{
		    	LinearLayout layoutBar = (LinearLayout) findViewById(R.id.configLayout);
		        
				view = new ConfigurationView(TabDemoActivity.this, infos.get(0).getRssi(), "邻区1", true);
				layoutBar.addView(view, new LayoutParams(108, LayoutParams.FILL_PARENT));
				
				view1 = new ConfigurationView(TabDemoActivity.this, infos.get(1).getRssi(), "邻区2", true);
				layoutBar.addView(view1, new LayoutParams(108, LayoutParams.FILL_PARENT));
				
				
				view2 = new ConfigurationView(TabDemoActivity.this, infos.get(2).getRssi(), "邻区3", true);
				layoutBar.addView(view2, new LayoutParams(108, LayoutParams.FILL_PARENT));
				
				
				view3 = new ConfigurationView(TabDemoActivity.this, infos.get(3).getRssi(), "邻区4", true);
				layoutBar.addView(view3, new LayoutParams(108, LayoutParams.FILL_PARENT));
				
				
				view4 = new ConfigurationView(TabDemoActivity.this, infos.get(4).getRssi(), "邻区5", true);
				layoutBar.addView(view4, new LayoutParams(108, LayoutParams.FILL_PARENT));
				
				view5 = new ConfigurationView(TabDemoActivity.this, infos.get(5).getRssi(), "邻区6", true);
				layoutBar.addView(view5, new LayoutParams(108, LayoutParams.FILL_PARENT));
				
		    	}
		    	catch (Exception e) {
					// TODO: handle exception
//		    		可以加入这个toast来检测信号变化
//		    		Toast.makeText(TabDemoActivity.this, "你的手机不支持获取邻区信息" , Toast.LENGTH_SHORT).show();
				}
		    	
		    	*/
		    	
		        }
		  
		    }
	 
	 
//	 这里用来调用了照相机拍照功能后重新返回到这个页面
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
	    
	    
	    
	    
	    
	    
//	    显示按了返回键之后的对话框
		private void showTips(){
			Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("检查到数据变化");
			alertDialog.setMessage("请选择您将进行的操作");
			alertDialog.setPositiveButton("保存并返回", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which)
				{
					//进度对话框的秒显用法
					ProgressDialog progressdialog = new ProgressDialog(TabDemoActivity.this);  

					progressdialog.setMessage("请稍候……"); 
					
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
			alertDialog.setNegativeButton("返回",new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which)
				{
					finish();
				}
			});
			
	        alertDialog.setNeutralButton("取消", new DialogInterface.OnClickListener() { 
	            
	            public void onClick(DialogInterface dialog, int which) { 
	                // TODO Auto-generated method stub  
	            	return;
	            } 
	        });
			
			
			alertDialog.create().show();;  //创建对话框
			
			}
		
			
		
		
//		按了返回键的监听器
			public boolean onKeyDown(int keyCode, KeyEvent event) {
				if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
				this.showTips();
				return false;
				}
				return false;
				}
			
			
			
			
			
//			关于动态图的功能模块
			protected XYMultipleSeriesRenderer buildRenderer(int color, PointStyle style, boolean fill) {
		    	XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		    	
		    	//设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
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
		    	//有关对图表的渲染可参看api文档
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
		    
		    
		    
		    
		    
		    
//		    更新view
		    private void updateChart() {
		    	
		    	//设置好下一个需要增加的节点
		    	addX = 0;
				addY = lastSignal;
				
				//移除数据集中旧的点集
				mDataset.removeSeries(series);
				
				//判断当前点集中到底有多少点，因为屏幕总共只能容纳100个，所以当点数超过100时，长度永远是100
//				这个作者算是考虑比较周到的了，其实也可以不管这些东西，不限制点的数量
				int length = series.getItemCount();
				if (length > 100) {
					length = 100;
				}
				
				//将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果
				for (int i = 0; i < length; i++) {
					xv[i] = (int) series.getX(i) + 1;
					yv[i] = (int) series.getY(i);
				}
				
				//点集先清空，为了做成新的点集而准备
				series.clear();
				
				//将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
				//这里可以试验一下把顺序颠倒过来是什么效果，即先运行循环体，再添加新产生的点
				series.add(addX, addY);
				for (int k = 0; k < length; k++) {
		    		series.add(xv[k], yv[k]);
		    	}
				
				//在数据集中添加新的点集
				mDataset.addSeries(series);
				
				//视图更新，没有这一步，曲线不会呈现动态
				//如果在非UI主线程中，需要调用postInvalidate()，具体参考api
				chart.invalidate();
		    }

}
