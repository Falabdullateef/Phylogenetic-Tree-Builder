package io.github.falabdullateef;

import java.util.List;

final class DistanceCalculators {
    private DistanceCalculators() {}

    static double hammingDNA(String seq1, String seq2) {
        double distance = 0;
        for (int i = 0; i < seq1.length(); i++) {
            if (seq1.charAt(i) != seq2.charAt(i)) distance++;
        }
        return distance;
    }

    static double hammingBinary(String v1, String v2) {
        double dist = 0;
        for (int i = 0; i < v1.length(); i++) {
            if (v1.charAt(i) != v2.charAt(i)) dist++;
        }
        return dist;
    }
}
