package akotlin

fun main() {

    val code = Code17()
    val result  = code.letterCombinations("23")
    result.forEach {
        println(it)
    }
}
class Code17 {

    fun letterCombinations(digits: String): List<String> {
        if(digits.isEmpty()) return ArrayList()

        val  result = mutableListOf<String>()

        digits.forEachIndexed { index,c ->
            if(index == 0 ){
                val chars = getChars(c)
                chars.forEach {
                    c2 -> result.add("" + c2)
                }

            }else {
                val chars = getChars(c)
                val tmpList = arrayListOf<String>().let {
                    it.addAll(result)
                    it
                }
                result.clear()
                chars.forEach { c2 ->
                    tmpList.forEach { s ->
                        result.add(s + c2)
                    }
                }
            }
        }
        return result
    }

    fun getChars(s:Char):CharArray{

        return when(s){
            '2' ->{ charArrayOf('a','b','c') }
            '3' ->{charArrayOf('d','e','f') }
            '4' ->{charArrayOf('g','h','i') }
            '5' ->{charArrayOf('j','k','l') }
            '6' ->{charArrayOf('m','n','o') }
            '7' ->{charArrayOf('p','q','r','s') }
            '8' ->{charArrayOf('t','u','v') }
            '9' ->{charArrayOf('w','x','y','z') }
            else -> {charArrayOf() }
        }

    }
}