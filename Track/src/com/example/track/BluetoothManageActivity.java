package com.example.track;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothManageActivity extends Activity {
	
	BluetoothSocket mSocket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSocket = BluetoothConnectActivity.btDevice;
		setContentView(R.layout.activity_bluetooth_manage);
		//manageBt();
	}
	
	
//	public void manageBt(){
//		InputStream mInput = null;
//		
//		try{
//			mInput = mSocket.getInputStream();
//		}catch (IOException e){}
//	}
	
	public void startCalibrate(View view){
		Intent intent = new Intent(this, CalibrateActivity.class);
		startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bluetooth_manage, menu);
		return true;
	}

}
