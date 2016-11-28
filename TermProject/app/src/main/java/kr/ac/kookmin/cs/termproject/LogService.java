package kr.ac.kookmin.cs.termproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.widget.Toast;

public class LogService extends Service {

    LocationManager lm;
    GPSListener gps;

    public LogService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        gps = new GPSListener();
        try{
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, gps);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, gps);
        }catch (SecurityException ex){
            Toast.makeText(this, "GPS리스너를 추가 에러 발생!", Toast.LENGTH_SHORT).show();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        lm.removeUpdates(gps);
        super.onDestroy();
    }
}
