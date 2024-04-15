package storage;

import data.exceptions.StorageFileException;
import data.exceptions.TaskManagerException;
import data.Task;
import data.TaskPriorityLevel;
import data.TaskType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static data.exceptions.StorageFileException.checkStorageTextDateFormat;

import static data.TaskManager.addTask;
import static data.TaskManager.getDayTasks;
import static data.TaskManager.parseTaskType;
import static data.TaskType.DEADLINE;
import static data.TaskType.EVENT;


/**
 * The Storage class handles all operations related to storing and retrieving tasks from a file.
 * It provides methods to create a new file, save tasks to a file, load tasks from a file, and check the file format.
 * It also provides methods to configure the statuses of tasks after they are loaded from the file.
 * The class uses a Logger to log information and warnings related to file handling and task management.
 */
public class Storage {

    public static final Path FILE_PATH = Path.of("./save/clintonData.txt");
    private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Creates directory and tasks.txt if it does not exist.
     *
     * @param path File Path of clintonData.txt file.
     * @throws IOException If an I/O exception occurs during file handling.
     */
    public static void createNewFile(Path path) throws IOException {
        if (!Files.isDirectory(path.getParent())) {
            Files.createDirectories(path.getParent());
            logger.log(Level.INFO, "new directory created");
        }
        if (!Files.exists(path)) {
            Files.createFile(path);
            logger.log(Level.INFO, "new clintonData.txt file created");
        }
    }

    //@@author kyhjonathan
    /**
     * Reads tasks in hashmap and writes it in formatted form to clintonData.txt.
     *
     * @param tasks Hashmap of tasks.
     * @param path File Path of clintonData.txt file.
     */
    public static void saveTasksToFile(Map<LocalDate, List<Task>> tasks, Path path) {
        try (FileWriter writer = new FileWriter(path.toFile())) {
            for (Map.Entry<LocalDate, List<Task>> entry : tasks.entrySet()) {
                assert entry != null;
                LocalDate date = entry.getKey();
                assert date != null;
                List<Task> taskList = entry.getValue();
                assert taskList != null;
                for (Task task : taskList) {
                    String taskSaveFormat = task.getSaveFormat();
                    writer.write(date + "|" + taskSaveFormat + System.lineSeparator());
                    String taskDescription = task.getName();
                    logger.log(Level.INFO, "task added: " + taskDescription);
                }
            }
        } catch (IOException e) {
            System.out.println("I/O exception occurred during file handling");
        }
    }

    /**
     * Loads tasks from test.txt to hashmap.
     *
     * @param path File Path of clintonData.txt file.
     * @return tasks hashmap of tasks read from test.txt.
     */
    public static Map<LocalDate, List<Task>> loadTasksFromFile(Path path) {
        Map<LocalDate, List<Task>> tasks = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!checkFileFormat(line)) {
                    throw new StorageFileException("Error in file format.");
                }
                String[] parts = line.split("\\|");
                checkStorageTextDateFormat(parts[0]);
                LocalDate date = LocalDate.parse(parts[0]);
                TaskType taskType = parseTaskType(parts[1]);
                String markedStatus = parts[2];
                String priorityLevel = parts[3];
                String taskDescription = parts[4];
                String[] dates = {null, null};
                String[] times = {null, null};
                if (taskType == DEADLINE) {
                    dates[0] = parts[5];
                    times[0] = parts[6];
                } else if (taskType == EVENT) {
                    dates[0] = parts[5];
                    dates[1] = parts[6];
                    times[0] = parts[7];
                    times[1] = parts[8];
                }
                addTask(date, taskDescription, taskType, dates, times);
                configureStatuses(date, markedStatus, priorityLevel);
            }
        } catch (IOException e) {
            System.out.println("I/O exception occurred during file handling");
            logger.log(Level.WARNING, "I/O exception occurred");
        } catch (StorageFileException e) {
            logger.log(Level.WARNING, "Wrong tasks.txt format");
        } catch (TaskManagerException e) {
            logger.log(Level.WARNING, "Invalid task type for task.");
        }
        logger.log(Level.INFO, "tasks returned");
        return tasks;
    }

    /**
     * Configures the statuses of the tasks after they are loaded from the file.
     * 
     * @param date Date of the task.
     * @param markedStatus Marked status of the task.
     * @param priorityLevel Priority level of the task.
     */
    private static void configureStatuses(LocalDate date, String markedStatus, String priorityLevel) {

        List<Task> allTasks = getDayTasks(date);
        Task recentlyAddedTask = allTasks.get(allTasks.size() - 1);

        setMarkedStatus(markedStatus, recentlyAddedTask);
        setPriorityLevelStatus(priorityLevel, recentlyAddedTask);
    }

    /**
     * Sets the priority level status of the task.
     * 
     * @param priorityLevel Priority level of the task.
     * @param recentlyAddedTask Task that was most recently added.
     */
    private static void setPriorityLevelStatus(String priorityLevel, Task recentlyAddedTask) {
        if (priorityLevel.equals("H")) {
            recentlyAddedTask.setPriorityLevel(TaskPriorityLevel.HIGH);
        } else if (priorityLevel.equals("M")) {
            recentlyAddedTask.setPriorityLevel(TaskPriorityLevel.MEDIUM);
        } else {
            recentlyAddedTask.setPriorityLevel(TaskPriorityLevel.LOW);
        }
    }

    /**
     * Sets the marked status of the task.
     * 
     * @param markedStatus Marked status of the task.
     * @param recentlyAddedTask Task that was most recently added.
     */
    private static void setMarkedStatus(String markedStatus, Task recentlyAddedTask) {
        if (markedStatus.equals("X")) {
            recentlyAddedTask.setCompleteness(true);
        }
    }

    /**
     * Checks if the file format is correct.
     *
     * @param line Line to be checked.
     * @return True if the file format is correct, false otherwise.
     */
    public static boolean checkFileFormat(String line) {
        String regex = "\\d{4}-\\d{2}-\\d{2}\\|.+";
        return line.matches(regex);
    }

}
