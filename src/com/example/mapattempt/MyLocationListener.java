package com.example.mapattempt;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;


public class MyLocationListener extends FragmentActivity implements LocationListener {

	private Location myCachedLoc;	
	public GoogleMap map;
	
	public MyLocationListener(double lat, double longi, GoogleMap gmap) {
		this.myCachedLoc = new Location(LocationManager.GPS_PROVIDER);
		this.myCachedLoc.setLatitude(lat);
		this.myCachedLoc.setLongitude(longi);
		map =  gmap;
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, longi), 15));
		this.pullAndPlace(10);
	}
	
	@Override
	public void onLocationChanged(Location arg0) {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(arg0.getLatitude(), arg0.getLongitude()), 15));
		if (arg0.distanceTo(this.myCachedLoc) > 8046){
			myCachedLoc.setLatitude(arg0.getLatitude());
			myCachedLoc.setLongitude(arg0.getLongitude());
			//Call your function
		}
		
	}

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

	public void pullAndPlace(int dist) {
		//AsyncHttpClient client = new AsyncHttpClient();
	//	RequestParams params = new RequestParams();
	//	params.put("latitude", Double.toString(myCachedLoc.getLatitude()));
		//params.put("longitude", Double.toString(myCachedLoc.getLongitude()));
		//params.put("distance_in_miles", Integer.toString(dist));
	/*	client.get("http://redlights.herokuapp.com/in_proximity_of",params ,new JsonHttpResponseHandler() {
			
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
					
					 	//CREATE AND ADD MARKERS HERE
					 	/*static final LatLng LIGHT = new LatLng(thisLat, thisLon);
					 	Marker light = mMap.addMarker(new MarkerOptions()
					 	                          .position(LIGHT)
					 	                          .title(name)
					 	                          .snippet(city + ", " + state));
					 	LatLng LIGHT = new LatLng(thisLat, thisLon);
					 	//map.addMarker()
					 	
						Log.d("FOUND RED LIGHT",name);
						Log.d("city",city);
						Log.d("state",state);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						
						Log.d("JSON ERROR", e.getMessage());
					}
					
				}
		    }
		});	*/
	}
	
	/*
	public void placeOnMap(JSONObject redLight){
		try{	
		 	double thisLon = redLight.getDouble("longitude");
		 	double thisLat = redLight.getDouble("latitude");
		 	String name = redLight.getString("name");
		 	String city = redLight.getString("city");
		 	String state = redLight.getString("state");
		
		 	//CREATE AND ADD MARKERS HERE
		 /*	static final LatLng LIGHT = new LatLng(thisLat, thisLon);
		 	Marker light = mMap.addMarker(new MarkerOptions()
		 	                          .position(LIGHT)
		 	                          .title(name)
		 	                          .snippet(city + ", " + state));
		 	
		 	
			Log.d("FOUND RED LIGHT",name);
			Log.d("city",city);
			Log.d("state",state);
		} catch(JSONException e) {
			Log.d("JSON ERROR", e.getMessage());
		}
	}
	*/
}
