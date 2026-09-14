import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskManager {

    static Scanner scanner = new Scanner(System.in);
    static List<Task> tasks = new ArrayList<>();
    static int nextId = 1;

    enum Priority {
        LOW, MEDIUM, HIGH
    }

    enum Status {
        PENDING, IN_PROGRESS, COMPLETED
    }

    static class Task {
        int id;
        String title;
        String description;
        Priority priority;
        Status status;
        LocalDateTime createdAt;

        Task(String title, String description, Priority priority) {
            this.id = nextId++;
            this.title = title;
            this.description = description;
            this.priority = priority;
            this.status = Status.PENDING;
            this.createdAt = LocalDateTime.now();
        }

        void display() {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            System.out.println("----------------------------------------");
            System.out.println("ID          : " + id);
            System.out.println("Title       : " + title);
            System.out.println("Description : " + description);
            System.out.println("Priority    : " + priority);
            System.out.println("Status      : " + status);
            System.out.println("Created     : " + createdAt.format(formatter));
            System.out.println("----------------------------------------");
        }
    }

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("       DOCKER TASK MANAGER APP");
        System.out.println("========================================");

        loadSampleTasks();

        while (true) {
            showMenu();

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addTask();
                    break;

                case 2:
                    showAllTasks();
                    break;

                case 3:
                    searchTask();
                    break;

                case 4:
                    updateStatus();
                    break;

                case 5:
                    deleteTask();
                    break;

                case 6:
                    showStatistics();
                    break;

                case 7:
                    System.out.println("\nThank you for using Task Manager!");
                    System.out.println("Application stopped.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void showMenu() {
        System.out.println("\n============== MENU ==============");
        System.out.println("1. Add Task");
        System.out.println("2. Show All Tasks");
        System.out.println("3. Search Task");
        System.out.println("4. Update Task Status");
        System.out.println("5. Delete Task");
        System.out.println("6. Task Statistics");
        System.out.println("7. Exit");
        System.out.println("==================================");
    }

    static void addTask() {

        System.out.println("\n---------- ADD NEW TASK ----------");

        System.out.print("Enter task title: ");
        String title = scanner.nextLine();

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        Priority priority = readPriority();

        Task task = new Task(title, description, priority);
        tasks.add(task);

        System.out.println("\nTask added successfully!");
        System.out.println("Task ID: " + task.id);
    }

    static void showAllTasks() {

        System.out.println("\n---------- ALL TASKS ----------");

        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }

        for (Task task : tasks) {
            task.display();
        }

        System.out.println("Total tasks: " + tasks.size());
    }

    static void searchTask() {

        System.out.println("\n---------- SEARCH TASK ----------");

        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine().toLowerCase();

        boolean found = false;

        for (Task task : tasks) {

            if (task.title.toLowerCase().contains(keyword)
                    || task.description.toLowerCase().contains(keyword)) {

                task.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching task found.");
        }
    }

    static void updateStatus() {

        System.out.println("\n---------- UPDATE STATUS ----------");

        int id = readInt("Enter task ID: ");

        Task task = findTaskById(id);

        if (task == null) {
            System.out.println("Task not found!");
            return;
        }

        System.out.println("Current status: " + task.status);
        System.out.println("1. PENDING");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. COMPLETED");

        int choice = readInt("Select new status: ");

        switch (choice) {
            case 1:
                task.status = Status.PENDING;
                break;

            case 2:
                task.status = Status.IN_PROGRESS;
                break;

            case 3:
                task.status = Status.COMPLETED;
                break;

            default:
                System.out.println("Invalid status.");
                return;
        }

        System.out.println("Status updated successfully!");
    }

    static void deleteTask() {

        System.out.println("\n---------- DELETE TASK ----------");

        int id = readInt("Enter task ID: ");

        Task task = findTaskById(id);

        if (task == null) {
            System.out.println("Task not found!");
            return;
        }

        System.out.println("Deleting task: " + task.title);

        tasks.remove(task);

        System.out.println("Task deleted successfully!");
    }

    static void showStatistics() {

        System.out.println("\n---------- TASK STATISTICS ----------");

        int pending = 0;
        int progress = 0;
        int completed = 0;

        int low = 0;
        int medium = 0;
        int high = 0;

        for (Task task : tasks) {

            switch (task.status) {
                case PENDING:
                    pending++;
                    break;

                case IN_PROGRESS:
                    progress++;
                    break;

                case COMPLETED:
                    completed++;
                    break;
            }

            switch (task.priority) {
                case LOW:
                    low++;
                    break;

                case MEDIUM:
                    medium++;
                    break;

                case HIGH:
                    high++;
                    break;
            }
        }

        System.out.println("Total Tasks     : " + tasks.size());
        System.out.println("Pending         : " + pending);
        System.out.println("In Progress     : " + progress);
        System.out.println("Completed       : " + completed);

        System.out.println("\nPriority:");
        System.out.println("Low             : " + low);
        System.out.println("Medium          : " + medium);
        System.out.println("High            : " + high);

        if (!tasks.isEmpty()) {
            double completionRate =
                    (completed * 100.0) / tasks.size();

            System.out.printf(
                    "\nCompletion Rate : %.2f%%\n",
                    completionRate
            );
        }
    }

    static Task findTaskById(int id) {

        for (Task task : tasks) {
            if (task.id == id) {
                return task;
            }
        }

        return null;
    }

    static Priority readPriority() {

        while (true) {

            System.out.println("\nSelect Priority:");
            System.out.println("1. LOW");
            System.out.println("2. MEDIUM");
            System.out.println("3. HIGH");

            int choice = readInt("Enter priority: ");

            switch (choice) {
                case 1:
                    return Priority.LOW;

                case 2:
                    return Priority.MEDIUM;

                case 3:
                    return Priority.HIGH;

                default:
                    System.out.println(
                            "Invalid priority. Try again."
                    );
            }
        }
    }

    static int readInt(String message) {

        while (true) {

            System.out.print(message);

            try {
                int value = Integer.parseInt(
                        scanner.nextLine()
                );

                return value;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    static void loadSampleTasks() {

        tasks.add(new Task(
                "Learn Docker",
                "Learn Docker images and containers",
                Priority.HIGH
        ));

        tasks.add(new Task(
                "Build Java App",
                "Create a Java application",
                Priority.MEDIUM
        ));

        tasks.add(new Task(
                "Push Code",
                "Push project to Git repository",
                Priority.LOW
        ));

        tasks.get(1).status = Status.IN_PROGRESS;
        tasks.get(2).status = Status.COMPLETED;
    }
}