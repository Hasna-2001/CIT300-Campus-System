
public class ActionStack {

    private class Node {
        String action;
        Node next;
        Node(String action) { this.action = action; }
    }

    private Node top;
    private int size;
    private static final int DISPLAY_LIMIT = 20; // avoid unbounded growth on screen

    public void push(String action) {
        Node newNode = new Node(action);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public String pop() {
        if (isEmpty()) {
            System.out.println("No recent actions to undo.");
            return null;
        }
        String action = top.action;
        top = top.next;
        size--;
        return action;
    }

    public String peek() {
        return isEmpty() ? null : top.action;
    }

    public boolean isEmpty() { return top == null; }

    public void displayRecent() {
        if (isEmpty()) {
            System.out.println("No recent actions recorded.");
            return;
        }
        System.out.println("--- Recent Actions (most recent first) ---");
        Node curr = top;
        int count = 0;
        while (curr != null && count < DISPLAY_LIMIT) {
            System.out.println((count + 1) + ". " + curr.action);
            curr = curr.next;
            count++;
        }
    }

    public int getSize() { return size; }
}
