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
        String a = "Create Table Event(ID INTEGER PRIMARY KEY AUTOINCREMENT, Name Text, Type Integer);";
        String b = "Create Table Goal(ID INTEGER PRIMARY KEY AUTOINCREMENT, GoalName Text, EventName Text, Type Integer, N Integer, Start Integer, End Integer)";
        db.execSQL(a);
        db.execSQL(b);
        Toast.makeText(context, "DB 생성 완료", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String a = "Create Table Event(ID INTEGER PRIMARY KEY AUTOINCREMENT, Name Text, Type Integer);";
        String b = "Create Table Goal(ID INTEGER PRIMARY KEY AUTOINCREMENT, GoalName Text, EventName Text, Type Integer, N Integer, Start Integer, End Integer)";
        db.execSQL(a);
        db.execSQL(b);
        Toast.makeText(context, "DB 생성 완료", Toast.LENGTH_SHORT).show();
    }
}