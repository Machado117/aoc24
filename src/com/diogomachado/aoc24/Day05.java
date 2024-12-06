package com.diogomachado.aoc24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Day05 {

    private final Map<Integer, Set<Integer>> orderingRules = new HashMap<>();
    private final List<List<Integer>> updates = new ArrayList<>();

    private Day05(final String input) {
        readInput(input);
    }

    private void readInput(final String path) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                String[] rule = line.split("\\|");
                Integer leftPage = Integer.parseInt(rule[0]);
                Integer rightPage = Integer.parseInt(rule[1]);
                final Set<Integer> precedences = orderingRules.computeIfAbsent(leftPage, k -> new HashSet<>());
                precedences.add(rightPage);
            }
            while ((line = reader.readLine()) != null) {
                String[] update = line.split(",");
                updates.add(Arrays.stream(update).map(Integer::parseInt).toList());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long part1() {
        long middleSum = 0;
        for (List<Integer> update : updates) {
            List<Integer> verifiedPages = new ArrayList<>();
            boolean validUpdate = true;
            for (Integer page : update) {
                Set<Integer> pageConstraints = orderingRules.get(page);
                final Optional<Integer> any = verifiedPages.stream()
                                                           .filter(precedingPage -> pageConstraints != null
                                                                   && pageConstraints.contains(precedingPage))
                                                           .findAny();
                if (any.isPresent()) {
                    validUpdate = false;
                    break;
                } else {
                    verifiedPages.add(page);
                }
            }
            if (validUpdate) {
                middleSum += update.get(update.size() / 2);
            }
        }
        return middleSum;
    }

    private long part2() {
        long middleSum = 0;
        for (List<Integer> update : updates) {
            List<Integer> verifiedPages = new LinkedList<>();
            boolean outOfOrderUpdate = false;
            for (Integer page : update) {
                boolean outOfOrderPage = false;
                Set<Integer> pageConstraints = orderingRules.get(page);
                if (pageConstraints != null) {
                    final ListIterator<Integer> iterator = verifiedPages.listIterator();
                    while (iterator.hasNext()) {
                        final Integer next = iterator.next();
                        if (pageConstraints.contains(next)) {
                            outOfOrderPage = true;
                            iterator.set(page);
                            iterator.add(next);
                            break;
                        }
                    }
                }
                if (outOfOrderPage) {
                    outOfOrderUpdate = true;
                } else {
                    verifiedPages.add(page);
                }
            }
            if (outOfOrderUpdate) {
                middleSum += verifiedPages.get(update.size() / 2);
            }
        }
        return middleSum;
    }

    public static void main(String[] args) {

        Day05 problem = new Day05("input/day05.txt");

        System.out.println(problem.part1());
        System.out.println(problem.part2());
    }
}
