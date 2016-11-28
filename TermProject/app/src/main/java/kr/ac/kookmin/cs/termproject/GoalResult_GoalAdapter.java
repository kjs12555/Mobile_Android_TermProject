package kr.ac.kookmin.cs.termproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2016-11-28.
 */

public class GoalResult_GoalAdapter extends BaseAdapter {

    ArrayList<GoalSave> dataArrayList;
    LayoutInflater inflater;
    GoalResult_GoalAdapter adapter = this;

    GoalResult_GoalAdapter(ArrayList<GoalSave> dataArrayList, LayoutInflater inflater){
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
        return dataArrayList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(R.layout.add_goal_row, null);
        }

        final Button buttonDelete = (Button) convertView.findViewById(R.id.add_goal_del);
        final TextView saveText = (TextView) convertView.findViewById(R.id.add_goal_text);
        final TextView savePosition = (TextView) convertView.findViewById(R.id.add_goal_position);

        buttonDelete.setVisibility(View.INVISIBLE);

        GoalSave data = dataArrayList.get(position);
        String text = data.getEventName()+"을 ";
        int type = data.getType();
        int firstType = (type%1000)/100;
        int secondType = (type%100)/10;
        int thirdType = type%10;

        switch (firstType){
            case 1:
                text = text.concat(Integer.toString(data.getN())+" ");
                switch (secondType){
                    case 0:
                        text = text.concat("시간 ");
                        break;
                    case 1:
                        text = text.concat("분 ");
                        break;
                    case 2:
                        text = text.concat("초 ");
                        break;
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
                text = text.concat(Integer.toString(data.getN())+"번 ");
                break;
        }
        switch (thirdType){
            case 0:
                text = text.concat("이상");
                break;
            case 1:
                text = text.concat("이하");
                break;
        }
        text = text.concat("한다");

        saveText.setText(text);

        savePosition.setText(Integer.toString(position));

        return convertView;
    }
}
