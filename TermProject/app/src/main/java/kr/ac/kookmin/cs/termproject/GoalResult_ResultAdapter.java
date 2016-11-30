package kr.ac.kookmin.cs.termproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by user on 2016-11-28.
 */

public class GoalResult_ResultAdapter extends BaseAdapter {

    GoalData dataArrayList;
    LayoutInflater inflater;
    DBHelper helper;
    SQLiteDatabase db;

    public GoalResult_ResultAdapter(GoalData dataArrayList, LayoutInflater inflater, DBHelper helper, SQLiteDatabase db) {
        this.dataArrayList = dataArrayList;
        this.inflater = inflater;
        this.helper = helper;
        this.db = db;
    }

    @Override
    public int getCount() {
        return dataArrayList.getDatas().size();
    }

    @Override
    public Object getItem(int position) { return dataArrayList.getDatas().get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(R.layout.add_goal_row, null);
        }

        final Button buttonDelete = (Button) convertView.findViewById(R.id.add_goal_del);
        final TextView saveText = (TextView) convertView.findViewById(R.id.add_goal_text);
        final TextView savePosition = (TextView) convertView.findViewById(R.id.add_goal_position);

        savePosition.setText(Integer.toString(position));

        buttonDelete.setVisibility(View.INVISIBLE);

        GoalSave data = dataArrayList.getDatas().get(position);

        String text = data.getEventName()+"을 ";
        int type = data.getType();
        int firstType = (type%1000)/100;
        int secondType = (type%100)/10;
        int thirdType = type%10;

        Cursor rs=null;
        int NCompare=0, N =0, tmp=0;
        switch (firstType){
            case 1:
                //한 시간을 받아와서 초 단위를 시/분/초로 바꿔줍니다.
                Toast.makeText(convertView.getContext(), dataArrayList.getStart()+" "+dataArrayList.getEnd(), Toast.LENGTH_SHORT).show();
                rs = db.rawQuery("select Sum(TimeGap) from Log where EventName=? and Time>=? and Time<Date(?, '+1 day');",new String[]{data.getEventName(),  dataArrayList.getStart(), dataArrayList.getEnd()});
                rs.moveToNext();
                N = rs.getInt(0);
                rs.close();
                NCompare = N;
                if(N>=3600){
                    tmp = N/(60*60);
                    N -= tmp*60*60;
                    text=text.concat(Integer.toString(tmp)+"시간 ");
                }
                if(N>=60){
                    tmp = N/60;
                    N -= tmp*60;
                    text=text.concat(Integer.toString(tmp)+"분 ");
                }
                if(N>=0){
                    text=text.concat(Integer.toString(N%60)+"초");
                }
                switch (secondType){
                    case 0:
                        NCompare/=60;
                    case 1:
                        NCompare/=60;
                }
                break;
            case 2:
                switch (secondType){
                    case 0:
                        text = text.concat("하루에 ");
                        break;
                    case 1:
                        text = text.concat("일주일에 ");
                        break;
                    case 2:
                        text = text.concat("한 달에 ");
                        break;
                }
                rs = db.rawQuery("select Count(*) from Log where EventName=? and Time>=? and Time<Date(?, '+1 day');",new String[]{data.getEventName(), dataArrayList.getStart(), dataArrayList.getEnd()});
                rs.moveToNext();
                NCompare = rs.getInt(0);
                rs.close();
                text = text.concat(NCompare+"번 ");
                break;
        }
        text = text.concat("했습니다.\n");
        switch (thirdType){
            case 0:
                if(NCompare>=data.getN()){
                    text = text.concat("목표를 달성하였습니다!");
                }else{
                    text = text.concat("목표를 달성하지 못하였습니다 8ㅅ8");
                }
                break;
            case 1:
                if(NCompare<=data.getN()){
                    text = text.concat("목표를 달성하였습니다!");
                }else{
                    text = text.concat("목표를 달성하지 못하였습니다 8ㅅ8");
                }
        }
        saveText.setText(text);
        return convertView;
    }
}
