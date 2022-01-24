package akotlin

fun main() {
    "ABC".toLowerCase().let {
        println(it)
    }
    Code125().isPalindrome("A man, a plan, a canal: Panama").let {
//    Code125().isPalindrome("race a car").let {
        println(it)
    }
}
class Code125 {
    fun isPalindrome(s: String): Boolean {
        val str  = s.toLowerCase()
        var l =0
        var r = str.length-1

        while(l<r){
            while(!isLegalChar(str[l])){
                if(l>r){
                    return  !isLegalChar(str[r])
                }else if(l == r) return true
                else l++
            }

            while(!isLegalChar(str[r])){
                if(l>r){
                    return  !isLegalChar(str[r])
                }else if(l == r ) return  true
                else r--
            }

            var c1 = str[l++]
            var c2 = str[r--]
//            println("c1 :$c1 ${c1.lowercase()}   c2 :$c2  ${c2.lowercase()}")
//            println("c1 :$c1 ${c1.isLowerCase()}   c2 :$c2  ${c2.isLowerCase()}")
            if(c1 != c2) return false

        }
        return true
    }

    fun isLegalChar(c:Char):Boolean{
        if(c in 'a'..'z' ) return  true
        if(c in 'A'..'Z') return  true
        if(c in '0'..'9') return  true
        return false
    }
}