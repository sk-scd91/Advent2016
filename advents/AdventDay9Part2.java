package com.skscd91.advent2016.advents;

/**
 * Created by Sean Deneen on 12/9/16.
 * Like part 1, but with recursive decompression.
 * Only keeps track of sum so that it won't require over 20GB of RAM.
 */
public class AdventDay9Part2 extends AdventDay9Part1 {

    private long count;

    @Override
    protected void append(String str, int start, int end) {
        if (start >= 0 && end >= start && end <= str.length())
            count += (end - start);
    }

    @Override
    protected void repeat(String cluster, int reps) {
        if (reps > 0) {
            long start = count;
            decompress(cluster);
            count += (count - start) * (reps - 1);
        }
    }

    @Override
    protected long decompressionLength(String compressedMessage) {
        count = 0L;
        decompress(compressedMessage);
        return count;
    }

}
