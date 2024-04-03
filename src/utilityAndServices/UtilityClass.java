package utilityAndServices;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public final class UtilityClass { // for storing different functions that can be used in the project
    public static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String finalDate =  currentDate.format(dateFormat);
        // we format the date as a string using the formatter we created
        return finalDate;
    }

}
