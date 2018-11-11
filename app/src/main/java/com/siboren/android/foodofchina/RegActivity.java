package com.siboren.android.foodofchina;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegActivity extends AppCompatActivity {

    private Button mRegisterButton;
    private Button mBackButton;
    private UserAPI UserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        UserData = new UserAPI(getApplicationContext());
        mRegisterButton=(Button)findViewById(R.id.register_in_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //String username=findViewById(R.id.reg_user).toString();
               User user=new User();
               user.ID=Integer.parseInt(((EditText)findViewById(R.id.reg_user)).getText().toString());
               user.password=((EditText)findViewById(R.id.reg_password)).getText().toString();
               String check_password=((EditText)findViewById(R.id.reg_check_password)).getText().toString();
               if (user.password.equals(check_password))
               {
                   UserData.insert(user);
                   Toast.makeText(RegActivity.this,R.string.reg_success,Toast.LENGTH_SHORT).show();
                   onDestroy();
               }else{
                   Toast.makeText(RegActivity.this,R.string.password_dif,Toast.LENGTH_SHORT).show();
               }
            }
        }
        );

        mBackButton=(Button)findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
            }
        });
    }

}
