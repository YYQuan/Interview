package akotlin

fun main() {
    Code343().integerBreak(10).let {
        println(it)
    }
}
class Code343 {
    fun integerBreak(n: Int): Int {
        if(n == 0 ) return 0
        if(n == 1 ) return 0
        if(n == 2 ) return 1
        if(n == 3 ) return 2
        if(n == 4 ) return 4
        val dp = IntArray(n+1)
        dp[0]=0
        dp[1]=1
        dp[2]=1
        dp[3]=2
        dp[4]=4

        for(i in 4..n){
            var max:Int? = null
            for( j  in 1 until i){
                val value = Math.max(j* dp[i-j],j*(i-j))
                max = if(max ==null) value else Math.max(max,value)
            }
            dp[i] = max!!
        }
        dp.forEachIndexed { index, it ->
            print("$index :$it ,")
        }

        return dp[n]

    }
}