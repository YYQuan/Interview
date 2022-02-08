package akotlin

class Code217 {
    fun containsDuplicate(nums: IntArray): Boolean {

        if(nums.isEmpty()) return false

        val set = HashSet<Int>()

        nums.forEach {
            if(set.contains(it))return true
            else  set.add(it)
        }
        return  false
    }
}