package akotlin

fun main() {
    val ints = intArrayOf(1,1,3)
    val code = Code26()
    code.removeDuplicates(ints).let {
        println(it)
    }
    ints.forEach {
        print("$it ")
    }
}
class Code26 {
    fun removeDuplicates(nums: IntArray): Int {
        if(nums.isEmpty()) return 0
        if(nums.size==1) return 1

        var l = 0
        var r = 0
        while(r<nums.size){
            nums[l] = nums[r]

            do{
                r++
            }while (r<nums.size&&nums[r-1]==nums[r])
            l++
        }
        return l

    }
}