package data;

import storage.Storage;
import time.MonthView;
import time.WeekView;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.exceptions.MarkTaskException;
import data.exceptions.SetPriorityException;
import data.exceptions.TaskManagerException;

import static data.TaskType.DEADLINE;
import static data.TaskType.EVENT;
import static data.TaskType.TODO;
import static data.exceptions.MarkTaskException.checkIfTaskIndexIsValidForMarkingTask;
import static data.exceptions.SetPriorityException.checkIfPriorityIsValid;
import static data.exceptions.SetPriorityException.checkIfTaskIndexIsValidForPriority;
import static data.exceptions.TaskManagerException.checkIfDateHasTasks;
import static data.exceptions.TaskManagerException.checkIfDateInFormat;
import static data.exceptions.TaskManagerException.checkIfDateTimeInFormat;
import static data.exceptions.TaskManagerException.checkIfTaskExistsInCurrentDate;
import static data.exceptions.TaskManagerException.checkIfTimeInFormat;
import static storage.Storage.saveTasksToFile;

/**
 * The TaskManager class manages tasks by providing functionalities to add, delete, and update tasks.
 * It provides methods to add tasks based on their type (Todo, Event, Deadline), delete tasks, update tasks,
 * mark tasks as completed or not completed, set priority levels for tasks, and retrieve tasks for a specific date.
 * It also provides methods to add tasks from a file and print free time slots for a specific date.
 * The class uses a Logger to log information and warnings related to task management.
 */
public class TaskManager {
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final Map<LocalDate, List<Task>> tasks = new HashMap<>();

    /**
     * Adds a task for a specific date.
     *
     * @param date The date for the task.
     * @param taskDescription The description of the task.
     * @param taskType The TaskType of the task to be added.
     * @param dates A String array that contains the relevant dates for the task to be added.
     */
    public static void addTask(LocalDate date, String taskDescription, TaskType taskType,
            String[] dates, String[] times)
            throws TaskManagerException {
        Task taskToAdd;

        switch (taskType) {
        case TODO:
            taskToAdd = new Todo(taskDescription);
            break;

        case EVENT:
            String startDate = dates[0];
            String endDate = dates[1];

            String startTime = times[0];
            checkIfTimeInFormat(startTime);
            String endTime = times[1];
            checkIfTimeInFormat(endTime);

            taskToAdd = new Event(taskDescription, startDate, endDate, startTime, endTime);
            break;

        case DEADLINE:
            String deadlineDate = dates[0];
            String deadlineTime = times[0];
            checkIfTimeInFormat(deadlineTime);
            taskToAdd = new Deadline(taskDescription, deadlineDate, deadlineTime);
            break;

        default:
            throw new TaskManagerException("Invalid task type given. T for Todo, E for event, D for deadline.");
        }

        tasks.computeIfAbsent(date, k -> new ArrayList<>()).add(taskToAdd);
    }

    /**
     * Deletes a task for a specific date and task index.
     * Adds an option to mute system outputs (Used for testing only).
     *
     * @param date The date of the task.
     * @param taskIndex The index of the task to delete.
     * @param isMuted Whether system outputs are muted.
     */
    public void deleteTask(LocalDate date, int taskIndex, boolean isMuted) {
        List<Task> dayTasks = tasks.get(date);
        if (dayTasks != null && taskIndex >= 0 && taskIndex < dayTasks.size()) {
            dayTasks.remove(taskIndex);
            if (dayTasks.isEmpty()) {
                tasks.remove(date);
            }
            if (!isMuted) {
                System.out.println("Task deleted.");
            }
        } else {
            if (!isMuted) {
                System.out.println("The task you are trying to delete does not exist.");
            }
        }
    }


