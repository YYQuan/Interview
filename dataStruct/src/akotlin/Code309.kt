package akotlin

fun main() {

//    Code309().maxProfit(intArrayOf(1,2,3,0,2)).let {
//    Code309().maxProfit(intArrayOf(1)).let {
    Code309().maxProfit(intArrayOf(6,1,3,2,4,7)).let {
        println(it)
    }
}
class Code309 {
    fun maxProfit(prices: IntArray): Int {
        if(prices.size<2) return 0
        // 买 卖 不卖 不买
        val dp = Array(prices.size){IntArray(4)}

        dp[0][0] = -prices[0]
        dp[0][1] = 0
        dp[0][2] = 0
        dp[0][3] = 0

        dp[1][0] = -prices[1]
        dp[1][1] = prices[1]+dp[0][0]
        dp[1][2] = dp[0][0]
        dp[1][3] = 0

        for( i in 2 until prices.size){
            dp[i][0]=dp[i-1][3]-prices[i]
            dp[i][1]=Math.max(dp[i-1][0],dp[i-1][2])+prices[i]
            dp[i][2]=Math.max(dp[i-1][0],dp[i-1][2])
//            dp[i][2]=dp[i-1][0]
            dp[i][3]=Math.max(dp[i-1][1],dp[i-1][3])
        }

//        dp.let {
//            it.forEach {
//               it.forEach {
//                   print("$it ")
//               }
//                println()
//            }
//        }
        return Math.max(dp[prices.size-1][1],dp[prices.size-1][3])
    }
}