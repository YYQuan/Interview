package src;

public class LeetCode_344 {

    public static void main(String[] args) {
        LeetCode_344 code = new LeetCode_344();
        int[] ints  = new int[]{2,7,11,15};
//        int[] result = code.solution(ints,9);
//        String s = "A man, a plan, a canal: Panama";
        char[] s = new char[]{'h','e','l','l','o'};
        code.reverseString(s);
        System.out.println(s);




    }


    public  void reverseString(char[] s){
        if(s.length<2) return ;

        int i = 0;
        int j = s.length-1;

        while(i<j){
            char t  = s[i];
            s[i++] = s[j];
            s[j--] = t;
        }




    }




}