    /**
     * Updates a task for a specific date and task index.
     *
     * @param date              The date of the task.
     * @param taskIndex         The index of the task to update.
     * @param newTaskDescription The updated description of the task.
     * @throws IndexOutOfBoundsException If the task index is out of bounds.
     */
    public static void updateTask(LocalDate date, int taskIndex, String newTaskDescription, Scanner scanner,
            boolean inMonthView, WeekView weekView)
            throws IndexOutOfBoundsException, TaskManagerException {
        try {
            List<Task> dayTasks = getDayTasks(date);
            boolean dayHasTasks = dayTasks != null;
            boolean taskIndexExists = taskIndex >= 0 && taskIndex < Objects.requireNonNull(dayTasks).size();
            assert dayHasTasks;
            assert taskIndexExists;

            String oldDescription = dayTasks.get(taskIndex).getName();
            String currentTaskType = dayTasks.get(taskIndex).getTaskType();

            Task task;
            switch (currentTaskType) {
            case "T":
                task = new Todo(newTaskDescription);
                logger.log(Level.INFO, "Updating task description from " +
                        oldDescription + " to: " + newTaskDescription);
                dayTasks.set(taskIndex, task);
                break;
            case "E":
                task = updateEventTask(scanner, dayTasks, taskIndex, newTaskDescription, oldDescription);
                break;
            case "D":
                task = updateDeadlineTask(scanner, dayTasks, taskIndex, newTaskDescription, oldDescription);
                break;
            default:
                throw new IllegalArgumentException("Invalid task type");
            }

        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Task index is out of bounds.");
        }
    }

