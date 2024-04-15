package time;

import data.TaskManager;
import data.exceptions.TaskManagerException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class View {
    protected LocalDate startOfView;
    protected final DateTimeFormatter dateFormatter;

    /**
     * Constructs a View with the specified start date and date formatter.
     *
     * @param startOfView   the start date of the view.
     * @param dateFormatter the date formatter for formatting dates.
     */
    public View(LocalDate startOfView, DateTimeFormatter dateFormatter) {
        this.startOfView = startOfView;
        this.dateFormatter = dateFormatter;
    }

    /**
     * Prints the view with tasks from the task manager.
     *
     * @param taskManager the TaskManager object containing tasks.
     */
    public abstract void printView(TaskManager taskManager);

    /**
     * Moves the view to the next period (e.g., next week or next month).
     */
    public abstract void next();

    /**
     * Moves the view to the previous period (e.g., previous week or previous month).
     */
    public abstract void previous();

    /**
     * Returns the start date of the view.
     *
     * @return the start date of the view
     */
    public LocalDate getStartOfView() {
        return startOfView;
    }

    /**
     * Returns the start date of the month associated with the view.
     *
     * @return the start date of the month
     */
    public LocalDate getStartOfMonth() {
        return startOfView.withDayOfMonth(1);
    }
    /**
     * Finds the date corresponding to a given day number within the same month
     * and year as the start of the view.
     *
     * @param dayInt the day number to find the date for.
     * @return the corresponding date.
     * @throws TaskManagerException if the day number is invalid.
     */
    public LocalDate findDateFromNumber(int dayInt) throws TaskManagerException {
        if (dayInt < 1 || dayInt > 31) {
            throw new TaskManagerException("Invalid day number. Day must be between 1 and 31.");
        }
        LocalDate targetDate = getStartOfMonth().withDayOfMonth(dayInt);
        if (targetDate.getMonth() != getStartOfMonth().getMonth()) {
            throw new TaskManagerException("Invalid day number for the current month.");
        }
        return targetDate;
    }
}
