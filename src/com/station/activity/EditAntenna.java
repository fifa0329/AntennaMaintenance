package com.station.activity;

import java.io.File;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class EditAntenna extends Activity {
	private static int TAKE_PICTURE = 1001;
	private String rowid = null;
	private String session_id = null;
	private ContentValues cv = null;
	private String photo_id = null;
	private ImageView antenna_photo = null;
	private Bitmap bm;
	
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_antenna);
        setTitle("具体天线信息");
        rowid = getIntent().getStringExtra("rowid");
        cv = new ContentValues();
        
        
        
        
        
        
        
        TextView antenna_name = (TextView) findViewById(R.id.antenna_name);
        
        
        
        
        
        
        
        final Spinner antenna_producer = (Spinner) findViewById(R.id.antenna_producer);
        ArrayAdapter<String> antenna_producer_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"未知", "厂家1", "厂家2"});
        antenna_producer_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        antenna_producer.setAdapter(antenna_producer_adapter);
        
        
        
        
        
        
        
        
        
        final Spinner antenna_type = (Spinner) findViewById(R.id.antenan_type);
        ArrayAdapter<String> antenna_type_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"未知", "型号1", "型号2"});
        antenna_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        antenna_type.setAdapter(antenna_type_adapter);
        
        
        
        
        
        
        
        
        
        antenna_photo = (ImageView) findViewById(R.id.antenna_photo);
        antenna_photo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
		    	photo_id = session_id + System.currentTimeMillis();
			    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			    File photo = new File(Environment.getExternalStorageDirectory() + "/com.enice.station/",  photo_id + ".jpg");
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
			    startActivityForResult(intent, TAKE_PICTURE);
			}
		});
        
        
        
        
        
        
        
        
        
        Button finish_button = (Button) findViewById(R.id.finish_button);
        finish_button.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				cv.put("producer", String.valueOf(antenna_producer.getSelectedItem()));
				cv.put("model", String.valueOf(antenna_type.getSelectedItem()));
				DatabaseHelper.getInstance().update("antenna_info", cv, "rowid=?", new String[]{rowid});
				finish();
			}
        });
        
        
        
        
        
        
        
        Cursor cursor = DatabaseHelper.getInstance().query("antenna_info", new String[]{"session_id", "antenna_name", "producer", "model", "photo_name"}, "rowid=?", new String[]{rowid}, null, null, null);
        cursor.moveToFirst();
        session_id = cursor.getString(0);
        antenna_name.setText(cursor.getString(1));
        antenna_producer.setSelection(antenna_producer_adapter.getPosition(cursor.getString(2)));
        antenna_type.setSelection(antenna_type_adapter.getPosition(cursor.getString(3)));
        String photo_name = cursor.getString(4);
        if (photo_name != null && !photo_name.equals("")) {
        	try{
        	BitmapFactory.Options opts = new BitmapFactory.Options();
        	opts.inSampleSize = 10 ;
        	bm=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/com.enice.station/" + photo_name + ".jpg",opts);
        	antenna_photo.setImageBitmap(bm);
        	}
        	catch (Exception e) {
        	    e.printStackTrace();
        	    System.out.println(e);
        	}
//        	if (bm != null && !bm.isRecycled())
//        	{
//        		bm.recycle();
//        	}

        }
        cursor.close();
        
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == TAKE_PICTURE) {
        	cv.put("photo_name", photo_id);
        	if(data==null)
        	{
        		Log.v("测试","data是空");//根本不是靠的data来传送的数据
        	}
        	try{
        	BitmapFactory.Options opts = new BitmapFactory.Options();
        	opts.inSampleSize = 10 ;
        	bm=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/com.enice.station/" + photo_id + ".jpg",opts);
        	antenna_photo.setImageBitmap(bm);
        	}
        	catch (Exception e) {
        	    e.printStackTrace();
        	    System.out.println(e);
        	}
//        	if (bm != null && !bm.isRecycled())
//        	{
//        		bm.recycle();
//        	}

        }
    }
}
