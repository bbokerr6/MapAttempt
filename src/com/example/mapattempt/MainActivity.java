package com.example.mapattempt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends FragmentActivity {

	
	public class Caller implements Runnable{

		public double lat;
		public double lon;
		public int dist;
		public GoogleMap map;
		
		public Caller(double lat, double lon, int dist, GoogleMap map){
			this.lat = lat;
			this.lon = lon;
			this.dist = dist;
			this.map = map;
		}
		
		@Override
		public void run() {
			Log.d("Got here","5");
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("latitude", Double.toString(lat));
			params.put("longitude", Double.toString(lon));
			params.put("distance_in_miles", Integer.toString(dist));
			client.get("http://redlights.herokuapp.com/in_proximity_of",params ,new JsonHttpResponseHandler() {
			    @Override
			    public void onSuccess(JSONArray red_lights) {
			        for (int i = 0; i < red_lights.length(); i++) {
						try {
							JSONObject redLight = red_lights.getJSONObject(i);
							//placeOnMap(redLight);
						 	double thisLon = redLight.getDouble("longitude");
						 	double thisLat = redLight.getDouble("latitude");
						 	String name = redLight.getString("name");
						 	String city = redLight.getString("city");
						 	String state = redLight.getString("state");
						
						 	map.addMarker(new MarkerOptions()
					        .position(new LatLng(thisLat, thisLon))
					        .title(name)
					        .snippet(city + ", " + state));
						 	

						 	//CREATE AND ADD MARKERS HERE
						 	/*static final LatLng LIGHT = new LatLng(thisLat, thisLon);
						 	Marker light = mMap.addMarker(new MarkerOptions()
						 	                          .position(LIGHT)
						 	                          .title(name)
						 	                          .snippet(city + ", " + state));*/
						 	
						 	
							Log.d("FOUND RED LIGHT",name);
							Log.d("city",city);
							Log.d("state",state);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							
							Log.d("JSON ERROR", e.getMessage());
						}
						
					}
			    }
			});	
			// TODO Auto-generated method stub
			
		}
		
	};
	
	Caller caller;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.d("Got here","1");
		
		
		Log.d("Got here","2");
		
		GoogleMap map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
		
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
		//If the GPS is not turned on, add it to the map
		if (!gpsEnabled) {
			enableLocationSettings();
		}
		

		
		Criteria criteria = new Criteria();
		//criteria.setAccuracy(Criteria.ACCURACY_FINE);
		//criteria.setCostAllowed(false);

		
		String provider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(provider);
		MyLocationListener currentLocation = new MyLocationListener(location.getLatitude(), location.getLongitude(), map);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600000, 8046, currentLocation);
		caller = new Caller(location.getLatitude(),location.getLongitude(), 5, map);
		caller.run();
	}
	private void enableLocationSettings() {
		Log.d("Got here","3");
		Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    startActivity(settingsIntent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d("Got here","4");
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
