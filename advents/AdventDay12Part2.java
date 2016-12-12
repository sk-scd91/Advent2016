package com.skscd91.advent2016.advents;

/**
 * Created by Sean Deneen on 12/12/16.
 * Run part 1 with register c set to 1.
 */
public class AdventDay12Part2 extends AdventDay12Part1 {

    @Override
    protected void initRegisters() {
        super.initRegisters();
        regs[regNames.indexOf('c')] = 1;
    }

}
