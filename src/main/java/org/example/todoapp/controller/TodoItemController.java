package org.example.todoapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.todoapp.database.CategoriesService;
import org.example.todoapp.database.TodoService;
import org.example.todoapp.model.Category;
import org.example.todoapp.model.Todo;

import java.util.List;

public class TodoItemController {
    private TodoService todoService;
    private DashboardController dashboardController;
    private Runnable onDelete;
    private Todo todo;
    private List<Category> categories;

    @FXML
    private Label titleLabel;


    @FXML
    private HBox categoryTags;

    public void setTodoService (TodoService todoService) {
        this.todoService = todoService;
    }

    public void setDashboardController (DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;

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
}
