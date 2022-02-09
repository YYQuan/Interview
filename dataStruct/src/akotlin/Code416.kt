package akotlin

fun main() {

//    Code416().canPartition(intArrayOf(1,5,11,5)).let {
//    Code416().canPartition(intArrayOf(1,2,3,5)).let {
//    Code416().canPartition(intArrayOf(14,9,8,4,3,2)).let {
    Code416().canPartition(intArrayOf(9,5)).let {
        println(it)
    }
}
class Code416 {
    // 动态规划  以币的数目的增加为 维度
    // 看能不能组合出 target值来
    fun canPartition(nums: IntArray): Boolean {
        if(nums.size<=1) return false
        var sum  =0
        nums.forEach {
            sum+=it
        }

        if(sum%2 ==1) return false
        val target = sum/2
//        println("target :$target")
        //dp[货币][能不能组合出该target]
        val  dp =Array(nums.size){BooleanArray(target+1)}

        if(nums[0]<= target )dp[0][nums[0]] = true

        for( i in 1 until  nums.size){
            for(j in 1 ..target){
                if(dp[i-1][j]){
                    dp[i][j] = true
                    if(j+nums[i]<=target) {
                        dp[i][j+nums[i]] = true
                    }
                }
                if(j == nums[i]) dp[i][j] = true
            }
        }
//
//        dp.forEach {
//            it.forEach {
//                print("$it  ")
//            }
//            println()
//        }
        for(i in nums.indices){
            if(dp[i][target]) return true
        }
        return false
    }
}