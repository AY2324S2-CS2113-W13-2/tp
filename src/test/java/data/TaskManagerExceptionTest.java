package data;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import time.DateUtils;
import time.WeekView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static data.TaskManagerException.checkIfDateHasTasks;
import static data.TaskManagerException.checkIfDateInCurrentMonth;
import static data.TaskManagerException.checkIfDateInCurrentWeek;

public class TaskManagerExceptionTest {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate today = LocalDate.now();
    LocalDate startOfWeek = DateUtils.getStartOfWeek(today);
    WeekView weekView = new WeekView(startOfWeek, dateFormatter);

    @Test
    public void checkIfDateInCurrentMonth_pastDateGiven_exceptionThrown () {
        TaskManagerException thrown = Assertions.assertThrows(TaskManagerException.class, () -> {
            checkIfDateInCurrentMonth(LocalDate.parse("1111-11-11"));
        }, "TaskManagerException was expected");

        Assertions.assertEquals("The date must be within the current month. " + "Please try again.",
                thrown.getMessage());
    }

    @Test
    public void checkIfDateInCurrentMonth_futureDateGiven_exceptionThrown () {
        TaskManagerException thrown = Assertions.assertThrows(TaskManagerException.class, () -> {
            checkIfDateInCurrentMonth(LocalDate.parse("2222-11-11"));
        }, "TaskManagerException was expected");

        Assertions.assertEquals("The date must be within the current month. " + "Please try again.",
                thrown.getMessage());
    }

    @Test
    public void checkIfDateInCurrentWeek_pastDateGiven_exceptionThrown () {
        TaskManagerException thrown = Assertions.assertThrows(TaskManagerException.class, () -> {
            checkIfDateInCurrentWeek(LocalDate.parse("1111-11-11"), weekView);
        }, "TaskManagerException was expected");

        Assertions.assertEquals("The date must be within the current week. Please try again.",
                thrown.getMessage());
    }

    @Test
    public void checkIfDateInCurrentWeek_futureDateGiven_exceptionThrown () {
        TaskManagerException thrown = Assertions.assertThrows(TaskManagerException.class, () -> {
            checkIfDateInCurrentWeek(LocalDate.parse("2222-11-11"), weekView);
        }, "TaskManagerException was expected");

        Assertions.assertEquals("The date must be within the current week. Please try again.",
                thrown.getMessage());
    }

    @Test
    public void checkIfDateHasTasks_noTasksOnDate_exceptionThrown () {
        List<Task> dayWithNoTasks = new ArrayList<Task>();
        TaskManagerException thrown = Assertions.assertThrows(TaskManagerException.class, () ->
                checkIfDateHasTasks(dayWithNoTasks));

        Assertions.assertEquals("No tasks to delete on this date.", thrown.getMessage());
    }
}
