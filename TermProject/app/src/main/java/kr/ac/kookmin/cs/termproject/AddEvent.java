package kr.ac.kookmin.cs.termproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by user on 2016-11-26.
 */

public class AddEvent extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        final Button complete = (Button)findViewById(R.id.add_complete);

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                EditText name = (EditText)findViewById(R.id.add_event_editText);
                intent.putExtra("eventName", name.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
