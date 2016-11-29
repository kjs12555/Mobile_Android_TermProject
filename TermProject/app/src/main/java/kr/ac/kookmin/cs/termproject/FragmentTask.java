package kr.ac.kookmin.cs.termproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class FragmentTask extends Fragment{
    ArrayList<EventData> datas;
    ListView mListView;
    EventDataAdapter mEventDataAdapter;
    LayoutInflater save;
    DBHelper helper;
    SQLiteDatabase db;
    View v;
    Button addEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_task, container, false);

        addEvent = (Button)v.findViewById(R.id.add_event);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddEvent.class);
                startActivityForResult(intent, 1);
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        helper = new DBHelper(v.getContext(), "TermProject.db" ,null, 1);
        db = helper.getWritableDatabase();
        getDatasToDB();

        mListView = (ListView)getActivity().findViewById(R.id.event_view);
        save = getLayoutInflater(savedInstanceState);
        mEventDataAdapter = new EventDataAdapter(save, datas, helper, db, getActivity());
        mListView.setAdapter(mEventDataAdapter);
    }

    public void getDatasToDB(){
        datas = new ArrayList<EventData>();
        Cursor rs = db.rawQuery("select * from Event order by id;", null);
        while(rs.moveToNext()){
            datas.add(new EventData(rs.getInt(0), rs.getString(1), rs.getInt(2)));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode==1){
                String name = data.getStringExtra("eventName");
                String sql = "Insert Into Event(Name, Type) values(?, 0);";
                db.execSQL(sql,new Object[]{name});
                Cursor rs = db.rawQuery("select Max(ID) from Event",null);
                rs.moveToNext();
                EventData tmp = new EventData(rs.getInt(0), name, 0);
                mEventDataAdapter.dataArrayList.add(tmp);
                mEventDataAdapter.notifyDataSetChanged();
            }
        }
    }
}
