package kr.ac.kookmin.cs.termproject;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    ActionBar mActionBar;
    ActionBar.Tab taskTab, mapTab, listTab, goalTab;

    Fragment mFragment;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    DBHelper helper;
    SQLiteDatabase db;

    static Activity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = this;

        helper = new DBHelper(this, "TermProject.db" ,null, 1);
        db = helper.getWritableDatabase();

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        View mActionBarLayout = getLayoutInflater().inflate(R.layout.actionbar_main,null);
        mActionBar.setCustomView(mActionBarLayout);

        //Task 탭
        taskTab = mActionBar.newTab();
        taskTab.setText("Task");
        taskTab.setTabListener(this);
        //Map 탭
        mapTab = mActionBar.newTab();
        mapTab.setText("Map");
        mapTab.setTabListener(this);
        //List 탭
        listTab = mActionBar.newTab();
        listTab.setText("List");
        listTab.setTabListener(this);
        //Goal 탭
        goalTab = mActionBar.newTab();
        goalTab.setText("Goal");
        goalTab.setTabListener(this);
        //최종 등록
        mActionBar.addTab(taskTab);
        mActionBar.addTab(mapTab);
        mActionBar.addTab(listTab);
        mActionBar.addTab(goalTab);
        //탭 구현 종료
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        int position = tab.getPosition();

        switch (position) {
            case 0:
                mFragment = new FragmentTask();
                break;
            case 1:
                mFragment = new FragmentMap();
                break;
            case 2:
                mFragment = new FragmentList();
                break;
            case 3:
                mFragment = new FragmentGoal();
                break;
        }
        ft.replace(R.id.fragmentContainer,mFragment);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {}

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {}

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int tabPosition = intent.getIntExtra("tabPosition",-1);
        int resultCode = intent.getIntExtra("resultCode", 0);

        if(tabPosition==0){
            FragmentTask task = (FragmentTask) mFragment;
            if(resultCode==1) {    //이벤트 이름 수정
                int position = intent.getIntExtra("eventPosition", -1);
                if (position != -1) {
                    String name = intent.getStringExtra("modifyName");
                    EventData data = task.mEventDataAdapter.dataArrayList.get(position);
                    data.setName(name);
                    task.mEventDataAdapter.notifyDataSetChanged();
                    int id = data.getId();
                    String sql = "update Event set Name=? where id=?;";
                    db.execSQL(sql, new Object[]{name, id});
                } else {
                    Toast.makeText(this, "position을 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }else if(tabPosition==2){   //List탭 관련으로 호출된 경우
            FragmentList list = (FragmentList) mFragment;
            if(resultCode==1){  //Note 사진 수정
                int position = intent.getIntExtra("eventPosition", -1);
                if(position!=-1){
                    String modifyUri = intent.getStringExtra("modifyUri");
                    LogSave data = list.adapter.dataArrayList.get(position);
                    data.setCameraPath(modifyUri);
                    list.adapter.notifyDataSetChanged();
                    int id = data.getId();
                    String sql = "update Log set CameraPath=? where id=?;";
                    db.execSQL(sql, new Object[]{modifyUri, id});
                }else{
                    Toast.makeText(this, "position을 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
