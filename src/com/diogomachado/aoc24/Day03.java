package com.diogomachado.aoc24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 {

    private record MultiplyInstruction(int factorA, int factorB, boolean enabled) {

        public long getProduct() {
            return (long) factorA * factorB;
        }
    }

    private final List<MultiplyInstruction> multiplications = new ArrayList<>();

    private Day03(final String input) {
        readInput(input);
    }

    private void readInput(final String path) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            boolean instructionsEnabled = true;
            while ((line = reader.readLine()) != null) {
                Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)|don't\\(\\)|do\\(\\)");
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String instruction = matcher.group(0).substring(0, matcher.group(0).indexOf("("));
                    switch (instruction) {
                        case "mul" -> multiplications.add(new MultiplyInstruction(Integer.parseInt(matcher.group(1)),
                                                                                  Integer.parseInt(matcher.group(2)),
                                                                                  instructionsEnabled
                        ));
                        case "do" -> instructionsEnabled = true;
                        case "don't" -> instructionsEnabled = false;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long part1() {
        return multiplications.stream().map(MultiplyInstruction::getProduct).reduce(0L, Long::sum);
    }

    private long part2() {
        return multiplications.stream()
                              .filter(MultiplyInstruction::enabled)
                              .map(MultiplyInstruction::getProduct)
                              .reduce(0L, Long::sum);
    }

    public static void main(String[] args) {
        Day03 problem = new Day03("input/day03.txt");

        System.out.println(problem.part1());
        System.out.println(problem.part2());
    }
}
