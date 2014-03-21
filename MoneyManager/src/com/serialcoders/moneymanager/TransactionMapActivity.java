package com.serialcoders.moneymanager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.FindCallback;

public class TransactionMapActivity extends FragmentActivity implements  LocationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

	private GoogleMap map;
	private ParseUser user;
	private Map<String,Marker> mapMarkers = new HashMap<String,Marker>();
	private ArrayList<Marker> newMarkerList = new ArrayList<Marker>();
	LocationManager mLocationManager;
	List<ParseObject> transactionList;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT", "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
        user = ParseUser.getCurrentUser();
        
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();       

        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //LatLngBounds gaTech = new LatLngBounds(
        		  //new LatLng(29.65, -82.34), new LatLng(29.67, -82.31));

        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(gaTech.getCenter(), 11));
        
        doMapPop();
        
    }
    
    private void doMapPop(){
    	  ParseQuery<ParseObject> transactionQuery = ParseQuery.getQuery("Transaction");
    	  transactionQuery.whereEqualTo("userName", user.getUsername());
    	  
		  mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);        
		  final Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		  final ParseGeoPoint userLocation = geoPointFromLocation(location);
		  
		  List<ParseObject> transactionList;
		  try {
			  transactionList = transactionQuery.find();
			  Set<String> toKeep = new HashSet<String>();
			  
	      	  //loops through results of search
	      	  for(ParseObject transaction : transactionList)	{
	      		  //add this post to list of map pins to keep
	      		  toKeep.add(transaction.getObjectId());
	      		  String title;
	      		  if(transaction.getDouble("amount")<0){
	      			title = DecimalFormat.getCurrencyInstance().format(-transaction.getDouble("amount"));
	      			title = "Withdrawal " + title;
	      		  }
	      		  else{
	      			title = DecimalFormat.getCurrencyInstance().format(transaction.getDouble("amount")); 
	      			title = "Deposit " + title;
	      		  }
	      		 
	      		  //title = title.concat(transaction.get("amount").toString().substring(1).toLowerCase());
	      		  LatLng position = new LatLng(transaction.getParseGeoPoint("transactionLocation").getLatitude(),transaction.getParseGeoPoint("transactionLocation").getLongitude());
	      		  newMarkerList.add(map.addMarker(new MarkerOptions().position(position).title(title)));  		  
	      	  }
		  } catch (ParseException e) {
			  transactionList = new ArrayList<ParseObject>();
			  Toast.makeText(TransactionMapActivity.this, "Cannot load Transactions: " + e, Toast.LENGTH_LONG).show();
		  }
	  
	}
    
    public void doMapWipe() {
		while (!newMarkerList.isEmpty()) {
			newMarkerList.get(0).remove();
			newMarkerList.remove(0);
		}
	}
    
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location location) {
		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
		
		map.moveCamera(center);
		map.animateCamera(zoom);
		
		doMapWipe();
		doMapPop();	
	}
	
	private ParseGeoPoint geoPointFromLocation(Location location) {
		  return new ParseGeoPoint(location.getLatitude(), location.getLongitude());
	}
}
