package data.exceptions;

import java.util.List;

import data.Task;

/**
 * Represents an exception that occurs when setting priority to a task.
 * Inherits from TaskManagerException class.
 * 
 */
public class SetPriorityException extends TaskManagerException {

    public static final String TASK_INDEX_OUT_OF_RANGE_FOR_DAY_WITH_TASKS_MESSAGE = 
            "The task index you attempted to set a priority to is out of range!";
    public static final String TASK_INDEX_WITH_NO_TASKS_MESSAGE = 
            "There are no tasks to set a priority to on this day!";
    public static final String INVALID_PRIORITY_MESSAGE = 
            "The priority you entered is invalid! Please enter a valid priority (L, M, H)!";

    /**
     * Constructor for SetPriorityException class.
     * 
     * @param errorMessage The error message to be displayed.
     */
    public SetPriorityException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Method that checks if the task index is valid for the given day.
     * 
     * @param dayTasks The list of tasks for the day.
     * @param taskIndex The index of the task to set a priority to.
     * @throws SetPriorityException if the task index is invalid.
     */
    public static void checkIfTaskIndexIsValidForPriority(List<Task> dayTasks, int taskIndex)
            throws SetPriorityException {
        
        boolean dayHasNoTasks = (dayTasks == null);
        if (dayHasNoTasks) {
            throw new SetPriorityException(TASK_INDEX_WITH_NO_TASKS_MESSAGE);
        }
        
        int taskListSize = dayTasks.size();
        boolean taskIndexOutOfRange = taskIndex < 0 || taskIndex > taskListSize;
        if (taskIndexOutOfRange) {
            throw new SetPriorityException(TASK_INDEX_OUT_OF_RANGE_FOR_DAY_WITH_TASKS_MESSAGE);
        }
    }

    /**
     * Method that checks if the priority entered is a valid string.
     * 
     * @param priority The priority to be checked.
     * @throws SetPriorityException if the priority is invalid.
     */
    public static void checkIfPriorityIsValid(String priority) throws SetPriorityException {
        boolean priorityIsInvalid = 
                !priority.equals("L") && !priority.equals("M") && !priority.equals("H");
        if (priorityIsInvalid) {
            throw new SetPriorityException(INVALID_PRIORITY_MESSAGE);
        }
    }

}
