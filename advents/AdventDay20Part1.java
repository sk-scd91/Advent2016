package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Sean Deneen on 12/20/16.
 * Find the lowest IP Adress not blocked by the firewall.
 */
public class AdventDay20Part1 implements Advent {

    protected class IPRange implements Comparable<IPRange> {

        private final long ipStart;
        private final long ipEnd;

        public IPRange(long start, long end) {
            ipStart = start;
            ipEnd   = end;
        }

        public IPRange(String range) {
            String[] addresses = range.split("-");
            ipStart = Long.parseLong(addresses[0]);
            ipEnd   = Long.parseLong(addresses[1]);
        }

        public long getIpStart() {
            return ipStart;
        }

        public long getIpEnd() {
            return ipEnd;
        }

        @Override
        public int compareTo(IPRange o) {
            int startComparison = Long.compare(ipStart, o.ipStart);
            return (startComparison != 0) ? startComparison : Long.compare(ipEnd, o.ipEnd);
        }
    }

    private long getLowestIP(List<IPRange> ranges) {
        long lastEndIP = -1;
        for (IPRange range : ranges) {
            if (range.getIpStart() - 1 > lastEndIP)
                return lastEndIP + 1;
            lastEndIP = Math.max(lastEndIP, range.getIpEnd());
        }
        return -1;
    }

    @Override
    public String compute(BufferedReader input) {
        List<IPRange> ranges = input.lines()
                .map(IPRange::new)
                .sorted()
                .collect(Collectors.toList());

        long ip = getLowestIP(ranges);

        return "The lowest IP Address not blocked is: " + ip;
    }
}
