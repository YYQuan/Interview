package src;

import java.util.*;

public class LeetCode_347 {

    public static void main(String[] args) {
        LeetCode_347 code = new LeetCode_347();

        int[] ints = new  int[]{1,1,1,2,2,3};

        int[] result  = code.topKFrequent(ints,2);

        System.out.println(Arrays.toString(result));
    }


    // 击败 86
    public int[] topKFrequent(int[] nums, int k) {
        if(nums==null||nums.length==0||k==0) return new int[]{};

        PriorityQueue<Pair> queue = new PriorityQueue<>();
        HashMap<Integer,Integer> map = new HashMap<>();
        for(int i = 0 ; i<nums.length;i++){

            map.put(nums[i],map.getOrDefault(nums[i],0)+1);

        }

        for(int key:map.keySet()){
            queue.add(new Pair(key,map.get(key)));
        }

        int[] result = new int[k];

        for(int i = 0 ; i<k;i++){
            result[i] = queue.poll().key;
        }
        return result;
    }

    class Pair  implements  Comparable<Pair> {
        int key ;
        int count ;

        public Pair(int key, int count) {
            this.key = key;
            this.count = count;
        }


        @Override
        public int compareTo(Pair o) {

            return o.count-count;
        }
    }

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
