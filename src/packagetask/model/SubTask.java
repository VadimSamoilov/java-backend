package packagetask.model;

import packagetask.util.Status;
import packagetask.util.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;

    //rj

    public SubTask(String name, String description,int epicId,  Duration duration, LocalDateTime startTime) {
        super(name, description, TaskType.SUBTASK, duration, startTime);
        this.epicId = epicId;
        this.setStatus(Status.NEW);
    }
    //перегруженный конструктор для записи subTask в Map из файла csv.
    public SubTask(String name, String description, int keyId, Status status, int epicId, TaskType type, Duration duration, LocalDateTime startTime) {
        super(name, description, keyId, status, type, duration, startTime);
        this.epicId = epicId;
    }

    //перегруженный конструктор для записи subTask в Map из файла csv.
//    public SubTask(String name, String description, int keyId, int epicId, Status status, TaskType type,
//                   Duration duration, LocalDateTime startTime) {
//        super(name, description, keyId, status, type);
//        this.epicId = epicId;
//    }

    @Override
    public LocalDateTime getStartTime() {
        return super.getStartTime();
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return type + "{name ='" + name + '\'' + ", description ='" + description + '\'' + ", keyId = '"
                + keyId + '\'' + ", status = '" + status + '\'' + ", epicId = '" + epicId + '\''
                + " duration ='" + duration + '\'' + ", startTime ='" + startTime + '\''+ "}";
    }
}
