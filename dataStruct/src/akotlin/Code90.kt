package akotlin

import java.lang.StringBuilder
import java.util.*

fun main() {
    val ints = intArrayOf(1,2,2)
    Code90().subsetsWithDup(ints).let {
        it.forEach {
            println()
            it.forEach {
                print("$it ")
            }
        }
    }
}
class Code90 {
    // 去重的核心思想是
    // 大包含小, 并且每级递归中相同的元素不再处理
    fun subsetsWithDup(nums: IntArray): List<List<Int>> {
        nums.sort()
        val result = mutableListOf<List<Int>>()
        result.add(arrayListOf())
        val list =LinkedList<Int>()
        subsetsWithDup(nums,0,list,result,0)
        return  result
    }

    fun subsetsWithDup(nums: IntArray,start:Int,list:LinkedList<Int>,result: MutableList<List<Int>>,depth:Int) {
        val str = StringBuilder().let {
            for(i in 0..depth){
                it.append("---")
            }
            it.append(">>")
            it.toString()
        }
        if(list.size !=0){
            println("$str  addList $list")

            result.add(mutableListOf<Int>().let {
                it.addAll(list)
                it
            })
        }
        nums.forEachIndexed { index, i ->
            println("$str     index : $index  start : $start")
            if(index<start) return@forEachIndexed
            println("$str  i :$i  nums[index-1]:${if(index>0) nums[index-1] else ""}  ")

            //每层递归都只选一个值,并且是不重复的,这样就避免了重复
            // 有index > = start +1 是因为 允许和上一级的递归重复
            if(index>=start+1&&  i == nums[index-1]) return@forEachIndexed
            println("$str addLast  $i $index $start")
            list.addLast(i)
            subsetsWithDup(nums,index+1,list,result,depth+1)
            list.removeLast()
        }
    }
}