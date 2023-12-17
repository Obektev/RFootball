package Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BattleCombinations {

    public static Map<String, List<String>> generateGoldenDivision(Activity activity) {
        List<Map<String, String>> ratingData = TableUtils.getTableData(activity);

        Map<String, List<String>> goldenDivisionData = new HashMap<>();
        List<String> battle = new ArrayList<>();
        battle.add(ratingData.get(0).get("commandName"));
        goldenDivisionData.put(ratingData.get(1).get("commandName"), battle);

        return goldenDivisionData;
    }
    public static Map<String, List<String>> generateSilverDivision(Activity activity) {
        List<Map<String, String>> ratingData = TableUtils.getTableData(activity);

        Map<String, List<String>> silverDivisionData = new HashMap<>();
        List<String> firstBattle = new ArrayList<>();
        firstBattle.add(ratingData.get(3).get("commandName"));
        silverDivisionData.put(ratingData.get(0).get("commandName"), firstBattle);
        List<String> secondBattle = new ArrayList<>();
        secondBattle.add(ratingData.get(1).get("commandName"));
        silverDivisionData.put(ratingData.get(2).get("commandName"), secondBattle);

        return silverDivisionData;
    }
    public static Map<String, List<String>> generateBattleCombinations(Activity activity) {
        List<String> commandNames = RFUtils.getCommands(activity);

        Map<String, List<String>> battleCombinations = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("0");
        battleCombinations.put("matches_count", list);

        for (int i = 0; i < commandNames.size(); i++) {
            String firstCommand = commandNames.get(i);

            for (int j = i + 1; j < commandNames.size(); j++) {
                String secondCommand = commandNames.get(j);
                if (battleCombinations.containsKey(firstCommand))
                    Objects.requireNonNull(battleCombinations.get(firstCommand)).add(secondCommand);
                else {
                    List<String> newList = new ArrayList<>();
                    newList.add(secondCommand);
                    battleCombinations.put(firstCommand, newList);
                }
                List<String> listing = Objects.requireNonNull(battleCombinations.get("matches_count"));
                listing.set(0, String.valueOf(Integer.parseInt(listing.get(0)) + 1));
            }
        }
        return battleCombinations;
    }

    public static void saveBattleCombination(Activity activity, Map<String, List<String>> battleCombination) {
        SharedPreferences preferences = activity.getSharedPreferences("battles_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(battleCombination);

        editor.putString("battle_combinations", json);
        editor.apply();
    }

    public static Map<String, List<String>> getBattleCombination(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("battles_preferences", Context.MODE_PRIVATE);

        String json = preferences.getString("battle_combinations", null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
            return gson.fromJson(json, type);
        } else {
            return new HashMap<>();
        }
    }

}
