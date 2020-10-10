import java.util.ArrayList;
import java.util.List;

public class LeetCode_167 {

    public static void main(String[] args) {
        LeetCode_167 code = new LeetCode_167();
        int[] ints  = new int[]{2,7,11,15};
        int[] result = code.solution(ints,9);
        System.out.println(result[0]+"  "+result[1]);

    }


    public  int[] solution(int[] arr  ,int target){

        if(arr == null || arr.length <=0 ) return null;

        if(arr[0]>=target)  return null;

        int[] result = new int[]{-1,-1};
        int i = 0,j =  arr.length-1 ;
        while(i<j){
            int num1 = arr[i];
            int num2 = arr[j];
            int sum = num1+num2;
            if(sum == target){
                result[0] = i+1 ;
                result[1] = j+1 ;
                break;
            }else if(sum >target){
                j--;
            }else{
                i++;
            }

        }

        return result;
    }
}
