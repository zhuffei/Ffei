package com.example.zhuffei.ffei.tool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2019/4/12 0012.
 */

public class DBOpenHelper extends SQLiteOpenHelper{
    private static final int VERSION=1;
    private static final String DB_NAME="zhuffei.ffei.db";
    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String user="create table db_user(_id integer primary key autoincrement,\n"+"phone integer,pwd varchar(200))";
        db.execSQL(user);
    }
}
