package akotlin

import java.lang.StringBuilder

fun main() {

    Code345().reverseVowels("hello").let {
        println(it)
    }
}
class Code345 {
    fun reverseVowels(s: String): String {

        var l = 0
        var r = s.length-1
        val array = s.toCharArray()
        while(l<r){

            while(!isLegal(s[l])){
                l++
                if(l>=r) {
                    val builder = StringBuilder()
                    array.forEach { builder.append(it) }
                    return builder.toString()
                }
            }

            while(!isLegal(s[r])){
                r--
                if(l>=r)  {
                    val builder = StringBuilder()
                    array.forEach { builder.append(it) }
                    return builder.toString()
                }
            }

            val t = array[l]
            array[l]  = array[r]
            array[r] =t
            l++
            r--

        }
        val builder = StringBuilder()
        array.forEach { builder.append(it) }
        return builder.toString()

    }

    fun isLegal(c:Char):Boolean{
        return when(c){
            'a'-> true
            'e'->true
            'i'->true
            'o'->true
            'u'->true
            'A'->true
            'E'->true
            'I'->true
            'O'->true
            'U'->true
            else ->false
        }

    }
}