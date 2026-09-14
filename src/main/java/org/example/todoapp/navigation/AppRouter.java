package org.example.todoapp.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.example.todoapp.auth.AuthManager;
import org.example.todoapp.controller.*;
import org.example.todoapp.database.CategoriesService;
import org.example.todoapp.database.TodoService;
import org.example.todoapp.model.Todo;

import java.io.IOException;

public class AppRouter {

    private final Stage stage;
    private final AuthManager authManager;
    private final TodoService todoService;
    private final CategoriesService categoriesService;

    public AppRouter(
            Stage stage,
            AuthManager authManager
    ) {
        this.stage = stage;
        this.authManager = authManager;
        this.todoService =
                new TodoService(
                        authManager.getSession()
                );
        this.categoriesService = new CategoriesService(authManager.getSession());
    }

    public void navigateTo(AppPage page) {
        navigateTo(page, null);
    }

    public void navigateTo(AppPage page, Todo todo) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(page.getViewPath())
            );

            Parent root = loader.load();

            Object controller = loader.getController();

            configureController(controller);

            if (controller instanceof AddTodoController addTodoController
                    && todo != null) {

                addTodoController.setTodo(todo);
            }
            Scene scene = new Scene(root, 800, 600);

            stage.setTitle(
                    "Todo App - " + page.getTitle()
            );

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configureController(Object controller) {

        if (controller instanceof LoginController loginController) {

            loginController.setAuthManager(authManager);
            loginController.setRouter(this);

        } else if (controller instanceof RegisterController registerController) {

            registerController.setAuthManager(authManager);
            registerController.setRouter(this);

        } else if (controller instanceof DashboardController dashboardController) {

            dashboardController.setTodoService(todoService);
            dashboardController.setCategoriesService(categoriesService);
            dashboardController.setAuthManager(authManager);
            dashboardController.setRouter(this);

        } else if (controller instanceof AddTodoController addTodoController) {
            addTodoController.setRouter(this);
            addTodoController.setTodoService(todoService);
            addTodoController.setCategoriesService(categoriesService);
        }
    }

}