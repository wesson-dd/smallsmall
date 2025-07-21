package com.small.leetcode.yourself;

import lombok.Data;

import java.util.HashMap;

/**
 * 输入一个链表的头节点，从尾到头反过来返回每个节点的值（用数组返回）。
 * <p>
 * 示例 1：
 * 输入：head = [1,3,2]
 * 输出：[2,3,1]
 *
 * @author wesson
 * Created on 2024/9/15 21:18
 **/
public class TreeTest {
    HashMap<Integer, Integer> interIndexMap = new HashMap<>();
    int[] preSort;
    int[] interSort;

    public static void main(String[] args) {
        TreeTest treeTest = new TreeTest();
        TreeNode originTree = treeTest.getOriginTree(new int[]{3, 9, 20, 15, 7}, new int[]{9, 3, 15, 20, 7});


        treeTest.getValue(originTree);
    }


    public void getValue(TreeNode treeNode) {
        if (treeNode != null) {
            System.out.println(treeNode.value);
            getValue(treeNode.left);
            getValue(treeNode.right);
        }
    }

    /**
     * 输入某二叉树的前序遍历和中序遍历的结果，请构建该二叉树并返回其根节点。
     * 假设输入的前序遍历和中序遍历的结果中都不含重复的数字。
     * <p>
     * Input: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
     * Output: [3,9,20,null,null,15,7]
     *
     * @param preSort
     * @param interSort
     * @return
     */
    public TreeNode getOriginTree(int[] preSort, int[] interSort) {
        if (preSort == null || preSort.length == 0) {
            return null;
        }


        this.preSort = preSort;
        this.interSort = interSort;
        int size = preSort.length;

        for (int i = 0; i < size; i++) {
            interIndexMap.put(interSort[i], i);
        }


        return buildTree(0, size - 1, 0, size - 1);

    }

    private TreeNode buildTree(int preStart, int preEnd, int rightStart, int rightEnd) {
        if (preStart > preEnd) {
            return null;
        }
        int rootValue = preSort[preStart];
        TreeNode root = new TreeNode(rootValue);
        int index = interIndexMap.get(rootValue);
        int leftLen = index - rightStart;

        root.setLeft(buildTree(preStart + 1, preStart + leftLen, rightStart, index - 1));
        root.setRight(buildTree(preStart + 1 + leftLen, preEnd, index + 1, rightEnd));

        return root;

    }

    @Data
    public static class TreeNode {
        private int value;
        private TreeNode left;
        private TreeNode right;

        public TreeNode(int value) {
            this.value = value;
        }
    }
}
