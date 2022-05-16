import packagetask.test.TasksManagerTest;
import packagetask.filebacked.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        File base = new File("dataBase.csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager().loadFromFile(base);
        TasksManagerTest tasksManagerTest = new TasksManagerTest(); //тест на корректность сохранения и поиска

//      тест на чтение из файла
         fileBackedTaskManager.fileReading(base);

//      тест на создание и сохранение в файл
        //tasksManagerTest.start(fileBackedTaskManager);
        tasksManagerTest.CodeTest(fileBackedTaskManager);

    }
}


