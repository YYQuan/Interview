package akotlin

fun main() {
//    Code322().coinChange(intArrayOf(1,2,5),11).let {
    Code322().coinChange(intArrayOf(2),3).let {
        println(it)
    }
}
class Code322 {
    fun coinChange(coins: IntArray, amount: Int): Int {
        if(amount ==0  ) return 0

        val dp = IntArray(amount+1).let {
            it.fill(-1)
            it
        }
        dp[0] = 0
        coins.forEach {
            if(it<=amount){
                dp[it] = 1
            }
        }

        for( i in 1..amount){
            if(dp[i] == -1){
                var min:Int? = null
                coins.forEach {
                    if(it>i) return@forEach
                    else if(it == i) {
                        min = 1
                    }else{
                        if(dp[i-it]!=-1){
                        val value =dp[i-it]+1
                        min = if(min ==null) value
                        else Math.min(min!!,value)
                        }
                    }
                }
                min?.let {
                    dp[i] = min!!
                }
            }
        }
        return dp[amount]
    }
}