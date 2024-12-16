package com.diogomachado.aoc24;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class Day08 {

    private int columns;
    private char[][] map;
    private int rows;

    private Day08(final String input) {
        readInput(input);
    }

    private void readInput(final String path) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {
            List<String> input = reader.lines().toList();
            rows = input.size();
            columns = input.getFirst().length();
            map = new char[rows][columns];
            for (int i = 0; i < rows; i++) {
                String line = input.get(i);
                map[i] = line.toCharArray();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Point> combinePairs(Map<Character, Set<Point>> antenas, BiFunction<Point, Point, Set<Point>> findAntinodesFunction) {
        Set<Point> antinodes = new HashSet<>();
        for (Set<Point> s : antenas.values()) {
            List<Point> positions = new ArrayList<>(s.stream().toList());
            for (int i = 0; i < positions.size() - 1; i++) {
                Point a = positions.get(i);
                for (int j = i + 1; j < positions.size(); j++) {
                    Point b = positions.get(j);
                    antinodes.addAll(findAntinodesFunction.apply(a, b));
                }
            }
        }
        return antinodes;
    }

    private long part1() {
        Map<Character, Set<Point>> antenas = findAntenas();
        Set<Point> antinodes = combinePairs(antenas, this::findAntinodes);

        return antinodes.size();

    }

    private Map<Character, Set<Point>> findAntenas() {
        Map<Character, Set<Point>> antenas = new HashMap<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (map[i][j] != '.') {
                    final Set<Point> points = antenas.computeIfAbsent(map[i][j], k -> new HashSet<>());
                    points.add(new Point(i, j));
                }
            }
        }
        return antenas;
    }

    private Set<Point> findAntinodes(final Point a, final Point b) {
        final Set<Point> antinodes = new HashSet<>();

        int[] diff = {1, -2};
        int diffX = a.x - b.x;
        int diffY = a.y - b.y;
        for (int i : diff) {
            int antinodeX = a.x + i * diffX;
            int antinodeY = a.y + i * diffY;

            if (checkValid(antinodeX, antinodeY)) {
                antinodes.add(new Point(antinodeX, antinodeY));
            }
        }
        return antinodes;
    }

    private Set<Point> findAntinodesUpdated(final Point a, final Point b) {
        final Set<Point> antinodes = new HashSet<>();

        int[] diff = {1, -1};
        int diffX = a.x - b.x;
        int diffY = a.y - b.y;
        for (int i : diff) {
            int factor = 0;
            while (true) {
                int antinodeX = a.x + factor * i * diffX;
                int antinodeY = a.y + factor * i * diffY;

                if (checkValid(antinodeX, antinodeY)) {
                    antinodes.add(new Point(antinodeX, antinodeY));
                    factor++;
                } else {
                    break;
                }
            }
        }
        return antinodes;
    }

    private boolean checkValid(final int antinodeX, final int antinodeY) {
        return antinodeX >= 0 && antinodeY >= 0 && antinodeX < rows && antinodeY < columns;
    }


    private long part2() {
        Map<Character, Set<Point>> antenas = findAntenas();
        Set<Point> antinodes = combinePairs(antenas, this::findAntinodesUpdated);

        return antinodes.size();
    }

    public static void main(String[] args) {

        Day08 problem = new Day08("input/day08.txt");

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
