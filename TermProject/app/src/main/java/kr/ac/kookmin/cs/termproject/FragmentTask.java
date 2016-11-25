package kr.ac.kookmin.cs.termproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class FragmentTask extends Fragment {
    ArrayList<EventData> datas = new ArrayList<EventData>();
    ListView mListView;
    EventDataAdapter mEventDataAdapter;
    LayoutInflater save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_task,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        datas.add(new EventData("게임"));
        datas.add(new EventData("이동"));
        datas.add(new EventData("휴식"));
        datas.add(new EventData("과제"));
        datas.add(new EventData("밥"));
        datas.add(new EventData("간식"));
        datas.add(new EventData("영화"));

        mListView = (ListView)getActivity().findViewById(R.id.event_view);
        save = getLayoutInflater(savedInstanceState);
        mEventDataAdapter = new EventDataAdapter(save, datas);
        mListView.setAdapter(mEventDataAdapter);
    }
}
