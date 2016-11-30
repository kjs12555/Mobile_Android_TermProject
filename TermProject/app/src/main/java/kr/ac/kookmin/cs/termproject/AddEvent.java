package kr.ac.kookmin.cs.termproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by user on 2016-11-26.
 */

public class AddEvent extends Activity {

    int eventPosition;
    DBHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_event);
        final Button complete = (Button)findViewById(R.id.add_complete);
        final EditText nameEditText = (EditText)findViewById(R.id.add_event_editText);
        final Intent intent = getIntent();
        final boolean flagModify = intent.getBooleanExtra("flagModify",false);
        if(flagModify){
            eventPosition = intent.getIntExtra("eventPosition", -1);
            TextView text = (TextView)findViewById(R.id.add_event_textView);
            text.setText("이벤트 수정");
        }

        helper = new DBHelper(this, "TermProject.db", null, 1);
        db = helper.getWritableDatabase();

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                Cursor rs =  db.rawQuery("select count(*) from Event where Name='"+name+"';",null);
                rs.moveToNext();
                int check = rs.getInt(0);
                if(name.equals("")){
                    Toast.makeText(v.getContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(check!=0){
                    Toast.makeText(v.getContext(), "중복인 이름입니다. 이름을 바꿔주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(flagModify){
                    Intent i = new Intent(AddEvent.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.putExtra("tabPosition",0);
                    i.putExtra("modifyName", name);
                    i.putExtra("resultCode",1);
                    i.putExtra("eventPosition", eventPosition);
                    startActivity(i);
                }else {
                    intent.putExtra("eventName", name);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
