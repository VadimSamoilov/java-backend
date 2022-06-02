package packagetask.managment;

import packagetask.http.HTTPTaskManager;
import packagetask.interfaсe.HistoryManager;

public class Managers {
    static HTTPTaskManager defaultManager;

    public static HistoryManager getDefaultHistory(){
        return  new InMemoryHistoryManager();
    }

    public static HTTPTaskManager getDefault() {
        //Создать экземпляр если его ещё нет
        if (defaultManager == null) {
            defaultManager = new HTTPTaskManager("http://localhost:8078");
        }
        return defaultManager;
    }
}
