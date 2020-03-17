package com.datdevteam.vmanageassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ViewAllRecords extends AppCompatActivity {

    MyDBHandler handler;
    Cursor myCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_records);

        handler = new MyDBHandler(this,null,null,1);
        SQLiteDatabase db = handler.getWritableDatabase();

        if(CurrentRecord.scannedRecord)
        {
            CurrentRecord.scannedRecord = false;
            myCursor = db.rawQuery("SELECT  * FROM vrecords WHERE numPlate = '"+
                    CurrentRecord.getPlateNum()+"';", null);
        }

        else
        {
            if(SearchByParams.isSearchByMethod())
            {
                myCursor = db.rawQuery("SELECT  * FROM vrecords WHERE "+
                        SearchByParams.getSearchBy()+" "+SearchByParams.getSearchOp()+" '"+
                        SearchByParams.getSearchByValue()+"';", null);
                SearchByParams.setSearchByMethod(false);
            }
            else
            {
                myCursor = db.rawQuery("SELECT  * FROM vrecords", null);
            }
        }

        ListView recordList = (ListView) findViewById(R.id.recordList);

        RecordAdapter rAdapter = new RecordAdapter(this, myCursor);
        recordList.setAdapter(rAdapter);

        recordList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long l) {

                        String currentPlateNum = handler.getCurrentPlateNum(myCursor);
                        VRecords currentRecord = handler.getRecordData(currentPlateNum);

                        CurrentRecord.setPlateNum(currentPlateNum);
                        CurrentRecord.setvRecords(currentRecord);

                        Toast.makeText(ViewAllRecords.this, currentPlateNum, Toast.LENGTH_LONG).show();

                        ItemOptionsDialog itemOptionsDialog = new ItemOptionsDialog();
                        itemOptionsDialog.setRecord(currentRecord, currentPlateNum);
                        itemOptionsDialog.show(getSupportFragmentManager(),"Item_Options_Dialog");
                    }
                }
        );
    }

    public void onSearchByVal(View v)
    {
        Intent i = new Intent(this, SearchByScreen.class);
        startActivity(i);
    }

    public void onHome(View v)
    {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
