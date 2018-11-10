package com.siboren.android.foodofchina;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    //每次你对数据表进行编辑，添加时候，你都需要对数据库的版本进行提升

    //数据库版本
    private static final int DATABASE_VERSION=3;

    //数据库名称
    private static final String DATABASE_NAME="test.db";


    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表
        String CREATE_TABLE_User="CREATE TABLE "+ User.TABLE+"("
                +User.KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                +User.KEY_name+" TEXT, "
                +User.KEY_password+" TEXT, "
                +User.KEY_gender+" INT,"
                +User.KEY_level+"INT);";
        db.execSQL(CREATE_TABLE_User);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //如果旧表存在，删除，所以数据将会消失
        db.execSQL("DROP TABLE IF EXISTS "+ User.TABLE);

        //再次创建表
        onCreate(db);
    }
}