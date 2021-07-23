package src;

import java.util.Arrays;

public class Util {
    public static void printDeep(int deep ,String content){
        StringBuilder builder = new StringBuilder();
        for(int j = 0 ; j<deep;j++){
            builder.append("-");
        }
        System.out.println(builder.toString()+ content);
    }

}
