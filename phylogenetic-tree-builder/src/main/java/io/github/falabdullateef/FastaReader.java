package io.github.falabdullateef;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

final class FastaReader {
    private FastaReader() {}

    static class Entry {
        final String name;
        final String sequence;
        Entry(String name, String sequence) {
            this.name = name;
            this.sequence = sequence;
        }
    }

    /**
     * Parse a FASTA file into a list of entries preserving order. Sequences are uppercased and whitespace removed.
     * Header line expected to start with '>' followed by the sequence name (first token until whitespace).
     */
    static List<Entry> parse(String path) throws IOException {
        List<Entry> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            String currentName = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.startsWith(">")) {
                    // flush previous
                    if (currentName != null) {
                        String seq = sb.toString().replaceAll("\\s+", "").toUpperCase(Locale.ROOT);
                        if (!seq.isEmpty()) result.add(new Entry(currentName, seq));
                        sb.setLength(0);
                    }
                    // get name = first token after '>'
                    String header = line.substring(1).trim();
                    String name = header.isEmpty() ? ("unnamed_" + (result.size()+1)) : header.split("\\s+")[0];
                    currentName = name;
                } else {
                    sb.append(line.trim());
                }
            }
            if (currentName != null) {
                String seq = sb.toString().replaceAll("\\s+", "").toUpperCase(Locale.ROOT);
                if (!seq.isEmpty()) result.add(new Entry(currentName, seq));
            }
        }
        return result;
    }

    /** Ensure all sequences equal length; throw if not. */
    static void assertEqualLengths(List<Entry> entries) {
        if (entries.isEmpty()) return;
        int L = entries.get(0).sequence.length();
        for (Entry e : entries) {
            if (e.sequence.length() != L) {
                throw new IllegalArgumentException("FASTA sequences must be equal length; got " + e.name + " with L=" + e.sequence.length() + " vs first L=" + L);
            }
        }
    }

    /** Return a species array and sequences list from FASTA entries, deduping names if necessary. */
    static Object[] toArrays(List<Entry> entries) {
        // dedupe names preserving order
        Map<String, Integer> seen = new LinkedHashMap<>();
        String[] names = new String[entries.size()];
        List<String> seqs = new ArrayList<>(entries.size());
        for (int i = 0; i < entries.size(); i++) {
            Entry e = entries.get(i);
            int count = seen.getOrDefault(e.name, 0) + 1;
            seen.put(e.name, count);
            String name = e.name;
            if (count > 1) name = e.name + "_" + count; // disambiguate duplicates
            names[i] = name;
            seqs.add(e.sequence);
        }
        return new Object[] { names, seqs };
    }
}
