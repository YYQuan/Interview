package akotlin

fun main() {
    Code242().isAnagram( "anagram", "nagaram").let {
        println(it)
    }
}
class Code242 {
    fun isAnagram(s: String, t: String): Boolean {
        if(s.length!=t.length) return false
        val map = HashMap<Char,Int>()

        s.forEach {
            map[it] = map.getOrDefault(it,0)+1
        }

        t.forEach {
            map[it] = map.getOrDefault(it,0)-1
            if(map[it]!! <0) return false
        }
        return true
    }
}