package akotlin

import java.util.*
import kotlin.collections.ArrayList

fun main() {
//    val ints  = intArrayOf(10,1,2,7,6,1,5)
    val ints  = intArrayOf(14,6,25,9,30,20,33,34,28,30,16,12,31,9,9,12,34,16,25,32,8,7,30,12,33,20,21,29,24,17,27,34,11,17,30,6,32,21,27,17,16,8,24,12,12,28,11,33,10,32,22,13,34,18,12)


    Code40().let {
        it.combinationSum2(ints,27)
    }.forEach {
        println()
        it.forEach {
            print("$it ")
        }
    }
}
class Code40 {
    fun combinationSum2(candidates: IntArray, target: Int): List<List<Int>> {
        candidates.sort()
//        candidates.forEach {
//            print("$it ")
//        }
//        println()
        val result = LinkedList<List<Int>>()
        combinationSum2(candidates,target,-1, LinkedList(),result)
        return result
    }
    fun combinationSum2(candidates: IntArray, target: Int, start:Int, list: LinkedList<Int>, result:LinkedList<List<Int>>){
        if(target == 0 ) {
            result.add(mutableListOf<Int>().let {
                it.addAll(list)
                it })
            return
        }
        candidates.forEachIndexed { index, i ->
            if(index>start){
                println( " $index  $i  $start $target")
                if(i>target) return@forEachIndexed
                if(index > start+1){
                    if(candidates[index] == candidates[index-1]){
                        return@forEachIndexed
                    }
                }
                list.addLast(i)
                combinationSum2(candidates, target - i, index, list, result)
                list.removeLast()
            }
        }
    }
}