package org.example.todoapp.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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

    @FXML
    private Button allBtn;

    @FXML
    private Button doneBtn;

    @FXML
    private Button pendingBtn;


    private TodoService todoService;
    private DashboardController dashboardController;
    private CategoriesService categoriesService;

    private List<Category> categories;
    private List<Todo> allTodos;
    private CategoryFilterBtnController selectedCategoryController;
    private enum StatusFilter {
        ALL, DONE, PENDING
    }

    private StatusFilter selectedStatus = StatusFilter.ALL;

    private String selectedCategoryId;

    public void setTodoService(TodoService todoService) throws Exception {
        this.todoService = todoService;

        loadCategories();
        loadTodos();

        setupTodoList();
        setupFilterBar();

        selectedStatus = StatusFilter.ALL;
        updateStatusButtonStyles();
        updateFilteredTodos();
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
                controller.setOnClick(() -> {

                    // Toggle off the currently selected category
                    if (selectedCategoryController == controller) {
                        controller.setSelected(false);
                        selectedCategoryController = null;
                        selectedCategoryId = null;
                    } else {

                        // Unselect the previous category
                        if (selectedCategoryController != null) {
                            selectedCategoryController.setSelected(false);
                        }

                        // Select the new category
                        controller.setSelected(true);
                        selectedCategoryController = controller;
                        selectedCategoryId = category.getId();
                    }

                    updateFilteredTodos();
                });

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
            updateFilteredTodos();
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
     * Called when a todo is added/edited/deleted.
     * Fetches fresh data from the server.
     */
    public void refreshTodos() {
        loadTodos();
    }

    public void loadCategories() throws Exception {
        this.categories = categoriesService.getCategories();
    }



    @FXML
    private void handleAllClick(ActionEvent event) {
        selectedStatus = StatusFilter.ALL;
        displayTodos(allTodos);

        updateStatusButtonStyles();
    }

    @FXML
    private void handleDoneClick(ActionEvent event) {
        selectedStatus = StatusFilter.DONE;
        List<Todo> doneTodos = allTodos.stream()
                .filter(Todo::isCompleted)
                .toList();

        displayTodos(doneTodos);
        updateStatusButtonStyles();
    }

    @FXML
    private void handlePendingClick(ActionEvent event) {
        selectedStatus = StatusFilter.PENDING;
        List<Todo> pendingTodos = allTodos.stream()
                .filter(todo -> !todo.isCompleted())
                .toList();

        displayTodos(pendingTodos);
        updateStatusButtonStyles();
    }


    private void updateFilteredTodos() {

        if (allTodos == null) {
            return;
        }

        List<Todo> filteredTodos = allTodos.stream()

                // Apply the status filter
                .filter(todo -> switch (selectedStatus) {
                    case ALL -> true;
                    case DONE -> todo.isCompleted();
                    case PENDING -> !todo.isCompleted();
                })

                // Apply the category filter
                .filter(todo ->
                        selectedCategoryId == null ||
                                (todo.getCategoryIds() != null &&
                                        todo.getCategoryIds().contains(selectedCategoryId))
                )

                .toList();

        displayTodos(filteredTodos);
    }

    private void updateStatusButtonStyles() {
        allBtn.setStyle("");
        doneBtn.setStyle("");
        pendingBtn.setStyle("");

        switch (selectedStatus) {
            case ALL -> allBtn.setStyle("-fx-background-color: #4f51d8;" + "-fx-text-fill: white;");
            case DONE -> doneBtn.setStyle("-fx-background-color: #4f51d8;" + "-fx-text-fill: white;");
            case PENDING -> pendingBtn.setStyle("-fx-background-color: #4f51d8;" + "-fx-text-fill: white;");
        }
    }
}