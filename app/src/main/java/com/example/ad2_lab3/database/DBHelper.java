package com.example.ad2_lab3.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context){
        super(context, "TodoDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE TASKS (ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT, CONTENT TEXT, DATE TEXT, TYPE TEXT, STATUS INTERGER )";
        sqLiteDatabase.execSQL(sql);

        String data = "INSERT INTO TASKS (ID,TITLE,CONTENT,DATE,TYPE,STATUS) VALUES ('1','android','android co ban','12/12/2022','De','1'),\n" +
                "('2','Kotlin','Kotlin co ban','12/12/2022','De','1'),\n" +
                "('3','PHP','PHP co ban','12/12/2022','De','1'),\n" +
                "('4','RUBY','RUBY co ban','12/12/2022','De','1')";

        sqLiteDatabase.execSQL(data);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i != i1){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TASKS");
            onCreate(sqLiteDatabase);
        }
    }
}
