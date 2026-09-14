package org.example.todoapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.todoapp.database.CategoriesService;
import org.example.todoapp.database.TodoService;
import org.example.todoapp.model.Category;
import org.example.todoapp.model.Todo;
import org.example.todoapp.navigation.AppPage;
import org.example.todoapp.navigation.AppRouter;

import javafx.event.ActionEvent;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class AddTodoController {
    public Label pageTitle;
    public Label pageSubtitle;
    public Button addTodoAction;
    private CategoriesService categoriesService;
    private AppRouter router;
    private TodoService todoService;
    private Todo todo;
    private List<String> categoryIds;

    private Runnable onTodoCreated;

//    public void setOnCategoryCreated(Runnable onTodoCreated) {
//        this.onTodoCreated = onTodoCreated;
//    }

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker dueDateField;

    @FXML
    private MenuButton categoriesDropdown;

    public void setTodoService(TodoService todoService) {
        this.todoService = todoService;
    }


    public void setCategoriesService(
            CategoriesService categoriesService
    ) {

        this.categoriesService = categoriesService;

        loadCategories();
    }


    public void setRouter(AppRouter router) {
        this.router = router;
    }

    private void loadCategories() {
        try {
            List<Category> categories = categoriesService.getCategories();

            categoriesDropdown.getItems().clear();

            List<String> selectedCategoryIds = todo != null
                    ? todo.getCategoryIds()
                    : List.of();

            for (Category category : categories) {

                CheckMenuItem menuItem =
                        new CheckMenuItem(category.getTitle());

                menuItem.setUserData(category);

                // Pre-select categories belonging to this Todo
                if (selectedCategoryIds.contains(category.getId())) {
                    menuItem.setSelected(true);
                }

                menuItem.setOnAction(event -> {
                    updateCategoriesDropdownText();

                    javafx.application.Platform.runLater(() -> {
                        categoriesDropdown.show();
                    });
                });

                categoriesDropdown.getItems().add(menuItem);
            }

            updateCategoriesDropdownText();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Category> getSelectedCategories() {

        return categoriesDropdown
                .getItems()
                .stream()

                .filter(item ->
                        item instanceof CheckMenuItem
                )

                .map(item ->
                        (CheckMenuItem) item
                )

                .filter(CheckMenuItem::isSelected)

                .map(item ->
                        (Category) item.getUserData()
                )

                .toList();
    }


    @FXML
    public void handleBack() {
        router.navigateTo(AppPage.DASHBOARD, null);
    }


    @FXML
    public void handleSave() throws Exception {

        String titleInput = titleField.getText();
        String descriptionInput = descriptionField.getText();
        LocalDate localDate = dueDateField.getValue();

        List<String> categoryIds = getSelectedCategories()
                .stream()
                .map(Category::getId)
                .toList();

        try {
            if (todo == null) {
                todoService.createTodo(
                        titleInput,
                        descriptionInput,
                        localDate,
                        categoryIds
                );
            } else {
                // EDIT
                todoService.updateTodo(
                        todo.getId(),
                        titleInput,
                        descriptionInput,
                        localDate,
                        categoryIds
                );
            }

//            if (onTodoCreated != null) {
//                onTodoCreated.run();
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        handleBack();
    }


    private void updateCategoriesDropdownText() {

        List<Category> selectedCategories =
                getSelectedCategories();

        if (selectedCategories.isEmpty()) {

            categoriesDropdown.setText(
                    "Select categories"
            );

            return;
        }

        String selectedTitles =
                selectedCategories
                        .stream()
                        .map(Category::getTitle)
                        .collect(
                                java.util.stream.Collectors.joining(", ")
                        );

        categoriesDropdown.setText(selectedTitles);
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;

        titleField.setText(todo.getTitle());
        descriptionField.setText(todo.getDescription());
        dueDateField.setValue(todo.getDueDate());

        if(todo != null){
            pageTitle.setText("Update Todo");
            pageSubtitle.setText("Update task and keep track of it.");
            addTodoAction.setText("Update");
        }
        loadCategories();
    }
}