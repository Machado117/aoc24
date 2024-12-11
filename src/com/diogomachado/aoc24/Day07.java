package com.diogomachado.aoc24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day07 {

    private record Equation(long testValue, List<Integer> calibrations) {
    }

    private final List<Equation> equations = new ArrayList<>();

    private Day07(final String input) {
        readInput(input);
    }

    private void readInput(final String path) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] equation = line.split(":");
                long testValue = Long.parseLong(equation[0]);
                List<Integer> calibrations = Arrays.stream(equation[1].trim().split(" "))
                                                   .map(Integer::parseInt)
                                                   .toList();
                equations.add(new Equation(testValue, calibrations));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean calibrateSm(long acc, List<Integer> calibrations, long testValue) {

        if (acc > testValue) {
            return false;
        }
        if (calibrations.isEmpty()) {
            return acc == testValue;
        }
        long sum = acc + calibrations.getFirst();
        long multiply = (acc == 0 ? 1 : acc) * calibrations.getFirst();
        return calibrateSm(sum, calibrations.subList(1, calibrations.size()), testValue)
                || calibrateSm(multiply, calibrations.subList(1, calibrations.size()), testValue);
    }

    private boolean calibrateSmc(long acc, List<Integer> calibrations, long testValue) {

        if (acc > testValue) {
            return false;
        }
        if (calibrations.isEmpty()) {
            return acc == testValue;
        }
        long sum = acc + calibrations.getFirst();
        long multiply = (acc == 0 ? 1 : acc) * calibrations.getFirst();
        long concatenate = concatenate(acc, calibrations.getFirst());
        return calibrateSmc(sum, calibrations.subList(1, calibrations.size()), testValue)
                || calibrateSmc(multiply, calibrations.subList(1, calibrations.size()), testValue)
                || calibrateSmc(concatenate, calibrations.subList(1, calibrations.size()), testValue);
    }

    private long concatenate(long left, long right) {
        if (left == 0) {
            return right;
        }
        return (long) (left * Math.pow(10, (int) (Math.log10(right)) + 1) + right);
    }

    private long part1() {

        return equations.stream()
                        .filter(e -> calibrateSm(0, e.calibrations(), e.testValue()))
                        .map(Equation::testValue)
                        .reduce(0L, Long::sum);

    }


    private long part2() {
        return equations.stream()
                        .filter(e -> calibrateSmc(0, e.calibrations(), e.testValue()))
                        .map(Equation::testValue)
                        .reduce(0L, Long::sum);
    }

    public static void main(String[] args) {

        Day07 problem = new Day07("input/day07.txt");

        long startPart1 = System.nanoTime();
        long resultPart1 = problem.part1();
        long endPart1 = System.nanoTime();
        System.out.printf("Part 1 result: %d (Time: %.3f seconds)%n%n", resultPart1, (endPart1 - startPart1) / 1e9);

        long startPart2 = System.nanoTime();
        long resultPart2 = problem.part2();
        long endPart2 = System.nanoTime();
        System.out.printf("Part 2 result: %d (Time: %.3f seconds)%n", resultPart2, (endPart2 - startPart2) / 1e9);

    }
}
