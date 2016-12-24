package com.skscd91.advent2016.advents;

/**
 * Created by Sean Deneen on 12/23/16.
 * Same as part 1, but starting with a keypad entry of 12.
 */
public class AdventDay23Part2 extends AdventDay23Part1 {

    @Override
    protected void initRegisters() {
        super.initRegisters();
        regs[regNames.indexOf('a')] = 12;
    }
}
