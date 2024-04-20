package Project2_135;


import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DataSet {
    private final SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);


    public void addData(String data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        graph.addVertex(data);
    }

    public void connectVertices() {
        List<String> vertices = new ArrayList<>(graph.vertexSet());
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                int cost = 0;
                String vertex1 = vertices.get(i);
                String vertex2 = vertices.get(j);
                int flag = isOneCharDiffOrPermutation(vertex1, vertex2);
                if (flag != -1) {
                    DefaultWeightedEdge edge = graph.addEdge(vertex1, vertex2);

                    if (flag == 1) {
                        cost = findCost(vertex1, vertex2);
                    }

                    if (edge != null) {
                        graph.setEdgeWeight(edge, cost);
                    }
                }
            }
        }
    }

    public int getEdgeWeight(DefaultWeightedEdge edge) {
        return (int) graph.getEdgeWeight(edge);
    }

    public GraphPath<String, DefaultWeightedEdge> findShortestPath(String startVertex, String targetVertex) {
        DijkstraShortestPath<String, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph);
        return path.getPath(startVertex, targetVertex);
    }

    public int findCost(String word1, String word2) {
        int i;
        for (i = 0; i < word1.length(); i++) {
            if (Character.toLowerCase(word1.charAt(i)) != Character.toLowerCase(word2.charAt(i))) {
                break;
            }
        }
        return Math.abs(Character.toLowerCase(word1.charAt(i)) - Character.toLowerCase(word2.charAt(i)));
    }

    private boolean isPermutation(String word1, String word2) {
        int[] count = new int[26];
        for (int i = 0; i < word1.length(); i++) {
            count[word1.charAt(i) - 'a']++;
            count[word2.charAt(i) - 'a']--;
        }

        for (int i = 0; i < 26; i++) {
            if (count[i] != 0) {
                return false;
            }
        }

        return true;
    }


    public int isOneCharDiffOrPermutation(String word1, String word2) {

        // Check permutation
        if (isPermutation(word1, word2)) {
            return 2;
        }

        // Check one char diff
        int diffCount = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                diffCount++;
            }
        }

        if (diffCount == 1) {
            return 1;
        }
        return -1;
    }

    public Set<String> regularExpression(String regex) {
        return graph.vertexSet().stream().filter(s -> s.toLowerCase().startsWith(regex.toLowerCase())).collect(Collectors.toSet());
    }
}


