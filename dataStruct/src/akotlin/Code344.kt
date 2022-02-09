package akotlin

fun main() {
//    val charArray = charArrayOf('h','e','l','l','o')
    println(11/2)
    val charArray = charArrayOf('A',' ','m','a','n',',',' ','a',' ','p','l','a','n',',',' ','a',' ','c','a','n','a','l',':',' ','P','a','n','a','m','a')
    val charArray2 = charArrayOf('a','m','a','n','a','P',' ',':','l','a','n','a','c',' ','a',' ',',','n','a','l','p',' ','a',' ',',','n','a','m',' ','A')
    Code344().reverseString(charArray)
    println(charArray.size)
    println(charArray)
    charArray.forEach {
        print(it)
    }
    println()
    charArray.forEachIndexed { index, c ->
        if(charArray2[index] != c ) {
            println("$index  ${charArray2[index]} c: $c ")
        }
    }
}
class Code344 {
    fun reverseString(s: CharArray): Unit {

        for(i in 0..(s.size)/2-1){
            val tmp = s[i]
            s[i] = s[s.size-i-1]
            s[s.size-i-1]  = tmp

        }
    }
}