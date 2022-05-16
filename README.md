## Это репозиторий проекта "Менеджер задач"

В back-end реализована **возможность**:
1. Добавлять и удалять простые и сложные (составные) задачи.
2. Сохранять и считывать их с файла
3. Хранит историю и время добавления/изменения задач
4. Проверяет не пересекаются ли задачи по времени выполнения
5. Основные методы покрыты тестами JUnit

Код написан на Java 17.0. Пример кода:
```java
public class Practicum {
    public static void main(String[] args) {
        File base = new File("dataBase.csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager().loadFromFile(base);
        
        //тест на корректность сохранения и поиска
        TasksManagerTest tasksManagerTest = new TasksManagerTest(); 
        
        //Чтение из файла
        fileBackedTaskManager.fileReading(base);
         
        //Создание и сохранение в файл
        tasksManagerTest.start(fileBackedTaskManager);
        tasksManagerTest.CodeTest(fileBackedTaskManager);
    }
}
```
---------------------------
Java 17.0, JUnit 5.8.1