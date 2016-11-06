package pgrela.spoj.TRIP;

import java.io.*;
import java.util.*;

public class Main {
    protected final BufferedReader inputStream;
    protected final PrintStream outputStream;

    public static void main(String[] args) throws Exception {
        new Main(new InputStreamReader(System.in), new BufferedOutputStream(System.out)).solve();
    }

    public Main(Reader inputStreamReader, OutputStream out) throws IOException {
        inputStream = new BufferedReader(inputStreamReader);
        outputStream = new PrintStream(out);
    }

    protected void solve() throws IOException {
        int testCases = Integer.parseInt(inputStream.readLine());

        for (int i = 0; i < testCases; i++) {
            char[] stringA = inputStream.readLine().toCharArray();
            char[] stringB = inputStream.readLine().toCharArray();

            solve(stringA, stringB);

            outputStream.println();
        }
        outputStream.flush();
        outputStream.close();
    }

    protected void solve(char[] stringA, char[] stringB) {
        int[][] lcs = createLCSArray(stringA, stringB);
        SortedSet<String> sortedAnswers = generateSolutions(stringA, stringB, lcs);
        printSolutions(sortedAnswers);
    }

    private void printSolutions(SortedSet<String> sortedAnswers) {
        sortedAnswers.forEach(outputStream::println);
    }

    private SortedSet<String> generateSolutions(char[] stringA, char[] stringB, int[][] lcs) {
        int cities = lcs[stringA.length][stringB.length];
        Node answer = new Node();
        List<List<Set<Node>>> visited = createVisitedStructure(stringA, stringB);
        backtrack(lcs, stringA, stringA.length, stringB, stringB.length, answer, cities, visited);

        SortedSet<String> sortedAnswers = new TreeSet<>();
        answer.generateSolutions(new char[cities], cities, sortedAnswers);
        return sortedAnswers;
    }

    private List<List<Set<Node>>> createVisitedStructure(char[] stringA, char[] stringB) {
        List<List<Set<Node>>> visited = new ArrayList<>();
        for (int i = 0; i <= stringA.length; i++) {
            ArrayList<Set<Node>> listB = new ArrayList<>();
            for (int j = 0; j <= stringB.length; j++) {
                listB.add(new HashSet<>());
            }
            visited.add(listB);
        }
        return visited;
    }

    private int[][] createLCSArray(char[] stringA, char[] stringB) {
        int[][] lcs = new int[stringA.length + 1][stringB.length + 1];
        for (int i = 0; i < stringA.length; ++i) {
            for (int j = 0; j < stringB.length; j++) {
                lcs[i + 1][j + 1] = stringA[i] == stringB[j] ? lcs[i][j] + 1 : Math.max(lcs[i + 1][j], lcs[i][j + 1]);
            }
        }
        return lcs;
    }

    private void backtrack(int[][] lcs, char[] stringA, int a, char[] stringB, int b, Node answer, int citiesLeft, List<List<Set<Node>>> visited) {
        if (citiesLeft == 0) return;
        if (a <= 0 || b <= 0) return;
        if (lcs[a][b] == -1) return;
        if (visited.get(a).get(b).contains(answer)) return;

        visited.get(a).get(b).add(answer);
        int currentLcs = lcs[a][b];

        if (stringA[a - 1] == stringB[b - 1]) {
            Node furtherNode = answer.append(stringA[a - 1]);
            backtrack(lcs, stringA, a - 1, stringB, b - 1, furtherNode, citiesLeft - 1, visited);
        }
        if (currentLcs == lcs[a - 1][b]) {
            backtrack(lcs, stringA, a - 1, stringB, b, answer, citiesLeft, visited);
        }
        if (currentLcs == lcs[a][b - 1]) {
            backtrack(lcs, stringA, a, stringB, b - 1, answer, citiesLeft, visited);
        }
    }

    private static class Node {
        public static final int A_INT = (int) 'a';

        private Node[] nodes = null;

        public Node append(char c) {
            int index = ((int) c) - A_INT;
            if (nodes == null) {
                nodes = new Node[26];
            }
            if (nodes[index] == null) {
                nodes[index] = new Node();
            }
            return nodes[index];
        }

        public void generateSolutions(char[] chars, int cities, SortedSet<String> sortedAnswers) {
            --cities;
            if (nodes != null) {
                for (int i = 0; i < 26; i++) {
                    if (nodes[i] != null) {
                        chars[cities] = (char) (i + A_INT);
                        if (cities == 0) {
                            sortedAnswers.add(String.valueOf(chars));
                        } else {
                            nodes[i].generateSolutions(chars, cities, sortedAnswers);
                        }
                    }
                }
            }
        }
    }

}
