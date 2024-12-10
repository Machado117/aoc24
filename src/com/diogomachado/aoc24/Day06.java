package com.diogomachado.aoc24;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day06 {
    private enum Direction {
        UP(-1, 0) {
            @Override
            public Direction rotate() {
                return Direction.RIGHT;
            }
        }, RIGHT(0, 1) {
            @Override
            public Direction rotate() {
                return Direction.DOWN;
            }
        }, DOWN(1, 0) {
            @Override
            public Direction rotate() {
                return Direction.LEFT;
            }
        }, LEFT(0, -1) {
            @Override
            public Direction rotate() {
                return Direction.UP;
            }
        };

        public abstract Direction rotate();

        private final int deltaX;
        private final int deltaY;

        Direction(int deltaX, int deltaY) {
            this.deltaX = deltaX;
            this.deltaY = deltaY;
        }

        public int getDeltaX() {
            return deltaX;
        }

        public int getDeltaY() {
            return deltaY;
        }
    }

    private static class Player {
        private final Point position;
        private Direction direction;

        public Player(Point position) {
            this.position = position.getLocation();
            this.direction = Direction.UP;
        }

        public void move(Point position) {
            this.position.move(position.x, position.y);
        }

        public void move(int x, int y) {
            this.position.move(x, y);
        }

        public void rotate() {
            direction = direction.rotate();
        }

        public Point getPosition() {
            return position;
        }

        public Direction getDirection() {
            return direction;
        }

    }

    private static class VisitedCells {
        private final Map<Point,EnumSet<Direction>> visitedCells = new HashMap<>();

        public VisitedCells() {

        }

        public boolean visit(Point position, Direction direction) {
            final EnumSet<Direction> directions = this.visitedCells.computeIfAbsent(
                    new Point(position),
                    k -> EnumSet.noneOf(Direction.class).clone()
            );
            return !directions.add(direction);
        }

        public VisitedCells(VisitedCells original) {
            original.visitedCells.forEach((key, value) -> this.visitedCells.put(new Point(key), EnumSet.copyOf(value)));
        }

        public boolean contains(Point position) {
            return this.visitedCells.containsKey(position);
        }

        public int getVisitedCells() {
            return visitedCells.size();
        }
    }

    private final Point initialPosition = new Point(0, 0);
    private int columns;
    private char[][] map;
    private int rows;

    private Day06(final String input) {
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
                final int index = line.indexOf("^");
                if (index != -1) {
                    initialPosition.setLocation(i, index);
                }
                map[i] = line.toCharArray();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    boolean isOutsideMap(Point position) {
        return position.x < 0 || position.y < 0 || position.x >= rows || position.y >= columns;
    }

    private int walk(boolean findLoops) {
        final Player guard = new Player(initialPosition);
        final VisitedCells visited = new VisitedCells();
        final Set<Point> obstructions = new HashSet<>();

        while (true) {
            visited.visit(guard.getPosition(), guard.direction);

            final Point nextPosition = getNextPosition(guard);

            if (isOutsideMap(nextPosition)) {
                break;
            }
            char next = map[nextPosition.x][nextPosition.y];
            if (next == '#') {
                guard.rotate();
            } else {
                if (findLoops && isValidObstructionPosition(nextPosition, visited, obstructions)) {
                    if (findLoop(guard, visited, nextPosition)) {
                        obstructions.add(nextPosition);
                    }
                }
                guard.move(nextPosition);
            }
        }
        return findLoops ? obstructions.size() : visited.getVisitedCells();
    }

    private boolean isValidObstructionPosition(final Point position, final VisitedCells visited, final Set<Point> obstructions) {
        return !position.equals(initialPosition) && !visited.contains(position) && !obstructions.contains(
                position);
    }

    private long part1() {
        return walk(false);
    }

    private static Point getNextPosition(final Player guard) {
        Direction dir = guard.getDirection();
        Point nextPosition = new Point(guard.getPosition());
        nextPosition.translate(dir.getDeltaX(), dir.getDeltaY());
        return nextPosition;
    }

    private long part2() {
        return walk(true);
    }

    private boolean findLoop(Player guard, VisitedCells visited, Point obstructionPosition) {
        final Player guardChecker = new Player(guard.position);
        guardChecker.direction = guard.direction;
        final VisitedCells visitedChecker = new VisitedCells(visited);

        guardChecker.rotate();
        while (true) {
            if (visitedChecker.visit(guardChecker.getPosition(), guardChecker.direction)) {
                return true;
            }
            final Point nextPosition = getNextPosition(guardChecker);

            if (isOutsideMap(nextPosition)) {
                return false;
            }
            char next = map[nextPosition.x][nextPosition.y];
            if (next == '#' || obstructionPosition.equals(nextPosition)) {
                guardChecker.rotate();
            } else {
                guardChecker.move(nextPosition);
            }
        }
    }

    public static void main(String[] args) {

        Day06 problem = new Day06("input/day06.txt");

        long startPart1 = System.nanoTime();
        long resultPart1 = problem.part1();
        long endPart1 = System.nanoTime();
        System.out.printf("Part 1 result: %d (Time: %.3f seconds)%n", resultPart1, (endPart1 - startPart1) / 1e9);

        long startPart2 = System.nanoTime();
        long resultPart2 = problem.part2();
        long endPart2 = System.nanoTime();
        System.out.printf("Part 2 result: %d (Time: %.3f seconds)%n", resultPart2, (endPart2 - startPart2) / 1e9);

    }
}

