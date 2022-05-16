package packagetask.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import packagetask.managment.InMemoryTaskManager;
import packagetask.model.Epic;
import packagetask.model.SubTask;
import packagetask.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


class InMemoryTaskManagerTest {

    private TaskManagerTest taskManagerTest;
    private Task task;
    private Task task1;
    private Epic epic;
    private Epic epic1;
    private SubTask subTask;
    private SubTask subTask1;


    @BeforeEach
    public void beforeEach() {
        taskManagerTest = new TaskManagerTest() {
        };
        task = new Task("Задача № 1",
                "Правильно прописать Task", Duration.ofHours(1),
                LocalDateTime.of(2022, 6, 8, 11, 0));
        task1 = new Task("Задача № 2",
                "Правильно прописать subTask", Duration.ofHours(1),
                LocalDateTime.of(202, 4, 12, 1, 0));

        // добавление эпика с подзадачами
        epic = new Epic("Задача №4", "Сюда добавляю подзадачи",
                Duration.ofHours(1), LocalDateTime.of(2023, 4, 15, 1, 0));
        subTask = new SubTask("Подзадача",
                "Необходимо настроить добавление", 1, Duration.ofHours(1),
                LocalDateTime.of(2022, 3, 18, 1, 0));
        subTask1 = new SubTask("Подзадача",
                "Необходимо настроить добавление в сабтаск", 5, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 23, 1, 0));


        // добавление эпика без подзадач
        epic1 = new Epic("Задача №5", "Сюда добавляю один эпик",
                Duration.ofHours(1), LocalDateTime.of(2023, 4, 8, 1, 0));
    }

    //добавление задач всех типов
    @Test
    public void shouldBeAddTaskEpicAndSubTask() {
        taskManagerTest.startTest();
        List<Task> epicChecklist = List.of(epic, epic1);
        List<Task> taskCheckList = List.of(task, task1);
        List<Task> subTaskCheckList = List.of(subTask, subTask1);

        Assertions.assertTrue(epicChecklist.containsAll(taskManagerTest.getStorageEpics()));
        Assertions.assertTrue(taskCheckList.containsAll(taskManagerTest.getStorageTasks()));
        Assertions.assertTrue(subTaskCheckList.containsAll(taskManagerTest.getStorageSubTask()));
    }

    @Test
    public void forIdEqualToNullDeleteAll() {
        taskManagerTest.startTest();
        taskManagerTest.delitingAllTask();

        Assertions.assertTrue(taskManagerTest.getStorageTasks().isEmpty());
    }

    @Test
    public void forDelitingTaskById() {
        taskManagerTest.startTest();
        taskManagerTest.delitingTaskById(task1.getKeyId());

        Assertions.assertFalse(taskManagerTest.getStorageTasks().contains(task1.getKeyId()));
    }

    @Test
    public void forDelitingEpicById() {
        taskManagerTest.startTest();
        taskManagerTest.delitingTaskById(epic.getKeyId());

        Assertions.assertFalse(taskManagerTest.getStorageEpics().contains(epic.getKeyId()));
    }

    @Test
    public void forDelitingSubTaskById() {
        taskManagerTest.startTest();
        taskManagerTest.delitingTaskById(subTask.getKeyId());

        Assertions.assertFalse(taskManagerTest.getStorageSubTask().contains(subTask.getKeyId()));
    }


    @Test
    public void forGetSubTasksEpicList() {
        taskManagerTest.startTest();
        List<Task> allSubTaskSpecificEpicCheckList = List.of(subTask);

        Assertions.assertTrue(allSubTaskSpecificEpicCheckList.containsAll(taskManagerTest.getEpicIdSubtask(epic.getKeyId())));
    }

    @Test
    public void forGetTaskById() {
        taskManagerTest.startTest();
        Assertions.assertEquals(epic, taskManagerTest.getTaskById(1));
        Assertions.assertNotEquals(task, taskManagerTest.getTaskById(8));
        Assertions.assertNotEquals(subTask, taskManagerTest.getTaskById(13));
    }

    @Test
    public void forUpdateTask() {
        taskManagerTest.startTest();
        Assertions.assertEquals(task, taskManagerTest.getTaskById(3));
        Assertions.assertEquals(epic, taskManagerTest.getTaskById(1));
        Assertions.assertNotEquals(subTask1, taskManagerTest.getTaskById(41), "Такой Сабтаски нет");
    }

    @Test
    public void forHistory() {
        taskManagerTest.startTest();
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add((Task) taskManagerTest.getTaskById(task.getKeyId()));
        tasks.add((Epic) taskManagerTest.getTaskById(epic.getKeyId()));
        tasks.add((SubTask) taskManagerTest.getTaskById(subTask.getKeyId()));

        Assertions.assertEquals(tasks, taskManagerTest.getHistoryStorage().getHistory());
    }

    @Test
    public void forGetPrioritizedTasks() {
        taskManagerTest.startTest();
        HashMap<LocalDateTime, Integer> tasks = new HashMap<>();
        tasks.put(subTask.getStartTime(), 2);
        tasks.put(task.getStartTime(), 3);
        ArrayList<Integer> longTask = new ArrayList<>(tasks.values());

        Assertions.assertEquals(longTask, new ArrayList<>(taskManagerTest.getPrioretyTasks().keySet()));
    }


    abstract class TaskManagerTest extends InMemoryTaskManager {

        void startTest() {
            taskManagerTest.addEpic(epic);
            taskManagerTest.addSubTask(subTask);
            taskManagerTest.addSimpleTask(task);
//            taskManagerTest.addSimpleTask(task1);
//            taskManagerTest.addEpic(epic1);
//            taskManagerTest.addSubTask(subTask1);

        }


    }


}