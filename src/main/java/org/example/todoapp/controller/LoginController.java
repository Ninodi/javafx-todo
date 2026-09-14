package org.example.todoapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.todoapp.auth.AuthManager;
import org.example.todoapp.navigation.AppPage;
import org.example.todoapp.navigation.AppRouter;

public class LoginController {
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
    private Button loginButton;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleLogin(ActionEvent event) {

        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter your email and password.");
            return;
        }

        try {
            authManager.login(email, password);

            // Login successful
            messageLabel.setText("Login successful!");


        } catch (Exception e) {
            messageLabel.setText("Invalid email or password.");
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegister() {
        router.navigateTo(AppPage.REGISTER, null);
    }
}