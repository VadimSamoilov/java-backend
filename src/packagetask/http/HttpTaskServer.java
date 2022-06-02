package packagetask.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import packagetask.interfaсe.HistoryManager;
import packagetask.managment.InMemoryTaskManager;
import packagetask.managment.Managers;
import packagetask.model.Epic;
import packagetask.model.SubTask;
import packagetask.model.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;


public  class HttpTaskServer {

    public  HttpTaskServer() throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks/task", new TaskTask());
        httpServer.createContext("/tasks/", new TaskPrioritized());
        httpServer.createContext("/tasks/history", new TaskHistory());
        httpServer.start();
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected Gson createGS() {
        return new com.google.gson.GsonBuilder().setPrettyPrinting().create();
    }


    //обрабатываем запросы
   class TaskTask implements HttpHandler {
        @Override
        public  void handle(HttpExchange httpExchange) throws IOException {
            HTTPTaskManager manager = Managers.getDefault();
            String method = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            String body = readText(httpExchange);
            com.google.gson.Gson gson = createGS();
            if (body.isEmpty()) {
                System.out.println("Body c задачей  пустой. указывается в теле запроса");
                httpExchange.sendResponseHeaders(400, 0);
                return;
            }
            Task task = gson.fromJson(body, Task.class);
            String line = "";
            httpExchange.sendResponseHeaders(200, 0);
            switch (method) {
                case "GET":
                    if (query == null) {
                        line = gson.toJson(manager.getStorageTasks());
                        line = line + gson.toJson(manager.getStorageEpics());
                        line = line + gson.toJson(manager.getStorageSubTask());
                    } else {
                        line = gson.toJson(manager.getTaskById(Integer.parseInt(query.split("=")[1])));
                    }
                    break;
                case "POST":
                    if (query == null) {
                        manager.addSimpleTask(task);
                    } else {
                        if (manager.getStorageEpics().contains(task)) {
                            manager.updateEpic((Epic) task);
                        } else if (manager.getStorageSubTask().contains(task)) {
                            manager.updateSubTask((SubTask) task);
                        }
                    }
                    break;
                case "DELETE":
                    if (query == null) {
                        manager.delitingAllTask();
                    } else {
                        int id = Integer.parseInt(query.split("=")[1]);
                        manager.delitingTaskById(id);
                    }
                    break;
                default:
                    line = "нет такого запроса";
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(line.getBytes());
            }
        }
    }

    class TaskPrioritized implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            InMemoryTaskManager manager = Managers.getDefault();
            String method = httpExchange.getRequestMethod();
            String body = readText(httpExchange);
            com.google.gson.Gson gson = createGS();
            if (body.isEmpty()) {
                System.out.println("Body c задачей  пустой. указывается в теле запроса");
                httpExchange.sendResponseHeaders(400, 0);
                return;
            }
            Task task = gson.fromJson(body, Task.class);
            String line = "";
            switch (method) {
                case "GET":
                    line = gson.toJson(manager.getPrioretyTasks()); // не доделано
                    break;
                case "POST":
                    if (task.getType().equals("EPIC")) {
                        manager.addEpic((Epic) task);
                    } else if (task.getType().equals("SUBTASK")) {
                        manager.addSubTask((SubTask) task);
                    } else {
                        manager.addSimpleTask(task);
                    }
                    break;
                case "DELETE":
                    manager.delitingTaskById(task.getKeyId());
                    break;
                default:
                    line = "нет такого запроса";
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(line.getBytes());
            }
        }
    }

    class TaskHistory implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            HistoryManager manager = Managers.getDefaultHistory();
            com.google.gson.Gson gson = createGS();
            String line = gson.toJson(manager.getHistory());
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(line.getBytes());
            }
        }
    }


}


