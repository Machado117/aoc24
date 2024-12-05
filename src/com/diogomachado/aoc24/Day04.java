package com.diogomachado.aoc24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Day04 {

    private char[][] wordSearchTable;
    private int rows;
    private int columns;

    private record Direction(int x, int y) {};

    private List<Direction> allDirections = List.of(new Direction(-1,-1), new Direction(-1,0), new Direction(-1,1),
                                                    new Direction(0,-1),                       new Direction(0,1),
                                                    new Direction(1,-1),  new Direction(1,0),  new Direction(1,1));

    private List<Direction> downDiagonal = List.of(new Direction(-1,-1),
                                                                        new Direction(1,1));


    private List<Direction> upDiagonal = List.of(                     new Direction(-1,1),
                                                 new Direction(1,-1));

    private Day04(final String input) {
        readInput(input);
    }

    private void readInput(final String path) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {
            List<String> wordSearch = reader.lines().toList();
            rows = wordSearch.size();
            columns = wordSearch.getFirst().length();
            wordSearchTable = new char[rows][columns];
            for (int i=0; i< rows; i++){
                String line = wordSearch.get(i);
                wordSearchTable[i] = line.toCharArray();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkDiagonal(final int x, final int y, final List<Direction> direction) {
        char letter1 = wordSearchTable[x + direction.get(0).x][y + direction.get(0).y];
        char letter2 = wordSearchTable[x + direction.get(1).x][y + direction.get(1).y];

        return letter1 == 'M' && letter2 == 'S' || letter1 == 'S' && letter2 == 'M';
    }

    private boolean checkLine(final int x, final int y, final Direction direction) {
        int xLast = x + direction.x() * 3;
        int yLast = y + direction.y() * 3;

        if (xLast >= 0 && xLast < rows && yLast >= 0 && yLast < columns) {
            return wordSearchTable[x + direction.x()][y + direction.y()] == 'M'
                    && wordSearchTable[x + direction.x() * 2][y + direction.y() * 2] == 'A'
                    && wordSearchTable[x + direction.x() * 3][y + direction.y() * 3] == 'S';
        } else {
            return false;
        }
    }

    private long part1() {
        int count = 0;
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                if (wordSearchTable[x][y] == 'X') {
                    for (Direction direction : allDirections) {
                        if (checkLine(x, y, direction)) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    private long part2() {
        int count = 0;
        for (int x = 1; x < rows - 1; x++) {
            for (int y = 1; y < columns - 1; y++) {
                if (wordSearchTable[x][y] == 'A') {
                    if (checkDiagonal(x, y, downDiagonal) && checkDiagonal(x, y, upDiagonal)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        Day04 problem = new Day04("input/day04.txt");

        System.out.println(problem.part1());
        System.out.println(problem.part2());
    }
}
