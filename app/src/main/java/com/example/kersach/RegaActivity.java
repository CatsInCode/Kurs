package com.example.kersach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegaActivity extends AppCompatActivity {

    private DatabaseReference mDdataBase;
    private EditText login;
    private EditText passw;
    private EditText cfPassw;
    private String USER_KEY = "User";
    private FirebaseAuth myAuth;

    private String loginTXT;
    private String passwordTXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rega);
        init();
    }

    private void init() {
        login = findViewById(R.id.editTextText);
        passw = findViewById(R.id.editTextTextPassword);
        cfPassw = findViewById(R.id.editTextText2);
        myAuth = FirebaseAuth.getInstance();
        mDdataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        Intent intent = getIntent();
        loginTXT = intent.getStringExtra("EXTRA_LOGIN");
        if (!TextUtils.isEmpty(loginTXT)) {
            login.setText(loginTXT);
        }
        passwordTXT = intent.getStringExtra("EXTRA_PASSWORD");
        if (!TextUtils.isEmpty(loginTXT)) {
            passw.setText(passwordTXT);
        }
    }

    public void onClickReg2(View view) {
        Toast.makeText(getApplicationContext(), "Wait...", Toast.LENGTH_SHORT).show();
        String loginTXT = login.getText().toString();
        String passwordTXT = passw.getText().toString();
        String confirm = cfPassw.getText().toString();
        if (!TextUtils.isEmpty(loginTXT) && !TextUtils.isEmpty(passwordTXT)) {
            if (passwordTXT.equals(confirm)) {
                myAuth.createUserWithEmailAndPassword(loginTXT, passwordTXT).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegaActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegaActivity.this, MainActivity.class);
                            intent.putExtra("EXTRA_LOGIN", login.getText().toString());
                            intent.putExtra("EXTRA_PASSWORD", passw.getText().toString());
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegaActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Неккоректные логин/пароль", Toast.LENGTH_SHORT).show();
        }

    }
}