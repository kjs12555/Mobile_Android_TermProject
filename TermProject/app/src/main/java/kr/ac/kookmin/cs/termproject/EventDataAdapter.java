package kr.ac.kookmin.cs.termproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
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

    public EventDataAdapter(LayoutInflater inflater, ArrayList<EventData> data, DBHelper helper, SQLiteDatabase db){
        dataArrayList = data;
        this.inflater = inflater;
        this.helper = helper;
        this.db = db;
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
        final TextView id = (TextView) convertView.findViewById(R.id.event_id);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonStart.getText().toString().equals("Start") && !startFlag){
                    startFlag = true;
                    buttonStart.setText("End");
                    buttonEdit.setVisibility(View.VISIBLE);
                    buttonDelete.setVisibility(View.INVISIBLE);
                    String sql = "update Event set Type=1 where id=?;";
                    db.execSQL(sql, new Object[]{Integer.parseInt(id.getText().toString())});
                }else if(buttonStart.getText().toString().equals("End")){
                    startFlag = false;
                    buttonStart.setText("Start");
                    buttonEdit.setVisibility(View.INVISIBLE);
                    buttonDelete.setVisibility(View.VISIBLE);
                    String sql = "update Event set Type=0 where id=?;";
                    db.execSQL(sql, new Object[]{Integer.parseInt(id.getText().toString())});
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startFlag) {
                    for (int i = 0; i < dataArrayList.size(); i++) {
                        if (dataArrayList.get(i).getId() == Integer.parseInt(id.getText().toString())) {
                            String sql = "Delete from Event where ID=?;";
                            db.execSQL(sql, new Object[]{dataArrayList.get(i).getId()});
                            dataArrayList.remove(i);
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }
        });

        // 셀 데이터 가저오기
        EventData data = dataArrayList.get(position);
        textName.setText(data.getName());
        id.setText(Integer.toString(data.getId()));
        if(data.getType()==1){
            startFlag = true;
            buttonStart.setText("End");
            buttonEdit.setVisibility(View.VISIBLE);
            buttonDelete.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
