package packagetask.model;

import packagetask.util.Status;
import packagetask.util.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {

    protected String name; // название задачи
    protected String description; // описание задачи
    protected Status status; // статус задачи
    protected int keyId; // уникальный ID задачи
    protected TaskType type;
    protected Duration duration; //  продолжительность задачи
    protected LocalDateTime startTime; // начало выполнения задачи
    protected LocalDateTime endTime;

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    // Конструктор для создания SubTask и Epic new
    public Task(String name, String description, TaskType type, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.duration = duration;
        this.startTime = startTime;
        this.status = Status.NEW;
    }

    // базовый конструктор для создания Task
    public Task(String name, String description, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        this.status = Status.NEW;
        this.type = TaskType.TASK;
    }

    // конструктор для чтения из файла new
    public Task(String name, String description,  int keyId,Status status, TaskType type, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.keyId = keyId;
        this.type = type;
        this.duration = duration;
        this.startTime = startTime;
    }

    // конструктор для чтения из файла
    public Task(String name, String description, int keyId, Status status, TaskType type) {
        this.name = name;
        this.description = description;
        this.keyId = keyId;
        this.status = status;
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public TaskType getType() {
        return type;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public String getDescription() {
        return description;
    }

    public int getKeyId() {
        return keyId;
    }


    public Duration getDuration() {
        if (duration == null) {
            return Duration.ZERO;
        }
        return duration;
    }

    public LocalDateTime getStartTime() {
        if (startTime != null) {
            return startTime;
        }
        return LocalDateTime.now();
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (getStartTime() == null) {
            return null;
        }
        return getStartTime().plus(getDuration());
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


    // формат вывода
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return type + "{" + "name ='" + name + '\'' + ", description ='" + description + '\'' + ", keyId = '"
                + keyId + '\'' + ", status = '" + status + '\'' + " duration ='" + duration + '\'' + ", startTime ='" + startTime.format(formatter) + '\'' + "}";
    }
}


