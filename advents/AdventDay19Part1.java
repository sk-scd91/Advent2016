package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.BitSet;

/**
 * Created by Sean Deneen on 12/19/16.
 * Find the elf that gets the presents, when each elf takes from the next elf.
 */
public class AdventDay19Part1 implements Advent {

    protected static final int ELF_COUNT = 3014603;

    // Get the next elf with presents.
    protected int getNextElf(BitSet elves, int index) {
        int nextElf = elves.nextSetBit(index + 1);
        if (nextElf < 0) // Wrap around.
            nextElf = elves.nextSetBit(0);
        return nextElf;
    }

    protected int findElf() {
        BitSet elves = new BitSet();
        elves.set(0, ELF_COUNT);
        int lastElf = -1;
        for (int elfCount = ELF_COUNT; elfCount > 1; elfCount--) { // Until there is only one elf.
            lastElf = getNextElf(elves, lastElf); // The next elf standing...
            elves.clear(getNextElf(elves, lastElf)); // ...takes from the elf after.
        }
        return lastElf + 1; // Use 1-based count.
    }

    @Override
    public String compute(BufferedReader input) {
        int elfNumber = findElf();

        return "Elf #" + elfNumber + " gets all the presents.";
    }
}
