package kr.ac.kookmin.cs.termproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class EventDataAdapter extends BaseAdapter {
    ArrayList<EventData> dataArrayList;
    LayoutInflater inflater;
    EventDataAdapter adapter = this;
    DBHelper helper;
    SQLiteDatabase db;
    boolean startFlag=false;
    Activity mActivity;

    public EventDataAdapter(LayoutInflater inflater, ArrayList<EventData> data, DBHelper helper, SQLiteDatabase db, Activity ac){
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
            convertView=inflater.inflate(R.layout.event_row,null);
        }

        //텍스트뷰  참조
        final TextView textName = (TextView) convertView.findViewById(R.id.name);
        final Button buttonStart = (Button) convertView.findViewById(R.id.start);
        final Button buttonDelete = (Button) convertView.findViewById(R.id.del);
        final Button buttonEdit = (Button) convertView.findViewById(R.id.edit);
        final TextView savePosition = (TextView) convertView.findViewById(R.id.event_position);
        final RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.event_layout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, AddEvent.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("flagModify", true);
                intent.putExtra("eventPosition", Integer.parseInt(savePosition.getText().toString()));
                mActivity.startActivity(intent);
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonStart.getText().toString().equals("Start") && !startFlag){
                    startFlag = true;
                    int position = Integer.parseInt(savePosition.getText().toString());
                    dataArrayList.get(position).setType(1);
                    String sql = "update Event set Type=1 where id=?;";
                    db.execSQL(sql, new Object[]{dataArrayList.get(position).getId()});
                    adapter.notifyDataSetChanged();
                    Intent intent = new Intent(mActivity, LogService.class);
                    mActivity.startService(intent);
                }else if(buttonStart.getText().toString().equals("End")){
                    startFlag = false;
                    int position = Integer.parseInt(savePosition.getText().toString());
                    dataArrayList.get(position).setType(0);
                    String sql = "update Event set Type=0 where id=?;";
                    db.execSQL(sql, new Object[]{dataArrayList.get(position).getId()});
                    adapter.notifyDataSetChanged();
                    Intent intent = new Intent(mActivity, LogService.class);
                    mActivity.stopService(intent);
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startFlag) {
                    int position = Integer.parseInt(savePosition.getText().toString());
                    String sql = "Delete from Event where ID=?;";
                    db.execSQL(sql, new Object[]{dataArrayList.get(position).getId()});
                    dataArrayList.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        // 셀 데이터 가저오기
        EventData data = dataArrayList.get(position);
        textName.setText(data.getName());
        savePosition.setText(Integer.toString(position));
        if(data.getType()==1){
            startFlag = true;
            buttonStart.setText("End");
            buttonEdit.setVisibility(View.VISIBLE);
            buttonDelete.setVisibility(View.INVISIBLE);
        }else{
            buttonStart.setText("Start");
            buttonEdit.setVisibility(View.INVISIBLE);
            buttonDelete.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
