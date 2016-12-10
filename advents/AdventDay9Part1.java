package com.skscd91.advent2016.advents;

import com.skscd91.advent2016.utils.IOUtils;

import java.io.BufferedReader;
import java.util.Collections;
import java.util.regex.*;

/**
 * Created by Sean Deneen on 12/9/16.
 * Find the length of the uncompressed message.
 */
public class AdventDay9Part1 implements Advent {

    private final Pattern RUN_PATTERN = Pattern.compile("\\((?<length>\\d+)x(?<reps>\\d+)\\)");

    private StringBuilder messageBuilder;

    protected void decompress(String compressedMessage) {
        Matcher messageMatcher = RUN_PATTERN.matcher(compressedMessage);
        int startIndex = 0;

        while(messageMatcher.find(startIndex)) {
            int end = messageMatcher.end();
            int length = Integer.parseInt(messageMatcher.group("length"));
            int reps = Integer.parseInt(messageMatcher.group("reps"));

            append(compressedMessage, startIndex, messageMatcher.start());
            repeat(compressedMessage.substring(end, end + length), reps);
            startIndex = end + length;
        }

        startIndex = Math.min(startIndex, compressedMessage.length());
        append(compressedMessage, startIndex, compressedMessage.length());
    }

    protected void append(String str, int start, int end) {
        messageBuilder.append(str, start, end);
    }

    /**
     * Decompresses a non-literal cluster.
     * @param cluster The string cluster to decompress.
     * @param reps The number of times to repeat the cluster.
     */
    protected void repeat(String cluster, int reps) {
        Collections.nCopies(reps, cluster).forEach(messageBuilder::append);
    }

    protected long decompressionLength(String compressedMessage) {
        messageBuilder = new StringBuilder(compressedMessage.length());
        decompress(compressedMessage);
        return (long)messageBuilder.length();
    }

    @Override
    public String compute(BufferedReader input) {
        String compressedMessage = IOUtils.readLineSilently(input);

        long messageLength = decompressionLength(compressedMessage);

        return "The length of the message is " + messageLength;
    }
}
