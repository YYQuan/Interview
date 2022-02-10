package akotlin

fun main() {

//    Code474().findMaxForm(arrayOf("10", "0001", "111001", "1", "0"),5,3).let {
//    Code474().findMaxForm(arrayOf("10", "1", "0"),1,1).let {
    Code474().findMaxForm(arrayOf("11111","100","1101","1101","11000"),5,7).let {
        println(it)
    }
}
class Code474 {
     // 动态规划 用 strs的元素来刷新 dp[m][n]
    //  从后往前刷 避免影响上一次的结果
    fun findMaxForm(strs: Array<String>, m: Int, n: Int): Int {


        val dp = Array(m+1){IntArray(n+1)}

         strs.forEach {

             var count0 = 0
             var count1 = 0
             it.forEach {
                 if(it == '0') count0++
                 else count1++
             }
//             println( " count0 $count0  count1 $count1")
             for(i in   m downTo  0){
                 for (j in n downTo  0){
//                     println("$dp")
                     if(i>=count0&&j>=count1)  dp[i][j] =  Math.max(dp[i-count0][j-count1]+1,dp[i][j])
                 }
             }


//             println( it)
//             dp.forEach {
//                 it.forEach {
//                     print("$it ")
//                 }
//                 println()
//             }

         }

         return dp[m][n]
    }


}