package com.example.rfootball;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private GridLayout calendarGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarGrid = findViewById(R.id.calendarGrid);

        List<String> dateList = generateDateList(); // Список дат

        // Добавляем ячейки в GridLayout
        for (String date : dateList) {
            addCalendarCell(date);
        }
    }

    private List<String> generateDateList() {
        List<String> dateList = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            addCalendarCell(String.valueOf(i) + "\ndec");
        }

        return dateList;
    }

    private void addCalendarCell(String date) {
        CardView cell = new CardView(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = getResources().getDimensionPixelSize(R.dimen.cell_width);
        params.height = getResources().getDimensionPixelSize(R.dimen.cell_height);
        params.setMargins(5, 5, 5, 5);

        cell.setLayoutParams(params);
        cell.setCardElevation(5f);
        cell.setRadius(8f);

        TextView textView = new TextView(this);
        textView.setLayoutParams(new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.MATCH_PARENT
        ));
        textView.setText(date);
        textView.setGravity(android.view.Gravity.CENTER);

        cell.addView(textView);
        calendarGrid.addView(cell);
    }

}