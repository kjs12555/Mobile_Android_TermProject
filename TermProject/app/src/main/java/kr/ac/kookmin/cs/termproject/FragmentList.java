package kr.ac.kookmin.cs.termproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentList extends Fragment {

    View v;
    //대분류 Spinner
    ArrayList<String> dataBig;
    Spinner spinnerBig;
    ArrayAdapter<String> adapterBig;
    //소분류 Spinner
    ArrayList<String> dataSmall;
    Spinner spinnerSmall;
    ArrayAdapter<String> adapterSmall;
    boolean typeText = true;    //Statistics버튼을 눌렀을 때, Text로 보여줄지 Graph로 보여줄지에 대한 Flag

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_list,container,false);
        dataBig = new ArrayList<String>();
        dataBig.add("All");
        dataBig.add("Event");
        dataBig.add("Log");
        adapterBig = new ArrayAdapter<String>(v.getContext(), R.layout.support_simple_spinner_dropdown_item, dataBig);
        spinnerBig = (Spinner) v.findViewById(R.id.spinner);
        spinnerBig.setAdapter(adapterBig);
        spinnerBig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                spinnerSmall = (Spinner)v.findViewById(R.id.spinner2);
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
                        //DB에서 해당하는 값을 가져와서 List를 지우고 다시 작성한다. DB작업을 마치고 작성할 예정입니다.
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        final Button typeButton = (Button) v.findViewById(R.id.type);
        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typeButton.getText().toString().equals("Text")){
                    typeButton.setText("Graph");
                    typeText = false;
                }else{
                    typeButton.setText("Text");
                    typeText = true;
                }
            }
        });

        Button statistics = (Button)v.findViewById(R.id.statistics);

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typeText){
                    //Text방식으로 출력하는 부분.
                }else{
                    //Graph방식으로 출력하는 부분.
                }
            }
        });

        return v;
    }
}
