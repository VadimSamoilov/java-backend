package packagetask.exeption;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String s) {
        super("Ошибка сохранения в файл");

    }

}
