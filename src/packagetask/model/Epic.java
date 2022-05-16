package packagetask.model;

import packagetask.util.Status;
import packagetask.util.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> epicSubTaskList = new ArrayList<>();
    private int current = 0;

    public Epic(String name, String description, Duration duration, LocalDateTime startTime) {
        super(name, description, TaskType.EPIC, duration, startTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        LocalDateTime startFirstSubtask = LocalDateTime.MAX;
        if (!epicSubTaskList.isEmpty()) {
            for (SubTask subTask : epicSubTaskList) {
                if (subTask.getEndTime() == null) {
                    startFirstSubtask = LocalDateTime.now();
                }
            }
            for (SubTask subtask : epicSubTaskList) {
                if (subtask.getStartTime() != null &&
                        startFirstSubtask.isAfter(subtask.getStartTime())) {
                    startFirstSubtask = subtask.getStartTime().minusSeconds(5);
                }
            }
            if (startFirstSubtask.isEqual(LocalDateTime.MAX)) {
                return null;
            } else {
                return startFirstSubtask;
            }
        }
        startFirstSubtask = super.getStartTime();
        return startFirstSubtask;
    }

    @Override
    public Duration getDuration() {
        if (getStartTime() == null) {
            return super.getDuration();
        }
        if (epicSubTaskList.isEmpty()) {
            LocalDateTime lastSubtaskEnd = LocalDateTime.MIN;
            for (SubTask subtask : epicSubTaskList) {
                if (subtask.getStartTime() == null) {
                    break;
                }
                if (subtask.getEndTime().isAfter(lastSubtaskEnd)) {
                    Duration sumDurationSubTask = Duration.ZERO;
                    for (SubTask subTask : epicSubTaskList) {
                        Duration durationSubTask = subTask.getDuration();
                        sumDurationSubTask = sumDurationSubTask.plus(durationSubTask);
                    }
                    lastSubtaskEnd = subtask.getStartTime().plus(sumDurationSubTask);
                }
            }
            return Duration.between(getStartTime(), lastSubtaskEnd);
        }
        return super.getDuration();
    }

    // конструктор для чтения из файла
    public Epic(String name, String description, int keyId, Status status, TaskType type) {
        super(name, description, keyId, status, type);
    }

    // конструктор для чтения из файла new
    public Epic(String name, String description, int keyId, Status status, TaskType type, Duration duration, LocalDateTime startTime) {
        super(name, description, keyId, status, type, duration, startTime);
    }

    @Override
    public String toString() {
        return type + "{name ='" + name + '\'' + ", description ='" + description + '\'' + ", keyId = '"
                + keyId + '\'' + ", status = '" + status + '\'' + " duration ='" + duration + '\''
                + ", startTime ='" + getStartTime() + '\'' + ", epicSubTaskList.size = '" + epicSubTaskList.size() + '\'' + "}";
    }

    public void addSubTask(SubTask subTask) {
        epicSubTaskList.add(subTask);
        currentStatus();
    }

    public ArrayList<SubTask> gettingAllTheEpicSubTasks() {
        return epicSubTaskList;
    }

    // проверка актуальности статуса Epic-а new
    public void currentStatus() {
        for (int i = 0; i < epicSubTaskList.size(); i++) {
            SubTask tio = epicSubTaskList.get(i);
            if (epicSubTaskList.get(i).status.equals(Status.NEW)) {
                current--;
                setStatus(Status.NEW);
            } else if (epicSubTaskList.get(i).status.equals(Status.DONE)) {
                current++;
                if (current == epicSubTaskList.size()) {
                    setStatus(Status.DONE);
                } else {
                    setStatus(Status.IN_PROGRESS);
                }
            } else if (epicSubTaskList.size() == 0) {
                setStatus(Status.NEW);
            } else {
                setStatus(Status.IN_PROGRESS);
            }
        }
    }
}

