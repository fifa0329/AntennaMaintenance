package com.station.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	private TextView tabDemo;
	private TextView read;
	private TextView write;
	private TextView failure;
	private String session_id = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		session_id = getIntent().getStringExtra("session_id");

		/*
		 * DatabaseHelper.createInstance(this); new
		 * File(Environment.getExternalStorageDirectory() +
		 * "/com.enice.station/").mkdir();
		 */

		tabDemo = (TextView) findViewById(R.id.TextView_StationShow);
		tabDemo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, TabDemoActivity.class);
				intent.putExtra("session_id", session_id);
				startActivity(intent);
			}
		});

		read = (TextView) findViewById(R.id.TextView_AtenaShow);
		read.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, QRcode.class);
				startActivity(intent);
			}
		});

		write = (TextView) findViewById(R.id.TextView_AtenaWrite);
		write.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, EditAntennaList.class);
				intent.putExtra("session_id", session_id);
				startActivity(intent);
			}
		});

		failure = (TextView) findViewById(R.id.TextView_FailureShow);
		failure.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				// intent.setClass(MainActivity.this, EditErrorList.class);
				// startActivity(intent);
				intent.setClass(MainActivity.this, EditErrorList.class);
				// String session_id;
				intent.putExtra("session_id", session_id);
				startActivity(intent);
			}
		});

	}

	private void showTips() {
		Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("退出程序");
		alertDialog.setMessage("是否退出程序？" + "\r\n" + "警告：未上传的数据会丢失，请慎重！");
		alertDialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						MyApplication mapp = new MyApplication();
						mapp.setExit(true);
						finish();
					}
				});
		alertDialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});

		alertDialog.create().show();
		; // 创建对话框
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.showTips();
			return false;
		}
		return false;
	}

	/*
	 * 
	 * public void onDestroy() { super.onDestroy();
	 * DatabaseHelper.destroyInstance(); }
	 */
}