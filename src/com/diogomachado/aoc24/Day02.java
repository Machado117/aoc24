package com.diogomachado.aoc24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day02 {

    private final List<List<Integer>> reports = new ArrayList<>();

    private Day02(final String input) {
        readInput(input);
    }

    private void readInput(final String path) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] values = line.split("\\s+");
                reports.add(Arrays.stream(values).map(Integer::parseInt).toList());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long part1() {
        int safeCount = 0;
        for (List<Integer> report : this.reports) {
            boolean safeReport = true;
            boolean ascOrder = report.get(0) < report.get(1);
            Integer prev = report.get(0);
            for (int j = 1; safeReport && j < report.size(); j++) {
                Integer cur = report.get(j);
                int difference = cur - prev;
                if (difference * (ascOrder ? 1 : -1) <= 0 || Math.abs(difference) > 3) {
                    safeReport = false;
                }
                prev = cur;
            }
            if (safeReport) {
                safeCount++;
            }
        }
        return safeCount;
    }

    private long part2() {
        return -1;
    }

    public static void main(String[] args) {

        Day02 problem = new Day02("input/day02.txt");

        System.out.println(problem.part1());
        System.out.println(problem.part2());
    }
}
