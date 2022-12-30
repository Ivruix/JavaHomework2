package javahomework2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TopologicalSorter {
    private final Map<String, Boolean> visited = new HashMap<>();
    private final Map<String, Boolean> inRecursionStack = new HashMap<>();
    private final Map<String, List<String>> edges = new HashMap<>();
    private final List<String> sortedVertices = new ArrayList<>();

    public TopologicalSorter() {
    }

    public void AddVertex(String vertex) {
        visited.put(vertex, false);
        inRecursionStack.put(vertex, false);
    }

    public void AddEdge(String from, String to) {
        edges.putIfAbsent(from, new ArrayList<>());
        edges.get(from).add(to);
    }

    public List<String> CalculateTopologicalSort() {
        visited.replaceAll((key, value) -> false);
        inRecursionStack.replaceAll((key, value) -> false);
        sortedVertices.clear();

        for (String vertex : visited.keySet()) {
            if (!visited.get(vertex)) {
                TopologicalSort(vertex);
            }
        }

        return new ArrayList<>(sortedVertices);
    }

    private void TopologicalSort(String vertex) {
        inRecursionStack.put(vertex, true);
        visited.put(vertex, true);

        if (edges.containsKey(vertex)) {
            for (String nextVertex : edges.get(vertex)) {
                if (!visited.containsKey(nextVertex)) {
                    continue;
                }

                if (inRecursionStack.get(nextVertex)) {
                    throw new IllegalStateException("The given graph has a cycle.");
                }
                if (!visited.get(nextVertex)) {
                    TopologicalSort(nextVertex);
                }
            }
        }

        sortedVertices.add(vertex);
        inRecursionStack.put(vertex, false);
    }
}
