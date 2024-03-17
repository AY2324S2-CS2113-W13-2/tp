package Time;
import data.TaskManager;
import static ui.UiRenderer.printWeekHeader;
import static ui.UiRenderer.printSeparator;
import static ui.UiRenderer.printWeekDays;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class WeekView {
    private LocalDate startOfWeek;
    private final DateTimeFormatter dateFormatter;

    public WeekView(LocalDate startOfWeek, DateTimeFormatter dateFormatter) {
        this.startOfWeek = startOfWeek;
        this.dateFormatter = dateFormatter;
    }

    public void printWeekView(TaskManager taskManager) {
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        System.out.println("\nWeek View: " + dateFormatter.format(startOfWeek) + " - " + dateFormatter.format(endOfWeek));

        printWeekHeader();
        printWeekDays(startOfWeek, dateFormatter, taskManager);
    }

    public void nextWeek() {
        startOfWeek = startOfWeek.plusWeeks(1);
    }

    public void previousWeek() {
        startOfWeek = startOfWeek.minusWeeks(1);
    }

    public LocalDate getStartOfWeek() {
        return startOfWeek;
    }

    public void printMonthView(TaskManager taskManager) {
        YearMonth yearMonth = YearMonth.from(startOfWeek);
        LocalDate firstOfMonth = startOfWeek.withDayOfMonth(1);
        LocalDate lastOfMonth = yearMonth.atEndOfMonth();
        LocalDate current = firstOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        // Create a formatter that only prints the day of the month
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("d");

        System.out.println("\nMonth View: " + yearMonth.getMonth() + " " + yearMonth.getYear());
        printWeekHeader();

        while (current.isBefore(firstOfMonth.plusMonths(1))) {
            for (int i = 0; i < 7; i++) {
                if (current.getMonth().equals(yearMonth.getMonth())) {
                    // Use the dayFormatter to print only the day number
                    System.out.printf("| %-10s ", dayFormatter.format(current));
                } else {
                    System.out.print("|            ");
                }
                current = current.plusDays(1);
            }
            System.out.println("|");
            printSeparator();

            // Find the maximum number of tasks for any day in the current week
            LocalDate weekStart = current.minusDays(7);
            int maxTasks = 0;
            for (int i = 0; i < 7; i++) {
                LocalDate date = weekStart.plusDays(i);
                maxTasks = Math.max(maxTasks, taskManager.getTasksForDate(date).size());
            }

            // Print tasks for each day in the current week
            for (int taskIndex = 0; taskIndex < maxTasks; taskIndex++) {
                for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
                    LocalDate date = weekStart.plusDays(dayIndex);
                    List<String> dayTasks = taskManager.getTasksForDate(date);
                    if (taskIndex < dayTasks.size()) {
                        String task = dayTasks.get(taskIndex);
                        System.out.printf("| %-10.10s ", task);
                    } else {
                        System.out.print("|            ");
                    }
                }
                System.out.println("|");
            }

            // Print the separator only if there were tasks in the week
            if (maxTasks > 0) {
                printSeparator();
            }
        }
    }

    public void nextMonth() {
        YearMonth currentMonth = YearMonth.from(startOfWeek);
        startOfWeek = currentMonth.plusMonths(1).atDay(1);
    }

    public void previousMonth() {
        YearMonth currentMonth = YearMonth.from(startOfWeek);
        startOfWeek = currentMonth.minusMonths(1).atDay(1);
    }
}
