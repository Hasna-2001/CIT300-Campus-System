// ============================================================================
// CampusGraph.java
// Author : M Hathiqu Ahamath
// ID     : 23DA2-0526
// Module : CIT300 - Data Structures and Algorithms
// Owns   : Requirements 7-11 (campus locations, roads, BFS/DFS traversal)
// ============================================================================

/**
 * An UNDIRECTED campus road network stored as a HAND-ROLLED adjacency list.
 *
 * <p>Storage shape: a linked list of vertices, where every vertex also owns a
 * linked list of its neighbours. No java.util collections are used anywhere in
 * this class - the adjacency list, the neighbour lists and the BFS queue are
 * all built from the private node classes below.
 *
 * <pre>
 *   vertices list:  [Library] -> [Canteen] -> [Lab] -> null
 *   Library.neighbours:  EdgeNode(Main Hall) -> EdgeNode(Canteen) -> null
 * </pre>
 *
 * <p>INVARIANT (relied upon by BFS/DFS, do not break it):
 * every key stored in a neighbour list also exists as a vertex in the graph.
 * {@link #addLocation} establishes it, {@link #addConnection} checks it, and
 * {@link #removeLocation} restores it by scrubbing the key from every list.
 *
 * <p>Names are matched case-insensitively ("Library" == "LIBRARY") while the
 * display keeps the casing the user originally typed.
 *
 * <p>Complexity (V = vertices, E = edges):
 * <ul>
 *   <li>find vertex, add/remove location : O(V) - linear scan, no hashing</li>
 *   <li>add/remove connection           : O(degree) of the endpoint</li>
 *   <li>BFS and DFS                     : O(V + E) - each vertex and edge once</li>
 *   <li>space                            : O(V + E)</li>
 * </ul>
 */
public class CampusGraph {

    // =========================================================================
    // Private node types - the hand-rolled structures
    // =========================================================================

    /** One neighbour inside a single vertex's neighbour list. */
    private static class EdgeNode {
        final String key;          // normalised name of the neighbour
        EdgeNode next;
        EdgeNode(String key) { this.key = key; }
    }

    /** One campus location: display name, lookup key, and its neighbour list. */
    private static class VertexNode {
        final String name;         // display casing, exactly as entered
        final String key;          // normalised, used for all comparisons
        EdgeNode neighbours;       // head of this vertex's neighbour list
        VertexNode next;           // next vertex in the whole graph
        boolean visited;           // working flag for BFS/DFS
        VertexNode(String name, String key) {
            this.name = name;
            this.key = key;
        }
    }

    /** Node of the FIFO queue used by BFS. */
    private static class QNode {
        final VertexNode data;
        QNode next;
        QNode(VertexNode data) { this.data = data; }
    }

    /**
     * Minimal FIFO queue. A tail pointer is kept so enqueue is O(1) instead of
     * walking the whole queue on every insert. Setting BOTH front and rear
     * when the queue is empty is what keeps a one-element queue consistent.
     */
    private static class VertexQueue {
        private QNode front, rear;
        void enqueue(VertexNode v) {
            QNode n = new QNode(v);
            if (rear == null) {
                front = rear = n;
            } else {
                rear.next = n;
                rear = n;
            }
        }
        VertexNode dequeue() {
            if (front == null) return null;
            VertexNode v = front.data;
            front = front.next;
            if (front == null) rear = null;   // queue just became empty
            return v;
        }
        boolean isEmpty() { return front == null; }
    }

    // =========================================================================
    // Graph state
    // =========================================================================

    private VertexNode vertices;      // head of the list of all locations
    private int vertexCount;
    private int edgeCount;

    // =========================================================================
    // Internal helpers
    // =========================================================================

    /** Normalises a name so lookups ignore case and stray whitespace. */
    private static String key(String input) {
        return input.trim().toUpperCase();
    }

    /** Linear scan of the vertex list. Returns null when not present. */
    private VertexNode findVertex(String input) {
        String k = key(input);
        VertexNode curr = vertices;
        while (curr != null) {
            if (curr.key.equals(k)) return curr;
            curr = curr.next;
        }
        return null;
    }

