package com.station.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRcode extends Activity {
	private TextView tvTitle;
	private Button btnBack;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.MyCustomTheme);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.qr_code);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		tvTitle = (TextView) findViewById(R.id.Titletext);
		tvTitle.setText("Ãı–Œ¬Î…®√Ë");

		btnBack = (Button) findViewById(R.id.TitleBackBtn);

		btnBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});


		TextView scan = (TextView) this.findViewById(R.id.scan);
		scan.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				IntentIntegrator.initiateScan(QRcode.this);
			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult != null) {
			TextView textView=(TextView) this.findViewById(R.id.show);
			textView.setText(scanResult.getContents());
		}
	}
}