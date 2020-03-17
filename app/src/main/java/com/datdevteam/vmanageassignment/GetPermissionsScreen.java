package com.datdevteam.vmanageassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class GetPermissionsScreen extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private static final int MY_PERMISSIONS_REQUEST_FILE_WRITE =1 ;
    ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_permissions_screen);
        checkPermissions();
        mainLayout = findViewById(R.id.mainLayout);
    }

    public void onGrantPermit(View v)
    {
        if(!hasStoragePermissionGranted())
            requestExternalStoragePermission();

        else
            requestSmsPermission();
    }

    private void checkPermissions() {
        if (hasStoragePermissionGranted() && hasSmsPermissionGranted()) {
            //You can do what whatever you want to do as permission is granted
            finish();
            Intent i = new Intent(this, Login_Screen.class);
            startActivity(i);
        }
    }

    public boolean hasStoragePermissionGranted(){
        return  ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean hasSmsPermissionGranted(){
        return  ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestExternalStoragePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_FILE_WRITE);
    }

    public void requestSmsPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS},
                MY_PERMISSIONS_REQUEST_SEND_SMS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //in case I need to do something here later
                } else {
                    //in case I need to do something here later
                }
                finish();
                Intent i = new Intent(GetPermissionsScreen.this, Login_Screen.class);
                startActivity(i);
            }
            break;
            case MY_PERMISSIONS_REQUEST_FILE_WRITE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //in case I need to do something here later

                } else if (ActivityCompat.shouldShowRequestPermissionRationale(GetPermissionsScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //User has deny from permission dialog
                    Snackbar.make(mainLayout, "Please enable storage permission", Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(GetPermissionsScreen.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            MY_PERMISSIONS_REQUEST_FILE_WRITE);
                                }
                            })
                            .show();
                } else {
                    // User has deny permission and checked never show permission dialog so you can redirect to Application settings page
                    Snackbar.make(mainLayout, "Please enable permission from settings",Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", GetPermissionsScreen.this.getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }

                if(!hasSmsPermissionGranted())
                {
                    requestSmsPermission();
                }
                else
                {
                    finish();
                    Intent i = new Intent(GetPermissionsScreen.this, Login_Screen.class);
                    startActivity(i);
                }
            }
        }
    }
}
