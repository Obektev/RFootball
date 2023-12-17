package Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TableUtils {

    public static void updateTableRow(Context context, String commandName, int score, int wins, int loses, int draws) {
        List<Map<String, String>> tableData = getTableData(context);

        for (int i = 0; i < tableData.size(); i++) {
            Map<String, String> rawData = tableData.get(i);

            if (Objects.equals(rawData.get("commandName"), commandName)) {
                Map<String, String> newData = new HashMap<>();
                newData.put("score", String.valueOf(score));
                newData.put("commandName", commandName);
                newData.put("wins", String.valueOf(wins));
                newData.put("loses", String.valueOf(loses));
                newData.put("draws", String.valueOf(draws));
                tableData.set(i, newData);
                break;
            }
        }

        Comparator<Map<String, String>> mapComparator = Comparator.comparing(m -> Integer.parseInt(Objects.requireNonNull(m.get("score"))));
        tableData.sort(mapComparator.reversed());

        saveTableData(context, tableData);
    }

    public static void addRowToTable(TableLayout tableLayout, Map<String, String> rowData) {
        TableRow newRow = new TableRow(tableLayout.getContext());
        ConstraintLayout.LayoutParams rowParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rowParams.topMargin = 5;
        newRow.setLayoutParams(rowParams);

        String score = rowData.get("score");
        String commandName = rowData.get("commandName");
        String wins = rowData.get("wins");
        String loses = rowData.get("loses");
        String draws = rowData.get("draws");

        addTextViewToRow(newRow, score);
        addTextViewToRow(newRow, commandName);
        addTextViewToRow(newRow, wins);
        addTextViewToRow(newRow, loses);
        addTextViewToRow(newRow, draws);

        tableLayout.addView(newRow);
    }

    private static void addTextViewToRow(TableRow row, String text) {
        TextView textView = new TextView(row.getContext());
        textView.setText(text);
        textView.setTextSize(20);
        textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        textView.setGravity(Gravity.CENTER);
        row.addView(textView);
    }


    public static void showTable(TableLayout tableLayout, Context context) {
        List<Map<String, String>> tableData = getTableData(context);

        for (Map<String, String> rowData : tableData)
            addRowToTable(tableLayout, rowData);

    }

    public static List<Map<String, String>> getTableData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("table_preferences", Context.MODE_PRIVATE);

        String json = preferences.getString("table_data", null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Map<String, String>>>() {}.getType();
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>();
        }
    }

    public static void saveTableData(Context context, List<Map<String, String>> tableData) {
        SharedPreferences preferences = context.getSharedPreferences("table_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(tableData);

        editor.putString("table_data", json);
        editor.apply();
    }

}
