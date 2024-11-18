package com.example.ad2_lab3.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ad2_lab3.database.DBHelper;
import com.example.ad2_lab3.model.TaskInFo;

import java.util.ArrayList;



public class TaskInfoDAO {
    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public TaskInfoDAO(Context context) {
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public long addInfo(TaskInFo inFo){
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", inFo.getTitle());
        values.put("content", inFo.getContent());
        values.put("date", inFo.getDate());
        values.put("type", inFo.getType());
        long check = sqLiteDatabase.insert("TASKS", null, values);
        if (check <= 0){
            return -1;
        }
        return 1;
    }

    public long updateInfo(TaskInFo inFo){
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", inFo.getId());
        values.put("title", inFo.getTitle());
        values.put("content", inFo.getContent());
        values.put("date", inFo.getDate());
        values.put("type", inFo.getType());
        values.put("status", inFo.getStatus());
        long check = sqLiteDatabase.update("TASKS", values,"id=?", new String[]{String.valueOf(inFo.getId())});
        if (check <= 0){
            return -1;
        }
        return 1;
    }

    public ArrayList<TaskInFo> getListInfo(){
        ArrayList<TaskInFo> lst = new ArrayList<>();
        sqLiteDatabase = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM TASKS", null);
            if (cursor.getCount() > 0){
                cursor.moveToFirst();
                do {
                    lst.add(new TaskInFo(cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getInt(5)));
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.e("Error DB", e.getMessage());
        }
        return lst;
    }

    public boolean removeInfo(int id){
        int row = sqLiteDatabase.delete("TASKS", "id=?", new String[]{String.valueOf(id)});
        return row != -1;
    }

    public boolean updateTypeInfo(int id, boolean check){
        int status = check ? 1:0;
        ContentValues values = new ContentValues();
        values.put("status", status);
        long row = sqLiteDatabase.update("TASKS", values, "id=?", new String[]{String.valueOf(id)});
        return row != -1;
    }
}
