package org.example.todoapp.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import org.example.todoapp.controller.CategoryItemController;
import org.example.todoapp.database.CategoriesService;
import org.example.todoapp.model.Category;

import java.io.IOException;

public class CategoryListCell extends ListCell<Category> {

    private Parent root;

    private CategoryItemController controller;

    public CategoryListCell(CategoriesService categoriesService, Runnable onDelete) {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/todoapp/ui/CategoryItem.fxml"
                    )
            );

            root = loader.load();

            controller = loader.getController();
            controller.setCategoriesService(categoriesService);
            controller.setOnDelete(onDelete);


        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(
            Category category,
            boolean empty
    ) {

        super.updateItem(category, empty);

        if (empty || category == null) {

            setGraphic(null);

        } else {

            controller.setCategory(category);

            setGraphic(root);
        }
    }
}