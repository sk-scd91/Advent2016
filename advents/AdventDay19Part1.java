package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.BitSet;

/**
 * Created by Sean Deneen on 12/19/16.
 * Find the elf that gets the presents.
 */
public class AdventDay19Part1 implements Advent {

    private static final int ELF_COUNT = 3014603;

    private int getNextElf(BitSet elves, int index) {
        int nextElf = elves.nextSetBit(index + 1);
        if (nextElf < 0)
            nextElf = elves.nextSetBit(0);
        return nextElf;
    }

    private int findElf() {
        BitSet elves = new BitSet();
        elves.set(0, ELF_COUNT);
        int lastElf = -1;
        for (int elfCount = ELF_COUNT; elfCount > 1; elfCount--) {
            lastElf = getNextElf(elves, lastElf);
            elves.clear(getNextElf(elves, lastElf));
        }
        return lastElf + 1;
    }

    @Override
    public String compute(BufferedReader input) {
        int elfNumber = findElf();

        return "Elf #" + elfNumber + " gets all the presents.";
    }
}
