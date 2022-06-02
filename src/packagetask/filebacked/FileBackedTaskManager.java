package packagetask.filebacked;

import packagetask.managment.*;
import packagetask.exeption.ManagerSaveException;
import packagetask.interfaсe.HistoryManager;
import packagetask.util.Status;
import packagetask.util.TaskType;
import packagetask.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.List.*;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private String taskSavedToFile = "type,name,description,id,epicId,status; \n";
    private static File dataBase;

    public FileBackedTaskManager() {
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        dataBase = file;
        try {
            dataBase.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FileBackedTaskManager();
    }

    @Override
    public void addEpic(Epic epicTask) {
        super.addEpic(epicTask);
        save();
    }

    @Override
    public List<SubTask> getEpicSubTask(int keyId) {
        List<SubTask> value = super.getEpicSubTask(keyId);
        save();
        return value;
    }

    @Override
    public Task getTaskById(int id) {
        return super.getTaskById(id);
    }

    @Override
    public void addSubTask(SubTask subTasks) {
        super.addSubTask(subTasks);
        save();
    }

    @Override
    public void delitingTaskById(int id) {
        super.delitingTaskById(id);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void addSimpleTask(Task task) {
        super.addSimpleTask(task);
        save();

    }

    // сохранение в файл
    public void save()  {
        String saveTask = taskSavedToFile;
        for (Task task : getStorageTasks()) {
            saveTask = saveTask + toString(task) + ";" + "\n";
        }
        for (Epic epic : getStorageEpics()) {
            saveTask = saveTask + toString(epic) + ";" + "\n";
        }
        for (SubTask subTask : getStorageSubTask()) {
            saveTask = saveTask + toString(subTask) + "" + "\n";
        }
        saveTask = saveTask + "\n" + toString(historyStorage);

        try (OutputStream outputStream = new FileOutputStream(dataBase)) {
            outputStream.write(saveTask.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось записать в файл");
        }
    }

    //преобразование строки c историей ID тасок в массив int
    static List<Integer> fromStrings(String value) {
        return (List) Arrays.asList(Arrays.stream(value.split(",")).mapToInt(Integer::parseInt).toArray());

    }

    // сохранение просмотра истории (id Тасок) в строку
    String toString(HistoryManager manager) {
        String history = "";
        int count = 0;
        List<Task> arrayHistory = manager.getHistory();
        for (Task task : arrayHistory) {
            count++;
            if (count < arrayHistory.size()) {
                history = history + task.getKeyId() + ',';
            } else {
                history = history + task.getKeyId();
            }
        }
        return history;
    }

    //чтение сохраненного файла и сохранение задач в Map-ы
    public void fileReading(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] array = new byte[1024];
            int count = inputStream.read(array);
            StringBuilder result = new StringBuilder();
            while (count > 0) {
                String strValue = result.append(new String(array)).toString();
                List<String> taskAll = of(strValue.split("\n"));
                for (String str : taskAll) {
                    if (str.contains("SUBTASK")) {
                        SubTask test = (SubTask) fromString(str);
                        int number = test.getEpicId();
                        for (Epic epic : getStorageEpics()) {
                            if (epic.getKeyId() == number) {
                                addSubTask(test);
                            }
                        }
                    } else if (str.contains("EPIC")) {
                        addEpic((Epic) fromString(str));
                    } else if (str.contains("TASK")) {
                        addSimpleTask(fromString(str));
                    } else if (str.contains("type") || str.contains("")) {
                    } else {
                        saveFileHistory(fromStrings(str));
                    }
                }
                count = inputStream.read(array);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFileHistory(List<Integer> listHistory) {
        for (Integer idKey : listHistory) {
            for (Task task : getStorageTasks()) {
                if (task.getKeyId() == idKey) {
                    historyStorage.add(task);
                }
            }
            for (Epic epic : getStorageEpics()) {
                if (epic.getKeyId() == idKey) {
                    historyStorage.add(epic);
                }
            }
            for (SubTask subTask : getStorageSubTask()) {
                if (subTask.getKeyId() == idKey) {
                    historyStorage.add(subTask);
                }
            }
        }
    }

    //Чтение статуса задачи
    Status changeStatus(String status) {
        if (status.equals("IN_PROGRESS")) {
            return Status.IN_PROGRESS;
        } else if (status.equals("DONE")) {
            return Status.DONE;
        }
        return Status.NEW;
    }

    //Преобразование строки в задачу
    Task fromString(String value) {
        DateTimeFormatter formatterDate = DateTimeFormatter.ISO_DATE_TIME;

        List<String> taskOne = of(value.split(","));


        if (taskOne.get(0).contains("EPIC")) {
            return new Epic(taskOne.get(1), taskOne.get(2), Integer.parseInt(taskOne.get(3)),
                    changeStatus(taskOne.get(4)), TaskType.EPIC, Duration.parse(taskOne.get(5)), LocalDateTime.parse(taskOne.get(6).substring(0, taskOne.get(6).length() - 1), formatterDate));
        } else if (taskOne.get(0).contains("SUBTASK")) {

            return new SubTask(taskOne.get(1), taskOne.get(2), Integer.parseInt(taskOne.get(3)), changeStatus(taskOne.get(5)),
                    getEpicId(taskOne.get(4)), TaskType.SUBTASK, Duration.parse(taskOne.get(6)), LocalDateTime.parse(taskOne.get(7).substring(0, taskOne.get(7).length()), formatterDate)
            );
        }

        return new Task(taskOne.get(1), taskOne.get(2), Integer.parseInt(taskOne.get(3))
                , changeStatus(taskOne.get(4)), TaskType.TASK, Duration.parse(taskOne.get(5)), LocalDateTime.parse(taskOne.get(6).substring(0, taskOne.get(6).length() - 1), formatterDate));
    }

    String toString(Task task) {
        return task.getType() + "," + task.getName() + "," + task.getDescription() + "," + task.getKeyId() + "," + task.getStatus()
                + "," + task.getDuration() + "," + task.getStartTime();
    }

    String toString(Epic epic) {
        return epic.getType() + "," + epic.getName() + "," + epic.getDescription() + "," + epic.getKeyId() + "," + epic.getStatus()
                + "," + epic.getDuration() + "," + epic.getStartTime();
    }

    String toString(SubTask subTask) {
        return subTask.getType() + "," + subTask.getName() + "," + subTask.getDescription() + "," + subTask.getKeyId()
                + "," + getEpicName(subTask.getEpicId()) + "," + subTask.getStatus() + "," + subTask.getDuration() + "," + subTask.getStartTime();
    }

}
