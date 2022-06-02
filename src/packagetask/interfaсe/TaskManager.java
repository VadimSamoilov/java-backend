package packagetask.interfaсe;
import packagetask.model.Epic;
import packagetask.model.SubTask;
import packagetask.model.Task;

import java.util.List;


public interface TaskManager {

    // добавляем простую задачу
     void addSimpleTask(Task tasks);

    // создаем Epic
     void addEpic(Epic epicTask);

    // добавляем subTask в эпик
     void addSubTask(SubTask subTasks);
     List<SubTask> getEpicSubTask(int keyId);

    // получаем задачу по идентификатору
     Object getTaskById(int id);
     void delitingTaskById(int id);
    void updateSubTask(SubTask subTask);
    void updateEpic(Epic epic);
        }



