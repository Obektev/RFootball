package com.example.rfootball;

import android.os.Bundle;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import Utils.TableUtils;

public class LeadersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaders);

        TableLayout tableLayout = findViewById(R.id.table_layout);
        TableUtils.showTable(tableLayout, getApplicationContext());

        AppCompatButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> this.finish());

    }
}
