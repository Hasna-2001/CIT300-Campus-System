
public class StudentLinkedList {

    private class Node {
        Student data;
        Node next;
        Node(Student data) { this.data = data; }
    }

    private Node head;
    private int size;

    public boolean add(Student s) {
        if (findNode(s.getStudentId()) != null) {
            System.out.println("Error: Duplicate Student ID '" + s.getStudentId() + "'. Record not added.");
            return false;
        }
        Node newNode = new Node(s);
        if (head == null) {
            head = newNode;
        } else {
            Node curr = head;
            while (curr.next != null) curr = curr.next;
            curr.next = newNode;
        }
        size++;
        return true;
    }

    public boolean update(String id, String name, String programme, double marks) {
        Node node = findNode(id);
        if (node == null) {
            System.out.println("Error: Student ID '" + id + "' not found.");
            return false;
        }
        node.data.setName(name);
        node.data.setProgramme(programme);
        node.data.setMarks(marks);
        return true;
    }

    public Student delete(String id) {
        Node curr = head, prev = null;
        while (curr != null) {
            if (curr.data.getStudentId().equals(id)) {
                if (prev == null) head = curr.next;
                else prev.next = curr.next;
                size--;
                return curr.data;
            }
            prev = curr;
            curr = curr.next;
        }
        System.out.println("Error: Student ID '" + id + "' not found.");
        return null;
    }

    public Student search(String id) {
        Node node = findNode(id);
        return node == null ? null : node.data;
    }

    private Node findNode(String id) {
        Node curr = head;
        while (curr != null) {
            if (curr.data.getStudentId().equals(id)) return curr;
            curr = curr.next;
        }
        return null;
    }

    public void displayAll() {
        if (head == null) {
            System.out.println("No student records found.");
            return;
        }
        System.out.println("--- All Student Records (Linked List) ---");
        Node curr = head;
        while (curr != null) {
            System.out.println(curr.data);
            curr = curr.next;
        }
        System.out.println("Total records: " + size);
    }

    public int getSize() { return size; }
}
