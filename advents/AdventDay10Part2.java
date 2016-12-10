package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.Arrays;

/**
 * Created by Sean Deneen on 12/10/16.
 * Run the bots, then multiply the first 3 outputs.
 */
public class AdventDay10Part2 extends AdventDay10Part1 {

    private int[] outputs;

    @Override
    protected void setOutput(int outputIndex, int value) {
        if (outputIndex >= 0 && outputIndex < outputs.length)
            outputs[outputIndex] = value;
    }

    @Override
    public String compute(BufferedReader input) {
        outputs = new int[3];
        Arrays.fill(outputs, -1);

        runBots(input)
                .anyMatch(str -> (outputs[0] | outputs[1] | outputs[2]) >= 0);

        int product = Arrays.stream(outputs).reduce(1, (x, y) -> x * y);

        return "The product of the outputs is " + product;
    }

}
