package akotlin

fun main() {
    Code167().twoSum(intArrayOf(2,7,11,15),9).let {
        it.forEach {
            print(" $it , ")
        }
        println()
    }
}
class Code167 {
    fun twoSum(numbers: IntArray, target: Int): IntArray {


        for(i in numbers.indices){
            for (j in i+1 until numbers.size){
                if(numbers[i]+numbers[j]  == target)return intArrayOf(i+1,j+1)
            }
        }
        throw IllegalStateException()
    }
}