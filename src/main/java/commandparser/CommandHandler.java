package commandparser;

import data.TaskManager;
import data.exceptions.TaskManagerException;
import time.MonthView;
import time.WeekView;
import ui.AvatarUi;
import java.util.Scanner;

/**
 * The CommandHandler class handles user commands by delegating to appropriate methods based on the command type.
 * It provides methods to handle various commands such as "next", "prev", "update", "add", "delete", "mark", "free",
 * "priority", "month", "week", "help", and "quit".
 * The class uses a Scanner object for user input, a TaskManager object for managing tasks, a WeekView object for
 * displaying weekly tasks, and a MonthView object for displaying monthly tasks.
 */
public class CommandHandler {
    private final Scanner scanner;
    private final TaskManager taskManager;
    private final WeekView weekView;
    private final MonthView monthView;
    private boolean inMonthView;


    /**
     * Constructs a CommandHandler with the specified dependencies.
     *
     * @param scanner    the Scanner object for user input.
     * @param taskManager the TaskManager object for managing tasks.
     * @param weekView   the WeekView object for displaying weekly tasks.
     * @param monthView  the MonthView object for displaying monthly tasks.
     */
    public CommandHandler(Scanner scanner, TaskManager taskManager, WeekView weekView, MonthView monthView) {
        this.scanner = scanner;
        this.taskManager = taskManager;
        this.weekView = weekView;
        this.monthView = monthView;
        this.inMonthView = false;
    }

    /**
     * Handles user commands by delegating to appropriate methods based on the command type.
     */
    public void handleCommand() {
        AvatarUi.printAvatar();
        if (!inMonthView) {
            weekView.printView(taskManager);
        } else {
            monthView.printView(taskManager);
        }

        System.out.println("Enter help to learn commands");
        String input = scanner.nextLine().trim().toLowerCase();
        String[] parts = input.split(",\\s*");
        String command = parts[0];

        switch (command) {
        case "next":
            handleNextCommand();
            break;
        case "prev":
            handlePreviousCommand();
            break;
        case "update":
            handleUpdateCommand(parts);
            break;
        case "add":
            handleAddCommand(parts);
            break;
        case "delete":
            handleDeleteCommand(parts);
            break;
        case "mark":
            handleMarkCommand(parts);
            break;
        case "free":
            handleFreeCommand(parts);
            break;
        case "priority":
            handlePriorityCommand(parts);
            break;
        case "month":
            handleMonthCommand();
            break;
        case "week":
            handleWeekCommand();
            break;
        case "help":
            printHelp();
            break;
        case "quit":
            handleQuitCommand();
            break;
        default:
            System.out.println("Invalid input. Please try again.");
        }
    }

    /**
     * Handles the "next" command to navigate to the next week or month.
     */
    private void handleNextCommand() {
        if (inMonthView) {
            monthView.next();
        } else {
            weekView.next();
        }
    }

    /**
     * Handles the "prev" command to navigate to the previous week or month.
     */
    private void handlePreviousCommand() {
        if (inMonthView) {
            monthView.previous();
        } else {
            weekView.previous();
        }
    }

    /**
     * Handles the "update" command to update task descriptions.
     *
     * @param parts the array of command parts.
     */
    private void handleUpdateCommand(String[] parts) {
        try {
            StringParser.validateUpdateCommand(parts);
            int day = StringParser.parseTaskIndex(parts[1]);
            int taskIndex = StringParser.parseTaskIndex(parts[2]);
            String newDescription = StringParser.parseTaskDescription(parts[3]);
            taskManager.updateManager(scanner, weekView, monthView, inMonthView, taskManager, day,
                    taskIndex, newDescription);
        } catch (TaskManagerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles the "add" command to add new tasks.
     *
     * @param parts the array of command parts.
     */
    private void handleAddCommand(String[] parts) {
        try {
            StringParser.validateAddCommand(parts);
            String day = parts[1].trim();
            String taskTypeString = parts[2].trim();
            String taskDescription = StringParser.parseTaskDescription(parts[3]);
            int dayIndex = StringParser.parseTaskIndex(day);
            taskManager.addManager(scanner, weekView, monthView, inMonthView, "add", day,
                    taskTypeString, taskDescription);
        } catch (TaskManagerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles the "delete" command to delete tasks.
     *
     * @param parts the array of command parts.
     */
    private void handleDeleteCommand(String[] parts) {
        try {
            StringParser.validateDeleteCommand(parts);
            String day = parts[1].trim();
            int taskIndex = StringParser.parseTaskIndex(parts[2]);
            int dayIndex = StringParser.parseTaskIndex(day);
            TaskManager.deleteManager(weekView, monthView, inMonthView, taskManager, day, taskIndex);
        } catch (TaskManagerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles the "mark" command to mark tasks as completed.
     *
     * @param parts the array of command parts.
     */
    private void handleMarkCommand(String[] parts) {
        try {
            StringParser.validateMarkCommand(parts);
            String day = parts[1].trim();
            int taskIndex = StringParser.parseTaskIndex(parts[2]);
            int dayIndex = StringParser.parseTaskIndex(day);
            taskManager.markManager(weekView, monthView, inMonthView, day, taskIndex);
        } catch (TaskManagerException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleFreeCommand(String[] parts) {
        try {
            StringParser.validateFreeCommand(parts);

            String day = parts[1].trim();
            taskManager.freeTimesManager(weekView, monthView, inMonthView, day);

        } catch (TaskManagerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles the "priority" command to set task priorities.
     *
     * @param parts the array of command parts.
     */
    private void handlePriorityCommand(String[] parts) {
        try {
            StringParser.validatePriorityCommand(parts);
            String day = parts[1].trim();
            int taskIndex = StringParser.parseTaskIndex(parts[2]);
            String priorityLevel = StringParser.parsePriorityLevel(parts[3]);
            int dayIndex = StringParser.parseTaskIndex(day);
            taskManager.priorityManager(weekView, monthView, inMonthView, day, taskIndex, priorityLevel);
        } catch (TaskManagerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles the "month" command to switch to the month view.
     */
    private void handleMonthCommand() {
        inMonthView = true;
    }

    /**
     * Handles the "week" command to switch to the week view.
     */
    private void handleWeekCommand() {
        inMonthView = false;
    }

    /**
     * Prints the help message with available commands.
     */
    private static void printHelp() {
        ui.UiRenderer.printHelp();
    }

    /**
     * Handles the "quit" command to exit the calendar application.
     */
    private void handleQuitCommand() {
        System.out.println("Exiting Calendar...");
        System.exit(0);
    }
}
