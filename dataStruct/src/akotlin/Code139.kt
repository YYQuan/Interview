package akotlin

fun main() {
//    "catsandog"
//    ["cats","dog","sand","and","cat"]
//    Code139().wordBreak("catsandog", mutableListOf<String>().let {
    Code139().wordBreak("leetcode", mutableListOf<String>().let {

        it.add("leet")
        it.add("code")

//        it.add("cats")
//        it.add("dog")
//        it.add("sand")
//        it.add("and")
//        it.add("cat")
        it
    }).let {
        println(it)
    }



    Code139().wordBreak("abcd", mutableListOf<String>().let {

        it.add("a")
        it.add("abc")
        it.add("b")
        it.add("cd")

        it
    }).let {
        println(it)
    }
}
class Code139 {
    fun wordBreak(s: String, wordDict: List<String>): Boolean {
        val dp = BooleanArray(s.length)

        s.forEachIndexed { index, c ->
            wordDict.forEach {
//                println("wordDict.forEach $it")
                val length = it.length-1
                if(length>index) return@forEach
                else if(length==index){
                    if(!dp[index]) {
                        val string = s.substring(0, index+1)
//                        println("length==index  $index  $string ${string == it}")
                        dp[index] = string == it
                    }
                }else{
//                    println("length>index  index: $index  ${index-length} ")
//                    println("length>index  $c  ${dp[index]}  ${dp[index-length-1]} ")
                    if(!dp[index]&&dp[index-length-1]) {
                        val string = s.substring(index-length, index+1)
//                        println("length>index  $index $it $string ${string == it}")
                        dp[index] = string == it
                    }
                }
            }
        }
//        dp.forEachIndexed { index, b ->
//            println("index $index   $b  ")
//        }
//        println()
        return dp[s.length-1]
    }

}