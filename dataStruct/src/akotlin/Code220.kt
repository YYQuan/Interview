package akotlin

import java.util.*

fun main() {
//    Code220().containsNearbyAlmostDuplicate(intArrayOf(1,2,3,1),3,0).let {
//    Code220().containsNearbyAlmostDuplicate(intArrayOf(-2147483648,2147483647),1,1).let {
    Code220().containsNearbyAlmostDuplicate(intArrayOf(Int.MIN_VALUE,Int.MAX_VALUE),1,1).let {
        println(it)
    }
}
class Code220 {
    fun containsNearbyAlmostDuplicate(nums: IntArray, k: Int, t: Int): Boolean {

        val  treeSet  = TreeSet<Long>()
        nums.forEachIndexed { index, it ->
            val floor = treeSet.floor(it.toLong())
            val ceil = treeSet.ceiling(it.toLong())

            if(floor!=null&&it-floor<=t) return true
            if(ceil!=null&&ceil-it<=t) return true

            treeSet.add(it.toLong())
            if(treeSet.size>k){
                treeSet.remove(nums[index-k].toLong())
            }
        }
      return false

    }
}