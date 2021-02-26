package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_401 {

    public static void main(String[] args) {
        LeetCode_401 code = new LeetCode_401();
//        int[] ints = new int[]{2,3,6,7};
//        int[] ints = new int[]{7};
//        int[] ints = new int[]{10,1,2,7,6,1,5};
//        int[] ints = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
//        int[] ints = new int[]{1,1,1,2};
//        int[] ints = new int[]{10,1,2,7,6,1,1,5};
//        int[] ints = new int[]{1,2,2};
        int[] ints = new int[]{4,4,4,1,4};
//        System.out.println(code.combinationSum(ints,7));
//        System.out.println(code.combinationSum2(ints,8));
        System.out.println(code.readBinaryWatch(2));

    }


    List<String> list = new ArrayList<>();
    List<Integer> path = new ArrayList<>();
    // 在 ALL TIME 里面 选  num个
    // 但是 输出规则 有点晕 思路对了就行了。 答案不对了
    public List<String> readBinaryWatch(int num) {
        readBinaryWatch(num,getAllTime(),0);
        return list;
    }
    public void readBinaryWatch(int num,List<Integer> times,int index) {

        if(path.size() == num) {
            String time = transferStr(path);

            if(time.length()>0) {
                list.add(time);
            }
            return;
        }

        for(int i = index ;i<times.size();i++){
            path.add(times.get(i));
            readBinaryWatch(num,times,i+1);
            path.remove(path.size()-1);
        }


    }



    public String transferStr(List<Integer> list){
        int sum = 0;
        for(int v:list){
            sum+=v;
        }
        if(sum >= 720){
            return "";
        }
        return String.format("%s:%02d",sum/60,sum%60);


    }

    public List<Integer>  getAllTime(){
        LinkedList<Integer> result = new LinkedList<>();
        result.addLast(1);
        result.addLast(2);
        result.addLast(4);
        result.addLast(8);
        result.addLast(16);
        result.addLast(32);
        result.addLast(60);
        result.addLast(120);
        result.addLast(240);
        result.addLast(480);

        return result;
    }
}
