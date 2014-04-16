package com.example.track;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class RunActivity extends Activity {
	private static GraphicalView view1;
	private static GraphicalView view2;
	private LineGraph line1 = new LineGraph();
	private LineGraph line2 = new LineGraph();
	private static Thread thread;
	LinearLayout layout;
	private Handler paintHandler;
	public static ArrayList<Integer> data1 = new ArrayList<Integer>();
	public static  ArrayList<Integer> data2 = new ArrayList<Integer>();
	private  int color;
	private boolean alert;
	public static NotificationCompat.Builder mBuilder;
	public static boolean collect = true;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run);
		layout = (LinearLayout) findViewById(R.id.LinearLayout1);

		Resources res = getResources();
		color = res.getColor(R.color.darkgreen);
		line1.setColor(color);
		line2.setColor(color);
		view1 = line1.getView(this);
		view2 = line2.getView(this);

		mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Track Notification")
				.setContentText("Irregular Pattern Detected");
		Intent notificationIntent = new Intent(this, RunActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		mBuilder.setContentIntent(intent);
		mBuilder.setAutoCancel(true);
		long[] pattern = { 500, 500 };
		mBuilder.setVibrate(pattern);
		mBuilder.setLights(Color.BLUE, 500, 500);
		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		mBuilder.setSound(alarmSound);

		paintHandler = new Handler() {
			public void handleMessage(Message msg) {
				Bundle b = msg.getData();
				int i = b.getInt("cycle");
				int val1 = b.getInt("data1");
				int val2 = b.getInt("data2");

				if (i > 1) {
					if (Math.abs(val1 - data1.get(i - 1)) > 50) {
						val1 = data1.get(i - 1);
					}
					if (Math.abs(val2 - data2.get(i - 1)) > 50) {
						val2 = data2.get(i - 1);
					}
				}

				data1.add(val1);
				data2.add(val2);

				if (val2 > 600 && !alert) {
					line2.setColor(Color.RED);

					NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					// mId allows you to update the notification later on.
					mNotificationManager.notify(1, mBuilder.build());

					alert = true;
				}

				if (val2 < 600) {
					line2.setColor(color);
					alert = false;
				}

				Point p1 = new Point(i, val1);
				Point p2 = new Point(i, val2);
				
				
				line1.addNewPoints(p1); // Add it to our graph
				line2.addNewPoints(p2);
				view1.repaint();
				view2.repaint();
			}
		};

		thread = new Thread() {
			public void run() {
				int i = 0;
				byte[] buffer = new byte[6];

//				try {
//					mInput = BluetoothConnectActivity.btDevice.getInputStream();
//				} catch (IOException e) {
//				}

				while (RunActivity.collect) {

					try {
						CalibrateActivity.mInput.read(buffer);
					} catch (IOException e) {
					}

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
					paintHandler.sendMessage(msg);
					i++;
				}
			}
		};
		thread.start();

		layout.addView(view1, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		layout.addView(view2, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 1));

	}

	public void endCollection(View view) {
		RunActivity.collect = false;
		Intent intent = new Intent(this, SaveActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.run, menu);
		return true;
	}

}
