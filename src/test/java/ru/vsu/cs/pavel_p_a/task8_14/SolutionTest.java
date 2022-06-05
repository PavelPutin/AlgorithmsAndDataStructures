package ru.vsu.cs.pavel_p_a.task8_14;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import ru.vsu.cs.course1.graph.AdjListsDigraph;
import ru.vsu.cs.course1.graph.AdjListsGraph;
import ru.vsu.cs.course1.graph.Graph;

class SolutionTest {

    @Test
    void Test1() {
        Graph graphA = new AdjListsGraph();
        boolean[][] adjMatrixA = {
                {false, true, false, true, false, true},
                {true, false, true, false, true, false},
                {false, true, false, true, false, true},
                {true, false, true, false, true, false},
                {false, true, false, true, false, true},
                {true, false, true, false, true, false},
        };
        createGraphEdges(graphA, adjMatrixA);
        Graph graphB = new AdjListsGraph();
        boolean[][] adjMatrixB = {
                {false, false, false, true, true, true},
                {false, false, false, true, true, true},
                {false, false, false, true, true, true},
                {true, true, true, false, false, false},
                {true, true, true, false, false, false},
                {true, true, true, false, false, false},
        };
        createGraphEdges(graphB, adjMatrixB);
        assertTrue(Solution.areGraphsIsomeric(graphA, graphB));
    }

    @Test
    public void Test2() {
        Graph graphA = new AdjListsGraph();
        boolean[][] adjMatrixA = new boolean[][]{
                {false, true, false},
                {true, false, true},
                {false, false, false}
        };
        createGraphEdges(graphA, adjMatrixA);
        Graph graphB = new AdjListsGraph();
        boolean[][] adjMatrixB = new boolean[][]{
                {false, true, false},
                {true, false, true},
                {false, false, false}
        };
        createGraphEdges(graphB, adjMatrixB);
        assertTrue(Solution.areGraphsIsomeric(graphA, graphB));
    }

    @Test
    public void Test3() {
        Graph graphA = new AdjListsGraph();
        boolean[][] adjMatrixA = new boolean[][] {
                {false, false, false},
                {true, false, true},
                {false, false, false}
        };
        createGraphEdges(graphA, adjMatrixA);
        Graph graphB = new AdjListsGraph();
        boolean[][] adjMatrixB = new boolean[][] {
                {false, true, false},
                {true, false, true},
                {false, false, false}
        };
        createGraphEdges(graphB, adjMatrixB);
        assertTrue(Solution.areGraphsIsomeric(graphA, graphB));
    }

    @Test
    public void Test4() {
        Graph graphA = new AdjListsGraph();
        boolean[][] adjMatrixA = new boolean[][] {
                {false, false, false},
                {true, false, true},
        };
        createGraphEdges(graphA, adjMatrixA);
        Graph graphB = new AdjListsGraph();
        boolean[][] adjMatrixB = new boolean[][] {
                {false, true, false},
                {true, false, true},
                {false, false, false}
        };
        createGraphEdges(graphB, adjMatrixB);
        assertFalse(Solution.areGraphsIsomeric(graphA, graphB));
    }

    @Test
    public void Test5() {
        Graph graphA = new AdjListsGraph();
        boolean[][] adjMatrixA = new boolean[][] {
                {false, false, false},
                {true, false, true},
                {true, false, true},
        };
        createGraphEdges(graphA, adjMatrixA);
        Graph graphB = new AdjListsGraph();
        boolean[][] adjMatrixB = new boolean[][] {
                {false, true, false},
                {true, false, true},
                {false, false, false}
        };
        createGraphEdges(graphB, adjMatrixB);
        assertFalse(Solution.areGraphsIsomeric(graphA, graphB));
    }

    @Test
    public void Test6() {
        Graph graphA = new AdjListsDigraph();
        boolean[][] adjMatrixA = new boolean[][] {
                {false, false, false},
                {true, false, true},
                {true, false, true},
        };
        createGraphEdges(graphA, adjMatrixA);
        Graph graphB = new AdjListsDigraph();
        boolean[][] adjMatrixB = new boolean[][] {
                {false, true, false},
                {true, false, true},
                {false, false, false}
        };
        createGraphEdges(graphB, adjMatrixB);
        assertFalse(Solution.areGraphsIsomeric(graphA, graphB));
    }

    @Test
    public void Test7() {
        Graph graphA = new AdjListsDigraph();
        boolean[][] adjMatrixA = new boolean[][] {
                {false, true, true},
                {false, false, false},
                {true, false, true}
        };
        createGraphEdges(graphA, adjMatrixA);
        Graph graphB = new AdjListsDigraph();
        boolean[][] adjMatrixB = new boolean[][] {
                {false, false, false},
                {true, false, true},
                {false, true, true}
        };
        createGraphEdges(graphB, adjMatrixB);
        assertTrue(Solution.areGraphsIsomeric(graphA, graphB));
    }

    private static void createGraphEdges(Graph graph, boolean[][] adjMatrix) {
        int n = adjMatrix.length;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (adjMatrix[r][c]) {
                    graph.addAdge(r, c);
                }
            }
        }
    }
}