package com.diogomachado.aoc24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.LongStream;

public class Day09 {

    private static class ContiguousBlocks {
        int index;
        int length;

        ContiguousBlocks(int index, int length) {
            this.index = index;
            this.length = length;
        }
    }

    private int[] disk;

    private Day09(final String input) {
        readInput(input);
    }

    private void readInput(final String path) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {
            disk = reader.readLine().codePoints().map(Character::getNumericValue).toArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long part1() {
        List<Integer> expanded = new ArrayList<>();
        for (int i = 0; i < disk.length; i++) {
            expanded.addAll(Collections.nCopies(disk[i], i % 2 == 0 ? i / 2 : null));
        }

        for (int i = 0; i < expanded.size(); i++) {
            if (expanded.get(i) == null) {
                expanded.set(i, expanded.removeLast());
                while (expanded.getLast() == null) {
                    expanded.removeLast();
                }
            }
        }
        return checksum(expanded);
    }

    private static long checksum(List<Integer> expanded) {
        return LongStream.range(0, expanded.size())
                .filter(i -> expanded.get((int)i) != null)
                .reduce(0L, (acc, i) -> acc + i * expanded.get((int)i));
    }

    private long part2() {
        List<Integer> expanded = new ArrayList<>();
        List<ContiguousBlocks> freeSpace = new ArrayList<>();
        List<ContiguousBlocks> files = new ArrayList<>();
        for (int i = 0; i < disk.length; i++) {
            ContiguousBlocks blocks = new ContiguousBlocks(expanded.size(), disk[i]);
            if (i % 2 == 0) {
                files.add(blocks);
                expanded.addAll(Collections.nCopies(disk[i], i/2));
            } else {
                freeSpace.add(blocks);
                expanded.addAll(Collections.nCopies(disk[i], null));
            }
        }

        for(int i = files.size() - 1; i >= 0; i--) {
            ContiguousBlocks file = files.get(i);
            if (freeSpace.getFirst().index > file.index) {
                break;
            }
            Iterator<ContiguousBlocks> iterator = freeSpace.iterator();
            while (iterator.hasNext()) {
                ContiguousBlocks free = iterator.next();
                if (free.index + free.length > file.index) {
                    break;
                }
                if (file.length <= free.length) {
                    Collections.fill(expanded.subList(free.index, free.index + file.length), i);
                    Collections.fill(expanded.subList(file.index, file.index + file.length), null);
                    file.index = free.index;
                    free.length = free.length - file.length;
                    if (free.length == 0) {
                        iterator.remove();
                    }
                    free.index += file.length;
                    break;
                }
            }
        }
        return checksum(expanded);
    }

    public static void main(String[] args) {

        Day09 problem = new Day09("input/day09.txt");

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
