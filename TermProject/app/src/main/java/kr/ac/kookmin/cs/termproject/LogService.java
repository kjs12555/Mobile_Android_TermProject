package kr.ac.kookmin.cs.termproject;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.TimeUtils;
import android.widget.Toast;

public class LogService extends Service implements SensorEventListener{

    static LocationManager lm;
    GPSListener gps;
    DBHelper helper;
    SQLiteDatabase db;
    static int foot=0;

    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;

    private final int SHAKE = 800;

    private SensorManager sensorManager;
    private Sensor accel;

    private long lastTime;
    private float last_X;
    private float last_Y;
    private float last_Z;

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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

        Cursor rs = db.rawQuery("select Foot from Save;",null);
        rs.moveToNext();
        foot = rs.getInt(0);
        rs.close();

        gps = new GPSListener(db);

        if(accel!=null){
            sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME);
        }

        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000*60, 10, gps);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000*60, 10, gps);
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
        }catch(SecurityException ex){}

        sensorManager.unregisterListener(this);

        foot = 0;

        if(EventDataAdapter.taskRun!=null) {
            EventDataAdapter.taskRun.setText(Integer.toString(foot));
        }

        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x,y,z;
        float speed;

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime-lastTime);

            if(gabOfTime>100){

                lastTime = currentTime;

                x = event.values[DATA_X];
                y = event.values[DATA_Y];
                z = event.values[DATA_Z];

                speed = Math.abs(x+y+z-last_X-last_Y-last_Z)/gabOfTime*10000;

                last_X = x;
                last_Y = y;
                last_Z = z;

                if(speed > SHAKE){
                    foot++;
                    if(EventDataAdapter.taskRun!=null){
                        EventDataAdapter.taskRun.setText(Integer.toString(foot));
                    }
                    db.execSQL("update Save set Foot=?;",new Object[]{LogService.foot});
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
