package src;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class LeetCode_451 {

    public static void main(String[] args) {
        LeetCode_451 code = new LeetCode_451();
        String s = "tree";
        String result = code.frequencySort(s);
        System.out.println(result);

    }


    public String frequencySort(String s) {

        HashMap<Character,Integer>  map = new HashMap<Character,Integer>();
        for(int i =0; i<s.length();i++){
            char c = s.charAt(i);
            if(map.keySet().contains(c)){
                int count = map.get(c)+1;
                map.put(c,count);
            }else{
                map.put(c,1);
            }
        }

        StringBuilder builder = new StringBuilder();

        Integer[]      countArray= new Integer[map.values().size()];
        int index= 0;

        Character[] countCharArray = new Character[map.keySet().size()];
        index = 0 ;
        for(Character c :map.keySet()){
            countCharArray[index] = c;
            countArray[index] =  map.get(c);
            index++;
        }
        for(int i = countArray.length-1 ; i>=0;i--) {

            for(int j = 0 ;j<i;j++){
                if(countArray[j]>countArray[i]){
                    swap(countArray,j,i);
                    swap(countCharArray,j,i);

                }
            }
            int count =countArray[i];
            while(count>0){
                builder.append(countCharArray[i]);
                count--;
            }
        }

        return builder.toString();


    }

    public void swap(Comparable[] nums ,int l ,int r){
        Comparable integer = nums[l];
        nums[l] = nums[r];
        nums[r] = integer;
    }
}