    private boolean hasEdge(VertexNode v, String otherKey) {
        for (EdgeNode e = v.neighbours; e != null; e = e.next) {
            if (e.key.equals(otherKey)) return true;
        }
        return false;
    }

    /** Appends to the end so neighbour order matches insertion order. */
    private void addNeighbour(VertexNode v, String otherKey) {
        EdgeNode e = new EdgeNode(otherKey);
        if (v.neighbours == null) {
            v.neighbours = e;
            return;
        }
        EdgeNode curr = v.neighbours;
        while (curr.next != null) curr = curr.next;
        curr.next = e;
    }

    /** Unlinks one neighbour key. Returns true if something was removed. */
    private boolean removeNeighbour(VertexNode v, String otherKey) {
        EdgeNode prev = null, curr = v.neighbours;
        while (curr != null) {
            if (curr.key.equals(otherKey)) {
                if (prev == null) v.neighbours = curr.next;
                else prev.next = curr.next;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    /** Clears the traversal flags so the next BFS/DFS starts fresh. */
    private void clearVisited() {
        for (VertexNode v = vertices; v != null; v = v.next) v.visited = false;
    }

    /** Renders one vertex's neighbour list as [A, B, C]. */
    private String renderNeighbours(VertexNode v) {
        if (v.neighbours == null) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (EdgeNode e = v.neighbours; e != null; e = e.next) {
            VertexNode nb = findVertex(e.key);
            sb.append(nb == null ? e.key : nb.name);   // null is defensive only
            if (e.next != null) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    // =========================================================================
    // Public API - signatures must stay compatible with CampusSystem.java
    // =========================================================================

    /** Adds a campus location. Rejects duplicates, ignoring case. */
    public boolean addLocation(String name) {
        if (findVertex(name) != null) {
            System.out.println("Error: Location '" + name.trim() + "' already exists.");
            return false;
        }
        VertexNode v = new VertexNode(name.trim(), key(name));
        if (vertices == null) {
            vertices = v;                              // graph was empty
        } else {
            VertexNode curr = vertices;                // append: keep insertion order
            while (curr.next != null) curr = curr.next;
            curr.next = v;
        }
        vertexCount++;
        return true;
    }

    /**
     * Removes a location AND scrubs its key from every other vertex's
     * neighbour list. Doing only the first half would leave dangling
     * references and break the invariant that BFS/DFS depend on.
     */
    public boolean removeLocation(String name) {
        VertexNode target = findVertex(name);
        if (target == null) {
            System.out.println("Error: Location '" + name.trim() + "' not found.");
            return false;
        }
        // 1. unlink the vertex itself
        VertexNode prev = null, curr = vertices;
        while (curr != null && curr != target) {
            prev = curr;
            curr = curr.next;
        }
        if (prev == null) vertices = target.next;
        else prev.next = target.next;

        // 2. remove the dangling reference from every remaining vertex
        for (VertexNode v = vertices; v != null; v = v.next) {
            if (removeNeighbour(v, target.key)) edgeCount--;
        }
        vertexCount--;
        return true;
    }

    /**
     * Adds an undirected road: the edge is recorded in BOTH endpoints'
     * neighbour lists, because a campus road can be walked either way.
     */
    public boolean addConnection(String a, String b) {
        String ka = key(a), kb = key(b);
        if (ka.equals(kb)) {
            // Without this guard a and b are the same object, so both
            // addNeighbour calls below hit one list and duplicate the entry.
            System.out.println("Error: A location cannot be connected to itself.");
            return false;
        }
        VertexNode va = findVertex(a);
        if (va == null) {
            System.out.println("Error: Location '" + a.trim() + "' does not exist.");
            return false;
        }
        VertexNode vb = findVertex(b);
        if (vb == null) {
            System.out.println("Error: Location '" + b.trim() + "' does not exist.");
            return false;
        }
        if (hasEdge(va, kb)) {
            System.out.println("Error: Connection already exists between '"
                    + va.name + "' and '" + vb.name + "'.");
            return false;
        }
        addNeighbour(va, kb);
        addNeighbour(vb, ka);
        edgeCount++;
        return true;
    }

    /** Removes an undirected road from both endpoints. */
    public boolean removeConnection(String a, String b) {
        VertexNode va = findVertex(a);
        if (va == null) {
            System.out.println("Error: Location '" + a.trim() + "' does not exist.");
            return false;
        }
        VertexNode vb = findVertex(b);
        if (vb == null) {
            System.out.println("Error: Location '" + b.trim() + "' does not exist.");
            return false;
        }
        boolean removed = removeNeighbour(va, key(b));
        removeNeighbour(vb, key(a));          // keep both sides consistent
        if (!removed) {
            System.out.println("Error: No connection exists between '"
                    + va.name + "' and '" + vb.name + "'.");
            return false;
        }
        edgeCount--;
        return true;
    }

    /** Case-insensitive membership test. */
    public boolean hasLocation(String name) {
        return findVertex(name) != null;
    }

    /** Prints the adjacency list plus vertex and edge counts. */
    public void displayNetwork() {
        if (vertices == null) {
            System.out.println("No campus locations added yet.");
            return;
        }
        System.out.println("--- Campus Network (Adjacency List) ---");
        for (VertexNode v = vertices; v != null; v = v.next) {
            System.out.printf("%-16s -> %s%n", v.name, renderNeighbours(v));
        }
        System.out.println("Total locations: " + vertexCount
                + " | Total connections: " + edgeCount);
    }

    /**
     * Breadth-first traversal: visits everything 1 hop away, then 2 hops, and
     * so on. Uses a FIFO queue because the earliest-discovered vertex must be
     * processed earliest.
     */
    public void bfs(String start) {
        VertexNode s = findVertex(start);
        if (s == null) {
            System.out.println("Error: Start location '" + start.trim() + "' not found.");
            return;
        }
        VertexQueue queue = new VertexQueue();
        s.visited = true;                 // mark on ENQUEUE, never on dequeue
        queue.enqueue(s);

        System.out.print("BFS from " + s.name + ": ");
        while (!queue.isEmpty()) {
            VertexNode curr = queue.dequeue();
            System.out.print(curr.name + "  ");
            for (EdgeNode e = curr.neighbours; e != null; e = e.next) {
                VertexNode nb = findVertex(e.key);
                if (nb != null && !nb.visited) {
                    nb.visited = true;    // prevents a vertex being queued twice
                    queue.enqueue(nb);
                }
            }
        }
        System.out.println();
        clearVisited();
    }

    /**
     * Depth-first traversal: follows one path as deep as possible, then
     * backtracks. No queue is needed - the recursion call stack is the stack.
     */
    public void dfs(String start) {
        VertexNode s = findVertex(start);
        if (s == null) {
            System.out.println("Error: Start location '" + start.trim() + "' not found.");
            return;
        }
        System.out.print("DFS from " + s.name + ": ");
        dfsRec(s);
        System.out.println();
        clearVisited();
    }

    private void dfsRec(VertexNode v) {
        v.visited = true;                 // mark on entry, before recursing
        System.out.print(v.name + "  ");
        for (EdgeNode e = v.neighbours; e != null; e = e.next) {
            VertexNode nb = findVertex(e.key);
            if (nb != null && !nb.visited) {
                dfsRec(nb);              // one level deeper
            }
        }
        // returning from here is the "backtrack" step
    }

    // ---- read-only accessors (additive; safe for the existing menu) ----

    public int getVertexCount() { return vertexCount; }

    public int getEdgeCount() { return edgeCount; }

    /** Number of roads touching a location; -1 when the location is unknown. */
    public int degree(String name) {
        VertexNode v = findVertex(name);
        if (v == null) return -1;
        int d = 0;
        for (EdgeNode e = v.neighbours; e != null; e = e.next) d++;
        return d;
    }
}
