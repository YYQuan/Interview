package akotlin

fun main() {
//    Code213().rob(intArrayOf(2,3,2)).let {
//    Code213().rob(intArrayOf(1,2,3,1)).let {
//    Code213().rob(intArrayOf(1,2,1,1)).let {
    Code213().rob(intArrayOf(2,1,1,2)).let {
        println(it)
    }
}
class Code213 {
    fun rob(nums: IntArray): Int {
        if(nums.isEmpty()) return 0
        if(nums.size == 1) return nums[0]


        val dp = IntArray(nums.size)
        dp[0] = nums[0]
        dp[1] = Math.max(nums[0],nums[1])

        if(nums.size == 2) return dp[1]
        for(i in 2 until nums.lastIndex){
            dp[i] = Math.max(dp[i-1],dp[i-2]+nums[i])
        }

        var result =  dp[nums.lastIndex-1]
//        println(result)
        // 不选 第一个
        dp[0] = 0
        dp[1] = nums[1]

        for(i in 2 ..nums.lastIndex){
            dp[i] = Math.max(dp[i-1],dp[i-2]+nums[i])
        }

        result =  Math.max(result, dp[nums.lastIndex])
//        println(result)

        return result
    }
}