package com.example.kersach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDdataBase;
    private EditText login;
    private EditText passw;
    private String USER_KEY = "User";
    private FirebaseAuth myAuth;
    private String loginTXT = "";
    private String passwordTXT = "";
    private ValueEventListener userValueEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        login = findViewById(R.id.editTextText);
        passw = findViewById(R.id.editTextTextPassword);
        myAuth = FirebaseAuth.getInstance();
        mDdataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        Intent intent = getIntent();
        loginTXT = intent.getStringExtra("EXTRA_LOGIN");
        if (!TextUtils.isEmpty(loginTXT) || true) {
            //login.setText(loginTXT);
            login.setText("qwerty@gmail.com");
        }
        passwordTXT = intent.getStringExtra("EXTRA_PASSWORD");
        if (!TextUtils.isEmpty(passwordTXT)) {
            passw.setText(passwordTXT);
        }
    }

    public void onClickReg(View view) {
        Intent intent = new Intent(MainActivity.this, RegaActivity.class);
        intent.putExtra("EXTRA_LOGIN", login.getText().toString());
        intent.putExtra("EXTRA_PASSWORD", passw.getText().toString());
        startActivity(intent);
    }

    public void onClickVxod(View view) {
        Toast.makeText(getApplicationContext(), "Подождите...", Toast.LENGTH_SHORT).show();
        loginTXT = login.getText().toString();
        passwordTXT = passw.getText().toString();
        if (!TextUtils.isEmpty(loginTXT) && !TextUtils.isEmpty(passwordTXT)) {
            myAuth.signInWithEmailAndPassword(loginTXT, passwordTXT).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = myAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(MainActivity.this, "Вход...", Toast.LENGTH_SHORT).show();
                            checkUserData(user.getUid());
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Ошибка входа", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Неверные логин/пароль", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Неккоректные логин/пароль", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserData(final String uid) {
        userValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User data exists, proceed to ElektroActivity
                    Intent intent = new Intent(MainActivity.this, Change.class);
                    intent.putExtra("EXTRA_UID", uid);
                    startActivity(intent);
                    // Удаление слушателя
                    mDdataBase.child(uid).removeEventListener(userValueEventListener);
                } else {
                    // User data does not exist, create default data
                    createDefaultUserData(uid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log the error and show a Toast message
                Toast.makeText(MainActivity.this, "checkUserData Fail: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        mDdataBase.child(uid).addListenerForSingleValueEvent(userValueEventListener);
    }

    private void createDefaultUserData(String uid) {
        Toast.makeText(MainActivity.this, "Create new account", Toast.LENGTH_SHORT).show();
        User defaultUser = new User();
        defaultUser.id = uid;
        defaultUser.login = loginTXT;
        defaultUser.electro = new Electro();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
        int year = calendar.get(Calendar.YEAR);
        defaultUser.electro.lastDate = new DMY(day, month, year);
        defaultUser.electro.lastPokaz = 0;
        defaultUser.electro.dolg = 0;
        defaultUser.electro.lastCost = 0;
        defaultUser.water = new Water();
        defaultUser.water.c_dolg= 0;
        defaultUser.water.h_dolg= 0;
        defaultUser.water.c_lastCost = 0;
        defaultUser.water.h_lastCost = 0;
        defaultUser.water.c_lastPokaz = 0;
        defaultUser.water.h_lastPokaz = 0;
        defaultUser.water.c_lastDate = new DMY(day, month, year);
        defaultUser.water.h_lastDate = new DMY(day, month, year);
        mDdataBase.child(uid).setValue(defaultUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "1234", Toast.LENGTH_SHORT).show();
                            // User data successfully created, proceed to ElektroActivity
                            Intent intent = new Intent(MainActivity.this, Change.class);
                            intent.putExtra("EXTRA_UID", uid);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "1234", Toast.LENGTH_SHORT).show();
                        } else {
                            // User data creation failed
                            Toast.makeText(MainActivity.this, "Ошибка создания нового пользователя", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}