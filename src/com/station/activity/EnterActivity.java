package com.station.activity;


import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;









//这个是总的程序的入口，非常的重要
public class EnterActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_activity);
        //在程序一开始的过程的时候就建立出一个数据库
        DatabaseHelper.createInstance(this);
        new File(Environment.getExternalStorageDirectory() + "/com.enice.station/").mkdir();
        
        
        
        

		
		
		
		
		
		//设计一个光标 用以管理历史记录
//        Cursor cursor = db.query("tower_info", new String[]{"rowid as _id", "station_name", "session_time", "session_id"}, null, null, null, null, null);
//        startManagingCursor(cursor);
        //一下一行是我自己改的，在一个什么也看不见的东西上面显示三种数据
//        我于8月22日9:26把首页的历史记录给注释掉了，注意，也就是下面这一行而已
        
        
        
        
        
        
        ImageView enter= (ImageView) findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(EnterActivity.this, GetStation.class);
				startActivity(intent);			    
			}
		});
//     oncreate就此结束   
	}
		

	
	
	
	
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	DatabaseHelper.destroyInstance();
    }
    
    
    
	protected void onStart() { 



		super.onStart(); 



		MyApplication mApp = (MyApplication)getApplication(); 



		if (mApp.isExit()) { 



		finish();
		 



		} 



		}
    
    
    
    
}