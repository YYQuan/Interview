package akotlin

fun main() {
    Code377().combinationSum4(intArrayOf(1,2,3),4).let {
//    Code377().combinationSum4(intArrayOf(9),3).let {
        println(it)
    }
}
class Code377 {
    fun combinationSum4(nums: IntArray, target: Int): Int {

        val dp = IntArray(target+1)
        dp[0] =0

        for( i in 1..target){
            var sum = 0
            nums.forEach {
                if(it>i){
                    return@forEach
                }
                sum+=dp[i-it] +if(it  == i) 1 else 0
            }
            dp[i] = sum

        }
        return dp[target]
    }
}