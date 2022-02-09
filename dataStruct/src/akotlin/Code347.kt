package akotlin

import java.util.*

fun main() {
//    Code347().topKFrequent(intArrayOf(1,1,1,1,2,2,3),2).let {
    Code347().topKFrequent(intArrayOf(1,2),2).let {
        it.forEach {
            print("$it ")
        }
        println()
    }
}
class Code347 {
    fun topKFrequent(nums: IntArray, k: Int): IntArray {


//        val set = TreeSet<Int>()
        val map = TreeMap<Int,Int>()

        nums.forEach {
            map[it] = map.getOrDefault(it,0)+1
        }

        val queue = PriorityQueue<Pair>()
        map.forEach { t, u ->
            queue.add(Pair(t,u))
        }
        val ints = IntArray(k)
        for(i in ints.indices){
            ints[i] = queue.poll().key
        }
        return ints
    }

    class Pair(val key :Int, val value :Int) :Comparable<Pair>{


        override fun compareTo(other: Pair): Int {
            return  other.value -value
        }

    }
}