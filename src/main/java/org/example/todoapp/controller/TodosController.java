package org.example.todoapp.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import org.example.todoapp.database.CategoriesService;
import org.example.todoapp.database.TodoService;
import org.example.todoapp.model.Category;
import org.example.todoapp.model.Todo;
import org.example.todoapp.ui.TodoListCell;

import java.io.IOException;
import java.util.List;

public class TodosController {

    @FXML
    private ListView<Todo> todoList;

    @FXML
    private HBox filterBar;

    private TodoService todoService;
    private DashboardController dashboardController;
    private CategoriesService categoriesService;

    private List<Category> categories;
    private List<Todo> allTodos;

    public void setTodoService(TodoService todoService) throws Exception {

        this.todoService = todoService;

        loadCategories();
        loadTodos();

        setupTodoList();
        setupFilterBar();
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void setCategoriesService(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    private void setupTodoList() {

        todoList.setCellFactory(
                listView -> new TodoListCell(
                        todoService,
                        this::refreshTodos,
                        dashboardController,
                        categories
                )
        );

        displayTodos(allTodos);
    }

    private void setupFilterBar() {

        filterBar.getChildren().clear();

        Button allButton = new Button("All");

        allButton.setOnAction(event -> displayTodos(allTodos));

        filterBar.getChildren().add(allButton);

        for (Category category : categories) {

            try {

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/todoapp/ui/CategoryFilterBtn.fxml"
                        )
                );

                Button categoryButton = loader.load();

                CategoryFilterBtnController controller =
                        loader.getController();

                controller.setCategory(category);

                controller.setOnClick(() ->
                        filterByCategory(category.getId())
                );

                filterBar.getChildren().add(categoryButton);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Fetch todos from the server/API.
     * This should only happen when we actually need fresh data.
     */
    private void loadTodos() {

        try {

            allTodos = todoService.getTodos();

            displayTodos(allTodos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates what is currently displayed in the ListView.
     * Does NOT make an API call.
     */
    private void displayTodos(List<Todo> todos) {

        todoList.setItems(
                FXCollections.observableArrayList(todos)
        );
    }

    /**
     * Filters the already-loaded todos.
     * Does NOT make an API call.
     */
    private void filterByCategory(String categoryId) {

        List<Todo> filteredTodos = allTodos.stream()
                .filter(todo ->
                        todo.getCategoryIds() != null &&
                                todo.getCategoryIds().contains(categoryId)
                )
                .toList();

        displayTodos(filteredTodos);
    }

    /**
     * Called when a todo is added/edited/deleted.
     * Fetches fresh data from the server.
     */
    public void refreshTodos() {
        loadTodos();
    }

    public void loadCategories() throws Exception {
        this.categories = categoriesService.getCategories();
    }
}