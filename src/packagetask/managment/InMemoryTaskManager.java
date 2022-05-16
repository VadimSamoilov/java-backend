package packagetask.managment;

import packagetask.exeption.ManagerSaveException;
import packagetask.interfaсe.TaskManager;
import packagetask.model.Epic;
import packagetask.model.SubTask;
import packagetask.model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    private HashMap<Integer, Task> storageTasks = new HashMap<>();
    private HashMap<Integer, Epic> storageEpics = new HashMap<>();
    private HashMap<Integer, SubTask> storageSubTask = new HashMap<>();
    protected InMemoryHistoryManager historyStorage = Managers.getDefaultHistory();

    protected Set<LocalDateTime> prioretyTasks = new TreeSet<>(new Comparator<LocalDateTime>() {
        @Override
        public int compare(LocalDateTime o1, LocalDateTime o2) {
            if (o1.isBefore(o2)){
                return -1;
            } else if (o1.isAfter(o2)){
                return 1;
            } else {
                return 0;
            }
        }
    }); //Тут TreeSet

    public InMemoryHistoryManager getHistoryStorage() {
        return historyStorage;
    }

    //возвращает массив всех задач
    public ArrayList<Task> getStorageTasks() {
        return new ArrayList<>(storageTasks.values());
    }

    //возвращает массив всех эпиков
    public ArrayList<Epic> getStorageEpics() {
        return new ArrayList<>(storageEpics.values());
    }

    //возвращает массив всех подадач
    public ArrayList<SubTask> getStorageSubTask() {
        return new ArrayList<>(storageSubTask.values());
    }

    // поиск эпика по имени (для чтения из файла)
    public int getEpicId(String epicName) {
        for (Epic epic : storageEpics.values()) {
            if (epic.getName().equals(epicName)) {
                id = epic.getKeyId();
                return id;
            }
        }
        return id;
    }

    public ArrayList<SubTask> getEpicIdSubtask(int epicId) {
        for (Epic epic : storageEpics.values()) {
            if (epic.getKeyId() == epicId) {
                return epic.gettingAllTheEpicSubTasks();
            }
        }
        return null;
    }

    // поиск эпика по Id
    public String getEpicName(int epicId) {
        Epic epic = null;
        if (storageEpics.containsKey(epicId)) {
            epic = storageEpics.get(epicId);
        }
        return epic.getName();
    }

    // добавляем простую задачу
    public void addSimpleTask(Task task) {
        task.setKeyId(++id);
        storageTasks.put(task.getKeyId(), task);
        prioretyTasks.add(task.getStartTime());

    }

    // создаем Epic
    public void addEpic(Epic epicTask) {
        epicTask.setKeyId(++id);
        storageEpics.put(epicTask.getKeyId(), epicTask);
    }

    // добавляем subTask в эпик
    public void addSubTask(SubTask subTasks) {
        try {
            if (checkPriority(subTasks)) {
                if (storageEpics.containsKey(subTasks.getEpicId())) {
                    Epic epics = storageEpics.get(subTasks.getEpicId());
                    subTasks.setKeyId(++id);
                    epics.addSubTask(subTasks);
                    epics.currentStatus();
                    storageSubTask.put(subTasks.getKeyId(), subTasks);
                    prioretyTasks.add(subTasks.getStartTime());
                } else {
                    System.out.println("Такого Epic-a не существует");
                }
            }
        } catch (RuntimeException e) {
            throw new ManagerSaveException("Ошибка! Пересечение задач!" + ": " + e.getMessage());
        }

    }

    public HashMap<Integer,Task> getPrioretyTasks() {
        HashMap<Integer,Task> priortyList = new HashMap<>();
        HashMap<Integer, Task> allTask = new HashMap<>();
        allTask.putAll(storageTasks);
        allTask.putAll(storageSubTask);
       for (Task task:allTask.values()){
           for (int i = 0; i < prioretyTasks.size(); i++) {
               if (task.getStartTime().equals(prioretyTasks.toArray()[i])){
                   priortyList.put(task.getKeyId(),task);
               }
           }
       }
        return priortyList;
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        SubTask updateSubtask = storageSubTask.get(subTask.getKeyId());
        if ((updateSubtask != null) && (checkPriority(subTask))) {
            updateSubtask.setDescription(subTask.getDescription());
            updateSubtask.setName(subTask.getName());
            updateSubtask.setStatus(subTask.getStatus());
            updateSubtask.setStartTime(subTask.getStartTime());
            updateSubtask.setDuration(subTask.getDuration());
            Epic epic = storageEpics.get(subTask.getEpicId());
            epic.currentStatus();
        } else {
            System.out.println("Подзадача не найдена. Невозможно обновить");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (storageEpics.containsKey(epic.getKeyId())) {
            storageEpics.put(epic.getKeyId(), epic);
            epic.currentStatus();
        } else {
            System.out.println("Эпик не найден. Невозможно обновить");
        }
    }

    // получаем subTaskи конкретного эпика, также добавляем их в историю просмотра
    public ArrayList<SubTask> getEpicSubTask(int keyId) {
        if (storageEpics.containsKey(keyId)) {
            Epic epic = storageEpics.get(keyId);
            historyStorage.add(epic);
            ArrayList<SubTask> subTasks = epic.gettingAllTheEpicSubTasks();
            for (Task subtask : subTasks) {
                historyStorage.add(subtask);
            }
            return epic.gettingAllTheEpicSubTasks();
        } else {
            System.out.println("Такого Epica не существует");
        }
        return null;
    }

    // получаем задачу по идентификатору
    public Object getTaskById(int id) throws ManagerSaveException {
        Object obj = null;
        if (storageTasks.containsKey(id)) {
            obj = storageTasks.get(id);
        } else if (storageEpics.containsKey(id)) {
            obj = storageEpics.get(id);
        } else if (storageSubTask.containsKey(id)) {
            obj = storageSubTask.get(id);
        }

        if (obj !=null){
            historyStorage.add((Task) obj);
            return obj;
        } else {
            return null;
        }
    }

    // удаление задачи по id
    @Override
    public void delitingTaskById(int id) {
        if (storageTasks.containsKey(id)) {
            storageTasks.remove(id);
        } else if (storageEpics.containsKey(id)) {
            ArrayList<SubTask> subList = storageEpics.get(id).gettingAllTheEpicSubTasks();
            for (int i = 0; i < subList.size(); i++) {
                SubTask subs = subList.get(i);
                int keySub = subs.getKeyId();
                if (storageSubTask.containsValue(subs)) {
                    storageSubTask.remove(keySub);
                    historyStorage.remove(keySub);
                }
            }
            storageEpics.remove(id);
            historyStorage.remove(id);
        } else if (storageSubTask.containsKey(id)) {
            storageSubTask.remove(id);
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    // удаление всех задач
    public void delitingAllTask() {
        storageTasks.clear();
        storageSubTask.clear();
        storageEpics.clear();
        List<Task> arrayOldHistory = historyStorage.getHistory();
        for (Task task : arrayOldHistory) {
            historyStorage.remove(task.getKeyId());
        }
    }

    //проверка пересечения
    public boolean checkPriority(Task task) {
        int count = 0;
        HashMap<Integer, Task> allTask = new HashMap<>();
        allTask.putAll(storageTasks);
        allTask.putAll(storageSubTask);
        for (Task task1 : allTask.values()) {
            if (task1.getStartTime().isBefore(task.getStartTime()) &&
                    task1.getEndTime().isBefore(task.getEndTime()) ||
                    (task1.getStartTime().isAfter(task.getStartTime()) &&
                            task1.getEndTime().isAfter(task.getEndTime()))) {
            } else {
                count++;
            }
        }
        if (count == 0) {
            return true;
        } else {
            return false;
        }
    }
}
