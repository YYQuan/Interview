package src;

import java.util.HashMap;
import java.util.HashSet;

public class LeetCode_76 {

    public static void main(String[] args) {
        LeetCode_76 code = new LeetCode_76();
        int[] ints  = new int[]{2,7,11,15};
//        int[] result = code.solution(ints,9);
//        String s = "ADOBECODEBANC";
//        String t = "ABC";
        String s = "a";
        String t = "a";
        String result = code.minWindow(s,t);
        System.out.println(result);

    }

    // 解法 1.0  击败 百分之5
    public String minWindow(String s, String t) {

        String  result = "";
        if(s.length()< t.length()) return result;


        HashMap<Character,Integer> tCountMap = new HashMap<>();
        HashMap<Character,Integer> countMap =  new HashMap<>();


        for(int i = 0 ; i<t.length();i++){
            addChar(tCountMap,t.charAt(i));
        }

        int l =0;
        int r =0;

        R:while(r<s.length()){

            while(!containT(countMap,tCountMap)){

                if(r<s.length()) {
                    addChar(countMap, s.charAt(r));
                    r++;
                }else{
                    break R;
                }
            }

            while(containT(countMap,tCountMap)){
                if(result.length()==0||r-l<result.length()){
                    result = s.substring(l,r);
                    if(result.length() == s.length())
                        return result;
                }
                downChar(countMap,s.charAt(l));
                l++;
            }
        }

        return result;
    }

    public void addChar(HashMap<Character,Integer> hashMap , Character c ){
        if(hashMap.containsKey(c)){
            int count = hashMap.get(c)+1;
            hashMap.put(c,count);
        }else{
            hashMap.put(c,1);
        }
    }

    public void downChar(HashMap<Character,Integer> hashMap , Character c ){
        if(hashMap.containsKey(c)){
            int count = hashMap.get(c)-1;
            hashMap.put(c,count);
        }
    }

    public boolean containT(HashMap<Character,Integer> nums1,HashMap<Character,Integer> nums2){
        if(nums1.keySet().size()<nums2.keySet().size())  return false;

        for(Character c : nums2.keySet()){
            if(nums1.get(c)==null||nums1.get(c)<nums2.get(c)){
                return false;
            }
        }
        return true;
    }
}
