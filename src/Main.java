import packagetask.http.KVServer;
import packagetask.http.KVTaskClient;
import packagetask.model.Task;
import packagetask.test.TasksManagerTest;
import packagetask.filebacked.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    //   private static final HTTPTaskManager manager = new HTTPTaskManager("http://localhost:8078/");
    public static void main(String[] args) throws IOException {
//        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager().loadFromFile(new File("dataBase.csv"));
//        TasksManagerTest tasksManagerTest = new TasksManagerTest(); //тест на корректность сохранения и поиска
//
////      тест на чтение из файла
//        //fileBackedTaskManager.fileReading(base);
//
////      тест на создание и сохранение в файл
//       tasksManagerTest.start(fileBackedTaskManager);
//        tasksManagerTest.CodeTest(fileBackedTaskManager);

//      HttpTaskServer httpServer = new HttpTaskServer();
        Task task = new Task("Задача № 2",
                "Правильно прописать subTask", Duration.ofHours(1),
                LocalDateTime.of(202, 4, 12, 1, 0));

        String json = "{\n" +
                "\t\"title\": \"Subtask\",\n" +
                "\t\"description\": \"Sub \",\n" +
                "\t\"id\": 1,\n" +
                "\t\"status\": \"NEW\",\n" +
                "\t\"type\": \"SUBTASK\",\n" +
                "\t\"epicID\": 0,\n" +
                "\t\"duration\": 0\n" +
                "}";
        KVServer server = new KVServer();
        server.start();
        KVTaskClient client = new KVTaskClient("http://localhost:8078/");
        client.put("DEBUG", json);
        System.out.println(client.load("DEBUG"));

        //  HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078/");
        //   manager.addTask(task);
        System.out.println(client.load("DEBUG"));

    }
}


