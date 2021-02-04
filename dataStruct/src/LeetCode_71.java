package src;

import java.util.Stack;

public class LeetCode_71 {

    public static void main(String[] args) {
        LeetCode_71 code = new LeetCode_71();
//        String s = "()(";
//        String s = "()[]{}";
//        String s = "/home/";
//        String s = "/../";
//        String s = "/home//foo/";
//        String s = "/a/./b/../../c/";
//        String s = "/.";
        String s = "/./";
//        String[] s = new String[]{"2","1","+","3","*"};
//        String[] s = new String[]{"4","13","5","/","+"};

        String result = code.simplifyPath(s);
        System.out.println(result);

    }

    // 概念题 略过
    public String simplifyPath(String path) {

        Stack<Character> stack = new Stack<>();

        for(int i = 0;i<path.length();i++){

            char c = path.charAt(i);
            if(stack.isEmpty()){
                stack.push(c);
            }else{
                char ctop =stack.peek();
                if(c == '.'){

                    // 可能是 .. 表示上级目录
                    // 可能是 ... 表示 非命令， 可能是文件名
                    if(ctop == '.'){

                        if(path.length()>i+1){
                            if('.'==path.charAt(i)){
                                stack.push(c);
                                continue;
                            }

                        }

                        backSubPath(stack);
                        continue;
                    }




                }else if(c == '/'){
                    // 可能是./  也可能是 //


                    //./ 当前目录
                    if(ctop =='.') {
                        backCurrentPath(stack);
                        continue;
                    }else if(ctop == '/'){
                        continue;
                    }

                }
                stack.push(c);
            }

        }

        while(!stack.empty()&&(stack.peek()=='/'||stack.peek()=='.')){
            stack.pop();
        }
        if(stack.empty()){
            stack.push('/');
        }
        Stack<Character> tmpStack = new Stack<>();

        while(!stack.empty()){
            tmpStack.push(stack.pop());
        }

        StringBuilder build  = new StringBuilder();
        while(!tmpStack.empty()){
            build.append(tmpStack.pop());
        }

        return build.toString();
    }


    void backSubPath(Stack<Character> stack){

        //找到两个/
        int count = 0;
        while(!stack.empty()){

            char  c = stack.peek();
            if(c =='/') {
                count++;
            }
            if(count==2){
                break;
            }

            stack.pop();
        }


        if(stack.empty()){
            stack.push('/');
        }


    }
    void backCurrentPath(Stack<Character> stack){

        while(!stack.empty()&&(stack.peek()=='.'||stack.peek()=='/')){
            stack.pop();
        }


        if(stack.empty()){
            stack.push('/');
        }

    }


}
