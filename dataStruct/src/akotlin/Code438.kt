package akotlin

fun main() {
//    Code438().findAnagrams( "cbaebabacd","abc").let {
    Code438().findAnagrams( "abab",  "ab").let {
        it.forEach {
            print("$it ")
        }
    }
}
class Code438 {
    fun findAnagrams(s: String, p: String): List<Int> {
        if(s.isEmpty()||p.isEmpty()) return listOf()
        if(s.length<p.length) return listOf()
        val result  = mutableListOf<Int>()
        val pChars = IntArray(26)
        val chars = IntArray(26)

        p.forEach {
            pChars[it-'a']++
        }

        val l = p.length

        s.substring(0,l).forEach {
            chars[it-'a']++
        }
        if(isLegal(chars, pChars)) result.add(0)
        for(i in l until s.length){
//            println("---->> i $i")
//            chars.forEach {
//                print("$it ")
//            }
//            println()
            chars[   s[i-l]-'a']--
            chars[   s[i]-'a']++
            if(isLegal(chars, pChars)) result.add(i-l+1)
        }
        return result
    }
    fun isLegal( cs :IntArray ,cs2 :IntArray):Boolean {

        for(i in 0..25){
            if(cs[i] !=cs2[i]) return false
        }

        return true
    }
}