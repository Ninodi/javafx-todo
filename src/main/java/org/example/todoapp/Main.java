package org.example.todoapp;

import javafx.application.Application;
import javafx.stage.Stage;

import org.example.todoapp.auth.AuthManager;
import org.example.todoapp.navigation.AppPage;
import org.example.todoapp.navigation.AppRouter;

public class Main extends Application {

    private AuthManager authManager;
    private AppRouter router;

    @Override
    public void start(Stage stage) {
        authManager = new AuthManager();

        router = new AppRouter(
                stage,
                authManager
        );

        authManager.getSession()
                .loggedInProperty()
                .addListener((obs, oldValue, newValue) -> {
                    updatePage();
                });

        updatePage();
    }

    private void updatePage() {

        if (authManager.isLoggedIn()) {

            router.navigateTo(AppPage.DASHBOARD, null);

        } else {

            router.navigateTo(AppPage.LOGIN, null);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}