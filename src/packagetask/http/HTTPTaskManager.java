package packagetask.http;

import com.google.gson.reflect.TypeToken;
import packagetask.filebacked.FileBackedTaskManager;
import packagetask.model.Epic;
import packagetask.model.SubTask;
import packagetask.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HTTPTaskManager extends FileBackedTaskManager {

    private KVTaskClient client;
    private com.google.gson.Gson gson;

    public HTTPTaskManager(String url) {
        super();
        this.client = new KVTaskClient(url);
        this.gson = new com.google.gson.GsonBuilder().create();
        load();
    }

    @Override
    public void save() {
        String tasksJson = gson.toJson(super.getStorageTasks().toString());
        String epicsJson = gson.toJson(super.getStorageEpics().toString());
        String subTasksJson = gson.toJson(super.getStorageSubTask().toString());
        String historyJson = gson.toJson(historyStorage.toString());
        // записать добавленное
        client.put("tasks", tasksJson);
        client.put("epics", epicsJson);
        client.put("subtasks", subTasksJson);
        client.put("history", historyJson);
    }
    private void load () {
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        for (Task task:tasks){
            addSimpleTask(task);
        }
        ArrayList<Epic> epics = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        for (Epic epic:epics){
            addEpic(epic);
        }
        ArrayList<SubTask> subtasks = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<SubTask>>() {
        }.getType());
        for (SubTask subTask:subtasks){
            addSubTask(subTask);
        }

        List<Integer> history = gson.fromJson(client.load("history"), new TypeToken< List<Integer>>() {
        }.getType());
        saveFileHistory(history);
        }

    }
