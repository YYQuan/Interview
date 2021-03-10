package src;

public class LeetCode_70 {

    public static void main(String[] args) {

        System.out.println(new LeetCode_70().climbStairs(45));
        System.out.println(new LeetCode_70().climbStairsOverTime(45));

    }

    //动态规划
    // 击败 100
    public int climbStairs(int n) {
        if(n == 0) return 0;
        if(n == 1) return 1;
        if(n == 2) return 2;

        int sum = 0;
        int sum1 = climbStairs(1);
        int sum2 = climbStairs(2);

        for(int i =3 ; i<=n;i++){
            sum =sum1 +sum2;
            sum1 = sum2;
            sum2 = sum;
        }

        return sum;
    }

    // 超时
    public int climbStairsOverTime(int n) {
        if(n == 0) return 0;
        if(n == 1) return 1;
        if(n == 2) return 2;


        return climbStairsOverTime(n-1)+climbStairsOverTime(n-2);
    }




}
