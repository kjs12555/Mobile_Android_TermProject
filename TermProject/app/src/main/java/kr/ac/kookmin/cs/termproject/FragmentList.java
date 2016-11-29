package kr.ac.kookmin.cs.termproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class FragmentList extends Fragment implements AbsListView.OnScrollListener {

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
        adapter = new LogSaveAdapter(dataArrayList, getLayoutInflater(savedInstanceState));
        textList.setAdapter(adapter);
        checkBottomOfScroll(textList);
        dataBig = new ArrayList<>();
        dataBig.add("Event");
        dataBig.add("Log");
        adapterBig = new ArrayAdapter<>(v.getContext(), R.layout.support_simple_spinner_dropdown_item, dataBig);
        spinnerBig = (Spinner) v.findViewById(R.id.list_big_spinner);
        spinnerBig.setAdapter(adapterBig);
        spinnerBig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, View view, int position, long l) {
                spinnerSmall = (Spinner) v.findViewById(R.id.list_small_spinner);
                dataSmall = new ArrayList<>();
                Cursor rs;
                bigSpinnerPosition = position;
                //position에 따라 전체 Log이름으로 초기화하거나 Event의 이름으로 초기화 하거나 Log별로 초기화 합니다. DB를 작성하면 마저 작성할 예정
                switch (position) {
                    case 0:
                        rs = db.rawQuery("select Distinct(Name) from Event;", null);
                        while (rs.moveToNext()) {
                            dataSmall.add(rs.getString(0));
                        }
                        break;
                    case 1:
//                        rs = db.rawQuery("select Distinct(LogName) from Log;", null);
//                        while (rs.moveToNext()) {
//                            String name = rs.getString(0);
//                            dataSmall.add(name);
//                        }
//                        break;
                }
                adapterSmall = new ArrayAdapter<>(view.getContext(), R.layout.support_simple_spinner_dropdown_item, dataSmall);
                spinnerSmall.setAdapter(adapterSmall);
                spinnerSmall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //DB에서 해당하는 값을 가져와서 List를 지우고 다시 작성한다. DB작업을 마치고 작성할 예정입니다.
                        Cursor rs;
                        String args[] = new String[2];
                        if (bigSpinnerPosition == 0) {
                            args[0] = "EventName";
                        } else {
                            args[0] = "LogName";
                        }
                        args[1] = spinnerSmall.getSelectedItem().toString();
                        rs = db.rawQuery("select * from Log where " + args[0] + "='" + args[1] + "' order by ID", null);
                        dataArrayList.clear();
                        while (rs.moveToNext()) {
                            dataArrayList.add(new LogSave(rs.getInt(0), rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9)));
                        }
                        adapter.notifyDataSetChanged();
                        textList.setAdapter(adapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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

    boolean lastitemVisibleFlag;

    private void checkBottomOfScroll(final ListView listview) {
        //화면에 리스트의 마지막 아이템이 보여지는지 체크
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
                lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태입니다.
                //즉 스크롤이 바닦에 닿아 멈춘 상태에 처리를 하겠다는 뜻
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    Toast.makeText(FragmentList.this.v.getContext(), "화면의 마지막 입니다.", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private boolean mLockListView;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이
        // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다.
        int count = totalItemCount - visibleItemCount;

        if (firstVisibleItem >= count && totalItemCount != 0
                && mLockListView == false) {
            //데이터 추가
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    /**
     * 임의의 방법으로 더미 아이템을 추가합니다.
     *
     * @param size
     */
    private void addItems(final int size) {
        // 아이템을 추가하는 동안 중복 요청을 방지하기 위해 락을 걸어둡니다.
        mLockListView = true;


    }
}
