package ru.vsu.cs.pavel_p_a.task7;

import ru.vsu.cs.course1.graph.Graph;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Solution {
    private static class Edge{
        int a, b;

        Edge(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return (a == edge.a && b == edge.b) || (a == edge.b && b == edge.a);
        }

        @Override
        public int hashCode() {
            return Objects.hash(Math.min(a, b), Math.max(a, b));
        }
    }

    public static int findMaxQuantityOfPeopleEdge(Graph graph, int[] entrances, int[] exits) {
        Set<Edge> visitedEdges = new HashSet<>();
        final int[] maxResult = {0};

        int exitsDegrees = 0;
        for (int v : exits) {
            for (int a : graph.adjacencies(v)) {
                exitsDegrees++;
            }
        }
        int probablyMaxResult = Math.min(entrances.length, exitsDegrees);

        class Inner {
            void visit(int currentResult, int currRoom, int prevRoom, int currentSource) {
                Edge e = null;
                if (prevRoom != -1) {
                    e = new Edge(currRoom, prevRoom);
                    visitedEdges.add(e);
                }
                boolean isExit = isExit(currRoom, exits);

                if (isExit) {
                    if (currentResult + 1 > maxResult[0]) {
                        maxResult[0] = currentResult + 1;
                        if (maxResult[0] == probablyMaxResult) {
                            return;
                        }
                    }

                    for (int i = currentSource + 1; i < entrances.length; i++) {
                        visit(currentResult + 1, entrances[i], -1, i);
                    }
                } else {
                    for (int v : graph.adjacencies(currRoom)) {
                        Edge newEdge = new Edge(currRoom, v);
                        if (!visitedEdges.contains(newEdge)) {
                            visit(currentResult, v, currRoom, currentSource);
                            if (maxResult[0] == probablyMaxResult) {
                                return;
                            }
                        }
                    }
                }

                if (prevRoom != -1) {
                    visitedEdges.remove(e);
                }
            }
        }

        int startEntrance = 0;
        new Inner().visit(0, entrances[startEntrance], -1, startEntrance);
        return maxResult[0];
    }

    private static boolean isExit(int v, int[] exits) {
        for (int i : exits) {
            if (v == i) { return true; }
        }
        return false;
    }
}
