package com.siboren.android.foodofchina;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
                String jj=((EditText)findViewById(R.id.login_user)).getText().toString();
                if (jj.length()==0)
                {
                    return;
                }
                int j=userapi.login(Integer.parseInt(jj),((EditText)findViewById(R.id.login_password)).getText().toString());
                Log.d("main_activity", "onCreate: check finished " +j);
                if (j==1)
                {
                    Intent i= new Intent(MainActivity.this,MapActivity.class);
                    startActivity(i);
                    onDestroy();
                }else{
                    if (j==0)
                    {
                        Toast.makeText(MainActivity.this,R.string.wrong_password,Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this,R.string.null_username,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        );

        mRegisterButton=(Button)findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                Intent k= new Intent(MainActivity.this,RegActivity.class);
                startActivity(k);
            }
        }
        );
    }
}
