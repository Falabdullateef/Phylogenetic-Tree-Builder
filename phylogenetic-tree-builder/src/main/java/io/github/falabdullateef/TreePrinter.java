package io.github.falabdullateef;

final class TreePrinter {
    private TreePrinter() {}

    static void print(TreeNode node, String prefix, boolean isLast) {
        if (node == null) return;
        System.out.println(prefix + (isLast ? "+-- " : "|-- ") + node.name);
        String newPrefix = prefix + (isLast ? "    " : "|   ");
        if (node.left != null) print(node.left, newPrefix, node.right == null);
        if (node.right != null) print(node.right, newPrefix, true);
    }
}
