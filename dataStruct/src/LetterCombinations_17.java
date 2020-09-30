import java.util.ArrayList;
import java.util.List;

public class LetterCombinations_17 {

    public static void main(String[] args) {

        System.out.println("Hello World!");
        LetterCombinations_17  solution = new LetterCombinations_17();
        System.out.println(solution.letterCombinations("23").toString());

    }

    private String letterMap[] = {
            " ",    //0
            "",     //1
            "abc",  //2
            "def",  //3
            "ghi",  //4
            "jkl",  //5
            "mno",  //6
            "pqrs", //7
            "tuv",  //8
            "wxyz"  //9
    };

    private ArrayList<String> res;

    public List<String> letterCombinations(String digits) {
        res = new ArrayList();
        if(digits ==null) return res;

        findCombination(digits,0,"");
        return res;
    }


    private void findCombination(String digits, int index, String s){
//        if(!s.isEmpty()){
//            res.add(s);
//        }
        if(index == digits.length()) {
            res.add(s);
            return ;
        }

        char c = digits.charAt(index);
        String  tmpS = letterMap[c-'0'];
        for(int i = 0 ; i< tmpS.length();i++){
            findCombination(digits,index+1,s+tmpS.charAt(i));
        }
        return ;
    }
}
