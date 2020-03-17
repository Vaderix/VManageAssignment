package com.datdevteam.vmanageassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=5;
    private static final String DATABASE_NAME="loginDetails.db";
    public static final String TABLE_ADMIN="admin";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_USERNAME="username";
    public static final String COLUMN_PASSWORD="password";
    public static final String COLUMN_MOBNUM="mobnum";


    public LoginDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query="CREATE TABLE IF NOT EXISTS "+TABLE_ADMIN+"("+
                COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COLUMN_USERNAME+" TEXT NOT NULL UNIQUE,"+
                COLUMN_PASSWORD+" TEXT NOT NULL,"+
                COLUMN_MOBNUM+" TEXT"+
                ");";
        db.execSQL(query);
        ContentValues values= new ContentValues();
        values.put(COLUMN_USERNAME, "admin");
        values.put(COLUMN_PASSWORD, "masterpassword");
        values.put(COLUMN_MOBNUM, "-");
        db.insertOrThrow(TABLE_ADMIN,null,values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ADMIN+";");
        onCreate(db);
    }

    public void addAdmin(String user, String pass, String mob)
    {
        ContentValues values= new ContentValues();
        values.put(COLUMN_USERNAME, user);
        values.put(COLUMN_PASSWORD, pass);
        values.put(COLUMN_MOBNUM, mob);

        SQLiteDatabase db= getWritableDatabase();
        db.insertOrThrow(TABLE_ADMIN,null,values);
        db.close();
    }

    public boolean userAvailable(String user)
    {
        SQLiteDatabase db= getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_ADMIN+" WHERE "+COLUMN_USERNAME+" = '"+user+"';",null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean checkLoginDetails(String user, String pass)
    {
        SQLiteDatabase db= getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_ADMIN+" WHERE "+COLUMN_USERNAME+" = '"+user+"' AND "+
                COLUMN_PASSWORD+" = '"+pass+"';",null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void changePassword(String pass)
    {
        ContentValues cv= new ContentValues();
        cv.put(COLUMN_PASSWORD, pass);

        SQLiteDatabase db= getWritableDatabase();
        db.update(TABLE_ADMIN,cv,COLUMN_USERNAME+" = '"+LoginSession.getLoggedUser()+"'",null);
    }
}
