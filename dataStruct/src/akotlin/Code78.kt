package akotlin

import java.util.*

fun main() {
    val ints = intArrayOf(1,2,3)
    Code78().subsets(ints).forEach {
        println()
        it.forEach {
            print("$it ")
        }
    }
}
class Code78 {
    fun subsets(nums: IntArray): List<List<Int>> {

        val result  = mutableListOf<List<Int>>()
        result.add(listOf())
        subsets(nums,0, LinkedList(),result)
        return result
    }
    fun subsets(nums: IntArray, start:Int, list: LinkedList<Int>, result: MutableList<List<Int>>) {

        if(list.size != 0) {
            result.add(mutableListOf<Int>().let {
                it.addAll(list)
                it
            })
        }


        nums.forEachIndexed { index, i ->
            if(index>=start){
                list.addLast(i)
                subsets(nums,index+1,list,result)
                list.removeLast()
            }
        }

    }
}