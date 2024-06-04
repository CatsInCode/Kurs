package com.example.kersach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ElektroActivity extends AppCompatActivity {

    private DatabaseReference mDdataBase;
    private String USER_KEY = "User";
    private String UID = "";

    private EditText  pokaz, data, pay, cost;

    private TextView lastP, lastD, dolg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elektro);
        init();
    }

    private void init() {
        lastP = findViewById(R.id.textView6);
        lastD = findViewById(R.id.textView3);
        dolg = findViewById(R.id.textView14);
        pokaz = findViewById(R.id.editTextText5);
        data = findViewById(R.id.editTextText6);
        pay = findViewById(R.id.editTextText4);
        cost = findViewById(R.id.editTextText3);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
        int year = calendar.get(Calendar.YEAR);
        lastD.setText(String.format("%02d", day) + "/" + String.format("%02d",month) + "/" + String.format("%04d", year));
        pokaz.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Handle the loss of focus event
                    String text = pokaz.getText().toString();
                    int text_ = Integer.parseInt(text);
                    pokaz.setText(String.format("%05d", text_));
                }
            }
        });
        mDdataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        Intent intent = getIntent();
        UID = intent.getStringExtra("EXTRA_UID");
        if (TextUtils.isEmpty(UID)) {
            Toast.makeText(ElektroActivity.this, "Success", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(ElektroActivity.this, MainActivity.class);
            startActivity(intent1);
        }
        loadUserData();
    }

    public void onPay(View view) {
            String payStr = pay.getText().toString();
            int pay_ = Integer.parseInt(payStr);
            String dolgStr = dolg.getText().toString();
            int dolg_ = Integer.parseInt(dolgStr);
            int rez = dolg_ - pay_;
            if (rez < 0) {
                rez = 0;
                Toast.makeText(this, "Долг погашен", Toast.LENGTH_SHORT).show();
            }
            // Update Firebase
            mDdataBase.child(UID).child("electro").child("dolg").setValue(rez);

            // Optionally show a success message
            loadUserData();
    }
    public void onPokas(View view) {
        String dateString = data.getText().toString();
        String pokazString = pokaz.getText().toString();
        String costString = cost.getText().toString();
        String dlgString = dolg.getText().toString();


        // Validate date format
        String[] dateParts = dateString.split("/");
        if (dateParts.length != 3) {
            Toast.makeText(ElektroActivity.this, "Неверный формат даты", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse and validate date values
        int day, month, year;
        try {
            day = Integer.parseInt(dateParts[0]);
            month = Integer.parseInt(dateParts[1]);
            year = Integer.parseInt(dateParts[2]);
        } catch (NumberFormatException e) {
            Toast.makeText(ElektroActivity.this, "Дата должна содержать только числа", Toast.LENGTH_SHORT).show();
            return;
        }

        if (day < 1 || day > 31) {
            Toast.makeText(ElektroActivity.this, "Неверный день", Toast.LENGTH_SHORT).show();
            return;
        }

        if (month < 1 || month > 12) {
            Toast.makeText(ElektroActivity.this, "Неверный месяц", Toast.LENGTH_SHORT).show();
            return;
        }

        DMY lastDate = new DMY(day, month, year);

        // Parse and validate pokaz, cost, and dolg values
        int lastPokaz, Lpokaz, cost_, oldDolg;
        try {
            lastPokaz = Integer.parseInt(pokazString);
            Lpokaz = Integer.parseInt(lastP.getText().toString());
            cost_ = Integer.parseInt(costString);
            oldDolg = Integer.parseInt(dlgString);
        } catch (NumberFormatException e) {
            Toast.makeText(ElektroActivity.this, "Показания, долг и стоимость должны быть числами", Toast.LENGTH_SHORT).show();
            return;
        }

        int calcPokaz = oldDolg + (lastPokaz - Lpokaz) * cost_;

        // Update Firebase
        if (calcPokaz >= 0) {
            mDdataBase.child(UID).child("electro").child("dolg").setValue(calcPokaz);
            mDdataBase.child(UID).child("electro").child("lastDate").setValue(lastDate);
            mDdataBase.child(UID).child("electro").child("lastPokaz").setValue(lastPokaz);
            mDdataBase.child(UID).child("electro").child("lastCost").setValue(cost_);

            // Optionally show a success message
            loadUserData();
            Toast.makeText(this, "Данные успешно обновлены", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Скрутка! Деньги не списаны!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserData() {
        if (!TextUtils.isEmpty(UID)) {
            mDdataBase.child(UID).child("electro").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Electro electro = dataSnapshot.getValue(Electro.class);
                    if (electro != null) {
                        // Display the data in the TextViews
                        lastP.setText(String.format("%05d", electro.lastPokaz));
                        DMY lastDate = electro.lastDate;

                        if (lastDate != null) {
                            lastD.setText(String.format("%02d", lastDate.getDay()) + "/" + String.format("%02d", lastDate.getMonth()) + "/" + String.format("%04d", lastDate.getYear()));
                        } else {
                            lastD.setText("N/A");
                        }
                        dolg.setText(String.valueOf(electro.dolg));

                        cost.setText(String.valueOf(electro.lastCost));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ElektroActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}