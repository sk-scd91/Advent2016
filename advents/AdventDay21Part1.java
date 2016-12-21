package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.function.Consumer;
import java.util.regex.*;

/**
 * Created by Sean Deneen on 12/21/16.
 * Find the new scrambled password based on the instructions.
 */
public class AdventDay21Part1 implements Advent {

    private static final String PASSWORD = "abcdefgh";

    protected enum InstructionMatchers {
        SWAP_POS (Pattern.compile("swap position (?<x>\\d+) with position (?<y>\\d+)")),
        SWAP_CHAR(Pattern.compile("swap letter (?<x>[a-z]) with letter (?<y>[a-z])")),
        ROTATE_STEPS(Pattern.compile("rotate (?<dir>left|right) (?<x>\\d+) steps?")),
        ROTATE_LI(Pattern.compile("rotate based on position of letter (?<x>[a-z])")),
        REVERSE(Pattern.compile("reverse positions? (?<x>\\d+) through (?<y>\\d+)")),
        MOVE(Pattern.compile("move position (?<x>\\d+) to position (?<y>\\d+)"));

        private final Pattern pattern;

        InstructionMatchers(Pattern pattern) {
            this.pattern = pattern;
        }

        public Matcher matcher(CharSequence instruction) {
            return pattern.matcher(instruction);
        }
    }

    private StringBuilder scrambler;

    private void swapPosition(int x, int y) {
        char t = scrambler.charAt(x);
        scrambler.setCharAt(x, scrambler.charAt(y));
        scrambler.setCharAt(y, t);
    }

    private void swapLetters(String x, String y) {
        swapPosition(scrambler.indexOf(x), scrambler.indexOf(y));
    }

    private void rotateSteps(String dir, int steps) {
        steps %= scrambler.length(); // in case number goes out of bounds.
        if ("left".equals(dir)) {
            String leftEnd = scrambler.substring(0, steps);
            scrambler.delete(0, steps);
            scrambler.append(leftEnd);
        } else {
            String rightEnd = scrambler.substring(scrambler.length() - steps);
            scrambler.delete(scrambler.length() - steps, scrambler.length());
            scrambler.insert(0, rightEnd);
        }
    }

    private void rotateLetterIndex(String x) {
        int shift = scrambler.indexOf(x);
        shift += (shift >= 4) ? 2 : 1;
        rotateSteps("right", shift);
    }

    private void reverse(int x, int y) {
        StringBuilder toReverse = new StringBuilder(scrambler.substring(x, y + 1))
                .reverse();
        scrambler.replace(x, y + 1, toReverse.toString());
    }

    private void move(int x, int y) {
        char t = scrambler.charAt(x);
        scrambler.deleteCharAt(x);
        scrambler.insert(y, t);
    }

    private void execInstruction(String instruction) {
        for (InstructionMatchers iMatcher : InstructionMatchers.values()) {
            Matcher matcher = iMatcher.matcher(instruction);
            if (matcher.matches()) {
                switch (iMatcher) {
                    case SWAP_POS:
                        swapPosition(Integer.parseInt(matcher.group("x")),
                                Integer.parseInt(matcher.group("y")));
                        break;
                    case SWAP_CHAR:
                        swapLetters(matcher.group("x"), matcher.group("y"));
                        break;
                    case ROTATE_STEPS:
                        rotateSteps(matcher.group("dir"), Integer.parseInt(matcher.group("x")));
                        break;
                    case ROTATE_LI:
                        rotateLetterIndex(matcher.group("x"));
                        break;
                    case REVERSE:
                        reverse(Integer.parseInt(matcher.group("x")),
                                Integer.parseInt(matcher.group("y")));
                        break;
                    case MOVE:
                        move(Integer.parseInt(matcher.group("x")),
                                Integer.parseInt(matcher.group("y")));
                        break;
                }
                return;
            }
        }
        throw new IllegalStateException("No instructions match: " + instruction);
    }

    @Override
    public String compute(BufferedReader input) {
        scrambler = new StringBuilder(PASSWORD);

        input.lines().forEachOrdered(this::execInstruction);

        String scrambled = scrambler.toString();

        return "The scrambled message is " + scrambled;
    }
}
