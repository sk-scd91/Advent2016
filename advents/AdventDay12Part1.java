package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntUnaryOperator;

/**
 * Created by Sean Deneen on 12/12/16.
 * Assemble the control system code, run it, then check register a.
 */
public class AdventDay12Part1 implements Advent {

    private static final String regNames = "abcd";
    private int[] regs = new int[regNames.length()];
    private List<IntUnaryOperator> compiledInstructions;

    // Step 1: Assemble
    private void assemble(String instruction) {
        String[] parts = instruction.split(" ");
        if (parts.length == 2) {
            final int regX = regNames.indexOf(parts[1]);
            if (regX < 0)
                throw new IllegalStateException(parts[1] + " is not a register for inc or dec.");
            switch (parts[0]) {
                case "inc": {
                    compiledInstructions.add(pc -> {
                        regs[regX]++;
                        return pc + 1;
                    });
                } break;
                case "dec": {
                    compiledInstructions.add(pc -> {
                        regs[regX]--;
                        return pc + 1;
                    });
                } break;
                default:
                    throw new IllegalStateException(parts + " with one argument is not an instruction.");
            }
            return;
        } else if (parts.length == 3) {
            switch (parts[0]) {
                case "cpy": {
                    final int regY = regNames.indexOf(parts[2]);
                    if (regY < 0)
                        throw new IllegalStateException(parts[2] + " is not a register for cpy.");
                    try {
                        final int x = Integer.parseInt(parts[1]);
                        compiledInstructions.add(pc -> {
                            regs[regY] = x;
                            return pc + 1;
                        });
                    } catch (NumberFormatException e) {
                        final int regX = regNames.indexOf(parts[1]);
                        if (regX < 0)
                            throw new IllegalStateException(parts[1] + " is not a value or register for cpy.");
                        compiledInstructions.add(pc -> {
                            regs[regY] = regs[regX];
                            return pc + 1;
                        });
                    }
                } break;
                case "jnz": {
                    final int regX = regNames.indexOf(parts[1]);
                    try {
                        final int offsetY = Integer.parseInt(parts[2]);
                        if (regX >= 0)
                            compiledInstructions.add(pc -> (regs[regX] != 0) ? pc + offsetY : pc + 1);
                         else {
                            try {
                                compiledInstructions.add(Integer.parseInt(parts[1]) != 0
                                        ? pc -> pc + offsetY : pc -> pc + 1);
                            } catch (NumberFormatException e) {
                                throw new IllegalStateException(parts[1] + " is not a value or register for jnz.");
                            }
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalStateException(parts[2] + " is not an offset value for jnz.");
                    }
                } break;
                default:
                    throw new IllegalStateException(parts + " with two arguments is not an instruction.");
            }
            return;
        }
        throw new IllegalStateException("Instruction not implemented: " + instruction);
    }

    // Step 2: Run
    private void run() {
        int pc = 0, len = compiledInstructions.size();
        while (pc < len)
            pc = compiledInstructions.get(pc).applyAsInt(pc);
    }

    @Override
    public String compute(BufferedReader input) {
        Arrays.fill(regs, 0);
        compiledInstructions = new ArrayList<>();

        input.lines().forEachOrdered(this::assemble);
        run();

        return "The value of register a is " + regs[0];
    }
}
