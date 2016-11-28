package kr.ac.kookmin.cs.termproject;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.TimeUtils;
import android.widget.Toast;

public class LogService extends Service {

    LocationManager lm;
    GPSListener gps;
    DBHelper helper;
    SQLiteDatabase db;
    public LogService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        helper = new DBHelper(this, "TermProject.db" ,null, 1);
        db = helper.getWritableDatabase();
        gps = new GPSListener(db);

        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000*10, 0, gps);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000*10, 0, gps);
        } catch (SecurityException ex) {
            Toast.makeText(this, "Location 권한 없음", Toast.LENGTH_SHORT).show();
            onDestroy();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        try {
            lm.removeUpdates(gps);
        }catch(SecurityException ex){
            Toast.makeText(this ,"Location 권한 없음", Toast.LENGTH_SHORT).show();
        }
        super.onDestroy();
    }
}
