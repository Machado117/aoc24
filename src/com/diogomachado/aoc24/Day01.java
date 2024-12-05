package com.diogomachado.aoc24;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day01 {

    final List<Long> leftList = new ArrayList<>();
    final List<Long> rightList = new ArrayList<>();

    public Day01(final String input) {
        readInput(input);
    }

    private void readInput(final String path) {
        try (final Scanner inputScanner = new Scanner(new File(path))) {

            while (inputScanner.hasNextLong()) {
                this.leftList.add(inputScanner.nextLong());
                this.rightList.add(inputScanner.nextLong());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public long part1() {
        final List<Long> leftListCopy = new ArrayList<>(this.leftList);
        final List<Long> rightListCopy = new ArrayList<>(this.rightList);
        Collections.sort(leftListCopy);
        Collections.sort(rightListCopy);

        long sum = 0;
        for (int i = 0; i < leftListCopy.size(); i++) {
            sum += Math.abs(leftListCopy.get(i) - rightListCopy.get(i));
        }
        return sum;
    }

    public long part2() {
        final Map<Long, Integer> rightListCounter = this.rightList.stream()
                                                               .collect(Collectors.toMap(Function.identity(),
                                                                                         value -> 1,
                                                                                         (oldValue, newValue) -> oldValue + 1
                                                               ));

        return this.leftList.stream()
                            .reduce(0L, (partialScore, value) -> partialScore + value * rightListCounter.getOrDefault(value, 0));
    }

    public static void main(String[] args) {

        Day01 problem = new Day01("input/day01.txt");

        System.out.println(problem.part1());
        System.out.println(problem.part2());
    }
}
