package akotlin

fun main() {
    Code454().fourSumCount(
        intArrayOf(1,2),
        intArrayOf(-2,-1),
        intArrayOf(-1,2),
        intArrayOf(0,2)
    ).let {
        println(it)
    }
}
class Code454 {
    fun fourSumCount(nums1: IntArray, nums2: IntArray, nums3: IntArray, nums4: IntArray): Int {

        val map1 = HashMap<Int,Int>()
        val map2 = HashMap<Int,Int>()

        for(i in 0 ..nums1.lastIndex){
            for(j in 0 .. nums1.lastIndex){
                map1[nums1[i]+nums2[j]]  = map1.getOrDefault(nums1[i]+nums2[j],0)+1
            }
        }

        for(i in 0 ..nums1.lastIndex){
            for(j in 0 .. nums1.lastIndex){
                map2[nums3[i]+nums4[j]]  = map2.getOrDefault(nums3[i]+nums4[j],0)+1
            }
        }


        var result  =0

        map1.forEach { t, u ->

            if(map2.containsKey(-t)){
                result += (map2[-t]!! * u )
            }

        }
        return result



    }
}