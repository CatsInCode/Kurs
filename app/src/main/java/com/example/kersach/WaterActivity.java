package com.example.kersach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WaterActivity extends AppCompatActivity {
    private DatabaseReference mDdataBase;
    private String USER_KEY = "User";
    private String UID = "";

    private Switch CH;

    private boolean togle = false;

    private EditText pokaz, data, pay, cost;

    private TextView C_lastP, H_lastP, lastD, dolg;

    private ImageView bg_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        init();
    }

    private void init() {
        C_lastP = findViewById(R.id.textView6);
        H_lastP = findViewById(R.id.textView13);
        lastD = findViewById(R.id.textView3);
        dolg = findViewById(R.id.textView14);
        pokaz = findViewById(R.id.editTextText5);
        data = findViewById(R.id.editTextText6);
        pay = findViewById(R.id.editTextText4);
        cost = findViewById(R.id.editTextText3);
        CH = findViewById(R.id.switch1);
        bg_button = findViewById(R.id.imageView39);
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
            Toast.makeText(WaterActivity.this, "Success", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(WaterActivity.this, MainActivity.class);
            startActivity(intent1);
        }
        loadUserData();
    }


    public void onSwitchClicked(View view) {
        // Получение значения Switch
        togle = CH.isChecked();
        if (togle) {
            CH.setText("Холодная");
            bg_button.setImageResource(R.drawable.bg_baners3);  // Использование setImageResource
            CH.setTextColor(getResources().getColor(R.color.white));  // Устанавливаем цвет текста
        } else {
            CH.setText("Горячая");
            bg_button.setImageResource(R.drawable.bg_baners2);  // Использование setImageResource
            CH.setTextColor(getResources().getColor(R.color.orange));  // Устанавливаем цвет текста
        }
        loadUserData();
    }

    private void loadUserData() {
        if (!TextUtils.isEmpty(UID)) {
            mDdataBase.child(UID).child("water").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Water water = dataSnapshot.getValue(Water.class);
                    if (water != null) {
                        // Display the data in the TextViews
                        H_lastP.setText(String.format("%05d", water.h_lastPokaz));
                        C_lastP.setText(String.format("%05d", water.c_lastPokaz));
                        DMY lastDate;
                        if (togle) {
                            lastDate = water.c_lastDate;
                        }
                        else {
                            lastDate = water.h_lastDate;
                        }
                        if (lastDate != null) {
                            lastD.setText(String.format("%02d", lastDate.getDay()) + "/" + String.format("%02d", lastDate.getMonth()) + "/" + String.format("%04d", lastDate.getYear()));
                        } else {
                            lastD.setText("N/A");
                        }
                        if (togle) {
                            dolg.setText(String.valueOf(water.c_dolg));
                            cost.setText(String.valueOf(water.c_lastCost));
                        }
                        else {
                            dolg.setText(String.valueOf(water.h_dolg));
                            cost.setText(String.valueOf(water.h_lastCost));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(WaterActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
        if (togle)
        {
            mDdataBase.child(UID).child("water").child("c_dolg").setValue(rez);
        }
        else {
            mDdataBase.child(UID).child("water").child("h_dolg").setValue(rez);
        }
        // Update Firebase


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
            Toast.makeText(WaterActivity.this, "Неверный формат даты", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse and validate date values
        int day, month, year;
        try {
            day = Integer.parseInt(dateParts[0]);
            month = Integer.parseInt(dateParts[1]);
            year = Integer.parseInt(dateParts[2]);
        } catch (NumberFormatException e) {
            Toast.makeText(WaterActivity.this, "Дата должна содержать только числа", Toast.LENGTH_SHORT).show();
            return;
        }

        if (day < 1 || day > 31) {
            Toast.makeText(WaterActivity.this, "Неверный день", Toast.LENGTH_SHORT).show();
            return;
        }

        if (month < 1 || month > 12) {
            Toast.makeText(WaterActivity.this, "Неверный месяц", Toast.LENGTH_SHORT).show();
            return;
        }

        DMY lastDate = new DMY(day, month, year);

        // Parse and validate pokaz, cost, and dolg values
        int lastPokaz, Lpokaz, cost_, oldDolg;
        try {
            lastPokaz = Integer.parseInt(pokazString);
            if (togle) {
                Lpokaz = Integer.parseInt(C_lastP.getText().toString());
            } else {
                Lpokaz = Integer.parseInt(H_lastP.getText().toString());
            }
            cost_ = Integer.parseInt(costString);
            oldDolg = Integer.parseInt(dlgString);
        } catch (NumberFormatException e) {
            Toast.makeText(WaterActivity.this, "Показания, долг и стоимость должны быть числами", Toast.LENGTH_SHORT).show();
            return;
        }

        int calcPokaz = oldDolg + (lastPokaz - Lpokaz) * cost_;

        // Update Firebase
        if (calcPokaz >= 0) {
            if (!togle) {
                mDdataBase.child(UID).child("water").child("h_dolg").setValue(calcPokaz);
                mDdataBase.child(UID).child("water").child("h_lastDate").setValue(lastDate);
                mDdataBase.child(UID).child("water").child("h_lastPokaz").setValue(lastPokaz);
                mDdataBase.child(UID).child("water").child("h_lastCost").setValue(cost_);
            }
            else {
                mDdataBase.child(UID).child("water").child("c_dolg").setValue(calcPokaz);
                mDdataBase.child(UID).child("water").child("c_lastDate").setValue(lastDate);
                mDdataBase.child(UID).child("water").child("c_lastPokaz").setValue(lastPokaz);
                mDdataBase.child(UID).child("water").child("c_lastCost").setValue(cost_);
            }

            // Optionally show a success message
            loadUserData();
            Toast.makeText(this, "Данные успешно обновлены", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Скрутка! Деньги не списаны!", Toast.LENGTH_SHORT).show();
        }
    }
}