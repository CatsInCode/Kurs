package com.example.kersach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Change extends AppCompatActivity {
    private DatabaseReference mDdataBase;
    private String USER_KEY = "User";
    private String UID = "";

    private TextView l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        Intent intent = getIntent();
        UID = intent.getStringExtra("EXTRA_UID");
        init();
    }

    private void init() {
        l = findViewById(R.id.textView18);
        mDdataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        mDdataBase.child(UID).child("login").addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String login = dataSnapshot.getValue(String.class); // Получаем значение как строку
                if (login != null) {
                    login = "Вы зашли как " + login;
                    Toast.makeText(Change.this, login, Toast.LENGTH_SHORT).show();
                    l.setText(login); // Устанавливаем текст в TextView
                } else {
                    l.setText("Login not found"); // Сообщение, если значение login отсутствуе
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибки
                l.setText("Failed to load login"); // Сообщение об ошибке
            }
        });
    }

    public void onExit(View wiew)
    {
        Intent intent1 = new Intent(Change.this, MainActivity.class);
        startActivity(intent1);
    }

    public void onWat(View wiew)
    {
        Intent intent = new Intent(Change.this, WaterActivity.class);
        intent.putExtra("EXTRA_UID", UID);
        startActivity(intent);
    }

    public void onEl(View wiew)
    {
        Intent intent = new Intent(Change.this, ElektroActivity.class);
        intent.putExtra("EXTRA_UID", UID);
        startActivity(intent);
    }
}