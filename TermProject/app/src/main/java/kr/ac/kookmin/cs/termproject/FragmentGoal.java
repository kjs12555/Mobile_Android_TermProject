package kr.ac.kookmin.cs.termproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class FragmentGoal extends Fragment {
    ArrayList<GoalData> datas;
    ListView mListView;
    GoalDataAdapter goalDataAdapter;
    DBHelper helper;
    SQLiteDatabase db;
    View v;
    Button addGoal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_goal,container,false);

        addGoal = (Button)v.findViewById(R.id.add_goal);
        addGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddGoal.class);
                startActivityForResult(intent, 1);
            }
        });

        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        helper = new DBHelper(v.getContext(), "TermProject.db" ,null, 1);
        db = helper.getWritableDatabase();
        getDatasToDB();

        mListView = (ListView)getActivity().findViewById(R.id.goal_view);
        goalDataAdapter = new GoalDataAdapter(getLayoutInflater(savedInstanceState), datas, helper, db, getActivity());
        mListView.setAdapter(goalDataAdapter);

    }

    public void getDatasToDB(){
        datas = new ArrayList<GoalData>();
        ArrayList<GoalSave> datasTmp = new ArrayList<GoalSave>();
        Cursor rs = db.rawQuery("select * from Goal order by id;", null);
        while(rs.moveToNext()){
            int type = rs.getInt(3);
            datasTmp.add(new GoalSave(rs.getInt(0), rs.getString(2), type, rs.getInt(4)));
            if(type > 1000){
                datas.add(new GoalData(rs.getString(1),datasTmp, rs.getString(5), rs.getString(6)));
                datasTmp = new ArrayList<GoalSave>();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode==1){
                int maxId = data.getIntExtra("maxId",0);
                ArrayList<GoalSave> datasTmp = new ArrayList<GoalSave>();
                String sql = "select * from Goal where id > "+ Integer.toString(maxId) +" order by id;";
                Cursor rs = db.rawQuery(sql, null);
                rs.moveToNext();
                String goalName = rs.getString(1);
                String start = rs.getString(5);
                String end = rs.getString(6);
                do{
                    datasTmp.add(new GoalSave(rs.getInt(0), rs.getString(2), rs.getInt(3), rs.getInt(4)));
                }while(rs.moveToNext());
                goalDataAdapter.dataArrayList.add(new GoalData(goalName, datasTmp, start, end));
                goalDataAdapter.notifyDataSetChanged();
            }
        }
    }
}
