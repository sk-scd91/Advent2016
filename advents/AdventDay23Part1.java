package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

/**
 * Created by Sean Deneen on 12/23/16.
 * Assemble the software for the safe computer, then find the keypad output to the safe.
 */
public class AdventDay23Part1 extends AdventDay12Part1 {

    private List<String> uncompiledInstructions;

    @Override
    protected IntUnaryOperator assemble(String instruction) {
        String[] parts = instruction.split(" ");
        if (parts.length == 2 && "tgl".equals(parts[0])) {
            final int regX = regNames.indexOf(parts[1]);
            if (regX < 0)
                throw new IllegalStateException(parts[1] + " is not a register for tgl.");
            return pc -> {
                toggleInstruction(pc + regs[regX]);
                return pc + 1;
            };
        } else if (parts.length == 3 && "jnz".equals(parts[0])) {
            IntSupplier lhs = jnzSubinstruction(parts[1]);
            IntSupplier rhs = jnzSubinstruction(parts[2]);
            return pc -> pc + ((lhs.getAsInt() == 0) ? 1 : rhs.getAsInt());
        }
        return super.assemble(instruction);
    }

    private void toggleInstruction(int index) {
        if (index < 0 || index >= compiledInstructions.size())
            return;
        String[] parts = uncompiledInstructions.get(index).split(" ");
        String newInstruction = null;
        if (parts.length == 2) {
            newInstruction = ("inc".equals(parts[0]) ? "dec" : "inc")
                    + " " + parts[1];
        } else if (parts.length == 3) {
            newInstruction = ("jnz".equals(parts[0]) ? "cpy" : "jnz")
                    + " " + parts[1] + " " + parts[2];
        } else {
            throw new IllegalStateException("Instruction not implemented: " + uncompiledInstructions.get(index));
        }
        uncompiledInstructions.set(index, newInstruction);
        try {
            IntUnaryOperator newOp = assemble(newInstruction);
            compiledInstructions.set(index, newOp);
        } catch (Exception e) {
            compiledInstructions.set(index, pc -> pc + 1); // NOP for invalid instructions.
        }
    }

    private IntSupplier jnzSubinstruction(String part) {
        int reg = regNames.indexOf(part);
        if (reg >= 0)
            return () -> regs[reg];
        try {
            int num = Integer.parseInt(part);
            return () -> num;
        } catch (NumberFormatException e) {
            throw new IllegalStateException(part + " is not an operator of jnz");
        }
    }

    @Override
    public String compute(BufferedReader input) {
        initRegisters();

        uncompiledInstructions = input.lines()
                .collect(Collectors.toList());

        compiledInstructions = uncompiledInstructions.stream()
                .map(this::assemble)
                .collect(Collectors.toList());
        run();

        return "The value sent to the safe is " + regs[regNames.indexOf('a')];
    }

    @Override
    protected void initRegisters() {
        super.initRegisters();
        regs[regNames.indexOf('a')] = 7;
    }
}
