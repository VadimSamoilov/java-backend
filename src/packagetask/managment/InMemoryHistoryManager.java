package packagetask.managment;

import packagetask.interfaсe.HistoryManager;
import packagetask.util.Node;
import packagetask.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int TASK_COUNT = 10;
    private MyLinkedList<Task> myLinkedList = new MyLinkedList<>();

    // Проверка есть ли данный Таск в связном списке, добавление в связный список.
    @Override
    public void add(Task task) {
            remove(task.getKeyId());
            myLinkedList.linkLast(task);
            if (myLinkedList.size > TASK_COUNT) { // проверяяем не превышает ли размер связного листа константу.
                myLinkedList.removeFirst();
            }
    }

    public void clear() {
        myLinkedList.clear();
    }

    @Override
    public void remove(int id) {
        Node node = myLinkedList.map.get(id);
        if (node != null) {
            myLinkedList.removeNode(node);
            myLinkedList.map.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        for (Node<Task> x = myLinkedList.head; x != null; x = x.getNext()) {
            history.add(x.getValueTask());
        }
        return history;
    }

    private static class MyLinkedList<T> {
        private Node<Task> head = null;
        private Node<Task> tail = null;
        private int size = 0;
        private final Map<Integer, Node<Task>> map = new HashMap<>();

        public Node<Task> linkLast(Task task) {
            final Node<Task> oldLast = tail;
            final Node<Task> newNode = new Node<Task>(oldLast, task, null);
            tail = newNode;
            if (oldLast == null)
                head = newNode;
            else
                oldLast.setNext(newNode);
            size++;
            if (size > TASK_COUNT) {
                removeFirst();
            }
            map.put(task.getKeyId(), newNode);
            return newNode;
        }


        private void removeNode(Node<Task> node) {
            final Task element = node.getValueTask();
            final Node<Task> next = node.getNext();
            final Node<Task> prev = node.getPrev();

            if (prev == null) {
                head = next;
            } else {
                prev.setNext(next);
                node.setPrev(null);
            }

            if (next == null) {
                tail = prev;
            } else {
                next.setPrev(prev);
                node.setNext(null);
            }
            node.setValueTask(null);
            size--;

        }

        private void clear() {
            map.clear();
        }

        private void removeFirst() {
            removeNode(head);
        }
    }
}
