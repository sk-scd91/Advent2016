package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.List;
import java.util.Objects;
import java.util.regex.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Sean Deneen on 12/22/16.
 * Find the number of viable nodes in a storage grid.
 */
public class AdventDay22Part1 implements Advent {

    private final Pattern FS_PATTERN = Pattern.compile("/dev/grid/node-x(?<x>\\d+)-y(?<y>\\d+)");

    private Node createNode(String line) {
        String[] columns = line.split("\\s+");
        Matcher fsMatcher = FS_PATTERN.matcher(columns[0]);
        if (fsMatcher.matches()) {
            int x = Integer.parseInt(fsMatcher.group("x"));
            int y = Integer.parseInt(fsMatcher.group("y"));
            int used = Integer.parseInt(columns[2].replaceAll("\\D", ""));
            int avail = Integer.parseInt(columns[3].replaceAll("\\D", ""));
            return new Node(x, y, used, avail);
        }
        return null;
    }

    protected List<Node> createNodeList(BufferedReader input) {
        return input.lines()
                .skip(2L)
                .map(this::createNode)
                // Assume in order.
                .collect(Collectors.toList());
    }

    protected Stream<Node[]> getViablePairs(List<Node> nodes) {
        return nodes.stream()
                .filter(Objects::nonNull)
                .filter(node -> node.used != 0)
                .flatMap(a -> nodes.stream()
                        .filter(Objects::nonNull)
                        .filter(b -> (a.x != b.x || a.y != b.y)
                                && a.canFitIn(b))
                        .map(b -> new Node[]{a, b}));
    }

    @Override
    public String compute(BufferedReader input) {
        List<Node> nodes = createNodeList(input);

        long count = getViablePairs(nodes).count();

        return "The number of viable pairs is " + count;
    }

    protected class Node {
        public final int x;
        public final int y;
        public final int used;
        public final int avail;

        public Node(int x, int y, int used, int avail) {
            this.x = x;
            this.y = y;
            this.used = used;
            this.avail = avail;
        }

        public boolean canFitIn(Node o) {
            return used <= o.avail;
        }

    }
}
