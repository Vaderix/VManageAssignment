package com.datdevteam.vmanageassignment;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=3;
    private static final String DATABASE_NAME="VRecords.db";
    public static final String COLUMN_ID="_id";
    public static final String TABLE_VRECORDS="vrecords";
    public static final String COLUMN_NUMPLATE="numPlate";
    public static final String COLUMN_VEHICLETYPE="vehicleType";
    public static final String COLUMN_OWNERNAME="ownerName";
    public static final String COLUMN_ADDRESS="address";
    public static final String COLUMN_MOBILENUM="mobileNum";
    public static final String COLUMN_PLATEIMG="plateIMG";


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE IF NOT EXISTS "+TABLE_VRECORDS+"("+
                COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COLUMN_NUMPLATE+" TEXT NOT NULL,"+
                COLUMN_VEHICLETYPE+" TEXT,"+
                COLUMN_OWNERNAME+" TEXT," +
                COLUMN_ADDRESS+" TEXT," +
                COLUMN_MOBILENUM+" TEXT," +
                COLUMN_PLATEIMG+" BLOB" +
                ");";
        db.execSQL(query);
    }

    public void addVRecord(VRecords vRecords){

        Bitmap img = vRecords.getPlateIMG();
        byte[] imgByte = getBitmapAsByteArray(img);

        ContentValues values= new ContentValues();

        values.put(COLUMN_NUMPLATE, vRecords.getNumPlate());
        values.put(COLUMN_VEHICLETYPE, vRecords.getVehicleType());
        values.put(COLUMN_OWNERNAME, vRecords.getOwnerName());
        values.put(COLUMN_ADDRESS, vRecords.getAddress());
        values.put(COLUMN_MOBILENUM, vRecords.getMobNum());
        values.put(COLUMN_PLATEIMG, imgByte);

        SQLiteDatabase db= getWritableDatabase();
        db.insert(TABLE_VRECORDS,null,values);
        db.close();
    }

    public void updateVRecord(VRecords vRecords)
    {
        Bitmap img = vRecords.getPlateIMG();
        byte[] imgByte = getBitmapAsByteArray(img);

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PLATEIMG, imgByte);

        SQLiteDatabase db= getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_VRECORDS+" SET "+
                COLUMN_NUMPLATE+" = '"+vRecords.getNumPlate()+"', "+
                COLUMN_VEHICLETYPE+" = '"+vRecords.getVehicleType()+"', "+
                COLUMN_OWNERNAME+" = '"+vRecords.getOwnerName()+"', "+
                COLUMN_ADDRESS+" = '"+vRecords.getAddress()+"', "+
                COLUMN_MOBILENUM+" = '"+vRecords.getMobNum()+"' WHERE "+
                COLUMN_NUMPLATE+" = '"+CurrentRecord.getPlateNum()+"';"
        );

        db.update(TABLE_VRECORDS,cv,COLUMN_NUMPLATE+" = '"+vRecords.getNumPlate()+"'",null);

    }

    public void deleteVRecord(String vNum){

        SQLiteDatabase db= getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_VRECORDS+" WHERE numPlate = '"+vNum+"';");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_VRECORDS+";");
        onCreate(db);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public VRecords getRecordData(String vNum){

        SQLiteDatabase db= getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_VRECORDS+" WHERE numPlate = '"+vNum+"';",null);
        cursor.moveToFirst();

        String plateNumValSTR = cursor.getString(cursor.getColumnIndexOrThrow("numPlate"));
        String vehicleTypeValSTR = cursor.getString(cursor.getColumnIndexOrThrow("vehicleType"));
        String ownerNameValSTR = cursor.getString(cursor.getColumnIndexOrThrow("ownerName"));
        String mobNumValSTR = cursor.getString(cursor.getColumnIndexOrThrow("mobileNum"));
        String addressValSTR = cursor.getString(cursor.getColumnIndexOrThrow("address"));
        byte[] imgByte = cursor.getBlob(cursor.getColumnIndexOrThrow("plateIMG"));

        Bitmap bmpIMG = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);

        return new VRecords(plateNumValSTR, vehicleTypeValSTR, ownerNameValSTR, addressValSTR, mobNumValSTR, bmpIMG);
    }

    public String getCurrentPlateNum(Cursor cursor){

        return cursor.getString(cursor.getColumnIndexOrThrow("numPlate"));
    }

    public boolean plateExists(String plateNum)
    {
        SQLiteDatabase db= getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_VRECORDS+" WHERE "+COLUMN_NUMPLATE+" = '"+plateNum+"';",null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

}
