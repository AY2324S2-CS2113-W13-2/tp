package seedu.duke;

import commandparser.CommandHandler;
import data.Task;
import data.TaskManager;
import data.exceptions.TaskManagerException;
import log.FileLogger;
import storage.Storage;
import time.DateUtils;
import time.MonthView;
import time.WeekView;
import ui.AvatarUi;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static storage.Storage.createNewFile;

public class Main {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) throws IOException, TaskManagerException{
        FileLogger.setupLogger();
        Scanner scanner = new Scanner(System.in);
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = DateUtils.getStartOfWeek(today);
        WeekView weekView = new WeekView(startOfWeek, dateFormatter);
        TaskManager taskManager = new TaskManager();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        MonthView monthView = new MonthView(startOfMonth, dateFormatter);

        //Creates directory and tasks.txt file if it does not exist
        createNewFile(Storage.FILE_PATH);
        //Reads tasks from txt file
        Map<LocalDate, List<Task>> tasksFromFile =
                Storage.loadTasksFromFile(Storage.FILE_PATH);
        //Loads tasks from txt file
        taskManager.addTasksFromFile(tasksFromFile);

        AvatarUi.printWelcomeMessage();

        CommandHandler commandHandler = new CommandHandler(scanner, taskManager, weekView, monthView);

        while (true) {
            commandHandler.handleCommand();
        }
    }
}
