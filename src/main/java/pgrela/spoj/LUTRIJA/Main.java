package pgrela.spoj.LUTRIJA;

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
        String[] graphCharacteristics = this.inputStream.readLine().split(" ");
        int vertices = Integer.parseInt(graphCharacteristics[0]);
        int edges = Integer.parseInt(graphCharacteristics[1]);

        if (vertices <= 1) {
            outputStream.append("0");
            return;
        }

        List<Node> matrix = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            matrix.add(new Node(i));
        }

        for (int i = 0; i < edges; i++) {
            String[] edgeCharacteristics = this.inputStream.readLine().split(" ");
            int startNode = Integer.parseInt(edgeCharacteristics[0]) - 1;
            int endNode = Integer.parseInt(edgeCharacteristics[1]) - 1;
            matrix.get(startNode).addNeighbour(matrix.get(endNode));
            matrix.get(endNode).addNeighbour(matrix.get(startNode));
        }

        Queue<Node> queue = new PriorityQueue<>((a, b) -> a.countNeighbours() - b.countNeighbours());
        queue.addAll(matrix);

        while (queue.size() > 1) {
            Node node = queue.poll();
            if (node.countNeighbours() == 1) {
                Node affectedNode = node.fold();
                queue.remove(affectedNode);
                queue.add(affectedNode);
            }
            if (node.countNeighbours() == 2) {
                Node legacy = node.decreaseLoop();
                if (legacy != null) {
                    queue.add(legacy);
                }
            }
        }
        Map<Integer, Integer> paths = matrix.stream().filter(n -> n.getFinishedPaths().size() > 0).findAny().get().getFinishedPaths();

        StringJoiner stringJoiner = new StringJoiner(" ");
        for (int i = 1; i <= vertices; i++) {
            Integer value = paths.get(i);
            stringJoiner.add(String.valueOf(value == null ? 0 : value));
        }
        outputStream.append(stringJoiner.toString());

        outputStream.flush();
        outputStream.close();
    }

    private static class Node {
        private final static long MODULO = 1_000_000_000 + 7;

        Map<Integer, Integer> finishedPaths = new HashMap<>();
        Map<Integer, Integer> possibleToExtend = new HashMap<>();
        Map<Node, List<Node>> loops = new HashMap<>();
        private boolean dead = false;

        private int id;


        public Node(int id) {
            finishedPaths.put(1, 1);
            possibleToExtend.put(1, 1);
            this.id = id;
        }

        public void addNeighbour(Node newNeighbour) {
            loops.put(newNeighbour, new ArrayList<>());
        }

        public int countNeighbours() {
            return loops.size();
        }

        public Node fold() {
            assert countNeighbours() == 1;
            Map.Entry<Node, List<Node>> onlyNeighbour = getAndRemoveOnlyNeighbour();
            Node lastLink = this;
            for (Node link : onlyNeighbour.getValue()) {
                link.incorporateSingleLeaf(lastLink);
                lastLink = link;
            }
            onlyNeighbour.getKey().incorporateNeighbour(lastLink);
            return onlyNeighbour.getKey();
        }

        private void incorporateNeighbour(Node neighbour) {
            List<Node> pathToIncorporate = loops.remove(neighbour);
            Collections.reverse(pathToIncorporate);
            Node lastLink = neighbour;
            for (Node link : pathToIncorporate) {
                link.incorporateSingleLeaf(lastLink);
                lastLink = link;
            }
            incorporateSingleLeaf(lastLink);
        }

        private void incorporateSingleLeaf(Node leaf) {
            assert leaf.countNeighbours() == 0;
            addFinishedPaths(leaf.finishedPaths);
            leaf.finishedPaths.clear();

            combinePaths(leaf.possibleToExtend);
            extendPossibleToExtend(leaf.possibleToExtend);
            leaf.possibleToExtend.clear();
        }

        private void extendPossibleToExtend(Map<Integer, Integer> possibleToExtend) {
            extendPossibleToExtend(possibleToExtend, 1);
        }


        private void extendPossibleToExtend(Map<Integer, Integer> possibleToExtend, int distance) {
            for (Map.Entry<Integer, Integer> extendable : possibleToExtend.entrySet()) {
                if (extendable.getValue() > 0) {
                    combineMapValue(this.possibleToExtend, extendable.getKey() + distance, extendable.getValue());
                }
            }
        }

        private void combinePaths(Map<Integer, Integer> endingInOtherNode) {
            for (Map.Entry<Integer, Integer> thisEntry : possibleToExtend.entrySet()) {
                for (Map.Entry<Integer, Integer> otherEntry : endingInOtherNode.entrySet()) {
                    assert thisEntry.getValue() >= 0;
                    assert otherEntry.getValue() >= 0;
                    combineMapValue(finishedPaths, thisEntry.getKey() + otherEntry.getKey(), (int) ((thisEntry.getValue() * (long) otherEntry.getValue() * 2L) % MODULO));
                }
            }
        }


        private void combineMapValue(Map<Integer, Integer> theMap, Integer pathLength, Integer pathsCount) {
            if (theMap.containsKey(pathLength)) {
                int value = (int) ((theMap.get(pathLength) + (long) pathsCount + MODULO) % MODULO);
                assert value >= 0;
                theMap.put(pathLength, value);
            } else {
                theMap.put(pathLength, (int) ((pathsCount + MODULO) % MODULO));
            }
        }

        private void addFinishedPaths(Map<Integer, Integer> pathsToAdd) {
            for (Map.Entry<Integer, Integer> entry : pathsToAdd.entrySet()) {
                Integer pathLength = entry.getKey();
                Integer pathsCount = entry.getValue();
                if (pathsCount > 0) {
                    combineMapValue(finishedPaths, pathLength, pathsCount);
                }
            }
        }

        public Map.Entry<Node, List<Node>> getAndRemoveOnlyNeighbour() {
            assert countNeighbours() == 1;
            Map.Entry<Node, List<Node>> next = loops.entrySet().iterator().next();
            loops.clear();
            return next;
        }

        @Override
        public String toString() {
            return String.valueOf(loops.size()) +
                    " neighbours, " +
                    finishedPaths.size() +
                    " finishedPaths, " +
                    possibleToExtend.size() +
                    " ending here";
        }

        public Node decreaseLoop() {
            assert countNeighbours() == 2;

            Iterator<Node> iterator = loops.keySet().iterator();
            Node left = iterator.next();
            Node right = iterator.next();

            if (left.isDead() || right.isDead()) {
                if (left.isDead()) {
                    right.foldTricycle(this, left, loops.get(left));
                } else {
                    left.foldTricycle(this, right, loops.get(right));
                }
                loops.clear();
                if (left.isDead()) {
                    return right;
                } else {
                    return left;
                }
            }

            if (left.doYouKnow(right)) {
                dead = true;
                loops.clear();
                return null;
            }

            List<Node> pathToLeft = loops.remove(left);
            List<Node> pathToRight = loops.remove(right);

            left.update(this, right, pathToRight);
            right.update(this, left, pathToLeft);
            return null;
        }

        private void foldTricycle(Node first, Node last, List<Node> pathFromFirstToLast) {
            List<Node> path = loops.remove(first);
            path.add(first);
            path.addAll(pathFromFirstToLast);
            path.add(last);
            List<Node> pathToLast = loops.remove(last);
            Collections.reverse(pathToLast);
            path.addAll(pathToLast);

            foldSimpleCycle(path);

        }

        private boolean doYouKnow(Node possibleNeighbour) {
            return loops.containsKey(possibleNeighbour);
        }

        private void update(Node oldNeighbour, Node newNeighbour, List<Node> pathFromOldToNew) {
            List<Node> path = loops.remove(oldNeighbour);
            path.add(oldNeighbour);
            path.addAll(pathFromOldToNew);
            loops.put(newNeighbour, path);
        }

        private void foldSimpleCycle(List<Node> cycle) {
            assert cycle.size() >= 2;
            Map<Integer, Integer> extendablePathsAgainstDirection = getExtendablePathsAgainstDirection(cycle);
            Map<Integer, Integer> extendablePathsInDirection = getExtendablePathsInDirection(cycle);
            for (Node link : cycle) {
                removePaths(extendablePathsAgainstDirection, link.possibleToExtend, 0);
                extendablePathsAgainstDirection = changeLengthOfExtendablePaths(extendablePathsAgainstDirection, -1);
                removePaths(extendablePathsInDirection, link.possibleToExtend, cycle.size());

                link.combinePaths(extendablePathsAgainstDirection);
                link.combinePaths(extendablePathsInDirection);

                extendablePathsInDirection = changeLengthOfExtendablePaths(extendablePathsInDirection, 1);

                addFinishedPaths(link.finishedPaths);
                link.finishedPaths.clear();

            }

            int verticesPast = 0;
            for (Node link : cycle) {
                extendPossibleToExtend(link.possibleToExtend, verticesPast + 1);
                extendPossibleToExtend(link.possibleToExtend, cycle.size() - verticesPast);
                link.possibleToExtend.clear();

                ++verticesPast;
            }

        }

        private Map<Integer, Integer> changeLengthOfExtendablePaths(Map<Integer, Integer> paths, int distance) {
            Map<Integer, Integer> newPaths = new HashMap<>();
            for (Map.Entry<Integer, Integer> path : paths.entrySet()) {
                if (path.getValue() > 0) {
                    newPaths.put(path.getKey() + distance, path.getValue());
                }
            }

            return newPaths;
        }

        private void removePaths(Map<Integer, Integer> pathsAgainstDirection, Map<Integer, Integer> junk, int overDistance) {
            for (Map.Entry<Integer, Integer> toRemove : junk.entrySet()) {
                Integer lengthToRemove = toRemove.getKey() + overDistance;
                Integer countToRemove = toRemove.getValue();

                Integer currentCount = pathsAgainstDirection.get(lengthToRemove);
                if (currentCount == null) {
                    currentCount = 0;
                }
                Integer newCount = (int) ((currentCount - (long) countToRemove + MODULO) % MODULO);

                pathsAgainstDirection.put(lengthToRemove, newCount);
            }
        }

        private Map<Integer, Integer> getExtendablePathsAgainstDirection(List<Node> cycle) {
            Map<Integer, Integer> extendableCCW = new HashMap<>();
            for (Map.Entry<Integer, Integer> path : possibleToExtend.entrySet()) {
                if (path.getValue() > 0) {
                    extendableCCW.put(path.getKey() + cycle.size(), path.getValue());
                }
            }

            int verticesPast = 0;
            for (Node link : cycle) {
                for (Map.Entry<Integer, Integer> extendable : link.possibleToExtend.entrySet()) {
                    if (extendable.getValue() > 0) {
                        combineMapValue(extendableCCW, extendable.getKey() + verticesPast, extendable.getValue());
                    }
                }
                ++verticesPast;
            }
            return extendableCCW;
        }

        private Map<Integer, Integer> getExtendablePathsInDirection(List<Node> cycle) {
            int verticesPast;
            verticesPast = 0;
            final Map<Integer, Integer> extendableCW = new HashMap<>(possibleToExtend);
            for (Node link : cycle) {
                for (Map.Entry<Integer, Integer> extendable : link.possibleToExtend.entrySet()) {
                    if (extendable.getValue() > 0) {
                        combineMapValue(extendableCW, extendable.getKey() + cycle.size() - verticesPast, extendable.getValue());
                    }
                }
                ++verticesPast;
            }
            return extendableCW;
        }

        public boolean isDead() {
            return dead;
        }

        public Map<Integer, Integer> getFinishedPaths() {
            return finishedPaths;
        }
    }

}
