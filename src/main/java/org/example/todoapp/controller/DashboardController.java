package org.example.todoapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.todoapp.auth.AuthManager;
import javafx.scene.control.Button;
import org.example.todoapp.database.CategoriesService;
import org.example.todoapp.database.TodoService;
import org.example.todoapp.model.Category;
import org.example.todoapp.navigation.AppPage;
import org.example.todoapp.navigation.AppRouter;
import java.util.List;
import java.io.IOException;

public class DashboardController {

    public StackPane modalContainer;
    private AuthManager authManager;
    private AppRouter router;
    private TodoService todoService;
    private CategoriesService categoriesService;

    public void setRouter(AppRouter router) {
        this.router = router;
    }

    @FXML
    private StackPane contentArea;


    public void setAuthManager(AuthManager authManager) {

        this.authManager = authManager;
        showTodos();
    }


    @FXML
    private Button todosButton;

    @FXML
    private Button categoriesButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button addTodoButton;

    private List<Button> navigationButtons;

    @FXML
    public void initialize() {

        navigationButtons = List.of(
                todosButton,
                categoriesButton,
                profileButton
        );

    }

    @FXML
    public void showTodos() {
        loadView("/todoapp/ui/TodosView.fxml");
        setActiveButton(todosButton);
    }


    @FXML
    public void showCategories() {
        loadView("/todoapp/ui/CategoriesView.fxml");
        setActiveButton(categoriesButton);
    }


    @FXML
    public void showProfile() {
        loadView("/todoapp/ui/ProfileView.fxml");
        setActiveButton(profileButton);
    }


    private void loadView(String fxml) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(fxml)
                    );

            Node view = loader.load();

            if (fxml.equals("/todoapp/ui/TodosView.fxml")) {

                TodosController controller =
                        loader.getController();

                controller.setTodoService(todoService);
            }

            if (fxml.equals("/todoapp/ui/CategoriesView.fxml")) {

                CategoriesController controller =
                        loader.getController();

                controller.setDashboardController(this);
                controller.setCategoriesService(categoriesService);
            }

            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleLogout() {
        authManager.logout();
    }

    private void setActiveButton(Button activeButton) {

        navigationButtons.forEach(button ->
                button.getStyleClass().remove("active")
        );

        activeButton.getStyleClass().add("active");
    }

    @FXML
    public void showAddTodoView() {
        router.navigateTo(AppPage.ADD_TODO);
    }

    @FXML
    public void openCategoryModal(Category category) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/todoapp/ui/AddCategoryView.fxml")
            );

            Parent root = loader.load();

            AddCategoryController controller = loader.getController();

            controller.setCategoriesService(categoriesService);
            if (category != null) {
                controller.setCategory(category);
            }


            controller.setOnCategoryCreated(() -> {
                showCategories();
                closeCategoryModal();
            });

            controller.setOnClose(this::closeCategoryModal);

            modalContainer.getChildren().add(root);
            modalContainer.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCategoryModal() {
        modalContainer.getChildren().clear();
        modalContainer.setVisible(false);
    }

    public void setTodoService(TodoService todoService) {
        this.todoService = todoService;
    }

    public void setCategoriesService(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }
}