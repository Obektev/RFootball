package com.example.rfootball;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Utils.DateUtils;


public class MainActivity extends AppCompatActivity {

    private GridLayout calendarGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarGrid = findViewById(R.id.calendarGrid);

        List<String> datesList = DateUtils.generateDateList("15/12/2023", 30);

        generateDateList(datesList);
    }

    private void generateDateList(List<String> dates) {

        for (String date : dates) {
            addCalendarCell(date);
        }

        // Пустышка для правильного отображения спика снизу
        View separator = new View(getApplicationContext());
        separator.setLayoutParams(new ViewGroup.LayoutParams(1, 200));
        calendarGrid.addView(separator);

    }

    private void addCalendarCell(String date) {
        Log.d("addCalendarCell", date + " added");
        // Create a new CardView
        CardView cardView = new CardView(getApplicationContext());

        // Set layout parameters for the CardView
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = dpToPx(150); // Convert dp to pixels
        params.height = dpToPx(150); // Convert dp to pixels
        params.setMargins(dpToPx(5), dpToPx(5), dpToPx(5), dpToPx(5)); // Convert dp to pixels
        params.setGravity(Gravity.CENTER);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(params);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.calendar_svgrepo_com);

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        imageParams.gravity = Gravity.START;
        imageParams.topMargin = 10;
        imageParams.width = 130;
        imageParams.height = 130;
        imageView.setLayoutParams(imageParams);

        TextView textView = new TextView(this);
        textView.setText(date);
        textView.setTextSize(20);

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.START;
        textParams.topMargin = 10;
        textView.setLayoutParams(textParams);

        // Add ImageView and TextView to the LinearLayout
        linearLayout.addView(imageView);
        linearLayout.addView(textView);

        cardView.addView(linearLayout);

        cardView.setRadius(dpToPx(8)); // Convert dp to pixels
        cardView.setCardElevation(dpToPx(5)); // Convert dp to pixels

        cardView.setLayoutParams(params);
        calendarGrid.addView(cardView);
    }
    private int dpToPx(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}