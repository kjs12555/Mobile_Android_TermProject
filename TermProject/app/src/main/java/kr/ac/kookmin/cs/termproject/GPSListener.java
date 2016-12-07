package kr.ac.kookmin.cs.termproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 2016-10-26.
 */

public class GPSListener implements LocationListener {
    SQLiteDatabase db;

    public GPSListener(SQLiteDatabase db){
        this.db = db;
    }

    public void onLocationChanged(Location location) {
        db.execSQL("insert into Log(Latitude, Longitude, Foot, Type) values(?, ?, ?, ?)", new Object[]{location.getLatitude(), location.getLongitude(), LogService.foot , 1}); //타입 - 시작 : 0, 진행 : 1 , 노트 : 2, 끝 : 3
    }
    public void onProviderDisabled(String provider) {}
    public void onProviderEnabled(String provider) {}
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
