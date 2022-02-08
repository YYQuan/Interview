package akotlin

class Code136 {
    fun singleNumber(nums: IntArray): Int {
        val set = HashSet<Int>()
        nums.forEach {
            if(set.contains(it)){
                set.remove(it)
            }else{
                set.add(it)
            }
        }
        set.forEach {
            return it
        }
        throw IllegalStateException()
    }
}