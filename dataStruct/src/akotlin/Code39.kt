package akotlin

import java.util.*

fun main() {

    val ints = intArrayOf(2,3,6,7)
    val code =Code39()
    code.combinationSum(ints,7).forEach {
        println()
        it.forEach { i ->
            print("$i ")
        }
    }
}
class Code39 {
    fun combinationSum(candidates: IntArray, target: Int): List<List<Int>> {
       val result =  mutableListOf<List<Int>>()
        combinationSum(candidates,target,0, LinkedList<Int>(), result)
        return  result
    }
    fun combinationSum(candidates: IntArray, target: Int,start:Int,list:LinkedList<Int>,result:MutableList<List<Int>>) {
        if(target == 0 ){
            result.add(arrayListOf<Int>().let {
                it.addAll(list)
                it })
            return
        }
        candidates.forEachIndexed{index,i ->
            if(index>=start){
                if(i<=target) {
                    list.addLast(i)
                    combinationSum(candidates, target - i, index , list, result)
                    list.removeLast()
                }
            }
        }
    }
}