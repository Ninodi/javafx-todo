package org.example.todoapp.database;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.tools.jconsole.JConsoleContext;
import org.example.todoapp.auth.AuthSession;
import org.example.todoapp.model.Category;
import org.example.todoapp.model.Todo;
import org.example.todoapp.navigation.AppRouter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoriesService {
    private final FirestoreClient firestoreClient;
    private final AuthSession session;
    private String editCategoryId;

    public CategoriesService(AuthSession session) {
        this.firestoreClient =
                new FirestoreClient(session);

        this.session = session;
    }
    private String getCategoriesPath() {
        return "/users/"
                + session.getUid()
                + "/categories";
    }

    public void createCategory (String title, String color) throws Exception {
        String body = String.format("""
        {
          "fields": {
            "title": {
              "stringValue": "%s"
            },
            "color": {
              "stringValue": "%s"
            }
          }
        }
        """, title, color);

        JsonNode response = firestoreClient.post(getCategoriesPath(), body);
    }

    public List<Category> getCategories () throws Exception{
        JsonNode response = firestoreClient.get(getCategoriesPath());

        List<Category> categories =
                new ArrayList<>();

        if(response == null) {
            return categories;
        }

        JsonNode documents =
                response.get("documents");

        if(documents == null) {
            return categories;
        }

        for (JsonNode document : documents) {

            String name = document.get("name").asText();
            String id = name.substring(name.lastIndexOf("/") + 1);

            JsonNode fields = document.get("fields");

            String title = fields.get("title")
                    .get("stringValue")
                    .asText();

            String color = fields.get("color")
                    .get("stringValue")
                    .asText();

            Category category = new Category(
                    id,
                    title,
                    color
            );

            categories.add(category);
        }

        return categories;

    }

    public void deleteCategory(String id) throws Exception {
        firestoreClient.delete(
                getCategoriesPath() + "/" + id
        );
    }

    public void setEditCategoryId (String editCategoryId) {
        this.editCategoryId = editCategoryId;
    }

    public void updateCategory(String id, String title, String color)
            throws IOException, InterruptedException {

        String body = String.format("""
    {
      "fields": {
        "title": {
          "stringValue": "%s"
        },
        "color": {
          "stringValue": "%s"
        }
      }
    }
    """, title, color);

        firestoreClient.patch(
                getCategoriesPath() + "/" + id,
                body
        );
    }
}
