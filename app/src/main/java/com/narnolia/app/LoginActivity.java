package com.narnolia.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Admin on 24-10-2016.
 */

public class LoginActivity extends AbstractActivity {

    EditText username,password;
    Button Login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_acitivity);

        username = (EditText)findViewById(R.id.etUserName);
        password = (EditText)findViewById(R.id.etPassword);

        Login = (Button)findViewById(R.id.buttonLogin);
        Login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pushActivity(LoginActivity.this, HomeActivity.class, null, true);
            }
        });
    }
}
