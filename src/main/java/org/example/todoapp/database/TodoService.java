package org.example.todoapp.database;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.todoapp.auth.AuthSession;
import org.example.todoapp.model.Todo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TodoService {

    private final FirestoreClient firestoreClient;
    private final AuthSession session;

    public TodoService(AuthSession session) {
        this.firestoreClient =
                new FirestoreClient(session);

        this.session = session;
    }

    private String getTodosPath() {
        return "/users/"
                + session.getUid()
                + "/todos";
    }

    public void updateTodo(
            String id,
            String title,
            String description,
            LocalDate date,
            List<String> categoryIds
    ) throws IOException, InterruptedException {

        String categoryIdsJson =
                buildCategoryIdsJson(categoryIds);

        String dueDateJson = "";

        if (date != null) {

            String formattedDate = date
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant()
                    .toString();

            dueDateJson = """
                "dueDate": {
                    "timestampValue": "%s"
                },
                """.formatted(formattedDate);
        }


        String body = """
            {
              "fields": {
                "title": {
                  "stringValue": "%s"
                },
                "description": {
                  "stringValue": "%s"
                },
                %s
                "completed": {
                  "booleanValue": false
                },
                "categoryIds": {
                  "arrayValue": {
                    "values": [
                      %s
                    ]
                  }
                }
              }
            }
            """.formatted(
                title,
                description,
                dueDateJson,
                categoryIdsJson
        );

        firestoreClient.patch(
                getTodosPath() + "/" + id,
                body
        );
    }


    public void createTodo(
            String title,
            String description,
            LocalDate date,
            List<String> categoryIds
    ) throws Exception {

        String categoryIdsJson =
                buildCategoryIdsJson(categoryIds);

        String dueDateJson = "";

        if (date != null) {

            String formattedDate = date
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant()
                    .toString();

            dueDateJson = """
            "dueDate": {
                "timestampValue": "%s"
            },
            """.formatted(formattedDate);
        }

        String body = """
        {
          "fields": {
            "title": {
              "stringValue": "%s"
            },
            "description": {
              "stringValue": "%s"
            },
            %s
            "completed": {
              "booleanValue": false
            },
            "categoryIds": {
              "arrayValue": {
                "values": [
                  %s
                ]
              }
            }
          }
        }
        """.formatted(
                title,
                description,
                dueDateJson,
                categoryIdsJson
        );

        System.out.println(body);

        firestoreClient.post(
                getTodosPath(),
                body
        );
    }


    public List<Todo> getTodos() throws Exception {

        JsonNode response =
                firestoreClient.get(
                        getTodosPath()
                );

        List<Todo> todos = new ArrayList<>();

        if (response == null) {
            return todos;
        }

        JsonNode documents = response.get("documents");

        if (documents == null || !documents.isArray()) {
            return todos;
        }

        for (JsonNode document : documents) {

            // Get document ID
            String name =
                    document.get("name").asText();

            String id =
                    name.substring(
                            name.lastIndexOf("/") + 1
                    );

            JsonNode fields =
                    document.get("fields");

            // Title
            String title =
                    fields.get("title")
                            .get("stringValue")
                            .asText();

            // Description
            String description =
                    fields.get("description")
                            .get("stringValue")
                            .asText();

            // Completed
            boolean completed =
                    fields.get("completed")
                            .get("booleanValue")
                            .asBoolean();

            LocalDate dueDate = null;

            JsonNode dueDateNode = fields.get("dueDate");

            if (dueDateNode != null
                    && dueDateNode.has("timestampValue")) {

                String timestamp =
                        dueDateNode
                                .get("timestampValue")
                                .asText();

                dueDate = java.time.Instant
                        .parse(timestamp)
                        .atZone(java.time.ZoneOffset.UTC)
                        .toLocalDate();
            }

            // Category IDs
            List<String> categoryIds =
                    new ArrayList<>();

            JsonNode categoryIdsNode =
                    fields.get("categoryIds");

            if (categoryIdsNode != null
                    && categoryIdsNode.has("arrayValue")
                    && categoryIdsNode
                    .get("arrayValue")
                    .has("values")) {

                JsonNode values =
                        categoryIdsNode
                                .get("arrayValue")
                                .get("values");

                for (JsonNode value : values) {

                    categoryIds.add(
                            value.get("stringValue")
                                    .asText()
                    );
                }
            }

            Todo todo = new Todo(
                    id,
                    title,
                    description,
                    completed,
                    dueDate,
                    categoryIds
            );

            todos.add(todo);
        }

        return todos;
    }

    //HELPER METHOD FOR GETTING CATEGORY IDs
    private String buildCategoryIdsJson(
            List<String> categoryIds
    ) {

        return categoryIds.stream()
                .map(id ->
                        String.format(
                                "{ \"stringValue\": \"%s\" }",
                                id
                        )
                )
                .collect(Collectors.joining(","));
    }

    public void deleteTodo(Todo todo) {
        String todoId = todo.getId();
        try {
            firestoreClient.delete(getTodosPath() + "/" + todoId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}