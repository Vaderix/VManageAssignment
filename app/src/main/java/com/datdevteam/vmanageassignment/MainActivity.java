package com.datdevteam.vmanageassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button adminCP, addVehicleButton;
    private int backCount;
    EditText numPlateVal;
    String requestMobNum = "7738299899";

    Button retrieveButton, submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adminCP = (Button)findViewById(R.id.adminCP);
        addVehicleButton = (Button)findViewById(R.id.addVehicleButton);
        retrieveButton = findViewById(R.id.retrieveButton);
        submitButton = findViewById(R.id.submitButton);
        numPlateVal = findViewById(R.id.numPlateVal);

        numPlateVal.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);

        backCount=0;

        InputFilter[] editFilters = numPlateVal.getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.AllCaps();
        numPlateVal.setFilters(newFilters);

        if(!LoginSession.isActiveSession())
        {
            adminCP.setEnabled(false);
            adminCP.setBackgroundResource(R.drawable.disabled_button_style);
            adminCP.setTextColor(Color.parseColor("#888888"));

            addVehicleButton.setEnabled(false);
            addVehicleButton.setBackgroundResource(R.drawable.disabled_button_style);
            addVehicleButton.setTextColor(Color.parseColor("#888888"));
        }
    }

    public void onAddVClick(View v)
    {
        Intent i = new Intent(this, AddVehicleScreen.class);
        startActivity(i);
    }

    public void onViewVClick(View v)
    {
        Intent i = new Intent(this, ViewAllRecords.class);
        startActivity(i);
    }

    public void onAdminCP(View v)
    {
        Intent i = new Intent(this, AdminControlPanel.class);
        startActivity(i);
    }

    public void onLogout(View v)
    {
        LoginSession.setLoggedUser("");
        LoginSession.setActiveSession(false);
        Intent i = new Intent(this,Login_Screen.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if(backCount==1)
        {
            LoginSession.setLoggedUser("");
            LoginSession.setActiveSession(false);

            finish();

            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
        else
        {
            backCount++;
            Toast.makeText(this, "Press back again to exit!", Toast.LENGTH_SHORT).show();
        }

    }

    public void onRetrieveClicked(View v)
    {
        retrieveButton.setEnabled(false);
        numPlateVal.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);
    }

    public void onSubmitClicked(View v)
    {
        if(!numPlateVal.getText().toString().equals(""))
        {
            sendMSGReq();

            Toast.makeText(this, "Message Request Sent!", Toast.LENGTH_SHORT).show();
            numPlateVal.setText("");
            numPlateVal.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);
            retrieveButton.setEnabled(true);
        }
        else
        {
            Toast.makeText(this, "Please enter Number plate value!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMSGReq()
    {
        String testSTR = numPlateVal.getText().toString().replaceAll(" ","");

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(requestMobNum, null,"VAHAN "+testSTR, null, null);
    }
}

