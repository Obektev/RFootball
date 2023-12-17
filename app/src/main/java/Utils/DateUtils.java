package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtils {

    public static List<String> generateDateList(String startDate, int numberOfDays) {
        List<String> dateList = new ArrayList<>();

        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMM yy", new Locale("ru", "RU"));
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yy", new Locale("ru", "RU"));

        try {
            Date currentDate = inputFormat.parse(startDate);

            for (int i = 0; i < numberOfDays; i++) {
                assert currentDate != null;
                dateList.add(outputFormat.format(currentDate));

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                currentDate = calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateList;
    }

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy", new Locale("ru", "RU"));

        return dateFormat.format(new Date());
    }
}
