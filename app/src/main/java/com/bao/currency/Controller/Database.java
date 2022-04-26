package com.bao.currency.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    private Context context;
    public static String DATABASE_NAME="Currency.db";
    public static int DATABASE_VERSION=1;

    public static String TABLE_NAME="history";
    public static String COLUMN_ID="_id";
    public static String COLUMN_FROM_RATE="from_rate";
    public static String COLUMN_TO_RATE="to_rate";
    public static String COLUMN_FROM="_from";
    public static String COLUMN_RESULT="_result";
    public static String COLUMN_DATE="_date";


    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
        this.context=context;
    }
    public void queryData(String sql){
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL(sql);
    }

    public Cursor getData(String sql){
        SQLiteDatabase database=getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query="CREATE TABLE "+TABLE_NAME +
                " (" +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_FROM_RATE+" TEXT, "+
                COLUMN_TO_RATE+" TEXT, "+
                COLUMN_FROM+ " TEXT, "+
                COLUMN_RESULT+" TEXT, "+
                COLUMN_DATE+" TEXT);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(String from_rate, String to_rate, String _from, String result, String date){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_FROM_RATE, from_rate);
        cv.put(COLUMN_TO_RATE, to_rate);
        cv.put(COLUMN_FROM, _from);
        cv.put(COLUMN_RESULT, result);
        cv.put(COLUMN_DATE, date);
        long kq=db.insert(TABLE_NAME,null, cv);
        if(kq==-1){
            //Toast.makeText(context, "FAILED", Toast.LENGTH_SHORT).show();
            System.out.println("FAILED");
        }else{
            //Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show();
            System.out.println("SUCCESS");
        }
    }

    public Cursor readAllData(){
        String query="SELECT * FROM "+TABLE_NAME+" ORDER BY "+COLUMN_ID+" DESC";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor=db.rawQuery(query, null);
        }
        return cursor;
    }


}
