package akotlin

import java.util.*

fun main() {
    Code131().partition("aab").forEach {
        println(it)
    }
}
class Code131 {
    fun partition(s: String): List<List<String>> {
        val result = mutableListOf<List<String>>()
        partition(s,0,LinkedList<String>(),result)
        return result
    }
    fun partition(s: String,start:Int, list: LinkedList<String>,result: MutableList<List<String>>) {

        if (start > s.length) return
        if (start == s.length) {
            result.add(mutableListOf<String>().let {
                it.addAll(list)
                it
            } )
        }

        for (i in start + 1..s.length) {
            val string = s.substring(start, i)
//            println(" string : $string ")
            if(isLegal(string)) {
                list.addLast(string)
                partition(s, i, list, result)
                list.removeLast()
            }
        }
    }
    fun isLegal(str:String) :Boolean{

        val queue :LinkedList<Char>  =LinkedList<Char>()
        str.forEach {
            queue.addLast(it)
        }
        while(queue.size>1){
            if(queue.removeLast() != queue.removeFirst()) return false
        }
        return true
    }
}