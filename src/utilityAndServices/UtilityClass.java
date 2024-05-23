package utilityAndServices;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public final class UtilityClass { // for storing different functions that can be used in the project
    public static String getCurrentDate() {
        LocalDateTime currentDate = LocalDateTime.now();

        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            String finalDate = currentDate.format(dateFormat);
            // we format the date as a string using the formatter we created
            return finalDate;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "error";
        }
    }
}
