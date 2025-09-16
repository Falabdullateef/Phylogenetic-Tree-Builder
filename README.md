# Phylogenetic Tree Builder

A simple interactive Java (Maven) CLI tool that constructs a phylogenetic clustering tree from:

1. DNA sequences (all equal length) – distances = raw Hamming distance (number of differing sites)
2. A user-entered distance matrix
3. Binary presence/absence character matrix (0/1) – distances = Hamming distance across characters

The program supports UPGMA (size‑weighted) and WPGMA (unweighted) average linkage clustering and prints the resulting rooted tree as an ASCII outline.

Branch lengths are now shown: each child line includes a square‑bracketed length `[len]` representing the distance from its parent (UPGMA height difference, WPGMA treated analogously). Internal node labels remain concatenations like `(A,B)`.

## Background & Mathematics

### Hamming Distance

For two equal-length sequences $x$ and $y$ of length $L$:

```math
d_H(x,y) = \sum_{i=1}^{L} [x_i \ne y_i]
```

where $[\cdot]$ is 1 if the condition is true, else 0. (See: Hamming distance — Wikipedia.)

### Cluster Distances (Average Linkage)

Let clusters $A$ and $B$ have sizes $|A|, |B|$ and pairwise distances $d(a,b)$ for $a\in A, b\in B$.

The average (mean) inter-cluster distance is:

```math
d(A,B) = \frac{1}{|A|\,|B|} \sum_{a\in A}\sum_{b\in B} d(a,b)
```

### UPGMA Update Rule

When merging clusters $A$ and $B$ into $C = A \cup B$, for any other cluster $X$:

```math
d(C,X) = \frac{|A|\,d(A,X) + |B|\,d(B,X)}{|A| + |B|}
```

This produces an ultrametric tree if the original distances are ultrametric (molecular clock assumption). (UPGMA — Wikipedia.)

### WPGMA Update Rule

Weighted Pair Group Method with Arithmetic Mean (historically "weighted" refers to equal cluster weights):

```math
d(C,X) = \frac{d(A,X) + d(B,X)}{2}
```

All clusters contribute equally regardless of size. (WPGMA — Wikipedia.)

### (Optional) Node Heights / Branch Lengths

If storing heights for UPGMA you can set the height of a new internal node $C$ as:

```math
h(C) = \frac{d(A,B)}{2}
```

Then branch length to child $A$ is $h(C) - h(A)$ (with leaves at height 0). Current implementation omits this; adding it would allow Newick export with branch lengths.

### Complexity

Naïve implementation (no priority queue) performs $O(n^2)$ distance lookups each of $O(n)$ merges: overall $O(n^3)$ for $n$ initial taxa. For small teaching datasets this is fine.

### Terminology

- UPGMA: Unweighted Pair Group Method with Arithmetic Mean (size-weighted average).
- WPGMA: Weighted Pair Group Method with Arithmetic Mean (equal cluster weights).
- These are agglomerative hierarchical clustering methods. (See: Hierarchical clustering — Wikipedia.)

### References

- Hamming distance: https://en.wikipedia.org/wiki/Hamming_distance
- UPGMA: https://en.wikipedia.org/wiki/UPGMA
- WPGMA (average linkage variants): covered under hierarchical clustering: https://en.wikipedia.org/wiki/Hierarchical_clustering
- Newick format: https://en.wikipedia.org/wiki/Newick_format

## Features

- Multiple input modalities (sequences, explicit matrix, binary traits)
- Automatic distance matrix construction for sequence / binary data
- Choice between UPGMA and WPGMA
- Optional display of the evolving distance matrix after each merge (each matrix followed by a separator line `--------` for readability)
- Auto‑generate species labels (a, b, c, …) if you prefer not to type names
- Simple ASCII tree output with branch lengths
- Minimal, dependency‑free core (pure Java)

## How It Works

1. Collect taxa (species) names (optionally auto‑generate a, b, c, …).
2. Depending on mode:
   - DNA: read all sequences (validated for equal length) and compute pairwise Hamming distances.
   - Distance matrix: user supplies upper triangle; matrix is symmetrized.
   - Binary traits: read fixed-length 0/1 patterns and compute Hamming distances.
3. Repeatedly find the closest pair of clusters.
4. Merge them into a new cluster `(X,Y)` updating distances by either:
   - UPGMA: weighted average by cluster sizes
   - WPGMA: simple (unweighted) average
5. Continue until a single cluster remains; print tree.

## Project Structure

```
phylogenetic-tree-builder/
├── pom.xml
├── README.md
└── src/main/java/io/github/falabdullateef/
    ├── Main.java               # CLI orchestration & clustering loop
    ├── DistanceCalculators.java# Hamming distance helpers
    ├── MatrixOps.java          # Distance lookup + symmetrization
    ├── MatrixPrinter.java      # (Printed during runs if showMatrices=true)
    ├── TreeNode.java           # Simple binary tree node
    └── TreePrinter.java        # ASCII tree output
```

## Requirements

- Java 17+ (adjust in `pom.xml` if needed)
- Maven 3.8+

## Build

```bash
mvn -q clean package
```

Produces a runnable jar in `target/` (e.g., `phylogenetic-tree-builder-1.0-SNAPSHOT.jar`).

## Run

From the project root:

```bash
mvn -q exec:java -Dexec.mainClass=io.github.falabdullateef.Main
```

Or using the jar (replace the version with your built version):

```bash
java -cp target/phylogenetic-tree-builder-1.0-SNAPSHOT.jar io.github.falabdullateef.Main
```

## Example Session (DNA Mode)

```
What do you have?
1. DNA sequence
2. Distance matrix
3. Binary presence/absence (0/1) characters
Enter your choice (1/2/3): 1
Use UPGMA (size-weighted) or WPGMA (unweighted)? Enter 'u' or 'w': u
Show distance matrix after each clustering step? (y/n): y
Enter the number of species: 3
Enter name for species 1: A
Enter name for species 2: B
Enter name for species 3: C
Enter sequence for A: ACTG
Enter sequence for B: ACTA
Enter sequence for C: AGTA
Initial distance matrix:
... (matrix output) ...
+-- ((A,B),C)
    +-- (A,B)
        +-- A
        +-- B
    +-- C
```

## Extending

- Add new distance metrics: implement additional static methods in `DistanceCalculators`.
- Export formats: traverse final tree to output Newick (e.g., `(A,B,(C,D));`) including branch lengths.
- Add NJ (Neighbor Joining): integrate a different reduction formula and Q-matrix.

## Design Notes / Assumptions

- Distances are treated as additive but no ultrametric enforcement besides UPGMA's logic.
- Input validation is minimal—non-numeric matrix entries will throw a runtime exception.
- Hamming distance is returned as a raw count (not normalized). You can divide by sequence length if you prefer proportional distance.
- Internal node naming `(X,Y)` is purely descriptive and not guaranteed unique if the same pair text recurs; for this binary agglomerative process it's fine.

## Potential Improvements

- Normalize DNA / binary distances to [0,1]
- Support reading FASTA files
- Show branch lengths proportional to distance
- Add unit tests for matrix updates
- Add Neighbor Joining algorithm

## License

Licensed under the MIT License. See the `LICENSE` file for details.

## Attribution

Created by @falabdullateef.

---

Feel free to open issues or PRs with enhancements.
