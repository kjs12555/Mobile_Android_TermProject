package kr.ac.kookmin.cs.termproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by user on 2016-11-26.
 */

public class AddGoal extends Activity {
    ArrayList<GoalSave> datas;
    ListView mListView;
    AddGoalDataAdapter goalDataAdapter;
    DBHelper helper;
    SQLiteDatabase db;
    TextView startText;
    static TextView endText;
    static boolean endFlag = false;
    static int endType = -1;
    static int[] maxDay = {0,31,28,31,30,31,30,31,31,30,31,30,31};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_goal);
        final Button complete = (Button) findViewById(R.id.add_goal_complete);
        final Button addGoalButton = (Button)findViewById(R.id.add_goal_add);
        helper = new DBHelper(this, "TermProject.db" ,null, 1);
        db = helper.getWritableDatabase();
        startText = (TextView)findViewById(R.id.start_text);
        endText = (TextView)findViewById(R.id.end_text);
        endFlag = false;
        endType = -1;

        startText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddGoal.this, CalendarActivity.class);
                intent.putExtra("startDateFlag", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivityForResult(intent, 2);
            }
        });

        endText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!endFlag) {
                    Intent intent = new Intent(AddGoal.this, CalendarActivity.class);
                    intent.putExtra("startDateFlag", false);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivityForResult(intent, 2);
                }
            }
        });

        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddGoal.this, AddGoalForm.class);
                intent.putExtra("Flag", endFlag);
                startActivityForResult(intent, 1);
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                EditText name = (EditText) findViewById(R.id.add_goal_editText);
                String goalName = name.getText().toString();
                String start = startText.getText().toString();
                String end = endText.getText().toString();
                if(goalName.equals("")){
                    Toast.makeText(v.getContext(), "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(datas.size()==0){
                    Toast.makeText(v.getContext(), "목표가 1개 이상 존재해야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(start.equals("클릭")){
                    Toast.makeText(v.getContext(), "시작 날짜를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(end.equals("클릭")){
                    Toast.makeText(v.getContext(), "종료 날짜를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                int startInt = Integer.parseInt(start);
                int endInt = -1;
                if(endFlag){    //날짜 자동완성 기능
                    endInt = moveDay(startInt, endType);
                }else{
                    endInt = Integer.parseInt(end);
                }
                String sql = "select Max(ID) from Goal;";
                Cursor rs = db.rawQuery(sql, null);
                rs.moveToNext();
                intent.putExtra("maxId", rs.getInt(0));
                int idx = datas.size()-1;
                datas.get(idx).setType(datas.get(idx).getType()+1000);
                for(GoalSave i : datas){
                    sql = "insert into Goal(GoalName, EventName, Type, N, Start, End) values(?, ?, ?, ?, ?, ?);";
                    db.execSQL(sql, new Object[]{goalName, i.getEventName(), i.getType(), i.getN(), startInt, endInt});
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        datas = new ArrayList<GoalSave>();
        mListView = (ListView)findViewById(R.id.add_goal_list);
        goalDataAdapter = new AddGoalDataAdapter(getLayoutInflater(), datas);
        mListView.setAdapter(goalDataAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode==1){
                int rN = data.getExtras().getInt("N");
                String rEventName = data.getExtras().getString("eventName");
                int rType = data.getExtras().getInt("Type");
                if(rType/100 == 2){
                    endFlag = true;
                    endType = rType%100/10;
                    if(startText.getText().toString().equals("클릭")) {
                        endText.setText("자동완성");
                    }else{
                        int startInt = Integer.parseInt(startText.getText().toString());
                        endText.setText(Integer.toString(moveDay(startInt, endType)));
                    }
                }
                goalDataAdapter.dataArrayList.add(new GoalSave(-1, rEventName, rType, rN));
                goalDataAdapter.notifyDataSetChanged();
            }else if(requestCode==2){
                if(data.getBooleanExtra("startDateFlag",false)){
                    startText.setText(data.getStringExtra("date"));
                    if(endFlag){
                        int startInt = Integer.parseInt(startText.getText().toString());
                        endText.setText(Integer.toString(moveDay(startInt, endType)));
                    }
                }else{
                    endText.setText(data.getStringExtra("date"));
                }
            }
        }
    }
    public int moveDay(int startDate, int type){    //type : 0-하루, 1-일주일, 2-한 달
        int res=startDate;
        int year = startDate/10000;
        int month = startDate%10000/100;
        int day = startDate%100;
        boolean leapFlag = year%400==0 || (year%4==0 && year%100!=0);

        if(type==2){
            if(month==12) {
                month = 0;
                year++;
            }
            month++;
            if(leapFlag && month==2){
                if(day>maxDay[month]+1){
                    day = maxDay[month]+1;
                }
            }else{
                if(day > maxDay[month]){
                    day = maxDay[month];
                }
            }
        }else{
            day += type*6+1;
            if(day>maxDay[month]){
                day-=maxDay[month++];
                if(month==13){
                    month=1;
                    year++;
                }
            }
        }
        res = year*10000+month*100+day;

        return res;
    }
}
