import java.util.ArrayList;
import java.util.List;

public class LeetCode_167 {

    public static void main(String[] args) {
        LeetCode_167 code = new LeetCode_167();
        int[] ints  = new int[]{2,7,11,15};
//        int[] result = code.solution(ints,9);
        int[] result = code.twoSum(ints,9);
        int[] result2 = code.twoSum2(ints,9);
        System.out.println(result[0]+"  "+result[1]);
        System.out.println(result2[0]+"  "+result2[1]);

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

    public int[] twoSum(int[] nums, int target){
        int[]  result = new int[]{1,1};

        if(nums.length<2) return result;

        int i = 0;
        int j = nums.length-1;

        while(i<j){
            if(nums[i]+nums[j]>target){
                j--;
            }else if(nums[i]+nums[j]<target){
                i++;
            }else{
                result[0] = i+1;
                result[1] = j+1;
                return result;
            }
        }


        return result;
    }

    public int[] twoSum2(int[] numbers, int target) {

        if(numbers == null ||numbers.length<2) return  null;

        int l = 0;
        int r = numbers.length-1;

        while(l<r){

            int sum= numbers[l]+numbers[r];
            if(sum == target){
                return new int[]{l+1,r+1};

            }else if(sum <target){
                l++;
            }else{
                r--;
            }


        }
        throw new IllegalArgumentException();

    }

}
