package com.siboren.android.foodofchina;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class UserAPI {
    private DBHelper dbHelper;

    public UserAPI(Context context){
        dbHelper=new DBHelper(context);
    }

    public int insert(User User){
        //打开连接，写入数据
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(User.KEY_ID,User.ID);
        values.put(User.KEY_name,User.name);
        values.put(User.KEY_password,User.password);
        values.put(User.KEY_gender,User.gender);
        values.put(User.KEY_level,User.level);
        //
        Log.d("api_", "insert: "+values );
        long User_Id=db.insert("user",null,values);
        Log.d("api_", "insert: "+User_Id );
        db.close();
        return (int)User_Id;
    }

    public void delete(int User_Id){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(User.TABLE,User.KEY_ID+"=?", new String[]{String.valueOf(User_Id)});
        db.close();
    }
    public void update(User User){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(User.KEY_ID,User.ID);
        values.put(User.KEY_name,User.name);
        values.put(User.KEY_password,User.password);
        values.put(User.KEY_gender,User.gender);
        values.put(User.KEY_level,User.level);

        db.update(User.TABLE,values,User.KEY_ID+"=?",new String[] { String.valueOf(User.ID) });
        db.close();
    }

    public User getUserById(int Id){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT *"
                +" FROM " + User.TABLE
                + " WHERE " +
                User.KEY_ID + "=?";
        int iCount=0;
        User User=new User();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(Id)});
        if(cursor.moveToFirst()){
            do{
                User.ID =cursor.getInt(cursor.getColumnIndex(User.KEY_ID));
                User.name =cursor.getString(cursor.getColumnIndex(User.KEY_name));
                User.password  =cursor.getString(cursor.getColumnIndex(User.KEY_password));
                User.gender =cursor.getInt(cursor.getColumnIndex(User.KEY_gender));
                User.level =cursor.getInt(cursor.getColumnIndex(User.KEY_level));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return User;
    }
    public void insert_initial(){
        User user_temp=new User();
        user_temp.ID=1511;
        user_temp.name="aaa";
        user_temp.password="123456";
        user_temp.gender=1;
        user_temp.level=1;
        insert(user_temp);
    }
    public int login(int ID,String password){
        User user_temp=new User();
        user_temp=getUserById(ID);
        Log.d("api", "login: "+user_temp.ID+"\t"+user_temp.password +"\t"+password);
        if (user_temp.ID==0){
            return 2;//用户不存在
        }
        if(user_temp.password.equals(password)){
            return 1;//密码正确
        }
        else{
            return 0;//密码错误
        }
    }
    public void reset(){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+ User.TABLE);
        db.close();
    }
}
