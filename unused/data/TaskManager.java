class TaskManager {

    /**
     * Lists task of the input date.
     *
     * @param taskManager Hashmap of tasks.
     * @param date Date that's prompted by user.
     * @param message Message to be prompted to the user.
     * @throws TaskManagerException If not in correct week/month view.
     */
    private static void listTasksAtDate(TaskManager taskManager, LocalDate date, String message)
            throws TaskManagerException {
        List<Task> dayTasks = taskManager.getTasksForDate(date);
        checkIfDateHasTasks(dayTasks);

        System.out.println(message);
        for (int i = 0; i < dayTasks.size(); i++) {
            System.out.println((i + 1) + ". " + dayTasks.get(i).getName());
        }
    }
}