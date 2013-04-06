package com.example.mapattempt;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class MyLocationListener extends FragmentActivity implements LocationListener {

	public class Caller implements Runnable{

		public double lat;
		public double lon;
		public int dist;
		private GoogleMap map;
//		public ArrayList<LatLng> locList;
		
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
						 	LatLng thisLoc = new LatLng(thisLat, thisLon);
						 
						 	
						 	//CREATE AND ADD MARKERS HERE
						 	map.addMarker(new MarkerOptions()
					        .position(thisLoc)
					        .title(name)
					        .snippet(city + ", " + state));
						 	
						 	//ADD LATLNG's TO AN ARRAY TO BE USED FOR ALERTS
						 //	locList.add(thisLoc);			 	
						 	
						 	/* KEEPING LOG FOR DEBUGGING
							Log.d("FOUND RED LIGHT",name);
							Log.d("city",city);
							Log.d("state",state);*/
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
	
	
	
	private Location myCachedLoc;	
	public GoogleMap map;
	
	public MyLocationListener(double lat, double longi, GoogleMap gmap) {
		this.myCachedLoc = new Location(LocationManager.GPS_PROVIDER);
		this.myCachedLoc.setLatitude(lat);
		this.myCachedLoc.setLongitude(longi);
		map =  gmap;
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, longi), 15));
		//The distance is currently hard-coded to 5 for the caller.  Could be made customizable for user later.
		caller = new Caller(lat,longi, 5, gmap);
		caller.run();
	}
	
	@Override
	public void onLocationChanged(Location arg0) {
		//map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(arg0.getLatitude(), arg0.getLongitude()), 15));

		//make new Location object to be compared to current location for alert checking
		Location locCheck;
		locCheck = arg0;
		
		//Compare current location to location of points and, if it is within a certain radius, issue alert
	/*	for (int i=0; i< caller.locList.size(); i++){
			locCheck.setLatitude(caller.locList.get(i).latitude);
			locCheck.setLongitude(caller.locList.get(i).longitude);
			if (arg0.distanceTo(locCheck)<100){
				Notify("HI", "HELLO");
			}
		}*/
		
		if (arg0.distanceTo(this.myCachedLoc) > 4023){
			myCachedLoc.setLatitude(arg0.getLatitude());
			myCachedLoc.setLongitude(arg0.getLongitude());
			//may need to clear map
			caller.lat = myCachedLoc.getLatitude();
			caller.lon = myCachedLoc.getLongitude();
			//caller.locList.clear();
			//Update locations on map by running caller
			//may need to clear the map when new points are added
			caller.run();
		}
		
	}
	
	
/*	private void Notify(String notificationTitle, String notificationMessage) {
	    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	    android.app.Notification notification = new android.app.Notification(R.drawable.ic_launcher, "A New Message from Dipak Keshariya (Android Developer)!",
	    System.currentTimeMillis());

	    Intent notificationIntent = new Intent(this, Notification.class);
	    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
	    notification.setLatestEventInfo(this, notificationTitle, notificationMessage, pendingIntent);
	    notificationManager.notify(10001, notification);
	}*/


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
