package data.exceptions;

import data.Task;
import data.TaskManager;
import data.TaskType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static data.TaskManager.addTask;
import static data.TaskManager.deleteAllTasksOnDate;
import static data.TaskManager.getDayTasks;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MarkTaskExceptionTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new TaskManager();
    }

    @AfterEach
    void resetTaskManager() {
        LocalDate date = LocalDate.now();
        deleteAllTasksOnDate(taskManager, date);
    }

    @Test
    public void checkIfTaskIndexIsValidForMarkingTask_validIndexGivenForDayWithTask_noExceptionThrown ()
            throws TaskManagerException {

        // Arrange
        LocalDate date = LocalDate.now();
        String taskDescription = "Test Todo";

        // Act
        TaskType testTaskType = TaskType.TODO;
        String[] dummyTestDates = new String[]{null};
        String[] dummyTestTimes = new String[]{null};
        addTask(date, taskDescription, testTaskType, dummyTestDates, dummyTestTimes);
        List<Task> dayTasks = getDayTasks(date);

        // Assert
        assertDoesNotThrow(() -> MarkTaskException.checkIfTaskIndexIsValidForMarkingTask(dayTasks, 1));

    }

    @Test
    public void checkIfTaskIndexIsValidForMarkingTask_invalidIndexGivenForDayWithTask_exceptionThrown ()
            throws TaskManagerException {

        // Arrange
        LocalDate date = LocalDate.now();
        String taskDescription = "Test Todo";

        // Act
        TaskType testTaskType = TaskType.TODO;
        String[] dummyTestDates = new String[]{null};
        String[] dummyTestTimes = new String[]{null};
        addTask(date, taskDescription, testTaskType, dummyTestDates, dummyTestTimes);
        List<Task> dayTasks = getDayTasks(date);

        // Assert
        MarkTaskException thrown = assertThrows(MarkTaskException.class, () ->
                MarkTaskException.checkIfTaskIndexIsValidForMarkingTask(dayTasks, 2));

        assertEquals("The task index you attempted to mark is out of range!", thrown.getMessage());

    }

    @Test
    public void checkIfTaskIndexIsValidForMarkingTask_indexGivenForDayWithNoTasks_exceptionThrown () {

        // Arrange
        LocalDate date = LocalDate.now();

        // Act
        List<Task> dayTasks = getDayTasks(date);

        // Assert
        MarkTaskException thrown = assertThrows(MarkTaskException.class, () ->
                MarkTaskException.checkIfTaskIndexIsValidForMarkingTask(dayTasks, 1));

        assertEquals("There are no tasks to mark on this day!", thrown.getMessage());

    }
}
