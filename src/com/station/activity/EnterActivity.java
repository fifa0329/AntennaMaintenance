package com.station.activity;


import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;









//������ܵĳ������ڣ��ǳ�����Ҫ
public class EnterActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_activity);
        //�ڳ���һ��ʼ�Ĺ��̵�ʱ��ͽ�����һ�����ݿ�
        DatabaseHelper.createInstance(this);
        new File(Environment.getExternalStorageDirectory() + "/com.enice.station/").mkdir();
        
        
        
        

		
		
		
		
		
		//���һ����� ���Թ�����ʷ��¼
//        Cursor cursor = db.query("tower_info", new String[]{"rowid as _id", "station_name", "session_time", "session_id"}, null, null, null, null, null);
//        startManagingCursor(cursor);
        //һ��һ�������Լ��ĵģ���һ��ʲôҲ�������Ķ���������ʾ��������
//        ����8��22��9:26����ҳ����ʷ��¼��ע�͵��ˣ�ע�⣬Ҳ����������һ�ж���
        
        
        
        
        
        
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
//     oncreate�ʹ˽���   
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