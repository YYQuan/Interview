package src;

import java.util.Stack;

public class LeetCode_20 {

    public static void main(String[] args) {
        LeetCode_20 code = new LeetCode_20();
//        String s = "()(";
        String s = "()[]{}";
//        String s = "[";
//        String s = "){";
        boolean result = code.isValid(s);
        boolean result2 = code.isValid2(s);
        System.out.println(result);
        System.out.println(result2);

    }


    public boolean isValid(String s) {
        if(s==null||s.length()==1) return false;

        Stack<Character> stack = new Stack<>();

        for(int i = 0 ; i<s.length();i++){
            char c = s.charAt(i);
            if(isLeftSign(c)){
                stack.push(c);
            }else if(isRightSign(c)){

                if(stack.empty()) return false;
                char popChar = stack.pop();
                if(!isPair(popChar,c)){
                    return false;
                }

            }else{
                throw new IllegalArgumentException();
            }


        }
        return stack.empty();
    }

    private boolean isPair(char c,char c2){
        return  (c == '('&&c2 ==')')||(c =='['&&c2==']')||(c =='{'&&c2 == '}');
    }

    private boolean isLeftSign(char c){

        return (c =='('||c=='['||c=='{');
    }

    private boolean isRightSign(char c){
        return (c ==')'||c==']'||c=='}');
    }


    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

    }


    public boolean isValid2(String s) {

        if(s == null ) return  false;
        Stack<Character>  cStack = new Stack<>();

        for(char c :s.toCharArray()){

            if(isLeftSign2(c)){
                cStack.push(c);
            }else if(isRightSign2(c)){
                if(cStack.isEmpty()){
                    return false;
                }else {
                    char  cPop = cStack.pop();
                    if(!isAdapter(cPop,c)){
                        return false;
                    }
                }
            }
        }

        return cStack.isEmpty();


    }

    public boolean isAdapter(char c,char c2){
        if(c =='('){
            return c2==')';
        }
        else if(c =='['){
            return c2==']';
        }
        else{
            return c2=='}';
        }

    }

    public boolean isLeftSign2(char c){

        if((c =='(')||(c =='[')||(c =='{')){
            return true;
        }

        return false;
    }

    public boolean isRightSign2(char c){
        if((c ==')')||(c ==']')||(c =='}')){
            return true;
        }
        return false;
    }

}
