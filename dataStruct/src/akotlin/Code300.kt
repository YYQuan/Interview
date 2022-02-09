package akotlin

fun main() {
    Code300().lengthOfLIS(intArrayOf(10,9,2,5,3,7,101,18)).let {
//    Code300().lengthOfLIS(intArrayOf(4,10,4,3,8,9)).let {
        println(it)
    }
}
class Code300 {
    fun lengthOfLIS(nums: IntArray): Int {
        if(nums.size <= 1) return nums.size
        val dp   = IntArray(nums.size)

        dp[0] =1
//        dp[1] =1

        for(i in 1 until nums.size){
            var max =1
            for(j in 0 until i){
                if (nums[i]>nums[j]){
                    max = Math.max(max,dp[j]+1)
                }
            }
            dp[i]=max
        }
        var result = Int.MIN_VALUE
        dp.forEach {
          result = Math.max(result,it)
        }
        return result
    }
}