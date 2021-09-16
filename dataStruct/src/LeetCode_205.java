package src;

import java.util.HashMap;

public class LeetCode_205 {

    public static void main(String[] args) {
        LeetCode_205 code = new LeetCode_205();
        String pattern = "foo";
        String s = "bar";
        boolean result = code.isIsomorphic(pattern,s);
        boolean result2 = code.isIsomorphic2(pattern,s);
        System.out.println(result);
        System.out.println(result2);

    }

    public boolean isIsomorphic(String s, String t) {

        if(s.length()!=t.length()) return false;

        HashMap<Character,Character> map = new HashMap<>();

        for(int i =0  ;i<t.length();i++){
            char sChar = s.charAt(i);
            if(!map.keySet().contains(sChar)){
                if(!map.containsValue(t.charAt(i))){
                    map.put(sChar,t.charAt(i));
                }else{
                    return false;
                }
            }else{
                char c = map.get(sChar);
                if(c != t.charAt(i)){
                    return false;
                }
            }


        }

        return true;
    }


    public boolean isIsomorphic2(String s, String t) {

        if(s == null || t == null)  return  false;
        if(s.length() != t.length()) return false;


        HashMap<Character,Character> map = new HashMap<>();

        for(int i = 0  ;i<s.length();i++){
            char c  = s.charAt(i);
            if(map.keySet().contains(c)){
                char tmpChar = map.get(c);
                char ts = t.charAt(i);
                if(tmpChar != ts ){
                    return false;
                }
            }else{
                char tmpChar = t.charAt(i);
                if(map.values().contains(tmpChar)){
                    return false;
                }
                map.put(c,tmpChar);
            }
        }
        return true;
    }
}
