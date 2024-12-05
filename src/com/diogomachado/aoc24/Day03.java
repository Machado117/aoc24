package com.diogomachado.aoc24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 {

    private final List<int[]> mulInstructions = new ArrayList<>();

    private Day03(final String input) {
        readInput(input);
    }

    private void readInput(final String path) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
                Matcher matcher = pattern.matcher(line);
                while(matcher.find()) {
                    mulInstructions.add(new int[]{Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))});
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long part1() {
        return mulInstructions.stream().map(factors -> (long) factors[0] * factors[1]).reduce(0L, Long::sum);
    }

    private long part2() {
        return -1;
    }

    public static void main(String[] args) {

        Day03 problem = new Day03("input/day03.txt");

        System.out.println(problem.part1());
        System.out.println(problem.part2());
    }
}
