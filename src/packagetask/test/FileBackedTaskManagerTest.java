package packagetask.test;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import packagetask.filebacked.FileBackedTaskManager;
import packagetask.model.Epic;
import packagetask.model.SubTask;
import packagetask.model.Task;


import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

class FileBackedTaskManagerTest {
    @Test
    public void saveAndLoadFromFile() {
        File baseTest = new File("dataBaseTest.csv");
        FileBackedTaskManager managerSave = FileBackedTaskManager.loadFromFile(baseTest);
        managerSave.addEpic(new Epic("Задача №4", "Сюда добавляю подзадачи",
                Duration.ofHours(1), LocalDateTime.of(2023, 4, 2, 1, 0)));
        managerSave.addSubTask(new SubTask("Подзадача",
                "Необходимо настроить добавление", 1, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 16, 1, 0)));
        managerSave.addSubTask(new SubTask("Подзадача",
                "Необходимо настроить добавление в сабтаск", 1, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 19, 1, 0)));

        managerSave.addSimpleTask(new Task("Задача № 1",
                "Правильно прописать Task", Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0)));


        managerSave.getTaskById(1);
        managerSave.getTaskById(2);
        managerSave.getTaskById(3);

        FileBackedTaskManager managerTest = FileBackedTaskManager.loadFromFile(baseTest);
        managerTest.fileReading(baseTest);
        Assertions.assertAll(
                () -> Assertions.assertEquals(managerSave.getStorageEpics().toString(), managerTest.getStorageEpics().toString()),
                () -> Assertions.assertEquals(managerSave.getStorageTasks().toString(), managerTest.getStorageTasks().toString()),
                () -> Assertions.assertEquals(managerSave.getHistoryStorage().toString(), managerTest.getHistoryStorage().toString())
        );
    }

    @Test
    public void saveAndLoadFromFileByEmptyTaskListAndByEmptyHistory() {
        File baseTest = new File("dataBaseTest.csv");
        FileBackedTaskManager managerSave = FileBackedTaskManager.loadFromFile(baseTest);
        Epic epic =new Epic("Задача №4", "Сюда добавляю подзадачи",
                Duration.ofHours(1), LocalDateTime.of(2023, 4, 2, 1, 0));
        managerSave.addEpic(epic);

        managerSave.delitingTaskById(epic.getKeyId());

        FileBackedTaskManager managerTest = FileBackedTaskManager.loadFromFile(baseTest);
        Assertions.assertTrue(managerTest.getStorageEpics().isEmpty());
    }

    @Test
    public void saveAndLoadFromFileByEpicWithoutSubtasks() {
        File baseTest = new File("dataBaseTest.csv");
        FileBackedTaskManager managerSave = FileBackedTaskManager.loadFromFile(baseTest);
        Epic epic =new Epic("Задача №4", "Сюда добавляю подзадачи",
                Duration.ofHours(3), LocalDateTime.of(2022, 4, 2, 1, 0));
//        Epic epic1 =new Epic("Задача №5", "Сюда добавляю один эпик",
//                Duration.ofHours(3), LocalDateTime.of(2022, 4, 8, 1, 0));
        managerSave.addEpic(epic);
    //    managerSave.addEpic(epic1);


        FileBackedTaskManager managerTest = FileBackedTaskManager.loadFromFile(baseTest);
        managerTest.fileReading(baseTest);
        Assertions.assertEquals(managerSave.getStorageEpics(), managerTest.getStorageEpics());
    }
}