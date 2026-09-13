package org.example.todoapp.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.todoapp.database.CategoriesService;
import org.example.todoapp.model.Category;
import org.example.todoapp.navigation.AppRouter;
import org.example.todoapp.ui.CategoryListCell;

import java.util.List;

public class CategoriesController {
    private CategoriesService categoriesService;
    private DashboardController dashboardController;

    public void setDashboardController(
            DashboardController dashboardController) {

        this.dashboardController = dashboardController;
    }
    @FXML
    private TextField titleField;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private ListView<Category> categoriesList;



    @FXML
    private Button addCategoryButton;


    public void setCategoriesService (CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
        setupCategoriesList();
        loadCategories();
    }

    private void setupCategoriesList() {

        categoriesList.setCellFactory(
                listView -> new CategoryListCell(categoriesService, this::refreshCategories)
        );
    }


    private void loadCategories() {

        try {

            List<Category> categories =
                    categoriesService.getCategories();

            categoriesList.setItems(
                    FXCollections.observableArrayList(categories)
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void handleOpenCategoryModal() {
        dashboardController.openCategoryModal();
    }


    public void refreshCategories() {
        loadCategories();
    }

}