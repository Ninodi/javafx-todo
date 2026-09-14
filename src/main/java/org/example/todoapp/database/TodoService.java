package org.example.todoapp.database;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.todoapp.auth.AuthSession;
import org.example.todoapp.model.Todo;

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

        firestoreClient.post(getTodosPath(), body);
    }

    public void testCreateTodo()
            throws Exception {

        String body = """
            {
              "fields": {
                "title": {
                  "stringValue": "Test POST Todo"
                },
                "description": {
                  "stringValue": "Created using FirestoreClient"
                },
                "completed": {
                  "booleanValue": false
                }
              }
            }
            """;

        JsonNode response =
                firestoreClient.post(
                        getTodosPath(),
                        body
                );

        System.out.println("Created todo:");
        System.out.println(response);
    }


    public List<Todo> getTodos()
            throws Exception {

        JsonNode response =
                firestoreClient.get(
                        getTodosPath()
                );

        List<Todo> todos =
                new ArrayList<>();

        if (response == null) {
            return todos;
        }

        JsonNode documents =
                response.get("documents");

        if (documents == null) {
            return todos;
        }

        for (JsonNode document : documents) {

            String name =
                    document.get("name").asText();

            String id =
                    name.substring(
                            name.lastIndexOf("/") + 1
                    );

            JsonNode fields =
                    document.get("fields");

            String title =
                    fields.get("title")
                            .get("stringValue")
                            .asText();

            String description =
                    fields.get("description")
                            .get("stringValue")
                            .asText();

            boolean completed =
                    fields.get("completed")
                            .get("booleanValue")
                            .asBoolean();

            Todo todo = new Todo(
                    id,
                    title,
                    description,
                    completed,
                    null
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
}