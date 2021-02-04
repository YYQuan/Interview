package src;

import javafx.util.Pair;

import java.util.*;

public class LeetCode_126 {

    public static void main(String[] args) {
        LeetCode_126 code = new LeetCode_126();

//        int[] ints1 = new int[]{1,2};
        int[] ints1 = new int[]{1,2,2,1};
//        int[] ints1 = new int[]{1,2,3,4};
//        int[] ints1 = new int[]{1,2,3,4,5};
//        int[] ints1 = new int[]{5,1,2,3,4,5,6,7,8};
        TreeNode node4 = new TreeNode(4);
        TreeNode node3 = new TreeNode(3);
        TreeNode node2 = new TreeNode(2);
        node2.left = node3;
        node2.right = node4;
        TreeNode node1 = new TreeNode(1);
        node1.right= node2;

//        String beginWord = "hit", endWord = "cog";
//        String[] wordList = new String[]{"hot","dot","dog","lot","log","cog"};

//        String beginWord = "hit", endWord = "cog";
//        String[] wordList = new String[]{"hot","dot","dog","lot","log"};
//        "hot" "dog" ["hot","cog","dog","tot","hog","hop","pot","dot"]
        String beginWord = "hot", endWord = "dog";
        String[] wordList = new String[]{"hot","cog","dog","tot","hog","hop","pot","dot"};


        List<String>  lists = new ArrayList<String>();
        for(String s :wordList){
            lists.add(s);
        }

//        int result = code.ladderLength(beginWord,endWord,lists);

//        System.out.println(result);
//        System.out.println(code.numSquares2(13));

    }

    // 和 127 进阶 先挂起
//    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
//
//    }


    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

}
