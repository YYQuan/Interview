package akotlin

fun main() {
//    Code209().minSubArrayLen(7,intArrayOf(2,3,1,2,4,3)).let {
//    Code209().minSubArrayLen(4,intArrayOf(1,4,4)).let {
//    Code209().minSubArrayLen(11,intArrayOf(1,1,1,1,1,1,1)).let {
    Code209().minSubArrayLen(11,intArrayOf(1,2,3,4,5)).let {
        println(it)
    }
}
class Code209 {
    fun minSubArrayLen(target: Int, nums: IntArray): Int {
        if(nums.isEmpty()) return 0

        var l = 0
        var r = 0
        var tmp = 0
        var result = Int.MAX_VALUE
        while(l<=r){

            while(tmp<target){
                if(r>=nums.size) return  if(result == Int.MAX_VALUE)  0 else result
                tmp +=nums[r]
                r++
            }
            println( " l $l  r $r $tmp")

            result = Math.min( r-l, result)

            while(tmp>=target){
                if(l>=r) return if(result == Int.MAX_VALUE)  0 else result

                result = Math.min( r-l, result)
                tmp  -=nums[l]
                l++
            }
        }
        return if(result == Int.MAX_VALUE)  0 else result
    }
}