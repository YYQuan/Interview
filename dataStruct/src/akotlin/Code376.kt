package akotlin

fun main() {
//    Code376().wiggleMaxLength(intArrayOf(1,7,4,9,2,5)).let {
//    Code376().wiggleMaxLength(intArrayOf(1,17,5,10,13,15,10,5,16,8)).let {
//    Code376().wiggleMaxLength(intArrayOf(1,2,3,4,5,6,7,8,9)).let {
//    Code376().wiggleMaxLength(intArrayOf(84)).let {
    Code376().wiggleMaxLength(intArrayOf(0,0)).let {
        println(it)
    }
}
class Code376 {
    fun wiggleMaxLength(nums: IntArray): Int {
        if(nums.size<=1)  return nums.size
        // { 升序 ,降序}
        val dp = Array(nums.size){IntArray(2)}
        dp[0][0] =1
        dp[0][1] =1
        dp[1][0] = if( nums[1]>nums[0]) 2 else 1
        dp[1][1] =  if(nums[1]< nums[0]) 2 else 1

        for(i in 2 until nums.size){
            for(j in 0 until i){
                dp[i][0]=Math.max(if(nums[i]>nums[j]){
                    dp[j][1]+1
                }else 1 , dp[i][0])

                dp[i][1] = Math.max(if(nums[i]<nums[j]){
                    dp[j][0]+1
                }else 1,dp[i][1])
            }
        }

//        return Math.max(dp[dp.lastIndex][0],dp[dp.lastIndex][1])
        var result = Int.MIN_VALUE
        dp.forEach {
            it.forEach {
//                print(" $it ")
                result = Math.max(result,it)
            }
//            println()
        }
        return result
    }
}