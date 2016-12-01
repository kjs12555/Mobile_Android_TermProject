package kr.ac.kookmin.cs.termproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FragmentMap extends Fragment implements OnMapReadyCallback {
    GoogleMap mMap;
    View v;
    //대분류 Spinner
    ArrayList<String> dataBig;
    Spinner spinnerBig;
    ArrayAdapter<String> adapterBig;
    //소분류 Spinner
    ArrayList<String> dataSmall = new ArrayList<>();
    Spinner spinnerSmall;
    ArrayAdapter<String> adapterSmall;
    ArrayList<LogSave> dataArrayList = new ArrayList<>();
    int bigSpinnerPosition=0;
    DBHelper helper;
    SQLiteDatabase db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(v!=null){
            ViewGroup parent = (ViewGroup) v.getParent();
            if(parent!=null){
                parent.removeView(v);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_map, container, false);
        ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        helper = new DBHelper(v.getContext(), "TermProject.db",null, 1);
        db = helper.getWritableDatabase();
        dataBig = new ArrayList<String>();
        dataBig.add("All");
        dataBig.add("Event");
        dataBig.add("Log");
        adapterBig = new ArrayAdapter<String>(v.getContext(), R.layout.support_simple_spinner_dropdown_item, dataBig);

        spinnerBig = (Spinner) v.findViewById(R.id.map_big);
        spinnerSmall = (Spinner)v.findViewById(R.id.map_small);

        spinnerBig.setAdapter(adapterBig);
        spinnerBig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor rs;
                bigSpinnerPosition = position;
                dataSmall.clear();
                //position에 따라 전체 Log이름으로 초기화하거나 Event의 이름으로 초기화 하거나 Log별로 초기화 합니다. DB를 작성하면 마저 작성할 예정
                switch (position){
                    case 0:
                        dataSmall.add("All");
                        break;
                    case 1:
                        //Event이름을 중복없이 add
                        rs = db.rawQuery("select Distinct(EventName) from Log;",null);
                        while(rs.moveToNext()){
                            dataSmall.add(rs.getString(0));
                        }
                        rs.close();
                        break;
                    case 2:
                        //Log이름을 중복없이 add
                        rs = db.rawQuery("select Distinct(LogName) from Log;",null);
                        while(rs.moveToNext()){
                            dataSmall.add(rs.getString(0));
                        }
                        rs.close();
                        break;
                }
                adapterSmall = new ArrayAdapter<String>(v.getContext(), R.layout.support_simple_spinner_dropdown_item, dataSmall);
                spinnerSmall.setAdapter(adapterSmall);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spinnerSmall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //DB에서 해당하는 값을 가져와서 Map을 지우고 다시 작성한다. DB작업을 마치고 작성할 예정입니다.
                Cursor rs;
                if(bigSpinnerPosition!=0) {
                    String args[] = new String[2];
                    if (bigSpinnerPosition == 1) {
                        args[0] = "EventName";
                    } else {
                        args[0] = "LogName";
                    }
                    args[1] = spinnerSmall.getSelectedItem().toString();
                    rs = db.rawQuery("select * from Log where " + args[0] + "='" + args[1] + "' order by ID", null);
                }else{
                    rs = db.rawQuery("select * from Log;",null);
                }
                dataArrayList.clear();
                while(rs.moveToNext()){
                    dataArrayList.add(new LogSave(rs.getInt(0), rs.getDouble(1), rs.getDouble(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9), rs.getLong(10), rs.getString(11)));
                }
                mMap.clear();
                //마커, 점, 선 만들기 함수 호출.
                makeMap();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.610215, 126.997202)));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        mMap.animateCamera(zoom);
    }

    public void makeMap(){
        List<LatLng> polyline = new LinkedList<>();
        int j=-1;
        LatLng latLng = null;
        for( LogSave i : dataArrayList){
            j++;
            int type = i.getType();
            if(type == 1){
                latLng = new LatLng(i.getLatitude(),i.getLongitude());
                mMap.addCircle(new CircleOptions().center(latLng).radius(5).fillColor(Color.RED));
                polyline.add(latLng);
            }else if(type==2){
                //Marker작성
                mMap.addMarker(new MarkerOptions().position(new LatLng(i.getLatitude(), i.getLongitude())).title(i.getEventName()).snippet(Integer.toString(j)));
            }
        }
        mMap.addPolyline(new PolylineOptions().addAll(polyline).width(3));
        if(latLng!=null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }else{
            latLng = new LatLng(37.610215, 126.997202);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            ArrayList<LogSave> dataArrayList = FragmentMap.this.dataArrayList;

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getActivity().getLayoutInflater().inflate(R.layout.info_window, null);

                TextView textView = (TextView)v.findViewById(R.id.info_text);
                ImageView imageView = (ImageView)v.findViewById(R.id.info_image);

                int position = Integer.parseInt(marker.getSnippet());
                LogSave data = dataArrayList.get(position);

                try {
                    imageView.setImageURI(Uri.parse(data.getCameraPath()));
                }catch (Exception e){}

                String text = "로그 이름 : "+data.getLogName()+"\n"+"이벤트 이름 : "+data.getEventName()+"\n"+"노트 이름 :  "+data.getNoteName()+"\n";
                text = text.concat(data.getNote()+"\n");
                text = text.concat("업데이트 시간 : "+data.getTime());

                textView.setText(text);

                return v;
            }
        });
    }
}
