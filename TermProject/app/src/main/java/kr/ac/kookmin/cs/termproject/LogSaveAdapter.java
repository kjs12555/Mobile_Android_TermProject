package kr.ac.kookmin.cs.termproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by user on 2016-11-29.
 */

public class LogSaveAdapter extends BaseAdapter{
    ArrayList<LogSave> dataArrayList;
    LayoutInflater inflater;

    public LogSaveAdapter(ArrayList<LogSave> dataArrayList, LayoutInflater inflater) {
        this.dataArrayList = dataArrayList;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return dataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setDataArrayList(ArrayList<LogSave> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_row, null);
        }
        LogSave data = dataArrayList.get(position);
        int type = data.getType();

        TextView general = (TextView)convertView.findViewById(R.id.list_row_general);
        TextView startEnd = (TextView)convertView.findViewById(R.id.list_row_start_end);
        String text = "";

        if(type==0 || type==3){
            general.setVisibility(View.INVISIBLE);
            startEnd.setVisibility(View.VISIBLE);
            if(type==0){    //시작
                text = text.concat("시작  : "+data.getEventName()+"\n");
                text = text.concat("Log이름 : "+data.getLogName());
                text = text.concat("시작 시간 : "+data.getTime());
            }else if(type==3){
                text = text.concat("종료  : "+data.getEventName()+"\n");
                text = text.concat("Log이름 : "+data.getLogName());
                text = text.concat("종료 시간 : "+data.getTime()+"\n");
                text = text.concat("걸음 횟수 : "+Integer.toString(data.getFoot()));
            }
            startEnd.setText(text);
        }else{
            general.setText("위치정보, 시간, 이미지 경로, 노트, 걸음 횟수 출력");
        }
        return convertView;
    }
}