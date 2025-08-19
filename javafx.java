import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TaskManagement extends Application {

    private TableView<Task> table;
    private ObservableList<Task> taskList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Task Management System");

        // Task list
        taskList = FXCollections.observableArrayList();

        // Table
        table = new TableView<>();
        table.setItems(taskList);

        TableColumn<Task, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Task, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Task, String> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));

        table.getColumns().addAll(titleCol, descCol, priorityCol);

        // Input fields
        TextField titleInput = new TextField();
        titleInput.setPromptText("Task Title");

        TextField descInput = new TextField();
        descInput.setPromptText("Description");

        ComboBox<String> priorityInput = new ComboBox<>();
        priorityInput.getItems().addAll("High", "Medium", "Low");
        priorityInput.setPromptText("Priority");

        // AI Suggest Priority button
        Button suggestButton = new Button("Suggest Priority");
        suggestButton.setOnAction(e -> {
            String desc = descInput.getText();
            String suggested = suggestPriority(desc);
            priorityInput.setValue(suggested);
        });

        Button addButton = new Button("Add Task");
        addButton.setOnAction(e -> {
            String title = titleInput.getText();
            String desc = descInput.getText();
            String priority = priorityInput.getValue();

            if (!title.isEmpty() && priority != null) {
                taskList.add(new Task(title, desc, priority));
                titleInput.clear();
                descInput.clear();
                priorityInput.setValue(null);
            } else {
                showAlert("Error", "Task title and priority are required!");
            }
        });

        Button deleteButton = new Button("Delete Task");
        deleteButton.setOnAction(e -> {
            Task selectedTask = table.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                taskList.remove(selectedTask);
            } else {
                showAlert("Error", "Please select a task to delete.");
            }
        });

        HBox inputBox = new HBox(10, titleInput, descInput, priorityInput, suggestButton, addButton, deleteButton);
        inputBox.setPadding(new Insets(10));

        VBox layout = new VBox(10, table, inputBox);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 900, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // AI priority suggestion method using simple keywords
    private String suggestPriority(String description) {
        if (description == null || description.isEmpty()) return "Medium";

        String desc = description.toLowerCase();
        if (desc.contains("urgent") || desc.contains("critical") || desc.contains("immediately") || desc.contains("asap")) {
            return "High";
        } else if (desc.contains("later") || desc.contains("whenever")) {
            return "Low";
        } else {
            return "Medium";
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Task class
    public static class Task {
        private String title;
        private String description;
        private String priority;

        public Task(String title, String description, String priority) {
            this.title = title;
            this.description = description;
            this.priority = priority;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getPriority() { return priority; }
    }
}