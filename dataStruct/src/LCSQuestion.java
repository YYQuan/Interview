import com.sun.deploy.util.StringUtils;

import java.util.Arrays;

/**
 * LCS 问题就是 最长公共序列问题
 */
public class LCSQuestion {
    ////////////////////////////
    // 递归
    // 时间复杂度 O（2^n^） 慢的令人发指
    public String solution(String s1 , String s2){

        if(s1 == null || s2 == null || s1.length()<= 0 || s2.length()<= 2){
            throw new IllegalArgumentException();
        }
        return solutionActual(s1,s2,s1.length()-1,s2.length()-1);
    }

    public  String solutionActual(String s1,String s2 ,int indexS1  , int indexS2){
        StringBuilder builder = new StringBuilder();
        for(int i =0 ; i<indexS1+indexS2 ;i++){
            builder.append("- - ");
        }
//        System.out.println(builder.toString() + "  index S1 :"+indexS1 +"  index S2 :"+indexS2+"   start" );

        if(indexS1 < 0 ||indexS2 <0 ){
            return "";
        }
        if(s1.charAt(indexS1) == s2.charAt(indexS2)){
//            System.out.println(builder.toString() + "  index S1 :"+indexS1 +"  index S2 :"+indexS2 + "    ==== end");

            return  solutionActual(s1,s2,indexS1-1,indexS2-1) +s1.charAt(indexS1);
        }else{
            String str1 = solutionActual(s1,s2,indexS1-1,indexS2);
            String str2 = solutionActual(s1,s2,indexS1,indexS2-1);
//            System.out.println(builder.toString() + "  index S1 :"+indexS1 +"  index S2 :"+indexS2  + "    !!!!= end");

            return str1.length()>str2.length() ?str1:str2;
        }

    }


    ////////////////////////////
    // 尝试记忆化
    String[][] tmpResult ;
    public String solution2(String s1 , String s2){

        if(s1 == null || s2 == null || s1.length()<= 0 || s2.length()<= 2){
            throw new IllegalArgumentException();
        }
        tmpResult = new String[s1.length()][s2.length()];

        return solutionActual2(s1,s2,s1.length()-1,s2.length()-1);
    }

    public  String solutionActual2(String s1,String s2 ,int indexS1  , int indexS2) {
        if (indexS1 < 0 || indexS2 < 0) {
            return "";
        }
        if (tmpResult[indexS1][indexS2] != null && tmpResult[indexS1][indexS2].length() > 0)
            return tmpResult[indexS1][indexS2];
        if (s1.charAt(indexS1) == s2.charAt(indexS2)) {
            tmpResult[indexS1][indexS2] = solutionActual2(s1, s2, indexS1 - 1, indexS2 - 1) + s1.charAt(indexS1);
        } else {
            if(indexS1>0&&indexS2>0) {
                tmpResult[indexS1 - 1][indexS2] = solutionActual2(s1, s2, indexS1 - 1, indexS2);
                tmpResult[indexS1][indexS2 - 1] = solutionActual2(s1, s2, indexS1, indexS2 - 1);
                tmpResult[indexS1][indexS2] = tmpResult[indexS1 - 1][indexS2].length() > tmpResult[indexS1][indexS2 - 1].length() ?
                        tmpResult[indexS1 - 1][indexS2] : tmpResult[indexS1][indexS2 - 1];

            }else if(indexS1>0){
                tmpResult[indexS1 - 1][indexS2] = solutionActual2(s1, s2, indexS1 - 1, indexS2);
                tmpResult[indexS1][indexS2] = tmpResult[indexS1 - 1][indexS2];
            }else if(indexS1<0){
                tmpResult[indexS1 ][indexS2-1] = solutionActual2(s1, s2, indexS1 - 1, indexS2);
                tmpResult[indexS1][indexS2] = tmpResult[indexS1 ][indexS2-1];
            }else{
                tmpResult[indexS1][indexS2] = "";
            }
        }
        return tmpResult[indexS1][indexS2];
    }


    //////////////////////////////////////
    //动态规划

    public  String  lcsSolution(String s1, String s2){
        if(s1 == null || s2 == null || s1.length()<=0 || s2.length()<=0){
            throw new IllegalArgumentException();
        }

        // 初始化  0 排 0 列的数据
        int[][] tmpData = new int[s1.length()][s2.length()];

        for(int i = 0 ; i< s2.length() ;i++){
            if(s1.charAt(0) == s2.charAt(i) ){
                for(int j = i ; j< s2.length() ; j++){
                    tmpData[0][j] = 1;
                }
                break;
            }
        }

        for(int i = 0 ; i<s1.length() ;i++){
            if(s1.charAt(i) == s2.charAt(0)){
                for(int j = i ; j<s1.length();j++){
                    tmpData[j][0] = 1;
                }
                break;
            }
        }


        //动态规划
        for(int i = 1 ; i<s1.length(); i++){
            for(int j =1 ;j<s2.length();j++){
                if(s1.charAt(i) == s2.charAt(j)){
                    tmpData[i][j] = 1+ tmpData[i-1][j-1];
                }else {
                    int maxValue = Math.max(tmpData[i - 1][j], tmpData[i][j - 1]);
                    tmpData[i][j] =  maxValue;
                }
            }
        }

        for(int i = 0 ; i< tmpData.length ;  i++){


            for( int j = 0  ; j < tmpData[0].length ;j++){

                System.out.print(tmpData[i][j] + (tmpData[i][j]>9 ?" ":"  "));
            }
            System.out.println(" -- 行"+i);
        }

        //由动态规划的数据中 恢复最长公共子序列
        return recoverData2(s1,s2,tmpData);

    }

