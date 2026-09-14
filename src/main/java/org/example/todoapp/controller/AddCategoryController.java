package org.example.todoapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.todoapp.database.CategoriesService;
import org.example.todoapp.model.Category;

public class AddCategoryController {
    private Runnable onClose;
    private CategoriesService categoriesService;
    private Runnable onCategoryCreated;
    private Category category;

    public void setOnCategoryCreated(Runnable onCategoryCreated) {
        this.onCategoryCreated = onCategoryCreated;
    }

    @FXML
    public Label modalTitle;

    @FXML
    public Label modalSubtitle;

    @FXML
    public Button modalAction;

    @FXML
    private TextField titleField;

    @FXML
    private ColorPicker colorPicker;

    public void setCategoriesService(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @FXML
    private void handleCreate() {
        String title = titleField.getText();
        String color = colorPicker.getValue().toString();

        try {
            if (category == null) {
                // CREATE
                categoriesService.createCategory(title, color);
            } else {
                // EDIT
                categoriesService.updateCategory(
                        category.getId(),
                        title,
                        color
                );
            }

            if (onCategoryCreated != null) {
                onCategoryCreated.run();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }

    @FXML
    private void handleCancel() {
        if (onClose != null) {
            onClose.run();
        }
    }

    public void setCategory(Category category) {
        this.category = category;
        titleField.setText(category.getTitle());
        colorPicker.setValue(Color.web(category.getColor()));

        if(category != null){
            modalTitle.setText("Edit Category");
            modalSubtitle.setText("Edit category to organize todos");
            modalAction.setText("Edit");
        }
    }
}