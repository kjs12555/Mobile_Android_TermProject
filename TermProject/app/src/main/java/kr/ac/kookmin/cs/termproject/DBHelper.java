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
        db.execSQL("Create Table Event(ID INTEGER PRIMARY KEY AUTOINCREMENT, Name Text, Type Integer);");
        db.execSQL("Create Table Goal(ID INTEGER PRIMARY KEY AUTOINCREMENT, GoalName Text, EventName Text, Type Integer, N Integer, Start Date, End Date)");
        db.execSQL("Create Table Log(ID INTEGER PRIMARY KEY AUTOINCREMENT, Latitude Real, Longitude Real, CameraPath Text, LogName Text, EventName Text, Note Text, Time Real default (datetime('now','localtime')), Foot Integer default(0), Type Integer, TimeGap Real default(0), NoteName Text)");
        db.execSQL("Create Table Save(MinID Integer, EventName Text, LogName Text default('이름없음'), Foot integer default(0), StartTime Real)");
        Toast.makeText(context, "DB 생성 완료", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}