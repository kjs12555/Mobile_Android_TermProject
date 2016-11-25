package kr.ac.kookmin.cs.termproject;

import android.content.Context;
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

    public EventDataAdapter(LayoutInflater inflater, ArrayList<EventData> data){
        dataArrayList = data;
        this.inflater = inflater;
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

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonStart.getText().toString().equals("Start")){
                    buttonStart.setText("End");
                    buttonEdit.setVisibility(View.VISIBLE);
                    buttonDelete.setVisibility(View.INVISIBLE);
                }else{
                    buttonStart.setText("Start");
                    buttonEdit.setVisibility(View.INVISIBLE);
                    buttonDelete.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<dataArrayList.size(); i++){
                    if(dataArrayList.get(i).getName().equals(textName.getText().toString())){
                        dataArrayList.remove(i);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        // 셀 데이터 가저오기
        EventData data = dataArrayList.get(position);
        textName.setText(data.getName());

        return convertView;
    }
}
