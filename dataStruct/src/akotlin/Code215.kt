package akotlin

fun main() {
//    Code215().findKthLargest(intArrayOf(3,2,1,5,6,4),2).let {
//    val array = intArrayOf(3,2,3,1,2,4,5,5,6)
    val array = intArrayOf(3,2,1,5,6,4)
    Code215().findKthLargest(array,2).let {
        println(it)
    }
    array.forEach {
        print("$it , ")
    }
}
class Code215 {
    fun findKthLargest(nums: IntArray, k: Int): Int {
        quickSort(nums,0,nums.size-1,nums.size-k)
        return nums[nums.size-k]
    }

    fun quickSort(nums:IntArray,start:Int,end:Int,k:Int){
        if(start >= end) return

        var mid = handle(nums,start,end)
        if(mid == k) return
        quickSort(nums,start,mid-1,k)
        quickSort(nums,mid+1,end,k)

    }

    fun handle(nums:IntArray,start:Int, end:Int):Int{

        var l  =start
        var r = end
        val base = nums[l]
        A@while(l<r){

            while(nums[l]<= base){
                l++
                if(l>r) {
                    break
                }
            }

            while(nums[r]>=base){
                r--
                if(r<=start){
                    break@A
                }
            }
            if(l<r) {
                val tmp = nums[r]
                nums[r] = nums[l]
                nums[l] = tmp
            }
        }

        val tmp = nums[r]
        nums[r] = nums[start]
        nums[start]=tmp

        return r
    }
}