package src;

import java.util.Stack;

public class LeetCode_150 {

    public static void main(String[] args) {
        LeetCode_150 code = new LeetCode_150();
//        String s = "()(";
//        String s = "()[]{}";
//        String s = "[";
//        String[] s = new String[]{"2","1","+","3","*"};
//        String[] s = new String[]{"4","13","5","/","+"};
        String[] s = new String[]{"10","6","9","3","+","-11","*","/","*","17","+","5","+"};

        int result = code.evalRPN(s);
        System.out.println(result);

    }

    // 击败 54
    public int evalRPN(String[] tokens) {

        Stack<Integer> stack = new Stack<>();

        for(int i =0 ; i<tokens.length;i++){
            String s = tokens[i];

            if(isSign(s.charAt(0))&&s.length()==1){
                if(stack.empty()) {
                    throw new IllegalArgumentException();
                }
                int operatorNumber1 =stack.pop();
//                System.out.println("pop 1 "+operatorNumber1);
//                System.out.println("stack  "+stack);

                if(stack.empty()) {
                    throw new IllegalArgumentException();
//                    return 0;
                }
                int operatorNumber2 =  stack.pop();
//                System.out.println("pop 2 "+operatorNumber2);
//                System.out.println("stack  "+stack);
//

                switch(s.charAt(0)){
                    case '+':
                        operatorNumber1 = operatorNumber1+operatorNumber2;
                        break;
                    case '-':
                        operatorNumber1 = operatorNumber2-operatorNumber1;
                        break;
                    case '*':
                        operatorNumber1 = operatorNumber1*operatorNumber2;
                        break;
                    case '/':
                        operatorNumber1 = operatorNumber2/operatorNumber1;
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                stack.push(operatorNumber1);
//                System.out.println("push "+operatorNumber1);
//                System.out.println("stack  "+stack);


            }else{
                stack.push(Integer.parseInt(s));
//                System.out.println("push "+s);
//                System.out.println("stack  "+stack);


            }
        }


        return stack.pop();

    }


    boolean isSign(char c){

        return c =='+'||c =='-'||c =='*'||c =='/';
    }



}
