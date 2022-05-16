package packagetask.test;

import packagetask.filebacked.FileBackedTaskManager;
import packagetask.model.Epic;
import packagetask.model.SubTask;
import packagetask.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TasksManagerTest extends FileBackedTaskManager { //Тестовый класс

    public void start(FileBackedTaskManager fileBackedTaskManager) {
        // добавление новой задачи
        fileBackedTaskManager.addSimpleTask(new Task("Задача № 1",
                "Правильно прописать Task", Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0)));
        fileBackedTaskManager.addSimpleTask(new Task("Задача № 2",
                "Правильно прописать subTask", Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0)));

        // добавление эпика с подзадачами
        fileBackedTaskManager.addEpic(new Epic("Задача №4", "Сюда добавляю подзадачи",
                Duration.ofHours(1), LocalDateTime.of(2023, 4, 8, 1, 0)));
        fileBackedTaskManager.addSubTask(new SubTask("Подзадача",
                "Необходимо настроить добавление", 3, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0)));
        fileBackedTaskManager.addSubTask(new SubTask("Подзадача",
                "Необходимо настроить добавление в сабтаск", 3,Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0)));


        // добавление эпика без подзадач
        fileBackedTaskManager.addEpic(new Epic("Задача №5", "Сюда добавляю один эпик",
                Duration.ofHours(1), LocalDateTime.of(2023, 4, 8, 1, 0)));
        fileBackedTaskManager.addSubTask(new SubTask("Подзадача Эпика 6",
                "Необходимо настроить добавлени в файл", 6,Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0)));
    }

    public void CodeTest(FileBackedTaskManager fileBackedTaskManager) {

        // вывод всех созданных тасок
        for (Task task : fileBackedTaskManager.getStorageTasks()) {
            System.out.println(task.toString());
        }

        // вывод созданных эпиков
        for (Epic epic : fileBackedTaskManager.getStorageEpics()) {
            System.out.println(epic.toString());
        }
        System.out.println();

        // вывод подзадач конкретного эпика
        try {
            for (SubTask subTask : fileBackedTaskManager.getEpicSubTask(3)) {
                System.out.println(subTask.toString());
            }
        } catch (Exception e) {
        }

        //повторный вывод всех задач
        for (Task task : fileBackedTaskManager.getStorageTasks()) {
            System.out.println(task.toString());
        }
        System.out.println();
    }

    public void testHistory(FileBackedTaskManager fileBackedTaskManager) {
        // проверка заполнения истории
        System.out.println("\nДавай проверим заполнение истории");
        ArrayList<Task> testArray = (ArrayList<Task>) fileBackedTaskManager.getHistoryStorage().getHistory();
        for (Task task : testArray) {
            System.out.println(task.toString());
        }
    }
}
