package com.example.track;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BluetoothConnectActivity extends Activity {

	private ListView lv;
	private ArrayAdapter<String> mArrayAdapter;
	private ArrayList<String> mList;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private final static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	public static BluetoothSocket btDevice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		Intent intent = getIntent();
		setContentView(R.layout.activity_bluetooth_connect);
		lv = (ListView)findViewById(R.id.listView1);
		mList = new ArrayList<String>();
		mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mList);
		lv.setAdapter(mArrayAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
		            int position, long id) {

	            // selected item
	           String selected = (String) lv.getItemAtPosition(position);
	           String[] info = selected.split("\\n");
	           String address = info[1];
	                   
	           connectBt(address.toUpperCase());
			}
			});
		findBt();
	}


	public void findBt() {
		
		
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		
		
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		        mList.add(device.getName() + "\n" + device.getAddress());
		    }
		}
		
		mArrayAdapter.notifyDataSetChanged();
	}
	
	public void connectBt(String address){		
		
		BluetoothDevice mDevice = mBluetoothAdapter.getRemoteDevice(address);
		BluetoothSocket mSocket = null;
		boolean connection = false;

		try {
			mSocket = mDevice.createRfcommSocketToServiceRecord(MY_UUID);
		}  catch(IOException e){	}
		mBluetoothAdapter.cancelDiscovery();
		try{
			mSocket.connect();
			connection=true;
		} catch (IOException connectException){
			Toast.makeText(getApplicationContext(),"Connection Failed!" 
			         ,Toast.LENGTH_LONG).show();
			try{
				mSocket.close();
			} catch(IOException closeException){}
		}
		
		if(connection){
			this.btDevice = mSocket;
			Intent intent = new Intent(this, BluetoothManageActivity.class);
			startActivity(intent);
		}		
	}
	
	protected void onResume(){
		super.onResume();
		if(!mBluetoothAdapter.isEnabled()){
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, 0);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bluetooth_connect, menu);
		return true;
	}
	
	

}
