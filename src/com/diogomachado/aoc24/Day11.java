package com.diogomachado.aoc24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class Day11 {
    private record Operation(long value, int iterations){}

    private List<Long> startingStones;
    private final Map<Operation,Long> memo = new HashMap<>();

    private Day11(final String input) {
        readInput(input);
    }

    private void readInput(final String path) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {
            startingStones = Arrays.stream(reader.readLine().split(" ")).map(Long::parseLong).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long part1() {
        List<Long> stones = new LinkedList<>(startingStones);
        for (int i = 0; i < 25; i++) {
            ListIterator<Long> iterator = stones.listIterator();
            while (iterator.hasNext()) {
                Long stone = iterator.next();
                int digits;
                if (stone == 0L) {
                    iterator.set(1L);
                } else if ((digits = ((int) Math.log10(stone)) + 1) % 2 == 0) {
                    int half = digits / 2;
                    long leftHalf = stone / (int) Math.pow(10, half);
                    long rightHalf = stone % (int) Math.pow(10, half);
                    iterator.set(leftHalf);
                    iterator.add(rightHalf);
                } else {
                    iterator.set(stone * 2024);
                }
            }
        }
        return stones.size();
    }

    private long totalStones(long stone, int iterations) {
        if (iterations == 0) {
            return 1;
        }
        Long numStones = memo.get(new Operation(stone, iterations));
        if (numStones != null) {
            return numStones;
        }
        int digits;
        if (stone == 0L) {
            numStones = totalStones(1L, iterations - 1);
        } else if ((digits = ((int) Math.log10(stone)) + 1) % 2 == 0) {
            int half = digits / 2;
            long leftHalf = stone / (int) Math.pow(10, half);
            long rightHalf = stone % (int) Math.pow(10, half);
            numStones = totalStones(leftHalf, iterations - 1) + totalStones(rightHalf, iterations - 1);
        } else {
            numStones = totalStones(stone * 2024, iterations - 1);
        }
        memo.put(new Operation(stone, iterations), numStones);
        return numStones;
    }

    private long part2() {
        return startingStones.stream().map(s -> totalStones(s, 75)).reduce(0L, Long::sum);
    }

    public static void main(String[] args) {

        Day11 problem = new Day11("input/day11.txt");

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
