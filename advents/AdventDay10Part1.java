package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;
import java.util.regex.*;

/**
 * Created by Sean Deneen on 12/10/16.
 * Program bots, then find the bot that compares the two specified values.
 */
public class AdventDay10Part1 implements Advent {

    private static final int LOW_COMP = 17;
    private static final int HIGH_COMP = 61;

    private final Pattern VALUE_PATTERN = Pattern.compile("value (?<value>\\d+) goes to bot (?<bot>\\d+)");
    private final String OUT_EXP = "(?<%sType>bot|output) (?<%sIndex>\\d+)";
    private final Pattern GIVE_PATTERN = Pattern.compile("bot (?<indexIn>\\d+) gives low to "
            + String.format(OUT_EXP, "low", "low") + " and high to " + String.format(OUT_EXP, "high", "high"));

    private Map<Integer,Bot> bots;
    private int foundIndex;

    private boolean matchValue(String instruction) {
        Matcher matcher = VALUE_PATTERN.matcher(instruction);
        if (matcher.matches()) {
            int value = Integer.parseInt(matcher.group("value"));
            int botIndex = Integer.parseInt(matcher.group("bot"));
            setValue(botIndex, value);
            return true;
        }
        return false;
    }

    private boolean matchGive(String instruction) {
        Matcher matcher = GIVE_PATTERN.matcher(instruction);
        if (matcher.matches()) {
            int botIndex = Integer.parseInt(matcher.group("indexIn"));
            int lowIndex = Integer.parseInt(matcher.group("lowIndex"));
            int highIndex = Integer.parseInt(matcher.group("highIndex"));
            setFunctions(botIndex, "bot".equals(matcher.group("lowType")), lowIndex,
                    "bot".equals(matcher.group("highType")), highIndex);
            return true;
        }
        return false;
    }

    protected void setValue(int botIndex, int value) {
        bots.computeIfAbsent(botIndex, k -> new Bot()).setChip(value);
    }

    protected void setOutput(int outputIndex, int value) {
        //stub
    }

    protected void setFunctions(int botIndex, boolean lowIsBot, int low, boolean highIsBot, int high) {
        Bot bot = bots.computeIfAbsent(botIndex, k -> new Bot());
        bot.setSpecialFunction((x, y) -> foundBot(botIndex, x, y));
        bot.setFunctions(
                lowIsBot ? (v -> setValue(low, v)) : (o -> setOutput(low, o)),
                highIsBot ? (v -> setValue(high, v)) : (o -> setOutput(high, o))
        );
    }

    protected void foundBot(int botIndex, int low, int high) {
        if (low == LOW_COMP && high == HIGH_COMP)
            foundIndex = botIndex;
    }

    @Override
    public String compute(BufferedReader input) {
        bots = new HashMap<>();
        foundIndex = -1;

        input.lines()
                .filter(inst -> matchValue(inst) || matchGive(inst))
                .anyMatch(str -> foundIndex >= 0);

        return "The bot is " + foundIndex;
    }

    protected class Bot {
        private int chip1 = -1;
        private int chip2 = -1;
        private IntConsumer lowFunction = null;
        private IntConsumer highFunction = null;
        private BiConsumer<Integer, Integer> specialFunction = null;

        public void setChip(int otherChip) {
            if (chip1 < 0)
                chip1 = otherChip;
            else {
                chip2 = otherChip;
                execute();
            }
        }

        private void execute() {
            if (lowFunction != null) {
                int low = Math.min(chip1, chip2);
                int high = Math.max(chip1, chip2);
                lowFunction.accept(low);
                highFunction.accept(high);
                if (specialFunction != null)
                    specialFunction.accept(low, high);
                chip1 = chip2 = -1;
            }
        }

        public void setFunctions(IntConsumer low, IntConsumer high) {
            lowFunction = low;
            highFunction = high;
            if (chip2 >= 0)
                execute();
        }

        public void setSpecialFunction(BiConsumer<Integer, Integer> specialFunction) {
            this.specialFunction = specialFunction;
        }
    }
}
