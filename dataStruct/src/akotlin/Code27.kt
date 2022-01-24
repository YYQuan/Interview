package akotlin

fun main() {
    val ints = intArrayOf(3,2,2,3)
    val code =Code27()
    code.removeElement(ints,3).let {
        println(it)
    }
    ints.forEach {
        print("$it ")
    }
}
class Code27 {
    fun removeElement(nums: IntArray, `val`: Int): Int {

        var l = 0
        var r = 0

        while (r<nums.size){
            while(r<nums.size&&nums[r]==`val`){
                r++
            }
            if(r<nums.size)  nums[l++] = nums[r++]
        }
        return l
    }
}