package akotlin

import kotlin.math.max

fun main() {
    var code = Code3()
    //        int[] result = code.solution(ints,9);
//    val s = "au"
//    val s = "abcabcbb"
//    val s ="tmmzuxt"
    val s ="bbtablud"
    println(" ${s.substring(0,2)}")
    val result: Int = code.lengthOfLongestSubstring(s)
    println(result)
}
class Code3 {
    fun lengthOfLongestSubstring(s: String): Int {
        Util.notNull(s){}?:return 0
        if(s.isEmpty()) return 0
        var l  = 0
        var  max = Int.MIN_VALUE
        var map = HashMap<Char,Int>()

        s.forEachIndexed { index, c ->
            if(!map.containsKey(c)){
                map[c] = index
                max = max(max,index-l+1)
            }else{
                l  = map[c]!!+1;
                map[c] =index
                map.clear()
                s.substring(l,index+1).forEachIndexed() {index, c ->
                    map[c] =index+l
                }
            }
        }
        return max
    }
}