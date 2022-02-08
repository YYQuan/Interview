package akotlin

fun main() {
//    "badc"
//    "baba"
    Code205().isIsomorphic("badc","baba").let {
        println(it)
    }
}
class Code205 {
    fun isIsomorphic(s: String, t: String): Boolean {

        if(s.length != t.length) return false

        val map = HashMap<Char,Char>()

        s.forEachIndexed { index, c ->

            if(map.keys.contains(c)){
                val tmp = map[c]
                if(t[index]!=tmp) return false
            }else{
                map[c] = t[index]
            }

        }

        map.clear()
        t.forEachIndexed { index, c ->
            if(map.keys.contains(c)){
                val tmp = map[c]
                if(s[index]!=tmp) return false
            }else{
                map[c] = s[index]
            }
        }
        return true

    }
}