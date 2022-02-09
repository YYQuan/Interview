package akotlin

fun main() {
    val array = intArrayOf(0,1,0,3,12)
    Code283().moveZeroes(array).let {

    }
    array.forEach {
        print("$it ")
    }
    println()
}
class Code283 {
    fun moveZeroes(nums: IntArray): Unit {


        var l =  0
        var r =  0
        while(l<nums.size){

            while(nums[l]!=0){
                l++
                if(l>=nums.size) return
            }
            r = l
            while(nums[r]==0){
                r++
                if(r>=nums.size) return
            }

            val tmp = nums[r]
            nums[r] = nums[l]
            nums[l] = tmp
            l++
        }

    }
}