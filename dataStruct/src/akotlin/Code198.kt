package akotlin

fun main() {
    Code198().rob(intArrayOf(1,2,3,1)).let {
        println(it)
    }
}
class Code198 {

    fun rob(nums: IntArray): Int {
        if(nums.isEmpty()) return 0
        if(nums.size==1) return nums[0]
        val dp = IntArray(nums.size).let {
            it.fill(0)
            it
        }
        dp[0] = nums[0]
        dp[1] = Math.max(nums[0],nums[1])

        for(i in 2 until nums.size){
            dp[i] = Math.max(dp[i-1],dp[i-2]+nums[i])
        }
        return dp[nums.size-1]

    }
}