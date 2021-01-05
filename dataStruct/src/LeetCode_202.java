package src;

import java.util.HashSet;
import java.util.LinkedList;

public class LeetCode_202 {

    public static void main(String[] args) {
        LeetCode_202 code = new LeetCode_202();
        int  n = 19;
        boolean result = code.isHappy(n);
        System.out.println(result);

    }

    public boolean isHappy(int n) {


        int  p = n;
        HashSet<Integer>  sets = new HashSet<>();
        sets.add(n);
        while(p!=1){
            LinkedList<Integer> list = parseN(p);
            p = parseList(list);
            if(sets.contains(p)) return false;
            sets.add(p);
        }

        return true;
    }

    public int parseList(LinkedList<Integer> list){
        int result = 0 ;
        for(int i :list){

            result += i*i;
        }
        return result;
    }




    public LinkedList<Integer> parseN(int n ){

        LinkedList<Integer> list = new LinkedList<>();
        int tmp = n ;
        while(tmp>=10){
            list.addFirst(tmp%10);
            tmp/=10;
        }
        list.addFirst(tmp);
        return list;
    }
}
