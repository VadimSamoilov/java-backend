package packagetask.managment;

public class Managers {

    public static InMemoryHistoryManager getDefaultHistory(){
        return  new InMemoryHistoryManager();
    }
}
