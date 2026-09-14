package org.example.todoapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.todoapp.database.CategoriesService;
import org.example.todoapp.database.TodoService;
import org.example.todoapp.model.Category;
import org.example.todoapp.model.Todo;

public class TodoItemController {
    private TodoService todoService;
    private DashboardController dashboardController;
    private Runnable onDelete;
    @FXML
    private Circle colorCircle;

    @FXML
    private Label titleLabel;

    private Todo todo;
    private String todoId;

    public void setTodoService (TodoService todoService) {
        this.todoService = todoService;
    }

    public void setDashboardController (DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
        this.todoId = todo.getId();

        titleLabel.setText(
                todo.getTitle()
        );

//        colorCircle.setFill(
//                Color.web(category.getColor())
//        );
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
