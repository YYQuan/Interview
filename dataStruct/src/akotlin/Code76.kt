package akotlin

fun main() {
//    Code76().minWindow("ADOBECODEBANC", t = "ABC").let {
    Code76().minWindow("a", t = "a").let {
        println(it)
    }
}

class Code76 {
    fun minWindow(s: String, t: String): String {
        val array = Array(52){0 as Int}
        t.forEach {
            if(it in 'a'..'z'){
                array[it - 'a']++
            }else{
                array[it -'A'+26]++
            }
        }
        return  minWindow(s,0,array)
    }
    fun minWindow(s: String,start:Int , array: Array<Int>): String {

        var l = start
        var r = l

        var result :String? = null
        val com = Array(52){0}
        while(r<s.length){
            while(!isLegal(com,array)) {
                if(r>= s.length) return result?:""
                val c = s[r]
                if(c in 'a'..'z'){
                    com[c - 'a']++
                }else{
                    com[c -'A'+26]++
                }
                r++
            }
//            println( "l: $l    r:$r  s.subs ${s.substring(l,Math.min(r+1,s.length))}")
//            if(r>=s.length) break
            while(isLegal(com,array)){
                if(l>=r) {
                    break
                }
                val c = s[l]
                if(c in 'a'..'z'){
                    com[c - 'a']--
                }else{
                    com[c -'A'+26]--
                }
                l++

            }
//            println( "l: $l    r:$r  s.subs ${s.substring(l,Math.min(r+1,s.length))}")
            if(result == null) result = s.substring(l-1,r)
            else if(result.length> r -l ) result = s.substring(l-1,r)
        }
        return result ?: ""
    }

    fun isLegal(com:Array<Int> ,base:Array<Int>):Boolean{
        com.forEachIndexed { index, c ->
            if(c< base[index]) {
//                println( "index $index   c: $c    base[index]:${base[index]}")
                return false
            }
        }
//        println( "islegal true")
        return true
    }
}