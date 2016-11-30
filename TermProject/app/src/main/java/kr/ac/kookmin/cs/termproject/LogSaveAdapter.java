package kr.ac.kookmin.cs.termproject;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by user on 2016-11-29.
 */

public class LogSaveAdapter extends BaseAdapter{
    ArrayList<LogSave> dataArrayList;
    LayoutInflater inflater;
    DBHelper helper;
    SQLiteDatabase db;

    public LogSaveAdapter(ArrayList<LogSave> dataArrayList, LayoutInflater inflater, DBHelper helper, SQLiteDatabase db) {
        this.dataArrayList = dataArrayList;
        this.inflater = inflater;
        this.helper = helper;
        this.db = db;
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
        String text = "";

        ImageView image = (ImageView)convertView.findViewById(R.id.log_imageView);

        image.setVisibility(View.INVISIBLE);
        if(type==0 || type==3){
            if(type==0){    //시작
                text = text.concat("시작  : "+data.getEventName()+"\n");
                text = text.concat("Log이름 : "+data.getLogName()+"\n");
                text = text.concat("시작 시간 : "+data.getTime());
            }else if(type==3){
                text = text.concat("종료  : "+data.getEventName()+"   ");
                text = text.concat("Log이름 : "+data.getLogName()+"\n");
                text = text.concat("종료 시간 : "+data.getTime()+"\n");
                text = text.concat("타이머 : "+data.getTimeGap()+"초"+"   ");
                text = text.concat("걸음 횟수 : "+Integer.toString(data.getFoot()));
            }
            general.setText(text);
            general.setTextSize(13);
        }else{  //위치정보, 시간, 이미지 경로, 노트, 걸음 횟수 출력
            text = text.concat("위도:"+Double.toString(data.getLatitude())+"  경도:"+Double.toString(data.getLongitude())+"\n");
            text = text.concat("시간:"+data.getTime()+"  ");
            if(data.getType()==2){
                image.setVisibility(View.VISIBLE);
                text = text.concat("\n노트이름:"+data.getNoteName());
                text = text.concat("  노트:"+data.getNote()+"\n");
                try {
                    image.setImageURI(Uri.parse(data.getCameraPath()));
                }catch (Exception e){
                    data.setCameraPath("");
                    db.execSQL("update Log set CameraPath='' where id=?",new Object[]{data.getId()});
                }
            }
            text = text.concat("  걸은 횟수:"+Integer.toString(data.getFoot()));
            general.setText(text);
            general.setTextSize(11);
        }
        return convertView;
    }
}