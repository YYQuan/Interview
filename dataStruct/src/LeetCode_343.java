import java.util.Arrays;

public class LeetCode_343 {

    public static void main(String[] args) {

        System.out.println(new LeetCode_343().solution2(
                50));
        System.out.println(new LeetCode_343().solution3(
                50));
    }


    public int integerBreak(int n) {



        return  0;
    }

    public  int solution(int n ){
        int res = -1;
        if(n == 1 )  return  1;
        if(n == 2 )  return  1;
        for(int i = 2; i<n ; i++){
            res = Math.max(res,Math.max(i*(n-i),i*solution(n-i)));
        }
        return res;
    }
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    int[]  tmpAnswer ;

    public  int solution2(int n ){
        tmpAnswer = new int[n+1];
        Arrays.fill(tmpAnswer,-1);

        solutionActual(n);

        return tmpAnswer[n];
    }

    private int solutionActual(int n){
        if( n == 1)  return 1;
        if( n == 2 ) return 1;

        if(tmpAnswer[n] != -1)  return tmpAnswer[n];

        int res = -1;
        for(int i = 2 ;i<n;i++)
            res = Math.max(res,Math.max(i*(n-i), i*solutionActual(n-i)));

        tmpAnswer[n] = res;
        return res;
    }

    //////////////////动态规划////////////////////////////
    public  int  solution3(int n ){
        int[]     tmp = new int[n+1];
        Arrays.fill(tmp,-1);
//        tmp[0] =1;
        tmp[1] =1;
        tmp[2] =1;
        for(int i = 2 ; i<= n ;i++){
            for(int j = 1 ; j<i;j++){
                tmp[i] =Math.max(tmp[i], Math.max(j*(i-j),j*tmp[i-j]));
            }
        }
        return tmp[n];

    }
}
