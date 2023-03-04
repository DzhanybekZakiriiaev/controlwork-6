package kz.attractor.java.lesson44;

import java.util.ArrayList;
import java.util.List;

public class DaysDataModel {
    private List<Day> days = new ArrayList<>();

    public DaysDataModel() {
        this.days = FileService.readDaysFile();
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }
}
