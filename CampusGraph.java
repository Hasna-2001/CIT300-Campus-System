
import java.util.*;

public class CampusGraph {

    private Map<String, List<String>> adjList = new LinkedHashMap<>();

    public boolean addLocation(String name) {
        if (adjList.containsKey(name)) {
            System.out.println("Error: Location '" + name + "' already exists.");
            return false;
        }
        adjList.put(name, new ArrayList<>());
        return true;
    }

    public boolean removeLocation(String name) {
        if (!adjList.containsKey(name)) {
            System.out.println("Error: Location '" + name + "' not found.");
            return false;
        }
        adjList.remove(name);
        for (List<String> neighbours : adjList.values()) {
            neighbours.remove(name);
        }
        return true;
    }

    public boolean addConnection(String a, String b) {
        if (!adjList.containsKey(a) || !adjList.containsKey(b)) {
            System.out.println("Error: Both locations must exist before connecting them.");
            return false;
        }
        if (adjList.get(a).contains(b)) {
            System.out.println("Error: Connection already exists.");
            return false;
        }
        adjList.get(a).add(b);
        adjList.get(b).add(a); 
        return true;
    }

    public boolean removeConnection(String a, String b) {
        if (!adjList.containsKey(a) || !adjList.containsKey(b)) {
            System.out.println("Error: One or both locations not found.");
            return false;
        }
        boolean removed = adjList.get(a).remove(b);
        adjList.get(b).remove(a);
        if (!removed) System.out.println("Error: No such connection exists.");
        return removed;
    }

    public void displayNetwork() {
        if (adjList.isEmpty()) {
            System.out.println("No campus locations added yet.");
            return;
        }
        System.out.println("--- Campus Network (Adjacency List) ---");
        for (String loc : adjList.keySet()) {
            System.out.println(loc + " -> " + adjList.get(loc));
        }
    }

    public void bfs(String start) {
        if (!adjList.containsKey(start)) {
            System.out.println("Error: Start location '" + start + "' not found.");
            return;
        }
        Set<String> visited = new LinkedHashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);
        System.out.print("BFS from " + start + ": ");
        while (!queue.isEmpty()) {
            String curr = queue.poll();
            System.out.print(curr + " ");
            for (String neighbour : adjList.get(curr)) {
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    queue.add(neighbour);
                }
            }
        }
        System.out.println();
    }

    public void dfs(String start) {
        if (!adjList.containsKey(start)) {
            System.out.println("Error: Start location '" + start + "' not found.");
            return;
        }
        Set<String> visited = new LinkedHashSet<>();
        System.out.print("DFS from " + start + ": ");
        dfsRec(start, visited);
        System.out.println();
    }

    private void dfsRec(String curr, Set<String> visited) {
        visited.add(curr);
        System.out.print(curr + " ");
        for (String neighbour : adjList.get(curr)) {
            if (!visited.contains(neighbour)) dfsRec(neighbour, visited);
        }
    }

    public boolean hasLocation(String name) { return adjList.containsKey(name); }
}
