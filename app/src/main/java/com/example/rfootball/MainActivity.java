package com.example.rfootball;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import Utils.BattleCombinations;
import Utils.DateUtils;
import Utils.RFUtils;
import Utils.TableUtils;


public class MainActivity extends AppCompatActivity {

    private GridLayout calendarGrid;
    private Typeface typeface;
    TextView emptyStackTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        typeface = ResourcesCompat.getFont(this, R.font.aldrich);
        calendarGrid = findViewById(R.id.calendarGrid);

        emptyStackTextView = findViewById(R.id.empty_stack_textview);

        AppCompatButton leadersTableButton = findViewById(R.id.leaders_table_button);
        leadersTableButton.setOnClickListener(view -> startActivity(new Intent(this, LeadersActivity.class)));

        AppCompatButton newCompanyButton = findViewById(R.id.create_new_company);
        newCompanyButton.setOnClickListener(view -> NewCompanyGenerator.openDialogMenu(this, () -> {
            addCommandsBadges(BattleCombinations.generateBattleCombinations(this));
            RFUtils.makeToast(getApplicationContext(), getString(R.string.commands_generated));
        }));
    }

    int matches_count = 0;
    int matches_played = 0;
    int silver_division_matches_played = 0;
    String lastDate;
    private void addCommandsBadges(Map<String, List<String>> commands) {
        emptyStackTextView.setVisibility(View.GONE);
        calendarGrid.removeAllViews();

        matches_played = 0;
        silver_division_matches_played = 0;

        matches_count = Integer.parseInt(Objects.requireNonNull(commands.get("matches_count")).get(0));
        List<String> datesList = DateUtils.generateDateList(DateUtils.getCurrentDate(), matches_count * (matches_count - 1) / 2 + 2);
        int i = 0;
        for (Map.Entry<String, List<String>> firstCommand: commands.entrySet()) {
            if (Objects.equals(firstCommand.getKey(), "matches_count"))
                continue;
            for (String secondCommand : firstCommand.getValue()) {
                addCalendarCell(datesList.get(i), firstCommand.getKey(), secondCommand, 0, -1);
                i++;
            }
        }
        lastDate = datesList.get(i);
        // Пустышка для правильного отображения спика снизу
        View separator = new View(getApplicationContext());
        separator.setLayoutParams(new ViewGroup.LayoutParams(1, 300));
        View separator2 = new View(getApplicationContext());
        separator2.setLayoutParams(new ViewGroup.LayoutParams(1, 300));
        calendarGrid.addView(separator);
        calendarGrid.addView(separator2);
    }

    private void addCalendarCell(String date, String firstCommand, String secondCommand, int division, int insertIndex) {

        CardView matchCardView = new CardView(getApplicationContext());
        matchCardView.setRadius(dpToPx(8));
        matchCardView.setCardElevation(dpToPx(5));
        if (division == 0)
            matchCardView.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.button_shape));
        if (division == 1)
            matchCardView.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.silver_devision_shape));
        if (division == 2)
            matchCardView.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.gold_devision_shape));

        // Set layout parameters for the CardView
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = dpToPx(150);
        params.height = dpToPx(150);
        params.setMargins(dpToPx(5), dpToPx(5), dpToPx(5), dpToPx(5)); // Convert dp to pixels
        params.setGravity(Gravity.CENTER);

        matchCardView.setLayoutParams(params);

        // Entire layout
        LinearLayout infoLayout = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams infoLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        infoLayoutParams.setMargins(0,5,0,0);
        infoLayout.setLayoutParams(infoLayoutParams);
        infoLayout.setGravity(Gravity.CENTER);
        infoLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dateParams.setMargins(45, 0,0,0);
        LinearLayout dateLayout = new LinearLayout(getApplicationContext());
        dateLayout.setGravity(Gravity.START);
        dateLayout.setOrientation(LinearLayout.HORIZONTAL);
        dateLayout.setLayoutParams(dateParams);

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(5, 0, 5, 0);

        TextView dateTextView = new TextView(getApplicationContext());
        dateTextView.setText(date);
        dateTextView.setTypeface(typeface, Typeface.BOLD);
        dateTextView.setTextSize(15);
        dateTextView.setGravity(Gravity.CENTER);
        dateTextView.setLayoutParams(textParams);

        TextView statusIco = new TextView(getApplicationContext()); // Use UTF-8 smiles
        statusIco.setGravity(Gravity.START);
        statusIco.setText("❔");
        statusIco.setTextSize(20);
        statusIco.setShadowLayer(5, 1, 1, Color.BLACK);
        statusIco.setLayoutParams(textParams);

        dateLayout.addView(statusIco);
        dateLayout.addView(dateTextView);

        TextView versusTextView = new TextView(getApplicationContext());
        versusTextView.setTextSize(20);
        versusTextView.setTypeface(typeface);
        versusTextView.setGravity(Gravity.CENTER);
        versusTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        versusTextView.setText(String.format("%s\nvs\n%s", firstCommand, secondCommand));

        TextView winnerTextView = new TextView(getApplicationContext());
        winnerTextView.setTextSize(20);
        winnerTextView.setTypeface(typeface);
        winnerTextView.setGravity(Gravity.CENTER);
        winnerTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        infoLayout.addView(dateLayout);
        //infoLayout.addView(dateTextView);
        //infoLayout.addView(statusIco);
        infoLayout.addView(versusTextView);
        infoLayout.addView(winnerTextView);

        matchCardView.addView(infoLayout);

        matchCardView.setOnClickListener(view -> {
            openVersusDialog(firstCommand, secondCommand, winner -> {
                if (division == 0) {
                    matches_played++;
                    cardWinnerLogic(winner, firstCommand, secondCommand, statusIco, winnerTextView, versusTextView);
                }
                else
                    if (division == 1) {
                        silver_division_matches_played++;
                        cardWinnerLogic(winner, firstCommand, secondCommand, statusIco, winnerTextView, versusTextView);
                    }
                    else {
                        RFUtils.allMatchesPlayedDialog(this, getString(R.string.final_),
                                String.format("%s%s", getString(R.string.competition_ended), winner));
                        cardWinnerLogic(winner, firstCommand, secondCommand, statusIco, winnerTextView, versusTextView);
                    }
            });
        });

        if (insertIndex == -1)
            calendarGrid.addView(matchCardView);
        else
            calendarGrid.addView(matchCardView, 0);
    }

    void cardWinnerLogic(String winner, String firstCommand, String secondCommand,
                         TextView statusIco, TextView winnerTextView, TextView versusTextView) {

        statusIco.setText("✅ ");
        winnerTextView.setText(String.format("победитель:\n%s", winner));
        versusTextView.setTextSize(15);
        Map<String, String> firstCommandData = getCommandRow(firstCommand);
        Map<String, String> secondCommandData = getCommandRow(secondCommand);

        int score1 = 0, wins1 = 0, loses1 = 0, draws1 = 0;
        score1 = Integer.parseInt(Objects.requireNonNull(firstCommandData.get("score")));
        wins1 = Integer.parseInt(Objects.requireNonNull(firstCommandData.get("wins")));
        loses1 = Integer.parseInt(Objects.requireNonNull(firstCommandData.get("loses")));
        draws1 = Integer.parseInt(Objects.requireNonNull(firstCommandData.get("draws")));

        int score2 = 0, wins2 = 0, loses2 = 0, draws2 = 0;
        score2 = Integer.parseInt(Objects.requireNonNull(secondCommandData.get("score")));
        wins2 = Integer.parseInt(Objects.requireNonNull(secondCommandData.get("wins")));
        loses2 = Integer.parseInt(Objects.requireNonNull(secondCommandData.get("loses")));
        draws2 = Integer.parseInt(Objects.requireNonNull(secondCommandData.get("draws")));

        if (Objects.equals(winner, firstCommand)) {
            score1 += 3;
            wins1 += 1;
            loses2 += 1;
        }
        if (Objects.equals(winner, secondCommand)) {
            score2 += 3;
            wins2 += 1;
            loses1 += 1;
        }
        if (Objects.equals(winner, "ничья")) {
            score1++;
            score2++;
            draws1++;
            draws2++;
        }

        TableUtils.updateTableRow(getApplicationContext(), firstCommand, score1, wins1, loses1, draws1);
        TableUtils.updateTableRow(getApplicationContext(), secondCommand, score2, wins2, loses2, draws2);

        if (matches_played == matches_count) {
            matches_played = 0;
            RFUtils.allMatchesPlayedDialog(this, getString(R.string.half_final), getString(R.string.half_final_text));
            Map<String, List<String>> silverDivision = BattleCombinations.generateSilverDivision(this);
            List<String> dates = DateUtils.generateDateList(lastDate, 2);
            int i = 1;
            for (Map.Entry<String, List<String>> battles : silverDivision.entrySet()) {
                addCalendarCell(dates.get(i--), battles.getKey(), battles.getValue().get(0),
                        1, 0);
            }
        }
        if (silver_division_matches_played == 2) {
            silver_division_matches_played = 0;
            RFUtils.allMatchesPlayedDialog(this, getString(R.string.final_), getString(R.string.half_final_ended));
            Map<String, List<String>> silverDivision = BattleCombinations.generateGoldenDivision(this);
            List<String> dates = DateUtils.generateDateList(lastDate, 2);
            int i = 1;
            for (Map.Entry<String, List<String>> battles : silverDivision.entrySet()) {
                addCalendarCell(dates.get(i--), battles.getKey(), battles.getValue().get(0),
                        2, 0);
            }
        }
    }

    Map<String, String> getCommandRow(String commandName) {
        List<Map<String, String>> tableData = TableUtils.getTableData(getApplicationContext());
        for (int i = 0; i < tableData.size(); i++) {
            Map<String, String> rawData = tableData.get(i);

            if (Objects.equals(rawData.get("commandName"), commandName)) {
                return rawData;
            }
        }
        return null;
    }

    interface VersusDialogListener {
        void onWinnerGot(String winner);
    }
    private void openVersusDialog(String firstCommandName, String secondCommandName, VersusDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(10,10,10,10);

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setElevation(3);
        layout.setPadding(20,20,20,25);
        layout.setTranslationZ(3);
        layout.setLayoutParams(layoutParams);
        layout.setPadding(5, 5, 5, 5);

        TextView title = new TextView(getApplicationContext());
        title.setText(getString(R.string.game_results));
        title.setTypeface(typeface);
        title.setGravity(Gravity.CENTER);
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        title.setTextSize(20);

        TextView semiTitle = new TextView(getApplicationContext());
        semiTitle.setText(getString(R.string.who_won));
        semiTitle.setTypeface(typeface);
        semiTitle.setGravity(Gravity.CENTER);
        semiTitle.setTextSize(20);

        LinearLayout buttonsLayout = new LinearLayout(getApplicationContext());
        buttonsLayout.setPadding(5,5,5,10);
        buttonsLayout.setGravity(Gravity.CENTER);
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonsLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ConstraintLayout.LayoutParams buttonsParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonsParams.setMargins(25,5,25,5);

        AppCompatButton firstCommandButton = new AppCompatButton(getApplicationContext());
        firstCommandButton.setTypeface(typeface);
        firstCommandButton.setText(firstCommandName);
        firstCommandButton.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.round_shape));
        firstCommandButton.setTextSize(25);
        firstCommandButton.setElevation(3);
        firstCommandButton.setTranslationZ(3);
        firstCommandButton.setPadding(15, 15, 15, 15);
        firstCommandButton.setLayoutParams(buttonsParams);

        AppCompatButton drawCommandButton = new AppCompatButton(getApplicationContext());
        drawCommandButton.setTypeface(typeface);
        drawCommandButton.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.round_shape));
        drawCommandButton.setText(getString(R.string.draw));
        drawCommandButton.setTextSize(25);
        drawCommandButton.setElevation(3);
        drawCommandButton.setTranslationZ(3);
        drawCommandButton.setPadding(15, 15, 15, 15);
        drawCommandButton.setLayoutParams(buttonsParams);

        AppCompatButton secondCommandButton = new AppCompatButton(getApplicationContext());
        secondCommandButton.setTypeface(typeface);
        secondCommandButton.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.round_shape));
        secondCommandButton.setText(secondCommandName);
        secondCommandButton.setTextSize(25);
        secondCommandButton.setElevation(3);
        secondCommandButton.setTranslationZ(3);
        secondCommandButton.setPadding(15, 15, 15, 15);
        secondCommandButton.setLayoutParams(buttonsParams);

        buttonsLayout.addView(firstCommandButton);
        buttonsLayout.addView(drawCommandButton);
        buttonsLayout.addView(secondCommandButton);

        layout.addView(semiTitle);
        layout.addView(buttonsLayout);

        builder.setCustomTitle(title);
        builder.setView(layout);

        Dialog dialog = builder.create();
        dialog.show();

        firstCommandButton.setOnClickListener(view -> {
            listener.onWinnerGot(firstCommandName);
            dialog.cancel();
        });
        drawCommandButton.setOnClickListener(view -> {
            listener.onWinnerGot("ничья");
            dialog.cancel();
        });
        secondCommandButton.setOnClickListener(view -> {
            listener.onWinnerGot(secondCommandName);
            dialog.cancel();
        });
    }

    private int dpToPx(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}