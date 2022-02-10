package akotlin

import java.util.*

fun main() {
    Code451().frequencySort("tree").let {
//    Code451().frequencySort("tr").let {
        println(it)
    }
}
class Code451 {
    //注意 treeset 中compare相同的元素只会保存一个，所以key也要加入判断
    fun frequencySort(s: String): String {

        val set = TreeSet<Pair>()
        val map = HashMap<Char,Int>()

        s.forEach {
//            print("char :$it ")
            map[it] = map.getOrDefault(it,0)+1
//            println("map $it  ${map[it]}")
        }
//        map.forEach { t, u ->
//            println( "t $t  u $u")
//        }
//        println()
        map.forEach { t, u ->
//            println( "t $t  u $u")
            set.add(Pair(t,u))
        }
//        println("set.size ${set.size}")
        val strBuilder  = StringBuilder()

        set.forEach {
//            print("${it.key} ")
            var count = it.v
            while(count-->0){
                strBuilder.append(it.key)
            }
        }
        println()
        return strBuilder.toString()
    }

    class Pair(val key:Char, val v:Int) :Comparable<Pair>{
        override fun compareTo(other: Pair): Int {
           return if(v != other.v) other.v-v else key-other.key
        }

    }

}