package com.example.track;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ViewRunActivity extends Activity {

	private static GraphicalView view1;
	private static GraphicalView view2;
	private LineGraph line1 = new LineGraph();
	private LineGraph line2 = new LineGraph();
	private Thread thread;
	LinearLayout layout;
	private Handler paintHandler;
	public static ArrayList<Integer> data1 = new ArrayList<Integer>();
	public static ArrayList<Integer> data2 = new ArrayList<Integer>();
	private int color;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_run);
		layout = (LinearLayout) findViewById(R.id.LinearLayout1);

		Resources res = getResources();
		color = res.getColor(R.color.darkgreen);
		line1.setColor(color);
		line2.setColor(color);
		view1 = line1.getView(this);
		view2 = line2.getView(this);

		paintHandler = new Handler() {
			public void handleMessage(Message msg) {
				Bundle b = msg.getData();
				int i = b.getInt("cycle");
				int val1 = b.getInt("data1");
				int val2 = b.getInt("data2");

				data1.add(val1);
				data2.add(val2);

				Point p1 = new Point(i, val1);
				Point p2 = new Point(i, val2);

				line1.addNewPoints2(p1); // Add it to our graph
				line2.addNewPoints2(p2);
				view1.repaint();
				view2.repaint();
			}
		};

		thread = new Thread() {
			public void run() {
				int i = 0;
				byte[] buffer = new byte[8];
				int returnVal = 0;

				FileInputStream mInput = null;

				try {
					mInput = openFileInput(SavedActivity.filename);
				} catch (IOException e) {
				}
				while (returnVal != -1) {

					try {
						returnVal = mInput.read(buffer);
					} catch (IOException e) {
					}

					ByteBuffer wrapped = ByteBuffer.wrap(buffer);
					int data1 = wrapped.getInt();
					int data2 = wrapped.getInt();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_run, menu);
		return true;
	}

}
