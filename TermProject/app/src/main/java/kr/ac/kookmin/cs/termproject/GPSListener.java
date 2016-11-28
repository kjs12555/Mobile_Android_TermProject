package kr.ac.kookmin.cs.termproject;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by user on 2016-10-26.
 */

public class GPSListener implements LocationListener {

    public GPSListener(){}

    public void onLocationChanged(Location location) {
        
    }
    public void onProviderDisabled(String provider) {}
    public void onProviderEnabled(String provider) {}
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
