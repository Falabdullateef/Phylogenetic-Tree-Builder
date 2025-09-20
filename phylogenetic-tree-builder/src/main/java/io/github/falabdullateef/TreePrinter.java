package io.github.falabdullateef;

final class TreePrinter {
    private TreePrinter() {}

    static void print(TreeNode node, String prefix, boolean isLast) {
        // Precompute the maximum branch length in the tree to scale ASCII connectors proportionally
        double rootHeight = node != null ? node.height : 0.0;
        double maxBranch = maxBranchLen(node, rootHeight);
        print(node, prefix, isLast, rootHeight, maxBranch);
    }

    private static void print(TreeNode node, String prefix, boolean isLast, double parentHeight, double maxBranchLen) {
        if (node == null) return;
        double branchLen = parentHeight - node.height; // for root parentHeight == node.height -> 0
        String label = node.name;
        // Show branch length except for root
        String decorated = label + (parentHeight == node.height ? "" : String.format(" [%.3f]", branchLen));

        // Scale the horizontal connector length in proportion to branch length
        final int MAX_DASH = 20; // visual width cap for the longest branch
        int dashCount = 0;
        if (branchLen > 0 && maxBranchLen > 1e-12) {
            dashCount = Math.max(1, (int)Math.round((branchLen / maxBranchLen) * MAX_DASH));
        }

        String branchStart = isLast ? "+" : "|";
        String dashes = repeat("-", dashCount + 2); // always at least "--" for visibility
        System.out.println(prefix + branchStart + dashes + " " + decorated);

        String newPrefix = prefix + (isLast ? "    " : "|   ");
        if (node.left != null) print(node.left, newPrefix, node.right == null, node.height, maxBranchLen);
        if (node.right != null) print(node.right, newPrefix, true, node.height, maxBranchLen);
    }

    private static String repeat(String s, int n) {
        if (n <= 0) return "";
        StringBuilder sb = new StringBuilder(s.length() * n);
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }

    private static double maxBranchLen(TreeNode node, double parentHeight) {
        if (node == null) return 0.0;
        double here = Math.max(0.0, parentHeight - node.height);
        double left = maxBranchLen(node.left, node.height);
        double right = maxBranchLen(node.right, node.height);
        return Math.max(here, Math.max(left, right));
    }
}
