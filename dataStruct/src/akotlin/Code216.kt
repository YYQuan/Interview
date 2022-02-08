package akotlin

import java.util.*

fun main() {
    Code216().combinationSum3(3,9).forEach {
        it.forEach {
            print("$it ")
        }
        println()
    }
}
class Code216 {
    fun combinationSum3(k: Int, n: Int): List<List<Int>> {

        val result = mutableListOf<List<Int>>()
        combinationSum3(k,n,1,LinkedList<Int>(),result)
        return result
    }
    fun combinationSum3(k: Int, n: Int, start:Int, list: LinkedList<Int>, result: MutableList<List<Int>>) {
        if(k<0 ) return
        if(k ==0 &&n == 0){
            result.add(mutableListOf<Int>().let {
                it.addAll(list)
                it
            })
            return
        }


        for(i in start..9){
            list.addLast(i)
            combinationSum3(k-1,n-i,i+1,list,result)
            list.removeLast()
        }

    }
}