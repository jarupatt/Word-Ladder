package Project2_135;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DataSet set = new DataSet();
        String userChoice = "";
        boolean quit = false;
        String path = "src/main/java/Project2_135/words/";
        boolean fileFound = false;
        while (!fileFound) {
            try {
                System.out.print("Enter word file = ");
                String filename = scanner.nextLine();
                Scanner fileScanner = new Scanner(new File(path + filename));
                storeData(fileScanner, set);
                fileFound = true;
                fileScanner.close();
            } catch (Exception e) {
            }
        }

        while (!quit) {
            System.out.println("\nEnter menu >> (S = search, L = ladder, Q = quit)");
            userChoice = scanner.nextLine();
            switch (userChoice.toLowerCase()) {
                case "q":
                    quit = true;
                    break;
                case "s":
                    Search(set);
                    break;
                case "l":
                    Ladder(set);
                    break;
                default:
            }
        }
        System.out.println();
        scanner.close();
    }

    public static void storeData(Scanner fileScanner, DataSet set) {
        // Store data
        while (fileScanner.hasNextLine()) {
            String col = fileScanner.nextLine().trim();
            set.addData(col);
        }
        set.connectVertices();
    }

    public static void Search(DataSet set) {
        System.out.println("\nSearch = ");
        Scanner scanner = new Scanner(System.in);
        String regex = scanner.nextLine();
        Set<String> filteredSet = set.regularExpression(regex);
        System.out.println("=== Available words ===");
        int counter = 0;
        List<String> list = new ArrayList<>(filteredSet);
        Collections.sort(list);
        for (String each : list) {
            System.out.print(each + "       ");
            counter++;
            if (counter == 10) {
                System.out.println();
                counter = 0;
            }
        }
        System.out.println();
    }

    public static void Ladder(DataSet set) {
        int totalCost = 0;
        int cost = 0;
        Scanner scanner = new Scanner(System.in);
        String word1 = "";
        String word2 = "";

        // Receive input from user
        while (word1.length() != 5) {
            System.out.println("Enter 5 letter word 1 = ");
            word1 = scanner.nextLine().toLowerCase();
        }

        // Receive input from user
        while (word2.length() != 5) {
            System.out.println("Enter 5 letter word 2 = ");
            word2 = scanner.nextLine().toLowerCase();
        }

        System.out.println();
        System.out.println(word1);

        // Check existing word in DataSet
        if (!(set.regularExpression(word1).size() == 1 || set.regularExpression(word2).size() == 1)) {
            System.out.printf("Cannot transform %s into %s\n", word1, word2);
            return;
        }

        // Get shortest path
        GraphPath<String, DefaultWeightedEdge> graph = set.findShortestPath(word1, word2);

        // Verify path
        if (graph.getVertexList().size() < 2) {
            System.out.printf("Cannot transform %s into %s\n", word1, word2);
            return;
        }

        // Get Vertices and edge
        List<String> vertices = graph.getVertexList();
        List<DefaultWeightedEdge> edge = graph.getEdgeList();

        // Loop print vertices and weight
        for (int i = 0; i < vertices.size() - 1; i++) {
            cost = set.getEdgeWeight(edge.get(i));
            if (cost > 0) {
                System.out.printf("%s (ladder   + %d)\n", vertices.get(i), cost);
            } else if (cost == 0) {
                System.out.printf("%s (elevator + %d)\n", vertices.get(i), cost);
            }
            totalCost += cost;
        }

        System.out.printf("\nTransformation cost = %d\n", totalCost);


    }
}