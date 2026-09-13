package org.example.todoapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import org.example.todoapp.database.CategoriesService;
import org.example.todoapp.model.Category;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import org.example.todoapp.navigation.AppRouter;

public class CategoryItemController {
    private CategoriesService categoriesService;
    private Runnable onDelete;
    @FXML
    private Circle colorCircle;

    @FXML
    private Label titleLabel;

    private Category category;
    private String categoryId;

    public void setCategoriesService (CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    public void setCategory(Category category) {

        this.category = category;
        this.categoryId = category.getId();

        titleLabel.setText(
                category.getTitle()
        );

        colorCircle.setFill(
                Color.web(category.getColor())
        );
    }

    public void setOnDelete(Runnable onDelete) {
        this.onDelete = onDelete;
    }
    @FXML
    public void handleDelete(ActionEvent actionEvent) {
        try {
            categoriesService.deleteCategory(categoryId);
            if (onDelete != null) {
                onDelete.run();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
