package kr.ac.kookmin.cs.termproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class FragmentGoal extends Fragment {
    ArrayList<GoalData> datas = new ArrayList<GoalData>();
    ListView mListView;
    GoalDataAdapter mEventDataAdapter;
    LayoutInflater save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_goal,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        datas.add(new GoalData("목표1"));
        datas.add(new GoalData("목표2"));
        datas.add(new GoalData("목표3"));
        datas.add(new GoalData("목표4"));
        datas.add(new GoalData("목표5"));

        mListView = (ListView)getActivity().findViewById(R.id.goal_view);
        save = getLayoutInflater(savedInstanceState);
        mEventDataAdapter = new GoalDataAdapter(save, datas);
        mListView.setAdapter(mEventDataAdapter);

    }
}
