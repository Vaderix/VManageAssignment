package com.datdevteam.vmanageassignment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;

public class AdminControlPanel extends AppCompatActivity {

    TextView timerText;
    String OTP;
    Button sendOTPButton;
    EditText usernameValue, passValue, mobNumValue, valueOTP;
    LoginDBHandler logHandler;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private static final int MY_PERMISSIONS_REQUEST_FILE_WRITE =1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control_panel);
        sendOTPButton = (Button)findViewById(R.id.sendOTPButton);
        timerText = (TextView)findViewById(R.id.timerText);

        usernameValue = (EditText)findViewById(R.id.usernameValue);
        passValue = (EditText)findViewById(R.id.passValue);
        mobNumValue = (EditText)findViewById(R.id.mobNumValue);
        valueOTP = (EditText)findViewById(R.id.valueOTP);

        valueOTP.setEnabled(false);

        logHandler= new LoginDBHandler(this,null,null,1);
    }

    public void onImport(View v)
    {
        getFileWPermission();
        impTry();
    }

    public void onExport(View v)
    {
        getFileWPermission();
        expTry();
    }

    public void impTry(){
        try
        {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            String CopyTo = "/data/com.datdevteam.vmanageassignment/databases/VRecords.db";
            String CopyFrom = "VRecords.db";
            File currentDB = new File(sd, CopyFrom);
            File backupDB = new File(data, CopyTo);

            if (currentDB.exists()) {

                if(backupDB.exists())
                {
                    this.deleteDatabase("VRecords.db");
                }

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Log.i("Import_Attempt", "Import Successful! RECORDS");

                Toast.makeText(this, "Import Complete!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Log.d("Import_Attempt","Fail Reason: " ,e);
            Toast.makeText(this, "Please provide permission from App Settings > Permissions", Toast.LENGTH_LONG).show();
        }

        try
        {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            String CopyTo = "/data/com.datdevteam.vmanageassignment/databases/loginDetails.db";
            String CopyFrom = "loginDetails.db";
            File currentDB = new File(sd, CopyFrom);
            File backupDB = new File(data, CopyTo);

            if (currentDB.exists()) {

                if(backupDB.exists())
                {
                    this.deleteDatabase("loginDetails.db");
                }

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB,false).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Log.i("Import_Attempt", "Import Successful! LOGIN");

                Toast.makeText(this, "Import Complete!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Log.d("Import_Attempt","Fail Reason: " ,e);
            Toast.makeText(this, "Please provide permission from App Settings > Permissions", Toast.LENGTH_LONG).show();
        }
    }


    public void expTry(){
        try
        {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            String CopyFrom = "/data/com.datdevteam.vmanageassignment/databases/VRecords.db";
            String CopyTo = "VRecords.db";
            File currentDB = new File(data, CopyFrom);
            File backupDB = new File(sd, CopyTo);

            if (currentDB.exists()) {

                if(backupDB.exists())
                {
                    boolean deleteCheck= backupDB.delete();
                }

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Log.i("Export_Attempt", "Export Successful! RECORDS");

                Toast.makeText(this, "Export Complete!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Log.d("Export_Attempt","Fail Reason: " ,e);
            Toast.makeText(this, "Please provide permission from App Settings > Permissions", Toast.LENGTH_LONG).show();
        }

        try
        {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            String CopyFrom = "/data/com.datdevteam.vmanageassignment/databases/loginDetails.db";
            String CopyTo = "loginDetails.db";
            File currentDB = new File(data, CopyFrom);
            File backupDB = new File(sd, CopyTo);

            if (currentDB.exists()) {

                if(backupDB.exists())
                {
                    boolean deleteCheck= backupDB.delete();
                }

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Log.i("Export_Attempt", "Export Successful! LOGIN");

                Toast.makeText(this, "Export Complete!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Log.d("Export_Attempt","Fail Reason: " ,e);
            Toast.makeText(this, "Please provide permission from App Settings > Permissions", Toast.LENGTH_LONG).show();
        }
    }

    public void onHome(View v)
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void onAddAccount(View v)
    {
        if (usernameValue.getText().toString().equals("")||
                passValue.getText().toString().equals("")||
                mobNumValue.getText().toString().equals(""))
        {
            Toast.makeText(this, "Please Enter all the details", Toast.LENGTH_SHORT).show();
        }

        else
        {
            if(valueOTP.getText().toString().equals(OTP))
            {
                if(logHandler.userAvailable(usernameValue.getText().toString()))
                {
                    logHandler.addAdmin(usernameValue.getText().toString(),passValue.getText().toString(),mobNumValue.getText().toString());
                    Toast.makeText(this, "Admin account added!", Toast.LENGTH_SHORT).show();

                    usernameValue.setText("");
                    passValue.setText("");
                    mobNumValue.setText("");
                    valueOTP.setText("");
                }
                else
                {
                    Toast.makeText(this,"Username already exists",Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(this, "Invalid OTP!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onSendOTP(View v)
    {
        try{
            if(mobNumValue.getText().toString().equals(""))
                Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();

            else
            {
                valueOTP.setEnabled(true);
                sendOTPButton.setEnabled(false);
                generateOTP();
                startTimer();
                getSMSPermission();
                //deleteSentOTP();
            }
        }catch(Exception e){
            Toast.makeText(this, "Please provide permission from App Settings > Permissions", Toast.LENGTH_LONG).show();
        }
    }


    public void getFileWPermission()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_FILE_WRITE);
            }
        }
    }

    public void getSMSPermission()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        msgSent();
    }

    public void msgSent()
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(mobNumValue.getText().toString(), null,"Your OTP: "+OTP, null, null);
        Toast.makeText(getApplicationContext(), "OTP sent.",
                Toast.LENGTH_LONG).show();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage(mobNumValue.getText().toString(), null,"Your OTP: "+OTP, null, null);
//                    Toast.makeText(getApplicationContext(), "OTP sent.",
//                            Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }

//    public void deleteSentOTP()
//    {
//        new CountDownTimer(3000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//            }
//
//            public void onFinish() {
//                getContentResolver().delete(Uri.parse("content://sms/sent"), "address =? and body =?",
//                        new String[] {mobNumValue.getText().toString(),"Your OTP: "+OTP});
//            }
//        }.start();
//
//    }

    public void onChangePass(View v)
    {
        Toast.makeText(this, "Temporarily Disabled", Toast.LENGTH_SHORT).show();
    }

    public void startTimer()
    {
        new CountDownTimer(30000, 1000) {

            StringBuilder timerBuild = new StringBuilder(6);

            public void onTick(long millisUntilFinished) {
                timerBuild.setLength(0);
                timerBuild.append("00:");
                if(millisUntilFinished / 1000 <10)
                    timerBuild.append("0");
                timerBuild.append(millisUntilFinished / 1000);
                timerText.setText(timerBuild.toString());
            }

            public void onFinish() {
                timerText.setText("");
                sendOTPButton.setEnabled(true);
            }

        }.start();
    }

    public void generateOTP()
    {
        StringBuilder sBuild = new StringBuilder(6);

        int num;
        SecureRandom random = new SecureRandom();

        for(int i=0;i<5;i++)
        {
            num = random.nextInt(9);
            sBuild = sBuild.append(num);
        }

        OTP = sBuild.toString();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
