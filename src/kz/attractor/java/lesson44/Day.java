package kz.attractor.java.lesson44;

import java.time.LocalDate;
import java.util.List;

public class Day {
    private LocalDate date;
    private List<Task> tasks;

    public Day(LocalDate date, List<Task> tasks) {
        this.date = date;
        this.tasks = tasks;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
