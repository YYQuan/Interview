package src;

import java.util.*;

public class LeetCodeTest {
    
    public static void main(String[] args) throws InterruptedException {
        LeetCodeTest code = new LeetCodeTest();
//        int[] ints  = new int[]{1,8,6,2,5,4,8,3,7};
        int[] ints  = new int[]{1,1};
//        int[] result = code.solution(ints,9);
//        String s = "A man, a plan, a canal: Panama";
        String s = "ab_a";
//        int result = code.solution(ints);
        int result = code.maxArea(ints);
        System.out.println(result);

    }

    public int maxArea(int[] height) {

        int  result = 0 ;
        for(int i = 0 ,j=height.length-1 ;i<j ;){
            int tmp = Math.min(height[i], height[j]) * (j-i);
            result = Math.max(tmp ,result);

            if(height[i]<=height[j]){
                int h = height[i];
                while(i<=j&&height[i]<=h){
                    i++;
                }
            }else{
                int h = height[j];
                while(i<=j&&height[j]<=h){
                    j--;
                }
            }
        }
        return result;

    }
    public static class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
}
