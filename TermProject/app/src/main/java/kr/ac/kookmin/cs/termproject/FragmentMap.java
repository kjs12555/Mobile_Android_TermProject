package kr.ac.kookmin.cs.termproject;

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
    ArrayList<String> dataSmall;
    Spinner spinnerSmall;
    ArrayAdapter<String> adapterSmall;

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
        dataBig = new ArrayList<String>();
        dataBig.add("All");
        dataBig.add("Event");
        dataBig.add("Log");
        adapterBig = new ArrayAdapter<String>(v.getContext(), R.layout.support_simple_spinner_dropdown_item, dataBig);
        spinnerBig = (Spinner) v.findViewById(R.id.map_big);
        spinnerBig.setAdapter(adapterBig);
        spinnerBig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                spinnerSmall = (Spinner)v.findViewById(R.id.map_small);
                dataSmall = new ArrayList<String>();
                //position에 따라 전체 Log이름으로 초기화하거나 Event의 이름으로 초기화 하거나 Log별로 초기화 합니다. DB를 작성하면 마저 작성할 예정
                switch (position){
                    case 0:
                        dataSmall.add("전체1");
                        dataSmall.add("전체2");
                        dataSmall.add("전체3");
                        break;
                    case 1:
                        dataSmall.add("Event1");
                        dataSmall.add("Event2");
                        dataSmall.add("Event3");
                        break;
                    case 2:
                        dataSmall.add("Log1");
                        dataSmall.add("Log2");
                        dataSmall.add("Log3");
                        break;
                }
                adapterSmall = new ArrayAdapter<String>(view.getContext(), R.layout.support_simple_spinner_dropdown_item, dataSmall);
                spinnerSmall.setAdapter(adapterSmall);
                spinnerSmall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //DB에서 해당하는 값을 가져와서 Map을 지우고 다시 작성한다. DB작업을 마치고 작성할 예정입니다.
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
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
}
