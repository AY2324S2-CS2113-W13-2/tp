package data;

import java.util.List;

public class SetPriorityException extends TaskManagerException {

    public static final String TASK_INDEX_OUT_OF_RANGE_FOR_DAY_WITH_TASKS_MESSAGE = 
            "The task index you attempted to set a priority to is out of range!";
    public static final String TASK_INDEX_WITH_NO_TASKS_MESSAGE = 
            "There are no tasks to set a priority to on this day!";

    public SetPriorityException(String errorMessage) {
        super(errorMessage);
    }

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

}