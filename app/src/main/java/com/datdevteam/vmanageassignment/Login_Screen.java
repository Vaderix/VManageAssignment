package com.datdevteam.vmanageassignment;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Login_Screen extends AppCompatActivity {

    private static final String TAG = "Login_Screen";

    EditText username;
    EditText password;
    CheckBox remMe;
    Button signIn;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private String x_username;
    private String x_password;
    private int backCount;

    LoginDBHandler dbHandler;

    @Override
    public void onBackPressed() {
        if(backCount==1)
        {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__screen);
        backCount= 0;

        dbHandler= new LoginDBHandler(this,null,null,1);
        LoginSession.setActiveSession(false);
        LoginSession.setLoggedUser("");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        signIn = (Button) findViewById(R.id.signIn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        remMe = (CheckBox) findViewById(R.id.remMe);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);                   //Remember to change key to unique key
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);                           //Remember to change key to unique key
        if (saveLogin == true) {
            username.setText(loginPreferences.getString("Login_Rem_Username", ""));                     //Remember to change key to unique key
            password.setText(loginPreferences.getString("Login_Rem_Password", ""));                     //Remember to change key to unique key
            remMe.setChecked(true);
        }

        //Second Parameter in loginPreferences.getStuff means "Assign this value if no value present"
    }

    public void onSignIn(View v) {

        boolean loginSuccess=false;
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(username.getWindowToken(), 0);

        x_username = username.getText().toString();
        x_password = password.getText().toString();

        loginSuccess= dbHandler.checkLoginDetails(x_username,x_password);

        if(loginSuccess)
        {
            setLoginPreferences(x_username,x_password);
            LoginSession.setActiveSession(true);
            LoginSession.setLoggedUser(x_username);
            LoginSession.setLoggedPass(x_password);

            finish();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        else
            Toast.makeText(this,"Invalid User/Password",Toast.LENGTH_LONG).show();

    }

    public void anonSignIn(View v)
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void setLoginPreferences(String username, String password) {

        if (remMe.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);                                               //Remember to change key to unique key
            loginPrefsEditor.putString("Login_Rem_Username", username);                                     //Remember to change key to unique key
            loginPrefsEditor.putString("Login_Rem_Password", password);                                     //Remember to change key to unique key
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }
    }
}
