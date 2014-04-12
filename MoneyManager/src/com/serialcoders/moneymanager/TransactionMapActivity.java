package com.serialcoders.moneymanager;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.ParseObject;
import com.parse.ParseQuery;


/**
 * 
 * Android activity for creating a google map with transaction markers. 
 * 
 * @author Tsz 
 *
 */
public class TransactionMapActivity extends SliderMenuActivity implements  LocationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {
	/**
     * @param map Google map
     */
    private GoogleMap map;
    /**
     * @param user current user
     */
    private ParseUser user;
    /**
     * @param newMarkerList a list of transaction markers
     */
    private ArrayList<Marker> newMarkerList = new ArrayList<Marker>();
    /**
     * @param mLocationManager gets the location
     */
    LocationManager mLocationManager;
    /**
     * @param transactionList lists of transactions by that user
     */
    List<ParseObject> transactionList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_map, null, false);
        drawerLayout.addView(contentView, 0); 
               
        Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT", "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
        user = ParseUser.getCurrentUser();
        
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();       

        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLngBounds gaTech = new LatLngBounds(new LatLng(33.7765, -84.4002), new LatLng(33.7765, -84.4002));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(gaTech.getCenter(), 15));
        
        doMapPop();
        
    }
    
    /**
     * 
     * Populates the map with transaction markers from information retrieved from the list of user transactions.
     * 
     */
    private void doMapPop() {
        ParseQuery<ParseObject> transactionQuery = ParseQuery.getQuery("Transaction");
        transactionQuery.whereEqualTo("userName", user.getUsername());
          
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);        
        Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            location = new Location("default");
        }
        
                 
        try {
            transactionList = transactionQuery.find();
            Set<String> toKeep = new HashSet<String>();
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            
            String on = " on ";
            String amt = "amount";
            String tLocation = "transactionLocation";
            for (ParseObject transaction : transactionList)    {
                //add this post to list of map pins to keep
                toKeep.add(transaction.getObjectId());
                String title;
                LatLng position = new LatLng(transaction.getParseGeoPoint(tLocation).getLatitude(), transaction.getParseGeoPoint(tLocation).getLongitude());
                String date = df.format(transaction.getCreatedAt());
                if (transaction.getDouble(amt) < 0) {
                    title = DecimalFormat.getCurrencyInstance().format(-transaction.getDouble(amt));
                    title = "Withdrawal " + title + on + date;
                    newMarkerList.add(map.addMarker(new MarkerOptions().position(position).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.withdrawal_marker)))); 
                }
                else {
                    title = DecimalFormat.getCurrencyInstance().format(transaction.getDouble(amt)); 
                    title = "Deposit " + title + on + date;
                    newMarkerList.add(map.addMarker(new MarkerOptions().position(position).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.deposit_marker)))); 
                }
                   
                    //title = title.concat(transaction.get("amount").toString().substring(1).toLowerCase());
                    
                               
            }
        } catch (ParseException e) {
            transactionList = new ArrayList<ParseObject>();
            Toast.makeText(TransactionMapActivity.this, "Cannot load Transactions: " + e, Toast.LENGTH_LONG).show();
        }
      
    }
    /**
     * 
     * Cleans the map and remove all markers.
     * 
     */
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
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        
        map.moveCamera(center);
        map.animateCamera(zoom);
        
        doMapWipe();
        doMapPop();    
    }
    
}
