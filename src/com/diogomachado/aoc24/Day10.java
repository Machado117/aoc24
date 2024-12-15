package com.diogomachado.aoc24;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Day10 {
    private enum Direction {
        UP(-1, 0),
        RIGHT(0, 1),
        DOWN(1, 0),
        LEFT(0, -1);

        private final int deltaX;
        private final int deltaY;

        Direction(int deltaX, int deltaY) {
            this.deltaX = deltaX;
            this.deltaY = deltaY;
        }
    }

    private static class Tile {
        Point coordinates;
        int value;
        Set<Point> reachablePeaks;
        int rating;

        Tile(int x, int y, int value) {
            this.coordinates = new Point(x, y);
            this.value = value;
            this.rating = 0;
        }
    }

    private int[][] map;
    private int rows;
    private int columns;
    private final List<Point> trailHeads = new ArrayList<>();
    private final Map<Point, Tile> visited = new HashMap<>();

    private Day10(final String input) {
        readInput(input);
    }

    private void readInput(final String path) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {
            List<String> input = reader.lines().toList();
            rows = input.size();
            columns = input.getFirst().length();
            map = new int[rows][columns];
            for (int i = 0; i < rows; i++) {
                String line = input.get(i);
                for (int j = 0; j < columns; j++) {
                    int value = Character.getNumericValue(line.charAt(j));
                    if (value == 0) {
                        trailHeads.add(new Point(i, j));
                    }
                    map[i][j] = value;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    boolean findTrail(Tile tile) {
        if (tile.value == 9) {
            tile.reachablePeaks = new HashSet<>();
            tile.reachablePeaks.add(tile.coordinates);
            tile.rating = 1;
            return true;
        }
        if (tile.reachablePeaks != null) {
            return !tile.reachablePeaks.isEmpty();
        }
        tile.reachablePeaks = new HashSet<>();
        for (Direction direction : Direction.values()) {
            int x = tile.coordinates.x + direction.deltaX;
            int y = tile.coordinates.y + direction.deltaY;
            if (isValidPoint(x, y) && map[x][y] == tile.value + 1) {
                Tile nextStep = visited.computeIfAbsent(new Point(x, y), _ -> new Tile(x, y, map[x][y]));
                if (findTrail(nextStep)) {
                    tile.reachablePeaks.addAll(nextStep.reachablePeaks);
                    tile.rating += nextStep.rating;
                }
            }
        }
        return !tile.reachablePeaks.isEmpty();
    }

    boolean isValidPoint(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < columns;
    }

    private Tile createStartingTile(Point p) {
        Tile start = new Tile(p.x, p.y, 0);
        visited.put(p, start);
        return start;
    }

    private long part1() {
        return trailHeads.stream()
                .map(this::createStartingTile)
                .filter(this::findTrail)
                .map(t -> t.reachablePeaks.size())
                .reduce(0, Integer::sum);
    }

    private long part2() {
        return trailHeads.stream()
                .map(trail -> visited.get(trail).rating)
                .reduce(0, Integer::sum);
    }

    public static void main(String[] args) {

        Day10 problem = new Day10("input/day10.txt");

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
