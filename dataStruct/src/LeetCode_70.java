public class LeetCode_70 {

    public static void main(String[] args) {

        System.out.println(new LeetCode_70().climbStairs(
                2));

    }


    public int climbStairs(int n) {

        assert  n>=1;

        int[]  tmp = new int[n+1];
        tmp[0]  = 1;
        tmp[1]  = 1;
        tmp[2]  = 2;

        for(int i = 2 ;  i<=n ;i++){
            tmp[i] = tmp[i-1] +tmp[i-2];
        }

        return  tmp[n];
    }

}
