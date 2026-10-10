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

    public void setSelected(boolean selected) {

        if (selected) {
            String color = category.getColor();

            if (color.startsWith("0x")) {
                color = "#" + color.substring(2, 8);
            }

            categoryButton.setStyle(
                    "-fx-background-color: " + color + ";" +
                            "-fx-border-width: 0.5px;" +
                            "-fx-border-color: black;" +
                            "-fx-border-radius: 5px;"
            );

            categoryCircle.setFill(Color.WHITE);
//            categoryTitle.setTextFill(Color.WHITE);

        } else {
            categoryButton.setStyle("");

            categoryCircle.setFill(
                    Color.web(category.getColor())
            );

            categoryTitle.setTextFill(Color.BLACK);
        }
    }
}