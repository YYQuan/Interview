import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_46 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LeetCode_46 code = new LeetCode_46();
        int[] ints  = new int[]{1,2,3};
        System.out.println(code.solution(ints));

    }


    List<List<Integer>>  result = new ArrayList();
    public List<List<Integer>>  solution(int[] arr){
        List list = new ArrayList();

        for(int i = 1 ; i<=arr.length ;i++){
            list.add(i);
        }

        solution(new ArrayList(),list);
        return result;
    }
    public void solution(List<Integer> tmp ,List<Integer> list){

        if(list.size()==0){
            result.add(new ArrayList<>(tmp));
            return ;
        }
        List<Integer> tmpList = new ArrayList<>(list);

        for(Integer i :tmpList){
            list.remove((Object)i);
            tmp.add(i);
            solution(tmp,list);
            tmp.remove((Object)i);
            list.add(i);
        }
        return ;
    }
}
