package com.datdevteam.vmanageassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SearchByScreen extends AppCompatActivity {

    EditText searchValue;
    RadioButton plateNumRB, ownerNameRB, addressRB, mobileNumRB, vehicleTypeRB;
    RadioButton twoWhRB, fourWhRB;
    RadioGroup columnGroup, vehicleTypeGroup, operatorGroup;
    RadioButton eqRB, eqHRB, eqLRB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_screen);

        searchValue = (EditText)findViewById(R.id.searchValue);
        columnGroup = (RadioGroup)findViewById(R.id.columnGroup);
        vehicleTypeGroup = (RadioGroup)findViewById(R.id.vehicleTypeGroup);
        plateNumRB = (RadioButton)findViewById(R.id.plateNumRB);
        ownerNameRB = (RadioButton)findViewById(R.id.ownerNameRB);
        addressRB = (RadioButton)findViewById(R.id.addressRB);
        mobileNumRB = (RadioButton)findViewById(R.id.mobileNumRB);
        vehicleTypeRB = (RadioButton)findViewById(R.id.vehicleTypeRB);

        twoWhRB = (RadioButton)findViewById(R.id.twoWhRB);
        fourWhRB = (RadioButton)findViewById(R.id.fourWhRB);

        operatorGroup = (RadioGroup)findViewById(R.id.operatorGroup);
        eqRB = (RadioButton)findViewById(R.id.eqRB);
        eqHRB = (RadioButton)findViewById(R.id.eqHRB);
        eqLRB = (RadioButton)findViewById(R.id.eqLRB);

        fourWhRB.setEnabled(false);
        twoWhRB.setEnabled(false);

        operatorGroup.check(eqRB.getId());

        columnGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(vehicleTypeRB.isChecked())
                {
                    searchValue.setEnabled(false);
                    searchValue.setText("");

                    fourWhRB.setEnabled(true);
                    twoWhRB.setEnabled(true);

                    eqRB.setEnabled(false);
                    eqHRB.setEnabled(false);
                    eqLRB.setEnabled(false);

                    SearchByParams.setSearchOp("=");
                }

                else
                {
                    searchValue.setEnabled(true);
                    vehicleTypeGroup.clearCheck();

                    fourWhRB.setEnabled(false);
                    twoWhRB.setEnabled(false);

                    eqRB.setEnabled(true);
                    eqHRB.setEnabled(true);
                    eqLRB.setEnabled(true);
                }
            }
        });

    }

    public void onBack(View v)
    {
        finish();
    }

    public void onSearch(View v)
    {
        boolean typeSelected = true;
        boolean methodSelected = true;
        boolean valueNotNull = true;
        SearchByParams.setSearchByMethod(true);
        SearchByParams.setSearchByValue(searchValue.getText().toString());

        if(searchValue.getText().toString().equals(""))
            valueNotNull = false;

        if(plateNumRB.isChecked())
        {
            SearchByParams.setSearchBy("numPlate");
        }
        else if(ownerNameRB.isChecked())
        {
            SearchByParams.setSearchBy("ownerName");
        }
        else if(addressRB.isChecked())
        {
            SearchByParams.setSearchBy("address");
        }
        else if(mobileNumRB.isChecked())
        {
            SearchByParams.setSearchBy("mobileNum");
        }
        else if(vehicleTypeRB.isChecked())
        {
            SearchByParams.setSearchBy("vehicleType");
            typeSelected=true;
            if(twoWhRB.isChecked()){
                SearchByParams.setSearchByValue("2-Wheeler");
            }


            else if(fourWhRB.isChecked()){
                SearchByParams.setSearchByValue("4-Wheeler");
            }

            else
                typeSelected = false;

        }
        else
        {
            Toast.makeText(this, "Please select the search method!",Toast.LENGTH_LONG).show();
            methodSelected = false;
        }

        if((valueNotNull||vehicleTypeRB.isChecked()) && typeSelected && methodSelected)
        {
            if(!vehicleTypeRB.isChecked())
            {
                if(eqRB.isChecked())
                    SearchByParams.setSearchOp("=");

                else if(eqHRB.isChecked())
                    SearchByParams.setSearchOp(">=");

                else if(eqLRB.isChecked())
                    SearchByParams.setSearchOp("<=");
            }

            Intent i = new Intent(this, ViewAllRecords.class);
            startActivity(i);
        }
        else
            Toast.makeText(this, "Please enter/select value!",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
