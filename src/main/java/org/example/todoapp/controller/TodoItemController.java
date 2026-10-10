package org.example.todoapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.todoapp.database.TodoService;
import org.example.todoapp.model.Category;
import org.example.todoapp.model.Todo;

import java.awt.*;
import java.util.List;

public class TodoItemController {
    private TodoService todoService;
    private DashboardController dashboardController;
    private Runnable onDelete;
    private Todo todo;

    @FXML
    private Label titleLabel;

    @FXML
    private Label titleDescr;

    @FXML
    private CheckBox completedStatus;



    @FXML
    private HBox categoryTags;

    public void setTodoService (TodoService todoService) {
        this.todoService = todoService;
    }

    public void setDashboardController (DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void setCategories(List<Category> categories) {

        categoryTags.getChildren().clear();

        for (Category category : categories) {
            Circle circle = new Circle(5);

            circle.setFill(Color.web(category.getColor()));
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(0.5);
            categoryTags.getChildren().add(circle);
        }
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
        titleLabel.setText(
                todo.getTitle()
        );

        titleDescr.setText(todo.getDescription());
        completedStatus.setSelected(todo.isCompleted());
    }

    public void setOnDelete(Runnable onDelete) {
        this.onDelete = onDelete;
    }

    @FXML
    public void handleDelete(ActionEvent actionEvent) {
        try {
            todoService.deleteTodo(todo);
            if (onDelete != null) {
                onDelete.run();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleEditMode(ActionEvent actionEvent) {
        dashboardController.showAddTodoView(todo);
    }

    @FXML
    public void handleStatusChange(ActionEvent actionEvent) {
        try {
            boolean completed = completedStatus.isSelected();

            todo.setCompleted(completed);

            todoService.updateTodo(
                    todo.getId(),
                    todo.getTitle(),
                    todo.getDescription(),
                    todo.getDueDate(),
                    todo.getCategoryIds(),
                    todo.isCompleted()
            );

        } catch (Exception e) {
            // Revert checkbox if the database update fails
            completedStatus.setSelected(todo.isCompleted());
            throw new RuntimeException("Failed to update todo status", e);
        }
    }
}
