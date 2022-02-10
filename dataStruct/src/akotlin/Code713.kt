package akotlin

fun main() {

    Code713().numSubarrayProductLessThanK(intArrayOf(10,5,2,6),100).let {
        println(it)
    }
}
class Code713 {
    fun numSubarrayProductLessThanK(nums: IntArray, k: Int): Int {
        return numSubarrayProductLessThanK(nums,0,k)
    }
    fun numSubarrayProductLessThanK(nums: IntArray,start:Int, k: Int): Int {
        if(start>=nums.size) return 0
        return getContainCount(nums,start,1,k)+numSubarrayProductLessThanK(nums,start+1,k)
    }
    fun  getContainCount(nums:IntArray ,start:Int,t:Int,k :Int):Int{
        if(t>k) return 0
        if(start>=nums.size) return  0
        if(t * nums[start] <k){
//            println("t: $t start :$start   nums[start] :${nums[start]} t * nums[start] ${t * nums[start]}")

            return 1 +getContainCount(nums,start+1,t*nums[start],k)
        }

        return 0
    }
}