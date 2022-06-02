package packagetask.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import packagetask.model.Epic;
import packagetask.model.SubTask;
import packagetask.util.Status;

import java.time.Duration;
import java.time.LocalDateTime;

class EpicTest {
    private Epic epic;
    private SubTask subTask;


    @BeforeEach
    public void beforeEach() {
        epic = new Epic("Задача №4", "Сюда добавляю подзадачи", Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0));
    }

    @Test
    public void emptyTaskList() {
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void shouldBeStatusNewByAllSubtasksStatusNew() {
        subTask = new SubTask("Подзадача", "Необходимо настроить добавление", 3, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0));
        subTask.setStatus(Status.NEW);
        epic.addSubTask(subTask);

        subTask = new SubTask("Подзадача", "Необходимо настроить добавление в сабтаск", 3, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0));
        subTask.setStatus(Status.NEW);
        epic.addSubTask(subTask);
        subTask = new SubTask("Сделка", "Необходимо проврить доки на авто", 3, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0));
        subTask.setStatus(Status.NEW);
        epic.addSubTask(subTask);
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void shouldBeStatusDoneByAllSubtasksStatusDone() {
        subTask = new SubTask("Подзадача", "Необходимо настроить добавление", 3, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0));
        subTask.setStatus(Status.DONE);
        epic.addSubTask(subTask);

        subTask = new SubTask("Подзадача", "Необходимо настроить добавление в сабтаск", 3, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0));
        subTask.setStatus(Status.DONE);
        epic.addSubTask(subTask);
        subTask = new SubTask("Сделка", "Необходимо проврить доки на авто", 3, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0));
        subTask.setStatus(Status.DONE);
        epic.addSubTask(subTask);

        Assertions.assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void shouldBeStatusInProgressBySubtasksStatusDoneAndNew() {
        subTask = new SubTask("Подзадача", "Необходимо настроить добавление", 3, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0));
        subTask.setStatus(Status.DONE);
        epic.addSubTask(subTask);

        subTask = new SubTask("Подзадача", "Необходимо настроить добавление в сабтаск", 3, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0));
        subTask.setStatus(Status.NEW);
        epic.addSubTask(subTask);
        subTask = new SubTask("Сделка", "Необходимо проврить доки на авто", 3, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0));
        subTask.setStatus(Status.DONE);
        epic.addSubTask(subTask);

        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldBeStatusInProgressByAllSubtasksStatusInProgress() {
        subTask = new SubTask("Подзадача", "Необходимо настроить добавление", 3, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0));
        subTask.setStatus(Status.IN_PROGRESS);
        epic.addSubTask(subTask);

        subTask = new SubTask("Подзадача", "Необходимо настроить добавление в сабтаск", 3, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0));
        subTask.setStatus(Status.IN_PROGRESS);
        epic.addSubTask(subTask);
        subTask = new SubTask("Сделка", "Необходимо проврить доки на авто", 3, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0));
        subTask.setStatus(Status.IN_PROGRESS);
        epic.addSubTask(subTask);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }


    @Test
    void getSubTasksList() {
        subTask = new SubTask("Подзадача", "Необходимо настроить добавление", 3, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0));
        epic.addSubTask(subTask);
        subTask = new SubTask("Подзадача", "Необходимо настроить добавление в сабтаск", 3, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0));
        epic.addSubTask(subTask);
        subTask = new SubTask("Сделка", "Необходимо проврить доки на авто", 3, Duration.ofHours(1),
                LocalDateTime.of(2023, 4, 8, 1, 0));
        epic.addSubTask(subTask);

        Assertions.assertEquals(3, epic.gettingAllTheEpicSubTasks().size());
    }

}