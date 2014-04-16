package com.example.track;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class SaveActivity extends Activity {

	private TextView runInfo;
	private String dateId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save);
		runInfo = (TextView) findViewById(R.id.textView1);
		dateId = DateFormat.getDateTimeInstance().format(new Date());
		runInfo.setText("Run Info: \n" + dateId);
	}

	public void showSaved(View view){
		String filename = dateId ;
		
		FileOutputStream fos = null;
		
		try{
			fos = openFileOutput(filename, Context.MODE_PRIVATE);	
		}catch(FileNotFoundException e){}
		
		
		Iterator<Integer> it1 = RunActivity.data1.iterator();
		Iterator<Integer> it2 = RunActivity.data2.iterator();
		
		while(it1.hasNext()){
			byte[] bytes1 = ByteBuffer.allocate(4).putInt(it1.next()).array();
			byte[] bytes2 = ByteBuffer.allocate(4).putInt(it2.next()).array();
			try{
				fos.write(bytes1,0,4);
				fos.write(bytes2,0,4);
			}catch(IOException e){}
		}
		try{
			fos.close();
		}catch(IOException e){}

		Intent intent = new Intent(this, SavedActivity.class);
		startActivity(intent);
	}
	
	public void returnHome(View view){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.save, menu);
		return true;
	}

}