    // 直接逻辑的恢复
    // 找到 S1 S2 在 增加的过程中， 出现的第一个 value (value:[1:resultLength])
    public  String  recoverData2(String s1,String s2 ,int[][] data){

        int n =0;
        int m =0;
        int currentValue = 1 ;
        StringBuilder result = new StringBuilder();
        while(n < s1.length() && m <s2.length()){
            if(data[n][m] == currentValue&&s1.charAt(n)==s2.charAt(m)){
                System.out.println(" currentValue : "+ currentValue + " n :"+ n + " m :"+m +"  s2.charAt(m):"+s2.charAt(m));

                result.append(s2.charAt(m));
                currentValue++;
            }

            if(n == s1.length()-1){
                m++;
            }else if(m == s1.length()-1){
                n++;
            }else if( n > m ){
                m++;
            }else{
                n++;
            }

        }
       return result.toString();

    }



    // 优化一下恢复过程
    // 其逻辑是 data[l1-1][l2-1]  肯定最 data 中的最大值  值为 x ,
    // 只要在data的值为 x 中  找到一个 s1[m] == s2 [n] , 那么这 s1[m] 就是最长公共子序列中的一员，
    // 然后判定他是不是在+1的位置 （变化的的位置）  用这个来判断 data[n-1][m]>data[n][m-1]

    // 另外  要有这个意识  data 中 相同的值都是连在一起的
    // 为啥呢？
    // 因为  在s1  s2 的逐个变化过程中 公共子序列  只能不变， 或者加1

    // 对于 data 中的同一个值 ，  data 值突变的位置， 肯定在起左 、 上、 左上方（ 优先左 还是优先上 是有关系的 ， 必须是最左边的  如果两个方向都上都有突变值的话，那么就是有多个解。）

    // 往左上 走  data 的值 一定是越来越小的

    // 只要在 data[n][m]  == value 处  找到一个 s1.charAt(n) == s2.charAt(m)
    //

    // 这种解析 只能解析出一个解
    public String  recoverData(String s1,String s2,int[][] data){

        int n = data.length-1;
        int m = data[0].length-1;
        StringBuilder result = new StringBuilder();
        while(n>=0&&m>=0){

            if(s1.charAt(n) == s2.charAt(m)){
                result.insert(0,s1.charAt(n));
                n--;
                m--;
            }else if(n == 0 ){
                m--;
            }else if(m == 0 ){
                n--;
            }else{
                //  不能有等号
                if(data[n-1][m]>data[n][m-1]){
                    n--;
                }else{
                    m--;
                }
            }

        }


        return result.toString();
    }


    ////////////////////////////
    // 大佬的lcs 1
    public String lcs(String s1, String s2){

        int m = s1.length();
        int n = s2.length();

        // 对memo的第0行和第0列进行初始化
        int[][] memo = new int[m][n];
        for(int j = 0 ; j < n ; j ++)
            if(s1.charAt(0) == s2.charAt(j)){
                for(int k = j ; k < n ; k ++)
                    memo[0][k] = 1;
                break;
            }

        for(int i = 0 ; i < m ; i ++)
            if(s1.charAt(i) == s2.charAt(0)) {
                for(int k = i ; k < m ; k ++)
                    memo[k][0] = 1;
                break;
            }

        // 动态规划的过程
        for(int i = 1 ; i < m ; i ++)
            for(int j = 1 ; j < n ; j ++)
                if(s1.charAt(i) == s2.charAt(j))
                    memo[i][j] = 1 + memo[i-1][j-1];
                else
                    memo[i][j] = Math.max(memo[i-1][j], memo[i][j-1]);

        // 通过memo反向求解s1和s2的最长公共子序列
        m = s1.length() - 1;
        n = s2.length() - 1;
        StringBuilder res = new StringBuilder("");
        while(m >= 0 && n >= 0)
            if(s1.charAt(m) == s2.charAt(n)){
                res.insert(0, s1.charAt(m));
                m --;
                n --;
            }
            else if(m == 0)
                n --;
            else if(n == 0)
                m --;
            else{
                if(memo[m-1][n] > memo[m][n-1])
                    m --;
                else
                    n --;
            }

        return res.toString();
    }

    public static void main(String[] args) {

        String s1 = "ABCDGH";
        String s2 = "AEDFHR";
//        System.out.println((new LCSQuestion()).lcs(s1, s2));
//        System.out.println((new LCSQuestion()).solution(s1, s2));
//        System.out.println((new LCSQuestion()).lcsSolution(s1, s2));

//        s2 = "AAACCGTGAGTTATTCGTTCTAGAA";
//        s1 = "AA";
//        s1 = "CACCCCTAAGGTACCTTTGGTTC";

        s1 = "AAACCGTGAGTTATTCGTTCTAGAA";
        s2 = "CACCCCTAAGGTACCTTTGGTTC";
        System.out.println((new LCSQuestion()).lcs(s1, s2));
//        System.out.println((new LCSQuestion()).solution(s1, s2));
//        System.out.println((new LCSQuestion()).solution2(s1, s2));
        System.out.println((new LCSQuestion()).lcsSolution(s1, s2));

        System.out.println((new LCSQuestion()).lcs(s1, s2));

    }


}
