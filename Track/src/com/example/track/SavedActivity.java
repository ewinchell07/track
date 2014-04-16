package com.example.track;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SavedActivity extends Activity {

	private ListView lv;
	private ArrayAdapter<String> mArrayAdapter;
	private ArrayList<String> mList;
	public static String filename;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved);
		lv = (ListView)findViewById(R.id.listView1);
		String[] files = this.getFilesDir().list();
		mList = new ArrayList<String>(Arrays.asList(files));
		mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mList);
		lv.setAdapter(mArrayAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
		            int position, long id) {

	            // selected item
	           String selected = (String) lv.getItemAtPosition(position);
	           filename = selected;
	 
	           viewRun();
	           }
			});
	}
	
	public void viewRun(){
		Intent intent = new Intent(this, ViewRunActivity.class);
        startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saved, menu);
		return true;
	}

}
