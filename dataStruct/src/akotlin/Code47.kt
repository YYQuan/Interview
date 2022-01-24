package akotlin

import java.lang.StringBuilder
import java.util.*

fun main() {
//    val ints = intArrayOf(1,1,2)
    val ints = intArrayOf(2,1,1,2)
//    val ints = intArrayOf(1,1)
    Code47().permuteUnique(ints).let {
        it.forEach {
            println()
            it.forEach {
                print("$it " )
            }
        }
    }
}
// 去重思路:
// 把已经使用的元素排除
// 每轮循环时 相同大小的元素只处理一次
// 注意要用 addLast 来控制顺序
class Code47 {
    fun permuteUnique(nums: IntArray): List<List<Int>> {
        nums.sort()
        val result = mutableListOf<List<Int>>()
        val list = LinkedList<Int>()
        val numsList = nums.let {
            val list = LinkedList<Int>()
            it.forEach {
                list.add(it)
            }
            list
        }
        permuteUnique(numsList, list,result)
        return result
    }

    fun permuteUnique(  nums: LinkedList<Int>, list: LinkedList<Int>, result:MutableList<List<Int>>) {


        if(nums.size == 0){
            result.add(mutableListOf<Int>().let {
                it.addAll(list)
                it
            })
            return
        }


        for(i in 0 until nums.size){
            val tmp = nums[i]
            if(i>0&&nums[i]==nums[i-1]) continue
            val tmpList = LinkedList<Int>().let {
                nums.forEachIndexed { index, value ->
                    if(index!=i) it.add(value)
                }
                it
            }
            // 注意要用 addLast 来控制顺序
            list.addLast(tmp)
            permuteUnique(tmpList,list,result)
            list.removeLast()

        }
    }
}