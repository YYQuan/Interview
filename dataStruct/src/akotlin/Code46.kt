package akotlin

import java.util.*

fun main() {
    val ints = intArrayOf(1,2,3)
    Code46().permute(ints).forEach {
        println()
        it.forEach {
            print("$it ")
        }
    }
}
class Code46 {
    fun permute(nums: IntArray): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        permute(nums, LinkedList(),result)
        return result
    }
    fun permute(nums: IntArray, list: LinkedList<Int>, result: MutableList<List<Int>>) {

        if(list.size == nums.size){
            result.add(mutableListOf<Int>().let {
                it.addAll(list)
                it
            })
            return
        }

        nums.forEach {
            if(!list.contains(it)){
                list.addLast(it)
                permute(nums,list,result)
                list.removeLast()
            }
        }

    }
}