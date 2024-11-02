
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TaskManager {
    private static final String FILE_PATH = "./TaskList.json";
    private List<Task> tasks;
    Scanner scanner = new Scanner(System.in);

    public TaskManager() {
        tasks = loadTasks();
    }

    private List<Task> loadTasks() {
        try {
            if (Files.exists(Paths.get(FILE_PATH))) {
                String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
                return parseTasks(content);
            } else {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveTasks() {
        try {
            String content = tasks.stream()
                    .map(this::taskToJson)
                    .collect(Collectors.joining(",\n", "[\n", "\n]"));
            Files.write(Paths.get(FILE_PATH), content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTask(Task task) {
        task.setId((tasks.size() + 1));
        task.setStatus(TaskStatus.TODO);
        task.setCreatedAt(LocalDateTime.now().withDayOfMonth(1));
        task.setUpdateAt(LocalDateTime.now().withDayOfMonth(1));
        tasks.add(task);
        saveTasks();
    }

    public void editTask(Integer id, String newDescription) {
        Optional<Task> taskOptional = tasks.stream().filter(t -> t.getId().equals(id)).findFirst();
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setDescription(newDescription);
            task.setUpdateAt(LocalDateTime.now());
            saveTasks();
        }
        System.out.println("want to change status? (y/n)");
        if (Objects.equals(scanner.nextLine(), "y")) {
            System.out.println("1. TODO");
            System.out.println("2. IN_PROGRESS");
            System.out.println("3. DONE");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    taskOptional.get().setStatus(TaskStatus.TODO);
                    saveTasks();
                    break;
                case 2:
                    taskOptional.get().setStatus(TaskStatus.IN_PROGRESS);
                    saveTasks();
                    break;
                case 3:
                    taskOptional.get().setStatus(TaskStatus.DONE);
                    saveTasks();
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public void deleteTask(int id) {
        tasks.removeIf(task -> task.equals(id));
        saveTasks();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    private List<Task> parseTasks(String content) {
        List<Task> taskList = new ArrayList<>();
        content = content.trim();
        if (content.startsWith("[") && content.endsWith("]")) {
            content = content.substring(1, content.length() - 1).trim();
            if (!content.isEmpty()) {
                String[] taskStrings = content.split("},\\s*\\{");
                for (String taskString : taskStrings) {
                    taskString = taskString.replaceAll("[\\[\\]{}]", "").trim();
                    String[] fields = taskString.split(",\\s*");
                    Task task = new Task();
                    for (String field : fields) {
                        String[] keyValue = field.split(":\\s*");
                        String key = keyValue[0].replaceAll("\"", "").trim();
                        String value = keyValue[0].replaceAll("\"", "").trim();
                        switch (key) {
                            case "id":
                                task.setId(Integer.parseInt(value));
                                break;
                            case "description":
                                task.setDescription(value);
                                break;
                            case "status":
                                task.setStatus(TaskStatus.valueOf(value));
                                break;
                            case "createdAt":
                                task.setCreatedAt(LocalDateTime.parse(value));
                                break;
                            case "updateAt":
                                task.setUpdateAt(LocalDateTime.parse(value));
                                break;
                        }
                    }
                    taskList.add(task);
                }
            }
        }
        return taskList;
    }

    private String taskToJson(Task task) {
        return String.format("{\"id\":%d,\"description\":\"%s\",\"status\":\"%s\",\"createdAt\":\"%s\",\"updateAt\":\"%s\"}",
                task.getId(), task.getDescription(), task.getStatus(), task.getCreatedAt(), task.getUpdateAt());
    }
}