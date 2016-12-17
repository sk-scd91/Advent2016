package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.BooleanSupplier;

/**
 * Created by Sean Deneen on 12/17/16.
 * Using an MD5 password, find the shortest path to the vault.
 */
public class AdventDay17Part1 implements Advent {

    private static final String PASSCODE = "pgflpeqp";
    private static final int GRID_WIDTH = 4;

    protected Deque<BooleanSupplier> searches;
    protected String minPath;

    private MessageDigest initDigest() {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {}
        return digest;
    }

    private MessageDigest cloneDigest(MessageDigest digest) {
        try {
            return (MessageDigest) digest.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    private boolean isOpen(int hex) {
        return (hex & 0xf) > 0xa;
    }

    private MessageDigest nextDirection(MessageDigest digest, char dir) {
        MessageDigest out = cloneDigest(digest);
        out.update((byte)dir);
        return out;
    }

    private boolean nextStep(MessageDigest digest, String path, int x, int y) {
        if (x * y == (GRID_WIDTH - 1) * (GRID_WIDTH - 1)) {
            minPath = path;
            return true;
        }
        byte[] hash = cloneDigest(digest).digest();

        if (y > 0 && isOpen(hash[0] >> 4))
            searches.add(() -> nextStep(nextDirection(digest, 'U'), path + 'U', x, y - 1));
        if (y + 1 < GRID_WIDTH && isOpen(hash[0]))
            searches.add(() -> nextStep(nextDirection(digest, 'D'), path + 'D', x, y + 1));
        if (x > 0 && isOpen(hash[1] >> 4))
            searches.add(() -> nextStep(nextDirection(digest, 'L'), path + 'L', x - 1, y));
        if (x + 1 < GRID_WIDTH && isOpen(hash[1]))
            searches.add(() -> nextStep(nextDirection(digest, 'R'), path + 'R', x + 1, y));

        return false;
    }

    @Override
    public String compute(BufferedReader input) {
        startSearch();

        while (continueSearch())
            ;

        return "The minimum path is " + minPath;
    }

    protected boolean continueSearch() {
        return !searches.isEmpty() && !searches.removeFirst().getAsBoolean();
    }

    protected void startSearch() {
        MessageDigest initialDigest = initDigest();
        initialDigest.reset();
        initialDigest.update(PASSCODE.getBytes());
        minPath = "";
        searches = new ArrayDeque<>();

        searches.add(() -> nextStep(initialDigest, "", 0, 0));
    }
}
