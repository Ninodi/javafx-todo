package org.example.todoapp.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import org.example.todoapp.controller.DashboardController;
import org.example.todoapp.controller.TodoItemController;
import org.example.todoapp.database.TodoService;
import org.example.todoapp.model.Todo;

import java.io.IOException;

public class TodoListCell extends ListCell<Todo> {

    private Parent root;

    private TodoItemController controller;

    public TodoListCell(TodoService todoService, Runnable onDelete, DashboardController dashboardController) {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/todoapp/ui/TodoItem.fxml"
                    )
            );

            root = loader.load();

            controller = loader.getController();
            controller.setTodoService(todoService);
            controller.setDashboardController(dashboardController);
            controller.setOnDelete(onDelete);


        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(
            Todo todo,
            boolean empty
    ) {

        super.updateItem(todo, empty);

        if (empty || todo == null) {

            setGraphic(null);

        } else {

            controller.setTodo(todo);

            setGraphic(root);
        }
    }
}