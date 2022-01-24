package akotlin

import java.util.*

fun main() {
    Code77().combine(4,2).let {

        it.forEach {
            println()
            it.forEach {
                print("$it ")
            }

        }
    }
}
class Code77 {

    fun combine(n: Int, k: Int): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        combine(n,1,k, LinkedList(),result)
        return result
    }
    fun combine(n: Int,start:Int, k: Int,list:LinkedList<Int>,result: MutableList<List<Int>>) {
        if(list.size == k ) {
            result.add(mutableListOf<Int>().let {
                it.addAll(list)
                it })
            return
        }
        for(i in start ..n){
            list.addLast(i)
            combine(n,i+1,k,list,result)
            list.removeLast()
        }
    }
}