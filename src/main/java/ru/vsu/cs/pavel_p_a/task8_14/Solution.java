package ru.vsu.cs.pavel_p_a.task8_14;

import ru.vsu.cs.course1.graph.AdjMatrixGraph;
import ru.vsu.cs.course1.graph.Graph;
import ru.vsu.cs.util.ArrayUtils;

import java.util.*;

public class Solution {

    public static boolean areGraphsIsomeric(Graph graphA, Graph graphB) {
        if (graphA.vertexCount() != graphB.vertexCount()) {
            return false;
        }

        if (graphA.edgeCount() != graphB.edgeCount()) {
            return false;
        }

        boolean[][] adjMatrixA = getAdjMatrix(graphA);
        boolean[][] adjMatrixB = getAdjMatrix(graphB);;

        int n = graphB.vertexCount();
        for (Integer[] permutationRows : new Permutation(n)) {
            for (Integer[] permutationColumns : new Permutation(n)) {
                boolean[][] permutedAdjMatrix = new boolean[n][n];
                for (int r = 0; r < n; r++) {
                    int elementNewRowIndex = permutationRows[r];
                    for (int c = 0; c < n; c++) {
                        int elementNewColumnIndex = permutationColumns[c];
                        permutedAdjMatrix[elementNewRowIndex][elementNewColumnIndex] = adjMatrixB[r][c];
                    }
                }
                if (areAdjMatricesEqual(adjMatrixA, permutedAdjMatrix)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean[][] getAdjMatrix(Graph graph) {
        boolean[][] adjMatrix = new boolean[graph.vertexCount()][graph.vertexCount()];
        for (int vertex = 0; vertex < graph.vertexCount(); vertex++) {
            for (int adjVertex : graph.adjacencies(vertex)) {
                adjMatrix[vertex][adjVertex] = true;
            }
        }
        return adjMatrix;
    }

    private static int degree(Graph g, int v) {
        int d = 0;
        for (int a : g.adjacencies(v)) {
            d++;
        }
        return d;
    }

    private static boolean areAdjMatricesEqual(boolean[][] adjMatrixA, boolean[][] adjMatrixB) {
        if (adjMatrixA.length != adjMatrixB.length || adjMatrixA[0].length != adjMatrixB[0].length) {
            return false;
        }
        int n = adjMatrixA.length;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (adjMatrixA[r][c] != adjMatrixB[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }
}
