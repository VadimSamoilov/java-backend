package packagetask.util;


public class Node <Task>{
    public void setValueTask(Task valueTask) {
        this.valueTask = valueTask;
    }

    private Task valueTask;
    private Node next;
    private Node prev;


    public Task getValueTask() {
        return valueTask;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node(Node prev, Task task, Node next) {
        this.valueTask = task;
        this.next = next;
        this.prev = prev;
    }
}
