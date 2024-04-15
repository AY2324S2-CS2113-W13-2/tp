package data.exceptions;

import java.util.List;

import data.Task;

/**
 * Represents an exception that occurs when marking a task.
 * Inherits from TaskManagerException class.
 * 
 */
public class MarkTaskException extends TaskManagerException {

    public static final String TASK_INDEX_OUT_OF_RANGE_FOR_DAY_WITH_TASKS_MESSAGE = 
            "The task index you attempted to mark is out of range!";
    public static final String TASK_INDEX_WITH_NO_TASKS_MESSAGE = 
            "There are no tasks to mark on this day!";

    /**
     * Constructor for MarkTaskException class.
     * 
     * @param errorMessage The error message to be displayed.
     */
    public MarkTaskException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Method that checks if the task index is valid for the given day.
     * 
     * @param dayTasks The list of tasks for the day.
     * @param taskIndex The index of the task to be marked.
     * @throws MarkTaskException if the task index is invalid.
     */
    public static void checkIfTaskIndexIsValidForMarkingTask(List<Task> dayTasks, int taskIndex)
            throws MarkTaskException {
        
        boolean dayHasNoTasks = (dayTasks == null);
        if (dayHasNoTasks) {
            throw new MarkTaskException(TASK_INDEX_WITH_NO_TASKS_MESSAGE);
        }
        
        int taskListSize = dayTasks.size();
        boolean taskIndexOutOfRange = taskIndex < 0 || taskIndex > taskListSize;
        if (taskIndexOutOfRange) {
            throw new MarkTaskException(TASK_INDEX_OUT_OF_RANGE_FOR_DAY_WITH_TASKS_MESSAGE);
        }
    }
}
