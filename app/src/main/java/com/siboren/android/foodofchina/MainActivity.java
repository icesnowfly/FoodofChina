package com.siboren.android.foodofchina;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mLoginButton;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoginButton=(Button)findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                Toast.makeText(MainActivity.this,R.string.login_toast,Toast.LENGTH_SHORT).show();
                Intent i= new Intent(MainActivity.this,MapActivity.class);
                startActivity(i);
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
