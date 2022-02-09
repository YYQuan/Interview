package akotlin

fun main() {
    Code279().numSquares(3).let {
        println(it)
    }
}
class Code279 {
    fun numSquares(n: Int): Int {
        val dp = IntArray(n+1)
        dp[0] =0
        dp[1] =1
        for(i in 1..n){
            var min  = Int.MAX_VALUE
            for(j in 1..i){
                if( j *j >i) break
                val value =  dp[i - j*j]+1
                min = Math.min(min,value)
            }
            dp[i] = min
        }
        return dp[n]
    }
}