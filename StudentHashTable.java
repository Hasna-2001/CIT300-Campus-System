
import java.util.LinkedList;

public class StudentHashTable {

    private static final int CAPACITY = 32;
    private LinkedList<Student>[] buckets;

    @SuppressWarnings("unchecked")
    public StudentHashTable() {
        buckets = new LinkedList[CAPACITY];
        for (int i = 0; i < CAPACITY; i++) buckets[i] = new LinkedList<>();
    }

    private int hash(String id) {
        return Math.abs(id.hashCode()) % CAPACITY;
    }

    public void put(Student s) {
        int index = hash(s.getStudentId());
        // remove any stale entry with the same ID first (e.g. after an update)
        buckets[index].removeIf(st -> st.getStudentId().equals(s.getStudentId()));
        buckets[index].add(s);
    }

    public Student get(String id) {
        int index = hash(id);
        for (Student s : buckets[index]) {
            if (s.getStudentId().equals(id)) return s;
        }
        return null;
    }

    public boolean remove(String id) {
        int index = hash(id);
        return buckets[index].removeIf(s -> s.getStudentId().equals(id));
    }

    public void displayBucketInfo() {
        System.out.println("--- Hash Table Bucket Usage ---");
        for (int i = 0; i < CAPACITY; i++) {
            if (!buckets[i].isEmpty()) {
                System.out.println("Bucket " + i + ": " + buckets[i].size() + " record(s)");
            }
        }
    }
}
