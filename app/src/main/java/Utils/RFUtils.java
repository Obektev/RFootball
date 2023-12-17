package Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import com.example.rfootball.NewCompanyGenerator;
import com.example.rfootball.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RFUtils {
    public static void makeToast(Context context, String announcement) {
        Toast.makeText(context, announcement, Toast.LENGTH_SHORT).show();
    }

    public interface CommandsListUpdated {
        void onCommandsListUpdated();
    }
    public static void updateCommandsList(Activity activity, List<String> commands, CommandsListUpdated listener) {

        clearLeadersTable(commands, activity);

        SharedPreferences sharedPreferences = activity.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(commands);

        editor.putString("commands", json);
        editor.apply();
        listener.onCommandsListUpdated();
    }
    private static void clearLeadersTable(List<String> commands, Activity activity) {
        List<Map<String, String>> tableData = new ArrayList<>();
        for (String command : commands) {
            Map<String, String> rowData = new HashMap<>();
            rowData.put("commandName", command);
            rowData.put("score", "0");
            rowData.put("wins", "0");
            rowData.put("loses", "0");
            rowData.put("draws", "0");

            tableData.add(rowData);
        }
        TableUtils.saveTableData(activity, tableData);
    }

    public static List<String> getCommands(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        String json = sharedPreferences.getString("commands", "");

        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public static void allMatchesPlayedDialog(Activity activity, String titleText, String semititleText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        Typeface typeface = ResourcesCompat.getFont(activity, R.font.aldrich);

        TextView title = new TextView(activity);
        title.setTypeface(typeface);
        title.setTextSize(25);
        title.setGravity(Gravity.CENTER);
        title.setText(titleText);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout layout = new LinearLayout(activity);
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView halfFinalTextView = new TextView(activity);
        halfFinalTextView.setTypeface(typeface);
        halfFinalTextView.setTextSize(20);
        halfFinalTextView.setGravity(Gravity.CENTER);
        halfFinalTextView.setText(semititleText);

        AppCompatButton exitButton = new AppCompatButton(activity);
        exitButton.setText(activity.getString(R.string.good));

        layout.addView(halfFinalTextView);
        layout.addView(exitButton);

        builder.setCustomTitle(title);
        builder.setView(layout);

        Dialog dialog = builder.create();
        dialog.show();

        exitButton.setOnClickListener(view -> dialog.cancel());
    }
}
