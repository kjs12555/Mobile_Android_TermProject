package kr.ac.kookmin.cs.termproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

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
    LinearLayout textView;
    LinearLayout graphView;

    PieChart mChart;
    boolean statisticsClicked= false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_list, container, false);
        helper = new DBHelper(v.getContext(), "TermProject.db", null, 1);
        db = helper.getWritableDatabase();

        textView = (LinearLayout)v.findViewById(R.id.list_text_view);
        graphView = (LinearLayout)v.findViewById(R.id.list_graph_view);

        textList = (ListView) v.findViewById(R.id.list_list);
        dataArrayList = new ArrayList<>();
        adapter = new LogSaveAdapter(dataArrayList, getLayoutInflater(savedInstanceState), helper, db, getActivity());
        textList.setAdapter(adapter);

        dataBig = new ArrayList<>();
        dataBig.add("All");
        dataBig.add("Event");
        dataBig.add("Log");
        adapterBig = new ArrayAdapter<>(v.getContext(), R.layout.support_simple_spinner_dropdown_item, dataBig);
        spinnerBig = (Spinner) v.findViewById(R.id.list_big_spinner);
        spinnerBig.setAdapter(adapterBig);

        spinnerSmall = (Spinner) v.findViewById(R.id.list_small_spinner);
        dataSmall = new ArrayList<>();
        adapterSmall = new ArrayAdapter<>(v.getContext(), R.layout.support_simple_spinner_dropdown_item, dataSmall);
        spinnerSmall.setAdapter(adapterSmall);

        spinnerBig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, View view, int position, long l) {
                dataSmall.clear();
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
                        rs.close();
                        break;
                    case 2:
                        rs = db.rawQuery("select Distinct(LogName) from Log;", null);
                        while (rs.moveToNext()) {
                            String name = rs.getString(0);
                            dataSmall.add(name);
                        }
                        rs.close();
                        break;
                }
                adapterSmall.notifyDataSetChanged();
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
                rs.close();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final Button typeButton = (Button) v.findViewById(R.id.type);
        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeButton.getText().toString().equals("텍스트")) {
                    typeButton.setText("그래프");
                    typeText = false;
                    textView.setVisibility(View.GONE);
                    graphView.setVisibility(View.VISIBLE);
                } else {
                    typeButton.setText("텍스트");
                    typeText = true;
                    textView.setVisibility(View.VISIBLE);
                    graphView.setVisibility(View.GONE);
                }
            }
        });

        final RelativeLayout chartLayout = (RelativeLayout)v.findViewById(R.id.chart_layout);
        mChart = new PieChart(v.getContext());    //PieChart에 대한 객체 설정. mainLayout에 넣을 값.

        final Button statistics = (Button) v.findViewById(R.id.statistics);
        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Graph에서 출력하는 부분.
                if(statisticsClicked){
                    return;
                }
                statisticsClicked = true;
                chartLayout.addView(mChart); //mainLayout에 mChart 추가.
                chartLayout.setBackgroundColor(Color.parseColor("#55656C")); //mainLayout에 배경색 설정.

                mChart.setUsePercentValues(true);
                mChart.setDescription("이벤트를 한 시간 통계");  //오른쪽 밑에 나타나는 PieChart 제목?

                mChart.setDrawHoleEnabled(true);    //중앙에 빈 원을 만들지에 대한 설정
                mChart.setHoleColorTransparent(true);  //중앙 빈 공간의 색깔을 비워둘지에 대한 설정. false일 경우 흰색으로 출력됨.
                mChart.setHoleRadius(5);    //중간 원의 빈 공간의 크기
                mChart.setTransparentCircleRadius(7);  //중간 원의 전체 크기

                mChart.setRotationAngle(0); //초기 각도 설정.
                mChart.setRotationEnabled(true);    //드래그 해서 돌리기 여부 설정

                mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() { //선택했을 때 발생하는 CallBack함수

                    @Override
                    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                        // display msg when value selected
                        if (e == null) {
                            return;
                        }

                        int data = (int)e.getVal();
                        String text="";
                        if(data>=3600){
                            text = text.concat(data%3600+"시간 ");
                            data/=3600;
                        }
                        if(data >= 60){
                            text = text.concat(data%60+"분 ");
                            data/=60;
                        }
                        text = text.concat(data+"초 하였습니다.");

                        Toast.makeText(v.getContext(),xVals.get(e.getXIndex()) + " = " +text, Toast.LENGTH_SHORT).show();    //e.getXIndex()를 하면 선택된 x의 인덱스를 가져온다. dataSetIndex는 뭐인지 잘 모르겠음. 이 예제에서는 0값만 리턴.
                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });

                FragmentList.this.addData();

                Legend l = mChart.getLegend();
                l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);    //데이터 종류를 보여주는 것 위치 지정.
                l.setXEntrySpace(7);
                l.setYEntrySpace(5);    //있는 데이터 종류를 보여주는 것에 대한 여백 설정.

            }
        });

        return v;
    }

    ArrayList<Entry> yVals1;
    ArrayList<String> xVals;

    public void addData(){
        yVals1 = new ArrayList<>();
        xVals = new ArrayList<>();

        Cursor rs = db.rawQuery("select EventName, Sum(TimeGap) from Log Group By EventName;",null);
        int i=0;
        while(rs.moveToNext()){
            yVals1.add(new Entry(rs.getFloat(1),i++));
            xVals.add(rs.getString(0));
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "Event Name");    //데이터의 정보와 오른쪽에 나오는 분류에 대한 정보를 넣습니다.
        dataSet.setSliceSpace(3);   //데이터간의 간격을 나타내어주는 가중치.
        dataSet.setSelectionShift(5);   //선택 했을 때, 얼마나 크기가 증가할지에 대한 가중치

        // add many colors
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);  //그래프에 사용될 색깔들을 추가합니다.

        PieData data = new PieData(xVals, dataSet); //mChart에 보여줄 것들 저장.
        data.setValueFormatter(new PercentFormatter()); //%로 데이터 포맷 설정.
        data.setValueTextSize(8f);  //데이터에 보여주는 데이터 이름과 백분율에 대한 크기.
        data.setValueTextColor(Color.GRAY); //데이터 이름과 백분율에 대한 색깔

        mChart.setData(data);   //Data를 View에 추가합니다.

        mChart.highlightValues(null);   //Highlight에 대한 정보를 담는 곳. 없으므로 null으로 넣습니다.

        mChart.animateXY(1000,1000);
        mChart.invalidate();    //새로고침
    }
}
