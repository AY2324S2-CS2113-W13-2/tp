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

public class SetPriorityExceptionTest {

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
    public void checkIfTaskIndexIsValidForPriority_validIndexGivenForDayWithTask_noExceptionThrown ()
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
        assertDoesNotThrow(() -> SetPriorityException.checkIfTaskIndexIsValidForPriority(dayTasks, 1));

    }

    @Test
    public void checkIfTaskIndexIsValidForPriority_invalidIndexGivenForDayWithTask_exceptionThrown ()
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
        SetPriorityException thrown = assertThrows(SetPriorityException.class, () ->
                SetPriorityException.checkIfTaskIndexIsValidForPriority(dayTasks, 2));

        assertEquals("The task index you attempted to set a priority to is out of range!", thrown.getMessage());

    }

    @Test
    public void checkIfTaskIndexIsValidForPriority_indexGivenForDayWithNoTasks_exceptionThrown () {

        // Arrange
        LocalDate date = LocalDate.now();

        // Act
        List<Task> dayTasks = getDayTasks(date);

        // Assert
        SetPriorityException thrown = assertThrows(SetPriorityException.class, () ->
                SetPriorityException.checkIfTaskIndexIsValidForPriority(dayTasks, 1));

        assertEquals("There are no tasks to set a priority to on this day!", thrown.getMessage());

    }

    @Test
    public void checkIfPriorityIsValid_validHPriorityGiven_noExceptionThrown () {
        // Arrange
        String priorityString = "H";

        // Act and Assert
        assertDoesNotThrow(() -> SetPriorityException.checkIfPriorityIsValid(priorityString));
    }

    @Test
    public void checkIfPriorityIsValid_validMPriorityGiven_noExceptionThrown () {
        // Arrange
        String priorityString = "M";

        // Act and Assert
        assertDoesNotThrow(() -> SetPriorityException.checkIfPriorityIsValid(priorityString));
    }

    @Test
    public void checkIfPriorityIsValid_validLPriorityGiven_noExceptionThrown () {
        // Arrange
        String priorityString = "L";

        // Act and Assert
        assertDoesNotThrow(() -> SetPriorityException.checkIfPriorityIsValid(priorityString));
    }

    @Test
    public void checkIfPriorityIsValid_invalidPriorityGiven_exceptionThrown () {
        // Arrange
        String priorityString = "low";

        // Act and Assert
        SetPriorityException thrown = assertThrows(SetPriorityException.class, () ->
                SetPriorityException.checkIfPriorityIsValid(priorityString));
        assertEquals("The priority you entered is invalid! Please enter a valid priority (L, M, H)!",
                thrown.getMessage());
    }

    @Test
    public void checkIfPriorityIsValid_integerGivenAsInput_exceptionThrown () {
        // Arrange
        String priorityString = "995";

        // Act and Assert
        SetPriorityException thrown = assertThrows(SetPriorityException.class, () ->
                SetPriorityException.checkIfPriorityIsValid(priorityString));
        assertEquals("The priority you entered is invalid! Please enter a valid priority (L, M, H)!",
                thrown.getMessage());
    }
}
