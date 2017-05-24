package com.example.call_the_roll.otheractivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.call_the_roll.MainActivity;
import com.example.call_the_roll.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText IDText;
    private EditText NameText;
    private Button login;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        IDText = (EditText) findViewById(R.id.IDText);
        NameText = (EditText) findViewById(R.id.NameText);
        String id = pref.getString("id","");
        String name = pref.getString("name","");
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        if(!id.equals("") && !name.equals("")){
            IDText.setText(id);
            NameText.setText(name);
            login.callOnClick();
        }
    }

    @Override
    public void onClick(View v){
        editor = pref.edit();
        String id = IDText.getText().toString();
        String name = NameText.getText().toString();
        Log.d("LoginActivity",id);
        Log.d("LoginActivity",name);
        editor.putString("id",id);
        editor.putString("name",name);
        editor.apply();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("ID",id);
        intent.putExtra("Name",name);
        startActivity(intent);
        finish();
    }
}
