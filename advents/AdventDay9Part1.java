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

    protected String decompress(String compressedMessage) {
        Matcher messageMatcher = Pattern.compile("\\((?<length>\\d+)x(?<reps>\\d+)\\)")
                .matcher(compressedMessage);
        StringBuilder messageBuilder = new StringBuilder();
        int startIndex = 0;

        while(messageMatcher.find(startIndex)) {
            int end = messageMatcher.end();
            int length = Integer.parseInt(messageMatcher.group("length"));
            int reps = Integer.parseInt(messageMatcher.group("reps"));

            messageBuilder.append(compressedMessage, startIndex, messageMatcher.start());
            Collections.nCopies(reps, compressedMessage.substring(end, end + length))
                    .forEach(messageBuilder::append);
            startIndex = end + length;
        }

        startIndex = Math.min(startIndex, compressedMessage.length());
        return messageBuilder.append(compressedMessage, startIndex, compressedMessage.length())
                .toString();
    }

    @Override
    public String compute(BufferedReader input) {
        String compressedMessage = IOUtils.readLineSilently(input);

        String message = decompress(compressedMessage);

        return "The length of the message is " + message.length();
    }
}
