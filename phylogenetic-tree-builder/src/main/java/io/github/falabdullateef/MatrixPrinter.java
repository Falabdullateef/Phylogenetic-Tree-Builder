package io.github.falabdullateef;

import java.util.*;

final class MatrixPrinter {
    private MatrixPrinter() {}

    static void printUpperTriangle(Map<String, Map<String, Double>> distanceMatrix, java.util.function.BiFunction<String,String,Double> distanceFn) {
        List<String> labels = new ArrayList<>(distanceMatrix.keySet());
        Collections.sort(labels);
        System.out.print("\t");
        for (String col : labels) System.out.print(col + "\t");
        System.out.println();
        for (int i = 0; i < labels.size(); i++) {
            String row = labels.get(i);
            System.out.print(row + "\t");
            for (int j = 0; j < labels.size(); j++) {
                if (j < i) {
                    System.out.print("\t");
                } else {
                    double d = distanceFn.apply(row, labels.get(j));
                    System.out.print(String.format(Locale.US, "%.2f\t", d));
                }
            }
            System.out.println();
        }
    }
}
