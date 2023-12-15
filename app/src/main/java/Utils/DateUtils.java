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

        // Define the input and output date formats
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yy", new Locale("ru", "RU"));

        try {
            // Parse the input start date
            Date currentDate = inputFormat.parse(startDate);

            // Add each formatted date to the list
            for (int i = 0; i < numberOfDays; i++) {
                dateList.add(outputFormat.format(currentDate));

                // Increment the date by one day
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
}
