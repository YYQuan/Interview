package akotlin

fun main() {
    Code349().intersection(intArrayOf(1,2,2,1), intArrayOf(2,2,)).let {
        it.forEach {
            print("$it ")
        }
    }
}
class Code349 {
    fun intersection(nums1: IntArray, nums2: IntArray): IntArray {


        val set =HashSet<Int>()
        val set2 =HashSet<Int>()

        nums1.forEach {
            set.add(it)
        }

        nums2.forEach {
            set2.add(it)
        }



        val ints = mutableListOf<Int>()

        set.forEach {
            if(set2.contains(it)){
                ints.add(it)
            }
        }

        return  ints.toIntArray()
    }
}