import java.util.Scanner;

public class CampusSystem {

    private static StudentLinkedList studentList = new StudentLinkedList();
    private static ActionStack actionStack = new ActionStack();
    private static ServiceQueue serviceQueue = new ServiceQueue();
    private static StudentBST studentBST = new StudentBST();
    private static StudentHashTable studentHash = new StudentHashTable();
    private static CampusGraph campusGraph = new CampusGraph();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            printMenu();
            choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> updateStudent();
                case 3 -> deleteStudent();
                case 4 -> studentList.displayAll();
                case 5 -> addServiceRequest();
                case 6 -> processServiceRequest();
                case 7 -> actionStack.displayRecent();
                case 8 -> studentBST.displayInOrder();
                case 9 -> searchByHashing();
                case 10 -> addLocation();
                case 11 -> removeLocation();
                case 12 -> addConnection();
                case 13 -> removeConnection();
                case 14 -> campusGraph.displayNetwork();
                case 15 -> traverseCampus();
                case 16 -> System.out.println("Exiting. Goodbye!");
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 16.");
            }
            System.out.println();
        } while (choice != 16);
        sc.close();
    }

    private static void printMenu() {
        System.out.println("===== University Student Record & Campus Route Management System =====");
        System.out.println(" 1. Add Student Record");
        System.out.println(" 2. Update Student Record");
        System.out.println(" 3. Delete Student Record");
        System.out.println(" 4. Display All Records (Linked List)");
        System.out.println(" 5. Add Service Request to Queue");
        System.out.println(" 6. Process Next Service Request");
        System.out.println(" 7. Display Recent Actions (Stack)");
        System.out.println(" 8. Display Students using BST");
        System.out.println(" 9. Search Student using Hashing");
        System.out.println("10. Add Campus Location");
        System.out.println("11. Remove Campus Location");
        System.out.println("12. Add Campus Connection/Road");
        System.out.println("13. Remove Campus Connection/Road");
        System.out.println("14. Display Campus Connections");
        System.out.println("15. Traverse Campus Locations (BFS/DFS)");
        System.out.println("16. Exit");
    }

    private static void addStudent() {
        String id = readNonEmpty("Student ID: ");
        String name = readNonEmpty("Name: ");
        String programme = readNonEmpty("Programme: ");
        double marks = readMarks("Marks (0-100): ");

        Student s = new Student(id, name, programme, marks);
        if (studentList.add(s)) {
            studentBST.insert(s);
            studentHash.put(s);
            actionStack.push("ADD student " + id);
            System.out.println("Student added successfully.");
        }
    }

    private static void updateStudent() {
        String id = readNonEmpty("Student ID to update: ");
        Student existing = studentList.search(id);
        if (existing == null) {
            System.out.println("Error: Student ID '" + id + "' not found.");
            return;
        }
        String name = readNonEmpty("New Name: ");
        String programme = readNonEmpty("New Programme: ");
        double marks = readMarks("New Marks (0-100): ");

        if (studentList.update(id, name, programme, marks)) {
            studentHash.put(studentList.search(id)); // refresh hash entry
            actionStack.push("UPDATE student " + id);
            System.out.println("Student updated successfully.");
        }
    }

    private static void deleteStudent() {
        String id = readNonEmpty("Student ID to delete: ");
        Student removed = studentList.delete(id);
        if (removed != null) {
            studentBST.delete(id);
            studentHash.remove(id);
            actionStack.push("DELETE student " + id + " (" + removed.getName() + ")");
            System.out.println("Student deleted successfully.");
        }
    }

    private static void searchByHashing() {
        String id = readNonEmpty("Student ID to search: ");
        Student s = studentHash.get(id);
        if (s == null) System.out.println("No student found with ID '" + id + "'.");
        else System.out.println("Found: " + s);
    }


    private static void addServiceRequest() {
        String req = readNonEmpty("Describe the service request: ");
        serviceQueue.enqueue(req);
        actionStack.push("ENQUEUE service request: " + req);
        System.out.println("Request added to queue.");
    }

    private static void processServiceRequest() {
        String req = serviceQueue.dequeue();
        if (req != null) {
            actionStack.push("PROCESSED service request: " + req);
            System.out.println("Processed: " + req);
        }
    }

    private static void addLocation() {
        String name = readNonEmpty("Location name: ");
        if (campusGraph.addLocation(name)) {
            actionStack.push("ADD location " + name);
            System.out.println("Location added.");
        }
    }

    private static void removeLocation() {
        String name = readNonEmpty("Location name to remove: ");
        if (campusGraph.removeLocation(name)) {
            actionStack.push("REMOVE location " + name);
            System.out.println("Location removed.");
        }
    }

    private static void addConnection() {
        String a = readNonEmpty("First location: ");
        String b = readNonEmpty("Second location: ");
        if (campusGraph.addConnection(a, b)) {
            actionStack.push("ADD connection " + a + " <-> " + b);
            System.out.println("Connection added.");
        }
    }

    private static void removeConnection() {
        String a = readNonEmpty("First location: ");
        String b = readNonEmpty("Second location: ");
        if (campusGraph.removeConnection(a, b)) {
            actionStack.push("REMOVE connection " + a + " <-> " + b);
            System.out.println("Connection removed.");
        }
    }

    private static void traverseCampus() {
        String start = readNonEmpty("Start location: ");
        if (!campusGraph.hasLocation(start)) {
            System.out.println("Error: Location '" + start + "' not found.");
            return;
        }
        String mode = readNonEmpty("Traversal type - enter BFS or DFS: ");
        if (mode.equalsIgnoreCase("BFS")) campusGraph.bfs(start);
        else if (mode.equalsIgnoreCase("DFS")) campusGraph.dfs(start);
        else System.out.println("Error: Please enter BFS or DFS.");
    }

    private static String readNonEmpty(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (input.isEmpty()) System.out.println("Input cannot be empty. Try again.");
        } while (input.isEmpty());
        return input;
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static double readMarks(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                double marks = Double.parseDouble(input);
                // NaN must be rejected explicitly because every comparison with NaN is false
                if (Double.isNaN(marks) || marks < 0 || marks > 100) {
                    System.out.println("Marks must be between 0 and 100.");
                    continue;
                }
                return marks;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}