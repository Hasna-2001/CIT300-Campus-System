// Standalone demo/test driver for StudentLinkedList.java
// This is NOT part of the group submission - it's just for testing/showing your part.
public class TestStudentLinkedList {
    public static void main(String[] args) {
        StudentLinkedList list = new StudentLinkedList();

        System.out.println("=== Adding students ===");
        list.add(new Student("S001", "Ahmed Zaid", "Computer Science", 78.5));
        list.add(new Student("S002", "Fathima Nisha", "Information Technology", 85.0));
        list.add(new Student("S003", "Kamal Perera", "Software Engineering", 62.3));

        System.out.println("\n=== Trying to add a duplicate ID ===");
        list.add(new Student("S001", "Duplicate Test", "Data Science", 90.0));

        System.out.println("\n=== Display all records ===");
        list.displayAll();

        System.out.println("\n=== Search for S002 ===");
        Student found = list.search("S002");
        System.out.println(found != null ? found : "Not found");

        System.out.println("\n=== Update S003 ===");
        list.update("S003", "Kamal J. Perera", "Software Engineering", 71.0);
        System.out.println(list.search("S003"));

        System.out.println("\n=== Delete S001 ===");
        Student deleted = list.delete("S001");
        System.out.println("Deleted: " + deleted);

        System.out.println("\n=== Final list ===");
        list.displayAll();

        System.out.println("\n=== Searching a non-existent ID ===");
        list.search("S999");
    }
}
