package ca.uwaterloo.cs446.medaid.medaid;

import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivityHelper {
    public String sortAndConcatMedTimes(Map<Integer, String> timeIDMap) {
        List<String> medTimes = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : timeIDMap.entrySet()) {
            medTimes.add(entry.getValue());
        }

        StringBuilder timesBuilder = new StringBuilder();
        String times;

        // TODO: Sort the times from earliest to latest in the day

        for (String time : medTimes) {
            timesBuilder.append(time);
            timesBuilder.append(",");
        }

        times = timesBuilder.toString();
        int lastCharIndex = times.length() - 1;
        return times.substring(0, lastCharIndex);
    }

    public String getDaysOfTheWeekString(CheckBox[] daysOfTheWeek) {
        StringBuilder daysOfTheWeekStringBuilder = new StringBuilder();
        for (CheckBox checkBox : daysOfTheWeek) {
            if (checkBox.isChecked()) {
                switch (checkBox.getId()) {
                    case R.id.monday:
                        daysOfTheWeekStringBuilder.append("MON");
                        break;
                    case R.id.tuesday:
                        daysOfTheWeekStringBuilder.append("TUES");
                        break;
                    case R.id.wednesday:
                        daysOfTheWeekStringBuilder.append("WED");
                        break;
                    case R.id.thursday:
                        daysOfTheWeekStringBuilder.append("THURS");
                        break;
                    case R.id.friday:
                        daysOfTheWeekStringBuilder.append("FRI");
                        break;
                    case R.id.saturday:
                        daysOfTheWeekStringBuilder.append("SAT");
                        break;
                    case R.id.sunday:
                        daysOfTheWeekStringBuilder.append("SUN");
                        break;
                }
                daysOfTheWeekStringBuilder.append(",");
            }
        }

        // Remove last comma
        String daysOfTheWeekString = daysOfTheWeekStringBuilder.toString();
        int lastCharIndex = daysOfTheWeekString.length() - 1;
        return daysOfTheWeekString.substring(0, lastCharIndex);
    }

    public String getTimesToTakeMedication(int hourOfDay, int minutes) {
        hourOfDay = hourOfDay % 12;
        if (hourOfDay == 0) {
            hourOfDay = 12;
        }

        String minutesString = Integer.toString(minutes);
        if (minutes < 10) {
            minutesString = "0" + minutes;
        }

        return hourOfDay + ":" + minutesString;
    }

    public String getTimesToTakeMedication24Hour(int hourOfDay, int minutes) {
        String minutesString = Integer.toString(minutes);
        if (minutes < 10) {
            minutesString = "0" + minutes;
        }

        return hourOfDay + ":" + minutesString;
    }
}
