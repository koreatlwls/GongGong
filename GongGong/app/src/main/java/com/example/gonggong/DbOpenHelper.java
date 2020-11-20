package com.example.gonggong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper {
    private static final String DATABASE_NAME="InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION=1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mContext;

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
            super(context,name,factory,version);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DataBase.CreateDB._CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DataBase.CreateDB._TABLENAME);
            onCreate(sqLiteDatabase);
        }
    }
    public DbOpenHelper(Context context){
        mContext=context;
    }

    public DbOpenHelper open() throws SQLException{
        mDBHelper=new DatabaseHelper(mContext,DATABASE_NAME,null,DATABASE_VERSION);
        mDB=mDBHelper.getWritableDatabase();
        return this;
    }
    public void create(){
        mDBHelper.onCreate(mDB);
    }

    public void close(){
        mDB.close();
    }

    public long insertColumn(String facilityName,String facilityAddr,int code,double latitude,double longitude){
        ContentValues values=new ContentValues();
        values.put(DataBase.CreateDB.FACILITYNAME,facilityName);
        values.put(DataBase.CreateDB.FACILITYADDRESS,facilityAddr);
        values.put(DataBase.CreateDB.CODE,code);
        values.put(DataBase.CreateDB.latitude,latitude);
        values.put(DataBase.CreateDB.longitude,longitude);
        return mDB.insert(DataBase.CreateDB._TABLENAME,null,values);
    }
    public void deleteAllColumns() {
        mDB.delete(DataBase.CreateDB._TABLENAME, null, null);
    }

    // Delete DB
    public boolean deleteColumn(long id){
        return mDB.delete(DataBase.CreateDB._TABLENAME, "_id="+id, null) > 0;
    }
    // Select DB
    public Cursor selectColumns(){
        return mDB.query(DataBase.CreateDB._TABLENAME, null, null, null, null, null, null);
    }

    // sort by column
    public Cursor sortColumn(String sort){
        Cursor c = mDB.rawQuery( "SELECT * FROM favoritetable ORDER BY " + sort + ";", null);
        return c;
    }
}
