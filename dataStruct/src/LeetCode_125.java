package src;

public class LeetCode_125 {

    public static void main(String[] args) {
        LeetCode_125 code = new LeetCode_125();
        int[] ints  = new int[]{2,7,11,15};
//        int[] result = code.solution(ints,9);
//        String s = "A man, a plan, a canal: Panama";
        String s = "ab_a";
        boolean result = code.solution(s);
        System.out.println(result);




    }


    public  boolean solution(String s){
        if(s.length()<2) return true;

        int i = 0;
        int j = s.length()-1;
        String lowerS = s.toLowerCase();
        while(i<j){
            if(!belongNumbersOrLetter(lowerS.charAt(i))){
                i++;
            }else if(!belongNumbersOrLetter(lowerS.charAt(j))){
                j--;
            }else if(lowerS.charAt(j)==lowerS.charAt(i)){
                i++;
                j--;
            }else{
                return false;
            }
        }

        return true;
    }


    boolean belongNumbersOrLetter(char  c){
        char charZ= 'z';
        char charA= 'a';
        char char9 = '9';
        char char0 = '0';
        if( c>=charA && c<= charZ)  return true;
        if( c>=char0 && c<= char9)  return true;
        return false;



    }

}
