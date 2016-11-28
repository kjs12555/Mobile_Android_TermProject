package kr.ac.kookmin.cs.termproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by user on 2016-11-27.
 */

class AddGoalDataAdapter extends BaseAdapter {
    ArrayList<GoalSave> dataArrayList;
    LayoutInflater mInflater;
    AddGoalDataAdapter adapter = this;

    public AddGoalDataAdapter(LayoutInflater inflater, ArrayList<GoalSave> data){
        dataArrayList = data;
        mInflater = inflater;
    }

    @Override
    public int getCount() { return dataArrayList.size(); }

    @Override
    public Object getItem(int position) { return dataArrayList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.add_goal_row,null);
        }

        final Button buttonDelete = (Button) convertView.findViewById(R.id.add_goal_del);
        final TextView saveText = (TextView) convertView.findViewById(R.id.add_goal_text);
        final TextView savePosition = (TextView) convertView.findViewById(R.id.add_goal_position);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(savePosition.getText().toString());
                if(dataArrayList.get(position).getType()%1000/100==2){
                    AddGoal.endFlag = false;
                    AddGoal.endText.setText("클릭");
                }
                dataArrayList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

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
