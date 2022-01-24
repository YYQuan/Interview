package akotlin

import java.util.*

fun main() {
    Code120().minimumTotal(
       mutableListOf<List<Int>>().let {
           it.add(mutableListOf<Int>().let {
               it.add(2)
               it
           })

           it.add(mutableListOf<Int>().let {
               it.add(3)
               it.add(4)
               it
           })
           it.add(mutableListOf<Int>().let {
               it.add(6)
               it.add(5)
               it.add(7)
               it
           })
           it.add(mutableListOf<Int>().let {
               it.add(4)
               it.add(1)
               it.add(8)
               it.add(3)
               it
           })
           it
       }
    )
        .let { println(it) }
}
class Code120 {
    fun minimumTotal(triangle: List<List<Int>>): Int {
        val array = Array<Int>(triangle.last().size){0}

        for( i in triangle.indices){
            for(j in triangle[i].size-1 downTo  0){
//                println(" i $i  , j $j ${array.size}  ${triangle[i].size} triangle[i][j] : ${triangle[i][j]}")
                when (j) {
                    0 -> array[j] = array[j] +triangle[i][j]
                    triangle[i].lastIndex -> array[j] = array[j-1] +triangle[i][j]

                    else -> {
                        array[j] = Math.min(array[j-1],array[j])+triangle[i][j]
                    }
                }

            }
            println()
            array.forEach{
                print("$it ")
            }
            println()
        }

        var min = Int.MAX_VALUE
        array.forEach{
            min = Math.min(it,min)
        }
        return min
    }

}