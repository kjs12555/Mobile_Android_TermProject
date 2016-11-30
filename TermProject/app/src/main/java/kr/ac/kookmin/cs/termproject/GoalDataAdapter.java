package kr.ac.kookmin.cs.termproject;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GoalDataAdapter extends BaseAdapter {
    ArrayList<GoalData> dataArrayList;
    LayoutInflater inflater;
    GoalDataAdapter adapter = this;
    DBHelper helper;
    SQLiteDatabase db;
    Activity mActivity;
    
    public GoalDataAdapter(LayoutInflater inflater, ArrayList<GoalData> data, DBHelper helper, SQLiteDatabase db, Activity ac){
        dataArrayList = data;
        this.inflater = inflater;
        this.helper = helper;
        this.db = db;
        mActivity = ac;
    }

    @Override
    public int getCount() {
        return dataArrayList.size();
    }

    @Override
    public Object getItem(int position){
        return dataArrayList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(R.layout.goal_row,null);
        }

        final TextView savePosition = (TextView)convertView.findViewById(R.id.goal_position);
        final TextView textName = (TextView)convertView.findViewById(R.id.goal_name);
        final TextView startEndText = (TextView)convertView.findViewById(R.id.start_end_text);
        final Button buttonDelete = (Button)convertView.findViewById(R.id.goal_del);
        final RelativeLayout layout = (RelativeLayout)convertView.findViewById(R.id.goal_layout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(savePosition.getText().toString());
                Intent intent = new Intent(mActivity, GoalResult.class);
                intent.putExtra("position", position);
                intent.putExtra("goalSave", dataArrayList.get(position));
                mActivity.startActivity(intent);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(savePosition.getText().toString());
                int minId = dataArrayList.get(position).getDatas().get(0).getId();
                int size = dataArrayList.get(position).getDatas().size();
                String sql = "Delete from Goal where id >= ? and id < ?;";
                db.execSQL(sql,new Object[]{minId, minId+size});
                dataArrayList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        GoalData data = dataArrayList.get(position);
        textName.setText(data.getGoalName());
        savePosition.setText(Integer.toString(position));
        startEndText.setText(data.getStart()+" ~ "+data.getEnd());

        return convertView;
    }
}
