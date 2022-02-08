package akotlin

fun main() {

    Code219().containsNearbyDuplicate(intArrayOf(1,2,3,1),3).let {
        println(it)
    }
}
class Code219 {
    fun containsNearbyDuplicate(nums: IntArray, k: Int): Boolean {

        val map = HashMap<Int,Int>()
        nums.forEachIndexed { index, i ->
            if(map.containsKey(i)){
                if(index - map[i]!! <= k ) return true
            }
            // 更新到最接近的那个就行
            map[i] = index
        }
        return false
    }
}