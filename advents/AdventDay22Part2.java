package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Sean Deneen on 12/22/16.
 * Find the shortest path to move data from the top right node to top left.
 */
public class AdventDay22Part2 extends AdventDay22Part1 {

    private List<Node> nodes;
    private int xLength;
    private int yLength;

    private Set<String> prevStates;

    @Override
    public String compute(BufferedReader input) {
        nodes = createNodeList(input);
        xLength = nodes.get(nodes.size() - 1).x + 1;
        yLength = nodes.get(nodes.size() - 1).y + 1;
        prevStates = new HashSet<>();

        BitSet unviableNodes = new BitSet(nodes.size());
        getViablePairs(nodes).forEach(path -> {
                    unviableNodes.set(path[0].x * yLength + path[0].y);
                    unviableNodes.set(path[1].x * yLength + path[1].y);
                });
        for (int i = unviableNodes.nextClearBit(0); i < nodes.size();
             i = unviableNodes.nextClearBit(i + 1))
            nodes.set(i, null);

        PriorityQueue<NodePath> searches = new PriorityQueue<>();
        getViablePairs(nodes)
                .filter(this::areAdjacent)
                .filter(coords -> coords[1].used == 0)
                .map(NodePath::new)
                .forEach(searches::offer);

        prevStates.add(printGrid(nodes, getFromGrid(nodes, xLength - 1, 0), nodes.get(0)));

        NodePath lastPath = searches.poll();
        while(!lastPath.reachedDest()) {
            searches.addAll(nextPath(lastPath));
            lastPath = searches.poll();
        }

        return "The least number of steps are " + lastPath.step;
    }

    private boolean areAdjacent(Node[] path) {
        return Math.abs(path[0].x - path[1].x) + Math.abs(path[0].y - path[1].y) == 1;
    }

    private boolean inBounds(int[] coordinates) {
        return (coordinates[0] >= 0 && coordinates[1] >= 0
                && coordinates[0] < xLength && coordinates[1] < yLength);
    }

    private Node getFromGrid(List<Node> grid, int x, int y) {
        return grid.get(x * yLength + y);
    }

    private void setToGrid(List<Node> grid, int x, int y, Node value) {
        grid.set(x * yLength + y, value);
    }

    private List<NodePath> nextPath(NodePath lastPath) {
        int step = lastPath.step + 1;
        List<Node> gridState = lastPath.gridState;

        Node dstNode = lastPath.dstNode;
        Node srcNode = lastPath.srcNode;

        return gridState.stream()
                .filter(Objects::nonNull)
                .filter(node -> node.used == 0)
                .peek(node -> { // Build a wall below an empty node to speed up search.
                    if (node.y >= 1) {
                        for (int i = 0; i < xLength; i++)
                            setToGrid(gridState, i, node.y + 1, null);
                    }
                })
                .flatMap(b -> IntStream.range(0, 4)
                        .mapToObj(i -> new int[] {b.x + ((i - 2) % 2), b.y + ((i - 1) % 2)})
                        .filter(this::inBounds)
                        .map(coords -> getFromGrid(gridState, coords[0], coords[1]))
                        .filter(Objects::nonNull)
                        .filter(a -> a.canFitIn(b))
                        .map(a -> new NodePath(a.x, a.y, b.x, b.y, step, srcNode, dstNode, gridState)))
                .filter(nodePath -> prevStates.add(printGrid(nodePath.gridState, nodePath.srcNode, nodePath.dstNode)))
                .collect(Collectors.toList());
    }

    private String printGrid(List<Node> grid, Node srcNode, Node dstNode) {
        return IntStream.range(0, yLength)
                .mapToObj(y -> IntStream.range(0, xLength)
                        .mapToObj(x -> getFromGrid(grid, x, y))
                        .map(node -> (node == null) ? "X"
                                : (node == srcNode) ? "S"
                                : (node == dstNode) ? "D"
                                : (node.used == 0) ? "_"
                                : ".")
                        .collect(Collectors.joining()))
                .collect(Collectors.joining("\n"));
    }

    private class NodePath implements Comparable<NodePath> {
        public final int fromX;
        public final int fromY;
        public final int toX;
        public final int toY;

        public final int step;
        public Node srcNode;
        public Node dstNode;

        public final List<Node> gridState;

        private int aStarScore = -1;

        public NodePath(int fromX, int fromY, int toX, int toY,
                        int step, Node srcNode, Node dstNode, List<Node> gridState) {
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
            this.step = step;
            this.srcNode = srcNode;
            this.dstNode = dstNode;
            this.gridState = new ArrayList<>(gridState);
            update();
        }

        public NodePath(Node[] path) {
            this(path[0].x, path[0].y, path[1].x, path[1].y,
                    1, nodes.get((xLength - 1) * yLength), nodes.get(0), nodes);
        }

        public int getAStarScore() {
            if (aStarScore < 0) {
                aStarScore = step * 2 + Math.abs(srcNode.x - dstNode.x) + Math.abs(srcNode.y - dstNode.y);
                gridState.stream()
                        .filter(Objects::nonNull)
                        .filter(node -> node.used == 0)
                        .mapToInt(emptyNode -> Math.abs(srcNode.x - emptyNode.x)
                                + Math.abs(srcNode.y - emptyNode.y))
                        .findFirst().ifPresent(dist -> aStarScore += dist);
            }
            return aStarScore;
        }

        public boolean reachedDest() {
            return srcNode == dstNode;
        }

        private void update() {
            Node a = getFromGrid(gridState, fromX, fromY);
            Node b = getFromGrid(gridState, toX, toY);
            Node newA = new Node(a.x, a.y, 0, a.used + a.avail);
            Node newB = new Node(b.x, b.y, b.used + a.used, b.avail - a.used);
            setToGrid(gridState, a.x, a.y, newA);
            setToGrid(gridState, b.x, b.y, newB);

            dstNode = getFromGrid(gridState, dstNode.x, dstNode.y);
            if(a == srcNode || b == srcNode)
                srcNode = newB;
        }

        @Override
        public int compareTo(NodePath o) {
            return Integer.compare(getAStarScore(), o.getAStarScore());
        }
    }
}
