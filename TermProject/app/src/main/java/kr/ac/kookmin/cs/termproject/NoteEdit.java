package kr.ac.kookmin.cs.termproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by user on 2016-11-30.
 */

public class NoteEdit extends Activity {
    ImageView image;
    EditText logName, noteName, note;
    DBHelper helper;
    SQLiteDatabase db;
    String saveUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);
        Button complete = (Button)findViewById(R.id.note_complete);
        image = (ImageView)findViewById(R.id.note_imageView);
        logName = (EditText)findViewById(R.id.note_log_name);
        noteName = (EditText)findViewById(R.id.note_name);
        note = (EditText)findViewById(R.id.note);

        helper = new DBHelper(this.getApplicationContext(), "TermProject.db", null, 1);
        db = helper.getWritableDatabase();

        Cursor rs = db.rawQuery("select LogName from Save;",null);
        rs.moveToNext();
        String name = rs.getString(0);
        rs.close();
        logName.setText(name);

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //완료를 눌렀을 때.
                Location networkLocation=null;
                Location gpsLocation=null;
                Location resultLocation = null;
                try {
                    networkLocation = LogService.lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    gpsLocation = LogService.lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }catch (SecurityException e){
                    Toast.makeText(v.getContext(), "GPS 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                if(networkLocation!=null && gpsLocation!=null){
                    if(networkLocation.getTime()-System.currentTimeMillis()<gpsLocation.getTime()-System.currentTimeMillis()+60*1000){  //gps가 정확성이 더 높기 때문에 1분의 가중치를 주었습니다.
                        resultLocation = gpsLocation;
                    }else{
                        resultLocation = networkLocation;
                    }
                }else if(networkLocation==null){
                    resultLocation = gpsLocation;
                }else{
                    resultLocation = networkLocation;
                }
                if(resultLocation!=null){
                    db.execSQL("update Save set LogName=?", new Object[]{logName.getText().toString()});
                    db.execSQL("insert into Log(Latitude, Longitude, CameraPath, Note, Type, NoteName) values(? ,? ,? ,? ,? ,?);",new Object[]{resultLocation.getLatitude(), resultLocation.getLongitude(), saveUri, note.getText().toString(), 2, noteName.getText().toString() });
                }
                finish();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode==1){
                saveUri = data.getDataString();
                image.setImageURI(data.getData());
            }
        }
    }
}
