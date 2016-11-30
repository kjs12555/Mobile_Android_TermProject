package kr.ac.kookmin.cs.termproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

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
    DBHelper helper;
    SQLiteDatabase db;
    int bigSpinnerPosition;
    ListView textList;
    LogSaveAdapter adapter;
    ArrayList<LogSave> dataArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_list, container, false);
        helper = new DBHelper(v.getContext(), "TermProject.db", null, 1);
        db = helper.getWritableDatabase();
        textList = (ListView) v.findViewById(R.id.list_list);
        dataArrayList = new ArrayList<>();
        adapter = new LogSaveAdapter(dataArrayList, getLayoutInflater(savedInstanceState), helper, db);
        textList.setAdapter(adapter);
        dataBig = new ArrayList<>();
        dataBig.add("All");
        dataBig.add("Event");
        dataBig.add("Log");
        adapterBig = new ArrayAdapter<>(v.getContext(), R.layout.support_simple_spinner_dropdown_item, dataBig);
        spinnerBig = (Spinner) v.findViewById(R.id.list_big_spinner);
        spinnerBig.setAdapter(adapterBig);
        spinnerSmall = (Spinner) v.findViewById(R.id.list_small_spinner);
        spinnerBig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, View view, int position, long l) {
                dataSmall = new ArrayList<>();
                Cursor rs;
                bigSpinnerPosition = position;
                //position에 따라 전체 Log이름으로 초기화하거나 Event의 이름으로 초기화 하거나 Log별로 초기화 합니다. DB를 작성하면 마저 작성할 예정
                switch (position) {
                    case 0:
                        dataSmall.add("All");
                        break;
                    case 1:
                        rs = db.rawQuery("select Distinct(EventName) from Log;", null);
                        while (rs.moveToNext()) {
                            dataSmall.add(rs.getString(0));
                        }
                        break;
                    case 2:
                        rs = db.rawQuery("select Distinct(LogName) from Log;", null);
                        while (rs.moveToNext()) {
                            String name = rs.getString(0);
                            dataSmall.add(name);
                        }
                        break;
                }
                adapterSmall = new ArrayAdapter<>(view.getContext(), R.layout.support_simple_spinner_dropdown_item, dataSmall);
                spinnerSmall.setAdapter(adapterSmall);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerSmall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //DB에서 해당하는 값을 가져와서 List를 지우고 다시 작성한다. DB작업을 마치고 작성할 예정입니다.
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
                while (rs.moveToNext()) {
                    dataArrayList.add(new LogSave(rs.getInt(0), rs.getDouble(1), rs.getDouble(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9), rs.getLong(10), rs.getString(11)));
                }
                textList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final Button statistics = (Button) v.findViewById(R.id.statistics);
        final Button typeButton = (Button) v.findViewById(R.id.type);
        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeButton.getText().toString().equals("Text")) {
                    typeButton.setText("Graph");
                    typeText = false;
                    statistics.setVisibility(View.VISIBLE);
                } else {
                    typeButton.setText("Text");
                    typeText = true;
                    statistics.setVisibility(View.INVISIBLE);
                }
            }
        });

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Graph에서 출력하는 부분.
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode==1){
                Toast.makeText(this.v.getContext(), data.getDataString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
