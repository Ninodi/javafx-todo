package org.example.todoapp.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.example.todoapp.database.CategoriesService;
import org.example.todoapp.database.TodoService;
import org.example.todoapp.model.Category;
import org.example.todoapp.model.Todo;
import org.example.todoapp.ui.TodoListCell;

import java.util.List;

public class TodosController {

    @FXML
    private ListView<Todo> todoList;

    private TodoService todoService;
    private DashboardController dashboardController;
    public List<Category> categories;
    private CategoriesService categoriesService;

    public void setTodoService(TodoService todoService) throws Exception {

        this.todoService = todoService;
        setupTodoList();

        loadTodos();
        loadCategories();
    }

    public void setDashboardController(DashboardController dashboardController) {

        this.dashboardController = dashboardController;
    }

    public void setCategoriesService(CategoriesService categoriesService) {

        this.categoriesService = categoriesService;
    }

    private void setupTodoList() {

        todoList.setCellFactory(
                listView -> new TodoListCell(todoService, this::refreshTodos, dashboardController, categories)
        );
    }


    private void loadTodos() {

        try {

            List<Todo> todos =
                    todoService.getTodos();

            todoList.setItems(
                    FXCollections.observableArrayList(todos)
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void refreshTodos() {
        loadTodos();
    }

    public void loadCategories() throws Exception{
        this.categories = categoriesService.getCategories();
    }
}