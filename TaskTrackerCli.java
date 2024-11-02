import java.util.Scanner;

public class TaskTrackerCli {
    public static void main(String[] args) {
        // Initialize the Task Tracker CLI
        System.out.println("Welcome to Task Tracker CLI");
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();

        while (true) {
            System.out.println("1. Add Task");
            System.out.println("2. List Tasks");
            System.out.println("3. Edit Task");
            System.out.println("4. Delete Task");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine();
                    Task task = new Task();
                    task.setDescription(description);
                    taskManager.addTask(task);
                    System.out.println("Task added successfully");
                    break;
                case 2:
                    System.out.println("Tasks:");
                    for (Task t : taskManager.getTasks()) {
                        System.out.println(t);
                    }
                    break;
                case 3:
                    System.out.print("Enter task ID to edit: ");
                    int editId = Math.toIntExact(scanner.nextLong());
                    scanner.nextLine();
                    System.out.print("Enter new task description: ");
                    String newDescription = scanner.nextLine();
                    taskManager.editTask(Math.toIntExact(editId), newDescription);
                    System.out.println("Task edited successfully");
                    break;
                case 4:
                    System.out.print("Enter task ID to delete: ");
                    int deleteId = Math.toIntExact(scanner.nextLong());
                    scanner.nextLine();
                    taskManager.deleteTask(Math.toIntExact(deleteId));
                    System.out.println("Task deleted successfully");
                    break;
                case 5:
                    System.out.println("Exiting Task Tracker CLI");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}