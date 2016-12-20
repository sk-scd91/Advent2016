package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.List;

/**
 * Created by Sean Deneen on 12/20/16.
 * Finds the amount of IP addresses not blocked by the firewall.
 */
public class AdventDay20Part2 extends AdventDay20Part1 {

    private static final long MAX_IP_ADDRESS = (1L << 32) - 1L;

    private long getIpAddressCount(List<IPRange> ranges) {
        long count = 0;
        long lastEndIP = -1;
        for (IPRange range : ranges) {
            if (range.getIpStart() - 1 > lastEndIP)
                count += range.getIpStart() - lastEndIP - 1;
            lastEndIP = Math.max(lastEndIP, range.getIpEnd());
        }
        count += MAX_IP_ADDRESS - lastEndIP;
        return count;
    }

    @Override
    public String compute(BufferedReader input) {
        List<IPRange> ranges = getListOfIPRanges(input);

        long ipCount = getIpAddressCount(ranges);

        return "The number of IP Addresses aloud is: " + ipCount;
    }
}