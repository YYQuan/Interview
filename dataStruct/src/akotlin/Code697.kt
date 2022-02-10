package akotlin

fun main() {

    Code697().findShortestSubArray(intArrayOf(1,2,2,3,1)).let {
//    Code697().findShortestSubArray(intArrayOf(1,2,2,3,1,4,2)).let {
        println(it)
    }
}
class Code697 {
    fun findShortestSubArray(nums: IntArray): Int {
        if(nums.size<2) return nums.size
        val map = HashMap<Int,Int>()
        for ( i in  0..nums.lastIndex){
            map[nums[i]] = map.getOrDefault(nums[i],0)+1
        }
        var max = Int.MIN_VALUE
        var maxList = mutableListOf<Int>()
        map.values.forEach {
            max = Math.max(it,max)
        }
        map.forEach { t, u ->

            if(max<u){
                maxList.clear()
                maxList.add(t)
            }else if(max ==u){
                maxList.add(t)
            }else{

            }

        }
        val mapIndex = HashMap<Int,IntArray>()
        nums.forEachIndexed { index, i ->
            if(maxList.contains(i)){

                val ints = mapIndex.getOrDefault(i, intArrayOf(-1,0))
                if(ints[0] == -1) mapIndex[i] = intArrayOf(index, 0)
                else {
                    mapIndex[i] = intArrayOf( ints[0], index )
                }
            }
        }
        var result   = Int.MAX_VALUE

        mapIndex.values.forEach {

            if(it[1] == 0) return 1

            result = Math.min(result, Math.abs(it[1]-it[0]))

        }



        return result+1

    }


}