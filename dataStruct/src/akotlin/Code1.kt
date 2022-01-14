package akotlin

fun main(args: Array<String>) {
    val code = Code1()
    val ints = intArrayOf(2, 7, 11, 15)

    val result: IntArray = code.twoSum(ints, 9)
    println(result[0].toString() + "  " + result[1])
}

class Code1 {

     fun twoSum(nums: IntArray, target: Int): IntArray {
        if(nums.size ==0)  throw IllegalStateException()

        val hashMap = HashMap<Int,Int>()
        nums.forEachIndexed { index, it ->
            if(hashMap.keys.contains(target-it)){
                return intArrayOf((hashMap[target-it]!!),index)
            }
            hashMap.put(it,index)
        }
        throw IllegalStateException()
    }
}
