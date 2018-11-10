package com.siboren.android.foodofchina;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mLoginButton;
    private Button mRegisterButton;

    public UserAPI userapi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        userapi=new UserAPI(MainActivity.this);
        Log.d("main_activity", "onCreate: insert" );
        mLoginButton=(Button)findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                Log.d("main_activity", "onCreate: check" );
                int j=userapi.login(1511,"123456");
                Log.d("main_activity", "onCreate: check finished " +j);
                Intent i= new Intent(MainActivity.this,MapActivity.class);
                startActivity(i);
                onDestroy();
            }
        }
        );

        mRegisterButton=(Button)findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                Toast.makeText(MainActivity.this,R.string.register_toast,Toast.LENGTH_SHORT).show();
            }
        }
        );

    }
}
