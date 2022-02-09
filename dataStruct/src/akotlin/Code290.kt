package akotlin

fun main() {

    Code290().wordPattern("abba", "dog cat cat dog").let {
//    Code290().wordPattern("abba", "dog dog dog dog").let {
        println(it)
    }
}
class Code290 {
    fun wordPattern(pattern: String, s: String): Boolean {


        val array = s.split(" ")
//        array.forEach {
//            print(it)
//        }
//        println()
        if(pattern.length!= array.size) return false

        val map = HashMap<String,String>()
        pattern.forEachIndexed { index, c ->
//           println(" $index $c")
           if(map.containsKey(c+"")){
               if(map[c+""]!=array[index]) return false
           }else{
               map[c+""] = array[index]
           }
       }
        map.clear()
        array.forEachIndexed { index, str ->
            if(map.containsKey(str)){
                if(map[str]!=(pattern[index]+"")) {
//                    println("${map[str]}   ${pattern[index]+""}")
                    return false
                }
            }else{
                map[str] = pattern[index]+""
            }
        }

        return true

    }
}