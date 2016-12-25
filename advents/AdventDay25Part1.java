package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Sean Deneen on 12/25/16.
 * Find the lowest initial positive integer that can generate 01* using the program provided.
 */
public class AdventDay25Part1 extends AdventDay23Part1 {

    private List<Integer> signal;
    private boolean canOutput;

    @Override
    protected IntUnaryOperator assemble(String instruction) {
        String[] parts = instruction.split(" ");
        if (parts.length == 2 && "out".equals(parts[0])) {
            final int regX = regNames.indexOf(parts[1]);
            if (regX < 0) {
                try {
                    final int x = Integer.parseInt(parts[1]);
                    return pc -> {
                        output(x);
                        return pc + 1;
                    };
                } catch (NumberFormatException e) {
                    throw new IllegalStateException(parts[1] + " is not a register or value for out.");
                }
            }
            return pc -> {
                output(regs[regX]);
                return pc + 1;
            };
        }
        return super.assemble(instruction);
    }

    // Write output to a list, if enabled.
    private void output(int out) {
        if (canOutput)
            signal.add(out);
    }

    private boolean runToFindLoop(int startValue) {
        initRegisters();
        regs[regNames.indexOf('a')] = startValue;
        signal = new ArrayList<>();
        canOutput = true;

        int signalLength = 0;
        int tPc = 0;
        while (signalLength < 4) { // Run single thread until signal starts to match pattern.
            tPc = runOnce(tPc);
            if (tPc < 0)
                return false;
            if (signalLength != signal.size()) { // Check if signal is correct again.
                if (signal.get(signalLength) != (signalLength & 1)) // If not 010101...
                    return false;
                signalLength = signal.size();
            }
        }

        // Clone the thread, then use tortoise-hare cycle detection.
        int hPc = tPc;
        int[] tRegs = regs;
        int[] hRegs = regs.clone();
        do {
            canOutput = false; // Turn off output for slow thread.
            tPc = runOnce(tPc);
            if (tPc < 0)
                return false;

            regs = hRegs; // Replace with fast thread's registers.
            canOutput = true; // Toggle output on.
            hPc = runOnce(hPc); // Run fast thread twice.
            hPc = runOnce(hPc);
            if (hPc < 0) // Only need to check once, since runOnce(-1) == -1.
                return false;
            regs = tRegs; // Switch back to slow thread.

            if (signalLength != signal.size()) { // Check if signal is correct again.
                if (signal.get(signalLength) != (signalLength & 1))
                    return false;
                signalLength = signal.size();
            }
        } while (tPc != hPc || !Arrays.equals(tRegs, hRegs)); // Loop until the threads match state.

        return true;
    }

    private int runOnce(int pc) {
        if (pc < 0 || pc >= compiledInstructions.size()) // If out of bounds, return -1.
            return -1;
        return compiledInstructions.get(pc).applyAsInt(pc);
    }

    @Override
    public String compute(BufferedReader input) {

        compiledInstructions = input.lines()
                .map(this::assemble)
                .collect(Collectors.toList());

        int lowest = IntStream.rangeClosed(0, Integer.MAX_VALUE)
                .filter(this::runToFindLoop)
                .findFirst().orElse(-1);

        return "The lowest starting variable that generates the signal is: " + lowest;
    }
}
