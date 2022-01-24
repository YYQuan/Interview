package akotlin

fun main() {
//    val ints  = intArrayOf(1,0,1,2,0,2)
    val ints  = intArrayOf(2,0,2,1,1,0)
    Code75().sortColors(ints)
    ints.forEach {
        print("$it ")
    }
}
class Code75 {
    fun sortColors(nums: IntArray): Unit {
        sortColors(nums,0,nums.size-1)
    }
    fun sortColors(nums: IntArray,start :Int ,end:Int): Unit {
        if(start>=end) return
        val base = nums[start]
        var l = start
        var r = end
        while(l<r){
            while(l<r&&nums[l]<=base){
                l++
            }
            while(start<r&&nums[r]>=base){
                r--
            }
            if(l>=r)break
            val tmp = nums[l]
            nums[l] = nums[r]
            nums[r] = tmp
        }
        val tmp = nums[start]
        nums[start] = nums[r]
        nums[r] = tmp

        sortColors(nums,start,r-1)
        sortColors(nums,r+1,end)

    }
}