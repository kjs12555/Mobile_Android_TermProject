package kr.ac.kookmin.cs.termproject;

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

    public GoalDataAdapter(LayoutInflater inflater, ArrayList<GoalData> data){
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
            convertView=inflater.inflate(R.layout.goal_row,null);
        }

        //텍스트뷰  참조
        final TextView textName = (TextView) convertView.findViewById(R.id.name);
        final Button buttonDelete = (Button) convertView.findViewById(R.id.del);
        final RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.goal_layout);

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

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<dataArrayList.size(); i++){
                    if(dataArrayList.get(i).getName().equals(textName.getText().toString())){
                        //클릭을 한 목표와 같은 이름을 가진 Array를 찾는다. 새로운 Activity를 띄워서 목표에 대한 정보를 보여준다.
                        Toast.makeText(v.getContext(), dataArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 셀 데이터 가저오기
        GoalData data = dataArrayList.get(position);
        textName.setText(data.getName());

        return convertView;
    }
}
