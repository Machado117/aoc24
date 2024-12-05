package com.diogomachado.aoc24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day05 {

    private final List<Long> inputValues = new ArrayList<>();

    private Day05(final String input) {
        readInput(input);
    }

    private void readInput(final String path) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                long value = Long.parseLong(line);
                inputValues.add(value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long part1() {
        return -1;
    }

    private long part2() {
        return -1;
    }

    public static void main(String[] args) {

        Day05 problem = new Day05("input/day05.txt");

        System.out.println(problem.part1());
        System.out.println(problem.part2());
    }
}
