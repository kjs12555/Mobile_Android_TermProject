package kr.ac.kookmin.cs.termproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

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
                //position에 따라 전체 Log이름으로 초기화하거나 Event의 이름으로 초기화 하거나 Log별로 초기화 합니다. DB를 작성하면 마저 작성할 예정
                switch (position){
                    case 0:
                        //Event이름을 중복없이 add
                        dataSmall.clear();
                        rs = db.rawQuery("select Distinct(EventName) from Log;",null);
                        while(rs.moveToNext()){
                            dataSmall.add(rs.getString(0));
                        }
                        rs.close();
                        break;
                    case 1:
                        //Log이름을 중복없이 add
                        dataSmall.clear();
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
                String args[] = new String[2];
                if(bigSpinnerPosition==0){
                    args[0] = "EventName";
                }else{
                    args[0] = "LogName";
                }
                args[1] = spinnerSmall.getSelectedItem().toString();
                rs = db.rawQuery("select * from Log where "+args[0]+"='"+args[1]+"' order by ID", null);
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

    }
}
