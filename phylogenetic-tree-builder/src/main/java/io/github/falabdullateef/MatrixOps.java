package io.github.falabdullateef;

import java.util.*;

final class MatrixOps {
    private MatrixOps() {}

    static double getDistance(Map<String, Map<String, Double>> matrix, String a, String b) {
        if (a.equals(b)) return 0.0;
        Map<String, Double> rowA = matrix.get(a);
        if (rowA != null && rowA.containsKey(b)) return rowA.get(b);
        Map<String, Double> rowB = matrix.get(b);
        if (rowB != null && rowB.containsKey(a)) return rowB.get(a);
        return Double.POSITIVE_INFINITY;
    }

    static void symmetrize(Map<String, Map<String, Double>> matrix) {
        List<String> labels = new ArrayList<>(matrix.keySet());
        for (String i : labels) matrix.computeIfAbsent(i, k -> new HashMap<>()).put(i, 0.0);
        for (String i : labels) {
            for (String j : labels) {
                if (i.equals(j)) continue;
                Map<String, Double> rowI = matrix.get(i);
                Double dij = rowI != null ? rowI.get(j) : null;
                Map<String, Double> rowJ = matrix.get(j);
                Double dji = rowJ != null ? rowJ.get(i) : null;
                if (dij == null && dji != null) {
                    matrix.get(i).put(j, dji);
                } else if (dji == null && dij != null) {
                    matrix.get(j).put(i, dij);
                } else if (dij != null && dji != null && Math.abs(dij - dji) > 1e-9) {
                    double avg = (dij + dji) / 2.0;
                    matrix.get(i).put(j, avg);
                    matrix.get(j).put(i, avg);
                }
            }
        }
    }
}
