package com.example.maigo;

import java.util.Date;

import android.app.Activity;
import android.location.Criteria;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {
	private LocationManager mLocationManager;
	private double lat, lon;
	private double destLati, destLongi;
	private double checkDist;
	private double lastDist;
	private double startDist;
	private boolean hardmode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		hardmode = false;
        destLati = 35.604730;
        destLongi = 139.683841;
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	    Button button = (Button) findViewById(R.id.button1);
	    button.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	    		Intent intent = new Intent(MainActivity.this, DestinationActivity.class);
	    		int requestCode = 1001;
	    		startActivityForResult(intent, requestCode);
	        }
	    });
	    Button hard_button = (Button) findViewById(R.id.button2);
	    hard_button.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	if(lastDist==0){
	        		return;
	        	}else if(hardmode){
	        		checkDist = lastDist;
	        	}else{
		        	hardmode = true;
		        	startDist = lastDist;
		        	checkDist = lastDist;
		            TextView tvLatitude = (TextView) findViewById(R.id.Latitude);
		            TextView tvLongtude = (TextView) findViewById(R.id.Longitude);
		            TextView tvDistance = (TextView) findViewById(R.id.Distance);
		            tvLatitude.setText("目標距離："+checkDist);
		            tvLongtude.setText("");
		            tvDistance.setText("ちょっとまってね");
		    	    Button hard_button = (Button) findViewById(R.id.button2);
		    	    hard_button.setText("チェックポイント設置");
	        	}
	        }
	    });
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
	
	protected void onPause(){
		mLocationManager.removeUpdates(this);
		super.onPause();
	}

	protected void onResume(){
		super.onResume();
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		String provider = LocationManager.GPS_PROVIDER;
		mLocationManager.requestLocationUpdates(provider, 3000, 0, (LocationListener) this);	
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
        lat = location.getLatitude();
        lon = location.getLongitude();
        Date date = new Date();
		Toast.makeText(this, ""+date, Toast.LENGTH_SHORT).show();
		if(hardmode){
			updateLocateHard(location);
		}else{
			updateLocateNormal(location);
		}
    }

	private void updateLocateNormal(Location location){
        TextView tvLatitude = (TextView) findViewById(R.id.Latitude);
        TextView tvLongtude = (TextView) findViewById(R.id.Longitude);
        TextView tvDistance = (TextView) findViewById(R.id.Distance);
        tvLatitude.setText("緯度:"+String.valueOf(lat));
        tvLongtude.setText("経度:"+String.valueOf(lon));
        float[] distance = new float[1];
		Location.distanceBetween(
				lat, lon,
				(double)destLati, (double)destLongi, distance);
        tvDistance.setText(String.valueOf("距離:"+distance[0])+"m");	
		lastDist = distance[0];
	}

	private void updateLocateHard(Location location){
        TextView message2 = (TextView) findViewById(R.id.Longitude);
        TextView message3 = (TextView) findViewById(R.id.Distance);
        float[] distance = new float[1];
		Location.distanceBetween(
				lat, lon,
				(double)destLati, (double)destLongi, distance);

		String mesDist;
        double percentDist = distance[0]/startDist;
        if(percentDist > 1.1){
            mesDist = "とおい";
        }else if(percentDist > 0.75){
            mesDist = "まだまだ"; 
        }else if(percentDist > 0.50){
            mesDist = "ちかづいてきた"; 
        }else if(percentDist > 0.25){
            mesDist = "ちかい";
        }else if(percentDist > 0.10){
            mesDist = "すぐちかく";
        }else{
            mesDist = "すぐそこ";
        }
        message2.setText("きょり："+mesDist);

        double progress = checkDist - distance[0];
		String mesProgress;
        if(progress > 40){
            mesProgress = "けっこう進んだ！";
        }else if(progress > 10){
            mesProgress = "進んだかも"; 
        }else if(progress > -10){
            mesProgress = "あんまりかわらないよ"; 
        }else if(progress > -40){
            mesProgress = "戻ったかも？";
        }else{
            mesProgress = "結構戻った";
        }	       
        	
        message3.setText(String.valueOf("チェックポイントから:\n"+mesProgress));	
		lastDist = distance[0];
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	public void onActivityResult( int requestCode, int resultCode, Intent intent ){
		if( requestCode == 1001 ){
			if( resultCode == Activity.RESULT_OK ){
				double lati = intent.getDoubleExtra("lati",0);
				double longi = intent.getDoubleExtra("longi",0);
				destLati = lati;
				destLongi = longi;  
			}
		}
	}
}
