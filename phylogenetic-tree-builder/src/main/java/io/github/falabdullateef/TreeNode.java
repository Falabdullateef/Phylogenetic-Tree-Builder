package io.github.falabdullateef;

class TreeNode {
    String name;
    TreeNode left;
    TreeNode right;
    double height; // distance from leaves (leaves = 0). For UPGMA: internal node height = d(children)/2

    TreeNode(String name) {
        this.name = name;
        this.height = 0.0;
    }
}
