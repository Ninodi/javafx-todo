package org.example.todoapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.todoapp.model.Category;

public class CategoryFilterBtnController {

    @FXML
    private Button categoryButton;

    @FXML
    private Circle categoryCircle;

    @FXML
    private Label categoryTitle;

    private Category category;

    private Runnable onClick;

    public void setCategory(Category category) {
        this.category = category;

        categoryTitle.setText(category.getTitle());
        categoryCircle.setFill(Color.web(category.getColor()));
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;

        categoryButton.setOnAction(event -> {
            if (this.onClick != null) {
                this.onClick.run();
            }
        });
    }
}