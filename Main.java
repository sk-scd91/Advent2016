package com.skscd91.advent2016;

import com.skscd91.advent2016.advents.Advent;

import java.io.*;

public class Main {

    private static final String ADVENT_FORMAT = "AdventDay%dPart%d";

    private static Advent getAdvent(int day, int part) {
        String className = String.format(ADVENT_FORMAT, day, part);
        try {
            return (Advent) Class.forName("com.skscd91.advent2016.advents." + className).newInstance();
        } catch (ClassNotFoundException e) {
            System.err.println(className + " not found.");
            e.printStackTrace();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        for (int i = 0; i + 2 <= args.length; i += 2) {
            int day = Integer.parseInt(args[i]);
            int part = Integer.parseInt(args[i + 1]);
            String adventName = String.format(ADVENT_FORMAT, day, part);
            Advent advent = getAdvent(day, part);

            if (advent == null)
                break;

            System.out.println("For " + adventName + ":");

            try (FileReader fileReader = new FileReader("Day" + day)) {
                System.out.println(advent.compute(new BufferedReader(fileReader)));
            } catch (FileNotFoundException e) {
                System.out.println(advent.compute(new BufferedReader(new StringReader(""))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
