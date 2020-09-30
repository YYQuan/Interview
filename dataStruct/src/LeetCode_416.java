import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeetCode_416 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LeetCode_416 code = new LeetCode_416();
//        int[] ints  = new int[]{1,2,3};
        int[] ints = {1, 5, 11, 5};
//        int[] ints = {1, 2, 3, 5};
        System.out.println(code.solution(ints));
        System.out.println(code.solutionDync(ints));

    }



////// 记忆化搜索

    int[][] tmpResult ;
    public boolean  solution(int[] arr){


        if(arr == null ||arr.length<=0)  return false;

        int sum =  0;

        for(int i = 0 ; i< arr.length ;i++){
            assert arr[i] >0 ;
            sum += arr[i];
        }

        if(sum%2 != 0 )  return false;

        tmpResult  = new  int[arr.length][sum/2+1];
        for(int i = 0 ; i<tmpResult.length; i++) {
            Arrays.fill(tmpResult[i], -1);
        }

        return solutionActual(arr,arr.length-1,sum/2);

    }

    //arr [0,index] 范围内 能不能 装满sum
    public  boolean  solutionActual(int[] arr,int  index ,int sum){
        if( index <=0 )  return false;
        if( sum == 0) return true;
        if(tmpResult[index][sum] > 0 ) return tmpResult[index][sum] != 0;

        tmpResult[index][sum] = solutionActual(arr,index-1,sum)||solutionActual(arr,index-1,sum-arr[index])? 1:0;

        return tmpResult[index][sum] != 0;
    }


    //////////////////////////////////
    // 动态规划

    public  boolean  solutionDync(int[] arr){

        if(arr == null ||arr.length<=0)  return false;

        int sum =  0;

        for(int i = 0 ; i< arr.length ;i++){
            assert arr[i] >0 ;
            sum += arr[i];
        }

        if(sum%2 != 0 )  return false;

        tmpResult  = new  int[arr.length][sum/2+1];
        for(int i = 0 ; i<tmpResult.length; i++) {
            Arrays.fill(tmpResult[i], -1);
        }

        boolean[] tmpResult = new boolean[sum/2+1];

        int totalSum = sum/2;
        if(arr[0]<=totalSum) {
            tmpResult[totalSum] = true;
        }


        for(int i = 0 ; i< arr.length ; i++){
            if(arr[i]<=totalSum) {
                tmpResult[i] = true;
            }
            for(int j = 0 ; j <totalSum+1; j++){
                if(tmpResult[j]){
                    int tmp = arr[i]+ j;
                    if(tmp <= totalSum) {
                        tmpResult[tmp] = true;
                    }
                }
            }
        }
        return  tmpResult[totalSum];
    }




}
