# CIT300 Graded Practical Assignment 1
## University Student Record and Campus Route Management System

**Module:** CIT300 Data Structures and Algorithms
**Contribution:** 10% of final module grade
**Deadline:** On or before 29 September (via designated LMS submission link)

## Group Members

| Name | Student ID | Assigned Responsibility |
|---|---|---|
| **MS Faathima Hasna** (Group Leader) | 23da2-1093 | BST implementation, Hashing/search implementation, integration of all modules into the main menu (`CampusSystem.java`), input validation, overall testing |
| MF Sheeraz Gulzum | 23da2-0589 | Linked list implementation and student-record management (`StudentLinkedList.java`) |
| R Halidha Nashath | 23da2-0535 | Stack (`ActionStack.java`) and Queue (`ServiceQueue.java`) implementation |
| M Hathiqu Ahamath | 23da2-0526 | Graph implementation: campus locations, connections, BFS/DFS traversal (`CampusGraph.java`) |

**All members:** integration testing, debugging, documentation, GitHub collaboration (branches, commits, pull requests), and individual contribution demonstrated in the merged video.

> Fill in each member's individual contribution notes here once everyone has committed their part, e.g. what they personally coded/tested and can explain in the demo video.

## Project Structure

```
CIT300-Campus-System/
├── Student.java            # Shared data model
├── StudentLinkedList.java  # Requirement 2 & 12 — Sheeraz
├── ActionStack.java        # Requirement 3        — Halidha
├── ServiceQueue.java       # Requirement 4         — Halidha
├── StudentBST.java         # Requirement 5         — Hasna
├── StudentHashTable.java   # Requirement 6         — Hasna
├── CampusGraph.java        # Requirements 7-11     — Hathiqu
├── CampusSystem.java       # Main menu / integration — Hasna
└── README.md
```

## How to Compile and Run

```bash
javac *.java
java CampusSystem
```

## Features Implemented

1. Student records (ID, Name, Programme, Marks) — linked list storage
2. Stack-based recent actions / undo history log
3. Queue-based service request handling (FIFO)
4. BST organizing/search of students by Student ID
5. Hash table (separate chaining) for O(1) average-case ID search
6. Graph (adjacency list) of campus locations and roads, with BFS and DFS traversal
7. 16-option menu-driven console interface with input validation (empty input, non-numeric input, out-of-range marks, duplicate IDs, missing records, unknown locations)

## Requirements Checklist (from the assignment brief)

- [x] All group member names/IDs recorded correctly
- [ ] Responsibilities and individual contributions documented (fill in after each member commits)
- [ ] GitHub repository with evidence of commits/branches/PRs from every member
- [ ] Demo video (< 15 minutes) with all members' faces visible, each explaining their part
- [ ] Google Drive folder set up (only if project files are too large for direct LMS upload) with **Editor access** given to:
  - asanka.r@sltc.ac.lk
  - kaushika.w@sltc.ac.lk
- [ ] Submitted via the designated LMS link before the deadline (29 September)