    /**
     * Updates the details of an Event task.
     *
     * @param scanner Scanner object to read user input for updating task details.
     * @param dayTasks List of tasks for the day.
     * @param taskIndex Index of the task to be updated.
     * @param newTaskDescription New description for the task.
     * @param oldDescription Old description of the task.
     * @return Updated Task object.
     */
    public static Task updateEventTask(Scanner scanner, List<Task> dayTasks,
            int taskIndex, String newTaskDescription, String oldDescription) throws TaskManagerException {
        Event oldEvent = (Event) dayTasks.get(taskIndex);


        System.out.println("Do you want to update the start and end dates and times? (yes/no)");
        String eventResponse = scanner.nextLine().trim().toLowerCase();
        if (eventResponse.equals("yes")) {
            System.out.println("Enter the new start date, end date, start time and end time, " +
                    "separated by spaces:");
            String[] newDatesAndTimes = scanner.nextLine().trim().split(" ");

            checkIfDateInFormat(newDatesAndTimes[0]);
            checkIfDateInFormat(newDatesAndTimes[1]);
            checkIfTimeInFormat(newDatesAndTimes[2]);
            checkIfTimeInFormat(newDatesAndTimes[3]);

            Task task = new Event(newTaskDescription, newDatesAndTimes[0], newDatesAndTimes[1], newDatesAndTimes[2],
                    newDatesAndTimes[3]);

            tasks.computeIfAbsent(LocalDate.parse(newDatesAndTimes[0], DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    k -> new ArrayList<>()).add(task);
            dayTasks.remove(taskIndex);

            updateEventLogging(newTaskDescription, oldDescription, oldEvent, newDatesAndTimes);
            return task;
        } else {
            Task task = new Event(newTaskDescription, oldEvent.getStartDate(), oldEvent.getEndDate(),
                    oldEvent.getStartTime(), oldEvent.getEndTime());

            logger.log(Level.INFO, "Updating task description from " +
                    oldDescription + " to: " + newTaskDescription);
            dayTasks.set(taskIndex, task);
            return task;
        }
    }

    /**
     * Updates the details of a Deadline task.
     *
     * @param scanner Scanner object to read user input for updating task details.
     * @param dayTasks List of tasks for the day.
     * @param taskIndex Index of the task to be updated.
     * @param newTaskDescription New description for the task.
     * @param oldDescription Old description of the task.
     * @return Updated Task object.
     */
    public static Task updateDeadlineTask(Scanner scanner, List<Task> dayTasks,
            int taskIndex, String newTaskDescription, String oldDescription) {
        Deadline oldDeadline = (Deadline) dayTasks.get(taskIndex);
        System.out.println("Do you want to update the deadline date and time? (yes/no)");
        String deadlineResponse = scanner.nextLine().trim().toLowerCase();
        if (deadlineResponse.equals("yes")) {
            System.out.println("Enter the new deadline date and time, separated by a space:");
            String[] newDatesAndTimes = scanner.nextLine().trim().split(" ");
            Task task = new Deadline(newTaskDescription, newDatesAndTimes[0], newDatesAndTimes[1]);

            tasks.computeIfAbsent(LocalDate.parse(newDatesAndTimes[0], DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    k -> new ArrayList<>()).add(task);
            dayTasks.remove(taskIndex);

            logger.log(Level.INFO, "Updating task description from " +
                    oldDescription + " to: " + newTaskDescription);
            logger.log(Level.INFO, "Updating task deadline date from " + oldDeadline.getByDate() + " to: "
                    + newDatesAndTimes[0]);
            logger.log(Level.INFO, "Updating task deadline time from " + oldDeadline.getByTime() + " to: "
                    + newDatesAndTimes[1]);
            return task;
        } else {
            Task task = new Deadline(newTaskDescription, oldDeadline.getByDate(), oldDeadline.getByTime());

            logger.log(Level.INFO, "Updating task description from " +
                    oldDescription + " to: " + newTaskDescription);

            dayTasks.set(taskIndex, task);
            return task;
        }
    }

    /**
     * Logs the updates made to an Event task.
     *
     * @param newTaskDescription New description for the task.
     * @param oldDescription Old description of the task.
     * @param oldEvent The old Event task that is being updated.
     * @param newDatesAndTimes Array containing the new start and end dates and times for the task.
     */
    private static void updateEventLogging(String newTaskDescription,
            String oldDescription, Event oldEvent, String[] newDatesAndTimes) {
        logger.log(Level.INFO, "Updating task description from " +
                oldDescription + " to: " + newTaskDescription);
        logger.log(Level.INFO, "Updating task start date from " +
                oldEvent.getStartDate() + " to: " + newDatesAndTimes[0]);
        logger.log(Level.INFO, "Updating task end date from " + oldEvent.getEndDate() + " to: "
                + newDatesAndTimes[1]);
        logger.log(Level.INFO, "Updating task start time from " + oldEvent.getStartTime() + " to: "
                + newDatesAndTimes[2]);
        logger.log(Level.INFO, "Updating task end time from " + oldEvent.getEndTime() + " to: "
                + newDatesAndTimes[3]);
    }

    /**
     * Method to get the tasks for a specified date.
     *
     * @param date The date to be checked.
     * @return An ArrayList of Tasks containing the tasks on the specified date.
     */

    public static List<Task> getDayTasks(LocalDate date) {
        return tasks.get(date);
    }

    /**
     * Marks a task as completed.
     *
     * @param date The date of the task.
     * @param taskIndex The index of the task to mark.
     */
    public void markTaskAsCompleted(LocalDate date, int taskIndex) {
        List<Task> dayTasks = tasks.get(date);

        try {
            dayTasks.get(taskIndex).setCompleteness(true);
            System.out.println("Task marked as done.");
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Task index is out of bounds.");
        }
    }

    /**
     * Marks a task as not completed.
     *
     * @param date The date of the task.
     * @param taskIndex The index of the task to mark.
     */
    public void markTaskAsNotCompleted(LocalDate date, int taskIndex) {
        List<Task> dayTasks = tasks.get(date);

        try {
            dayTasks.get(taskIndex).setCompleteness(false);
            System.out.println("Unmarked task.");
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Task index is out of bounds.");
        }
    }

    /**
     * Retrieves tasks for a specific date.
     *
     * @param date The date to retrieve tasks for.
     * @return A list of tasks for the given date.
     */
    public List<Task> getTasksForDate(LocalDate date) {
        return tasks.getOrDefault(date, new ArrayList<>());
    }

    /**
     * Adds a task from user input along with the date.
     *
     * @param scanner The scanner object used to read user input.
     * @param weekView WeekView object to validate the date.
     * @param monthView MonthView object to validate the date.
     * @param inMonthView A boolean indicating whether the view is in month view or not.
     * @param day The String representing the date to add the task in.
     * @param taskTypeString The String representing the taskType of the task to be added.
     * @param taskDescription The String representing the description of the task to be added.
     * @throws TaskManagerException If there is an error in managing tasks.
     * @throws DateTimeParseException If there is an error parsing the date.
     */
    public void addManager(Scanner scanner, WeekView weekView, MonthView monthView, boolean inMonthView, String action,
            String day, String taskTypeString, String taskDescription)
            throws TaskManagerException,DateTimeParseException {

        // Convert the day to a LocalDate
        LocalDate date;

        // Convert dayString to int
        int dayInt = Integer.parseInt(day);

        date = findDateFromDayNumber(weekView, monthView, inMonthView, dayInt);

        // Parse the task type
        TaskType taskType = parseTaskType(taskTypeString.toUpperCase());
        String typeName = taskType.equals(TODO) ? "Todo" : taskType.equals(DEADLINE) ? "Deadline" : "Event";

        //Add task based on type
        addTaskBasedOnType(scanner, taskDescription, taskType, date);

        // Save tasks to file
        saveTasksToFile(tasks, Storage.FILE_PATH); // Update tasks.txt file
        System.out.println(typeName + " added.");
    }

    /**
     * Adds a task based on its type.
     *
     * @param scanner Scanner object to read user input for task details.
     * @param taskDescription Description of the task.
     * @param taskType Type of the task (TODO, EVENT, DEADLINE).
     * @param date Date for the task.
     * @throws TaskManagerException If an invalid task type is provided.
     */
    private static void addTaskBasedOnType(Scanner scanner, String taskDescription,
            TaskType taskType, LocalDate date) throws TaskManagerException {
        if (taskType == null) {
            throw new TaskManagerException("Invalid task type. Please provide valid task type: " +
                    "T for Todo, E for event, D for deadline.");
        } else if (taskType == DEADLINE) {
            parseAndAddDeadline(scanner, taskDescription, taskType, date);
        } else if (taskType == EVENT) {
            parseAndAddEvent(scanner, taskDescription, taskType, date);
        } else {
            // dummy String array to pass into function call
            String[] dummyDates = {null};
            // dummy String array to pass into function call
            String[] dummyTimes = {null};
            // Call function to add tasks
            addTask(date, taskDescription, taskType, dummyDates, dummyTimes);
        }
    }

    /**
     * Parses and adds an Event task.
     *
     * @param scanner Scanner object to read user input for task details.
     * @param taskDescription Description of the task.
     * @param taskType Type of the task (EVENT).
     * @param date Date for the task.
     * @throws TaskManagerException If an invalid task type is provided or if there is an error in date/time format.
     */
    private static void parseAndAddEvent(Scanner scanner, String taskDescription,
            TaskType taskType, LocalDate date) throws TaskManagerException {
        System.out.println("Enter the start date of this task, along with the start time separated by a space:");
        String inputStartDateAndTime = scanner.nextLine().trim();
        checkIfDateTimeInFormat(inputStartDateAndTime);
        String[] startDateAndTime = inputStartDateAndTime.split(" ");
        String startDate = startDateAndTime[0];
        String startTime = startDateAndTime[1];

        System.out.println("Enter the end date of this task, along with the end time separated by a space:");
        String inputEndDateAndTime = scanner.nextLine().trim();
        checkIfDateTimeInFormat(inputEndDateAndTime);
        String[] endDateAndTime = inputEndDateAndTime.split(" ");
        String endDate = endDateAndTime[0];
        String endTime = endDateAndTime[1];

        String [] startAndEndDates = new String[]{startDate, endDate};
        String [] startAndEndTimes = new String[]{startTime, endTime};

        addTask(date, taskDescription, taskType, startAndEndDates, startAndEndTimes);
    }

    /**
     * Parses and adds a Deadline task.
     *
     * @param scanner Scanner object to read user input for task details.
     * @param taskDescription Description of the task.
     * @param taskType Type of the task (DEADLINE).
     * @param date Date for the task.
     * @throws TaskManagerException If an invalid task type is provided or if there is an error in date/time format.
     */
    private static void parseAndAddDeadline(Scanner scanner, String taskDescription,
            TaskType taskType, LocalDate date) throws TaskManagerException {
        System.out.println("Enter the deadline date and time of this task, separated by a space:");
        String inputDeadlineDateAndTime = scanner.nextLine().trim();
        checkIfDateTimeInFormat(inputDeadlineDateAndTime);
        String[] deadlineDateAndTime = inputDeadlineDateAndTime.split(" ");
        String[] deadlineDate = new String[]{deadlineDateAndTime[0]};
        String[] deadlineTime = new String[]{deadlineDateAndTime[1]};

        addTask(date, taskDescription, taskType, deadlineDate, deadlineTime);
    }


    /**
     * @param weekView The WeekView object for finding the date.
     * @param monthView The MonthView object for finding the date.
     * @param inMonthView A boolean indicating whether the view is in month view or not.
     * @param day The day of the task to show free times for.
     * @throws TaskManagerException If there is an error in managing tasks.
     * @throws DateTimeParseException If there is an error parsing the date.
     */
    public void freeTimesManager(WeekView weekView, MonthView monthView, boolean inMonthView, String day)
            throws TaskManagerException, DateTimeParseException {
        LocalDate date;
        int dayInt = Integer.parseInt(day);
        date = findDateFromDayNumber(weekView, monthView, inMonthView, dayInt);

        List<Task> eventsForDate = getEventsForDate(date);
        List<String> freeTimes = getFreeTimeSlots(eventsForDate, date);

        printFreeTimeSlots(freeTimes, date);
    }

    /**
     * Marks a task as completed or not completed based its current marked status.
     *
     * @param weekView WeekView object for finding the date.
     * @param monthView MonthView object for finding the date.
     * @param inMonthView A boolean indicating whether the view is in month view or not.
     * @param day The day of the task to be marked.
     * @param taskIndex The index of the task to be marked.
     * @throws TaskManagerException If the date given is not in the current month or week being viewed.
     * @throws DateTimeParseException If there is an error parsing the date.
     */
    public void markManager(WeekView weekView, MonthView monthView, boolean inMonthView, String day, int taskIndex)
            throws TaskManagerException, DateTimeParseException, MarkTaskException {
        LocalDate date;
        int dayInt = Integer.parseInt(day);
        date = findDateFromDayNumber(weekView, monthView, inMonthView, dayInt);

        List<Task> dayTasks = tasks.get(date);
        checkIfTaskIndexIsValidForMarkingTask(dayTasks, taskIndex);

        handleMarkingOfTask(taskIndex, date);
        saveTasksToFile(tasks, Storage.FILE_PATH);
    }

    /**
     * Finds the date based on the day number.
     *
     * @param weekView WeekView object for finding the date.
     * @param monthView MonthView object for finding the date.
     * @param inMonthView A boolean indicating whether the view is in month view or not.
     * @param dayInt The day number to find the date for.
     * @return The date corresponding to the day number.
     * @throws TaskManagerException If the date is not in the current month or week being viewed.
     */
    private static LocalDate findDateFromDayNumber(WeekView weekView, MonthView monthView,
            boolean inMonthView, int dayInt) throws TaskManagerException {
      
        if (dayInt < 1 || dayInt > 31) {
            throw new TaskManagerException("Invalid day number. Day must be between 1 and 31.");
        }

        if (inMonthView) {
            LocalDate startOfMonth = monthView.getStartOfMonth();
            int daysInMonth = startOfMonth.lengthOfMonth();
            if (dayInt > daysInMonth) {
                throw new TaskManagerException("Invalid day for month view. Please enter a day between 1 and "
                        + daysInMonth + ".");
            }
            return startOfMonth.withDayOfMonth(dayInt);
        } else {
            LocalDate startOfWeek = weekView.getStartOfWeek();
            LocalDate endOfWeek = startOfWeek.plusDays(6);
            LocalDate startOfMonth = startOfWeek.withDayOfMonth(1);
            LocalDate possibleDate = startOfMonth.plusDays(dayInt - 1);

            boolean isBeforeStartOfWeek = possibleDate.isBefore(startOfWeek);
            boolean isNotInSameMonth = possibleDate.getMonth() != startOfWeek.getMonth();
            boolean dayIntRefersToNextMonth = isBeforeStartOfWeek || isNotInSameMonth;

            if (dayIntRefersToNextMonth) {
                LocalDate startOfNextMonth = startOfMonth.plusMonths(1).withDayOfMonth(dayInt);
                if (startOfNextMonth.isAfter(endOfWeek)) {
                    throw new TaskManagerException("Invalid day for week view." +
                            " Please enter a day that falls within the current week.");
                }
                return startOfNextMonth;
            } else {
                if (possibleDate.isBefore(startOfWeek) || possibleDate.isAfter(endOfWeek)) {
                    throw new TaskManagerException("Invalid day for week view." +
                            " Please enter a day that falls within the current week.");
                }
                return possibleDate;
            }
        }
    }

    /**
     * Handles the marking of a task based on the task index and date.
     *
     * @param taskIndex The index of the task to be marked.
     * @param date The date of the task to be marked.
     */
    private void handleMarkingOfTask(int taskIndex, LocalDate date) {
        logger.log(Level.INFO, "Marking task at index " + taskIndex + " for date " + date);
        assert tasks.get(date) != null;
        boolean taskIsCompleted = tasks.get(date).get(taskIndex - 1).isCompleted();
        if (taskIsCompleted) {
            markTaskAsNotCompleted(date, taskIndex - 1);
        } else {
            markTaskAsCompleted(date, taskIndex - 1);
        }
    }

    /**
     * Performs the setting of priority levels based on the user's input.
     *
     * @param weekView WeekView object for finding the date.
     * @param monthView MonthView object for finding the date.
     * @param inMonthView A boolean indicating whether the view is in month view or not.
     * @param day The day of the task to be marked.
     * @param taskIndex The index of the task to be marked.
     * @param priorityLevelString The priority level to set the task to.
     * @throws TaskManagerException If there is an error in managing tasks.
     * @throws DateTimeParseException If there is an error parsing the date.
     */
    public void priorityManager(WeekView weekView, MonthView monthView, boolean inMonthView, String day,
            int taskIndex, String priorityLevelString)
            throws TaskManagerException, DateTimeParseException, SetPriorityException {
        LocalDate date;
        int dayInt = Integer.parseInt(day);

        date = findDateFromDayNumber(weekView, monthView, inMonthView, dayInt);

        List<Task> dayTasks = tasks.get(date);
        checkIfTaskIndexIsValidForPriority(dayTasks, taskIndex);
        checkIfPriorityIsValid(priorityLevelString);

        setPriorityLevelOfTask(taskIndex, date, priorityLevelString);
        saveTasksToFile(tasks, Storage.FILE_PATH);
    }

    /**
     * Sets the priority level of a task.
     *
     * @param taskIndex The index of the task to set the priority level for.
     * @param date The date of the task to set the priority level for.
     * @param priorityLevelString The priority level to set the task to.
     */
    private void setPriorityLevelOfTask(int taskIndex, LocalDate date, String priorityLevelString) {
        logger.log(Level.INFO, "Setting priority level of task at index " + taskIndex + " for date " + date);
        assert tasks.get(date) != null;
        List<Task> dayTasks = tasks.get(date);
        Task task = dayTasks.get(taskIndex - 1);
        TaskPriorityLevel priorityLevelToSet =
                priorityLevelString.equals("H") ? TaskPriorityLevel.HIGH :
                priorityLevelString.equals("M") ? TaskPriorityLevel.MEDIUM :
                TaskPriorityLevel.LOW;
        task.setPriorityLevel(priorityLevelToSet);
    }


    /**
     * Method that parses the TaskType to be specified based on the user's input.
     *
     * @param userInput The String containing the user's cleaned input.
     * @return TaskType of the user's choosing.
     */
    public static TaskType parseTaskType(String userInput) {
        TaskType currentTaskType;
        switch (userInput) {
        case "T":
            currentTaskType = TODO;
            break;

        case "D":
            currentTaskType = DEADLINE;
            break;

        case "E":
            currentTaskType = EVENT;
            break;

        default:
            currentTaskType = null;
            // intentional fallthrough since addTask method checks for invalid taskType already
        }

        return currentTaskType;
    }

    /**
     * Prompts user for updated task description and updates task in the task list and tasks.txt file.
     *
     * @param scanner Scanner object to read user input for task details.
     * @param weekView WeekView object for finding the date.
     * @param monthView MonthView object for finding the date.
     * @param inMonthView A boolean indicating whether the view is in month view or not.
     * @param taskManager The TaskManager instance being used.
     * @param day The int representing the day containing the task to be updated.
     * @param taskIndex The int representing the task index to be updated.
     * @param newDescription The String with the new task description.
     * @throws TaskManagerException If not in correct week/month view.
     * @throws DateTimeParseException If there is an error parsing the date.
     */
    public void updateManager(Scanner scanner, WeekView weekView, MonthView monthView, boolean inMonthView,
            TaskManager taskManager,int day, int taskIndex, String newDescription)
            throws TaskManagerException, DateTimeParseException {

        // Convert the day to a LocalDate
        LocalDate date;

        date = findDateFromDayNumber(weekView, monthView, inMonthView, day);

        checkIfTaskExistsInCurrentDate(taskManager.getTasksForDate(date), taskIndex);

        checkIfDateHasTasks(taskManager.getTasksForDate(date));

        String currentTaskType = taskManager.getTasksForDate(date).get(taskIndex - 1).getTaskType();
        String typeName = currentTaskType.equals("T") ? "Todo" : currentTaskType.equals("D") ? "Deadline" : "Event";

        updateTask(date, taskIndex - 1, newDescription, scanner, inMonthView, weekView);
        saveTasksToFile(tasks,Storage.FILE_PATH); //Update tasks.txt file
        System.out.println(typeName + " updated.");

    }


    //@@author kyhjonathan
    /**
     * Adds tasks from a file to the TaskManager.
     *
     * @param tasksFromFile A map containing tasks read from a file.
     * @throws TaskManagerException If there is an error adding tasks.
     */
    public void addTasksFromFile(Map<LocalDate, List<Task>> tasksFromFile) throws TaskManagerException {
        for (Map.Entry<LocalDate, List<Task>> entry : tasksFromFile.entrySet()) {
            LocalDate date = entry.getKey();
            List<Task> tasksList = entry.getValue();

            for (Task task : tasksList) {
                String taskDescription = task.getName();
                TaskType taskType = parseTaskType(task.getTaskType());
                String[] dates = new String[]{null, null};
                String[] times = new String[]{null, null};

                switch (taskType) {
                case TODO:
                    dates[0] = taskDescription;
                    break;

                case EVENT:
                    String startDate = task.getStartDate();
                    String endDate = task.getEndDate();

                    String startTime = task.getStartTime();
                    String endTime = task.getEndTime();

                    dates[0] = startDate;
                    dates[1] = endDate;

                    times[0] = startTime;
                    times[1] = endTime;
                    break;

                case DEADLINE:
                    String deadlineDate = task.getByDate();
                    String deadlineTime = task.getByTime();

                    dates[0] = deadlineDate;
                    times[0] = deadlineTime;
                    break;

                default:
                    logger.log(Level.INFO, "Task to add was invalid. Task in question: " + taskDescription);
                }

                addTask(date, taskDescription, taskType, dates, times);
            }
        }
    }

    /**
     * Prompts user for task description and deletes task from hashmap and tasks.txt file.
     *
     * @param weekView Current week being viewed.
     * @param inMonthView Whether month is being viewed.
     * @param taskManager The taskManager class being used.
     * @param day The String containing the day being viewed.
     * @param monthView The MonthView instance being used.
     * @param taskIndex The int representing the task index.
     * @throws TaskManagerException If not in correct week/month view
     * @throws DateTimeParseException If there is an error parsing the date.
     */
    public static void deleteManager(WeekView weekView,MonthView monthView, boolean inMonthView,
            TaskManager taskManager,String day, int taskIndex)
            throws TaskManagerException, DateTimeParseException {

        // Convert the day to a LocalDate
        LocalDate date;

        int dayInt = Integer.parseInt(day);
        date = findDateFromDayNumber(weekView, monthView, inMonthView, dayInt);

        // Delete the task based on the parsed inputs
        taskManager.deleteTask(date, taskIndex - 1, false);
        // Subtract 1 to convert to zero-based index
        //System.out.println("Task deleted.");

        // Save tasks to file
        saveTasksToFile(tasks, Storage.FILE_PATH); // Update tasks.txt file
    }

    /**
     * Function to delete all tasks on a specified date.
     * Currently only used to complement JUnit testing.
     *
     * @param taskManager The taskManager class in use.
     * @param specifiedDate The date on which all tasks are to be deleted.
     */

    public static void deleteAllTasksOnDate (TaskManager taskManager, LocalDate specifiedDate) {
        List<Task> dayTasks = tasks.get(specifiedDate);
        if (dayTasks != null) {
            int numOfTasks = dayTasks.size();
            for (int i = numOfTasks; i >= 0; i--) {
                taskManager.deleteTask(specifiedDate, i - 1, true);
            }
        }
    }

    /**
     * Retrieves all Event tasks for a specific date.
     *
     * @param date The date to retrieve Event tasks for.
     * @return A list of Event tasks for the given date.
     */
    public static List<Task> getEventsForDate(LocalDate date) {
        List<Task> events = new ArrayList<>();
        List<Task> taskList = tasks.get(date);
        if (taskList != null) {
            for (Task task : taskList) {
                if (task.getTaskType().equals("E")) {
                    events.add(task);
                }
            }
        }
        return events;
    }

    /**
     * Retrieves all free time slots for a specific date.
     *
     * @param events A list of Event tasks for the date.
     * @param currentDate The date to show free times for.
     * @return A list of free time slots for the given date.
     */
    public List<String> getFreeTimeSlots(List<Task> events, LocalDate currentDate) {
        List<String> freeTimeSlots = new ArrayList<>();
        LocalTime startOfDay = LocalTime.of(0, 0);
        LocalTime endOfDay = LocalTime.of(23, 59);

        // Sort events by start time and date
        events.sort((e1, e2) -> {
            if (e1.getStartDate().equals(e2.getStartDate())) {
                return LocalTime.parse(e1.getStartTime(), DateTimeFormatter.ofPattern("HHmm"))
                        .compareTo(LocalTime.parse(e2.getStartTime(), DateTimeFormatter.ofPattern("HHmm")));
            } else {
                return e1.getStartDate().compareTo(e2.getStartDate());
            }
        });

        // Initialize the last end time to the start of the day for the first day
        // Initialize the last end time to the start of the day for the first day
        LocalTime lastEndTime = startOfDay;

        for (Task event : events) {
            // Parse the start and end dates and times of the event as LocalDate and LocalTime
            LocalDate eventStartDate = LocalDate.parse(event.getStartDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate eventEndDate = LocalDate.parse(event.getEndDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime eventStartTime = LocalTime.parse(event.getStartTime(), DateTimeFormatter.ofPattern("HHmm"));
            LocalTime eventEndTime = LocalTime.parse(event.getEndTime(), DateTimeFormatter.ofPattern("HHmm"));

            // Only process events that start on the current date
            if (eventStartDate.isEqual(currentDate)) {
                // If the event ends on a different day, adjust the end time to the end of the current day
                if (!eventEndDate.isEqual(currentDate)) {
                    eventEndTime = endOfDay;
                }

                // Add free time slot before the event
                if (Duration.between(lastEndTime, eventStartTime).toMinutes() > 0) {
                    freeTimeSlots.add(lastEndTime + " - " + eventStartTime);
                }

                // Update the last end time
                lastEndTime = eventEndTime;
            }
        }

        // Add remaining time of the day to free slots only if lastEndTime is not endOfDay
        if (!lastEndTime.equals(endOfDay) && Duration.between(lastEndTime, endOfDay).toMinutes() > 0) {
            freeTimeSlots.add(lastEndTime + " - " + endOfDay.toString());
        }

        return freeTimeSlots;
    }

    /**
     * Prints the free time slots for a specific date.
     *
     * @param freeTimeSlots A list of free time slots for the date.
     * @param startDate The date to show free times for.
     */
    public void printFreeTimeSlots(List<String> freeTimeSlots, LocalDate startDate) {

        System.out.println("Free time slots for " + startDate + ":");

        for (String slot : freeTimeSlots) {
            System.out.println(slot);
        }
    }
}
