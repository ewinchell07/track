package com.example.track;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CalibrateActivity extends Activity {

	public static boolean calibrate = true;
	private Thread thread;
	private Handler textHandler;
	private ArrayList<Integer> calData1 = new ArrayList<Integer>();
	private ArrayList<Integer> calData2 = new ArrayList<Integer>();
	private int squats = 5;
	private int top;
	private int bottom;
	private TextView squatsR;
	private int topIdx = 0;
	private Button begin;
	public static InputStream mInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calibrate);
		squatsR = (TextView) findViewById(R.id.textView2);
		begin = (Button) findViewById(R.id.button1);

		textHandler = new Handler() {
			public void handleMessage(Message msg) {
				Bundle b = msg.getData();
				int i = b.getInt("cycle");
				int val1 = b.getInt("data1");
				int val2 = b.getInt("data2");

				if (i > 1) {
					if (Math.abs(val1 - calData1.get(i - 1)) > 50) {
						val1 = calData1.get(i - 1);
					}
					if (Math.abs(val2 - calData2.get(i - 1)) > 50) {
						val2 = calData2.get(i - 1);
					}
				}
				

				if (i > (topIdx + 150)) {
					if (Math.abs(val1 - calData1.get(topIdx)) < 10) {
						topIdx = i;
						squats = squats - 1;
						squatsR.setText(String.valueOf(squats));
					}
				}
				
				if (squats == 0) {
					CalibrateActivity.calibrate = false;
					begin.setVisibility(View.VISIBLE);
				}

				
				calData1.add(val1);
				calData2.add(val2);

			}
		};

		thread = new Thread() {
			public void run() {
				int i = 0;
				byte[] buffer = new byte[6];

				CalibrateActivity.mInput = null;
				
				try {
					CalibrateActivity.mInput = BluetoothConnectActivity.btDevice.getInputStream();
				} catch (IOException e) {}

				while (CalibrateActivity.calibrate) {

					try {
						CalibrateActivity.mInput.read(buffer);
					} catch (IOException e) {}

					String val1 = new String(Arrays.copyOfRange(buffer, 0, 3));
					String val2 = new String(Arrays.copyOfRange(buffer, 3, 6));
					int data1 = Integer.parseInt(val1);
					int data2 = Integer.parseInt(val2);
					Bundle b = new Bundle();
					Message msg = new Message();
					b.putInt("cycle", i);
					b.putInt("data1", data1);
					b.putInt("data2", data2);
					msg.setData(b);
					textHandler.sendMessage(msg);
					i++;
				}
			}
		};
		thread.start();
	}

	public void startRun(View view) {
		Intent intent = new Intent(this, RunActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calibrate, menu);
		return true;
	}

}
