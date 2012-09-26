package com.station.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRcode extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qr_code);
		setTitle("Ãı–Œ¬Î…®√Ëƒ£øÈ");
		Button button = (Button) this.findViewById(R.id.scan);
		button.setOnClickListener(new OnClickListener() {
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