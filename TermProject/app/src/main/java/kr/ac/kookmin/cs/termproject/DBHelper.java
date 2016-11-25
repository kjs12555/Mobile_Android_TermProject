package kr.ac.kookmin.cs.termproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by user on 2016-10-25.
 */

class DBHelper extends SQLiteOpenHelper {
    Context context;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String a = "Create Table Logger(X double, Y double, Division text, LogContext text);";
        db.execSQL(a);
        Toast.makeText(context, "DB 생성 완료", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String a = "Create Table Logger(X double, Y double, Division text, LogContext text);";
        db.execSQL(a);
        Toast.makeText(context, "DB 업데이트 완료", Toast.LENGTH_SHORT).show();
    }
}