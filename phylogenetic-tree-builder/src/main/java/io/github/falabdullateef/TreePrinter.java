package io.github.falabdullateef;

final class TreePrinter {
    private TreePrinter() {}

    static void print(TreeNode node, String prefix, boolean isLast) {
        print(node, prefix, isLast, node != null ? node.height : 0.0);
    }

    private static void print(TreeNode node, String prefix, boolean isLast, double parentHeight) {
        if (node == null) return;
        double branchLen = parentHeight - node.height; // for root parentHeight == node.height -> 0
        String label = node.name;
        // Show branch length except for root
        String decorated = label + (parentHeight == node.height ? "" : String.format(" [%.3f]", branchLen));
        System.out.println(prefix + (isLast ? "+-- " : "|-- ") + decorated);
        String newPrefix = prefix + (isLast ? "    " : "|   ");
        if (node.left != null) print(node.left, newPrefix, node.right == null, node.height);
        if (node.right != null) print(node.right, newPrefix, true, node.height);
    }
}
