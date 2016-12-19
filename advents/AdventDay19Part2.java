package com.skscd91.advent2016.advents;

import java.util.BitSet;

/**
 * Created by Sean Deneen on 12/19/16.
 * Find the elf that gets the presents, when each elf takes from the elf opposite from the table.
 */
public class AdventDay19Part2 extends AdventDay19Part1 {

    @Override
    protected int findElf() {
        BitSet elves = new BitSet();
        elves.set(0, ELF_COUNT);
        int lastElf = -1;
        int elfGap = 0;
        int elfToTake = ELF_COUNT / 2; // Start at lower half.
        for (int elfCount = ELF_COUNT; elfCount > 1; elfCount--) {
            int prevElf = lastElf;
            lastElf = getNextElf(elves, lastElf);
            elfGap -= (lastElf + ELF_COUNT - prevElf - 1) % ELF_COUNT; // Gaps passed by, now out of range.
            while (elfToTake != (lastElf + elfGap + elfCount / 2) % ELF_COUNT) { // Until it lands on the elf across the table.
                int prevElfToTake = elfToTake;
                elfToTake = getNextElf(elves, elfToTake);
                elfGap += (elfToTake + ELF_COUNT - prevElfToTake - 1) % ELF_COUNT; // Gaps passed by, now in range.
            }
            elves.clear(elfToTake);
            elfGap++; // Include newly removed elf space as a gap in range.
        }
        return lastElf + 1;
    }

}
