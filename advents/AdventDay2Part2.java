package com.skscd91.advent2016.advents;

import java.io.BufferedReader;

/**
 * Created by Sean Deneen on 12/2/16.
 * Get the bathroom keycode for a diamond shaped keypad.
 */
public class AdventDay2Part2 extends AdventDay2Part1 {

    private static String[] keypad = new String[] {
            "  1  ",
            " 234 ",
            "56789",
            " ABC ",
            "  D  "
    };

    private int keyX;
    private int keyY;

    @Override
    protected void nextKey(int direction) {
        switch (direction) {
            case 'U':
                if (keyY > 0 && keypad[keyY - 1].charAt(keyX) != ' ')
                    keyY--;
                break;
            case 'D':
                if (keyY + 1 < keypad.length && keypad[keyY + 1].charAt(keyX) != ' ')
                    keyY++;
                break;
            case 'L':
                if (keyX > 0 && keypad[keyY].charAt(keyX - 1) != ' ')
                    keyX--;
                break;
            case 'R':
                if (keyX + 1 < keypad.length && keypad[keyY].charAt(keyX + 1) != ' ')
                    keyX++;
                break;
        }
    }

    @Override
    protected String getKeyChar() {
        return keypad[keyY].substring(keyX, keyX + 1);
    }

    @Override
    public String compute(BufferedReader input) {
        keyX = 0;
        keyY = keypad.length / 2; // Start at 5 key.

        String keycode = getKeycode(input);

        return "The bathroom code is: " + keycode;
    }

}
