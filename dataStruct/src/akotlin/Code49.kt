package akotlin

import java.util.*

fun main() {
//    val stringArr =  arrayOf("eat", "tea", "tan", "ate", "nat", "bat")
    val stringArr =  arrayOf("ac", "c")
    Code49().groupAnagrams(stringArr).forEach {
        println()
        it.forEach {
            print("$it ")
        }
    }
}
class Code49 {
    fun groupAnagrams(strs: Array<String>): List<List<String>> {

        val result = mutableListOf<MutableList<String>>()
        strs.forEach { str->
            var hasAnagrams = false
            for(resultItem in result){
//                println("resultItem.first()  ${resultItem.first()},str $str ,  ${isLegal(resultItem.first(),str)}")
                if(isLegal(resultItem.first(),str)){
                    resultItem.add(str)
                    hasAnagrams = true
                    break
                }
            }
            if(!hasAnagrams) result.add(mutableListOf<String>().let { it.add(str)
                it
            })


        }
        return result

    }


    fun isLegal(s1:String,s2:String):Boolean{

        val array = IntArray(26)
        s1.forEach {
            array[it-'a']++
        }

        s2.forEach {
            array[it-'a']--
            if(array[it-'a']<0) {
                return false
            }
        }
        array.forEach {
            if(it>0) return false
        }

        return true
    }
}