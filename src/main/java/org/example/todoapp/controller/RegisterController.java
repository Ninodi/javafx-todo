package org.example.todoapp.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.todoapp.auth.AuthManager;
import org.example.todoapp.navigation.AppPage;
import org.example.todoapp.navigation.AppRouter;

import java.io.IOException;

public class RegisterController {
    private AuthManager authManager;

    public void setAuthManager(AuthManager authManager) {
        this.authManager = authManager;
    }

    private AppRouter router;

    public void setRouter(AppRouter router) {
        this.router = router;
    }
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField repeatPasswordField;

    @FXML
    private Button registerButton;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleRegister(ActionEvent event) {

        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String repeatPassword = repeatPasswordField.getText();

        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        if (!password.equals(repeatPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        try {
            authManager.register(email, password);


        } catch (Exception e) {
            messageLabel.setText("Registration failed.");
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogin() {
        router.navigateTo(AppPage.LOGIN, null);
    }
}