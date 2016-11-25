package kr.ac.kookmin.cs.termproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    ActionBar mActionBar;
    ActionBar.Tab taskTab, mapTab, listTab, goalTab;

    Fragment mFragment;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}