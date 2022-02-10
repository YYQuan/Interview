package akotlin

fun main() {
    Code494().findTargetSumWays(intArrayOf(1,1,1,1,1),3).let {
        println(it)
    }
}
class Code494 {
    fun findTargetSumWays(nums: IntArray, target: Int): Int {


        return findTargetSumWays(nums,0,target)
    }
    fun findTargetSumWays(nums: IntArray,start:Int, target: Int): Int {
//        println("start $start")
        if(start>=nums.size){
            return if(target == 0)  1 else 0
        }
        var result = 0
        val int =nums[start]
        result +=findTargetSumWays(nums,start+1,target+int)
        result +=findTargetSumWays(nums,start+1,target-int)
        return result

    }
}