package com.skscd91.advent2016.utils;

import java.io.*;

/**
 * Created by Sean Deneen on 12/1/16.
 *
 * Utilities to help with files and other IO.
 */
public class IOUtils {

    public static String readLineSilently(BufferedReader reader) {
        return reader.lines().findFirst().orElse("");
    }

}
