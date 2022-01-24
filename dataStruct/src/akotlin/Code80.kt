package akotlin

fun main() {
    val ints = intArrayOf(1,1,1,2,2,3)
    Code80().removeDuplicates(ints).let {
        println(it)
    }
}
class Code80 {
    // 队列是有序的
    // 逻辑是这样的 一个值 是否允许被插入队列后面
    // 有两个条件，
    // 1.如果要插入的值和 队列最后一个值不一样，那么队列中就肯定乜有相同的
    // 2.如果队列的最后一个值 和 队列倒数第二个值 不一样 ，那么就随便什么值都能加
    fun removeDuplicates(nums: IntArray): Int {
        if(nums.isEmpty()) return 0
        if(nums.size<=2) return nums.size
        var l = 1
        var r = 2

        while(r<nums.size){

            if(nums[l]!=nums[l-1]){
                nums[++l] = nums[r++]
            }else if(nums[l] != nums[r]){
                nums[++l] = nums[r++]
            }else{
                r++
            }
        }
        return l +1
    }
}