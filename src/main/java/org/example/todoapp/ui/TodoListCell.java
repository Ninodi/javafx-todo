package org.example.todoapp.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import org.example.todoapp.controller.CategoryItemController;
import org.example.todoapp.controller.DashboardController;
import org.example.todoapp.controller.TodosController;
import org.example.todoapp.database.CategoriesService;
import org.example.todoapp.model.Category;
import org.example.todoapp.model.Todo;

import java.io.IOException;

public class TodoListCell extends ListCell<Todo> {

    private Parent root;

    private TodosController controller;

    public TodoListCell(CategoriesService categoriesService, Runnable onDelete, DashboardController dashboardController) {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/todoapp/ui/CategoryItem.fxml"
                    )
            );

            root = loader.load();

            controller = loader.getController();
//            controller.setCategoriesService(categoriesService);
//            controller.setDashboardController(dashboardController);
//            controller.setOnDelete(onDelete);


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

//            controller.setCategory(category);

            setGraphic(root);
        }
    }
}