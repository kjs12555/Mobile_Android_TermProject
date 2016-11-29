package kr.ac.kookmin.cs.termproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by user on 2016-11-27.
 */

public class AddGoalForm extends Activity {
    final int radioButton1 = R.id.radioButton;
    final int radioButton2 = R.id.radioButton2;
    int firstType = 1;
    DBHelper helper;
    SQLiteDatabase db;
    Spinner addGoalEventName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_goal_form);
        final Button complete = (Button)findViewById(R.id.add_goal_complete);
        final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        addGoalEventName = (Spinner)findViewById(R.id.add_goal_event_name);
        final Spinner spinner2 = (Spinner)findViewById(R.id.add_goal_form_spinner2);
        final EditText timeEditText = (EditText)findViewById(R.id.time_editText);
        final Spinner timeSpinner = (Spinner)findViewById(R.id.time_spinner);
        final EditText countEditText = (EditText)findViewById(R.id.count_editText);
        final Spinner countSpinner = (Spinner)findViewById(R.id.count_spinner);
        helper = new DBHelper(this, "TermProject.db" ,null, 1);
        db = helper.getWritableDatabase();
        getDatasToDB();
        ArrayList<String> spinner2Data = new ArrayList<String>();
        spinner2Data.add("이상");
        spinner2Data.add("이하");
        ArrayAdapter<String> spinner2Adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinner2Data);
        spinner2.setAdapter(spinner2Adapter);
        ArrayList<String> timeSpinnerData = new ArrayList<String>();
        timeSpinnerData.add("시간");
        timeSpinnerData.add("분");
        timeSpinnerData.add("초");
        ArrayAdapter<String> timeSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, timeSpinnerData);
        timeSpinner.setAdapter(timeSpinnerAdapter);
        ArrayList<String> countSpinnerData = new ArrayList<String>();
        countSpinnerData.add("하루");
        countSpinnerData.add("일주일");
        countSpinnerData.add("한 달");
        ArrayAdapter<String> countSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, countSpinnerData);
        countSpinner.setAdapter(countSpinnerAdapter);

        final Intent intent = getIntent();
        if(intent.getExtras().getBoolean("Flag")){
            RadioButton countRadioButton = (RadioButton)findViewById(radioButton2);
            countRadioButton.setVisibility(View.INVISIBLE);
        }

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("eventName", (String)addGoalEventName.getSelectedItem());
                int type = firstType*100;
                int N=-1;
                switch (firstType){
                    case 1:
                        type += timeSpinner.getSelectedItemPosition()*10;
                        try {
                            N = Integer.parseInt(timeEditText.getText().toString());
                        }catch (NumberFormatException e){
                            Toast.makeText(v.getContext(), "숫자를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        break;
                    case 2:
                        type += countSpinner.getSelectedItemPosition()*10;
                        try {
                            N = Integer.parseInt(countEditText.getText().toString());
                        }catch (NumberFormatException e){
                            Toast.makeText(v.getContext(), "숫자를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        break;
                }
                if(N==0){
                    Toast.makeText(v.getContext(), "0은 입력이 불가능 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                type += spinner2.getSelectedItemPosition();
                intent.putExtra("Type", type);
                intent.putExtra("N", N);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case radioButton1:
                        firstType = 1;
                        timeEditText.setVisibility(View.VISIBLE);
                        timeSpinner.setVisibility(View.VISIBLE);
                        countEditText.setVisibility(View.INVISIBLE);
                        countSpinner.setVisibility(View.INVISIBLE);
                        timeEditText.setText(countEditText.getText().toString());
                        timeSpinner.setSelection(countSpinner.getSelectedItemPosition());
                        break;
                    case radioButton2:
                        firstType = 2;
                        timeEditText.setVisibility(View.INVISIBLE);
                        timeSpinner.setVisibility(View.INVISIBLE);
                        countEditText.setVisibility(View.VISIBLE);
                        countSpinner.setVisibility(View.VISIBLE);
                        countEditText.setText(timeEditText.getText().toString());
                        countSpinner.setSelection(timeSpinner.getSelectedItemPosition());
                }
            }
        });
    }

    private void getDatasToDB() {
        ArrayList<String> data = new ArrayList<String>();
        Cursor rs = db.rawQuery("select Distinct(Name) from Event;",null);
        while(rs.moveToNext()){
            data.add(rs.getString(0));
        }
        ArrayAdapter<String> adapterBig = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, data);
        addGoalEventName.setAdapter(adapterBig);
    }
}
