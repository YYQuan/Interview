import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_77 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LeetCode_77  code = new LeetCode_77();
        System.out.println(code.combine(4,2));

    }


    List<List<Integer>> res = new ArrayList();
    public List<List<Integer>> combine(int n ,int k ){
        res = new ArrayList<List<Integer>>();
        LinkedList<Integer> c  = new LinkedList();
        solution(n,k,1,c);
        return res;

    }

    private void solution(int n ,int k ,int start ,LinkedList<Integer> c){
        if(c.size() == k) {
            res.add((List<Integer>) c.clone());
            return ;
        }

        // 核心在这
        // 都从自己的后面取 能保证没有重复的。
        // 但其实还是不太理解怎么就保证没重复的了
        // 因为是组合 组合是不在乎 顺序的
        // 比如 1-3-4 和 1-4-3

        for(int i = start ;i<=n;i++){
            c.addLast(i);
            solution(n,k,i+1,c);
//            这里实现回溯
            c.removeLast();
        }
    }
}
