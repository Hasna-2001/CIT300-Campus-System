
public class ServiceQueue {

    private class Node {
        String request;
        Node next;
        Node(String request) { this.request = request; }
    }

    private Node front, rear;
    private int size;

    public void enqueue(String request) {
        Node newNode = new Node(request);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public String dequeue() {
        if (isEmpty()) {
            System.out.println("No pending service requests.");
            return null;
        }
        String request = front.request;
        front = front.next;
        if (front == null) rear = null;
        size--;
        return request;
    }

    public boolean isEmpty() { return front == null; }

    public void displayAll() {
        if (isEmpty()) {
            System.out.println("No pending service requests.");
            return;
        }
        System.out.println("--- Pending Service Requests (arrival order) ---");
        Node curr = front;
        int pos = 1;
        while (curr != null) {
            System.out.println(pos + ". " + curr.request);
            curr = curr.next;
            pos++;
        }
    }

    public int getSize() { return size; }
}
