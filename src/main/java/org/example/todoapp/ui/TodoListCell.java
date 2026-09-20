package org.example.todoapp.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import org.example.todoapp.controller.DashboardController;
import org.example.todoapp.controller.TodoItemController;
import org.example.todoapp.database.TodoService;
import org.example.todoapp.model.Category;
import org.example.todoapp.model.Todo;

import java.io.IOException;
import java.util.List;

public class TodoListCell extends ListCell<Todo> {

    private Parent root;
    private TodoItemController controller;


    private final List<Category> categories;

    public TodoListCell(
            TodoService todoService,
            Runnable onDelete,
            DashboardController dashboardController,
            List<Category> categories
    ) {

        this.categories = categories;

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

            List<Category> todoCategories = categories.stream()
                    .filter(category ->
                            todo.getCategoryIds()
                                    .contains(category.getId())
                    )
                    .toList();

            controller.setTodo(todo);
            controller.setCategories(todoCategories);

            setGraphic(root);
        }
    }
}