package packagetask.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import packagetask.http.HTTPTaskManager;
import packagetask.http.KVServer;
import packagetask.http.KVTaskClient;
import packagetask.model.Epic;
import packagetask.model.SubTask;
import packagetask.model.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPTaskManagerTest {
    private final LocalDateTime startTime = LocalDateTime.MIN;
    private final Duration duration = Duration.between(startTime, startTime.plusSeconds(10));

    private final Task task = new Task("Задача № 2",
            "Правильно прописать subTask", Duration.ofHours(1),
            LocalDateTime.of(202, 4, 12, 1, 0));
    private final Epic epic = new Epic("Задача №4", "Сюда добавляю подзадачи",
            Duration.ofHours(1), LocalDateTime.of(2023, 4, 15, 1, 0));
    private final SubTask subtask = new SubTask("Подзадача",
            "Необходимо настроить добавление", 1, Duration.ofHours(1),
            LocalDateTime.of(2022, 3, 18, 1, 0));

    private final HTTPTaskManager manager = new HTTPTaskManager("http://localhost:8078/");
    private static KVTaskClient client;
    private static com.google.gson.Gson gson;

    @BeforeAll
    public static void beforeAll() {
        try {
            KVServer kvServer = new KVServer();
            kvServer.start();
            //  gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
            client = new KVTaskClient("http://localhost:8078/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void taskTask() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/task/task/");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(response.body(), manager.getStorageTasks());
    }

    @Test
    void taskSubtask() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/task/subtask/");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(response.body(), manager.getStorageSubTask());
    }

    @Test
    void taskEpic() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/task/epic/");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(response.body(), manager.getStorageEpics());
    }

    @Test
    void saveTask() {
        manager.addSimpleTask(task);
        String body = client.load("/tasks/task");
        com.google.gson.JsonArray jsonArray = com.google.gson.JsonParser.parseString(body).getAsJsonArray();
        Task taskInServer = gson.fromJson(jsonArray.get(0), Task.class);
        assertEquals(taskInServer, task);
    }

    @Test
    void saveEpic() {
        manager.addEpic(epic);
        String body = client.load("/tasks/epic");
        com.google.gson.JsonArray jsonArray = com.google.gson.JsonParser.parseString(body).getAsJsonArray();
        Epic taskInServer = gson.fromJson(jsonArray.get(0), Epic.class);
        assertEquals(taskInServer, epic);
    }

    @Test
    void saveSubtask() {
        manager.addSubTask(subtask);
        String body = client.load("/tasks/epic");
        com.google.gson.JsonArray jsonArray = com.google.gson.JsonParser.parseString(body).getAsJsonArray();
        SubTask taskInServer = gson.fromJson(jsonArray.get(0), SubTask.class);
        assertEquals(taskInServer, subtask);
    }
}