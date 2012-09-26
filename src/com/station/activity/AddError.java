package com.station.activity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AddError extends Activity {
	
	private static int TAKE_PICTURE_BEFORE	= 1001;
	private static int TAKE_PICTURE_AFTER 	= 1002;
	private String photo_id_before;
	private String photo_id_after;
	private String errortype;
	private String session_id;
	private ContentValues cv = null;
	private Bitmap bm;
	private ImageView error_photo_before=null;
	private ImageView error_photo_after=null;
	private EditText description=null;
	private EditText completion=null;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_error);
        errortype=getIntent().getStringExtra("errortype");
        session_id = getIntent().getStringExtra("session_id");
        cv = new ContentValues();
        
    	error_photo_before=(ImageView)findViewById(R.id.error_photo_before);
    	error_photo_after=(ImageView)findViewById(R.id.error_photo_after);
    	description=(EditText)findViewById(R.id.description);
    	completion=(EditText)findViewById(R.id.completion);
    	ImageView takepictureup=(ImageView)findViewById(R.id.takepictureup);
    	ImageView takepicturedown=(ImageView)findViewById(R.id.takepicturedown);
    	
    	description.setText(errortype);
    	
    	takepictureup.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo_id_before = session_id + System.currentTimeMillis();
			    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			    File photo = new File(Environment.getExternalStorageDirectory() + "/com.enice.station/",  photo_id_before + ".jpg");
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
			    startActivityForResult(intent, TAKE_PICTURE_BEFORE);
			    }
			    
			});
    	
    	
    	takepicturedown.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
		    	photo_id_after = session_id + System.currentTimeMillis();
			    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			    File photo = new File(Environment.getExternalStorageDirectory() + "/com.enice.station/",  photo_id_after + ".jpg");
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
			    startActivityForResult(intent, TAKE_PICTURE_AFTER);
			    }
			    
			});
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	/**
    	Cursor cursor = DatabaseHelper.getInstance().query("tower_error", new String[]{"session_id", "error_type", "description", "photo_name_before", "photo_name_after"}, "rowid=?", new String[]{rowid}, null, null, null);
        cursor.moveToFirst();
        session_id = cursor.getString(0);
        error_type.setSelection(error_type_adapter.getPosition(cursor.getString(1)));
        description.setText(cursor.getString(2));
        String photo_name_before = cursor.getString(3);
        /*
         * 
         */
    	

    	
    	
    	

    	
    	
    	

    }
	
	
	
	
	
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == TAKE_PICTURE_BEFORE) {
        	cv.put("photo_name_before", photo_id_before);
        	try{
        	BitmapFactory.Options opts = new BitmapFactory.Options();
        	opts.inSampleSize = 10 ;
        	bm=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/com.enice.station/" + photo_id_before + ".jpg",opts);
        	error_photo_before.setImageBitmap(bm);
        	}
        	catch (Exception e) {
        	    e.printStackTrace();
        	    System.out.println(e);
        	}
        }
        if (resultCode == Activity.RESULT_OK && requestCode == TAKE_PICTURE_AFTER) {
        	cv.put("photo_name_after", photo_id_after);
        	try{
        	BitmapFactory.Options opts = new BitmapFactory.Options();
        	opts.inSampleSize = 10 ;
        	bm=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/com.enice.station/" + photo_id_after + ".jpg",opts);
        	error_photo_after.setImageBitmap(bm);
        	}
        	catch (Exception e) {
        	    e.printStackTrace();
        	    System.out.println(e);
        	}
        }
        

    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void showTips(){
		Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("退出故障详情处理");
		alertDialog.setMessage("请选择您将进行的操作");
		alertDialog.setPositiveButton("保存并返回", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which)
			{
				//进度对话框的秒显用法
				ProgressDialog progressdialog = new ProgressDialog(AddError.this);  

				progressdialog.setMessage("请稍候……"); 
				
				progressdialog.show();
				cv.put("error_type", errortype);
				cv.put("description", description.getText().toString());
				cv.put("completion", completion.getText().toString());
//				DatabaseHelper.getInstance().update("tower_error", cv, "session_id=?", new String[]{session_id});
				DatabaseHelper.getInstance().insert("tower_error", null, cv); 
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
	
		
	
	
	
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
			this.showTips();
			return false;
			}
			return false;
			}

}