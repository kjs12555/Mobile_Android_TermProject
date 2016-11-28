package kr.ac.kookmin.cs.termproject;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by user on 2016-11-28.
 */

public class GoalResult extends Activity {

    DBHelper helper;
    SQLiteDatabase db;
    ArrayList<GoalSave> goalSaves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_result);

        Intent intent = getIntent();

        int position = intent.getIntExtra("position",-1);
        GoalData data = (GoalData)intent.getSerializableExtra("goalSave");

        if(position == -1 || data==null){
            Log.d("testResult",Integer.toString(position));
            Toast.makeText(this, "Position의 값을 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            String goalName = data.getGoalName();
            helper = new DBHelper(this, "TermProject.db", null, 1);
            db = helper.getWritableDatabase();
            goalSaves = data.getDatas();
            //goalSaves와 db를 사용해서 결과값을 GoalResult_resultAdapter에 보내준다.

            ListView goalList = (ListView) findViewById(R.id.goal_list);
            ListView resultList = (ListView) findViewById(R.id.result_list);
            GoalResult_GoalAdapter goalAdapter = new GoalResult_GoalAdapter(goalSaves, getLayoutInflater());
            GoalResult_ResultAdapter resultAdapter = new GoalResult_ResultAdapter();
            goalList.setAdapter(goalAdapter);
            resultList.setAdapter(resultAdapter);

            TextView title = (TextView)findViewById(R.id.result_goal_name);
            title.setText(goalName);
        }
    }
}
