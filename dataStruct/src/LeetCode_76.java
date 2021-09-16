package src;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class LeetCode_76 {

    public static void main(String[] args) {
        LeetCode_76 code = new LeetCode_76();
        int[] ints  = new int[]{2,7,11,15};
//        int[] result = code.solution(ints,9);
        String s = "ADOBECODEBANC";
        String t = "ABC";
//        String s = "a";
//        String t = "aa";
        String result = code.minWindow(s,t);
        String result2 = code.minWindow3(s,t);
//        String result2 = code.minWindow2(s,t);
        System.out.println(result);
        System.out.println(result2);

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


    // 1  用map  记录下
    public String minWindow2(String s, String t) {

        if(s == null || t==null) return "";

        List<Character> cSets = t.chars().mapToObj(i -> (char) i).collect(Collectors.toList());
        //HashSet<Character> cSets = new HashSet<>(cList);


        int resultL =-1;
        int resultR =-1;
        int min =Integer.MAX_VALUE;
        int l =0;
        int r =-1;
        while(l< s.length()){

            while(l< s.length()&&!t.contains(s.charAt(l)+"")){
                l++;
            }
            if(l<s.length()) {
                cSets.remove((Object)s.charAt(l));
            }else{
                break;
            }

            if(r==-1) {
                r = l + 1;
            }else{

            }

            if(min<Integer.MAX_VALUE&&cSets.size()==1){
                String subStr = s.substring(l,r);
                if(subStr.contains(cSets.get(0)+"")){
                    cSets.remove(0);
                }
            }
            while(r<s.length()&&!cSets.isEmpty()){

                if(cSets.contains(s.charAt(r))){
                    cSets.remove((Object)s.charAt(r));
                }
                r++;
            }


            if(cSets.isEmpty()){

                System.out.println(String.format("l %d  r %d  resultL %d  resultR %d",l,r,resultL,resultR));
                if(min>(r-l)) {
                    min = r - l;
                    resultL = l;
                    resultR = r;
                }else{

                }
                cSets.add(s.charAt(l));
                l++;
            }else {

                if(resultL == -1){
                    return "";
                }else{
                    return s.substring(resultL, resultR);
                }
            }

        }

        if(resultL == -1){
            return "";
        }else {
            return s.substring(resultL, resultR);
        }


    }
//
// 1  用数组  记录下  字符出现的次数， 次数的信息是必要的，
//    重点是记录下 当前 字符串的状态， 以及增减 一个字符时 的变化。
    public String minWindow3(String s, String t) {

        int tCount[] = new int[52];
        int tmpCount[] = new int[52];
        Arrays.fill(tCount ,0);
        Arrays.fill(tmpCount ,0);
        for(char c :t.toCharArray()){
            putChar2Arrays(tCount,c);
        }

        int l = 0,r = 0;
        int resultL = 0, resultR = 0,min = Integer.MAX_VALUE;
        while(l<s.length()){

            while(r<s.length()&&!isLegal(tmpCount,tCount)){
                putChar2Arrays(tmpCount,s.charAt(r));
                r++;
            }


            if(isLegal(tmpCount,tCount)){

                if(min>(r-l)){
                    min = r-l;
                    resultL = l ;
                    resultR = r;
                }
            }

            removeChar2Arrays(tmpCount,s.charAt(l));
            l++;

        }


        return  s.substring(resultL,resultR);




    }

    void putChar2Arrays(int[] ints ,char c){
        if(c>='a'&&c<='z'){
            ints[c-'a']++;
        }else if(c >='A'&&c<='Z'){
            ints[c-'A'+26]++;
        }
    }

    void removeChar2Arrays(int[] ints ,char c){
        if(c>='a'&&c<='z'){
            ints[c-'a'] = Math.max(ints[c-'a']-1,0);
        }else if(c >='A'&&c<='Z'){
            ints[c-'A'+26] = Math.max(ints[c-'A'+26]-1,0);
        }
    }

    boolean  isLegal(int[] ints1 ,int[] ints2){

        if(ints2.length!=ints1.length) return false;
        for(int i = 0 ; i<ints1.length;i++){
            if(ints2[i]>0&&ints1[i]<ints2[i]){
                return false;
            }
        }
        return true;
    }

}
