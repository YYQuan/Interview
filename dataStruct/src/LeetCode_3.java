package src;

import java.util.HashMap;
import java.util.HashSet;

public class LeetCode_3 {

    public static void main(String[] args) {
        LeetCode_3 code = new LeetCode_3();
        int[] ints  = new int[]{2,7,11,15};
//        int[] result = code.solution(ints,9);
        String s = "pwwkew";
        int result = code.lengthOfLongestSubstring(s);
        System.out.println(result);

    }


    public int lengthOfLongestSubstring(String s) {

        HashSet<Character>  characterHashSet = new HashSet<>();


        int l =0;
        int r = 0;
        int length = 0;
        while(l<s.length()){

            // r 到第一个  满足条件的最大index处 直到 r == s.length
            // 让[l,r-1] 满足条件
            while(r<s.length()&& !characterHashSet.contains(s.charAt(r))){
                characterHashSet.add(s.charAt(r));
                length = Math.max(length,characterHashSet.size());
                r++;
            }


            // 移动l  从新让 [l,r] 满足条件
            while(l<r){
                if(r<s.length()&&s.charAt(l) != s.charAt(r)) {
                    characterHashSet.remove(s.charAt(l));
                    l++;
                }else{
                    characterHashSet.remove(s.charAt(l));
                    l++;
                    break;
                }
            }




        }
        return  length;



    }

}
