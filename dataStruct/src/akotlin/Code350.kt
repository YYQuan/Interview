package akotlin

fun main() {
    Code350().intersect(intArrayOf(1,2,2,1), intArrayOf(2,2)).let {
        it.forEach {
            print("$it ")
        }
    }
}
class Code350 {
    fun intersect(nums1: IntArray, nums2: IntArray): IntArray {

        val map = HashMap<Int,Int>()
        val map2 = HashMap<Int,Int>()

        nums1.forEach {
            map[it] = map.getOrDefault(it,0)+1
        }
        nums2.forEach {
            map2[it] = map2.getOrDefault(it,0)+1
        }

        val list = mutableListOf<Int>()

        map.keys.forEach{

            if(map2.containsKey(it)){
                var min  = Math.min(map[it]!!,map2[it]!!)
                while(min-->0){
                    list.add(it)
                }
            }

        }
        return list.toIntArray()

    }
}